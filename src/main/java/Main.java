import TPALGO3.Patience.Freecell;
import TPALGO3.Patience.Klondike;
import TPALGO3.Patience.Solitaire;
import TPALGO3.UI.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.WindowEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;

public class Main extends Application {
    private static final String backgroundImageKlondike = "file:resources/fondomesa_klondike.jpg";
    private static final String backgroundImageFreecell = "file:resources/fondomesa_freecell.jpg";
    private static final String backgroundImageMenu = "file:resources/fondo-menu-solitaire.jpg";
    private static final int menuWidth = 371;
    private static final int menuHeight = 289;
    private static final int klondikeWidth = 650;
    private static final int klondikeHeight = 600;
    private static final int freecellWidth = 810;
    private static final int freecellHeight = 600;
    private static final String fileNameKlondike = "estado_juego_klondike.dat";
    private static final String fileNameFreecell = "estado_juego_freecell.dat";

    public void setImageBackground(Region root, String IMAGE){
        Image img = new Image(IMAGE);
        BackgroundImage myBI = new BackgroundImage(img,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        root.setBackground(new Background(myBI));
    }

    public void generateKlondikeGame(Stage stage, Klondike klondike) {
        GridPane root = new GridPane();
        setImageBackground(root,backgroundImageKlondike);

        if(klondike == null){
            klondike = new Klondike();
            klondike.initPatience();
        }

        Klondike finalKlondike = klondike;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(fileNameKlondike)) {
                    finalKlondike.serialize(fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ((Stage) stage.getScene().getWindow()).close();
            }
        });

        KlondikeView klondikeView = new KlondikeView(stage, root, klondike);

        Menu newGame = newGameMenu(stage);
        Menu optionsGame = gameOptionsMenu(stage, finalKlondike, null);

        BorderPane borderPane = addMenuBarOnTop(newGame, optionsGame,root);

