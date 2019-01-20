package logic.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.SupremeManufacture.filemanager.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import data.App;
import data.SimpleFileWrapper;
import logic.helpers.FileTypeUtils;
import logic.helpers.FileUtils;
import logic.helpers.MyLogs;
import logic.helpers.ThemeColorsHelper;
import logic.helpers.Utils;
import logic.listener.OnItmClickListener;

/**
 * Created by vasil on 23-Oct-17.
 */

public class SimpleFilesAdapter extends RecyclerView.Adapter<SimpleFilesAdapter.ViewHolder> {

    private Context mActivityContext;
    private List<SimpleFileWrapper> mFilesList;
    private OnItmClickListener mOnItmClickListener;
    private String itms = App.getAppCtx().getResources().getString(R.string.txt_itms);

    private List<String> cuttedFileUrls = new ArrayList<>();


    public SimpleFilesAdapter(Context activityContext, List<SimpleFileWrapper> list, OnItmClickListener listener) {
        this.mActivityContext = activityContext;
        this.mFilesList = list;
        this.mOnItmClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_file_itm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        SimpleFileWrapper simpleFileWrapper = mFilesList.get(position);
        File currentFile = simpleFileWrapper.getFile();
        FileTypeUtils.FileType fileType = FileTypeUtils.getFileType(currentFile);

        viewHolder.tvTitle.setText(currentFile.getName());

        if (fileType == FileTypeUtils.FileType.IMAGE) {
            Picasso.with(mActivityContext)
                    .load("file://" + currentFile.getAbsolutePath())
                    .config(Bitmap.Config.RGB_565)
                    .fit().centerCrop()
                    .placeholder(Utils.getTintedDrawable(fileType.getIcon()))
                    .error(Utils.getTintedDrawable(fileType.getIcon()))
                    .into(viewHolder.ivIcon);

        } else {
            viewHolder.ivIcon.setImageResource(fileType.getIcon());
            ImageViewCompat.setImageTintList(viewHolder.ivIcon, ColorStateList.valueOf(App.getAppCtx().getResources().getColor(ThemeColorsHelper.getColorPrimary50())));
        }

        if (currentFile.isDirectory()) {
            viewHolder.tvDescr.setText(currentFile.listFiles().length + " " + itms + " | " + Utils.getPrettyDate(currentFile.lastModified()));

        } else {
            viewHolder.tvDescr.setText(FileUtils.getReadableFileSize(currentFile.length()) + " | " + Utils.getPrettyDate(currentFile.lastModified()));
        }

        if (currentFile.isDirectory()) {
            viewHolder.cbSelection.setVisibility(View.GONE);

        } else {
            viewHolder.cbSelection.setVisibility(View.VISIBLE);

            if (simpleFileWrapper.isSelected()) {
                viewHolder.cbSelection.setChecked(true);

            } else {
                viewHolder.cbSelection.setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFilesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout llWholeItm;
        ImageView ivIcon;
        TextView tvTitle, tvDescr;
        CheckBox cbSelection;

        ViewHolder(final View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.item_file_image);
            tvTitle = (TextView) itemView.findViewById(R.id.item_file_title);
            tvDescr = (TextView) itemView.findViewById(R.id.item_file_subtitle);

            cbSelection = (CheckBox) itemView.findViewById(R.id.cb_selected);
            cbSelection.setOnClickListener(this);

            llWholeItm = (RelativeLayout) itemView.findViewById(R.id.ll_whole_itm);
            llWholeItm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION && mOnItmClickListener != null) {
                switch (view.getId()) {
                    case R.id.ll_whole_itm:
                        mOnItmClickListener.onItemClicked(mFilesList.get(clickedPosition).getFile());
                        break;

                    case R.id.cb_selected:
                        manageItmSelection(clickedPosition);
                        break;
                }
            }
        }
    }


    private void manageItmSelection(int pos) {
        boolean newStatus = !mFilesList.get(pos).isSelected();
        String filePath = mFilesList.get(pos).getFile().getAbsolutePath();
        MyLogs.LOG("SimpleFilesAdapter", "manageItmSelection", "pos: " + pos + " newStatus: " + newStatus + " filePath: " + filePath);

        mFilesList.get(pos).setSelected(newStatus);
        this.notifyItemChanged(pos);

        if (newStatus) {
            if (!cuttedFileUrls.contains(filePath)) cuttedFileUrls.add(filePath);

        } else {
            cuttedFileUrls.remove(filePath);
        }

        mOnItmClickListener.onCutPasteListChanged(cuttedFileUrls);
    }

}