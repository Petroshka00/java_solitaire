package TPALGO3.UI;

import TPALGO3.Patience.Klondike;
import TPALGO3.Patience.Solitaire;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class DrawView extends Button implements ObservableSubject, Observer {
    private static final int width = 73;
    private static final int height = 97;
    private static final int fontSize = 12;
    private static final int translate = 10;
    private static final String style = "-fx-background-color: transparent;";
    private List<Observer> observers;

    public DrawView(Klondike klondike, List<Observer> observers, Movement movement){

        this.observers = observers;
        buildDrawLayout(klondike);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!movement.isOnGoing()) {
                    if (!klondike.isDrawEmpty())
                        klondike.drawToWaste();
                     else
                        klondike.wasteToDraw();

                    notifyObservers(klondike);
                } else {
                    movement.resetMovement();
                }
            }
        });

    }

    private void buildDrawLayout(Solitaire solitaire){
        if(!((Klondike)solitaire).isDrawEmpty()){
            ImageView image = new ImageView(CardView.getBack());
            image.setVisible(true);

            setTranslateX(translate);
            setTranslateY(translate);
            setMinSize(width, height);
            setMaxSize(width, height);
            setGraphic(image);
            setStyle(style);
        } else {
            setGraphic(createResetCanvas());
        }
    }

    private Canvas createResetCanvas(){
        Canvas canvas = new Canvas(width,height);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setTextAlign(TextAlignment.CENTER);
        context.setFill(Color.DARKKHAKI);
        context.setFont(Font.font(Font.getDefault().getName(), fontSize));
        context.fillText("Click to reset", canvas.getWidth()/2, canvas.getHeight()/2);
        return canvas;
    }

    public void update(Solitaire solitaire) { buildDrawLayout(solitaire); }

    public void registerObserver(Observer observer){ observers.add(observer); }

    public void removeObserver(Observer observer){ observers.remove(observer); }

    public void notifyObservers(Solitaire solitaire) {
        for (Observer observer : observers)
            observer.update(solitaire);
    }
}


