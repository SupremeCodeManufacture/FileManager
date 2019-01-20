package view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.SupremeManufacture.filemanager.R;
import com.soloviof.easyads.InterstitialAddsHelper;

import java.io.File;
import java.util.List;

import data.App;
import data.GenericConstants;
import logic.helpers.FileUtils;
import logic.listener.OnItmClickListener;
import logic.listener.OnRestartFilesSelectionsListener;
import view.activity.HomeActivity;

public abstract class BaseFrg extends Fragment implements
        OnItmClickListener,
        OnRestartFilesSelectionsListener {


    @Override
    public void onItemClicked(File file) {
        openFile(file);
    }

    @Override
    public void onCutomCatSelected(String patternRegex, String title) {
        ((HomeActivity) getActivity()).openListByLastUsed(patternRegex, title);
    }

    @Override
    public void onCutPasteListChanged(List<String> list) {
        if (getActivity() != null) {
            HomeActivity instance = getActivity() != null ? (HomeActivity) getActivity() : null;
            if (instance != null) instance.manageMenuBtns(list);
        }
    }

    @Override
    public void onRestartSelections() {
        //implemented foreach frg with selectable files
    }


    private void openFile(File file) {
        if (file.isDirectory()) {
            ((HomeActivity) getActivity()).openListByFolder(file.getAbsolutePath(), false);

        } else {
            FileUtils.openFile(getActivity(), file);
        }
    }

    protected void showNoItems(int state, RecyclerView mRvItems, RelativeLayout mRlLoading, RelativeLayout mLlNoItms) {
        switch (state) {
            case GenericConstants.STATE_LOADING:
                mRvItems.setVisibility(View.GONE);
                mLlNoItms.setVisibility(View.GONE);
                mRlLoading.setVisibility(View.VISIBLE);
                break;

            case GenericConstants.STATE_OK:
                mRvItems.setVisibility(View.VISIBLE);
                mLlNoItms.setVisibility(View.GONE);
                mRlLoading.setVisibility(View.GONE);
                break;

            case GenericConstants.STATE_EMPTY:
                mRvItems.setVisibility(View.GONE);
                mLlNoItms.setVisibility(View.VISIBLE);
                mRlLoading.setVisibility(View.GONE);
                break;
        }
    }

}