        stage.setResizable(false);
        stage.setTitle("Klondike");
        stage.setScene(new Scene(borderPane, klondikeWidth, klondikeHeight));
        stage.show();
    }

    public void generateFreecellGame(Stage stage, Freecell freecell){
        GridPane root = new GridPane();
        setImageBackground(root,backgroundImageFreecell);

        if(freecell == null){
            freecell = new Freecell();
            freecell.initPatience();
        }

        Freecell finalFreecell = freecell;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(fileNameFreecell)) {
                    finalFreecell.serialize(fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ((Stage) stage.getScene().getWindow()).close();
            }
        });

        FreecellView freecellView = new FreecellView(stage, root, freecell);

        Menu newGame = newGameMenu(stage);
        Menu optionsGame = gameOptionsMenu(stage, null, finalFreecell);

        BorderPane borderPane = addMenuBarOnTop(newGame,optionsGame, root);

        stage.setResizable(false);
        stage.setTitle("Freecell");
        stage.setScene(new Scene(borderPane, freecellWidth, freecellHeight));
        stage.show();
    }

    public Menu newGameMenu(Stage stage){
        Menu menu = new Menu("New game");

        MenuItem klondikeItem = new MenuItem("Klondike");
        MenuItem freecellItem = new MenuItem("Freecell");
        menu.getItems().addAll(klondikeItem,freecellItem);

        klondikeItem.setOnAction(actionEvent -> generateKlondikeGame(stage, null));
        freecellItem.setOnAction(event -> generateFreecellGame(stage, null));

        return menu;
    }

    private void continueGameSolitaire(Stage stage, Solitaire solitaire, String solitaireType) {
        if (solitaireType.equals("Klondike")) {
            try (FileInputStream fileInputStream = new FileInputStream(fileNameKlondike)) {
                solitaire = (Klondike) Klondike.deserialize(fileInputStream);

            } catch (ClassNotFoundException | IOException e) {
                solitaire = null;
            }
        } else {
            try (FileInputStream fileInputStream = new FileInputStream(fileNameFreecell)) {
                solitaire = (Freecell) Freecell.deserialize(fileInputStream);

            } catch (ClassNotFoundException | IOException e) {
                solitaire = null;
            }
        }

        if (solitaire == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error loading Solitaire Game");
            alert.setHeaderText("There were no prior games of Solitaire " + solitaireType + " found.");

            alert.getButtonTypes().stream()
                    .filter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData())
                    .findFirst()
                    .ifPresent(buttonType -> (
                            (ButtonBase) alert.
                                    getDialogPane().
                                    lookupButton(buttonType)).
                            setText("Ok"));

            alert.showAndWait();

        } else {
            if (solitaireType.equals("Klondike")) {
                generateKlondikeGame(stage, (Klondike) solitaire);
            } else {
                generateFreecellGame(stage, (Freecell) solitaire);
            }
        }
    }

    public Menu continueGameMenu(Stage stage){
        Menu menu = new Menu("Continue game");

        MenuItem klondikeItem = new MenuItem("Klondike");
        MenuItem freecellItem = new MenuItem("Freecell");
        menu.getItems().addAll(klondikeItem, freecellItem);

        klondikeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Klondike klondike = null;
                continueGameSolitaire(stage, klondike, "Klondike");
            }
        });

        freecellItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Freecell freecell = null;
                continueGameSolitaire(stage, freecell, "Freecell");
            }
        });

        return menu;
    }

    public Menu gameOptionsMenu(Stage stage, Klondike klondike, Freecell freecell){
        Menu menu = new Menu("Options");

        MenuItem saveGameMenu = saveGameMenu(klondike, freecell);

        Menu continueGameMenu = continueGameMenu(stage);
        menu.getItems().addAll(saveGameMenu, continueGameMenu);

        return menu;
    }

    public MenuItem saveGameMenu(Klondike klondike, Freecell freecell){
        MenuItem saveItem = new MenuItem("Save");

        saveItem.setOnAction(actionEvent -> {
            if(freecell != null){
                try (FileOutputStream fileOutputStream = new FileOutputStream(fileNameFreecell)) {
                    freecell.serialize(fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (klondike != null){
                try (FileOutputStream fileOutputStream = new FileOutputStream(fileNameKlondike)) {
                    klondike.serialize(fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        return saveItem;
    }

    private BorderPane addMenuBarOnTop(Menu menu0,Menu menu1, Region root){
        MenuBar mb = new MenuBar();

        mb.getMenus().addAll(menu0, menu1);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(mb);
        borderPane.setCenter(root);

        return borderPane;
    }

    public void decideKlondike(Stage stage){
        Klondike klondike = null;

        try (FileInputStream fileInputStream = new FileInputStream(fileNameKlondike)) {
            klondike = (Klondike) Klondike.deserialize(fileInputStream);
        } catch (ClassNotFoundException | IOException e) {
            klondike = null;
        } finally {
            generateKlondikeGame(stage, klondike);
        }
    }

    public void decideFreecell(Stage stage){
        Freecell freecell = null;

        try (FileInputStream fileInputStream = new FileInputStream(fileNameFreecell)){
            freecell = (Freecell) Freecell.deserialize(fileInputStream);
        } catch (ClassNotFoundException | IOException e) {
            freecell = null;
        } finally {
            generateFreecellGame(stage, freecell);
        }
    }

    public void welcomeMenu(Stage stage){
        VBox root = new VBox();
        setImageBackground(root, backgroundImageMenu);

        MenuButton mb = new MenuButton ("Choose a solitaire version");
        MenuItem klondikeItem = new MenuItem("Klondike");
        MenuItem freecellItem = new MenuItem("Freecell");
        mb.getItems().addAll(klondikeItem, freecellItem);

        klondikeItem.setOnAction(event -> decideKlondike(stage));
        freecellItem.setOnAction(event -> decideFreecell(stage));

        root.setAlignment(Pos.CENTER);
        root.getChildren().add(mb);

        stage.setResizable(false);
        stage.setScene(new Scene(root, menuWidth, menuHeight));
        stage.setTitle("Solitaire");
        stage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        welcomeMenu(stage);
    }
}
