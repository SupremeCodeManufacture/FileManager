package data;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.SupremeManufacture.filemanager.BuildConfig;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import logic.payment.util.IabHelper;

public class App extends MultiDexApplication {

    private static Context mContext;
    public static IabHelper PAYMENT_HELPER;
    public static int APP_BUILDS;
    public static boolean USER_PRO;
    public static int SELECTED_THEME;
    public static String selectedMemoryDefPath;
    public static String selectedMemoryCopyMovePath;
    public static long FIRST_LAUNCH_MILIS;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(BuildConfig.DEBUG)
                .build();
        Fabric.with(fabric);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static Context getAppCtx() {
        return mContext;
    }

    public static IabHelper getPaymentHelper() {
        return PAYMENT_HELPER;
    }

    public static void setPaymentHelper(IabHelper paymentHelper) {
        PAYMENT_HELPER = paymentHelper;
    }

    public static boolean isUserPro() {
        return USER_PRO || SharedPrefs.getSharedPrefsBool(GenericConstants.KEY_SP_IS_USER_PRO);
    }

    public static void setUserPro(boolean userPro) {
        USER_PRO = userPro;
        SharedPrefs.setSharedPrefsBool(GenericConstants.KEY_SP_IS_USER_PRO, userPro);
    }

    public static int getAppBuilds() {
        return APP_BUILDS != 0 ? APP_BUILDS : SharedPrefs.getSharedPrefsInt(GenericConstants.KEY_SP_APP_BUILDS);
    }

    public static void setAppBuilds(int appBuilds) {
        APP_BUILDS = appBuilds;
        SharedPrefs.setSharedPrefsInt(GenericConstants.KEY_SP_APP_BUILDS, appBuilds);
    }

    public static int getSelectedTheme() {
        if (SELECTED_THEME != 0)
            return SELECTED_THEME;

        return SharedPrefs.getSharedPrefsInt(GenericConstants.KEY_SP_SELECTED_THEME);
    }

    public static void setSelectedTheme(int selectedTheme) {
        SELECTED_THEME = selectedTheme;
        SharedPrefs.setSharedPrefsInt(GenericConstants.KEY_SP_SELECTED_THEME, selectedTheme);
    }

    public static String getSelectedMemoryDefPath() {
        if (selectedMemoryDefPath != null)
            return selectedMemoryDefPath;

        String sp = SharedPrefs.getSharedPrefsString(GenericConstants.KEY_SP_SELECTED_DEF_PATH);

        return sp != null ? sp : GenericConstants.EXTRA_ALL_INTERNAL_FILES_PATH;
    }

    public static void setSelectedMemoryDefPath(String selectedMemoryDefPath) {
        App.selectedMemoryDefPath = selectedMemoryDefPath;
        SharedPrefs.setSharedPrefsString(GenericConstants.KEY_SP_SELECTED_DEF_PATH, selectedMemoryDefPath);
    }

    public static String getSelectedMemoryCopyMovePath() {
        return selectedMemoryCopyMovePath;
    }

    public static void setSelectedMemoryCopyMovePath(String selectedMemoryCopyMovePath) {
        App.selectedMemoryCopyMovePath = selectedMemoryCopyMovePath;
    }

    public static long getFirstLaunchMilis() {
        return FIRST_LAUNCH_MILIS != 0 ? FIRST_LAUNCH_MILIS : SharedPrefs.getSharedPrefsLong(GenericConstants.KEY_FIRST_LAUNCH);
    }

    public static void setFirstLaunchMilis(long firstLaunchMilis) {
        FIRST_LAUNCH_MILIS = firstLaunchMilis;
        SharedPrefs.setSharedPrefsLong(GenericConstants.KEY_FIRST_LAUNCH, firstLaunchMilis);
    }
}