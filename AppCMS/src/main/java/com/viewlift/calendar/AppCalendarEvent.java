package com.viewlift.calendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.Calendar;
import java.util.TimeZone;

import javax.inject.Inject;

import rx.functions.Action1;

public class AppCalendarEvent {
    Context context = null;
    private static final int REQUEST_WRITE_CALENDAR = 2003;
    private static final int REQUEST_READ_CALENDAR = 2004;

    @Inject
    public AppCalendarEvent(Context context) {
        this.context = context;
    }

    private void askForWriteCalendarPermission(boolean checkToShowPermissionRationale,
                                               final Action1<Boolean> calendarPermission, AppCMSPresenter appCMSPresenter) {
        if (!hasWriteCalendarPermission(appCMSPresenter.getCurrentActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (appCMSPresenter.getCurrentActivity() != null /*&& !hasWriteExternalStoragePermission()*/) {
                    if (checkToShowPermissionRationale && ActivityCompat.shouldShowRequestPermissionRationale(appCMSPresenter.getCurrentActivity(),
                            android.Manifest.permission.WRITE_CALENDAR)) {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.REQUEST_WRITE_CALENDAR,
                                appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_write_calendar_permission_rationale_message),
                                true,
                                () -> {
                                    try {
                                        askForWriteCalendarPermission(false, calendarPermission, appCMSPresenter);
                                    } catch (Exception e) {
                                        //Log.e(TAG, "Error handling request permissions result: " + e.getMessage());
                                    }
                                },
                                null, null);
                    } else {
                        ActivityCompat.requestPermissions(appCMSPresenter.getCurrentActivity(),
                                new String[]{android.Manifest.permission.WRITE_CALENDAR},
                                REQUEST_WRITE_CALENDAR);
                    }
                }
            }
        } else {
            calendarPermission.call(true);
        }

    }

    private boolean hasWriteCalendarPermission(Activity currentActivity) {
        if (currentActivity != null) {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                    (ContextCompat.checkSelfPermission(currentActivity,
                            Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED);
        }
        return false;
    }

    private boolean hasReadCalendarPermission(Activity currentActivity) {
        if (currentActivity != null) {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                    (ContextCompat.checkSelfPermission(currentActivity,
                            Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED);
        }
        return false;
    }

    private void askForReadCalendarPermission(boolean checkToShowPermissionRationale,
                                              final Action1<Boolean> calendarPermission, AppCMSPresenter appCMSPresenter) {
        if (!hasReadCalendarPermission(appCMSPresenter.getCurrentActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (appCMSPresenter.getCurrentActivity() != null) {
                    if (checkToShowPermissionRationale && ActivityCompat.shouldShowRequestPermissionRationale(appCMSPresenter.getCurrentActivity(),
                            Manifest.permission.READ_CALENDAR)) {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.REQUEST_WRITE_CALENDAR,
                                appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_write_calendar_permission_rationale_message),
                                true,
                                () -> {
                                    try {
                                        askForReadCalendarPermission(false, calendarPermission, appCMSPresenter);
                                    } catch (Exception e) {
                                        //Log.e(TAG, "Error handling request permissions result: " + e.getMessage());
                                    }
                                },
                                null, null);
                    } else {
                        ActivityCompat.requestPermissions(appCMSPresenter.getCurrentActivity(),
                                new String[]{android.Manifest.permission.READ_CALENDAR},
                                REQUEST_READ_CALENDAR);
                    }
                } else {
                    calendarPermission.call(true);
                }
            }
        } else {
            calendarPermission.call(true);
        }

    }

    private int getCalendarId() {
        Cursor cursor = null;
        ContentResolver contentResolver = context.getContentResolver();
        Uri calendars = CalendarContract.Calendars.CONTENT_URI;

        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT,                 // 3
                CalendarContract.Calendars.IS_PRIMARY                     // 4
        };

        int PROJECTION_ID_INDEX = 0;
        int PROJECTION_DISPLAY_NAME_INDEX = 2;
        int PROJECTION_VISIBLE = 4;
        cursor = contentResolver.query(calendars, EVENT_PROJECTION, null, null, null);

        if (cursor.moveToFirst()) {
            String calName;
            long calId = 0;
            String visible;
            do {
                calName = cursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
                calId = cursor.getLong(PROJECTION_ID_INDEX);
                visible = cursor.getString(PROJECTION_VISIBLE);
                Log.e("Calendar Id : ", "" + calId + " : " + calName + " : " + visible);
                if (visible.equals("1")) {
                    return (int) calId;
                }

            } while (cursor.moveToNext());

            return (int) calId;
        }
        return 1;
    }

    private void addEventToCalendar(ContentDatum contentDatum, AppCMSPresenter appCMSPresenter) {
        askForWriteCalendarPermission(true, new Action1<Boolean>() {
            @Override
            public void call(Boolean grantedPermission) {
                if (grantedPermission) {
                    ContentResolver cr = appCMSPresenter.getCurrentContext().getContentResolver();
                    ContentValues values = new ContentValues();

                    Calendar startDate = Calendar.getInstance();
                    startDate.setTimeInMillis(contentDatum.getGist().getScheduleStartDate());
                    Calendar endDate = Calendar.getInstance();
                    endDate.setTimeInMillis(contentDatum.getGist().getScheduleEndDate());

                    values.put(CalendarContract.Events.DTSTART, startDate.getTimeInMillis());
                    values.put(CalendarContract.Events.DTEND, endDate.getTimeInMillis());
                    values.put(CalendarContract.Events.TITLE, contentDatum.getGist().getTitle());
                    values.put(CalendarContract.Events.DESCRIPTION, contentDatum.getGist().getDescription());

                    values.put(CalendarContract.Events.CALENDAR_ID, getCalendarId());
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                    Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                    long eventID = Long.parseLong(uri.getLastPathSegment());
                    appCMSPresenter.getAppPreference().setCalendarEventId(eventID);
                    calenderAddToast = Toast.makeText(appCMSPresenter.getCurrentContext(), contentDatum.getGist().getTitle() + " added to your calendar.", Toast.LENGTH_SHORT);
                    calenderAddToast.show();
                }
            }
        }, appCMSPresenter);


    }

    public void checkCalendarEventExistsAndAddEvent(ContentDatum contentDatum, AppCMSPresenter appCMSPresenter) {
        askForReadCalendarPermission(true, new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    boolean calendarEventExist = false;
                    if (appCMSPresenter.getAppPreference().getCalendarEventId() != null) {
                        String[] calendarEventIds = appCMSPresenter.getAppPreference().getCalendarEventId().split(";");
                        for (int i = 0; i < calendarEventIds.length; i++) {
                            long eventID = Long.parseLong(calendarEventIds[i]);
                            Uri event = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                            Cursor cursor;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                cursor = appCMSPresenter.getCurrentContext().getContentResolver().query(event, null, null, null);
                            } else {
                                cursor = appCMSPresenter.getCurrentActivity().managedQuery(event, null, null, null, null);
                            }
                            if (cursor.moveToFirst()) {
                                do {
                                    if ((cursor.getInt(cursor.getColumnIndex(CalendarContract.Events.DELETED)) == 1)) {
                                        calendarEventExist = false;
                                        break;
                                    }
                                    if (cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE)).equalsIgnoreCase(contentDatum.getGist().getTitle())
                                            && cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)).equalsIgnoreCase(contentDatum.getGist().getDescription())) {
//                                        Toast.makeText(getCurrentContext(), "Event already exists in your calendar.", Toast.LENGTH_SHORT).show();
                                        calendarEventExist = true;
                                        break;
                                    }
                                } while (cursor.moveToNext());

                            }
                        }
                    } else {
                        calendarEventExist = false;
                    }
                    if (!calendarEventExist) {
                        addEventToCalendar(contentDatum, appCMSPresenter);
                    } else {
                        if (calenderAddToast == null || calenderAddToast.getView().getWindowVisibility() != View.VISIBLE) {
                            Toast.makeText(appCMSPresenter.getCurrentContext(), "Event already exists in your calendar.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        }, appCMSPresenter);


    }

    Toast calenderAddToast;
}
