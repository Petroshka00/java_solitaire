package TPALGO3.UI;

import TPALGO3.Patience.Klondike;

import javafx.scene.layout.*;
import javafx.stage.Stage;

public class KlondikeView extends SolitaireView {
    private DrawView drawView;
    private WasteView wasteView;
    private static final int drawX = 0;
    private static final int wasteX = 1;

    public KlondikeView(Stage stage, GridPane root, Klondike klondike) {
        super(stage, root, klondike);

        drawView = new DrawView(klondike, observers, movement);
        root.add(drawView, drawX, rowIndexForRoot);
        observers.add(drawView);

        wasteView = new WasteView(klondike, observers, movement);
        root.add(wasteView, wasteX, rowIndexForRoot);
        observers.add(wasteView);

        stage.setTitle("Klondike");
    }
}