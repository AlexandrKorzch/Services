package com.korzh.user.threadstest;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.korzh.user.threadstest.broadcast.PlayerBroadCast;
import com.korzh.user.threadstest.callback.PositionCallBack;
import com.korzh.user.threadstest.service.ServicePlayer;

import static com.korzh.user.threadstest.service.ServiceBinder.doBindService;
import static com.korzh.user.threadstest.service.ServiceBinder.doUnbindService;
import static com.korzh.user.threadstest.service.ServiceBinder.getPlayerServiceIntent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private ServicePlayer playerService;

    private PlayerBroadCast playerBroadCast;
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_play).setOnClickListener(this);
        findViewById(R.id.bt_stop).setOnClickListener(this);

        SeekBar seekBar = (SeekBar)findViewById(R.id.sb_progress);
        TextView textPosition = (TextView)findViewById(R.id.tx_time);


        playerBroadCast = new PlayerBroadCast();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);

        startService(getPlayerServiceIntent(this));
        doBindService(this, service -> {
            playerService = service;
            playerService.setLocalBroadcastManager(mLocalBroadcastManager);
        });

        setUpdateByProgressLogic(seekBar, textPosition);
    }


    private void setUpdateByProgressLogic(SeekBar seekBar, TextView textPosition) {
        playerBroadCast.setPositionCallBack(position -> {
            Log.d(TAG, "onCreate: position - "+position);
            seekBar.setProgress(position);

            int progressInSeconds = position/1000;
            int min = progressInSeconds/60;
            int sec = progressInSeconds%60;
            String time = min+":"+sec;

            textPosition.setText(time);
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    private void registerReceiver(){
        IntentFilter filter=new IntentFilter();
        filter.addAction(ServicePlayer.ACTION);
        mLocalBroadcastManager.registerReceiver(playerBroadCast , filter);
    }

    private void unregisterReceiver(){
        mLocalBroadcastManager.unregisterReceiver(playerBroadCast);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bt_play:{
                playerService.play();
                break;
            }
            case R.id.bt_stop:{
                playerService.stop();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService(this);
    }
}
