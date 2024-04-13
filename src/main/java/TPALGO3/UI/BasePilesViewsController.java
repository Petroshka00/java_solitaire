package TPALGO3.UI;

import TPALGO3.Patience.Solitaire;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class BasePilesViewsController extends HBox{

    private Solitaire solitaire;
    private List<BasePileView> basePilesViews;
    private int amountOfBasePilesViews;
    private List<Observer> observers;

    private final int rowIndexForRoot = 1;
    private static final int translate = 4;

    public BasePilesViewsController(Solitaire solitaire, int amountOfBasePilesViews,
                                    List<Observer> observers, Movement movement) {
        this.solitaire = solitaire;
        this.amountOfBasePilesViews = amountOfBasePilesViews;
        this.basePilesViews = new ArrayList<>();
        this.observers = observers;

        for (int i = 0; i < this.amountOfBasePilesViews; i++) {
            basePilesViews.add(new BasePileView(solitaire, i, observers, movement));
            this.observers.add(basePilesViews.get(i));
            basePilesViews.get(i).setTranslateX(translate*i);
        }

    }

    private void notifyObservers(Solitaire solitaire) {
        for (Observer observer : observers) {
            observer.update(solitaire);
        }
    }

    public void addBPViewsToRoot(GridPane root) {
        for (int i = 0; i < amountOfBasePilesViews; i++) {
            root.add(basePilesViews.get(i), i, rowIndexForRoot);
        }
    }

}
