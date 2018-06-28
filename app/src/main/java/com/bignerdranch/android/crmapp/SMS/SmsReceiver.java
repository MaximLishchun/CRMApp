package com.bignerdranch.android.crmapp.SMS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static OnSmsDataTransmission onSmsDataTransmission;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String date = String.valueOf((((messages[0].getTimestampMillis()-32)*5)/9));
                String message = sb.toString();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                // prevent any other broadcast receivers from receiving broadcast
                // abortBroadcast();
                if (onSmsDataTransmission != null)
                    onSmsDataTransmission.smsDataTransmission(sender, date, message);
            }
        }
    }

    public static void setOnSmsDataTransmission(OnSmsDataTransmission thisOnSmsDataTransmission){
        onSmsDataTransmission = thisOnSmsDataTransmission;
    }
}
