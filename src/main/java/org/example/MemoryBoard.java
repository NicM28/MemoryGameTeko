package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MemoryBoard {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Memory Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel = new JPanel(new GridLayout(6,6));

        for (int i = 0; i< 36; i++){
            ImageIcon icon = new ImageIcon("Muster.jpg");
            JLabel card = new JLabel(icon);
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            card.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(card);
        }

        frame.add(panel);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

static class MemoryCard extends JLabel implements MouseListener{



    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}


}
