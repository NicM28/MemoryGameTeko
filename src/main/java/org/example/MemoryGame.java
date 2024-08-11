// MemoryGame.java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hauptklasse des Memory-Spiels.
 * Diese Klasse startet die JavaFX-Anwendung und initialisiert das MVC-Pattern.
 */
public class MemoryGame extends Application {

    /**
     * Start-Methode der JavaFX-Anwendung.
     * Hier wird das MVC-Pattern initialisiert und die Hauptszene gesetzt.
     *
     * @param primaryStage Die Hauptbühne der Anwendung
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialisierung des Models
        MemoryModel model = new MemoryModel();
        
        // Initialisierung des Controllers
        MemoryController controller = new MemoryController(model);
        
        // Initialisierung der View
        MemoryView view = new MemoryView(controller);
        
        // Setzen der Szene
        Scene scene = new Scene(view.getRoot(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Memory Game");
        primaryStage.show();
    }

    /**
     * Hauptmethode zum Starten der Anwendung.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        launch(args);
    }
}

// MemoryModel.java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model-Klasse des Memory-Spiels.
 * Diese Klasse verwaltet den Zustand des Spiels und die Logik.
 */
public class MemoryModel {
    private List<String> cards;
    private boolean[] flippedCards;
    private int pairsFound;

    /**
     * Konstruktor der MemoryModel-Klasse.
     * Initialisiert die Karten und den Spielzustand.
     */
    public MemoryModel() {
        initializeCards();
        flippedCards = new boolean[16];
        pairsFound = 0;
    }

    /**
     * Initialisiert die Karten des Spiels.
     * Erstellt 8 Paare und mischt sie.
     */
    private void initializeCards() {
        cards = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            cards.add(String.valueOf(i));
            cards.add(String.valueOf(i));
        }
        Collections.shuffle(cards);
    }

    /**
     * Dreht eine Karte um und prüft, ob ein Paar gefunden wurde.
     *
     * @param index Der Index der umzudrehenden Karte
     * @return true, wenn ein Paar gefunden wurde, sonst false
     */
    public boolean flipCard(int index) {
        flippedCards[index] = true;
        
        // Prüfen, ob zwei Karten umgedreht sind
        if (getFlippedCount() == 2) {
            int firstIndex = getFirstFlippedIndex();
            if (cards.get(firstIndex).equals(cards.get(index))) {
                pairsFound++;
                return true;
            }
        }
        return false;
    }

    /**
     * Zählt die Anzahl der aktuell umgedrehten Karten.
     *
     * @return Anzahl der umgedrehten Karten
     */
    public int getFlippedCount() {
        int count = 0;
        for (boolean flipped : flippedCards) {
            if (flipped) count++;
        }
        return count;
    }

    /**
     * Findet den Index der ersten umgedrehten Karte.
     *
     * @return Index der ersten umgedrehten Karte
     */
    public int getFirstFlippedIndex() {
        for (int i = 0; i < flippedCards.length; i++) {
            if (flippedCards[i]) return i;
        }
        return -1;
    }

    /**
     * Dreht alle Karten wieder um.
     */
    public void resetFlippedCards() {
        for (int i = 0; i < flippedCards.length; i++) {
            flippedCards[i] = false;
        }
    }

    /**
     * Prüft, ob das Spiel beendet ist.
     *
     * @return true, wenn alle Paare gefunden wurden, sonst false
     */
    public boolean isGameOver() {
        return pairsFound == 8;
    }

    /**
     * Gibt den Wert einer Karte zurück.
     *
     * @param index Der Index der Karte
     * @return Der Wert der Karte als String
     */
    public String getCardValue(int index) {
        return cards.get(index);
    }

    /**
     * Prüft, ob eine Karte umgedreht ist.
     *
     * @param index Der Index der Karte
     * @return true, wenn die Karte umgedreht ist, sonst false
     */
    public boolean isCardFlipped(int index) {
        return flippedCards[index];
    }
}

