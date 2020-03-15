package connections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import game.Gamestate;
import game.Player;

public class Main {
	private static ArrayList<ConnectionHandler> connections = new ArrayList<ConnectionHandler>();
	private static int LISTENING_PORT = 42022;
	private static String ipAddress = "localhost";
	private static Gamestate gamestate;
	private static Player[] players;
	private static LinkedList<Packet> packetQueue = new LinkedList<Packet>();
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		ServerSocket ss = new ServerSocket(LISTENING_PORT, 5, InetAddress.getByName(ipAddress));

		System.out.println("Listening on port " + LISTENING_PORT);
		
		ConnectionListener connectionListener = new ConnectionListener(ss);
		System.out.println("Press enter when all players connected");
		
		Scanner scnr = new Scanner(System.in);
	    scnr.nextLine();
	    
	    boolean running = true;
	    connectionListener.close();
	    
	    players = new Player[]{new Player(0), new Player(1), new Player(2), new Player(3)};
		gamestate = new Gamestate(players);
		gamestate.startGame();
	    
	    while(running) {
	    	System.out.println("Starting Game");
	    	while(gamestate.getGameOver()) {
	    		if(packetQueue.size() > 0) {
		        	gamestate.parsePacket(packetQueue.poll());
		        	//here, tell all connection handlers to send the gamestate
		        	for(int i = 0; i <connections.size(); i++) {
		        		connections.get(i).sendGamestate(gamestate);
		        	}
		        }
	    	}
	    	while(!scnr.hasNextLine()) {
	    		
	    	}
	    	if(scnr.nextLine().equalsIgnoreCase("q")) {
    			running = false;
    		} else {
    			gamestate.startRound();
    		} 
	    }
	    scnr.close();
	}

	public static void addPacketToQueue(Packet p) {
		Main.packetQueue.add(p);
	}
	
	public static void addConnection(ConnectionHandler ch) {
		Main.connections.add(ch);
	}

	public static int getLISTENING_PORT() {
		return LISTENING_PORT;
	}

}
