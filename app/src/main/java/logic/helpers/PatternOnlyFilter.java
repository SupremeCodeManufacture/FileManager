package logic.helpers;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.regex.Pattern;

public class PatternOnlyFilter implements FileFilter, Serializable {

    private Pattern mPattern;

    public PatternOnlyFilter(Pattern pattern) {
        this.mPattern = pattern;
    }

    @Override
    public boolean accept(File f) {
        return !f.isHidden() && (f.isDirectory() || mPattern.matcher(f.getName()).matches());
    }
}