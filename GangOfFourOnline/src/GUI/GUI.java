package GUI;

import game.Card;
import game.Gamestate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class GUI implements ActionListener {

    private JFrame window;
    private Container con;
    private JPanel cardsAtBottom, miscButtons, prevHand;
    private JPanel infoAtTop;
    private Gamestate gamestate;
    private int playerID;
    private List<Card> currentHand = new ArrayList<>();
    private List<Card> playHand = new ArrayList<>();
    private Boolean choosingHand = true;

    public GUI(int playerID, Gamestate gamestate){
        //playerID and gamestate to identify
        this.playerID = playerID;
        this.gamestate = gamestate;

        //create new Frame
        window = new Frame();

        //get contentPane to draw stuff on
        con = window.getContentPane();

        //cards at bottom
        cardsAtBottom = new JPanel(new GridLayout(2,8));
        cardsAtBottom.setBounds(0, 500, 1280, 185);
        cardsAtBottom.setBackground(Color.gray);

        //get hand from gamestate

        currentHand = gamestate.players[playerID].hand;

        //draw cards on screen
        for(int i = 0; i<currentHand.size(); i++){
            button card = new button(i);
            card.setBackground(Color.black);
            card.setForeground(Color.white);
            card.setText(currentHand.get(i).toString());
            card.addActionListener(new buttonListener(i));
            card.setMaximumSize(new Dimension(50, 50));
            cardsAtBottom.add(card);
        }
        con.add(cardsAtBottom);

        //sort, pass, play, buttons
        miscButtons = new JPanel(new GridLayout(1,4));
        miscButtons.setBounds(0, 400, 400, 100);
        miscButtons.setBackground(Color.gray);

        //play and sortValue
        JButton play = new JButton("Play Hand");
        JButton sortValue = new JButton("Sort By Value");

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("play hand");
                choosingHand = false;
            } });
        sortValue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playHand.clear();
                playHand.add(new Card(0, -1));
                choosingHand = false;
            } });
        miscButtons.add(play);
        miscButtons.add(sortValue);
        con.add(miscButtons);


        //info at top
        infoAtTop = new JPanel();
        infoAtTop.setBounds(0, 0, 1280, 180);
        infoAtTop.setBackground(Color.gray);

        JLabel currentPlayerLabel = new JLabel("Current Player: " + gamestate.getCurrentPlayerID());
        currentPlayerLabel.setFont(new Font("Courier New", Font.PLAIN, 24));
        currentPlayerLabel.setForeground(Color.black);
        infoAtTop.add(currentPlayerLabel);

        JLabel playerLabel = new JLabel("Your PlayerID: " + playerID);
        playerLabel.setFont(new Font("Courier New", Font.PLAIN, 24));
        playerLabel.setForeground(Color.black);
        infoAtTop.add(playerLabel);
        con.add(infoAtTop);


        //previous hand jpanel

        prevHand = new JPanel();
        prevHand.setBounds(400, 200, 500, 200);
        prevHand.setBackground(Color.gray);
        for(Card card : gamestate.getPrevHandCards()){
            JLabel card1 = new JLabel(card.toString());
            playerLabel.setFont(new Font("Courier New", Font.PLAIN, 24));
            playerLabel.setForeground(Color.black);
            prevHand.add(playerLabel);
        }
        con.add(prevHand);

        window.setVisible(true);
    }

    public void repaint(){

        currentHand = gamestate.players[playerID].hand;
        System.out.println(currentHand);
        cardsAtBottom.removeAll();
        //draw cards on screen
        for(int i = 0; i<currentHand.size(); i++){
            button card = new button(i);
            card.setBackground(Color.black);
            card.setForeground(Color.white);
            card.setText(currentHand.get(i).toString());
            card.addActionListener(new buttonListener(i));
            card.setMaximumSize(new Dimension(50, 50));
            cardsAtBottom.add(card);
        }
        con.add(cardsAtBottom);
    }
    public void setGamestate(Gamestate gamestate){
        this.gamestate = gamestate;
    }

    public List<Card> getPlayHand() {
        return playHand;
    }

    public Boolean getChoosingHand() {
        return choosingHand;
    }

    public void setChoosingHand(Boolean choosingHand) {
        this.choosingHand = choosingHand;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    //    public static void main(String[] args) {
//        new GUI();
//    }

    private class buttonListener implements ActionListener {
        int i;

        public buttonListener(int i){
            this.i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Card card = currentHand.get(i);
            if(playHand.contains(card)){
                playHand.remove(card);
            }
            else{
                playHand.add(card);
            }
            System.out.println(playHand);
        }

    }
}
