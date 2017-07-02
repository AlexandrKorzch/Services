package com.korzh.user.threadstest.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by alex on 7/2/17.
 */

public class ServiceBinder {

    private static boolean mIsBound;
    private static OnBindServiceCallBack mCallBack;

    public static Intent getPlayerServiceIntent(Context context) {
        return new Intent(context, ServicePlayer.class);
    }

    private static ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mCallBack.onGetService(((ServicePlayer.LocalBinder)service).getService());
        }
        public void onServiceDisconnected(ComponentName className) {
            mCallBack.onGetService(null);
        }
    };

    public static void doBindService(Context context, OnBindServiceCallBack callBack) {
        mCallBack = callBack;
        context.bindService(getPlayerServiceIntent(context), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    public static void doUnbindService(Context context) {
        if (mIsBound) {
            context.unbindService(mConnection);
            mIsBound = false;
        }
    }

    public interface OnBindServiceCallBack{
        void onGetService(ServicePlayer service);
    }
}
