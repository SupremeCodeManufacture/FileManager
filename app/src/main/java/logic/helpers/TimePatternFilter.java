package logic.helpers;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.regex.Pattern;

public class TimePatternFilter implements FileFilter, Serializable {

    private Pattern mPattern;
    private long mTimeSince;

    public TimePatternFilter(Pattern pattern, long timeSince) {
        this.mPattern = pattern;
        this.mTimeSince = timeSince;
    }

    @Override
    public boolean accept(File f) {
        return !f.isHidden() && (f.isDirectory() || (f.lastModified() > mTimeSince && mPattern.matcher(f.getName()).matches()));
    }
}