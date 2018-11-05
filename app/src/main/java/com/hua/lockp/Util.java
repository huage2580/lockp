package com.hua.lockp;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Util {

    public static boolean copyAssetFile(Context context, String fromPath, String toPath) {
        if (context == null || TextUtils.isEmpty(fromPath) || TextUtils.isEmpty(toPath)) {
            return false;
        }
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            is = context.getAssets().open(fromPath);
            File toFile = new File(toPath);
            if (toFile.exists()) {
                toFile.delete();
            }
            toFile.createNewFile();
            toFile.setReadable(true, false);
            toFile.setWritable(true, false);
            toFile.setExecutable(true, false);
            fos = new FileOutputStream(toFile);
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            // 循环从输入流读取数据到 buffer 中
            while ((byteCount = is.read(buffer)) != -1) {
                // 将读取的输入流写入到输出流
                fos.write(buffer, 0, byteCount);
            }
            // 刷新缓冲区
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
