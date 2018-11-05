package com.hua.lockp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Client client = new Client(5777, new Client.MsgCallBack() {
            @Override
            public void onMsg(String text) {
                //do nothings
            }
        });
        if (intent.getIntExtra("type", -1) == 666){
            final Client finalClient = client;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (finalClient !=null){
                        finalClient.send(Client.POWER_EVENT);
                        finalClient.close();
                    }
                }
            }).start();
        }
    }
}
