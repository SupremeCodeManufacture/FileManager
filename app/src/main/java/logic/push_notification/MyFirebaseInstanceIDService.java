package logic.push_notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import data.GenericConstants;
import logic.helpers.MyLogs;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       //MyLogs.LOG("MyFirebaseInstanceIDService", "onTokenRefresh", "tk: " + refreshedToken);

        //subscribe to notifications topic
        if (FirebaseInstanceId.getInstance().getToken() != null) {
            FirebaseMessaging.getInstance().subscribeToTopic(GenericConstants.TOPIC_ALL);
           //MyLogs.LOG("MyFirebaseInstanceIDService", "onTokenRefresh", "subscribe to topic: " + GenericConstants.TOPIC_ALL);
        }
    }
}