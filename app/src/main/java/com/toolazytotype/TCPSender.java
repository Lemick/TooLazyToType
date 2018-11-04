package com.toolazytotype;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPSender implements Action {

    public static final String LOG_TAG = "TCPSender";

    @Override
    public void trigger(String context) {
        AsyncTCPAction tcpTask = new AsyncTCPAction();
        tcpTask.execute(context);
    }

    private class AsyncTCPAction extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... args) {
            Socket socket;
            try {
                SocketAddress sockAddress = new InetSocketAddress("192.168.1.10", 5000);
                socket = new Socket();
                socket.connect(sockAddress, 1000);
                OutputStream out = socket.getOutputStream();
                PrintWriter output = new PrintWriter(out);
                output.println(args[0]);
                output.flush();
                socket.close();
                Log.d(LOG_TAG, "TCP message successfully sent");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


}
