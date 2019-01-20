package data;

public class ThemeObj {

    private int themeId;
    private int primaryColor;
    private int primaryDarkColor;
    private int accentColor;

    public ThemeObj(int id, int primary, int dark, int accent) {
        this.themeId = id;
        this.primaryColor = primary;
        this.primaryDarkColor = dark;
        this.accentColor = accent;
    }


    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public void setPrimaryDarkColor(int primaryDarkColor) {
        this.primaryDarkColor = primaryDarkColor;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
    }
}
