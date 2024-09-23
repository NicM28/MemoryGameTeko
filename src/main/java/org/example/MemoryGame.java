package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame extends JFrame {
    private JPanel cardPanel;
    private JLabel scoreLabel1, scoreLabel2, currentPlayerLabel;
    private JButton restartButton;
    private ArrayList<Card> cards;
    private Card firstCard, secondCard;
    private Timer timer;
    private int currentPlayer = 1;
    private int score1 = 0, score2 = 0;
    private int pairsFound = 0;
    private boolean hardMode = false;

    public MemoryGame() {
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        cardPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        add(cardPanel, BorderLayout.CENTER);

        JPanel scorePanel = new JPanel();
        scoreLabel1 = new JLabel("Player 1: 0");
        scoreLabel2 = new JLabel("Player 2: 0");
        currentPlayerLabel = new JLabel("Current Player: 1");
        scorePanel.add(scoreLabel1);
        scorePanel.add(scoreLabel2);
        scorePanel.add(currentPlayerLabel);
        add(scorePanel, BorderLayout.NORTH);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        restartButton.setVisible(false);

        JButton modeButton = new JButton("Switch to Hard Mode");
        modeButton.addActionListener(e -> switchMode());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restartButton);
        buttonPanel.add(modeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        initializeCards();

        timer = new Timer(750, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCards();
            }
        });
        timer.setRepeats(false);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeCards() {
        cards = new ArrayList<>();
        String backImagePath = "images\\Muster.jpg";
        ImageIcon backImage = new ImageIcon(backImagePath);

        String[] imageNames = {"ape", "camelion", "eagle", "krokodil", "nashorn", "nilpferd", "schlange", "tiger"};

        for (String imageName : imageNames) {
            String frontImagePath = "images\\" + imageName + ".png";
            ImageIcon frontImage = new ImageIcon(frontImagePath);
            if (frontImage.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.err.println("Failed to load front image: " + frontImagePath);
            }

            cards.add(new Card(frontImage, backImage, imageName));
            cards.add(new Card(frontImage, backImage, imageName));
        }
        shuffleCards();
    }

    private void shuffleCards() {
        Collections.shuffle(cards);
        cardPanel.removeAll();
        for (Card card : cards) {
            cardPanel.add(card);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void checkCards() {
        if (firstCard.getId().equals(secondCard.getId())) {
            firstCard.setMatched(true);
            secondCard.setMatched(true);
            pairsFound++;
            updateScore();
            firstCard = null;
            secondCard = null;
        } else {
            Timer flipBackTimer = new Timer(750, e -> {
                firstCard.flipCard();
                secondCard.flipCard();
                firstCard = null;
                secondCard = null;
                switchPlayer();
            });
            flipBackTimer.setRepeats(false);
            flipBackTimer.start();
        }

        if (pairsFound == 8) {
            endGame();
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        currentPlayerLabel.setText("Current Player: " + currentPlayer);
        if (hardMode) {
            shuffleUnmatchedCards();
        }
    }

    private void shuffleUnmatchedCards() {
        ArrayList<Card> unmatchedCards = new ArrayList<>();
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof Card) {
                Card card = (Card) comp;
                if (!card.isMatched() && !card.isFaceUp()) {
                    unmatchedCards.add(card);
                }
            }
        }

        Collections.shuffle(unmatchedCards);

        cardPanel.removeAll();
        for (Component comp : cards) {
            Card card = (Card) comp;
            if (card.isMatched() || card.isFaceUp()) {
                cardPanel.add(card);
            }
        }
        for (Card card : unmatchedCards) {
            cardPanel.add(card);
        }

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void updateScore() {
        if (currentPlayer == 1) {
            score1++;
            scoreLabel1.setText("Player 1: " + score1);
        } else {
            score2++;
            scoreLabel2.setText("Player 2: " + score2);
        }
    }

    private void endGame() {
        String winner = (score1 > score2) ? "Player 1 wins!" : (score2 > score1) ? "Player 2 wins!" : "It's a tie";
        JOptionPane.showMessageDialog(this, "Game Over! " + winner);
        restartButton.setVisible(true);
    }

    private void restartGame() {
        score1 = 0;
        score2 = 0;
        pairsFound = 0;
        currentPlayer = 1;
        scoreLabel1.setText("Player 1: 0");
        scoreLabel2.setText("Player 2: 0");
        currentPlayerLabel.setText("Current Player: 1");
        restartButton.setVisible(false);
        initializeCards();
    }

    private void switchMode() {
        hardMode = !hardMode;
        String mode = hardMode ? "Hard" : "Normal";
        JOptionPane.showMessageDialog(this, "Switched to " + mode + " Mode");
        restartGame();
    }

    private class Card extends JButton {
        private String id;
        private ImageIcon frontImage;
        private ImageIcon backImage;
        private boolean faceUp = false;
        private boolean matched = false;

        public Card(ImageIcon frontImage, ImageIcon backImage, String id) {
            this.frontImage = frontImage;
            this.backImage = backImage;
            this.id = id;

            if (backImage.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.err.println("Failed to load back image for card: " + id);
                setText("?");
            } else {
                setIcon(backImage);
            }

            if (frontImage.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.err.println("Failed to load front image for card: " + id);
                setText(id);
            }

            setPreferredSize(new Dimension(100, 100));

            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!faceUp && !matched && !timer.isRunning()) {
                        flipCard();
                        if (firstCard == null) {
                            firstCard = Card.this;
                        } else if (secondCard == null && firstCard != Card.this) {
                            secondCard = Card.this;
                            timer.start();
                        }
                    }
                }
            });
        }

        public String getId() {
            return id;
        }

        public void setMatched(boolean matched) {
            this.matched = matched;
            setEnabled(!matched);
        }

        public boolean isMatched() {
            return matched;
        }

        public boolean isFaceUp() {
            return faceUp;
        }

        public void flipCard() {
            faceUp = !faceUp;
            setIcon(faceUp ? frontImage : backImage);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MemoryGame();
            }
        });
    }
}