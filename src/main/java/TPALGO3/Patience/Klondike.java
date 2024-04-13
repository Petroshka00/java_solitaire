package TPALGO3.Patience;

import TPALGO3.Deck.Card;

import java.util.ArrayList;
import java.util.List;

import static TPALGO3.Deck.StackOfCards.indexCorrector;

public class Klondike extends Solitaire {

    private static final int amountOfFoundation = 4;
    protected static final int amountOfBasePiles = 7;
    private final List<Card> draw;
    private final List<Card> waste;
    private static final int minIndex = 0;

    /***
     * Constructor proporcionado por la clase abstracta Solitaire.
     */
    public Klondike() {
        super(amountOfFoundation, amountOfBasePiles);
        this.draw = new ArrayList<>();
        this.waste = new ArrayList<>();
    }

    @Override
    public void initPatience() {
        int correctorAmountOfCardsPerBasePile = 1;
        int basePilesSize = getBasePilesSize();
        for(int i = 0; i < basePilesSize; i++){
            for(int j = 0; j < i + correctorAmountOfCardsPerBasePile; j++){
                if(i == j){
                    deck.getCard(getDeckSize() - 1).setFaceUp(true);
                }
                addCardToBasePile(
                        removeCardFromDeck(getDeckSize() - indexCorrector), i);
            }
        }

        int startingDeckSize = getDeckSize();
        for(int i = 0; i < startingDeckSize; i++){
            deck.getCard(getDeckSize() - 1).setFaceUp(true);
            addCardToDraw(removeCardFromDeck(getDeckSize() - indexCorrector));
        }

    }

    /***
     * @param cardIndex posicion en el waste de la carta deseada.
     * @return carta del waste en la posición recibida.
     */
    public Card getCardFromWaste(int cardIndex){
        if (cardIndex < minIndex || cardIndex > waste.size() - indexCorrector) {
            return null;
        }
        return waste.get(cardIndex);
    }

    /***
     * @return true si el waste esta vacío, false en caso contrario.
     */
    public boolean isDrawEmpty() { return draw.isEmpty(); }

    /***
     * @return el tamaño del draw.
     */
    public int getDrawSize() {
        return draw.size();
    }

    /***
     * @return el tamaño del waste.
     */
    public int getWasteSize() {
        return waste.size();
    }

    /***
     * Quita la carta en cardIndex del waste.
     * @param cardIndex índice de la carta.
     * @return devuelve la carta.
     */
    public Card removeCardFromWaste(int cardIndex){
        return waste.remove(cardIndex);
    }

    /***
     * Quita la carta en cardIndex del draw.
     * @param cardIndex índice de la carta.
     * @return devuelve la carta.
     */
    public Card removeCardFromDraw(int cardIndex){ return draw.remove(cardIndex); }

    /***
     * @param card carta a agregar.
     */
    public void addCardToWaste(Card card) { waste.add(card); }

    /***
     * @param card carta a agregar.
     */
    public void addCardToDraw(Card card) { draw.add(card); }

    /***
     * @param topCardIndex índice de la carta a devolver.
     * @return la carta en el índice topCardIndex del draw.
     */
    public Card getDrawCard(int topCardIndex) { return draw.get(topCardIndex); }

    /***
     * Realiza el movimiento de la última carta del waste a una basePile si cumple los requisitos
     * de un movimiento válido.
     * @param destBasePileIndex índice de la carta en la basePile a trasladar.
     * @return true si se realizó el movimiento, o false en caso contrario.
     */
    public boolean wasteToBasePile(int destBasePileIndex) {
        if(waste.isEmpty())
            return false;

        if(!isBasePileEmpty(destBasePileIndex)){
            Card destBasePileLastCard = getBasePileCard(
                    destBasePileIndex, getBasePileSize(destBasePileIndex) - indexCorrector);

            Card wasteLastCard = getCardFromWaste( getWasteSize() - indexCorrector);

            if (!isMoveValid(wasteLastCard, destBasePileLastCard))
                return false;

            addCardToBasePile(removeCardFromWaste(getWasteSize() - indexCorrector), destBasePileIndex);

            return true;
        } else if (getCardFromWaste(getWasteSize() - 1).getValue() == Card.Value.K){
            addCardToBasePile(removeCardFromWaste(getWasteSize() - indexCorrector), destBasePileIndex);
            return true;
        } else {
            return false;
        }
    }

    /***
     * Mueve la carta superior del waste a una destFoundationIndex.
     * @param destFoundationIndex Indice de la foundation de destino.
     * @return true si la operacion fue exitosa, false en caso contrario.
     */
    public boolean wasteToFoundation(int destFoundationIndex){
        if (waste.isEmpty())
            return false;

        Card card = getCardFromWaste(getWasteSize() - indexCorrector);
        boolean ok = foundations[destFoundationIndex].push(card);
        if (ok) {
            removeCardFromWaste(getWasteSize() - indexCorrector);
            return true;
        } else {
            return false;
        }
    }

    /***
     * Traslada cada carta del waste al draw, cuando el draw se encuentra vacío.
     */
    public void wasteToDraw() {
        if(!isDrawEmpty())
            return;

        int initialSize = getWasteSize();

        for (int i = 0; i < initialSize; i++) {
            Card card = removeCardFromWaste(getWasteSize() - indexCorrector);
            addCardToDraw(card);
        }
    }

    /***
     * Traslada la carta superior del draw al waste.
     */
    public void drawToWaste() {
        if(isDrawEmpty())
            return;

        Card card = removeCardFromDraw(getDrawSize() - indexCorrector);
        addCardToWaste(card);
    }
}
