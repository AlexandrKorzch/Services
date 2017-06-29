package com.korzh.user.threadstest.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.korzh.user.threadstest.MainActivity;
import com.korzh.user.threadstest.R;

public class ServiceExample extends Service {

    private static final String TAG = "ServiceExample";

    public ServiceExample() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        Log.d(TAG, "onStartCommand: this - "+this);
        showNotification();
        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        public ServiceExample getService() {
            return ServiceExample.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return new LocalBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNM.cancel(R.string.app_name);
        Log.d(TAG, "onDestroy: ");
    }

    NotificationManager mNM;

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "Notification from service";

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Title")  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.app_name, notification);
    }
    
    public void hello(){
        Log.d(TAG, "hello: ");
    }
    

}
