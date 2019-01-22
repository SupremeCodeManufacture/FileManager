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
import data.CustomFile;
import logic.helpers.FileTypeUtils;
import logic.helpers.FileUtils;
import logic.helpers.MyLogs;
import logic.helpers.ThemeColorsHelper;
import logic.helpers.Utils;
import logic.listener.OnItmClickListener;

public class CustomFilesAdapter extends RecyclerView.Adapter<CustomFilesAdapter.ViewHolder> {

    private Context mActivityContext;
    private List<CustomFile> mFilesList;
    private OnItmClickListener mOnItmClickListener;

    private List<String> cuttedFileUrls = new ArrayList<>();


    public CustomFilesAdapter(Context activityContext, List<CustomFile> list, OnItmClickListener listener) {
        this.mActivityContext = activityContext;
        this.mFilesList = list;
        this.mOnItmClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.complex_itm_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final CustomFile customFile = mFilesList.get(position);
        File currentFile = customFile.getFile();

        FileTypeUtils.FileType fileType = FileTypeUtils.getFileType(currentFile);
        viewHolder.tvTitle.setText(currentFile.getName());
        viewHolder.tvDescr.setText(FileUtils.getReadableFileSize(currentFile.length()) + " | " + Utils.getPrettyDate(currentFile.lastModified()));

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

        if (customFile.getCatName() != null) {
            viewHolder.rlHeader.setVisibility(View.VISIBLE);
            viewHolder.tvCatName.setText(customFile.getCatName());

        } else {
            viewHolder.rlHeader.setVisibility(View.GONE);
        }

        if (currentFile.isDirectory()) {
            viewHolder.cbSelection.setVisibility(View.GONE);

        } else {
            viewHolder.cbSelection.setVisibility(View.VISIBLE);

            if (customFile.isSelected()) {
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
        RelativeLayout rlHeader, llWholeItm;
        ImageView ivIcon;
        TextView tvTitle, tvDescr, tvCatName;
        CheckBox cbSelection;

        ViewHolder(final View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.item_file_image);
            tvTitle = (TextView) itemView.findViewById(R.id.item_file_title);
            tvDescr = (TextView) itemView.findViewById(R.id.item_file_subtitle);
            tvCatName = (TextView) itemView.findViewById(R.id.tv_title);

            rlHeader = (RelativeLayout) itemView.findViewById(R.id.zone_go_to);
            rlHeader.setBackgroundResource(ThemeColorsHelper.getColorPrimaryLight50());
            rlHeader.setOnClickListener(this);

            llWholeItm = (RelativeLayout) itemView.findViewById(R.id.ll_whole_itm);
            llWholeItm.setOnClickListener(this);

            cbSelection = (CheckBox) itemView.findViewById(R.id.cb_selected);
            cbSelection.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION && mOnItmClickListener != null) {
                switch (view.getId()) {
                    case R.id.zone_go_to:
                        mOnItmClickListener.onCutomCatSelected(mFilesList.get(clickedPosition).getPatternRegex(), mFilesList.get(clickedPosition).getCatName());
                        break;

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
        //MyLogs.LOG("CustomFilesAdapter", "manageItmSelection", "pos: " + pos + " newStatus: " + newStatus + " filePath: " + filePath);

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