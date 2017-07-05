package com.korzh.user.threadstest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.korzh.user.threadstest.callback.PositionCallBack;

import static com.korzh.user.threadstest.service.ServicePlayer.KEY;

/**
 * Created by user on 05.07.17.
 */

public class PlayerBroadCast extends BroadcastReceiver {

    private static final String TAG = "PlayerBroadCast";
    private PositionCallBack positionCallBack;

    @Override
    public void onReceive(Context context, Intent intent) {
        int position = intent.getExtras().getInt(KEY);
        positionCallBack.onGetPosition(position);
    }

    public void setPositionCallBack(PositionCallBack positionCallBack) {
        this.positionCallBack = positionCallBack;
    }
}