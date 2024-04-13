package TPALGO3.UI;

import TPALGO3.Deck.Card;
import TPALGO3.Patience.Freecell;
import TPALGO3.Patience.Klondike;
import TPALGO3.Patience.Solitaire;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

public class FoundationView extends Button implements ObservableSubject, Observer {
    private static final int width = 73;
    private static final int height = 97;
    private static final double opacityClick = 0.75;
    private static final double opacityNormal = 1;
    private static final int translate = 10;
    private static final int defaultAmount = -1;
    private static final String sourceMovementClass = "Foundation";
    private static final String sourceMovementClassWaste = "Waste";
    private static final String sourceMovementClassBasePile = "BasePile";
    private static final String sourceMovementClassCell = "Cell";
    private static final String styleEmptyFoundation = "-fx-background-color: transparent;"
                                                + "-fx-border-color: lightgray;"
                                                + "-fx-border-with: 3;";
    private static final String styleFoundation = "-fx-background-color: transparent;";
    private static final String fileNameKlondike = "estado_juego_klondike.dat";
    private static final String fileNameFreecell = "estado_juego_freecell.dat";
    private List<Observer> observers;
    private Movement movement;
    private int foundationIndex;
    private Stage stage;


    public FoundationView(Solitaire solitaire, int foundationIndex, List<Observer> observers,
                          Movement movement, Stage stage){
        this.observers = observers;
        this.movement = movement;
        this.foundationIndex = foundationIndex;
        this.stage = stage;
        buildFoundationLayout(solitaire);
    }

    public void buildFoundationLayout(Solitaire solitaire){
        if(solitaire.isFoundationEmpty(foundationIndex)){
            ImageView image = new ImageView(CardView.getBack());
            image.setVisible(false);

            setTranslateX(translate);
            setTranslateY(translate);
            setMinSize(width, height);
            setMaxSize(width, height);

            setGraphic(image);
            setStyle(styleEmptyFoundation);
        } else {
            Card card = solitaire.getFoundationTopCard(foundationIndex);
            String cardCode = card.toString();

            setId(cardCode);
            setTranslateX(translate);
            setTranslateY(translate);
            setMinSize(width, height);
            setMaxSize(width, height);

            ImageView image = new ImageView(CardView.getImage(cardCode));
            image.setVisible(true);

            setGraphic(image);
            setStyle(styleFoundation);
        }
        setOpacity(opacityNormal);
        manageFoundation(solitaire);
    }

    public void manageFoundation(Solitaire solitaire) {

        setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    if(!movement.isOnGoing()){
                        movement.setOnGoing(true);
                        movement.setSourceIndex(foundationIndex);
                        movement.setSourceMovementClass(sourceMovementClass);
                        ((Button) mouseEvent.getSource()).setOpacity(opacityClick);
                        movement.setAmountOfCardsToMove(defaultAmount);
                    } else {
                        ((Button) mouseEvent.getSource()).setOpacity(opacityNormal);
                        int destFoundationIndex = foundationIndex;
                        String sourceMovementClass = movement.getSourceMovementClass();

                        switch(sourceMovementClass){
                            case sourceMovementClassWaste:
                                ((Klondike)solitaire).wasteToFoundation(destFoundationIndex);

                                notifyObservers(solitaire);
                                movement.resetMovement();

                                break;
                            case sourceMovementClassBasePile:
                                int remitBasePileIndex = movement.getSourceIndex();

                                solitaire.basePileToFoundation(remitBasePileIndex, destFoundationIndex);

                                notifyObservers(solitaire);
                                movement.resetMovement();

                                break;
                            case sourceMovementClassCell:
                                int remitCellIndex = movement.getSourceIndex();

                                ((Freecell)solitaire).cellToFoundation(remitCellIndex, destFoundationIndex);

                                notifyObservers(solitaire);
                                movement.resetMovement();

                            default:
                                movement.resetMovement();
                                notifyObservers(solitaire);
                        }
                    }

                    if(solitaire.gameFinished()){
                        try{
                            if(solitaire instanceof Klondike){
                                Files.delete(Path.of(fileNameKlondike));
                            } else {
                                Files.delete(Path.of(fileNameFreecell));
                            }
                        } catch (IOException ignored){
                        } finally {
                            Alert alert = new Alert(Alert.AlertType.WARNING);

                            alert.setTitle("¡¡¡YOU WON!!!");
                            alert.setHeaderText("We hope you had a pleasant game :)");

                            alert.getButtonTypes().stream()
                                    .filter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData())
                                    .findFirst()
                                    .ifPresent(buttonType -> ((ButtonBase) alert.getDialogPane().lookupButton(buttonType)).
                                            setText("Close"));

                            alert.getButtonTypes().stream()
                                    .filter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData())
                                    .findFirst()
                                    .ifPresent(buttonType -> ((ButtonBase) alert.getDialogPane().lookupButton(buttonType)).
                                            setOnAction(new EventHandler<ActionEvent>() {
                                                @Override
                                                public void handle(ActionEvent actionEvent) {
                                                    alert.close();
                                                    ((Stage) stage.getScene().getWindow()).close();
                                                }
                                            }));

                            alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                                @Override
                                public void handle(DialogEvent dialogEvent) {
                                    alert.close();
                                    ((Stage) stage.getScene().getWindow()).close();
                                }
                            });

                            alert.showAndWait();

                            stage.close();
                        }
                    }
                }
            });
    }



    public void update(Solitaire solitaire) {
        buildFoundationLayout(solitaire);
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