// MemoryController.java

/**
 * Controller-Klasse des Memory-Spiels.
 * Diese Klasse vermittelt zwischen Model und View.
 */
public class MemoryController {
    private MemoryModel model;

    /**
     * Konstruktor der MemoryController-Klasse.
     *
     * @param model Das MemoryModel-Objekt
     */
    public MemoryController(MemoryModel model) {
        this.model = model;
    }

    /**
     * Verarbeitet einen Klick auf eine Karte.
     *
     * @param index Der Index der angeklickten Karte
     * @return true, wenn ein Paar gefunden wurde, sonst false
     */
    public boolean handleCardClick(int index) {
        if (model.isCardFlipped(index)) return false;
        
        boolean pairFound = model.flipCard(index);
        
        if (model.getFlippedCount() == 2) {
            if (!pairFound) {
                // Karten wieder umdrehen, wenn kein Paar gefunden wurde
                model.resetFlippedCards();
            }
        }
        
        return pairFound;
    }

    /**
     * Prüft, ob das Spiel beendet ist.
     *
     * @return true, wenn das Spiel beendet ist, sonst false
     */
    public boolean isGameOver() {
        return model.isGameOver();
    }

    /**
     * Gibt den Wert einer Karte zurück.
     *
     * @param index Der Index der Karte
     * @return Der Wert der Karte als String
     */
    public String getCardValue(int index) {
        return model.getCardValue(index);
    }

    /**
     * Prüft, ob eine Karte umgedreht ist.
     *
     * @param index Der Index der Karte
     * @return true, wenn die Karte umgedreht ist, sonst false
     */
    public boolean isCardFlipped(int index) {
        return model.isCardFlipped(index);
    }
}

// MemoryView.java
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * View-Klasse des Memory-Spiels.
 * Diese Klasse ist für die grafische Darstellung des Spiels verantwortlich.
 */
public class MemoryView {
    private MemoryController controller;
    private GridPane grid;
    private Button[] cardButtons;

    /**
     * Konstruktor der MemoryView-Klasse.
     *
     * @param controller Der MemoryController
     */
    public MemoryView(MemoryController controller) {
        this.controller = controller;
        initializeView();
    }

    /**
     * Initialisiert die grafische Oberfläche des Spiels.
     */
    private void initializeView() {
        grid = new GridPane();
        cardButtons = new Button[16];

        for (int i = 0; i < 16; i++) {
            final int index = i;
            Button button = new Button("?");
            button.setPrefSize(80, 80);
            button.setOnAction(e -> handleButtonClick(index));
            cardButtons[i] = button;
            grid.add(button, i % 4, i / 4);
        }
    }

    /**
     * Verarbeitet einen Klick auf einen Kartenbutton.
     *
     * @param index Der Index des geklickten Buttons
     */
    private void handleButtonClick(int index) {
        boolean pairFound = controller.handleCardClick(index);
        updateView();
        
        if (controller.isGameOver()) {
            showGameOverMessage();
        }
    }

    /**
     * Aktualisiert die Ansicht des Spielfelds.
     */
    private void updateView() {
        for (int i = 0; i < 16; i++) {
            if (controller.isCardFlipped(i)) {
                cardButtons[i].setText(controller.getCardValue(i));
            } else {
                cardButtons[i].setText("?");
            }
        }
    }

    /**
     * Zeigt eine Nachricht an, wenn das Spiel beendet ist.
     */
    private void showGameOverMessage() {
        // Hier könnte eine Meldung angezeigt werden, z.B. ein Alert
        System.out.println("Spiel beendet! Alle Paare gefunden!");
    }

    /**
     * Gibt das Wurzelelement der View zurück.
     *
     * @return Das Wurzelelement (GridPane) der View
     */
    public StackPane getRoot() {
        return new StackPane(grid);
    }
}