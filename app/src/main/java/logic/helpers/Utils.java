package logic.helpers;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.format.DateFormat;

import com.SupremeManufacture.filemanager.R;

import java.util.Calendar;

import data.App;
import data.GenericConstants;

public class Utils {

    public static String getPrettyDate(long timeMilis) {
        Calendar nowTime = Calendar.getInstance();
        Calendar neededTime = Calendar.getInstance();
        neededTime.setTimeInMillis(timeMilis);

        //validate if time is between -4/+4 years from current time
        if (neededTime.get(Calendar.YEAR) < nowTime.get(Calendar.YEAR) + 5 && neededTime.get(Calendar.YEAR) > nowTime.get(Calendar.YEAR) - 5)
            try {
                if ((neededTime.get(Calendar.YEAR) == nowTime.get(Calendar.YEAR))) {

                    if ((neededTime.get(Calendar.MONTH) == nowTime.get(Calendar.MONTH))) {
                        if (neededTime.get(Calendar.DATE) - nowTime.get(Calendar.DATE) == 1) {
                            return App.getAppCtx().getResources().getString(R.string.txt_tomorrow) + ", " + DateFormat.format("HH:mm", neededTime);

                        } else if (nowTime.get(Calendar.DATE) == neededTime.get(Calendar.DATE)) {
                            return App.getAppCtx().getResources().getString(R.string.txt_today) + ", " + DateFormat.format("HH:mm", neededTime);

                        } else if (nowTime.get(Calendar.DATE) - neededTime.get(Calendar.DATE) == 1) {
                            return App.getAppCtx().getResources().getString(R.string.txt_yesterd) + ", " + DateFormat.format("HH:mm", neededTime);

                        } else {
                            return DateFormat.format("d MMMM, HH:mm", neededTime).toString();
                        }

                    } else {
                        return DateFormat.format("d MMMM, HH:mm", neededTime).toString();
                    }

                } else {
                    return DateFormat.format("dd MMMM yyyy, HH:mm", neededTime).toString();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        return App.getAppCtx().getResources().getString(R.string.txt_unknw_time);
    }

    public static Drawable getTintedDrawable(int alphaIconResId) {
        Drawable drawable = App.getAppCtx().getResources().getDrawable(alphaIconResId);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(App.getAppCtx(), ThemeColorsHelper.getColorPrimary50()));
        return drawable;
    }

    public static boolean isPassedAdsFree() {
        return System.currentTimeMillis() > (App.getFirstLaunchMilis() + GenericConstants.THREE_DAYS_IN_MILIS);
    }
}
