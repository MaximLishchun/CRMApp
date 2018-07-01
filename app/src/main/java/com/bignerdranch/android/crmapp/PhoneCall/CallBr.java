package com.bignerdranch.android.crmapp.PhoneCall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Handler;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.Telephony.Mms.Part.FILENAME;

public class CallBr extends BroadcastReceiver {
    Bundle bundle;
    String state;
    String inCall, outCall;
    public boolean wasRinging = false;

    MediaRecorder recorder;
    File audiofile;
    String name, phonenumber;
    String audio_format;
    public String Audio_Type;
    int audioSource;
    Context context;
    private Handler handler;
    Timer timer;
    Boolean offHook = false, ringing = false;
    Toast toast;
    Boolean isOffHook = false;
    private boolean recordstarted = false;

    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";

    private static OnPlayCall onPlayCall;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_IN)) {
            Log.d("call", "ok1");
            if ((bundle = intent.getExtras()) != null) {
                Log.d("call", "ok2");
                state = bundle.getString(TelephonyManager.EXTRA_STATE);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    Log.d("call", "ok3");
                    inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    wasRinging = true;
                    Toast.makeText(context, "IN : " + inCall, Toast.LENGTH_LONG).show();
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Log.d("call", "ok4");
                    wasRinging = true;
                    if (wasRinging == true) {
                        Log.d("call", "ok5");
                        Toast.makeText(context, "ANSWERED", Toast.LENGTH_LONG).show();

                        //String out = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
//                        File mydir = new File(context.getFilesDir(), "RecordData"); //Creating an internal dir;
//                        if (!mydir.exists()) {
//                            mydir.mkdirs();
//                        }
                        File folder = new File(Environment.getExternalStorageDirectory() +
                                File.separator + "TollCulator");
                        if(!folder.exists()){
                            folder.mkdirs();
                        }
//                        File sampleDir = new File(Environment.getExternalStorageDirectory(), "RecordingDada");
//                        if (!sampleDir.exists()) {
//                            sampleDir.mkdirs();
//                        }
//                        try {
//                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
//                                    context.openFileOutput(FILENAME, MODE_PRIVATE)));
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
                        String file_name = "Record";
                        try {
                            audiofile = File.createTempFile(file_name, ".amr", folder);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //String path = Environment.getExternalStorageDirectory().getAbsolutePath();

                        if (recorder == null) {
                            recorder = new MediaRecorder();
                            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                            //recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            recorder.setOutputFile(audiofile.getAbsolutePath()+"/Record.amr");
                        }
                        if (!recordstarted) {
                            try {
                                recorder.prepare();
                                recorder.start();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            onPlayCall.playCall(file_name);
                            recordstarted = true;
                        }
                    }
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    wasRinging = false;
                    Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                    if (recordstarted) {
                        recorder.stop();
                        recordstarted = false;
                    }
                }
            }
        } else if (intent.getAction().equals(ACTION_OUT)) {
            if ((bundle = intent.getExtras()) != null) {
                outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Toast.makeText(context, "OUT : " + outCall, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void setOnPlayCall(OnPlayCall thisOnPlayCall){
        onPlayCall = thisOnPlayCall;
    }
}
