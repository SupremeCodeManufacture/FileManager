package view.fragment;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.SupremeManufacture.filemanager.R;

import java.io.File;
import java.util.List;

import data.App;
import data.GenericConstants;
import data.SimpleFileWrapper;
import logic.adapter.DirSelectionAdapter;
import logic.async_await.AsyncTaskWorker;
import logic.async_await.CallableObj;
import logic.async_await.OnAsyncDoneRsObjListener;
import logic.helpers.FileUtils;
import logic.helpers.ThemeColorsHelper;
import view.activity.PathSelectionActivity;
import view.custom.WrapLayoutManager;

public class FrgListPathSelection extends BaseFrg implements
        View.OnClickListener {

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

            showNoItems(GenericConstants.STATE_LOADING, mRvItems, mRlLoading, mLlNoItms);
            asyncGetFiles();

        } else {
            showNoItems(GenericConstants.STATE_EMPTY, mRvItems, mRlLoading, mLlNoItms);
        }

        return view;
    }

    private void initViews(View view) {
        LinearLayout zonePasteBtns = (LinearLayout) view.findViewById(R.id.zone_btns);
        zonePasteBtns.setBackgroundResource(ThemeColorsHelper.getColorPrimaryLight());
        zonePasteBtns.setVisibility(View.VISIBLE);

        mRvItems = (RecyclerView) view.findViewById(R.id.rv_items_simple);
        mRlLoading = (RelativeLayout) view.findViewById(R.id.rv_loading);
        mLlNoItms = (RelativeLayout) view.findViewById(R.id.directory_empty_view);
        tvFilepathsHistory = (TextView) view.findViewById(R.id.tv_path_name);
        tvFilepathsHistory.setBackgroundResource(ThemeColorsHelper.getColorPrimaryLight50());

        TextView btnCancel = (TextView) view.findViewById(R.id.cancel_action);
        setTvCompoundImg(btnCancel, R.drawable.ic_cancel);
        btnCancel.setOnClickListener(this);

        TextView btnPaste = (TextView) view.findViewById(R.id.paste_action);
        setTvCompoundImg(btnPaste, R.drawable.ic_check);
        btnPaste.setOnClickListener(this);

        getActivity().setTitle(App.getAppCtx().getResources().getString(R.string.by_folder_select));
    }

    private void setTvCompoundImg(TextView tv, int imgRes) {
        Drawable drawable = App.getAppCtx().getResources().getDrawable(imgRes);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, App.getAppCtx().getResources().getColor(ThemeColorsHelper.getColorPrimaryDark()));
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }

    private void asyncGetFiles() {
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

            mRvItems.setAdapter(new DirSelectionAdapter(getActivity(), files, FrgListPathSelection.this));
            mRvItems.setLayoutManager(new WrapLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mRvItems.setHasFixedSize(true);

        } else {
            showNoItems(GenericConstants.STATE_EMPTY, mRvItems, mRlLoading, mLlNoItms);
        }
    }

    @Override
    public void onItemClicked(File file) {
        //here is received only dir file from adapter
        ((PathSelectionActivity) getActivity()).openPasteListByFolder(file.getAbsolutePath(), false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.paste_action:
                ((PathSelectionActivity) getActivity()).returnPathToPaste(mPath);
                break;

            case R.id.cancel_action:
                ((PathSelectionActivity) getActivity()).returnPathToPaste(null);
                break;
        }
    }

}
