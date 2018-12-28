package com.app.talk.communication;

import com.app.talk.Receiver;
import com.app.talk.Sender;
import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;
import com.app.talk.command.RemoteCommandProcessor;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * A combination of a sender and a receiver threads.
 */
public class Communicator {
	Context context;
	private Socket socket;
	private Sender sender;
	private Receiver receiver;
	private RemoteCommandProcessor commandProcessor;
	private Thread senderThread;
	private Thread receiverThread;
	private Thread commandProcessorThread;
	
	
	/**
	 * The constructor creates and activates the two threads. One for the sender (+ given user name), one for the receiver
	 * 
	 * @param socket
	 */
	Communicator(Socket socket) throws IOException {
		this.socket = socket;
		this.init();		
	} //constructor
	
	/**
	 * fetches socket
	 * @return socket object.
	 */
	public Socket getSocket(){
		return socket;
	}
	/**
	 * fetches sender.
	 * @return sender object.
	 */
	public Sender getSender() {
		return sender;
	} 
	
	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}
	
	/**
	 * fetches receiver.
	 * @return receiver object.
	 */
	public Receiver getReceiver() {
		return receiver;
	} 
	/**
	 * fetches sender thread
	 * @return thread object.
	 */
	public Thread getSenderThread(){
		return senderThread;
	}
	
    /**
     * Creates a Sender and a Receiver object.
     */
    private void init() throws IOException {
    	System.out.println("Trying to connect to remote " + socket.getInetAddress() + ":" + socket.getPort());    	
    	
		this.sender = new Sender(this.socket);
	    senderThread = new Thread(sender);    	
    	
    	this.receiver = new Receiver(this.socket);
        receiverThread = new Thread(receiver);

       

        // Given the thread a name
        receiverThread.setName(this.socket.getLocalPort() + " -> " + this.socket.getPort() + "-Receiver");
        senderThread.setName(this.socket.getLocalPort() + " -> " + this.socket.getPort() + "-Sender");
        

        
    } //start

    
    public void start() throws IOException {
    	initCommandProcessor();
		commandProcessorThread.start();
        receiverThread.start();
        senderThread.start();
	}
    
    private void initCommandProcessor() {
    	commandProcessor = new RemoteCommandProcessor(this.context);
    	commandProcessorThread = new Thread(commandProcessor);
    	Observer observer = new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				RemoteCommand remoteCommand = (RemoteCommand)arg;			
				try {
					commandProcessor.put(remoteCommand);
				} catch (InterruptedException e) {
					e.printStackTrace();
					//This is ok
				}
			}
		};
	        
		this.receiver.addObserver(observer);
	}
    
	/**
     *   
     * @param heartbeat the timeout to set
     */
    void setHeartbeat(RemoteCommand heartbeat) {
		this.sender.setHeartbeat(heartbeat);
	}

	/**
	 * @param timeout the timeout to set
	 */
	void setHeartbeatTimeout(long timeout) {
		this.sender.setTimeout(timeout);
	}
    
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		senderThread.interrupt();
		commandProcessorThread.interrupt();
	}

	public void setContext(Context context) {
		this.context = context;
		
	}
} //Communicator Class
