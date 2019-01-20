package logic.adapter;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.SupremeManufacture.filemanager.R;

import java.util.List;

import data.App;
import data.ThemeObj;
import logic.helpers.ThemeColorsHelper;
import logic.listener.OnThemeSelectedListener;

public class ThemesAdapter extends RecyclerView.Adapter<ThemesAdapter.ViewHolder> {

    private List<ThemeObj> mData;
    private OnThemeSelectedListener mListener;

    public ThemesAdapter( OnThemeSelectedListener listener) {
        this.mData = ThemeColorsHelper.getThemesData();
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_theme_itm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        ThemeObj themeObj = mData.get(position);

        if (themeObj != null) {
            viewHolder.statusBarView.setBackgroundResource(themeObj.getPrimaryDarkColor());
            viewHolder.toolbarView.setBackgroundResource(themeObj.getPrimaryColor());
            viewHolder.floatingBtn.setBackgroundTintList(ColorStateList.valueOf(App.getAppCtx().getResources().getColor(themeObj.getAccentColor())));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout llWholeItm;
        View statusBarView, toolbarView;
        FloatingActionButton floatingBtn;

        ViewHolder(final View itemView) {
            super(itemView);

            statusBarView = (View) itemView.findViewById(R.id.v1);
            toolbarView = (View) itemView.findViewById(R.id.v2);
            floatingBtn = (FloatingActionButton) itemView.findViewById(R.id.float_btn);

            llWholeItm = (RelativeLayout) itemView.findViewById(R.id.zone_theme);
            llWholeItm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION && mListener != null) {
                switch (view.getId()) {
                    case R.id.zone_theme:
                        mListener.onThemeSelected(mData.get(clickedPosition));
                        break;
                }
            }
        }
    }

}