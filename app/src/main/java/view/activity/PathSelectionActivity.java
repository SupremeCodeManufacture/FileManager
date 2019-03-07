package view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.SupremeManufacture.filemanager.R;

import data.App;
import data.GenericConstants;
import logic.helpers.FileUtils;
import logic.helpers.PermissionsHelper;
import logic.helpers.ThemeColorsHelper;
import view.fragment.FrgListPathSelection;
import view.fragment.FrgMemorySelector;

public class PathSelectionActivity extends AppCompatActivity {

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

    /**
     * decide to select path for copy or move actions from local or SD
     */
    public void decideMemoryPaths() {
        String sdCardPath = FileUtils.getSdCardPath();

        //open frg to select memory SD or local then from there just call 'openPasteListByFolder' OR  'returnPathToPaste()'
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
            openPasteListByFolder(GenericConstants.EXTRA_ALL_INTERNAL_FILES_PATH, false);
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
}