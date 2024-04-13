package TPALGO3.UI;

import TPALGO3.Patience.Klondike;
import TPALGO3.Patience.Solitaire;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FoundationViewController extends HBox {
    private Solitaire solitaire;
    private List<FoundationView> foundationViews;
    private List<Observer> observers;
    private int amountOfFoundations;
    private static final int foundationX = 0;

    public FoundationViewController(Solitaire solitaire, int amountOfFoundations,
                                    List<Observer> observers, Movement movement, Stage stage) {
        this.solitaire = solitaire;
        this.amountOfFoundations = amountOfFoundations;
        this.observers = observers;

        this.foundationViews = new ArrayList<>();
        for (int i = 0; i < amountOfFoundations; i++) {
            foundationViews.add(new FoundationView(solitaire, i, observers, movement, stage));
            this.observers.add(foundationViews.get(i));
        }
    }

    public void addFoundationsToRoot(GridPane root) {
        int xOffset;
        if(solitaire instanceof Klondike){
            xOffset = 3;
        } else {
            xOffset = 5;
        }

        for (int i = 0; i < amountOfFoundations; i++) {
            root.add(foundationViews.get(i), i + xOffset, foundationX);
        }
    }
}
