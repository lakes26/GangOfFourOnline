package GUI;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    Component component;
    public Frame()
    {
        this.setTitle("Gang of Four");
        this.setSize(1280,720);
        this.setLocation(100, 50);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.white);
        this.setLayout(null);
        this.setResizable(true);
    }
}
