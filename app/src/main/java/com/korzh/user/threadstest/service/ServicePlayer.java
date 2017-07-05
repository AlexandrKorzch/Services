package com.korzh.user.threadstest.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.korzh.user.threadstest.MainActivity;
import com.korzh.user.threadstest.R;

import java.io.IOException;

public class ServicePlayer extends Service {

    private static final String TAG = "ServicePlayer";

    public static final String ACTION  = "player.action.music";
    public static final String KEY  = "player.position";

    private NotificationManager notificationManager;
    private MediaPlayer mediaPlayer;
    private LocalBroadcastManager localBroadcastManager;

    private boolean sendBroadcastIntent = true;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
                mediaPlayer.setOnPreparedListener(mediaPlayer12 -> showNotification(R.drawable.ic_play, "Play", true));
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
            showNotification(R.drawable.ic_stop, "Stop", false);
            sendBroadcastIntent = false;
        }
    }

    private void showNotification(int res, String text, boolean ongoing) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(res)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Music")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setOngoing(ongoing)
                .build();
        notificationManager.notify(R.string.app_name, notification);
    }


    private void listenPlayer(){
        new Thread(() -> {
            while(sendBroadcastIntent){
                try {
                    Thread.sleep(300);
                    sendBroadcastEvent(mediaPlayer.getCurrentPosition());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        mediaPlayer.getCurrentPosition();
    }

    private void sendBroadcastEvent(int currentPosition) {
        Intent intent = new Intent(ACTION);
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
}
