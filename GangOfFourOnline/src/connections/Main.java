package connections;

import game.Gamestate;
import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	private static ArrayList<ConnectionHandler> connections = new ArrayList<>();
	private static int LISTENING_PORT = 42022;
	private static Player[] players = new Player[4];
	private static LinkedList<Packet> packetQueue = new LinkedList<>();
	
	public static void main(String[] args) throws IOException, InterruptedException {

		//create server
		String ipAddress = "192.168.86.243";
		ServerSocket ss = new ServerSocket(LISTENING_PORT, 5, InetAddress.getByName(ipAddress));
		
		String systemipaddress = ""; 
        try
        { 
            URL url_name = new URL("http://bot.whatismyipaddress.com"); 
  
            BufferedReader sc = new BufferedReader(new BufferedReader(new InputStreamReader(url_name.openStream()))); 
  
            // reads system IPAddress 
            systemipaddress = sc.readLine().trim();
            
            System.out.println("Public IP Address: " + systemipaddress);            
        } 
        catch (Exception e) 
        { 
            systemipaddress = "Cannot Retreive Public IP Address"; 
        } 
         
        System.out.println("Listening on port " + LISTENING_PORT + "\n");
		

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

		Gamestate gamestate = new Gamestate(players);
		gamestate.startGame();
		System.out.println("Starting Game");

	    while(running) {
	    	//send initial gamestate
			for (ConnectionHandler connection : connections) {
				connection.sendGamestate(gamestate);
			}

			//while game is not over continually poll for packets
	    	while(!gamestate.getGameOver()) {
				Thread.sleep(1000);

	    		if(packetQueue.size() > 0) {
		        	//parse packet for client hand played
	    			gamestate.parsePacket(packetQueue.poll());

		        	//connection handlers send out gamestate to each client
					for (ConnectionHandler connection : connections) {
						connection.sendGamestate(gamestate);
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
		for (ConnectionHandler connection : connections) {
			connection.setActive(false);
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
