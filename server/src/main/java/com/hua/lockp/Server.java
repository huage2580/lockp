package com.hua.lockp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Server.java
 * 后台待机进程
 * Created by hua on 2018/11/2.
 */

public class Server {
    private static final String TAG = "Server";
    private static final int PORT = 5666;
    private static final int MAX_BYTE = 1024;

    public static void main(String args[]) {
        new Server();
    }

    private DatagramPacket datagramPacket;
    private DatagramSocket datagramSocket;
    private byte[] receMsgs = new byte[MAX_BYTE];

    public Server() {
        try {
            datagramSocket = new DatagramSocket(PORT);
            datagramPacket = new DatagramPacket(receMsgs, receMsgs.length);
            while (true) {
                System.out.println("@wait...");
                datagramSocket.receive(datagramPacket);
                String receStr = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                if (receStr.startsWith("@heart-beat")) {
                    replay("@i-m-here");
                    continue;
                }
                Process process = Runtime.getRuntime().exec(receStr.trim());
                InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line;
                int pid = 0;
                replay("@ok-output");
                while ((line = reader.readLine()) != null) {
                   System.out.println(line);
                    replay(line);
                }
                process.destroy();
                replay("@ok");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭socket
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }

    }

    private void replay(String text) {
        byte[] buf = text.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, datagramPacket.getAddress(), datagramPacket.getPort());
        try {
            datagramSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
