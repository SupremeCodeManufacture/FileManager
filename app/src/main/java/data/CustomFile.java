package data;

import java.io.File;

public class CustomFile {

    private String catName;
    private String patternRegex;
    private File file;
    private boolean selected;


    public CustomFile(String name, String pattern, File file, boolean selected) {
        this.catName = name;
        this.patternRegex = pattern;
        this.file = file;
        this.selected = selected;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPatternRegex() {
        return patternRegex;
    }

    public void setPatternRegex(String patternRegex) {
        this.patternRegex = patternRegex;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
