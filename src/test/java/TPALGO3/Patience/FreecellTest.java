package TPALGO3.Patience;

import TPALGO3.Deck.Card;
import org.junit.Test;

import static TPALGO3.Deck.Card.Color.*;
import static TPALGO3.Deck.Card.Suit.*;
import static TPALGO3.Deck.Card.Value.*;
import static org.junit.Assert.*;
import static TPALGO3.Deck.StackOfCards.indexCorrector;


public class FreecellTest {
    int pileOneIndex = 0, destCellSlot = 3;

    @Test
    public void testInitPatience(){
        // Arrange
        var freecell =  new Freecell();
        freecell.initPatience();

        int[] numCardsInEachBP = {7, 6};
        int amountOfBasePiles = 8;

        // Act
        boolean correctNumOfCardsInEachBP = true;

        for(int i = 0; i < amountOfBasePiles; i++) {
            if(freecell.getBasePileSize(i) != numCardsInEachBP[i/4]){
                correctNumOfCardsInEachBP = false;
                break;
            }
        }

        // Assert
        assertTrue(correctNumOfCardsInEachBP);
    }

    @Test
    public void basePileToCellInvalid(){
        // Arrange
        var freecell = new Freecell();
        Card card1 = new Card(K, RED, HEART);
        Card card2 = new Card(Q, BLACK, SPADE);
        int pileTwoIndex = 1;
        freecell.addCardToBasePile(card1, pileOneIndex);
        freecell.addCardToBasePile(card2, pileTwoIndex);

        // Act
        freecell.basePileToCell(pileOneIndex, destCellSlot);
        boolean couldNotMove = freecell.basePileToCell(pileOneIndex, destCellSlot);

        // Assert
        assertFalse(couldNotMove);
    }

    @Test
    public void basePileToCellValid(){
        // Arrange
        var freecell = new Freecell();
        Card card1 = new Card(K, RED, HEART);
        freecell.addCardToBasePile(card1, pileOneIndex);

        // Act
        boolean movedCorrectly = freecell.basePileToCell(pileOneIndex, destCellSlot);

        // Assert
        assertTrue(movedCorrectly);
    }

    @Test
    public void cellToBasePileInvalid(){
        // Arrange
        var freecell = new Freecell();
        Card card1 = new Card(K, RED, HEART);
        Card card2 = new Card(FOUR, RED, HEART);
        int BPIndex = 0, CellIndex = 0;

        // Act
        freecell.addCardToBasePile(card1, BPIndex);
        freecell.insertToCell(card2, CellIndex);
        boolean couldNotMove = freecell.cellToBasePile(CellIndex, BPIndex);

        // Assert
        assertFalse(couldNotMove);
        assertEquals(card1, freecell.getBasePileCard(BPIndex, freecell.getBasePileSize(BPIndex) - indexCorrector));
    }

    @Test
    public void cellToBasePileValid(){
        // Arrange
        var freecell = new Freecell();
        Card card1 = new Card(K, RED, HEART);
        Card card2 = new Card(Q, BLACK, SPADE);
        int BPIndex = 0, CellIndex = 0;

        // Act
        freecell.addCardToBasePile(card1, BPIndex);
        freecell.insertToCell(card2, CellIndex);
        boolean couldMove = freecell.cellToBasePile(CellIndex, BPIndex);

        // Assert
        assertTrue(couldMove);
        assertEquals(card2, freecell.getBasePileCard(BPIndex, freecell.getBasePileSize(BPIndex) - indexCorrector));
    }
}
