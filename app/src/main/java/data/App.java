package data;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.ap.gdpr.ApGdpr;
import com.ap.gdpr.internal.IAgreementConsentType;
import com.ap.gdpr.internal.IOnRemoteStatusListener;
import com.ap.gdpr.internal.ISdkAgreement;

import logic.helpers.MyLogs;
import logic.payment.util.IabHelper;

public class App extends MultiDexApplication implements ISdkAgreement {

    private static Context mContext;
    public static IabHelper PAYMENT_HELPER;
    public static int APP_BUILDS;
    public static boolean USER_PRO;
    public static int SELECTED_THEME;
    public static String selectedMemoryDefPath;
    public static String selectedMemoryCopyMovePath;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        //ads tapcore SDK
        initAdsSdk();
        com.SupremeManufacture.filemanager.SdkAgreement.getAgreement(getApplicationContext());
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

    private void initAdsSdk() {
        ApGdpr.init(this);
        registerAgreements();
        Thread fetchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ApGdpr.fetchRemoteStatuses();
            }
        });
        fetchThread.setPriority(Thread.MIN_PRIORITY);
        fetchThread.start();
    }

    private void registerAgreements() {
        ApGdpr.registerAgreement(new App());
    }


    //ads sdk
    @Override
    public IAgreementConsentType[] getConsentTypes() {
        //MyLogs.LOG("App", "getConsentTypes", "...");
        return new IAgreementConsentType[0];
    }

    @Override
    public void setUserConsent(IAgreementConsentType iAgreementConsentType, long l, boolean b) {
        //MyLogs.LOG("App", "setUserConsent", "...");
    }

    @Override
    public String getSdkIdentifier() {
        //MyLogs.LOG("App", "getSdkIdentifier", "...");
        return null;
    }

    @Override
    public void fetchRemoteStatus(IAgreementConsentType iAgreementConsentType, IOnRemoteStatusListener iOnRemoteStatusListener) {
        //MyLogs.LOG("App", "fetchRemoteStatus", "...");
    }
}