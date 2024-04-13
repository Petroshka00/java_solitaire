package TPALGO3.Patience;

import TPALGO3.Deck.Card;
import static TPALGO3.Deck.StackOfCards.indexCorrector;

import java.io.Serializable;

public class Freecell extends Solitaire implements Serializable {

    private static final int amountOfFoundation = 4;
    protected static final int amountOfBasePiles = 8;
    private static final int amountOfCells = 4;
    private final Card[] cells;

    public Freecell() {
        super(amountOfFoundation, amountOfBasePiles);
        this.cells = new Card[amountOfCells];
    }

    @Override
    public void initPatience() {
        int[] numCardsInEachBP = {7, 6};

        for(int i = 0; i < amountOfBasePiles; i++){
            for(int j = 0; j < numCardsInEachBP[i/4]; j++){
                addCardToBasePile(removeCardFromDeck(getDeckSize() - indexCorrector), i);
            }
        }
    }

    /***
     * Traslada una carta de una remitBasePileIndex a una destCellIndex,
     * cuando esa celda está vacía.
     * @param remitBasePileIndex índice de la basePile remitente.
     * @param destCellIndex índice de cellSlot destino.
     * @return true si la operación fue exitosa, o false en caso contrario.
     */
    public boolean basePileToCell(int remitBasePileIndex, int destCellIndex){
        if(cells[destCellIndex] != null)
            return false;

        cells[destCellIndex] = removeBasePileCard(
                remitBasePileIndex, getBasePileSize(remitBasePileIndex) - indexCorrector);

        return true;
    }

    /***
     *Traslada una carta de una remitCellIndex a una destBasePileIndex,
     * cuando cumple los requisitos de que el movimiento sea válido.
     * @param remitCellIndex índice de cellSlot remitente.
     * @param destBasePileIndex índice de la basePile destino.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean cellToBasePile(int remitCellIndex, int destBasePileIndex){
        if((cells[remitCellIndex] == null || !isMoveValid(cells[remitCellIndex], getBasePileCard(
                destBasePileIndex, getBasePileSize(destBasePileIndex) - indexCorrector))) &&
                !(isBasePileEmpty(destBasePileIndex) && cells[remitCellIndex].getValue() == Card.Value.K))
            return false;

        addCardToBasePile(cells[remitCellIndex],
                destBasePileIndex);
        cells[remitCellIndex] = null;
        return true;
    }


    /***
     * Traslada una carta de una remitCellIndex a una destCellIndex,
     * cuando cumple los requisitos para el movimiento.
     * @param remitCellIndex indice de cell remitente.
     * @param destCellIndex indice de cell destino.
     * @return true si la operacion fue exitosa, false en caso contrario.
     */
    public boolean cellToCell(int remitCellIndex, int destCellIndex){
        if(cells[remitCellIndex] != null && cells[destCellIndex] == null){
            cells[destCellIndex] = cells[remitCellIndex];
            cells[remitCellIndex] = null;
            return true;
        }
        return false;
    }

    /***
     * Traslada una carta de una remitCellIndex a una destFoundationIndex,
     * cuando cumple los requisitos para el movimiento.
     * @param remitCellIndex indice del cell remitente.
     * @param destFoundationIndex indice de la foundation destino.
     * @return true si la operacion fue exitosa, false en caso contrario.
     */
    public boolean cellToFoundation(int remitCellIndex, int destFoundationIndex){
        if(cells[remitCellIndex] == null)
            return false;

        boolean ok = pushToSpecificFoundation(cells[remitCellIndex], destFoundationIndex);
        if(ok){
            cells[remitCellIndex] = null;
            return true;
        }
        return false;
    }

    /***
     * @param card carta a insertar.
     * @param cellIndex indice de cells a insertar la carta.
     */
    public void insertToCell (Card card, int cellIndex) { cells[cellIndex] = card; }

    /***
     * @param cellIndex índice de la cell de la que se quiere conseguir la carta.
     * @return la carta en la cell.
     */
    public Card getCellCard (int cellIndex) { return cells[cellIndex]; }

    /***
     * @param cellIndex indice de la cell que se quiere revisar.
     * @return true si la cell esta vacía, false en caso contrario.
     */
    public boolean isCellEmpty (int cellIndex) { return cells[cellIndex] == null; }
}


