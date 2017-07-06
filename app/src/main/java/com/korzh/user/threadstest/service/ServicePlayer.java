package com.korzh.user.threadstest.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.korzh.user.threadstest.R;

import java.io.IOException;

public class ServicePlayer extends Service {

    private static final String TAG = "ServicePlayer";

    public static final String ACTION_POSITION = "player.action.music";
    public static final String ACTION_PLAY  = "player.play";
    public static final String ACTION_STOP  = "player.stop";



    public static final String KEY  = "player.position";
    private static final String INTENT_ACTION  = "action";

    private NotificationManagerCompat notificationManager;
    private MediaPlayer mediaPlayer;
    private LocalBroadcastManager localBroadcastManager;

    private boolean sendBroadcastIntent = true;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }


    public void play() {
        if (mediaPlayer == null) {
            String url = "http://www.brothershouse.narod.ru/music/pepe_link_-_guitar_vibe_113_club_mix.mp3";
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(mediaPlayer1 -> stop());
                mediaPlayer.setOnPreparedListener(mediaPlayer12 -> showNotification(R.drawable.ic_play, ACTION_PLAY, "Play", true));
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.start();
                sendBroadcastIntent = true;
                listenPlayer();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
            showNotification(R.drawable.ic_stop, ACTION_STOP, "Stop", false);
            sendBroadcastIntent = false;
        }
    }




    private void listenPlayer(){
        new Thread(() -> {
            while(sendBroadcastIntent){
                try {
                    Thread.sleep(300);
                    if(mediaPlayer != null)
                    sendBroadcastEvent(mediaPlayer.getCurrentPosition());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendBroadcastEvent(int currentPosition) {
        Intent intent = new Intent(ACTION_POSITION);
        intent.putExtra(KEY, currentPosition);
        localBroadcastManager.sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(R.string.app_name);
    }

    public void setLocalBroadcastManager(LocalBroadcastManager localBroadcastManager) {
        this.localBroadcastManager = localBroadcastManager;
    }

    class LocalBinder extends Binder {
        ServicePlayer getService() {
            return ServicePlayer.this;
        }
    }






    private void showNotification(int res, String text, String action, boolean ongoing) {

//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
//        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(res)
//                .setTicker(text)
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle("Music")
//                .setContentText(text)
//                .setContentIntent(contentIntent)
//                .setOngoing(ongoing)
//                .build();
//        notificationManager.notify(R.string.app_name, notification);



//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        Intent resultIntent = new Intent(this, MainActivity.class);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

//        TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(this);
//        Intent resultIntent2 = new Intent(this, LoginActivity.class);
//        stackBuilder2.addParentStack(LoginActivity.class);
//        stackBuilder2.addNextIntent(resultIntent2);
//        PendingIntent resultPendingIntent2 =
//                stackBuilder2.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setCategory(NotificationCompat.CATEGORY_ERROR)
                .setContentText(text)
                .setSmallIcon(res)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Music")
                .setContentText(text)
                .setOngoing(ongoing)
//                .setContentIntent(contentIntent)
//                .setContentIntent(resultPendingIntent)


                .addAction(res, text, pendingIntent)

//                .setStyle(new NotificationCompat.InboxStyle()
//                        .addLine("hello_1")
//                        .addLine("hello_2")
//                        .setSummaryText("+3 more"))

                .setColor(Color.CYAN)
                .build();

        notificationManager.notify(0, notification);

    }

//    public static class ButtonListener extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d("Here", "I am here");
////            FlashOnOff flashLight;
////            flashLight = new FlashOnOff();
////            flashLight.flashLightOff();
////            flashLight.releaseCamera();
//        }
//    }

}
