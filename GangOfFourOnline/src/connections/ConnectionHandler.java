package connections;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import game.Card;
import game.Gamestate;
import game.Player;


public class ConnectionHandler extends Thread{
	private Socket clientSocket; //The connection to the client.
	private InputStream inStream;
	private OutputStream outStream;
	private ObjectOutputStream objOut;
	private ObjectInputStream objIn;
	
	private boolean active = true;
	private Packet packet;
	private Player player;
	
	public ConnectionHandler(Socket socket) {
		this.clientSocket = socket;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		String clientAddress = clientSocket.getInetAddress().toString();
		try {
			System.out.println("Connection from " + clientAddress);
			Main.addConnection(this);
			this.inStream = clientSocket.getInputStream();
			this.outStream = clientSocket.getOutputStream();
		} catch (Exception e) {
			System.out.println("error in connection handler thread");
		}
		
		this.player = new Player(ConnectionListener.getNextPlayerID());
		ConnectionListener.incrementPlayerID();
		
		try {
			objOut = new ObjectOutputStream(outStream);
			objIn = new ObjectInputStream(inStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(active) {
			try {
				ArrayList<Card> cards = (ArrayList<Card>) objIn.readObject();
				this.sendPacketToQueue(cards);
				Thread.sleep(100);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			clientSocket.close();
			System.out.println("disconnected " + clientAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPacketToQueue(ArrayList<Card> cards) {
		int sortRequest = 0;
		if((cards.size() == 1) && cards.get(0).getValue() == -1 || cards.get(0).getValue() == -2) {
			sortRequest = Math.abs(cards.get(0).getValue());
		}
		packet = new Packet(this.player.getPlayerID(), sortRequest, cards);
		Main.addPacketToQueue(packet);
	}
	
	
	public void sendGamestate(Gamestate gamestate) {
		try {
			objOut.writeObject(gamestate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
}
