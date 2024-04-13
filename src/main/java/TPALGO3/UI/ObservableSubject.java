package TPALGO3.UI;

import TPALGO3.Patience.Solitaire;

public interface ObservableSubject {

    public void registerObserver(Observer observer);

    public void removeObserver(Observer observer);

    public void notifyObservers(Solitaire solitaire);
}

