package com.viewlift.models.network.rest;

import android.content.Context;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.google.gson.Gson;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.api.AppCMSParentalRatingMapResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;

public class AppCMSParentalRatingMapCall {

    private static final String TAG = "ParentalRatingMap";

    private final AppCMSParentalRatingMapRest appCMSParentalRatingMapRest;

    @SuppressWarnings({"unused, FieldCanBeLocal"})
    private final Gson gson;
    private final File storageDirectory;

    @Inject
    public AppCMSParentalRatingMapCall(AppCMSParentalRatingMapRest appCMSParentalRatingMapRest, Gson gson, File storageDirectory) {
        this.appCMSParentalRatingMapRest = appCMSParentalRatingMapRest;
        this.gson = gson;
        this.storageDirectory = storageDirectory;
    }

    @WorkerThread
    public List<AppCMSParentalRatingMapResponse> call(Context context, int tryCount, boolean networkDisconnected) {
        final String appCMSParentalRatingMapUrl = context.getString(R.string.app_cms_rating_map_api, Utils.getProperty("BaseUrl", context));
        List<AppCMSParentalRatingMapResponse> ratingMapResponse = null;
        try {
            Log.d(TAG, "Attempting to retrieve rating-map.json: " + appCMSParentalRatingMapUrl);
            if (!networkDisconnected) {
                try {
                    ratingMapResponse = appCMSParentalRatingMapRest.getRatingMap(appCMSParentalRatingMapUrl).execute().body();
                } catch (Exception e) {
                    Log.w(TAG, "Failed to read rating-map.json from network: " + e.getMessage());
                }
            }

            final List<AppCMSParentalRatingMapResponse> mainFromNetwork = ratingMapResponse;
            List<AppCMSParentalRatingMapResponse> mainInStorage = null;
            String filename = getResourceFilename(appCMSParentalRatingMapUrl);
            try {
                mainInStorage = readMainFromFile(filename);
            } catch (Exception e) {
                Log.w(TAG, ":"+e);
            }

            if (ratingMapResponse != null) {
                try {
                    writeMainToFile(filename, mainFromNetwork);
                } catch (Exception e) {
                    Log.d(TAG, ":"+e);
                }
            } else if (mainInStorage != null) {
                ratingMapResponse = mainInStorage;
            }
        } catch (Exception e) {
            Log.e(TAG, "A serious error has occurred: " + e.getMessage());
        }

        if (ratingMapResponse == null && tryCount == 0) {
            return call(context, tryCount + 1, networkDisconnected);
        }

        return ratingMapResponse;
    }

    private void writeMainToFile(String outputFilename, List<AppCMSParentalRatingMapResponse> main) throws IOException {
        OutputStream outputStream = new FileOutputStream(new File(storageDirectory.toString() + File.separatorChar + outputFilename));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(main);
        outputStream.close();
    }

    private List<AppCMSParentalRatingMapResponse> readMainFromFile(String inputFilename) throws Exception {
        InputStream inputStream = new FileInputStream(storageDirectory.toString() + File.separatorChar + inputFilename);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        List<AppCMSParentalRatingMapResponse> main = (List<AppCMSParentalRatingMapResponse>) objectInputStream.readObject();
        inputStream.close();
        return main;
    }

    private String getResourceFilename(String url) {
        final String PATH_SEP = "/";
        final String JSON_EXT = ".json";
        int endIndex = url.indexOf(JSON_EXT) + JSON_EXT.length();
        int startIndex = url.lastIndexOf(PATH_SEP);
        if (0 <= startIndex && startIndex < endIndex) {
            return url.substring(startIndex + 1, endIndex);
        }
        return url;
    }
}
