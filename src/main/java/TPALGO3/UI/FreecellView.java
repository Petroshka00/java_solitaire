package TPALGO3.UI;

import TPALGO3.Patience.Freecell;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FreecellView extends SolitaireView{
    private CellViewController cellViewController;
    private static final int amountOfCells = 4;

    public FreecellView(Stage stage, GridPane root, Freecell freecell) {
        super(stage, root, freecell);

        cellViewController = new CellViewController(freecell, amountOfCells, observers, movement, stage);
        cellViewController.addCellsToRoot(root);

        stage.setTitle("Freecell");
    }
}
