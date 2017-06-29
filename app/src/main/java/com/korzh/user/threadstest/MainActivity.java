package com.korzh.user.threadstest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.korzh.user.threadstest.service.ServiceExample;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ServiceExample";
    private boolean mIsBound;

    private ServiceExample mBoundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, ServiceExample.class)) ;
        doBindService();
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((ServiceExample.LocalBinder)service).getService();
            Log.d(TAG, "onServiceConnected: "+mBoundService);
            mBoundService.hello();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    void doBindService() {
        bindService(new Intent(this, ServiceExample.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        doUnbindService();
    }
}





















//    public interface CallBack{
//        void onGetBitmap(Bitmap bitmap);
//    }



//        String url = "http://2.bp.blogspot.com/-5pAQzxsNPVU/VS5Xo7GIvPI/AAAAAAAAEPA/5tmsaBavBj8/s1600/Handler.png";
//
//        MyTask task = new MyTask(url, new CallBack() {
//            @Override
//            public void onGetBitmap(Bitmap bitmap) {
//
//                Log.d(TAG, "onGetBitmap() called with: bitmap = [" + bitmap + "]");
//
//            }
//        });
//        task.execute();

//        Log.d(TAG, "run: "+" count - "+Thread.activeCount());

//        Handler handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                Log.d(TAG, "handleMessage: "+msg.obj);
//                return true;
//            }
//        });


//        Thread thread1 = new MyThread("thread1", handler);
//        Thread thread2 = new MyThread("thread2");
//        Thread thread3 = new MyThread("thread3");
//        Thread thread4 = new MyThread("thread4");
//        Thread thread5 = new MyThread("thread5");
//
//        thread1.start();
//        thread2.start();
//        thread3.start();
//        thread4.start();
//        thread5.start();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "run: "+" count - "+Thread.activeCount());
//            }
//        }, 1000);