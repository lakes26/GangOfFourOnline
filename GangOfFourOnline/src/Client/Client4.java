package Client;

import java.io.*;
import java.net.*;
import java.util.List;
import game.*;
import GUI.*;

public class Client4 {

    public static void main(String[] args) {
        try
        {
            //connect to server
            InetAddress ip = InetAddress.getByName("localhost");
            Socket s = new Socket(ip, 42022);

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

                while(game.getCurrentPlayerID() != playerID){
                    game = (Gamestate) ois.readObject();
                    gui.setGamestate(game);
                    gui.repaint();
                }

                while(gui.getChoosingHand()){
                    Thread.sleep(100);
                }
                sendHand = gui.getPlayHand();
                System.out.println("Hand sent");
                gui.setChoosingHand(true);

                oos.reset();
                oos.writeObject(sendHand);

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
