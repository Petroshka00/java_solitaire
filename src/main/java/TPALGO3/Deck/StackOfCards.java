package TPALGO3.Deck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.util.Collections.shuffle;
import TPALGO3.Deck.Card.*;
import static TPALGO3.Deck.Card.Color.*;
import static TPALGO3.Deck.Card.Suit.*;

public class StackOfCards implements Serializable {

    public static final int totalCardsPerSuit = 13;
    public static final int indexCorrector = 1;
    private final List<Card> deck;

    /***
     * Constructor: inicializa deck, mezcla según semilla aleatoria.
     */
    public StackOfCards() {
        this.deck = new ArrayList<>();
        initDeck();
        shuffle(deck, new Random());
    }

    /***
     * Constructor: inicializa deck, mezcla según semilla recibida.
     * @param seed la semilla elegida.
     */
    public StackOfCards(long seed) {
        this.deck = new ArrayList<>();
        initDeck();
        shuffle(deck, new Random(seed));
    }

    /***
     * Inicializa deck de cartas francesas.
     */
    private void initDeck() {
        for (int i = 0; i < totalCardsPerSuit; i++) {
            deck.add(new Card(Value.values()[i], RED, HEART));
            deck.add(new Card(Value.values()[i], RED, DIAMOND));
            deck.add(new Card(Value.values()[i], BLACK, CLUB));
            deck.add(new Card(Value.values()[i], BLACK, SPADE));
        }
    }

    /**
     * @param cardIndex  posición en el deck de la carta deseada.
     * @return carta del deck en la posición recibida.
     */
    public Card getCard(int cardIndex) { return deck.get(cardIndex); }

    /***
     * @return última carta en el deck.
     */
    public Card removeTopCard(){ return deck.remove(deck.size() - indexCorrector); }

    /***
     * @return tamaño del deck.
     */
    public int size(){ return deck.size(); }

    /***
     * @param cardIndex índice de la carta.
     * @return la carta.
     */
    public Card removeCard(int cardIndex) {
        return deck.remove(cardIndex);
    }
}

