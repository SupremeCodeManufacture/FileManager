package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import data.App;
import data.GenericConstants;
import logic.helpers.MyLogs;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        App.setAppBuilds(App.getAppBuilds() + 1);

        if (App.getFirstLaunchMilis() == 0)
            App.setFirstLaunchMilis(System.currentTimeMillis());

        boolean needToShowPayBannerFlag = getIntent().getBooleanExtra(GenericConstants.EXTRA_NEED_UPGRADE, false);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(GenericConstants.EXTRA_NEED_UPGRADE, needToShowPayBannerFlag);
        SplashScreenActivity.this.startActivity(intent);
        SplashScreenActivity.this.finish();
    }
}