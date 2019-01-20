package logic.helpers;

import com.SupremeManufacture.filemanager.R;

import java.util.ArrayList;
import java.util.List;

import data.App;
import data.GenericConstants;
import data.ThemeObj;

public class ThemeColorsHelper {

    public static int getTheme() {
        switch (App.getSelectedTheme()) {
            case GenericConstants.KEY_SELECTED_THEME_1:
                return R.style.AppTheme1;

            case GenericConstants.KEY_SELECTED_THEME_2:
                return R.style.AppTheme2;

            case GenericConstants.KEY_SELECTED_THEME_3:
                return R.style.AppTheme3;

            case GenericConstants.KEY_SELECTED_THEME_4:
                return R.style.AppTheme4;

            case GenericConstants.KEY_SELECTED_THEME_5:
                return R.style.AppTheme5;
        }

        return R.style.AppTheme0;
    }

    public static int getNoTitleTheme() {
        switch (App.getSelectedTheme()) {
            case GenericConstants.KEY_SELECTED_THEME_1:
                return R.style.AppTheme1_NoActionBar;

            case GenericConstants.KEY_SELECTED_THEME_2:
                return R.style.AppTheme2_NoActionBar;

            case GenericConstants.KEY_SELECTED_THEME_3:
                return R.style.AppTheme3_NoActionBar;

            case GenericConstants.KEY_SELECTED_THEME_4:
                return R.style.AppTheme4_NoActionBar;

            case GenericConstants.KEY_SELECTED_THEME_5:
                return R.style.AppTheme5_NoActionBar;
        }

        return R.style.AppTheme0_NoActionBar;
    }


    public static int getColorPrimary() {
        switch (App.getSelectedTheme()) {
            case GenericConstants.KEY_SELECTED_THEME_1:
                return R.color.primary_1;

            case GenericConstants.KEY_SELECTED_THEME_2:
                return R.color.primary_2;

            case GenericConstants.KEY_SELECTED_THEME_3:
                return R.color.primary_3;

            case GenericConstants.KEY_SELECTED_THEME_4:
                return R.color.primary_4;

            case GenericConstants.KEY_SELECTED_THEME_5:
                return R.color.primary_5;
        }

        return R.color.primary_0;
    }

    public static int getColorPrimaryLight() {
        switch (App.getSelectedTheme()) {
            case GenericConstants.KEY_SELECTED_THEME_1:
                return R.color.primary_light_1;

            case GenericConstants.KEY_SELECTED_THEME_2:
                return R.color.primary_light_2;

            case GenericConstants.KEY_SELECTED_THEME_3:
                return R.color.primary_light_3;

            case GenericConstants.KEY_SELECTED_THEME_4:
                return R.color.primary_light_4;

            case GenericConstants.KEY_SELECTED_THEME_5:
                return R.color.primary_light_5;
        }

        return R.color.primary_light_0;
    }

    public static int getColorPrimary50() {
        switch (App.getSelectedTheme()) {
            case GenericConstants.KEY_SELECTED_THEME_1:
                return R.color.primary_50_1;

            case GenericConstants.KEY_SELECTED_THEME_2:
                return R.color.primary_50_2;

            case GenericConstants.KEY_SELECTED_THEME_3:
                return R.color.primary_50_3;

            case GenericConstants.KEY_SELECTED_THEME_4:
                return R.color.primary_50_4;

            case GenericConstants.KEY_SELECTED_THEME_5:
                return R.color.primary_50_5;
        }

        return R.color.primary_50_0;
    }

    public static int getColorPrimaryLight50() {
        switch (App.getSelectedTheme()) {
            case GenericConstants.KEY_SELECTED_THEME_1:
                return R.color.primary_light_50_1;

            case GenericConstants.KEY_SELECTED_THEME_2:
                return R.color.primary_light_50_2;

            case GenericConstants.KEY_SELECTED_THEME_3:
                return R.color.primary_light_50_3;

            case GenericConstants.KEY_SELECTED_THEME_4:
                return R.color.primary_light_50_4;

            case GenericConstants.KEY_SELECTED_THEME_5:
                return R.color.primary_light_50_5;
        }

        return R.color.primary_light_50_0;
    }

    public static int getColorPrimaryDark() {
        switch (App.getSelectedTheme()) {
            case GenericConstants.KEY_SELECTED_THEME_1:
                return R.color.primary_dark_1;

            case GenericConstants.KEY_SELECTED_THEME_2:
                return R.color.primary_dark_2;

            case GenericConstants.KEY_SELECTED_THEME_3:
                return R.color.primary_dark_3;

            case GenericConstants.KEY_SELECTED_THEME_4:
                return R.color.primary_dark_4;

            case GenericConstants.KEY_SELECTED_THEME_5:
                return R.color.primary_dark_5;
        }

        return R.color.primary_dark_0;
    }


    public static List<ThemeObj> getThemesData() {
        List<ThemeObj> list = new ArrayList<>();
        list.add(new ThemeObj(0, R.color.primary_0, R.color.primary_dark_0, R.color.accent_0));
        list.add(new ThemeObj(1, R.color.primary_1, R.color.primary_dark_1, R.color.accent_1));
        list.add(new ThemeObj(2, R.color.primary_2, R.color.primary_dark_2, R.color.accent_2));
        list.add(new ThemeObj(3, R.color.primary_3, R.color.primary_dark_3, R.color.accent_3));
        list.add(new ThemeObj(4, R.color.primary_4, R.color.primary_dark_4, R.color.accent_4));
        list.add(new ThemeObj(5, R.color.primary_5, R.color.primary_dark_5, R.color.accent_5));

        return list;
    }
}