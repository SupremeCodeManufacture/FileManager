package view.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SupremeManufacture.filemanager.BuildConfig;
import com.SupremeManufacture.filemanager.R;
import com.soloviof.easyads.AdsRepo;
import com.soloviof.easyads.InitApp;
import com.soloviof.easyads.InterstitialAddsHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import data.App;
import data.GenericConstants;
import data.ThemeObj;
import logic.helpers.FileUtils;
import logic.helpers.PermissionsHelper;
import logic.helpers.ThemeColorsHelper;
import logic.helpers.Utils;
import logic.listener.OnRestartFilesSelectionsListener;
import logic.listener.OnSingleSelectionListener;
import logic.listener.OnThemeSelectedListener;
import logic.payment.PaymentHelper;
import logic.payment.util.IabHelper;
import logic.payment.util.IabResult;
import logic.payment.util.Inventory;
import logic.payment.util.Purchase;
import view.custom.Dialogs;
import view.custom.OnTextInsertedListener;
import view.custom.SimpleDialogPopUpListener;
import view.custom.UpgradeDialog;
import view.fragment.FrgListByFolders;
import view.fragment.FrgListByLastUsed;
import view.fragment.FrgListByLastUsedFull;
import view.fragment.FrgMemorySelector;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        IabHelper.OnIabSetupFinishedListener,
        IabHelper.OnIabPurchaseFinishedListener,
        IabHelper.QueryInventoryFinishedListener,
        OnThemeSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private BottomSheetBehavior sheetBehavior;
    private TextView tvRename, tvDet;
    private View mFakeViewPadding;


    private OnRestartFilesSelectionsListener listener;
    private List<String> mListSelectedFiles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(ThemeColorsHelper.getNoTitleTheme(), true);
        setContentView(R.layout.activity_home);

        initViews();

        boolean needToShowPayBannerFlag = getIntent().getBooleanExtra(GenericConstants.EXTRA_NEED_UPGRADE, false);
        if (needToShowPayBannerFlag) {
            showUpgradeDialog();
        }

        //initialised only here - just to know if it's PRO for further activities
        PaymentHelper.setUpPayments(HomeActivity.this, HomeActivity.this);

        //setupAdMobAds
        InitApp.doFirebaseInit(HomeActivity.this, AdsRepo.getAppId(App.getAppCtx(), App.getAppBuilds(), App.getAppCtx().getResources().getString(R.string.ads_app_id)));

        InterstitialAddsHelper.prepareInterstitialAds(
                HomeActivity.this,
                App.getAppBuilds(),
                App.getAppCtx().getResources().getString(R.string.banner_id_interstitial));


        if (!PermissionsHelper.hasPermissions(this, PermissionsHelper.PERMISSIONS_STORAGE)) {
            ActivityCompat.requestPermissions(this, PermissionsHelper.PERMISSIONS_STORAGE, PermissionsHelper.ACTION_MANAGE_STORE_PERMISSION_CODE);

        } else {
            decideMemoryPaths(GenericConstants.KEY_SELECTED_BY_PATH, true);
        }
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getHeaderView(0).findViewById(R.id.drawer_head).setBackgroundResource(ThemeColorsHelper.getColorPrimary());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.tv_title_nav);
        String str = App.getAppCtx().getResources().getString(R.string.app_name) + " v. " + BuildConfig.VERSION_NAME;
        navUsername.setText(str);

        LinearLayout mSheetBehavior = (LinearLayout) findViewById(R.id.bottom_sheet);
        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.bottom_bar);
        bottomBar.setBackgroundResource(ThemeColorsHelper.getColorPrimaryLight());
        sheetBehavior = BottomSheetBehavior.from(mSheetBehavior);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mFakeViewPadding.setVisibility(View.GONE);

                    if (mListSelectedFiles.size() > 0)
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                } else {
                    mFakeViewPadding.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        TextView tvShare = (TextView) findViewById(R.id.btn_share);
        setTvCompoundImg(tvShare, R.drawable.ic_share);
        tvShare.setOnClickListener(this);

        TextView tvMove = (TextView) findViewById(R.id.btn_move);
        setTvCompoundImg(tvMove, R.drawable.ic_move);
        tvMove.setOnClickListener(this);

        TextView tvDelete = (TextView) findViewById(R.id.btn_delete);
        setTvCompoundImg(tvDelete, R.drawable.ic_delete);
        tvDelete.setOnClickListener(this);

        TextView tvMore = (TextView) findViewById(R.id.btn_more);
        setTvCompoundImg(tvMore, R.drawable.ic_more);
        tvMore.setOnClickListener(this);

        TextView tvCopy = (TextView) findViewById(R.id.tv_copy);
        tvCopy.setOnClickListener(this);

        tvRename = (TextView) findViewById(R.id.tv_rename);
        tvRename.setOnClickListener(this);

        tvDet = (TextView) findViewById(R.id.tv_det);
        tvDet.setOnClickListener(this);

        mFakeViewPadding = (View) findViewById(R.id.view_bottom_mask);

        showHideRemoveAdsOption(App.isUserPro());
    }

    private void showHideRemoveAdsOption(boolean isPro) {
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_delete_ads).setVisible(!isPro);
    }

    private void setTvCompoundImg(TextView tv, int imgRes) {
        Drawable drawable = App.getAppCtx().getResources().getDrawable(imgRes);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, App.getAppCtx().getResources().getColor(ThemeColorsHelper.getColorPrimaryDark()));
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }


    public void decideMemoryPaths(int whereToGoFurther, boolean isFirstOpen) {
        String sdCardPath = FileUtils.getSdCardPath();
        //MyLogs.LOG("HomeActivity", "decideMemoryPaths", "sdCardPath: " + sdCardPath);

        if (sdCardPath != null) {
            FrgMemorySelector frgMemorySelector = new FrgMemorySelector();
            Bundle args = new Bundle();
            args.putInt(GenericConstants.EXTRA_ARG_VIEW_TYPE, whereToGoFurther);
            args.putString(GenericConstants.EXTRA_ARG_SD_CARD_PATH, sdCardPath);
            frgMemorySelector.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            //this will clear the back stack and displays no animation on the screen
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


            fragmentManager.beginTransaction()
                    .replace(R.id.frg_container, frgMemorySelector, "FrgMemorySelector")
                    .commit();

        } else {
            switch (whereToGoFurther) {
                case GenericConstants.KEY_SELECTED_LAST_USED:
                    openListByLastUsed();
                    break;

                case GenericConstants.KEY_SELECTED_BY_PATH:
                    openListByFolder(GenericConstants.EXTRA_ALL_INTERNAL_FILES_PATH, isFirstOpen);
                    break;
            }
        }
    }

    public void openListByFolder(String path, boolean isFirstFrg) {
        FrgListByFolders instance = new FrgListByFolders();
        this.listener = instance;//rely frg

        Bundle args = new Bundle();
        args.putString(GenericConstants.EXTRA_ARG_PATH, path);
        instance.setArguments(args);

        if (isFirstFrg) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frg_container, instance, "FrgListByFolders")
                    .commit();

        } else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frg_container, instance, "FrgListByFolders")
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void openListByLastUsed() {
        FrgListByLastUsed instance = new FrgListByLastUsed();
        this.listener = instance;//rely frg

        getFragmentManager().beginTransaction()
                .replace(R.id.frg_container, instance, "FrgListByLastUsed")
                .addToBackStack(null)
                .commit();
    }

    public void openListByLastUsed(String pattern, String title) {
        FrgListByLastUsedFull instance = new FrgListByLastUsedFull();
        this.listener = instance;//rely frg

        Bundle args = new Bundle();
        args.putString(GenericConstants.EXTRA_ARG_PATTERN, pattern);
        args.putString(GenericConstants.EXTRA_ARG_TITLE, title);
        instance.setArguments(args);

        getFragmentManager().beginTransaction()
                .replace(R.id.frg_container, instance, "FrgListByLastUsedFull")
                .addToBackStack(null)
                .commit();
    }


    public void manageMenuBtns(List<String> listToMove) {
        if (listToMove != null && listToMove.size() > 0) {
            this.mListSelectedFiles.addAll(listToMove);

            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            if (listToMove.size() == 1) {
                tvDet.setVisibility(View.VISIBLE);
                tvRename.setVisibility(View.VISIBLE);

            } else {
                tvDet.setVisibility(View.GONE);
                tvRename.setVisibility(View.GONE);
            }

        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            this.mListSelectedFiles.clear();
        }
    }

    public void doPastFiles(String destinationPath) {
        boolean statusOk = false;

        if (mListSelectedFiles.size() > 0) {
            for (String filePath : mListSelectedFiles) {
                //MyLogs.LOG("HomeActivity", "doPastFiles", "filePath: " + filePath);
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                String pathTo = destinationPath + "/" + fileName;
                //MyLogs.LOG("HomeActivity", "doPastFiles", "fileName: " + fileName + " pathTo: " + pathTo);

                statusOk = FileUtils.moveFileNative(filePath, pathTo) || FileUtils.moveFileLib(filePath, pathTo);
                //MyLogs.LOG("HomeActivity", "doPastFiles", "statusOk: " + statusOk);
            }
        }

        Toast.makeText(HomeActivity.this, statusOk ? App.getAppCtx().getResources().getString(R.string.txt_past_ok) : App.getAppCtx().getResources().getString(R.string.err_filed), Toast.LENGTH_SHORT).show();
    }

    public void doCopyFiles(String destinationPath) {
        boolean statusOk = false;

        if (mListSelectedFiles.size() > 0) {
            for (String filePath : mListSelectedFiles) {
                //MyLogs.LOG("HomeActivity", "doCopyFiles", "filePath: " + filePath);
                String pathFrom = filePath.substring(0, filePath.lastIndexOf("/") + 1);
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                String pathTo = destinationPath + "/" + fileName;
                //MyLogs.LOG("HomeActivity", "doCopyFiles", "pathFrom: " + pathFrom + " fileName: " + fileName + " pathTo: " + pathTo);

                statusOk = FileUtils.copyFileLib(filePath, pathTo);
            }
        }

        Toast.makeText(HomeActivity.this, statusOk ? App.getAppCtx().getResources().getString(R.string.txt_copy_ok) : App.getAppCtx().getResources().getString(R.string.err_filed), Toast.LENGTH_SHORT).show();
    }


    public void doFileDelete() {
        boolean statusOk = false;

        if (mListSelectedFiles.size() > 0) {
            for (String filePath : mListSelectedFiles) {
                //MyLogs.LOG("HomeActivity", "doFileDelete", "filePath: " + filePath);

                statusOk = FileUtils.removeFile(filePath);
            }
        }

        //refresh selection
        if (listener != null) listener.onRestartSelections();

        Toast.makeText(HomeActivity.this, statusOk ? App.getAppCtx().getResources().getString(R.string.txt_del_ok) : App.getAppCtx().getResources().getString(R.string.err_filed), Toast.LENGTH_SHORT).show();
    }

    public void doRenameFile(final String fileName) {
        final File file = new File(fileName);

        if (file.exists()) {
            //MyLogs.LOG("HomeActivity", "doRenameFile", "ok");

            Dialogs.showOptionalInputTxtDialog(HomeActivity.this, file.getName(), new OnTextInsertedListener() {
                @Override
                public void onTxtInserted(String txt) {
                    boolean statusOk = FileUtils.renameFile(fileName, file.getParent() + File.separator + txt);
                    Toast.makeText(HomeActivity.this, statusOk ? App.getAppCtx().getResources().getString(R.string.txt_renamed_ok) : App.getAppCtx().getResources().getString(R.string.err_filed), Toast.LENGTH_SHORT).show();

                    //refresh selection
                    if (listener != null) listener.onRestartSelections();
                }
            });

        } else {
            //refresh selection
            if (listener != null) listener.onRestartSelections();
        }
    }

    private void showUpgradeDialog() {
        new UpgradeDialog(HomeActivity.this, new OnSingleSelectionListener() {
            @Override
            public void onConfirmButtonClick() {
                PaymentHelper.doLifePayment(HomeActivity.this, App.getPaymentHelper(), HomeActivity.this);
            }
        }).show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_by_last:
                decideMemoryPaths(GenericConstants.KEY_SELECTED_LAST_USED, false);
                break;

            case R.id.nav_delete_ads:
                showUpgradeDialog();
                break;

            case R.id.nav_by_folder:
                decideMemoryPaths(GenericConstants.KEY_SELECTED_BY_PATH, false);
                break;

            case R.id.nav_ask:
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", GenericConstants.SUPPORT_EMAIL, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, App.getAppCtx().getResources().getString(R.string.app_name));
                    startActivity(emailIntent);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(HomeActivity.this, App.getAppCtx().getResources().getString(R.string.error_txt_no_mail), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.nav_rate:
                try {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + App.getAppCtx().getPackageName());
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(HomeActivity.this, App.getAppCtx().getResources().getString(R.string.error_open_google_play), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.nav_about:
                String str = App.getAppCtx().getResources().getString(R.string.app_name) + " v. " + BuildConfig.VERSION_NAME;
                Toast.makeText(HomeActivity.this, str, Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_settings:
                Dialogs.showThemePikerDialog(HomeActivity.this, HomeActivity.this);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionsHelper.ACTION_MANAGE_STORE_PERMISSION_CODE) {
            if (!PermissionsHelper.areAllPermisionsGranted(grantResults)) {
                Dialogs.showSimpleDialog(HomeActivity.this,
                        App.getAppCtx().getResources().getString(R.string.txt_warn),
                        App.getAppCtx().getResources().getString(R.string.txt_no_mem_read_permissions),
                        true,
                        null, null,
                        new SimpleDialogPopUpListener() {
                            @Override
                            public void onConfirmButtonClick() {
                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                            }

                            @Override
                            public void onCancelButtonClick() {
                                HomeActivity.this.finish();
                            }
                        }
                );

            } else {
                decideMemoryPaths(GenericConstants.KEY_SELECTED_BY_PATH, true);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass on the activity result to the helper for handling
        if (!App.getPaymentHelper().handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where we perform any handling of activity results not related to in-app billing...
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == Activity.RESULT_OK) {
                InterstitialAddsHelper.tryShowInterstitialAdNow(Utils.isPassedAdsFree() && !App.isUserPro());

                switch (requestCode) {
                    case GenericConstants.CODE_PATH_TO_MOVE_SELECTED:
                        doPastFiles(data.getStringExtra(GenericConstants.EXTRA_ARG_PATH_TO));
                        break;

                    case GenericConstants.CODE_PATH_TO_COPY_SELECTED:
                        doCopyFiles(data.getStringExtra(GenericConstants.EXTRA_ARG_PATH_TO));
                        break;
                }

                //refresh selection
                if (listener != null) listener.onRestartSelections();
            }

        } else {
            //MyLogs.LOG("BaseActivity", "onActivityResult", "handled by IABUtil");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                if (mListSelectedFiles.size() > 0)
                    FileUtils.shareFile(HomeActivity.this, mListSelectedFiles.get(0));

                //refresh selection
                if (listener != null) listener.onRestartSelections();
                break;

            case R.id.btn_move:
                startActivityForResult(new Intent(HomeActivity.this, PathSelectionActivity.class), GenericConstants.CODE_PATH_TO_MOVE_SELECTED);
                break;

            case R.id.btn_delete:
                doFileDelete();
                break;

            case R.id.btn_more:
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;

            case R.id.tv_copy:
                startActivityForResult(new Intent(HomeActivity.this, PathSelectionActivity.class), GenericConstants.CODE_PATH_TO_COPY_SELECTED);
                break;

            case R.id.tv_rename:
                if (mListSelectedFiles.size() > 0)
                    doRenameFile(mListSelectedFiles.get(0));
                break;

            case R.id.tv_det:
                if (mListSelectedFiles.size() > 0)
                    FileUtils.showFileDetails(HomeActivity.this, mListSelectedFiles.get(0));

                //refresh selection
                if (listener != null) listener.onRestartSelections();
                break;
        }
    }


    //=============================== PAYMENTS FUNCTIONAL ========================================//
    @Override
    public void onIabSetupFinished(IabResult result) {
        if (result.isSuccess()) {
            //MyLogs.LOG("HomeActivity", "onIabSetupFinished", "Setting up In-app Billing succesfull");
            PaymentHelper.getLifePaymentStatus(App.getPaymentHelper(), HomeActivity.this);

        } else {
            //MyLogs.LOG("HomeActivity", "onIabSetupFinished", "Problem setting up In-app Billing: " + result);
        }
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        if (result.isSuccess()) {
            boolean isPro = inventory.hasPurchase(GenericConstants.KEY_IN_APP_SKU_ID);

            //MyLogs.LOG("HomeActivity", "onQueryInventoryFinished", "isPro: " + isPro);
            App.setUserPro(isPro);
            showHideRemoveAdsOption(isPro);

        } else {
            //MyLogs.LOG("HomeActivity", "onQueryInventoryFinished", "Error query inventory: " + result);
        }
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        if (result.isSuccess()) {
            //MyLogs.LOG("BaseActivity", "onIabPurchaseFinished", "purchese SKU: " + purchase.getSku());
            App.setUserPro(true);
            showHideRemoveAdsOption(true);
            Toast.makeText(HomeActivity.this, App.getAppCtx().getResources().getString(R.string.txt_worning_pro_done), Toast.LENGTH_LONG).show();

        } else {
            //MyLogs.LOG("BaseActivity", "onIabPurchaseFinished", "Error purchasing: " + result);
        }
    }

    @Override
    public void onThemeSelected(final ThemeObj themeObj) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                App.setSelectedTheme(themeObj.getThemeId());
                recreate();
            }
        }, 300);


    }
}