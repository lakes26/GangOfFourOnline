package GUI;

import game.Card;
import game.Gamestate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUI implements ActionListener {

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
        JFrame window = new Frame();

        //get contentPane to draw stuff on
        con = window.getContentPane();

        //cards at bottom
        cardsAtBottom = new JPanel(new GridLayout(2,8));
        cardsAtBottom.setBounds(0, 500, 1280, 185);
        cardsAtBottom.setBackground(Color.gray);

        //sort, pass, play, buttons
        miscButtons = new JPanel(new GridLayout(1,4));
        miscButtons.setBounds(0, 400, 400, 100);
        miscButtons.setBackground(Color.gray);

        //info at top
        infoAtTop = new JPanel();
        infoAtTop.setBounds(0, 0, 1280, 180);
        infoAtTop.setBackground(Color.gray);

        //previous hand
        prevHand = new JPanel();
        prevHand.setBounds(400, 200, 500, 200);
        prevHand.setBackground(Color.gray);

        window.setVisible(true);
    }

    public void repaintMiscButtons(){
        miscButtons.removeAll();

        JButton play = new JButton("Play Hand");
        play.addActionListener(e -> {
            //only works if it is your turn
            if(gamestate.getCurrentPlayerID() == playerID) {
                System.out.println("Hand Played" + playHand);
                Card sort1 = new Card(0, -1, 1);
                playHand.remove(sort1);
                choosingHand = false;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                playHand.clear();
            }
        });

        JButton sortValue = new JButton("Sort By Value");
        sortValue.addActionListener(e -> {
            playHand.clear();
            playHand.add(new Card(0, -1, 1));
            choosingHand = false;
        });

        miscButtons.add(play);
        miscButtons.add(sortValue);
        con.add(miscButtons);

        miscButtons.revalidate();
        miscButtons.repaint();
    }

    public void repaintCardsAtBottom(){
        cardsAtBottom.removeAll();

        for(int i = 0; i<currentHand.size(); i++){
            Card theCard = currentHand.get(i);
            button card = new button(i);

            //set card background color
            int color = theCard.getColor();
            if(!playHand.contains(theCard)) {
                if (color == 0)
                    card.setBackground(new Color(0, 100, 0));
                else if (color == 1)
                    card.setBackground(new Color(150, 150, 0));
                else if (color == 2)
                    card.setBackground(new Color(150, 0, 0));
                else
                    card.setBackground(new Color(0, 0, 125));
            }
            else{
                if (color == 0)
                    card.setBackground(new Color(0, 255, 0));
                else if (color == 1)
                    card.setBackground(new Color(255, 255, 0));
                else if (color == 2)
                    card.setBackground(new Color(255, 0, 0));
                else
                    card.setBackground(new Color(0, 0, 255));
            }

            card.setForeground(Color.black);
            card.setText(theCard.toString());
            card.setFont(new Font("Courier New", Font.PLAIN, 15));
            card.addActionListener(new buttonListener(i));
            card.setMaximumSize(new Dimension(50, 50));
            cardsAtBottom.add(card);
        }

        con.add(cardsAtBottom);
        cardsAtBottom.revalidate();
        cardsAtBottom.repaint();
    }


    public void repaintInfoAtTop(){
        infoAtTop.removeAll();

        JLabel currentPlayerLabel = new JLabel("Current Player: " + gamestate.getCurrentPlayerID());
        currentPlayerLabel.setFont(new Font("Courier New", Font.PLAIN, 24));
        currentPlayerLabel.setForeground(Color.black);
        infoAtTop.add(currentPlayerLabel);

        JLabel playerLabel = new JLabel("Your PlayerID: " + playerID);
        playerLabel.setFont(new Font("Courier New", Font.PLAIN, 24));
        playerLabel.setForeground(Color.black);
        infoAtTop.add(playerLabel);


        con.add(infoAtTop);
        infoAtTop.revalidate();
        infoAtTop.repaint();
    }


    public void repaintPrevHand(){
        prevHand.removeAll();

        for(Card card : gamestate.getPrevHandCards()){
            JLabel card1 = new JLabel(card.toString());
            card1.setFont(new Font("Courier New", Font.PLAIN, 24));
            card1.setForeground(Color.black);
            prevHand.add(card1);
        }

        con.add(prevHand);
        prevHand.revalidate();
        prevHand.repaint();
    }

    public void repaint(){
        currentHand = gamestate.players[playerID].hand;

        repaintCardsAtBottom();
        repaintInfoAtTop();
        repaintPrevHand();
        repaintMiscButtons();
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


    //buttonListener for cards
    private class buttonListener implements ActionListener {
        int i; //position of card in hand

        public buttonListener(int i){
            this.i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Card card = currentHand.get(i);
            Card sort1 = new Card(0,-1, 1);
            playHand.remove(sort1);

            if(playHand.contains(card)){
                playHand.remove(card);
            }
            else{
                playHand.add(card);
            }
            repaintCardsAtBottom();
        }

    }
}
