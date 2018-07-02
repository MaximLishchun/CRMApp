package com.bignerdranch.android.crmapp.PhoneCall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Handler;

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
                        File folder = new File(Environment.getExternalStorageDirectory() +
                                File.separator + "TollCurator");
                        if(!folder.exists()){
                            folder.mkdirs();
                        }else if (!folder.isDirectory() && folder.canWrite()){
                            folder.delete();
                            folder.mkdirs();
                        }
                            //String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                        if(audiofile == null) {
                            String file_name = "Record";
                            try {
                                audiofile = File.createTempFile(file_name, ".amr", folder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (recorder == null) {
                            recorder = new MediaRecorder();
                            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                            //recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            recorder.setOutputFile(audiofile.getAbsolutePath());
                            Log.d("call", audiofile.getAbsolutePath());
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
//                            onPlayCall.playCall(file_name);
                            recordstarted = true;
                        }
                    }
                }
                else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Log.d("call", "ok6");
                    wasRinging = false;
                    Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                    if (recordstarted) {
                        recorder.stop();
                        recordstarted = false;
                    }
                }
            }
        } else if (intent.getAction().equals(ACTION_OUT)) {
            Log.d("call", "ok7");
            if ((bundle = intent.getExtras()) != null) {
                outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Toast.makeText(context, "OUT : " + outCall, Toast.LENGTH_LONG).show();
            }
        }
    }
}
