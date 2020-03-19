package GUI;

import javax.swing.*;

public class button extends JButton {
    int cardNumber;

    //create JButton with associated int to know which card in list of cards
    public button (int cardNumber){
        super();
        this.cardNumber = cardNumber;
    }
}
