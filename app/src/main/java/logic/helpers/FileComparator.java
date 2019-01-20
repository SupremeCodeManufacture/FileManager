package logic.helpers;

import java.io.File;
import java.util.Comparator;

import data.SimpleFileWrapper;

/**
 * Created by abarova on 30.11.18.
 */
public class FileComparator implements Comparator<SimpleFileWrapper> {
    @Override
    public int compare(SimpleFileWrapper f1, SimpleFileWrapper f2) {
        if (f1 == f2) {
            return 0;
        }
        if (f1.getFile().isDirectory() && f2.getFile().isFile()) {
            // Show directories above files
            return -1;
        }
        if (f1.getFile().isFile() && f2.getFile().isDirectory()) {
            // Show files below directories
            return 1;
        }
        // Sort the directories alphabetically
        return f1.getFile().getName().compareToIgnoreCase(f2.getFile().getName());
    }
}
