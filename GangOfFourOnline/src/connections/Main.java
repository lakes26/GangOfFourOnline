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
	private static Player[] players = new Player[4];
	private static LinkedList<Packet> packetQueue = new LinkedList<Packet>();
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

		//create server
		ServerSocket ss = new ServerSocket(LISTENING_PORT, 5, InetAddress.getByName(ipAddress));

		System.out.println("Listening on port " + LISTENING_PORT);

		//create connectionListener
		ConnectionListener connectionListener = new ConnectionListener(ss);
		Thread t1 = new Thread(connectionListener);
		t1.start();

		//scanner stops program until enter is pressed
		System.out.println("Press enter when all players connected");
		
		Scanner scnr = new Scanner(System.in);
		System.out.println(scnr.nextLine()); //waits for \n when all players connected


		//start gamestate
	    boolean running = true;

		gamestate = new Gamestate(players);
		gamestate.startGame();
		System.out.println("Starting Game");

	    while(running) {
	    	//send initial gamestate
			for(int i = 0; i <connections.size(); i++) {
				connections.get(i).sendGamestate(gamestate);
			}

			//while game is not over continually poll for packets
	    	while(!gamestate.getGameOver()) {
				System.out.println("waiting for packet");
				Thread.sleep(1000);
	    		if(packetQueue.size() > 0) {
		        	//parse packet for client hand played
	    			gamestate.parsePacket(packetQueue.poll());
					System.out.println(packetQueue.size());
					System.out.println(gamestate.getPrevHandCards());

		        	//connection handlers send out gamestate to each client
		        	for(int i = 0; i < connections.size(); i++) {
		        		connections.get(i).sendGamestate(gamestate);
		        	}
		        }
	    	}

	    	//end server if q is entered in server stdin
			System.out.println("Press q to end game");
	    	if(scnr.nextLine().equalsIgnoreCase("q")) {
    			running = false;
    		} else {
    			gamestate.startRound();
    		}
	    }
	    //close all connections
		for(int i = 0; i <connections.size(); i++) {
			connections.get(i).setActive(false);
		}
	    scnr.close();
	}

	public static void addPacketToQueue(Packet p) {
		Main.packetQueue.add(p);
	}

	public static void addPlayer(Player player){
		if(player.getPlayerID()>3)
			System.out.println("There are already 4 players");
		else
			Main.players[player.getPlayerID()] = player;
	}
	
	public static void addConnection(ConnectionHandler ch) {
		Main.connections.add(ch);
	}

	public static int getLISTENING_PORT() {
		return LISTENING_PORT;
	}

}
