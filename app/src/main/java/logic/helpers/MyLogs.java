package logic.helpers;

import android.util.Log;

public class MyLogs {

    public static void LOG(String theClass, String theMethod, String theComment) {
        Log.d("!!! MyLogs !!!", "class: " + theClass + " meth : " + theMethod + " comm : " + theComment);
    }
}