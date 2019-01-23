package logic.push_notification;

import android.net.Uri;
import android.os.Build;

import com.SupremeManufacture.filemanager.R;
import com.google.gson.Gson;

import java.net.URLDecoder;


public class DataFormatConverter {


    public static CloudDataObj getObjFromJson(String json) {
        try {
            return new Gson().fromJson(json, CloudDataObj.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

    public static Uri safeConvertUrlToUri(String url) {
        try {
            return Uri.parse(URLDecoder.decode(url, "UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}