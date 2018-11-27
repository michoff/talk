package com.app.talk.communication;

import com.app.talk.common.User;

import java.io.IOException;
import java.net.Socket;

public class CommunicatorFactory {

    private static CommunicatorFactory single = new CommunicatorFactory();

    private CommunicatorFactory() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Returns the single factory object.
     *
     * @return CommunicatorFactory the single Factory
     */
    public static CommunicatorFactory getInstance() {
        return single;
    }

    /**
     * Server Communicator.
     *
     * @param socket
     * @return Communicator object
     */
    public Communicator createCommunicator(Socket socket, User user) throws IOException {
        return new Communicator(socket, user);
    }

    /**
     * Client Communicator.
     *
     * @param socket
     * @return Communicator object
     */
    public Communicator createCommunicator(Socket socket) throws IOException {
        return new Communicator(socket);
    }
}
