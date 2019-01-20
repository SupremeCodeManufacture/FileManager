package view.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.SupremeManufacture.filemanager.R;

import java.io.File;

import data.App;
import data.ThemeObj;
import logic.adapter.ThemesAdapter;
import logic.helpers.FileUtils;
import logic.helpers.Utils;
import logic.listener.OnThemeSelectedListener;


public class Dialogs {

    public static void showSimpleDialog(final Context context, String title, String msg, boolean hasCancel, String customOkTxt, String customCancelTxt, final SimpleDialogPopUpListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);

        String okTxt = App.getAppCtx().getResources().getString(android.R.string.yes);
        if (customOkTxt != null)
            okTxt = customOkTxt;

        builder.setPositiveButton(okTxt,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        if (listener != null)
                            listener.onConfirmButtonClick();
                    }
                });

        if (hasCancel) {
            String negativeText = context.getResources().getString(android.R.string.cancel);
            if (customCancelTxt != null)
                negativeText = customCancelTxt;

            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (listener != null)
                                listener.onCancelButtonClick();
                        }
                    });
        }

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    public static void showShareToUserDialog(final Activity context, File file) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_details);

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        tvTitle.setText(file.getName());

        TextView tvPath = (TextView) dialog.findViewById(R.id.tv_path);
        tvPath.setText(file.getPath());

        TextView tvSize = (TextView) dialog.findViewById(R.id.tv_size);
        tvSize.setText(FileUtils.getReadableFileSize(file.length()));

        TextView tvModif = (TextView) dialog.findViewById(R.id.tv_modif);
        tvModif.setText(Utils.getPrettyDate(file.lastModified()));

        TextView tvRead = (TextView) dialog.findViewById(R.id.tv_write);
        tvRead.setText(file.canRead() ? App.getAppCtx().getResources().getString(android.R.string.yes) : App.getAppCtx().getResources().getString(android.R.string.no));

        TextView tvWrite = (TextView) dialog.findViewById(R.id.tv_read);
        tvWrite.setText(file.canWrite() ? App.getAppCtx().getResources().getString(android.R.string.yes) : App.getAppCtx().getResources().getString(android.R.string.no));

        TextView tvHidden = (TextView) dialog.findViewById(R.id.tv_hidden);
        tvHidden.setText(file.isHidden() ? App.getAppCtx().getResources().getString(android.R.string.yes) : App.getAppCtx().getResources().getString(android.R.string.no));

        Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public static void showOptionalInputTxtDialog(Activity activityCtx, String fileName, final OnTextInsertedListener onTextInsertedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityCtx);
        builder.setTitle(App.getAppCtx().getResources().getString(R.string.txt_rename));
        builder.setPositiveButton(App.getAppCtx().getResources().getString(android.R.string.ok), null);
        builder.setNegativeButton(App.getAppCtx().getResources().getString(android.R.string.cancel), null);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        View dialogView = activityCtx.getLayoutInflater().inflate(R.layout.dialog_optional_text, null);
        float dpi = activityCtx.getResources().getDisplayMetrics().density;
        dialog.setView(dialogView, (int) (19 * dpi), (int) (15 * dpi), (int) (14 * dpi), (int) (15 * dpi));

        final EditText etInput = (EditText) dialogView.findViewById(R.id.et_input);
        etInput.setText(fileName);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button pozitiveBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                pozitiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onTextInsertedListener.onTxtInserted(etInput.getText().toString());
                        dialog.dismiss();
                    }
                });

                Button negativeBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }


    public static void showThemePikerDialog(Activity context, final OnThemeSelectedListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_theme_piker);

        RecyclerView rvItms = (RecyclerView) dialog.findViewById(R.id.itms);
        rvItms.setAdapter(new ThemesAdapter(new OnThemeSelectedListener() {
            @Override
            public void onThemeSelected(ThemeObj themeObj) {
                dialog.dismiss();
                listener.onThemeSelected(themeObj);
            }
        }));

        rvItms.setLayoutManager(new WrapLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvItms.setHasFixedSize(true);

        dialog.show();
    }
}
