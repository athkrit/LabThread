package com.example.labthread;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.labthread.service.CounterIntentService;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {
    TextView tvCount;
    int count;
    Thread thread;
    Handler handler;
    HandlerThread backgroundHandlerThread;
    Handler backgroundHandler;
    Handler mainHandler;
//    SampleAsyncTask sampleAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCount = findViewById(R.id.tvCount);

        count = 0;
        // Method 1
        /*
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //BG thread
                for (int i = 0 ; i<100 ; i++){
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //ใส่เพื่อหยุดการทำงาน
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //UI thread
                            tvCount.setText(count+"");
                        }
                    });
                }
            }
        });
        thread.start();*/
        //Method 2
        /*
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //run in mainthread
                tvCount.setText(msg.arg1+"");
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //BG thread
                for (int i = 0 ; i<100 ; i++){
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //ใส่เพื่อหยุดการทำงาน
                        return;
                    }
                    Message msg = new Message();
                    msg.arg1 =count;
                    handler.sendMessage(msg);
                }
            }
        });
        thread.start();*/

        //method 3
        /*
        handler = new Handler(getMainLooper())
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                count++;
                tvCount.setText(count+"");
                if(count<100)
                    sendEmptyMessageDelayed(0,1000);
            }
        };
        handler.sendEmptyMessageDelayed(10,1000);*/
        //handler thread
//        backgroundHandlerThread = new HandlerThread("BackgroundHandlerThead");
//        backgroundHandlerThread.start();
//
//        backgroundHandler = new Handler(backgroundHandlerThread.getLooper())
//        {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                //run in background
//                Message msgMain = new Message();
//                msgMain.arg1 = msg.arg1+1;
//                mainHandler.sendMessage(msgMain);
//            }
//        };
//        mainHandler = new Handler(getMainLooper())
//        {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                // run in mainthread
//                tvCount.setText(msg.arg1+"");
//                if(msg.arg1 < 10){
//                    Message msgBack = new Message();
//                    msgBack.arg1 = msg.arg1;
//                    backgroundHandler.sendMessageDelayed(msgBack,1000);
//                }
//
//            }
//        };
//        Message msgBack = new Message();
//        msgBack.arg1 = 0;
//        backgroundHandler.sendMessageDelayed(msgBack,1000);
        //AsyncTask
//        sampleAsyncTask = new SampleAsyncTask();
//        sampleAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,0,100);
        //Method 6 AsyncTaskLoader
//        LoaderManager.getInstance(this).initLoader(1, null, MainActivity.this).forceLoad();
        //Method 7 :IntentService
        LocalBroadcastManager.getInstance(MainActivity.this)
                .registerReceiver(counterBroadcastReceiver,new IntentFilter("CounterIntentServiceUpdate"));
        Intent intent = new Intent(MainActivity.this, CounterIntentService.class);
        startService(intent);
    }
    protected BroadcastReceiver counterBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tvCount.setText(""+intent.getIntExtra("counter",0));
        }
    };

    @NonNull
    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(counterBroadcastReceiver);
//        thread.interrupt();
        //destroy thread
//        backgroundHandlerThread.quit();
//        sampleAsyncTask.cancel(true);
    }

    @NonNull
    @Override
    public Loader<Object> onCreateLoader(int i, @Nullable Bundle bundle) {
//        if (i == 1) {
//            Log.d("Hiiii", "Load Again");
//            return new AdderAsyncTaskLoader(MainActivity.this, 5, 11);
//        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if (loader.getId() == 1) {
            Log.d("load", "Love you 7000");
            Integer result = (Integer) data;
            tvCount.setText(result + "");
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

//    static class AdderAsyncTaskLoader extends AsyncTaskLoader<Object> {
//        int a;
//        int b;
//        Integer result;
//
//        Handler handler;
//
//        public AdderAsyncTaskLoader(Context context, Integer a, Integer b) {
//            super(context);
//            this.a = a;
//            this.b = b;
//        }
//
//        @Nullable
//        @Override
//        protected void onStartLoading() {
//            super.onStartLoading();
//            if (result != null) {
//                deliverResult(result);
//            }
//            //initialize handler
//            if (handler == null) {
//                handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        a = (int) (Math.random() * 100);
//                        b = (int) (Math.random() * 100);
//                        onContentChanged();
//                        handler.sendEmptyMessageDelayed(0, 3000);
//                    }
//                };
//                handler.sendEmptyMessageDelayed(0, 3000);
//
//            }
//            if (takeContentChanged() || result == null) {
//                forceLoad();
//            }
//        }
//
//        @Override
//        public Integer loadInBackground() {
//            //Do in background thread
////            try {
////                Thread.sleep(5000);
////            } catch (InterruptedException e) {
////            }
//            result = a + b;
//            return result;
//        }
//
//        @Override
//        protected void onReset() {
//            super.onReset();
//            if(handler != null){
//                handler.removeCallbacksAndMessages(null);
//                handler = null;
//            }
//        }
//    }

    //AsyncTask <input , progress , output>
    //less than 5 sec
//    class SampleAsyncTask extends AsyncTask<Integer, Float, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Integer... integers) {
//            //ใส่ input กี่ตัวก้ได้
//            int start = integers[0];
//            int end = integers[1];
//            for (int i = start; i <= end; i++) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    return false;
//                }
//                publishProgress(i + 0.0f);
//            }
//            return true;
//        }
//
//        @Override
//        protected void onProgressUpdate(Float... values) {
//            super.onProgressUpdate(values);
//            Float progress = values[0];
//            tvCount.setText(progress + "%");
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            //work on Mainthread
//            super.onPostExecute(aBoolean);
//            //work with Boolean
//        }
//    }
}
