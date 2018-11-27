package com.app.talk.communication;

import com.app.talk.Receiver;
import com.app.talk.Sender;
import com.app.talk.common.User;

import java.io.IOException;
import java.net.Socket;

/**
 * A combination of a sender and a receiver threads.
 */
class Communicator {

    private Socket socket;
    private User user;

    /**
     * Communicator for Client
     *
     * @param socket
     */
    Communicator(Socket socket, User user) throws IOException {
        this.socket = socket;
        this.user = user;
        Sender sender = new Sender(this.socket, this.user);
        Thread senderThread = new Thread(sender);

        senderThread.start();
    }

    /**
     * Communicator for Server
     *
     * @param socket
     */
    Communicator(Socket socket) throws IOException {
        this.socket = socket;
        Receiver receiver = new Receiver(this.socket);
        Thread receiverThread = new Thread(receiver);

        receiverThread.start();
    }
}
