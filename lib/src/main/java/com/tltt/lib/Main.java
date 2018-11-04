package com.tltt.lib;

import java.util.concurrent.TimeUnit;

public class Main {

    public static TCPServer server;
    public static void main(String [] args) throws InterruptedException {
        server = new TCPServer("0.0.0.0", 5000);
        server.open();

        while(server.isRunning())
            TimeUnit.SECONDS.sleep(1);
    }

    public static void Log(String log) {
        System.out.println(log);
    }
}
