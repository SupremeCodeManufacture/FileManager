package logic.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.SupremeManufacture.filemanager.BuildConfig;
import com.SupremeManufacture.filemanager.R;
import com.snatik.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

import data.App;
import data.CustomFile;
import data.GenericConstants;
import data.SimpleFileWrapper;
import view.custom.Dialogs;

/**
 * Created by abarova on 30.11.18.
 */
public class FileUtils {

    public static List<SimpleFileWrapper> getFileListByDirPath(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files == null) {
            return new ArrayList<>();
        }

        List<SimpleFileWrapper> result = new ArrayList<>();
        for (File file : files) {
            result.add(new SimpleFileWrapper(file, false));
        }

        Collections.sort(result, new FileComparator());
        return result;
    }

    public static LinkedList<CustomFile> getFilesByType(String path, String pattern, String catName, long sinceTime) {
        LinkedList<CustomFile> inFiles = new LinkedList<>();

        Queue<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(new File(path).listFiles(new TimePatternFilter(Pattern.compile(pattern), sinceTime))));

        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles(new TimePatternFilter(Pattern.compile(pattern), sinceTime))));

            } else if (!file.isHidden()) {
                int listSize = inFiles.size();

                if (listSize == 0) {
                    inFiles.add(new CustomFile(catName, pattern, file, false));

                } else {
                    inFiles.add(new CustomFile(null, pattern, file, false));
                }

                if (inFiles.size() == 3)
                    break;
            }
        }

        Collections.sort(inFiles, new Comparator<CustomFile>() {
            @Override
            public int compare(CustomFile file1, CustomFile file2) {
                long k = file1.getFile().lastModified() - file2.getFile().lastModified();
                if (k > 0) {
                    return 1;
                } else if (k == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        return inFiles;
    }

    public static LinkedList<SimpleFileWrapper> getAllFilesByType(String path, String pattern) {
        LinkedList<SimpleFileWrapper> inFiles = new LinkedList<>();

        Queue<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(new File(path).listFiles(new PatternOnlyFilter(Pattern.compile(pattern)))));

        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles(new PatternOnlyFilter(Pattern.compile(pattern)))));

            } else {
                inFiles.add(new SimpleFileWrapper(file, false));
            }
        }

        Collections.sort(inFiles, new Comparator<SimpleFileWrapper>() {
            @Override
            public int compare(SimpleFileWrapper file1, SimpleFileWrapper file2) {
                long k = file1.getFile().lastModified() - file2.getFile().lastModified();
                if (k > 0) {
                    return 1;
                } else if (k == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        return inFiles;
    }


    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static boolean moveFileNative(String fromPath, String toPasth) {
        try {
            return new File(fromPath).renameTo(new File(toPasth));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean moveFileLib(String fromPath, String toPasth) {
        try {
            return new Storage(App.getAppCtx()).move(fromPath, toPasth);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean copyFileLib(String fromPath, String toPasth) {
        try {
            return new Storage(App.getAppCtx()).copy(fromPath, toPasth);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean removeFile(String filePath) {
        try {
//            return App.getAppCtx().deleteFile(filePath);
            return new File(filePath).delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean renameFile(String fromPath, String toPasth) {
        try {
            //MyLogs.LOG("FileUtils", "renameFile", "fromPath: " + fromPath + " toPasth: " + toPasth);
            return new File(fromPath).renameTo(new File(toPasth));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String getFileMime(File file) {
        String mime = null;

        try {
            mime = URLConnection.guessContentTypeFromStream(new FileInputStream(file));

            if (mime == null) mime = URLConnection.guessContentTypeFromName(file.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mime;
    }


    public static void openFile(Context context, File file) {
        String mime = getFileMime(file);

        if (mime != null && file.canRead()) {
            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                myIntent.setDataAndType(getFileUri(file), mime);
                myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                myIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                context.startActivity(myIntent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, App.getAppCtx().getResources().getString(R.string.err_open_file), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(context, App.getAppCtx().getResources().getString(R.string.err_open_file), Toast.LENGTH_LONG).show();
        }
    }

    public static Uri getFileUri(File file) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return FileProvider.getUriForFile(App.getAppCtx(), BuildConfig.APPLICATION_ID + ".provider", file);

        return Uri.fromFile(file);
    }

    public static void shareFile(Context context, String filePath) {
        try {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(filePath);

            if (fileWithinMyDir.exists()) {
                String mime = getFileMime(fileWithinMyDir);

                if (mime != null) {
                    intentShareFile.setType(mime);
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, getFileUri(fileWithinMyDir));
                    context.startActivity(intentShareFile);

                } else {
                    Toast.makeText(context, App.getAppCtx().getResources().getString(R.string.err_share_file), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(context, App.getAppCtx().getResources().getString(R.string.err_share_file), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, App.getAppCtx().getResources().getString(R.string.err_share_file), Toast.LENGTH_LONG).show();
        }
    }

    public static void showFileDetails(Activity context, String filePath) {
        try {
            File fileWithinMyDir = new File(filePath);

            if (fileWithinMyDir.exists()) {
                Dialogs.showShareToUserDialog(context, fileWithinMyDir);
            }

        } catch (Exception e) {
            Toast.makeText(context, App.getAppCtx().getResources().getString(R.string.err_det_file), Toast.LENGTH_LONG).show();
        }
    }


    public static String getSdCardPath() {
        try {
            final File[] appsDir = ContextCompat.getExternalFilesDirs(App.getAppCtx(), null);
            for (final File file : appsDir) {
                String path = file.getAbsolutePath();
                int start = path.indexOf("/Android/");
                String prefix = path.substring(0, start);

                if (!prefix.equals(GenericConstants.EXTRA_ALL_INTERNAL_FILES_PATH))
                    return prefix;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
