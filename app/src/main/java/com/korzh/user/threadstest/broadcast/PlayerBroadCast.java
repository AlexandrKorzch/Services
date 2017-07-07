package com.korzh.user.threadstest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.korzh.user.threadstest.callback.PositionCallBack;

import static com.korzh.user.threadstest.service.ServicePlayer.KEY;


public class PlayerBroadCast extends BroadcastReceiver {

    private PositionCallBack positionCallBack;

    @Override
    public void onReceive(Context context, Intent intent) {
        positionCallBack.onGetPosition(intent.getExtras().getInt(KEY));
    }

    public void setPositionCallBack(PositionCallBack positionCallBack) {
        this.positionCallBack = positionCallBack;
    }
}
