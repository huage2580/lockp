package com.hua.lockp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
    private DatagramSocket datagramSocket;
    private InetAddress address;
    public static final String HEART_BEAT = "@heart-beat";
    public static final String POWER_EVENT = "input keyevent 26";
    public static final String HEART_BEAT_OK = "@i-m-here";
    public static final String OK = "@ok";
    ReciverThread reciverThread;
    public Client(int port,MsgCallBack callBack) {

        try {
            datagramSocket = new DatagramSocket(port);
            datagramSocket.setSoTimeout(3000);
            address =  InetAddress.getByName("127.0.0.1");
            reciverThread = new ReciverThread(datagramSocket,callBack);
            reciverThread.start();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg){
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf,0,buf.length,address,5666);
        try {
            datagramSocket.send(packet);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void close(){
        datagramSocket.close();
        reciverThread.setRun(false);
    }

    class ReciverThread extends Thread{
        DatagramSocket socket;
        DatagramPacket packet;
        private MsgCallBack callBack;
        private boolean run = true;

        public void setRun(boolean run) {
            this.run = run;
        }

        public ReciverThread(DatagramSocket socket, MsgCallBack callBack) {
            this.socket = socket;
            this.callBack = callBack;
        }

        @Override
        public void run() {
            byte[] buff = new byte[1024];
            packet = new DatagramPacket(buff,0,buff.length);
            while (run){
                try {
                    socket.receive(packet);
                    String receStr = new String(packet.getData(), 0 , packet.getLength());
                    if (callBack!=null){
                        callBack.onMsg(receStr);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public interface MsgCallBack{
        void onMsg(String text);
    }
}
