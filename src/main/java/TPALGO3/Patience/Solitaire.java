package TPALGO3.Patience;

import TPALGO3.Deck.Card;
import TPALGO3.Deck.StackOfCards;
import static TPALGO3.Deck.Card.Value.*;
import static TPALGO3.Deck.StackOfCards.indexCorrector;
import java.io.*;

import java.util.ArrayList;

public abstract class Solitaire implements Serializable{

    protected final StackOfCards deck;
    protected final Foundation[] foundations;
    private final ArrayList<Card>[] basePiles;

    /***
     * Constructor proporcionado por la clase abstracta Solitaire.
     * Se reservan las estructuras de los atributos.
     * @param amountOfFoundation cantidad de Foundations dadas en una subclase de Solitaire.
     * @param amountOfBasePiles cantidad de basePiles dadas en una subclase de Solitaire.
     */
    public Solitaire(int amountOfFoundation, int amountOfBasePiles) {
        this.deck = new StackOfCards();
        this.foundations = new Foundation[amountOfFoundation];
        for (int i = 0; i < amountOfFoundation; i++)
            foundations[i] = new Foundation();

        this.basePiles = new ArrayList[amountOfBasePiles];
        for (int i = 0; i < amountOfBasePiles; i++)
            basePiles[i] = new ArrayList<>();
    }

    /***
     * Inicializa un juego completo de Solitario.
     */
    public abstract void initPatience();

    /***
     * @param card carta a pushear.
     * @param foundationIndex indice del foundation a pushear la carta.
     */
    public boolean pushToSpecificFoundation(Card card, int foundationIndex){ return foundations[foundationIndex].push(card); }

    /***
     * @param basePileIndex índice de la basePile a conseguir el tamaño.
     * @return tamaño de la basePile en el índice basePileIndex en basePiles.
     */
    public int getBasePileSize(int basePileIndex) { return basePiles[basePileIndex].size(); }

    /***
     * @return el tamaño de basePiles.
     */
    public int getBasePilesSize() { return basePiles.length; }

    /***
     * @param basePileIndex índice de la basePile deseada.
     * @return true si está vacía, false en caso contrario.
     */
    public boolean isBasePileEmpty(int basePileIndex) {
        return basePiles[basePileIndex].isEmpty();
    }

    /***
     * @param card carta a agregar.
     * @param basePileIndex índice de la basePile a agregar la carta.
     */
    public void addCardToBasePile(Card card, int basePileIndex) {
        basePiles[basePileIndex].add(card);
    }

    /***
     * @param basePileIndex índice de la basePile en basePiles.
     * @param cardIndex índice de la carta en basePile.
     * @return devuelve la carta.
     */
    public Card getBasePileCard(int basePileIndex, int cardIndex) {
        if (isBasePileEmpty(basePileIndex))
            return null;
        return basePiles[basePileIndex].get(cardIndex);
    }

    /***
     * @param basePileIndex índice de la basePile en basePiles.
     * @param cardIndex índice de la carta en basePile.
     * @return devuelve la carta.
     */
    public Card removeBasePileCard(int basePileIndex, int cardIndex) { return basePiles[basePileIndex].remove(cardIndex); }

    /***
     * @param foundationIndex índice de la foundation a chequear.
     * @return true si la foundation indicada está vacía, y false en caso contrario.
     */
    public boolean isFoundationEmpty(int foundationIndex){
        return foundations[foundationIndex].isEmpty();
    }
    /***
     * Determina si se cumplieron las condiciones necesarias para que el juego se considere como
     * finalizado.
     * @return true si se terminó el juego, o false en caso contrario.
     */
    public boolean gameFinished() {
        for (Foundation foundation : foundations)
            if (!foundation.isComplete())
                return false;

        return true;
    }

    /***
     * Determina si cardToMove puede moverse encima de previousCard, teniendo en cuenta que
     * los colores sean distintos y que los valores estén en orden descendente.
     * @param cardToMove carta a mover.
     * @param previousCard carta por sobre la cual se colocaría cardToMove.
     * @return true si el movimiento es válido, o false en caso contrario.
     */
    protected boolean isMoveValid(Card cardToMove, Card previousCard) {
        if (cardToMove == null || previousCard == null)
            return false;

        return previousCard.getColor() != cardToMove.getColor() &&
                previousCard.getValue().ordinal() == cardToMove.getValue().ordinal() + indexCorrector;
    }

