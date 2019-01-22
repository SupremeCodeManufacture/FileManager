package view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;

import com.SupremeManufacture.filemanager.R;

import java.io.IOException;
import java.io.OutputStream;

import data.App;
import data.GenericConstants;
import logic.helpers.CopyMoveUtils;
import logic.helpers.FileUtils;
import logic.helpers.MyLogs;
import logic.helpers.PermissionsHelper;
import logic.helpers.ThemeColorsHelper;
import view.fragment.FrgListPathSelection;
import view.fragment.FrgMemorySelector;

public class PathSelectionActivity extends AppCompatActivity {

    private static final int CODE_ACCESS = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(ThemeColorsHelper.getTheme(), true);
        setContentView(R.layout.content_home);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(App.getAppCtx().getResources().getString(R.string.by_folder_select));

        if (PermissionsHelper.hasPermissions(this, PermissionsHelper.PERMISSIONS_STORAGE)) {
            decideMemoryPaths();
        }
    }

    public void decideMemoryPaths() {
        String sdCardPath = FileUtils.getSdCardPath();

        if (sdCardPath != null) {
            FrgMemorySelector frgMemorySelector = new FrgMemorySelector();
            Bundle args = new Bundle();
            args.putInt(GenericConstants.EXTRA_ARG_VIEW_TYPE, GenericConstants.KEY_SELECTED_FOR_COPY_MOVE);
            args.putString(GenericConstants.EXTRA_ARG_SD_CARD_PATH, sdCardPath);
            frgMemorySelector.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.frg_container, frgMemorySelector, "FrgMemorySelector")
                    .commit();

        } else {
            openPasteListByFolder(App.getSelectedMemoryCopyMovePath(), true);
        }
    }

    public void openPasteListByFolder(String path, boolean isFirstFrg) {
        FrgListPathSelection instance = new FrgListPathSelection();

        Bundle args = new Bundle();
        args.putString(GenericConstants.EXTRA_ARG_PATH, path);
        instance.setArguments(args);

        if (isFirstFrg) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frg_container, instance)
                    .commit();

        } else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frg_container, instance)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void openPasteByFolderSD() {
        startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE), CODE_ACCESS);

    }


    //called from frg
    public void returnPathToPaste(String path) {
        Intent resultIntent = new Intent();

        if (path != null) {
            resultIntent.putExtra(GenericConstants.EXTRA_ARG_PATH_TO, path);
            setResult(Activity.RESULT_OK, resultIntent);

        } else {
            setResult(Activity.RESULT_CANCELED, resultIntent);
        }

        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == CODE_ACCESS) {
            if (resultCode == RESULT_OK) {
                Uri treeUri = resultData.getData();
                DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);
                grantUriPermission(getPackageName(), treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

//                writeFile(pickedDir);

                //MyLogs.LOG("PathSelectionActivity", "onActivityResult", "treeUri: " + treeUri.getPath());
            }
        }
    }


    public void writeFile(DocumentFile pickedDir) {
        try {
            DocumentFile file = pickedDir.createFile("image/jpeg", "try2.jpg");
            OutputStream out = getContentResolver().openOutputStream(file.getUri());
            try {

                //pathFrom: /storage/emulated/0/DCIM/
                // fileName: IMG-840de8f27db1a7ba67ff67cda23e1786-V.jpg
                // pathTo: /storage/9016-4EF8/IMG-840de8f27db1a7ba67ff67cda23e1786-V.jpg

                CopyMoveUtils.copyFile("/storage/emulated/0/DCIM/", "IMG-840de8f27db1a7ba67ff67cda23e1786-V.jpg","/storage/9016-4EF8/" );

                // write the image content

            } finally {
                if (out != null) out.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}