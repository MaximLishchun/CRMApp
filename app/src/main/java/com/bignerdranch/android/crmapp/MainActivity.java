package com.bignerdranch.android.crmapp;

import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.crmapp.PhoneCall.CallBr;
import com.bignerdranch.android.crmapp.PhoneCall.OnPlayCall;
import com.bignerdranch.android.crmapp.Room.AppRoom;
import com.bignerdranch.android.crmapp.Room.DBSmsObject;
import com.bignerdranch.android.crmapp.Room.DataBase;
import com.bignerdranch.android.crmapp.Room.SmsDao;
import com.bignerdranch.android.crmapp.SMS.OnSmsDataTransmission;
import com.bignerdranch.android.crmapp.SMS.OutgoingSMSReceiver;
import com.bignerdranch.android.crmapp.SMS.SmsReceiver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSmsDataTransmission{

    private SmsReceiver smsReceiver;

    private DataBase dataBase;
    private SmsDao smsDao;
    private DBSmsObject dbSmsObject;

    private TextView messageSms;
    private TextView dateSms;
    private TextView senderSms;

    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;

    private PendingIntent pendingIntent;
    private Intent intentPI;
    private final int RECORD_CODE = 001;
    public final static String PARAM_PINTENT = "pendingIntent";

    private CallBr callBr;
    private String nameRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageSms = (TextView) findViewById(R.id.message);
        dateSms = (TextView) findViewById(R.id.dateSms);
        senderSms = (TextView) findViewById(R.id.senderSms);

        smsReceiver = new SmsReceiver();
        smsReceiver.setOnSmsDataTransmission(this);

        callBr = new CallBr();

        startService(new Intent(this, OutgoingSMSReceiver.class));
//        startService(new Intent(this, TService.class));
//        try {
//            // Initiate DevicePolicyManager.
//            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//            mAdminName = new ComponentName(this, DeviceAdminDemo.class);
//
//            if (!mDPM.isAdminActive(mAdminName)) {
//                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
//                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
//                startActivityForResult(intent, REQUEST_CODE);
//            } else {
//                // mDPM.lockNow();
//                // Intent intent = new Intent(MainActivity.this,
//                // TrackDeviceService.class);
//                // startService(intent);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //pendingIntent = createPendingResult(RECORD_CODE, null, 0);
        //intentPI = new Intent(this, TService.class).putExtra(PARAM_PINTENT , pendingIntent);
    }

    @Override
    public void smsDataTransmission(final String sender, final String date, final String message) {
        if(sender != null && date != null && message != null){
            messageSms.setText(message);
            dateSms.setText(date);
            senderSms.setText(sender);
        }
//        new Thread(new Runnable() {
//            public void run() {
//                saveSms(sender, date, message);
//            }
//        }).start();
//        Toast.makeText(getApplicationContext(), "Save in DB!", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void saveSms(String sender, String date, String message){
        dataBase = AppRoom.getInstance().getDataBase();
        smsDao = dataBase.getSmsDao();
        dbSmsObject = new DBSmsObject();
        dbSmsObject.sender = sender;
        dbSmsObject.date = date;
        dbSmsObject.message = message;
        smsDao.saveSms(dbSmsObject);
    }
}
