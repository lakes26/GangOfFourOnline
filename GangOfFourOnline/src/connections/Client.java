package connections;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import game.*;
import GUI.*;

public class Client {

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



            //player Hand
            List<Card> cards = new ArrayList<>();
            List<Card> sendHand = new ArrayList<>();
//            testSort.add(new Card(1, -1));

            //get playerID
            int playerID = ois.readInt();
            System.out.println("Your playerID: " + playerID);

            //get gamestate
            Gamestate game = (Gamestate) ois.readObject();

            //create new GUI
            GUI gui;
            gui = new GUI(playerID, game);

            //Gameloop
            Boolean gameOver = false;
            Boolean choosingHand = true;
            while(!gameOver) {
                gui.setGamestate(game);
                gui.repaint();

                while(choosingHand){
                    choosingHand = gui.getChoosingHand();
                    Thread.sleep(1000);
                }

                sendHand = gui.getPlayHand();

                oos.reset();
                oos.writeObject(sendHand);

                game = (Gamestate) ois.readObject();
                gameOver = game.getGameOver();
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
