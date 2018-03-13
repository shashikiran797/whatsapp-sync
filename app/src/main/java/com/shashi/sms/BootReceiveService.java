package com.shashi.sms;

/**
 * Created by shashikiranms on 13/03/18
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BootReceiveService extends BroadcastReceiver {
    public static final String TAG = BootReceiveService.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: Boot completed");

        Intent startServiceIntent = new Intent(context, IncomingSMSReceiver.class);
        context.startService(startServiceIntent);
    }
}
