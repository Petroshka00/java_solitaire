package TPALGO3.Patience;

import TPALGO3.Deck.Card;
import org.junit.Test;
import static TPALGO3.Deck.Card.Color.*;
import static TPALGO3.Deck.Card.Suit.*;
import static TPALGO3.Deck.Card.Value.*;
import static TPALGO3.Deck.Card.Value.Q;
import static org.junit.Assert.*;
import static TPALGO3.Patience.Klondike.amountOfBasePiles;
import static TPALGO3.Deck.StackOfCardsTest.totalCardsOnDeck;
import static TPALGO3.Deck.StackOfCards.indexCorrector;

public class KlondikeTest {
    private static final int pileOneIndex = 0;

    @Test
    public void testInitPatience() {
        //Arrange
        var klondike = new Klondike();

        //Act
        klondike.initPatience();
        var drawSize = klondike.getDrawSize();
        int basePilesTotalSize = 0;

        //Assert
        for(int i = 0; i < amountOfBasePiles; i++){
            assertEquals(i + indexCorrector, klondike.getBasePileSize(i));
            basePilesTotalSize += klondike.getBasePileSize(i);
        }

        assertEquals(totalCardsOnDeck - basePilesTotalSize, drawSize);
    }

    @Test
    public void wasteToBasePileValid() {
        // Arrange
        var klondike = new Klondike();
        Card card1 = new Card(K, RED, HEART);
        Card card2 = new Card(Q, BLACK, CLUB);

        klondike.addCardToBasePile(card1, pileOneIndex);
        klondike.addCardToWaste(card2);

        int finalBasePileSize = 2;

        // Act
        boolean movedStatus = klondike.wasteToBasePile(pileOneIndex);
        int basePileSize = klondike.getBasePileSize(pileOneIndex);
        Card card = klondike.getBasePileCard(pileOneIndex, basePileSize - indexCorrector);

        // Assert
        assertTrue(movedStatus);
        assertEquals(finalBasePileSize, basePileSize);
        assertEquals(card2.getValue(), card.getValue());
        assertEquals(card2.getColor(), card.getColor());
        assertEquals(card2.getSuit(), card.getSuit());
    }

    @Test
    public void wasteToBasePileInvalid() {
        // Arrange
        var klondike = new Klondike();
        Card card1 = new Card(K, RED, HEART);
        Card card2 = new Card(ACE, BLACK, CLUB);
        Card card3 = new Card(Q, RED, DIAMOND);

        klondike.addCardToBasePile(card1, pileOneIndex);
        klondike.addCardToWaste(card2);
        klondike.addCardToWaste(card3);

        var basePileSize = klondike.getBasePileSize(pileOneIndex);
        int finalBasePileSize = 1;

        // Act
        boolean movedStatus1 = klondike.wasteToBasePile(pileOneIndex);
        boolean movedStatus2 = klondike.wasteToBasePile(pileOneIndex);

        // Assert
        assertFalse(movedStatus1);
        assertFalse(movedStatus2);
        assertEquals(finalBasePileSize, basePileSize);
    }

    @Test
    public void moveWasteToDraw() {
        // Arrange
        Klondike klondike = new Klondike();
        int quantityOfCards = 5;
        for(int i = 0; i < quantityOfCards; i++)
            klondike.addCardToWaste(new Card(Card.Value.values()[i], BLACK, SPADE));

        // Act
        klondike.wasteToDraw();

        // Assert
        assertEquals(quantityOfCards, klondike.getDrawSize());
    }

    @Test
    public void drawToWaste() {
        // Arrange
        Klondike klondike = new Klondike();
        int quantityOfCardsInDraw = 5;
        int quantityRemoved = 2;
        for(int i = 0; i < quantityOfCardsInDraw; i++)
            klondike.addCardToDraw(new Card(Card.Value.values()[i], BLACK, SPADE));

        // Act
        for(int i = 0; i < quantityRemoved; i++)
            klondike.drawToWaste();


        // Assert
        assertEquals(quantityOfCardsInDraw - quantityRemoved, klondike.getDrawSize());
        assertEquals(quantityRemoved, klondike.getWasteSize());
    }

    @Test
    public void drawPileMaintainsOrder(){
        // Arrange
        Klondike klondike = new Klondike();
        int quantityOfCardsInDraw = 5;
        int topCardIndex = quantityOfCardsInDraw - indexCorrector, secondTopCardIndex = quantityOfCardsInDraw - 2;
        for(int i = 0; i < quantityOfCardsInDraw; i++)
            klondike.addCardToDraw(new Card(Card.Value.values()[i], BLACK, SPADE));

        Card topCard = klondike.getDrawCard(topCardIndex);
        Card secondCard = klondike.getDrawCard(secondTopCardIndex);

        // Act
        for(int i = 0; i < quantityOfCardsInDraw; i++)
            klondike.drawToWaste();

        klondike.wasteToDraw();

        // Assert
        assertEquals(topCard, klondike.getDrawCard(topCardIndex));
        assertEquals(secondCard, klondike.getDrawCard(secondTopCardIndex));
    }

    @Test
    public void wasteToFoundationValid(){
        // Arrange
        Klondike klondike = new Klondike();
        int foundationIndex = 0;

        // Act
        klondike.pushToSpecificFoundation(new Card(ACE, BLACK, SPADE), foundationIndex);
        klondike.addCardToWaste(new Card(TWO, BLACK, SPADE));
        boolean pushedCorrectly = klondike.wasteToFoundation(foundationIndex);

        // Assert
        assertTrue(pushedCorrectly);
    }

    @Test
    public void wasteToFoundationInvalid(){
        // Arrange
        Klondike klondike = new Klondike();
        int foundationIndex = 0;

        // Act
        klondike.pushToSpecificFoundation(new Card(TEN, BLACK, SPADE), foundationIndex);
        klondike.addCardToWaste(new Card(TWO, BLACK, SPADE));
        boolean pushedCorrectly = klondike.wasteToFoundation(foundationIndex);

        // Assert
        assertFalse(pushedCorrectly);
    }

}