package Client;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;

import game.*;
import GUI.*;

public class Client {

    public static void main(String[] args) {
    	
    	Scanner sc = new Scanner(System.in);
    	boolean IPread = false;
    	
    	InetAddress ip = null;
    	int port = 0;
    	
    	while(!IPread) {
    		System.out.print("Enter the server IP:  ");
    		try {
    			
    			String serverIP = sc.nextLine();
    			ip = InetAddress.getByName(serverIP);
    			System.out.println(ip);
    			IPread = true;
    		} catch(Exception e) {
    			System.out.println("Invalid Address. Enter the server IP");
    		}
    	}
    	
    	boolean portRead = false;
    	while(!portRead) {
    		try {
    			System.out.print("Enter the port:  ");
    			String portString = sc.nextLine();
    			port = Integer.parseInt(portString);
    			portRead = true;
    		} catch(Exception e) {
    			System.out.println("Invalid port. Enter the port");
    		}
    	}
    	
    	sc.close();
    	
        try
        {
            Socket s = new Socket(ip, port);
            System.out.println("connected");

            //read and write streams
            InputStream dis = s.getInputStream();
            OutputStream dos = s.getOutputStream();

            ObjectOutputStream oos = new ObjectOutputStream(dos);
            ObjectInputStream ois = new ObjectInputStream(dis);


            List<Card> sendHand;

            //get playerID
            int playerID = ois.readInt();
            System.out.println("Your playerID: " + playerID);

            //get gamestate
            Gamestate game = (Gamestate) ois.readObject();

            //create new GUI
            GUI gui;
            gui = new GUI(playerID, game);

            //Gameloop
            boolean gameOver = false;
            while(!gameOver) {
                gui.setGamestate(game);
                gui.repaint();

                //while not your turn, keep updating gamestate for other people
                while(game.getCurrentPlayerID() != playerID){
                    game = (Gamestate) ois.readObject();
                    gui.setGamestate(game);
                    gui.repaint();
                }

                //when your turn, wait until play button is pressed
                while(gui.getChoosingHand()){
                    Thread.sleep(100);
                }

                //send Hand to Server
                gui.setChoosingHand(true);

                oos.reset();
                oos.writeObject(gui.getPlayHand());
                System.out.println("Hand sent");

                //get gamestate back and check if gameOver
                game = (Gamestate) ois.readObject();
                gameOver = game.getGameOver();
                Thread.sleep(100);
            }


            //GameOver Close Socket
            System.out.println("Closing socket");
            dis.close();
            dos.close();
            s.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
