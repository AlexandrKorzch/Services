package com.korzh.user.threadstest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.korzh.user.threadstest.service.ServicePlayer;

import static com.korzh.user.threadstest.service.ServiceBinder.doBindService;
import static com.korzh.user.threadstest.service.ServiceBinder.doUnbindService;
import static com.korzh.user.threadstest.service.ServiceBinder.getPlayerServiceIntent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ServicePlayer playerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_play).setOnClickListener(this);
        findViewById(R.id.bt_stop).setOnClickListener(this);

        startService(getPlayerServiceIntent(this));
        doBindService(this, service -> playerService = service);
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
