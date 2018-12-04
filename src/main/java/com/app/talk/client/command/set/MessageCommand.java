package com.app.talk.client.command.set;

import com.app.talk.command.RemoteCommand;
import com.app.talk.communication.Communicator;

/**
 * Represents a command that will write a message to standard output.
 */
public class MessageCommand implements RemoteCommand {
    private static final long serialVersionUID = 8308526182902674398L;
    /**
     * textual message to be shown.
     */
    private String message;

    /**
     * A command that prints a message.
     *
     * @param message the message to send
     */
    public MessageCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(Communicator communicator) {
        System.out.println(message);
    }
} //MessageCommand Class
