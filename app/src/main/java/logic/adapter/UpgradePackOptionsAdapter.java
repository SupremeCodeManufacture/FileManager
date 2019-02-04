package logic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.SupremeManufacture.filemanager.R;

import logic.helpers.MyLogs;

public class UpgradePackOptionsAdapter extends RecyclerView.Adapter<UpgradePackOptionsAdapter.ViewHolder> {

    private String[] mData;


    public UpgradePackOptionsAdapter(String[] array) {
       //MyLogs.LOG("UpgradePackOptionsAdapter", "constructor", "..." + array.length);
        this.mData = array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //MyLogs.LOG("UpgradePackOptionsAdapter", "onCreateViewHolder", "...");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_selectable_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        String str = mData[position];

        if (str != null) {
            viewHolder.tvTxt.setText(str);
        }
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTxt;

        ViewHolder(final View itemView) {
            super(itemView);
            tvTxt = (TextView) itemView.findViewById(R.id.tv_itm_txt);
        }
    }
}