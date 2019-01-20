package logic.helpers;

import android.content.Context;
import android.util.Log;

import data.App;

public class MyLogs {

    public static void LOG(String theClass, String theMethod, String theComment) {
        Log.d("!!! MyLogs !!!", "class: " + theClass + " meth : " + theMethod + " comm : " + theComment);
    }
}
