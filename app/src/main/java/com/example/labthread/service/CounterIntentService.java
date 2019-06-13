package com.example.labthread.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class CounterIntentService extends IntentService {

    public CounterIntentService(){
        super("");
    }
    public CounterIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        for(int i = 0 ; i<100 ; i++){
            Log.d("IntentService","i = "+i);
            Intent broadcast = new Intent("CounterIntentServiceUpdate");
            broadcast.putExtra("counter",i);
            LocalBroadcastManager.getInstance(CounterIntentService.this)
                    .sendBroadcast(broadcast);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }
}
