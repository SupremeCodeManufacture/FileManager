package view.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.widget.ImageViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.SupremeManufacture.filemanager.R;

import data.App;
import data.GenericConstants;
import logic.helpers.MyLogs;
import logic.helpers.ThemeColorsHelper;
import view.activity.HomeActivity;
import view.activity.PathSelectionActivity;

public class FrgMemorySelector extends BaseFrg implements View.OnClickListener {

    private int viewTypeSelection;
    private String sdardPath;
    private boolean isSelectedForCopyMove;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_memory_selecting, container, false);
        initViews(view);

        if (getArguments() != null) {
            viewTypeSelection = getArguments().getInt(GenericConstants.EXTRA_ARG_VIEW_TYPE);
            sdardPath = getArguments().getString(GenericConstants.EXTRA_ARG_SD_CARD_PATH);
            isSelectedForCopyMove = viewTypeSelection == GenericConstants.KEY_SELECTED_FOR_COPY_MOVE;

            MyLogs.LOG("FrgMemorySelector", "onCreateView", "viewTypeSelection: " + viewTypeSelection + " sdardPath: " + sdardPath + " isSelectedForCopyMove: " + isSelectedForCopyMove);
        }

        return view;
    }

    private void initViews(View view) {
        RelativeLayout zoneInternal = (RelativeLayout) view.findViewById(R.id.itm_internal_memory);
        zoneInternal.setOnClickListener(this);

        RelativeLayout zoneExternal = (RelativeLayout) view.findViewById(R.id.itm_external_memory);
        zoneExternal.setOnClickListener(this);

        ImageView ivInternal = (ImageView) view.findViewById(R.id.iv_0012);
        ImageViewCompat.setImageTintList(ivInternal, ColorStateList.valueOf(App.getAppCtx().getResources().getColor(ThemeColorsHelper.getColorPrimary50())));

        ImageView ivExternal = (ImageView) view.findViewById(R.id.iv_0013);
        ImageViewCompat.setImageTintList(ivExternal, ColorStateList.valueOf(App.getAppCtx().getResources().getColor(ThemeColorsHelper.getColorPrimary50())));
    }

    private void goToNextScreen() {
        switch (viewTypeSelection) {
            case GenericConstants.KEY_SELECTED_LAST_USED:
                ((HomeActivity) getActivity()).openListByLastUsed();
                break;

            case GenericConstants.KEY_SELECTED_BY_PATH:
                ((HomeActivity) getActivity()).openListByFolder(App.getSelectedMemoryDefPath(), false);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.itm_internal_memory:
                if (isSelectedForCopyMove) {
                    ((PathSelectionActivity) getActivity()).openPasteListByFolder(GenericConstants.EXTRA_ALL_INTERNAL_FILES_PATH, false);

                } else {
                    App.setSelectedMemoryDefPath(GenericConstants.EXTRA_ALL_INTERNAL_FILES_PATH);
                    goToNextScreen();
                }
                break;

            case R.id.itm_external_memory:
                if (isSelectedForCopyMove) {
                    ((PathSelectionActivity) getActivity()).openPasteListByFolder(sdardPath, false);
//                    ((PathSelectionActivity) getActivity()).openPasteByFolderSD();

                } else {
                    App.setSelectedMemoryDefPath(sdardPath);
                    goToNextScreen();
                }
                break;
        }
    }
}