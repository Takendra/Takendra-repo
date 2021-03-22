package com.viewlift.models.network.rest;

import androidx.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.viewlift.models.data.appcms.ui.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by viewlift on 5/9/17.
 */

public class AppCMSResourceCall {
    private static final String TAG = "AppCMSPageUICall";

    private final AppCMSPageUIRest appCMSPageUIRest;
    private final Gson gson;
    private final File storageDirectory;

    @Inject
    public AppCMSResourceCall(AppCMSPageUIRest appCMSPageUIRest,
                              Gson gson,
                              File storageDirectory) {
        this.appCMSPageUIRest = appCMSPageUIRest;
        this.gson = gson;
        this.storageDirectory = storageDirectory;
    }

    @WorkerThread
    public boolean writeToFile(Resources resources, String url) {
        String filename = getResourceFilename(url);
        try {
            writePageToFile(filename, resources);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to write AppCMSPageUI file: " +
                    url);
        }
        return false;
    }

    @WorkerThread
    public Resources call(String url, String xApiKey, boolean bustCache, boolean loadFromFile) throws IOException {
        String filename = getResourceFilename(url);

        Resources appCMSPageUI = null;
        if (loadFromFile) {
            try {
                appCMSPageUI = readPageFromFile(filename);
                if(appCMSPageUI == null || (appCMSPageUI != null
                        && appCMSPageUI.getResourcesMap() == null)){
                    StringBuilder urlWithCacheBuster = new StringBuilder(url);
                    //urlWithCacheBuster.append("?x=");
                    //urlWithCacheBuster.append(new Date().getTime());
                    appCMSPageUI = loadFromNetwork(urlWithCacheBuster.toString(), filename, xApiKey);
                    System.out.println("Language Url is = "+urlWithCacheBuster.toString());
                }
                appCMSPageUI.setLoadedFromNetwork(false);
            } catch (Exception e) {
                Log.e(TAG, "Error reading file AppCMS UI JSON file: " + e.getMessage());
                try {
                    if (bustCache) {
                        StringBuilder urlWithCacheBuster = new StringBuilder(url);
                        //urlWithCacheBuster.append("?x=");
                        //urlWithCacheBuster.append(new Date().getTime());
                        appCMSPageUI = loadFromNetwork(urlWithCacheBuster.toString(), filename, xApiKey);
                    } else {
                        appCMSPageUI = loadFromNetwork(url, filename, xApiKey);
                    }
                } catch (Exception e2) {
                    //Log.e(TAG, "A last ditch effort to download the AppCMS UI JSON did not succeed: " +
//                        e2.getMessage());
                }
            }
        } else {
            if (bustCache) {
                StringBuilder urlWithCacheBuster = new StringBuilder(url);
               // urlWithCacheBuster.append("?x=");
                //urlWithCacheBuster.append(new Date().getTime());
                appCMSPageUI = loadFromNetwork(urlWithCacheBuster.toString(), filename, xApiKey);
            } else {
                appCMSPageUI = loadFromNetwork(url, filename, xApiKey);
            }

            if (appCMSPageUI == null) {
                try {
                    appCMSPageUI = readPageFromFile(filename);
                    appCMSPageUI.setLoadedFromNetwork(false);
                } catch (Exception e) {
                    Log.e(TAG, "Error reading file AppCMS UI JSON file: " + e.getMessage());
                }
            }
        }
        return appCMSPageUI;
    }

    private Resources loadFromNetwork(String url, String filename, String xApiKey) {
        Resources appCMSPageUI = null;
        try {


            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("x-api-key", xApiKey);
            Log.d(TAG,url);
            deletePreviousFiles(url);
            appCMSPageUI = writePageToFile(filename, appCMSPageUIRest.getLanguageResourceFile(url, authTokenMap)
                    .execute().body());
            appCMSPageUI.setLoadedFromNetwork(true);
        } catch (Exception e) {
            Log.e(TAG, "Error receiving network data " + url + ": " + e.getMessage());
        }
        return appCMSPageUI;
    }

    private Resources writePageToFile(String outputFilename, Resources appCMSPageUI) throws IOException {
        OutputStream outputStream = new FileOutputStream(
                new File(storageDirectory.toString() +
                        File.separatorChar +
                        outputFilename));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(appCMSPageUI);
        outputStream.close();
        return appCMSPageUI;
    }

    private void deletePreviousFiles(String url) {
        String fileToDeleteFilenamePattern = getResourceFilenameWithJsonOnly(url);
        if (storageDirectory.isDirectory()) {
            String[] listExistingFiles = storageDirectory.list();
            for (String existingFilename : listExistingFiles) {
                if (existingFilename.contains(fileToDeleteFilenamePattern)) {
                    File fileToDelete = new File(storageDirectory, existingFilename);
                    try {
                        if (fileToDelete.delete()) {
                            //Log.i(TAG, "Successfully deleted pre-existing file: " + fileToDelete);
                        } else {
                            //Log.e(TAG, "Failed to delete pre-existing file: " + fileToDelete);
                        }
                    } catch (Exception e) {
                        //Log.e(TAG, "Failed to delete pre-existing file: " + fileToDelete);
                    }
                }
            }
        }
    }

    private Resources readPageFromFile(String inputFilename) throws Exception {
        InputStream inputStream = new FileInputStream(new File(storageDirectory.toString() +
                        File.separatorChar +
                        inputFilename));
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Resources appCMSPageUI = (Resources) objectInputStream.readObject();
        inputStream.close();
        return appCMSPageUI;
    }



    private String getResourceFilenameWithJsonOnly(String url) {
        final String JSON_EXT = ".json";
        int startIndex = url.lastIndexOf(File.separatorChar);
        int endIndex = url.indexOf(JSON_EXT) + JSON_EXT.length();
        if (0 <= startIndex && startIndex < endIndex) {
            return url.substring(startIndex + 1, endIndex);
        }
        return url;
    }

    private String getResourceFilename(String url) {
        try {
            String[] atrArray = url.split(File.separator);
            String languageCode = atrArray[atrArray.length - 2];
            String fileName = atrArray[atrArray.length - 1];
            String[] fnameArray = fileName.split("\\?");
            String finalFileName = languageCode + "_" + fnameArray[0];
            return finalFileName;
        } catch (Exception e) {
            return url;
        }
    }
}