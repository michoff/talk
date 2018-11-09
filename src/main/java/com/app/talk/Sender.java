package com.app.talk;

import java.net.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * A simple sender of network traffic.
 */
public class Sender implements Runnable {

    private String remoteHost;
    private int port;
    private Socket client = null;
    private DataOutputStream outputStream = null;
    private String userName;
    private Scanner scanner = new Scanner(System.in);

    /**
     * A sender of information over the network.
     *
     * @param remoteHost - remote machine to talk to.
     * @param port       - remote port to talk to.
     */
    Sender(String remoteHost, int port, String userName) {
        this.remoteHost = remoteHost;
        this.port = port;
        this.userName = userName;
    }


    public void run() {
        System.out.println("Waiting for connection to: " + this.remoteHost + ":" + this.port + "...");
        this.establishConnection();
        System.out.println("Connection established.");

        this.setOutputStream();

        this.sendUserName();

        this.sendUserInput();

        this.closeConnection();
        System.out.println("Connection closed.");
    }

    private void sendUserName() {
        this.sendMsg("?userName&" + this.userName);
    }

    private void establishConnection() {
        try {
            this.connect();
        } catch (IOException e) {
            this.reconnect();
        }
    }

    private void connect() throws IOException {
        this.client = new Socket(this.remoteHost, this.port);
    }

    private void reconnect() {
        try {
            TimeUnit.SECONDS.sleep(10);
            this.establishConnection();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void setOutputStream() {
        try {
            this.outputStream = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + this.remoteHost);
        }
    }

    private void sendUserInput() {
        String userInput;

        while (true) {
            userInput = scanner.nextLine();

            this.sendMsg(userInput);

            if (Objects.equals("end", userInput)) {
                break;
            }
        }
    }

    private void sendMsg(String msg) {
        try {
            this.outputStream.writeBytes(msg + "\n");
            this.outputStream.flush();
        } catch (IOException e) {
            System.err.println("IOException: " + e);
        }
    }

    private void closeConnection() {
        try {
        	this.client.close(); //closes OutputStream as well
        } catch (IOException e) {
        	System.err.println("IOException: " + e);
        }
        this.scanner.close();
    }
}
