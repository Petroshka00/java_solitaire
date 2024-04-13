package TPALGO3.UI;

import TPALGO3.Deck.Card;
import TPALGO3.Patience.Freecell;
import TPALGO3.Patience.Klondike;
import TPALGO3.Patience.Solitaire;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.util.List;


public class BasePileView extends StackPane implements ObservableSubject, Observer {
    private static final int yOffset = 25;
    private static final int width = 73;
    private static final int height = 97;
    private static final double opacityClick = 0.75;
    private static final double opacityNormal = 1;
    private static final String sourceMovementClass = "BasePile";
    private static final String sourceMovementClassWaste = "Waste";
    private static final String sourceMovementClassFoundation = "Foundation";
    private static final String sourceMovementClassBasePile = "BasePile";
    private static final String sourceMovementClassCell = "Cell";
    private static final String style = "-fx-background-color: transparent;";
    private List<Observer> observers;
    private Movement movement;
    private int basePileIndex;
    private int basePileSize;
    public boolean isSelected;

    public BasePileView(Solitaire solitaire, int basePileIndex, List<Observer> observers, Movement movement){
        this.basePileIndex = basePileIndex;
        this.basePileSize = solitaire.getBasePileSize(basePileIndex);
        this.isSelected = false;
        this.observers = observers;
        setMaxSize(width, height);
        this.movement = movement;
        buildBasePileLayout(solitaire);
    }

    public void buildBasePileLayout(Solitaire solitaire){
        int offset = 0;
        this.basePileSize = solitaire.getBasePileSize(basePileIndex);
        int size = this.basePileSize;

        Button[] buttons = new Button[size];

        getChildren().clear();

        if(solitaire.isBasePileEmpty(this.basePileIndex)) {
            Button empty = new Button();
            ImageView image = new ImageView(CardView.getBack());
            image.setVisible(false);
            empty.setGraphic(image);
            empty.setStyle(style);
            getChildren().add(empty);
        }

        for(int i = 0; i < size; i++) {
            Card card = solitaire.getBasePileCard(basePileIndex, i);
            if(solitaire instanceof Klondike){
                if (i == size - 1) {
                    card.setFaceUp(true);
                }
            } else {
                card.setFaceUp(true);
            }

            boolean isCardFacedUp = card.getFaceUp();
            String cardCode = card.toString();

            buttons[i] = new Button();
            buttons[i].setId(cardCode);

            ImageView image = new ImageView(CardView.getImage(cardCode, isCardFacedUp));

            offset++;
            buttons[i].setTranslateY(yOffset * offset);
            buttons[i].setGraphic(image);
            buttons[i].setStyle(style);
            setAlignment(buttons[i], Pos.BOTTOM_CENTER);
            getChildren().add(buttons[i]);

        }

        manageBasePile(solitaire);
    }

    private void manageBasePile(Solitaire solitaire) {
        ObservableList<Node> cards = this.getChildren();

        int cardsSize = cards.size();

        for (int i = 0; i < cardsSize; i++) {

            int finalI = i;
            cards.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!movement.isOnGoing()){
                        movement.setOnGoing(true);
                        movement.setSourceIndex(basePileIndex);
                        movement.setSourceMovementClass(sourceMovementClass);
                        ((Button) mouseEvent.getSource()).setOpacity(opacityClick);

                        if(solitaire.isBasePileEmpty(basePileIndex)){
                            movement.setAmountOfCardsToMove(cardsSize - finalI - 1);
                        } else {
                            movement.setAmountOfCardsToMove(cardsSize - finalI);
                        }
                    } else {
                        setOpacity(opacityNormal);
                        String sourceMovementClass = movement.getSourceMovementClass();
                        int destBasePileIndex = basePileIndex;
                        switch (sourceMovementClass) {
                            case sourceMovementClassBasePile:
                                int remitBasePileIndex = movement.getSourceIndex();
                                int amountOfCardsToMove = movement.getAmountOfCardsToMove();

                               solitaire.basePileToBasePile(remitBasePileIndex,
                                        destBasePileIndex,
                                        amountOfCardsToMove);

                                notifyObservers(solitaire);
                                movement.resetMovement();
                                break;
                            case sourceMovementClassWaste:
                                ((Klondike)solitaire).wasteToBasePile(destBasePileIndex);

                                notifyObservers(solitaire);
                                movement.resetMovement();
                                break;
                            case sourceMovementClassFoundation:
                                int remitFoundationIndex = movement.getSourceIndex();

                                solitaire.foundationToBasePile(remitFoundationIndex, destBasePileIndex);

                                notifyObservers(solitaire);
                                movement.resetMovement();
                                break;

                            case sourceMovementClassCell:
                                int remitCellIndex = movement.getSourceIndex();

                                ((Freecell)solitaire).cellToBasePile(remitCellIndex, destBasePileIndex);

                                notifyObservers(solitaire);
                                movement.resetMovement();

                            default:
                                movement.resetMovement();
                                notifyObservers(solitaire);
                        }
                    }
                }
            });
        }
    }


    public void update(Solitaire solitaire) {
        this.basePileSize = solitaire.getBasePileSize(basePileIndex);
        buildBasePileLayout(solitaire);
    }

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
