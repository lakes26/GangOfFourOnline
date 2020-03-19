package connections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionListener implements Runnable {
		private ServerSocket ss;
		private boolean running;
		private ArrayList<InetAddress> allAddresses;
		private static int playerID = 0;
		
		
		public ConnectionListener(ServerSocket ss) {
			this.ss = ss;
			this.allAddresses = new ArrayList<InetAddress>();
		}
		
		public void run() {
			//create socket connection up to 4 players
			int i = 0;
			while(i<4) {
				Socket connection = null;
				try {
					connection = ss.accept();
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
				allAddresses.add(connection.getInetAddress());
				ConnectionHandler ch = new ConnectionHandler(connection);
				ch.start();
				i++;
			}
		}
		
		public void close() {
			running = false;
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public static int getNextPlayerID() {
			return playerID;
		}

		public static void incrementPlayerID() {
			playerID++;		
		}		
}
