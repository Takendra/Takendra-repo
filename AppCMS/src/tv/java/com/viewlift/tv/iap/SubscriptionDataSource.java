package com.viewlift.tv.iap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.viewlift.models.data.appcms.api.ContentDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for sample purchase data
 * 
 * 
 */
public class SubscriptionDataSource {

    private static final String TAG = "SampleIAPManager";

    private SQLiteDatabase database;
    private final IapSQLiteHelper dbHelper;

    private final String[] allColumns = { IapSQLiteHelper.COLUMN_RECEIPT_ID, IapSQLiteHelper.COLUMN_USER_ID,
            IapSQLiteHelper.COLUMN_DATE_FROM, IapSQLiteHelper.COLUMN_DATE_TO, IapSQLiteHelper.COLUMN_SKU,
            IapSQLiteHelper.COLUMN_VL_EMAIL,IapSQLiteHelper.COLUMN_CURRENCY_CODE};

    public SubscriptionDataSource(final Context context) {
        dbHelper = new IapSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private SubscriptionRecord cursorToSubscriptionRecord(final Cursor cursor) {
        final SubscriptionRecord subsRecord = new SubscriptionRecord();
        subsRecord.setAmazonReceiptId(cursor.getString(cursor.getColumnIndex(IapSQLiteHelper.COLUMN_RECEIPT_ID)));
        subsRecord.setAmazonUserId(cursor.getString(cursor.getColumnIndex(IapSQLiteHelper.COLUMN_USER_ID)));
        subsRecord.setFrom(cursor.getLong(cursor.getColumnIndex(IapSQLiteHelper.COLUMN_DATE_FROM)));
        subsRecord.setTo(cursor.getLong(cursor.getColumnIndex(IapSQLiteHelper.COLUMN_DATE_TO)));
        subsRecord.setSku(cursor.getString(cursor.getColumnIndex(IapSQLiteHelper.COLUMN_SKU)));
        subsRecord.setVlUserId(cursor.getString(cursor.getColumnIndex(IapSQLiteHelper.COLUMN_VL_EMAIL)));
        subsRecord.setCurrencyCode(cursor.getString(cursor.getColumnIndex(IapSQLiteHelper.COLUMN_CURRENCY_CODE)));
        return subsRecord;
    }

    /**
     * Return all subscription records for the user
     * 
     * @param userId
     *            user id used to verify the purchase record
     * @return
     */
    public final List<SubscriptionRecord> getSubscriptionRecords(final String userId) {
        Log.d(TAG, "getSubscriptionRecord: userId (" + userId + ")");

        final String where = IapSQLiteHelper.COLUMN_USER_ID + " = ?";
        final Cursor cursor = database.query(IapSQLiteHelper.TABLE_SUBSCRIPTIONS,
                                             allColumns,
                                             where,
                                             new String[] { userId },
                                             null,
                                             null,
                                             null);
        cursor.moveToFirst();
        final List<SubscriptionRecord> results = new ArrayList<SubscriptionRecord>();
        while (!cursor.isAfterLast()) {
            final SubscriptionRecord subsRecord = cursorToSubscriptionRecord(cursor);
            results.add(subsRecord);
            cursor.moveToNext();
        }
        Log.d(TAG, "getSubscriptionRecord: found " + results.size() + " records");
        cursor.close();
        return results;

    }

    /**
     * Insert or update the subscription record by receiptId
     * @param receiptId
     *            The receipt id
     * @param userId
     *            Amazon user id
     * @param dateFrom
*            Timestamp for subscription's valid from date
     * @param dateTo
*            Timestamp for subscription's valid to date. less than 1 means
*            cancel date not set, the subscription in active status.
     * @param sku
     * @param loggedInUser
     * @param contentDataForAmazonPurchase
     */
    public void insertOrUpdateSubscriptionRecord(final String receiptId,
                                                 final String userId,
                                                 final long dateFrom,
                                                 final long dateTo,
                                                 final String sku, String loggedInUser, ContentDatum contentDataForAmazonPurchase) {
        Log.d(TAG, "insertOrUpdateSubscriptionRecord: receiptId (" + receiptId + "),userId (" + userId + ")");
        final String where = IapSQLiteHelper.COLUMN_RECEIPT_ID + " = ? and "
                             + IapSQLiteHelper.COLUMN_DATE_TO
                             + " > 0";

        final Cursor cursor = database.query(IapSQLiteHelper.TABLE_SUBSCRIPTIONS,
                                             allColumns,
                                             where,
                                             new String[] { receiptId },
                                             null,
                                             null,
                                             null);
        final int count = cursor.getCount();
        cursor.close();

        String currencyCode ="";
        if(contentDataForAmazonPurchase != null
                && contentDataForAmazonPurchase.getPlanDetails() != null
                && contentDataForAmazonPurchase.getPlanDetails().get(0) !=null){
            currencyCode = contentDataForAmazonPurchase.getPlanDetails().get(0).getRecurringPaymentCurrencyCode();
        }


        if (count > 0) {
            // There are record with given receipt id and cancel_date>0 in the
            // table, this record should be final and cannot be overwritten
            // anymore.
            Log.w(TAG, "Record already in final state");
        } else {
            // Insert the record into database with CONFLICT_REPLACE flag.
            final ContentValues values = new ContentValues();
            values.put(IapSQLiteHelper.COLUMN_RECEIPT_ID, receiptId);
            values.put(IapSQLiteHelper.COLUMN_USER_ID, userId);
            values.put(IapSQLiteHelper.COLUMN_DATE_FROM, dateFrom);
            values.put(IapSQLiteHelper.COLUMN_DATE_TO, dateTo);
            values.put(IapSQLiteHelper.COLUMN_SKU, sku);
            values.put(IapSQLiteHelper.COLUMN_VL_EMAIL,loggedInUser);
            values.put(IapSQLiteHelper.COLUMN_CURRENCY_CODE,currencyCode);

            database.insertWithOnConflict(IapSQLiteHelper.TABLE_SUBSCRIPTIONS,
                                          null,
                                          values,
                                          SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    /**
     * Cancel a subscription by set the cancel date for the subscription record
     * 
     * @param receiptId
     *            The receipt id
     * @param cancelDate
     *            Timestamp for the cancel date
     * @return
     */
    public boolean cancelSubscription(final String receiptId, final long cancelDate) {
        Log.d(TAG, "cancelSubscription: receiptId (" + receiptId + "), cancelDate:(" + cancelDate + ")");

        final String where = IapSQLiteHelper.COLUMN_RECEIPT_ID + " = ?";
        final ContentValues values = new ContentValues();
        values.put(IapSQLiteHelper.COLUMN_DATE_TO, cancelDate);
        final int updated = database.update(IapSQLiteHelper.TABLE_SUBSCRIPTIONS,
                                            values,
                                            where,
                                            new String[] { receiptId });
        Log.d(TAG, "cancelSubscription: updated " + updated);
        return updated > 0;

    }
}
