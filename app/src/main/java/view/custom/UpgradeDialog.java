package view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.SupremeManufacture.filemanager.R;

import data.App;
import logic.adapter.UpgradePackOptionsAdapter;
import logic.listener.OnSingleSelectionListener;


public class UpgradeDialog extends Dialog implements View.OnClickListener {

    private RecyclerView rvPackItemDescription;

    private OnSingleSelectionListener mOnConfirmUpgradeListener;


    public UpgradeDialog(@NonNull Context context, OnSingleSelectionListener listener) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (this.getWindow() != null)
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setContentView(R.layout.dialog_upgrade);
        this.mOnConfirmUpgradeListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeViews();

        loadData();
    }

    private void initializeViews() {
        rvPackItemDescription = (RecyclerView) findViewById(R.id.rv_options);

        TextView tvOldPrice = (TextView) findViewById(R.id.discount_price);
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Button btnBuyNow = (Button) findViewById(R.id.btn_upgrade);
        btnBuyNow.setOnClickListener(this);
    }

    private void loadData() {
        rvPackItemDescription.setAdapter(new UpgradePackOptionsAdapter(App.getAppCtx().getResources().getStringArray(R.array.pro_options)));
        rvPackItemDescription.setLayoutManager(new WrapLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rvPackItemDescription.setHasFixedSize(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_upgrade:
                if (mOnConfirmUpgradeListener != null)
                    mOnConfirmUpgradeListener.onConfirmButtonClick();
                UpgradeDialog.this.dismiss();
        }
    }
}
