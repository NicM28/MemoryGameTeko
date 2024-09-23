package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * MemoryGame is a Swing-based implementation of the classic memory matching game.
 * Players take turns flipping cards to find matching pairs. The game supports
 * both normal and hard modes, where hard mode shuffles unmatched cards after each turn.
 */

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

    /**
     * Constructs a new MemoryGame, setting up the UI and initializing the game.
     */

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

    /**
     * Initializes the cards for the game, creating pairs of matching cards
     * and adding them to the card panel.
     */

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

    /**
     * Shuffles the cards and resets the card panel.
     */

    private void shuffleCards() {
        Collections.shuffle(cards);
        cardPanel.removeAll();
        for (Card card : cards) {
            cardPanel.add(card);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    /**
     * Checks if the two flipped cards match. If they do, updates the score and checks for game end.
     * If they don't match, flips them back and switches to the other player.
     */

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

    /**
     * Switches the current player and, if in hard mode, shuffles the unmatched cards.
     */

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        currentPlayerLabel.setText("Current Player: " + currentPlayer);
        if (hardMode) {
            shuffleUnmatchedCards();
        }
    }

    /**
     * Shuffles all unmatched and face-down cards on the board.
     * This method is called in hard mode after each player's turn.
     */

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

    /**
     * Updates the score for the current player and refreshes the score display.
     */

    private void updateScore() {
        if (currentPlayer == 1) {
            score1++;
            scoreLabel1.setText("Player 1: " + score1);
        } else {
            score2++;
            scoreLabel2.setText("Player 2: " + score2);
        }
    }

    /**
     * Ends the game, displays the winner, and shows the restart button.
     */

    private void endGame() {
        String winner = (score1 > score2) ? "Player 1 wins!" : (score2 > score1) ? "Player 2 wins!" : "It's a tie";
        JOptionPane.showMessageDialog(this, "Game Over! " + winner);
        restartButton.setVisible(true);
    }

    /**
     * Restarts the game, resetting scores, shuffling cards, and hiding the restart button.
     */

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

    /**
     * Toggles between normal and hard mode, restarting the game afterwards.
     */

    private void switchMode() {
        hardMode = !hardMode;
        String mode = hardMode ? "Hard" : "Normal";
        JOptionPane.showMessageDialog(this, "Switched to " + mode + " Mode");
        restartGame();
    }

    /**
     * Represents a card in the memory game.
     * Each card has a front and back image, and can be flipped and matched.
     */

    private class Card extends JButton {
        private String id;
        private ImageIcon frontImage;
        private ImageIcon backImage;
        private boolean faceUp = false;
        private boolean matched = false;

        /**
         * Constructs a new Card with the given front and back images and an identifier.
         *
         * @param frontImage The image shown when the card is face up
         * @param backImage The image shown when the card is face down
         * @param id The unique identifier for this card
         */

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

        /**
         * Returns the unique identifier of this card.
         *
         * @return The card's identifier
         */

        public String getId() {
            return id;
        }

        /**
         * Sets the matched state of the card and updates its enabled state.
         *
         * @param matched true if the card has been matched, false otherwise
         */

        public void setMatched(boolean matched) {
            this.matched = matched;
            setEnabled(!matched);
        }

        /**
         * Checks if the card has been matched.
         *
         * @return true if the card is matched, false otherwise
         */

        public boolean isMatched() {
            return matched;
        }

        /**
         * Checks if the card is currently face up.
         *
         * @return true if the card is face up, false otherwise
         */

        public boolean isFaceUp() {
            return faceUp;
        }

        /**
         * Flips the card, changing its face-up state and displayed image.
         */

        public void flipCard() {
            faceUp = !faceUp;
            setIcon(faceUp ? frontImage : backImage);
        }
    }

    /**
     * The main method to start the Memory Game application.
     *
     * @param args Command line arguments (not used)
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MemoryGame();
            }
        });
    }
}