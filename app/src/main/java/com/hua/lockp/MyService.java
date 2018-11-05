package com.hua.lockp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MyService extends Service {
    private Client client;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (client == null){
            client = new Client(5777, new Client.MsgCallBack() {
                @Override
                public void onMsg(String text) {
                    //do nothings
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (client == null){
            client = new Client(5777, new Client.MsgCallBack() {
                @Override
                public void onMsg(String text) {
                    //do nothings
                }
            });
        }

        if (client!=null && intent.getIntExtra("type",-1) == 666){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.send(Client.POWER_EVENT);
                }
            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
