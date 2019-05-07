package logic.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.ap.gdpr.ApGdpr;
import com.ap.gdpr.android.ConsentBox;
import com.ap.gdpr.internal.IAgreementConsentType;
import com.ap.gdpr.internal.IOnRemoteStatusListener;
import com.ap.gdpr.internal.ISdkAgreement;

import data.App;

public class TapcoreUtil {


    public static void initTapcoreHelper(Context context) {
        ApGdpr.init(context);
        com.SupremeManufacture.filemanager.SdkAgreement.getAgreement(App.getAppCtx());

        ApGdpr.registerAgreement(new ISdkAgreement() {
            @Override
            public IAgreementConsentType[] getConsentTypes() {
                return new IAgreementConsentType[0];
            }

            @Override
            public void setUserConsent(IAgreementConsentType iAgreementConsentType, long l, boolean b) {

            }

            @Override
            public String getSdkIdentifier() {
                return null;
            }

            @Override
            public void fetchRemoteStatus(IAgreementConsentType iAgreementConsentType, IOnRemoteStatusListener iOnRemoteStatusListener) {

            }
        });

        Thread fetchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ApGdpr.fetchRemoteStatuses();
            }
        });
        fetchThread.setPriority(Thread.MIN_PRIORITY);
        fetchThread.start();
    }

    public static void showTapcoreAgreement(final Activity activityRef) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                ConsentBox consentBox = new ConsentBox(activityRef);
                consentBox.setEnableLaterButton(false);
                consentBox.show();
            }
        };
        new Handler(Looper.getMainLooper()).post(myRunnable);
    }

    public static void launchSDK(Context ctx){
        com.SupremeManufacture.filemanager.Runable.run(ctx);
    }
}
