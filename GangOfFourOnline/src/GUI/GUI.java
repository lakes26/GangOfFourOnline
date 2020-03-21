package GUI;

import game.Card;
import game.Gamestate;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUI implements ActionListener {

    private Container con;
    private JPanel cardsAtBottom, playButtons, prevHand, cardsLeft, scoreboard;
    private JPanel infoAtTop, sortButtons;
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

        //info at top middle
        infoAtTop = new JPanel(new GridLayout(2, 1));
        infoAtTop.setBounds(450, 0, 380, 100);

        //cardsLeft at top left
        cardsLeft = new JPanel(new GridLayout(2, 4));
        cardsLeft.setBounds(0, 0, 400, 100);

        //scoreboard at top right
        scoreboard = new JPanel(new GridLayout(2, 4));
        scoreboard.setBounds(880, 0, 400, 100);

        //pass, play, buttons in middle left
        playButtons = new JPanel(new GridLayout(2,1));
        playButtons.setBounds(0, 230, 250, 150);

        //previous hand in middle
        prevHand = new JPanel(new FlowLayout());
        prevHand.setBounds(315, 175, 650, 175);
        prevHand.setBackground(Color.gray);

        //sort buttons in middle right
        sortButtons = new JPanel(new GridLayout(2,1));
        sortButtons.setBounds(1030, 230, 250, 150);

        //cards at bottom
        cardsAtBottom = new JPanel(new FlowLayout());
        cardsAtBottom.setBounds(0, 380, 1280, 300);

        window.setVisible(true);
    }

    public void repaint(){
        //need this for cardsAtBottom?
        currentHand = gamestate.players[playerID].hand;

        repaintCardsAtBottom();
        repaintInfoAtTop();
        repaintPrevHand();
        repaintPlayButtons();
        repaintSortButtons();
        repaintCardsLeft();
        repaintScoreboard();
    }

    public void repaintScoreboard(){
        scoreboard.removeAll();
        for(int i = 0; i<4; i++){
            JLabel aPlayerLabel = new JLabel("Player " + i, SwingConstants.CENTER);
            aPlayerLabel.setFont(new Font("Courier New", Font.BOLD, 12));
            aPlayerLabel.setForeground(Color.black);
            scoreboard.add(aPlayerLabel);
        }
        int[] playerScores = gamestate.getPlayerScores();

        for(int i = 0; i<4; i++){
            JLabel scoreLabel = new JLabel(String.valueOf(playerScores[i]), SwingConstants.CENTER);
            scoreLabel.setFont(new Font("Courier New", Font.BOLD, 24));
            scoreLabel.setForeground(Color.black);
            scoreboard.add(scoreLabel);
        }

        con.add(scoreboard);

        scoreboard.revalidate();
        scoreboard.repaint();
    }

    public void repaintCardsLeft(){
        cardsLeft.removeAll();

        for(int i = 0; i<4; i++){
            JLabel aPlayerLabel = new JLabel("Player " + i, SwingConstants.CENTER);
            aPlayerLabel.setFont(new Font("Courier New", Font.BOLD, 12));
            aPlayerLabel.setForeground(Color.black);
            cardsLeft.add(aPlayerLabel);
        }
        Player[] players = gamestate.getPlayers();

        for(int i = 0; i<4; i++){
            JLabel cardsLeftLabel = new JLabel(String.valueOf(players[i].getNumCards()), SwingConstants.CENTER);
            cardsLeftLabel.setFont(new Font("Courier New", Font.BOLD, 24));
            cardsLeftLabel.setForeground(Color.black);
            cardsLeft.add(cardsLeftLabel);
        }

        con.add(cardsLeft);

        cardsLeft.revalidate();
        cardsLeft.repaint();
    }

    public void repaintSortButtons(){
        sortButtons.removeAll();

        JButton sortValue = new JButton("Sort by Value");
        sortValue.setFont(new Font("Courier New", Font.BOLD, 18));
        sortValue.addActionListener(e -> {
            playHand.clear();
            playHand.add(new Card(0, -1, 1));
            choosingHand = false;
        });

        JButton sortColor = new JButton("Sort by Color");
        sortColor.setFont(new Font("Courier New", Font.BOLD, 18));
        sortColor.addActionListener(e -> {
            playHand.clear();
            playHand.add(new Card(0, -2, 1));
            choosingHand = false;
        });

        sortButtons.add(sortColor);
        sortButtons.add(sortValue);
        con.add(sortButtons);

        sortButtons.revalidate();
        sortButtons.repaint();

    }

    public void repaintPlayButtons(){
        playButtons.removeAll();

        JButton play = new JButton("Play Hand");
        play.setFont(new Font("Courier New", Font.BOLD, 18));
        play.addActionListener(e -> {
            //only works if it is your turn
            if(gamestate.getCurrentPlayerID() == playerID && !playHand.isEmpty()) {
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
            else{
                //TODO display a popup message
            }
        });

        JButton pass = new JButton("Pass");
        pass.setFont(new Font("Courier New", Font.BOLD, 18));
        pass.addActionListener(e -> {
            //only works if it is your turn
            if(gamestate.getCurrentPlayerID() == playerID) {
                System.out.println("Passed");
                playHand.clear();
                Card sort1 = new Card(0, -1, 1);
                playHand.remove(sort1);
                choosingHand = false;
            }
            else{
                //TODO display a popup message
            }
        });

        playButtons.add(play);
        playButtons.add(pass);
        con.add(playButtons);

        playButtons.revalidate();
        playButtons.repaint();
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
                    card.setBackground(new Color(0, 125, 0));
                else if (color == 1)
                    card.setBackground(new Color(175, 175, 0));
                else if (color == 2)
                    card.setBackground(new Color(125, 0, 0));
                else
                    card.setBackground(new Color(0, 50, 125));
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

            int value = theCard.getValue();
            if(value < 11) {
                card.setText(String.valueOf(value));
                card.setFont(new Font("Courier New", Font.BOLD, 40));
            }
            else if (value == 11) {
                card.setText("Phoenix");
                card.setFont(new Font("Courier New", Font.BOLD, 20));
            }
            else {
                card.setText("Dragon");
                card.setFont(new Font("Courier New", Font.BOLD, 20));
            }
            card.addActionListener(new buttonListener(i));
            card.setPreferredSize(new Dimension(125, 150));
            cardsAtBottom.add(card);
        }

        con.add(cardsAtBottom);
        cardsAtBottom.revalidate();
        cardsAtBottom.repaint();
    }


    public void repaintInfoAtTop(){
        infoAtTop.removeAll();

        JLabel currentPlayerLabel = new JLabel("Current Player: " + gamestate.getCurrentPlayerID(), SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Courier New", Font.PLAIN, 24));
        currentPlayerLabel.setForeground(Color.black);
        infoAtTop.add(currentPlayerLabel);

        JLabel playerLabel = new JLabel("Your PlayerID: " + playerID, SwingConstants.CENTER);
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
            JLabel card1 = new JLabel(card.toString(), SwingConstants.CENTER);
            int value = card.getValue();
            if(value < 11) {
                card1.setText(String.valueOf(value));
                card1.setFont(new Font("Courier New", Font.BOLD, 40));
            }
            else if (value == 11) {
                card1.setText("Phoenix");
                card1.setFont(new Font("Courier New", Font.BOLD, 20));
            }
            else {
                card1.setText("Dragon");
                card1.setFont(new Font("Courier New", Font.BOLD, 20));
            }

            card1.setForeground(Color.black);
            card1.setOpaque(true);

            int color = card.getColor();
            if (color == 0)
                card1.setBackground(new Color(0, 150, 0));
            else if (color == 1)
                card1.setBackground(new Color(200, 200, 0));
            else if (color == 2)
                card1.setBackground(new Color(175, 0, 0));
            else
                card1.setBackground(new Color(0, 50, 150));
            card1.setPreferredSize(new Dimension(125,150));
            prevHand.add(card1);
        }

        con.add(prevHand);
        prevHand.revalidate();
        prevHand.repaint();
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
