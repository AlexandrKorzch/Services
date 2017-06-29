//package com.korzh.user.threadstest.async;
//
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
///**
// * Created by user on 27.06.17.
// */
//
//public class MyThread extends Thread {
//
//    private static final String TAG = "MyThread";
//
//    private Handler mHandler;
//
//    public MyThread(String name, Handler handler) {
//        super(name);
//        mHandler = handler;
//    }
//
//    @Override
//    public void run() {
//        super.run();
//        Log.d(TAG, "run: "+Thread.currentThread().getName()+ " count - "+Thread.activeCount());
//        for (int i = 0; i < 20; i++) {
//            try {
//                Thread.sleep(1000);
//
//                Message message = new Message();
//                message.obj = "kashdkahskdhkas";
//
//                mHandler.sendMessage(message);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
