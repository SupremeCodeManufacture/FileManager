package view.custom;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.SupremeManufacture.filemanager.R;

import data.App;
import data.GenericConstants;
import logic.push_notification.DataFormatConverter;
import view.activity.SplashScreenActivity;

import static android.content.Context.NOTIFICATION_SERVICE;


public class Notifications {

    private static Context ctx = App.getAppCtx();
    private static final int FAKE_PID = 123;
    private static String channelId = "id123";


    public static void showFCMNotification(int pid, String from, String messageBody, Uri googlePlayUri) {
        //MyLogs.LOG("Notifications", "showFCMNotification", "messageBody: " + messageBody + " googlePlayUri: " + googlePlayUri);
        PendingIntent pendingIntent;

        if (googlePlayUri != null) {
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, googlePlayUri);
            pendingIntent = PendingIntent.getActivity(ctx, (int) System.currentTimeMillis() / 1000, goToMarket, PendingIntent.FLAG_ONE_SHOT);

        } else {
            Intent intent = new Intent(ctx, SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(ctx, (int) System.currentTimeMillis() / 1000, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        if (from == null)
            from = ctx.getResources().getString(R.string.app_name);

        if (messageBody == null)
            messageBody = ctx.getResources().getString(R.string.txt_def_notific);

        showNotific(from, messageBody, pid, pendingIntent);
    }

    public static void fakePromo(String from, String messageBody, Uri googlePlayUri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && googlePlayUri != null) {
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, googlePlayUri);
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx, (int) System.currentTimeMillis() / 1000, goToMarket, PendingIntent.FLAG_ONE_SHOT);

            if (from == null)
                from = ctx.getResources().getString(R.string.txt_fake_from);

            if (messageBody == null)
                messageBody = ctx.getResources().getString(R.string.txt_fake_msg);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, channelId)
                    .setSmallIcon(R.mipmap.ic_fake_img)
                    .setContentTitle(from)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = setupNotifChannel();
            if (notificationManager != null)
                notificationManager.notify(FAKE_PID, mBuilder.build());
        }
    }

    public static void showNotificForUpgrade(int pid, String from, String messageBody) {
        Intent intent = new Intent(ctx, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(GenericConstants.EXTRA_NEED_UPGRADE, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, (int) System.currentTimeMillis() / 1000, intent, PendingIntent.FLAG_ONE_SHOT);

        if (from == null)
            from = ctx.getResources().getString(R.string.app_name);

        if (messageBody == null)
            messageBody = ctx.getResources().getString(R.string.txt_def_notific);

        showNotific(from, messageBody, pid, pendingIntent);
    }


    private static void showNotific(String from, String messageBody, int pid, PendingIntent pendingIntent) {
        NotificationManager notificationManager = setupNotifChannel();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(DataFormatConverter.getNotificationIcon())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            mBuilder.setContentTitle(from);

        if (notificationManager != null)
            notificationManager.notify(pid, mBuilder.build());
    }

    private static NotificationManager setupNotifChannel() {
        NotificationManager notificationManager = (NotificationManager) App.getAppCtx().getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId,
                    "Pro channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Register the channel with the system
            if (notificationManager != null) {
                //MyLogs.LOG("Notifications", "setupNotifChannel", "OK setup");
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        return notificationManager;
    }

}