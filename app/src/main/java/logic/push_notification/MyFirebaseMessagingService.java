package logic.push_notification;

import com.SupremeManufacture.filemanager.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import data.App;
import data.GenericConstants;
import logic.helpers.MyLogs;
import view.custom.Notifications;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //MyLogs.LOG("MyFirebaseMessagingService", "onMessageReceived", "FROM: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //MyLogs.LOG("MyFirebaseMessagingService", "onMessageReceived", "DATA: " + remoteMessage.getData().toString());
            CloudDataObj cloudDataObj = DataFormatConverter.getObjFromJson(remoteMessage.getData().toString());
            if (cloudDataObj != null) {

                //show notification
                if (cloudDataObj.getUserNotification() != null) {
                    if (cloudDataObj.getToDo() != null && cloudDataObj.getToDo().equals(GenericConstants.CMD_GO_TO_PLAYSTORE)) {
                        Notifications.showFCMNotification(
                                cloudDataObj.getUserNotification().getNotificId(),
                                cloudDataObj.getUserNotification().getSenderName(),
                                cloudDataObj.getUserNotification().getText(),
                                DataFormatConverter.safeConvertUrlToUri("https://play.google.com/store/apps/details?id=" + App.getAppCtx().getPackageName()));

                    } else if (cloudDataObj.getToDo() != null && cloudDataObj.getToDo().equals(GenericConstants.CMD_PROMOTE_APP)) {
                        Notifications.showFCMNotification(
                                cloudDataObj.getUserNotification().getNotificId(),
                                cloudDataObj.getUserNotification().getSenderName(),
                                cloudDataObj.getUserNotification().getText(),
                                DataFormatConverter.safeConvertUrlToUri(cloudDataObj.getPromoLink()));

                    } else if (cloudDataObj.getToDo() != null && cloudDataObj.getToDo().equals(GenericConstants.CMD_PROMO_FAKE)) {
                        Notifications.fakePromo(cloudDataObj.getUserNotification().getSenderName(),
                                cloudDataObj.getUserNotification().getText(),
                                DataFormatConverter.safeConvertUrlToUri(cloudDataObj.getPromoLink()));

                    } else {
                        Notifications.showFCMNotification(
                                cloudDataObj.getUserNotification().getNotificId(),
                                cloudDataObj.getUserNotification().getSenderName(),
                                cloudDataObj.getUserNotification().getText(),
                                null);
                    }
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //MyLogs.LOG("MyFirebaseMessagingService", "onMessageReceived", "NOTIFICATION: " + remoteMessage.getNotification().getBody());
            Notifications.showFCMNotification(1,
                    App.getAppCtx().getResources().getString(R.string.app_name),
                    remoteMessage.getNotification().getBody(),
                    null);
        }
    }
}