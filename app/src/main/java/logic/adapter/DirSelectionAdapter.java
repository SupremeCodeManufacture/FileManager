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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.SupremeManufacture.filemanager.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import data.App;
import data.SimpleFileWrapper;
import logic.helpers.FileTypeUtils;
import logic.helpers.FileUtils;
import logic.helpers.ThemeColorsHelper;
import logic.helpers.Utils;
import logic.listener.OnItmClickListener;

public class DirSelectionAdapter extends RecyclerView.Adapter<DirSelectionAdapter.ViewHolder> {

    private Context mActivityContext;
    private List<SimpleFileWrapper> mFilesList;
    private OnItmClickListener mOnItmClickListener;
    private String itms = App.getAppCtx().getResources().getString(R.string.txt_itms);


    public DirSelectionAdapter(Context activityContext, List<SimpleFileWrapper> list, OnItmClickListener listener) {
        this.mActivityContext = activityContext;
        this.mOnItmClickListener = listener;
        this.mFilesList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_file_itm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        File currentFile = mFilesList.get(position).getFile();
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
            viewHolder.llWholeItm.setBackgroundResource(R.drawable.bg_clickable);

        } else {
            viewHolder.llWholeItm.setBackgroundColor(App.getAppCtx().getResources().getColor(R.color.compatibility_pressed_color));
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

        ViewHolder(final View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.item_file_image);
            tvTitle = (TextView) itemView.findViewById(R.id.item_file_title);
            tvDescr = (TextView) itemView.findViewById(R.id.item_file_subtitle);

            llWholeItm = (RelativeLayout) itemView.findViewById(R.id.ll_whole_itm);
            llWholeItm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION && mOnItmClickListener != null) {
                switch (view.getId()) {
                    case R.id.ll_whole_itm:
                        if (mFilesList.get(clickedPosition).getFile().isDirectory())
                            mOnItmClickListener.onItemClicked(mFilesList.get(clickedPosition).getFile());
                        break;
                }
            }
        }
    }

}