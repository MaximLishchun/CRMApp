package com.bignerdranch.android.crmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.crmapp.Room.AppRoom;
import com.bignerdranch.android.crmapp.Room.DBSmsObject;
import com.bignerdranch.android.crmapp.Room.DataBase;
import com.bignerdranch.android.crmapp.Room.SmsDao;
import com.bignerdranch.android.crmapp.SMS.OnSmsDataTransmission;
import com.bignerdranch.android.crmapp.SMS.OutgoingSMSReceiver;
import com.bignerdranch.android.crmapp.SMS.SmsReceiver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSmsDataTransmission {

    private SmsReceiver smsReceiver;

    private DataBase dataBase;
    private SmsDao smsDao;
    private DBSmsObject dbSmsObject;
    private List<DBSmsObject> smsObjectList;

    private TextView messageSms;
    private TextView dateSms;
    private TextView senderSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageSms = (TextView) findViewById(R.id.message);
        dateSms = (TextView) findViewById(R.id.dateSms);
        senderSms = (TextView) findViewById(R.id.senderSms);

        smsObjectList = new ArrayList<>();

        smsReceiver = new SmsReceiver();
        smsReceiver.setOnSmsDataTransmission(this);

        startService(new Intent(this, OutgoingSMSReceiver.class));
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
