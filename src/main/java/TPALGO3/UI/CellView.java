package TPALGO3.UI;

import TPALGO3.Deck.Card;
import TPALGO3.Patience.Freecell;
import TPALGO3.Patience.Solitaire;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;

public class CellView extends Button implements ObservableSubject, Observer{
    private static final int width = 73;
    private static final int height = 97;
    private static final int translate = 10;
    private static final double opacityClick = 0.75;
    private static final double opacityNormal = 1;
    private static final int defaultAmount = -1;
    private static final String styleEmptyCell = "-fx-background-color: transparent;"
                                        + "-fx-border-color: black;";
    private static final String styleNormal = "-fx-background-color: transparent;";
    private static final String sourceMovementClass = "Cell";
    private static final String sourceMovementClassBasePile = "BasePile";
    private static final String sourceMovementClassCell = "Cell";
    private List<Observer> observers;
    private Movement movement;
    private int cellIndex;
    private Stage stage;

    public CellView(Freecell freecell, int cellIndex, List<Observer> observers, Movement movement, Stage stage) {
        this.observers = observers;
        this.movement = movement;
        this.stage = stage;
        this.cellIndex = cellIndex;
        buildCellLayout(freecell);
    }

    public void buildCellLayout(Freecell freecell) {
        if(freecell.isCellEmpty(cellIndex)) {
            setMinSize(width, height);
            setMaxSize(width, height);
            setTranslateX(translate);
            setTranslateY(translate);

            ImageView image = new ImageView(CardView.getBack());
            image.setVisible(false);

            setGraphic(image);
            setStyle(styleEmptyCell);
        } else {
            setTranslateX(translate);
            setTranslateY(translate);
            setMinSize(width, height);
            setMaxSize(width, height);

            Card card = freecell.getCellCard(cellIndex);
            String cardCode = card.toString();
            setId(cardCode);

            ImageView image = new ImageView(CardView.getImage(cardCode));

            setGraphic(image);
            setStyle(styleNormal);
        }

        setOpacity(opacityNormal);
        manageCells(freecell);
    }

    public void manageCells(Freecell freecell) {

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!movement.isOnGoing()) {
                    movement.setOnGoing(true);
                    movement.setSourceIndex(cellIndex);
                    movement.setSourceMovementClass(sourceMovementClass);
                    ((Button) mouseEvent.getSource()).setOpacity(opacityClick);
                    movement.setAmountOfCardsToMove(defaultAmount);
                } else {
                    ((Button) mouseEvent.getSource()).setOpacity(opacityNormal);
                    int destCellIndex = cellIndex;
                    String sourceMovementClass = movement.getSourceMovementClass();

                    switch (sourceMovementClass) {
                        case sourceMovementClassBasePile:
                            int remitBasePileIndex = movement.getSourceIndex();

                            freecell.basePileToCell(remitBasePileIndex, destCellIndex);

                            notifyObservers(freecell);
                            movement.resetMovement();
                            break;

                        case sourceMovementClassCell:
                            int remitCellIndex = movement.getSourceIndex();

                            freecell.cellToCell(remitCellIndex, destCellIndex);

                            notifyObservers(freecell);
                            movement.resetMovement();
                            break;

                        default:
                            movement.resetMovement();
                            notifyObservers(freecell);
                    }
                }
            }
        });
    }

    @Override
    public void update(Solitaire solitaire) {
        buildCellLayout((Freecell) solitaire);
    }
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Solitaire solitaire) {
        for (Observer observer : observers)
            observer.update(solitaire);
    }
}
