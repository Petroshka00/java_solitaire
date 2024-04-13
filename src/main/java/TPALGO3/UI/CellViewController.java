package TPALGO3.UI;

import TPALGO3.Patience.Freecell;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CellViewController extends HBox {
    private Freecell freecell;
    private List<CellView> cellViews;
    private List<Observer> observers;
    private int amountOfCells;
    private int rowIndexForRoot = 0;

    public CellViewController(Freecell freecell, int amountOfCells,
                              List<Observer> observers, Movement movement, Stage stage){
        this.freecell = freecell;
        this.amountOfCells = amountOfCells;
        this.observers = observers;

        this.cellViews = new ArrayList<>();
        for (int i = 0; i < amountOfCells; i++){
            cellViews.add(new CellView(freecell, i, observers, movement, stage));
            this.observers.add(cellViews.get(i));
        }
    }

    public void addCellsToRoot(GridPane root){
        for (int i = 0; i < amountOfCells; i++){
            root.add(cellViews.get(i), i, rowIndexForRoot);
        }
    }
}
