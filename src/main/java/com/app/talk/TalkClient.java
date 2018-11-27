package com.app.talk;

import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;
import com.app.talk.common.User;
import com.app.talk.communication.CommunicatorFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * A client to connect to a TalkServer.
 */
public class TalkClient {
    /**
     * Client socket.
     */
    private Socket socket;
    /**
     * User Object containing username.
     */
    private User user = null;

    /**
     * Client constructor.
     *
     * @param config client configuration
     * @throws IOException
     */
    private TalkClient(Config config, User user) throws IOException {
        this.user = user;
        this.socket = new Socket(config.getRemoteHost(), config.getPort());
    }

    private void run() throws IOException {
        System.out.println("Trying to connect to remote " + socket.getInetAddress() + ":" + socket.getPort());
        CommunicatorFactory.getInstance().createCommunicator(socket, user);
    }

    /**
     * Starts the client.
     *
     * @param args
     * @throws ConfigParserException
     * @throws IOException
     */
    public static void main(String[] args) throws ConfigParserException, IOException {
        ConfigParser configParser = new ConfigParser(args);
        Config config = configParser.getConfig();

        User user = new User();
        user.setNameFromUserInput();

        TalkClient client = new TalkClient(config, user);
        client.run();
    }
}
