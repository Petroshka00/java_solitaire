package TPALGO3.UI;

import TPALGO3.Deck.Card;
import TPALGO3.Patience.Solitaire;

import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public abstract class SolitaireView {
    protected BasePilesViewsController basePilesViewsController;
    protected FoundationViewController foundationViewsController;
    protected List<Observer> observers;
    protected Movement movement;
    protected final int rowIndexForRoot = 0;

    public SolitaireView(Stage stage, GridPane root, Solitaire solitaire){
        observers = new ArrayList<>();
        movement = new Movement();

        basePilesViewsController = new BasePilesViewsController(solitaire, solitaire.getBasePilesSize(),
                observers, movement);
        basePilesViewsController.addBPViewsToRoot(root);

        foundationViewsController = new FoundationViewController(solitaire, Card.Suit.values().length,
                observers, movement, stage);
        foundationViewsController.addFoundationsToRoot(root);
    }
}


