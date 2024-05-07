package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MemoryBoard {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Memory Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel = new JPanel(new GridLayout(4,4));

        for (int i = 0; i< 16; i++){
            MemoryCard card = new MemoryCard();
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            card.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(card);
        }

        frame.add(panel);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

static class MemoryCard extends JLabel implements MouseListener{

    ImageIcon backgroundIcon = new ImageIcon("C:\\Users\\nicol\\IdeaProjects\\MemoryGameTeko\\src\\main\\java\\org\\example\\Muster.jpg");
    ImageIcon ape = new ImageIcon("C:\\Users\\nicol\\IdeaProjects\\MemoryGameTeko\\src\\main\\java\\org\\example\\ape.png");
    ImageIcon eagle = new ImageIcon("C:\\Users\\nicol\\IdeaProjects\\MemoryGameTeko\\src\\main\\java\\org\\example\\eagle.png");

    boolean isFlipped = false;

    public MemoryCard(){
        setIcon(backgroundIcon);
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isFlipped){
            setIcon(ape);
            isFlipped = true;
        }else{
            setIcon(backgroundIcon);
            isFlipped = false;
        }

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
