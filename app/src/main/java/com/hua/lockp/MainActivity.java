package com.hua.lockp;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView icon;
    Button pc;
    Button hy;
    private Client client;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1){
                handler.removeMessages(2);
                icon.setImageResource(R.drawable.success);
                handler.sendEmptyMessageDelayed(2,2000);
            }
            if (msg.what == 2){
                icon.setImageResource(R.drawable.fail);
            }

        }
    };
    private boolean run = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        icon = findViewById(R.id.iv_icon);
        pc = findViewById(R.id.pc);
        hy = findViewById(R.id.hy);

        client =new Client(5467,new Client.MsgCallBack() {
            @Override
            public void onMsg(String text) {
               handler.sendEmptyMessage(1);
            }
        });
        new HeartThread().start();
        exJar();
        exSh();
        final ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将文本内容放到系统剪贴板里。
                cm.setText(getString(R.string.pc_cmd));
                Toast.makeText(MainActivity.this, R.string.success, Toast.LENGTH_LONG).show();
            }
        });
        hy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将文本内容放到系统剪贴板里。
                cm.setText(getString(R.string.hy_cmd));
                Toast.makeText(MainActivity.this, R.string.success, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void exJar(){
        String fromPath = "server.jar";
        String toPath = getFilesDir().getParentFile() + "/" + "server.jar";
        Log.i("fuck", "exJar: "+toPath);
        Util.copyAssetFile(this, fromPath, toPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Os.chmod(getFilesDir().getParentFile().getAbsolutePath(),489);
                Os.chmod(toPath,420);
            } catch (ErrnoException e) {
                e.printStackTrace();
            }
        }

    }

    public void exSh(){
        String fromPath = "lockp.bash";
        String toPath =getFilesDir().getParentFile() + "/" + "lockp.bash";
        Util.copyAssetFile(this, fromPath, toPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Os.chmod(getFilesDir().getParentFile().getAbsolutePath(),489);
                Os.chmod(toPath,420);
            } catch (ErrnoException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        run = false;
    }

    class HeartThread extends Thread{
        @Override
        public void run() {
            while (run){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                client.send(Client.HEART_BEAT);
            }
        }
    }

}
