package logic.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import data.App;

/**
 * Created by vasil on 09-Feb-17.
 */

public class PermissionsHelper {

    public final static int ACTION_MANAGE_STORE_PERMISSION_CODE = 5467;

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean areAllPermisionsGranted(int[] resultsPermissions) {
        boolean response = true;

        for (int isGranted : resultsPermissions) {
            if (isGranted != PackageManager.PERMISSION_GRANTED) {
                response = false;
            }
        }

        return response;
    }

}
