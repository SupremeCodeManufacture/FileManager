package view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.SupremeManufacture.filemanager.R;
import com.google.android.gms.ads.AdSize;
import com.soloviof.easyads.AdsRepo;
import com.soloviof.easyads.CustomizeAds;

import java.util.ArrayList;
import java.util.List;

import data.App;
import data.CustomFile;
import data.GenericConstants;
import logic.adapter.CustomFilesAdapter;
import logic.async_await.AsyncTaskWorker;
import logic.async_await.CallableObj;
import logic.async_await.OnAsyncDoneRsObjListener;
import logic.helpers.FileUtils;
import logic.helpers.MyLogs;
import logic.listener.OnItmClickListener;
import view.activity.HomeActivity;
import view.custom.WrapLayoutManager;

public class FrgListByLastUsed extends BaseFrg implements View.OnClickListener, OnItmClickListener {

    //views
    private LinearLayout zoneImgs, zoneVids, zoneDocs, zoneMusic, zoneDownloads, zoneApks;
    private RecyclerView mRvItems;
    private RelativeLayout mRlLoading, mLlNoItms;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_current, container, false);
        initViews(view);

        asyncLoadAllRecent();

        return view;
    }

    private void initViews(View view) {
        zoneImgs = (LinearLayout) view.findViewById(R.id.zone_imgs);
        zoneImgs.setOnClickListener(this);

        zoneVids = (LinearLayout) view.findViewById(R.id.zone_vids);
        zoneVids.setOnClickListener(this);

        zoneDocs = (LinearLayout) view.findViewById(R.id.zone_docs);
        zoneDocs.setOnClickListener(this);

        zoneMusic = (LinearLayout) view.findViewById(R.id.zone_music);
        zoneMusic.setOnClickListener(this);

        zoneDownloads = (LinearLayout) view.findViewById(R.id.zone_downloads);
        zoneDownloads.setOnClickListener(this);

        zoneApks = (LinearLayout) view.findViewById(R.id.zone_apk);
        zoneApks.setOnClickListener(this);

        mRvItems = (RecyclerView) view.findViewById(R.id.itms_last);
        mRlLoading = (RelativeLayout) view.findViewById(R.id.rv_loading);
        mLlNoItms = (RelativeLayout) view.findViewById(R.id.directory_empty_view);

        getActivity().setTitle(App.getAppCtx().getResources().getString(R.string.by_last));

        LinearLayout bannerHolder = (LinearLayout) view.findViewById(R.id.ll_banner);
        initBanner(bannerHolder, "FrgListByLastUsed");
    }

    private void asyncLoadAllRecent() {
        showNoItems(GenericConstants.STATE_LOADING, mRvItems, mRlLoading, mLlNoItms);

        //no cut/past functional here
        onCutPasteListChanged(null);

        new AsyncTaskWorker(
                new CallableObj<List<CustomFile>>() {
                    public List<CustomFile> call() {

                        //final String pattern, final long timeSince
                        List<CustomFile> list = new ArrayList<>();
                        list.addAll(FileUtils.getFilesByType(App.getSelectedMemoryDefPath(), GenericConstants.REGEX_IMAGES, App.getAppCtx().getResources().getString(R.string.tv_imgs), GenericConstants.TIME_SINCE_3_HOURS));
                        list.addAll(FileUtils.getFilesByType(App.getSelectedMemoryDefPath(), GenericConstants.REGEX_VIDS, App.getAppCtx().getResources().getString(R.string.tv_vids), GenericConstants.TIME_SINCE_3_HOURS));
                        list.addAll(FileUtils.getFilesByType(App.getSelectedMemoryDefPath(), GenericConstants.REGEX_DOCS, App.getAppCtx().getResources().getString(R.string.tv_docs), GenericConstants.TIME_SINCE_3_HOURS));
                        list.addAll(FileUtils.getFilesByType(App.getSelectedMemoryDefPath(), GenericConstants.REGEX_MUSIC, App.getAppCtx().getResources().getString(R.string.tv_music), GenericConstants.TIME_SINCE_3_HOURS));
                        list.addAll(FileUtils.getFilesByType(App.getSelectedMemoryDefPath(), GenericConstants.REGEX_APKS, App.getAppCtx().getResources().getString(R.string.tv_apks), GenericConstants.TIME_SINCE_3_HOURS));

                        return list;
                    }
                },
                new OnAsyncDoneRsObjListener() {
                    @Override
                    public <T> void onDone(T t) {
                        if (t != null) {
                            loadItems((List<CustomFile>) t);
                        }
                    }
                }
        ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void loadItems(List<CustomFile> list) {
        if (list != null && list.size() > 0) {
            showNoItems(GenericConstants.STATE_OK, mRvItems, mRlLoading, mLlNoItms);

            mRvItems.setAdapter(new CustomFilesAdapter(getActivity(), list, FrgListByLastUsed.this));
            mRvItems.setLayoutManager(new WrapLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mRvItems.setHasFixedSize(true);

        } else {
            showNoItems(GenericConstants.STATE_EMPTY, mRvItems, mRlLoading, mLlNoItms);
        }
    }


    @Override
    public void onRestartSelections() {
        //MyLogs.LOG("FrgListByLastUsed", "onRestartSelections", "...");
        asyncLoadAllRecent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zone_imgs:
                ((HomeActivity) getActivity()).openListByLastUsed(GenericConstants.REGEX_IMAGES, App.getAppCtx().getResources().getString(R.string.tv_imgs));
                break;

            case R.id.zone_vids:
                ((HomeActivity) getActivity()).openListByLastUsed(GenericConstants.REGEX_VIDS, App.getAppCtx().getResources().getString(R.string.tv_vids));
                break;

            case R.id.zone_docs:
                ((HomeActivity) getActivity()).openListByLastUsed(GenericConstants.REGEX_DOCS, App.getAppCtx().getResources().getString(R.string.tv_docs));
                break;

            case R.id.zone_music:
                ((HomeActivity) getActivity()).openListByLastUsed(GenericConstants.REGEX_MUSIC, App.getAppCtx().getResources().getString(R.string.tv_music));
                break;

            case R.id.zone_downloads:
                ((HomeActivity) getActivity()).openListByFolder("/storage/emulated/0/Download", false);
                break;

            case R.id.zone_apk:
                ((HomeActivity) getActivity()).openListByLastUsed(GenericConstants.REGEX_APKS, App.getAppCtx().getResources().getString(R.string.tv_apks));
                break;
        }
    }
}