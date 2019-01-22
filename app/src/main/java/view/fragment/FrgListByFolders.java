package view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.SupremeManufacture.filemanager.R;
import com.google.android.gms.ads.AdSize;
import com.soloviof.easyads.AdsRepo;
import com.soloviof.easyads.CustomizeAds;

import java.util.List;

import data.App;
import data.GenericConstants;
import data.SimpleFileWrapper;
import logic.adapter.SimpleFilesAdapter;
import logic.async_await.AsyncTaskWorker;
import logic.async_await.CallableObj;
import logic.async_await.OnAsyncDoneRsObjListener;
import logic.helpers.FileUtils;
import logic.helpers.MyLogs;
import logic.helpers.ThemeColorsHelper;
import view.custom.WrapLayoutManager;

public class FrgListByFolders extends BaseFrg {

    //views
    private RecyclerView mRvItems;
    private RelativeLayout mRlLoading, mLlNoItms;
    private TextView tvFilepathsHistory;

    //vars
    private String mPath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_list, container, false);
        initViews(view);

        mPath = getArguments().getString(GenericConstants.EXTRA_ARG_PATH);
        if (mPath != null) {
            tvFilepathsHistory.setText(mPath);

            asyncGetFiles();

        } else {
            showNoItems(GenericConstants.STATE_EMPTY, mRvItems, mRlLoading, mLlNoItms);
        }

        return view;
    }

    private void initViews(View view) {
        mRvItems = (RecyclerView) view.findViewById(R.id.rv_items_simple);
        mRlLoading = (RelativeLayout) view.findViewById(R.id.rv_loading);
        mLlNoItms = (RelativeLayout) view.findViewById(R.id.directory_empty_view);
        tvFilepathsHistory = (TextView) view.findViewById(R.id.tv_path_name);
        tvFilepathsHistory.setBackgroundResource(ThemeColorsHelper.getColorPrimaryLight50());

        getActivity().setTitle(App.getAppCtx().getResources().getString(R.string.by_folder));

        initBanner(view);
    }

    private void initBanner(View view) {
        LinearLayout bannerHolder = (LinearLayout) view.findViewById(R.id.ll_banner);
        if (!App.isUserPro()) {
            bannerHolder.setVisibility(View.VISIBLE);

            CustomizeAds.setupAddBanner(
                    getActivity(),
                    bannerHolder,
                    AdSize.SMART_BANNER,
                    AdsRepo.getBannerId1(App.getAppCtx(), App.getAppBuilds(), App.getAppCtx().getResources().getString(R.string.banner_id)),
                    "FrgListByFolders screen");

        } else {
            bannerHolder.setVisibility(View.GONE);
        }
    }

    private void asyncGetFiles() {
        showNoItems(GenericConstants.STATE_LOADING, mRvItems, mRlLoading, mLlNoItms);

        //for cut paste functional
        onCutPasteListChanged(null);

        new AsyncTaskWorker(
                new CallableObj<List<SimpleFileWrapper>>() {
                    public List<SimpleFileWrapper> call() {
                        return FileUtils.getFileListByDirPath(mPath);
                    }
                },
                new OnAsyncDoneRsObjListener() {
                    @Override
                    public <T> void onDone(T t) {
                        if (t != null) {
                            loadItems((List<SimpleFileWrapper>) t);
                        }
                    }
                }
        ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void loadItems(List<SimpleFileWrapper> files) {
        if (files != null && files.size() > 0) {
            showNoItems(GenericConstants.STATE_OK, mRvItems, mRlLoading, mLlNoItms);

            mRvItems.setAdapter(new SimpleFilesAdapter(getActivity(), files, FrgListByFolders.this));
            mRvItems.setLayoutManager(new WrapLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mRvItems.setHasFixedSize(true);

        } else {
            showNoItems(GenericConstants.STATE_EMPTY, mRvItems, mRlLoading, mLlNoItms);
        }
    }


    @Override
    public void onRestartSelections() {
        //MyLogs.LOG("FrgListByFolders", "onRestartSelections", "...");
        asyncGetFiles();
    }
}