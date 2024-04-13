package TPALGO3.UI;

import TPALGO3.Deck.Card;
import TPALGO3.Patience.Klondike;
import TPALGO3.Patience.Solitaire;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.control.Button;

import java.util.List;

public class WasteView extends Button implements ObservableSubject, Observer {
    private static final int width = 73;
    private static final int height = 97;
    private static final String style = "-fx-background-color: transparent;";
    private static final double opacityClick = 0.75;
    private static final double opacityNormal = 1;
    private static final int translate = 10;
    private static final String sourceMovementClass = "Waste";
    private List<Observer> observers;

    public WasteView(Klondike klondike, List<Observer> observers, Movement movement) {

        this.observers = observers;
        buildWasteLayout(klondike);

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!movement.isOnGoing()) {
                    movement.setOnGoing(true);
                    ((Button) mouseEvent.getSource()).setOpacity(opacityClick);
                    movement.setSourceMovementClass(sourceMovementClass);
                    movement.setAmountOfCardsToMove(-1);
                    movement.setSourceIndex(-1);
                } else {
                    ((Button) mouseEvent.getSource()).setOpacity(opacityNormal);
                    movement.resetMovement();
                    notifyObservers(klondike);
                }
            }
        });
    }

    private void buildWasteLayout(Solitaire solitaire){
        if(((Klondike)solitaire).getWasteSize() != 0){
            Card card = ((Klondike)solitaire).getCardFromWaste(((Klondike)solitaire).getWasteSize() - 1);
            String code = card.toString();
            ImageView image = new ImageView(CardView.getImage(code));
            image.setVisible(true);
            setOpacity(opacityNormal);
            setTranslateX(translate);
            setTranslateY(translate);
            setMinSize(width, height);
            setMaxSize(width, height);
            setStyle(style);
            setGraphic(image);
        } else {
            ImageView image = new ImageView(CardView.getBack());
            image.setVisible(false);
            setTranslateX(translate);
            setTranslateY(translate);
            setMinSize(width, height);
            setMaxSize(width, height);
            setStyle(style);
            setGraphic(image);
        }
    }

    public void update(Solitaire solitaire){ buildWasteLayout(solitaire); }

    public void registerObserver(Observer observer){
        observers.add(observer);
    }

    public void removeObserver(Observer observer){
        observers.remove(observer);
    }

    public void notifyObservers(Solitaire solitaire) {
        for (Observer observer : observers)
            observer.update(solitaire);
    }
}