    /***
     * Determina si el movimiento de la carta de una basePile a otra es válido.
     * Es decir que, si el basePile destino está vacío la carta sea un rey,
     * y si no está vacío, verifica que el color sea distinto y el orden sea descendente.
     * @param remitBasePileIndex índice en basePiles de la basePile remitente.
     * @param destBasePileIndex índice en basePiles de la basePile destino.
     * @param indexOfCardToMove indice en remitBasePile de la carta a mover.
     * @return true si el movimiento es válido, o false en caso contrario.
     */
    private boolean isMoveValidBPToBP(int remitBasePileIndex, int destBasePileIndex, int indexOfCardToMove) {
        boolean isDestBPEmpty = isBasePileEmpty(destBasePileIndex);

        var firstCardToMove = getBasePileCard(remitBasePileIndex, indexOfCardToMove);
        if (firstCardToMove == null)
            return false;


        if (isDestBPEmpty)
            return firstCardToMove.getValue() == K;

        var destBPLastCard = getBasePileCard(destBasePileIndex, getBasePileSize(destBasePileIndex) - indexCorrector);

        return isMoveValid(firstCardToMove, destBPLastCard);
    }

    /***
     * Traslada una cantidad dada de cartas superiores de una remitBasePile a una destinationBasePile
     * cuando cumple los requisitos de que el movimiento sea válido.
     * @param remitBasePileIndex indice en basePiles de la basePile remitente.
     * @param destBasePileIndex indice de la basePile a la basePile destino.
     * @param amountOfCards cantidad de cartas seleccionadas a mover.
     * @return true si la operación fue exitosa, o false en caso contrario.
     */
    public boolean basePileToBasePile(int remitBasePileIndex, int destBasePileIndex, int amountOfCards) {
        int indexOfCardToMove = getBasePileSize(remitBasePileIndex) - amountOfCards;

        if (!isMoveValidBPToBP(remitBasePileIndex, destBasePileIndex, indexOfCardToMove))
            return false;

        for (int i = 0; i < amountOfCards; i++) {
            Card card = removeBasePileCard(remitBasePileIndex, indexOfCardToMove);

            addCardToBasePile(card, destBasePileIndex);
        }

        return true;
    }

    /***
     * Intenta mover la ultima carta de una basePile dada a otra.
     * @param basePileIndex indice en basePiles de la basePile remitente.
     * @param foundationIndex indice en foundations de la foundation destino.
     * @return true si el movimiento es válido, o false en caso contrario.
     */
    public boolean basePileToFoundation(int basePileIndex, int foundationIndex) {
        if(isBasePileEmpty(basePileIndex)){
            return false;
        }
        int cardToMoveIndex = getBasePileSize(basePileIndex) - indexCorrector;

        Card card = getBasePileCard(basePileIndex, cardToMoveIndex);
        boolean ok = foundations[foundationIndex].push(card);

        if(ok){
            removeBasePileCard(basePileIndex, cardToMoveIndex);
            return true;
        } else {
            return false;
        }
    }

    /***
     * Mueve la carta superior del foundation indicado a la basePile indicada.
     * @param foundationIndex índice del foundation desde el que se quiere mover la carta.
     * @param basePileIndex índice de la basePile a la que se quiere mover la carta.
     * @return true si el movimiento fue realizado, false en caso contrario.
     */
    public boolean foundationToBasePile(int foundationIndex, int basePileIndex){
        if(foundations[foundationIndex].isEmpty()){
            return false;
        }

        if(isMoveValid(foundations[foundationIndex].peek(), getBasePileCard(basePileIndex,
                getBasePileSize(basePileIndex) - 1))){
            addCardToBasePile(foundations[foundationIndex].pop(), basePileIndex);
            return true;
        }
        return false;
    }

    /***
     * @param cardIndex índice de la carta deseada.
     * @return devuelve la carta.
     */
    public Card removeCardFromDeck(int cardIndex) { return deck.removeCard(cardIndex); }

    /***
     * @return el tamaño del deck.
     */
    public int getDeckSize() {
        return deck.size();
    }

    /***
     * @param foundationIndex índice de la foundation de la que se quiere la carta superior.
     * @return la carta superior del foundation indicado.
     */
    public Card getFoundationTopCard(int foundationIndex) {
        return foundations[foundationIndex].peek();
    }

    public void serialize(OutputStream os) throws IOException {
        ObjectOutputStream objectOutStream = new ObjectOutputStream(os);
        objectOutStream.writeObject(this);
        objectOutStream.flush();
    }

    public static Solitaire deserialize(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInStream = new ObjectInputStream(is);
        return (Solitaire) objectInStream.readObject();
    }
}
