package logic.payment;

import android.app.Activity;
import android.content.Context;

import com.SupremeManufacture.filemanager.R;

import data.App;
import data.GenericConstants;
import logic.helpers.MyLogs;
import logic.payment.util.IabHelper;
import view.custom.Dialogs;

public class PaymentHelper {

    public static void setUpPayments(Context activityCtx, IabHelper.OnIabSetupFinishedListener registerListener) {
        try {
            String base64EncodedPublicKey = App.getAppCtx().getResources().getString(R.string.app_key);
            base64EncodedPublicKey = base64EncodedPublicKey.replaceAll("\\s+", "");
            IabHelper iabHelper = new IabHelper(activityCtx, base64EncodedPublicKey);

            iabHelper.startSetup(registerListener);
            App.setPaymentHelper(iabHelper);
            MyLogs.LOG("PaymentHelper", "setUpPayments", "succesfuly");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            MyLogs.LOG("PaymentHelper", "setUpPayments", "ERROR --> " + throwable.toString());
        }
    }


    public static void getLifePaymentStatus(IabHelper iabHelper, IabHelper.QueryInventoryFinishedListener inventoryListener) {
        try {
            iabHelper.flagEndAsync();
            iabHelper.queryInventoryAsync(inventoryListener);

        } catch (Throwable e) {
            MyLogs.LOG("PaymentHelper", "getLifePaymentStatus", "ERROR --> " + e.getMessage());
        }
    }

    public static void doLifePayment(final Activity activity, IabHelper iabHelper, IabHelper.OnIabPurchaseFinishedListener purchaseListener) {
        try {
            MyLogs.LOG("PaymentHelper", "doLifePayment", "google dialog shold be visible");
            iabHelper.flagEndAsync();
            iabHelper.launchPurchaseFlow(activity, GenericConstants.KEY_IN_APP_SKU_ID, GenericConstants.KEY_IN_APP_RESPONSE_CODE, purchaseListener, null);

        } catch (Exception e) {
            if (activity != null && !activity.isFinishing()) {
                Dialogs.showSimpleDialog(activity,
                        App.getAppCtx().getResources().getString(R.string.txt_warn),
                        App.getAppCtx().getResources().getString(R.string.txt_err_upgrade),
                        false, null, null, null
                );
                MyLogs.LOG("PaymentHelper", "doLifePayment", "ERROR --> " + e.getMessage());
            }
        }
    }
}