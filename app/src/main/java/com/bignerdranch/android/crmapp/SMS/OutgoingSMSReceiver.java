package com.bignerdranch.android.crmapp.SMS;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class OutgoingSMSReceiver extends Service{

    private static final String CONTENT_SMS = "content://sms/";
    static String messageId = "";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyContentObserver extends ContentObserver {

        Context context;
        private SharedPreferences prefs;
        private String phoneNumberBlocked;

        public MyContentObserver(Context context) {
            super(null);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            prefs = context.getSharedPreferences("com.example.testcall", Context.MODE_PRIVATE);
            phoneNumberBlocked = prefs.getString("number", "");

            Uri uriSMSURI = Uri.parse(CONTENT_SMS);
            Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);

            if (cur.moveToNext()) {
                String message_id = cur.getString(cur.getColumnIndex("_id"));
                String type = cur.getString(cur.getColumnIndex("type"));
                String numberTelephone = cur.getString(cur.getColumnIndex("address")).trim();
                String body = cur.getString(cur.getColumnIndex("body")).trim();
                Date currentTime = Calendar.getInstance().getTime();
//                long dateSentLong = Math.round((((Long.parseLong(cur.getString(cur.getColumnIndex("date")).trim()))-32)*5)/9);
//                String dateSent = new java.text.SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
//                        .format(new java.util.Date(dateSentLong * 1000));
                String date = String.valueOf(currentTime);

                Log.d("msg", type);
                Log.d("msg", numberTelephone);
                Log.d("msg", body);
                Log.d("msg", String.valueOf(currentTime));

                if(!date.equals(String.valueOf(currentTime))){
                    Log.d("msg", "change");
                }
            }
        }
        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }
    }

    @Override
    public void onCreate() {
        MyContentObserver contentObserver = new MyContentObserver(getApplicationContext());
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        contentResolver.registerContentObserver(Uri.parse(CONTENT_SMS), true, contentObserver);
    }
}
