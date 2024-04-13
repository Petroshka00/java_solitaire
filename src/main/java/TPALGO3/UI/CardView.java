package TPALGO3.UI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import TPALGO3.Patience.Solitaire;
import javafx.scene.image.Image;

public class CardView implements ObservableSubject, Observer {

    private static Map<String, Image> cards  = new HashMap<>();
    private static final String IMAGE_LOCATION = "resources/";
    private static final String IMAGE_SUFFIX = ".gif";
    private static final String IMAGE_BACK = "BACK";
    private List<Observer> observers;

    public CardView(List<Observer> observers){
        this.observers = observers;
    }

    public static Image getImage(String code) {
        Image image = cards.get(code);

        if(image != null)
            return image;
        try {
            Image gifImage = new Image(new FileInputStream(IMAGE_LOCATION + code + IMAGE_SUFFIX));
            cards.put(code, gifImage);

            return gifImage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image getImage(String code, boolean status){
        if(status){
            return getImage(code);
        } else {
            return getBack();
        }
    }

    public static Image getBack() {
        return getImage(IMAGE_BACK);
    }

    public void update(Solitaire solitaire) {
        return;
    }

    public void registerObserver(Observer observer){
        observers.add(observer);
    }

    public void removeObserver(Observer observer){
        observers.add(observer);
    }

    public void notifyObservers(Solitaire solitaire) {
        for (Observer observer : observers)
            observer.update(solitaire);
    }
}




