package com.viewlift.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.downloads.DownloadVideoRealm;
import com.viewlift.models.data.appcms.downloads.RealmController;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class for storage utilities
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    private static final String[] physicalPaths = {
            "/storage/sdcard0", "/storage/sdcard1", // Motorola Xoom
            "/storage/extsdcard", // Samsung SGS3
            "/storage/sdcard0/external_sdcard", // User request
            "/mnt/extsdcard", "/mnt/sdcard/external_sd", // Samsung galaxy family
            "/mnt/external_sd", "/mnt/media_rw/sdcard1", // 4.4.2 on CyanogenMod S3
            "/removable/microsd", // Asus transformer prime
            "/mnt/emmc", "/storage/external_SD", // LG
            "/storage/ext_sd", // HTC One Max
            "/storage/removable/sdcard1", // Sony Xperia Z1
            "/data/sdext", "/data/sdext2", "/data/sdext3", "/data/sdext4", "/sdcard1", // Sony Xperia Z
            "/sdcard2", // HTC One M8s
            "/storage/microsd" // ASUS ZenFone 2
    };

    /**
     * Description : finds the storage option available in device
     *
     * @param context Context
     * @return array of storage locations available on device
     */
    public static String[] getStorageDirectories(Context context) {
        HashSet<String> paths = new HashSet<>();
        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                List<String> results = new ArrayList<>();
                File[] externalDirs = context.getExternalFilesDirs(null);
                for (File file : externalDirs) {
                    String path;
                    try {
                        path = file.getAbsolutePath();
                    } catch (Exception e) {
                        path = null;
                        if( e.getMessage()!=null)
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                    if (path != null) {
                        if (Environment.isExternalStorageRemovable(file) || rawSecondaryStoragesStr != null && rawSecondaryStoragesStr.contains(path)) {
                            results.add(path);
                        }
                    }
                }

                paths.addAll(results);

            } else {
                if (TextUtils.isEmpty(rawExternalStorage)) {
                    paths.addAll(Arrays.asList(physicalPaths));
                } else {
                    paths.add(rawExternalStorage);
                }
            }
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();

            String[] folders = Pattern.compile("/").split(path);
            String lastFolder = folders[folders.length - 1];
            boolean isDigit = false;
            try {
                Integer.valueOf(lastFolder);
                isDigit = true;
            } catch (NumberFormatException ignored) {
            }

            String rawUserId = isDigit ? lastFolder : "";
            if (TextUtils.isEmpty(rawUserId)) {
                paths.add(rawEmulatedStorageTarget);
            } else {
                paths.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }

        return paths.toArray(new String[paths.size()]);
    }


    /**
     * Description : Calculate the space available in storage location in MB.
     *
     * @param context                  Context
     * @param userDownloadLocationPref if user preferred location is available
     * @return available space on user-preferred/default location
     */
    public static long getMegabytesAvailable(Context context, boolean userDownloadLocationPref) {
        File storagePath;
        if (!userDownloadLocationPref) {
            storagePath = Environment.getExternalStorageDirectory();
        } else {
            storagePath = new File(getStorageDirectories(context)[0]);
        }
        return getMegabytesAvailable(storagePath);
    }

    /**
     * Description : Calculate the space available on given path location in MB.
     *
     * @param file storage location
     * @return size of available space in MB
     */
    public static long getMegabytesAvailable(File file) {
        long bytesAvailable = 0L;
        try {
            StatFs stat = new StatFs(file.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
            } else {
                bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
            }
        } catch (Exception e) {
            Log.d(TAG, ":"+e);
            e.printStackTrace();
        }
        return bytesAvailable / (1024L * 1024L);
    }


    /**
     * Description : finds if any removable sd card available in device
     *
     * @param context Context
     * @return if removable sd card available in device
     */
    public static boolean isRemovableSDCardAvailable(Context context) {
        return context != null && getStorageDirectories(context).length >= 1;
    }

    /**
     * Description : Returns absolute path of given dir if available otherwise creates and return
     *
     * @param context Context
     * @param dirName directory name
     * @return absolute path of given dir name
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getSDCardPath(Context context, String dirName) {
        String dirPath = getSDCardPath(context) + File.separator + dirName;
        File dir = new File(dirPath);
        if (!dir.isDirectory())
            dir.mkdirs();

        return dir.getAbsolutePath();

    }

    /**
     * Description : returns the absolute path of storage location
     *
     * @param context Context
     * @return sd card path
     */
    private static String getSDCardPath(Context context) {
        File baseSDCardDir;
        String[] dirs = getStorageDirectories(context);
        baseSDCardDir = new File(dirs[0]);

        return baseSDCardDir.getAbsolutePath();
    }

    /**
     * Description : Finds and returns if memory available after queued downloads
     *
     * @param context       Context
     * @param appPreference AppPreference instance
     * @return return if space available after queued downloads
     */
    public static boolean isMemorySpaceAvailable(Context context, AppPreference appPreference) {
        File storagePath;
        if (getStorageDirectories(context).length > 0) {
            try {
                storagePath = new File(getStorageDirectories(context)[0]);
            } catch (Exception e) {
                appPreference.setUserDownloadLocationPref(false);
                storagePath = Environment.getExternalStorageDirectory();
                Log.e(TAG, e.getMessage());
            }
        } else {
            appPreference.setUserDownloadLocationPref(false);
            storagePath = Environment.getExternalStorageDirectory();
        }
        storagePath.mkdirs();
        return getMegabytesAvailable(storagePath) > getRemainingDownloadSize(context, appPreference);
    }

    /**
     * Description : Checkes if external storage available in device.
     *
     * @param context activity context
     * @return boolean value indicating if external storage.
     */
    public static boolean isExternalStorageAvailable(Context context) {
        return getStorageDirectories(context).length > 0;
    }

    /**
     * Description : returns the remaining download
     *
     * @param context       Context
     * @param appPreference AppPreference object
     * @return remaining download size of files being downloaded in MB
     */
    public static long getRemainingDownloadSize(Context context, AppPreference appPreference) {
        if (context != null) {
            RealmController realmController = RealmController.with((Activity) context);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (realmController != null) {
                List<DownloadVideoRealm> remainDownloads = realmController.getAllUnfinishedDownloades(appPreference.getLoggedInUser());
                long bytesRemainDownload = 0L;
                if (remainDownloads != null && remainDownloads.size() > 0) {
                    for (DownloadVideoRealm downloadVideoRealm : remainDownloads) {

                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(downloadVideoRealm.getVideoId_DM());
                        Cursor c = downloadManager.query(query);
                        if (c != null) {
                            if (c.moveToFirst()) {
                                long totalSize = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                long downloaded = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                totalSize -= downloaded;
                                bytesRemainDownload += totalSize;
                            }
                            c.close();
                        }

                    }
                    return bytesRemainDownload / (1024L * 1024L);
                }
            }
        }
        return 0L;
    }

    public static long getOfflineFileSizeMB(long size) {
        String fileSize;
        DecimalFormat dec = new DecimalFormat("0");
        long sizeKB = (size / 1024);
        long megaByte = (long) (sizeKB / 1024.0);
        return megaByte;
    }

    public static boolean isMemoryOfflineAvailable(Context context, AppPreference appPreference, long videoContentLength ) {
        File storagePath;
        if (getStorageDirectories(context).length > 0) {
            try {
                storagePath = new File(getStorageDirectories(context)[0]);
            } catch (Exception e) {
                appPreference.setUserDownloadLocationPref(false);
                storagePath = Environment.getExternalStorageDirectory();
                Log.e(TAG, e.getMessage());
            }
        } else {
            appPreference.setUserDownloadLocationPref(false);
            storagePath = Environment.getExternalStorageDirectory();
        }
        return getMegabytesAvailable(storagePath) > getOfflineFileSizeMB(videoContentLength);
    }

}
