package com.korzh.user.threadstest.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;

import com.korzh.user.threadstest.MainActivity;
import com.korzh.user.threadstest.R;

import java.io.IOException;

public class ServicePlayer extends Service {

    public static final String ACTION_POSITION = "player.action.music";
    public static final String ACTION_PLAY  = "player.play";
    public static final String ACTION_STOP  = "player.stop";
    public static final String KEY  = "player.position";

    private MediaPlayer mediaPlayer;
    private LocalBroadcastManager localBroadcastManager;
    private NotificationManagerCompat notificationManager;
    private NotificationBtnClickReceiver mNotificationBtnClickReceiver;

    private boolean sendBroadcastIntent = true;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
        registerPlayStopNotificationReceiver();
    }

    private void registerPlayStopNotificationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_STOP);
        mNotificationBtnClickReceiver = new NotificationBtnClickReceiver();
        registerReceiver(mNotificationBtnClickReceiver, filter);
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
                mediaPlayer.setOnPreparedListener(mediaPlayer12 -> showNotification(true));
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.start();
                sendBroadcastIntent = true;
                listenPlayerPosition();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
            showNotification(false);
            sendBroadcastIntent = false;
        }
    }

    private void listenPlayerPosition(){
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

    public void setLocalBroadcastManager(LocalBroadcastManager localBroadcastManager) {
        this.localBroadcastManager = localBroadcastManager;
    }

    private void showNotification(boolean play) {

        int iconId;
        int actionBtId;
        String stayText;
        String actionText;
        String action;
        boolean ongoing;

        if(play){
            iconId = R.drawable.ic_play;
            stayText = getString(R.string.play);
            actionText = getString(R.string.stop);
            actionBtId = R.drawable.ic_stop_black_18dp;
            action = ACTION_STOP;
            ongoing = true;
        }else{
            iconId = R.drawable.ic_stop;
            stayText = getString(R.string.stop);
            actionText = getString(R.string.play);
            actionBtId = R.drawable.ic_play_arrow_black_18dp;
            action = ACTION_PLAY;
            ongoing = false;
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        Intent openActivityIntent = new Intent(this, MainActivity.class);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(openActivityIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent actionIntent = new Intent(action);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(this, 0, actionIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setCategory(NotificationCompat.CATEGORY_ERROR)
                .setContentText(stayText)
                .setContentText(stayText)
                .setTicker(stayText)
                .setSmallIcon(iconId)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.player))
                .setOngoing(ongoing)
                .setContentIntent(resultPendingIntent)
                .addAction(actionBtId, actionText, actionPendingIntent)
                .setColor(Color.CYAN)
                .build();

        notificationManager.notify(0, notification);
    }

    private class NotificationBtnClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case ACTION_PLAY:{
                    play();
                    break;
                }
                case ACTION_STOP:{
                    stop();
                    break;
                }
            }
        }
    }

    class LocalBinder extends Binder {
        ServicePlayer getService() {
            return ServicePlayer.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(R.string.app_name);
        unregisterReceiver(mNotificationBtnClickReceiver);
    }
}
