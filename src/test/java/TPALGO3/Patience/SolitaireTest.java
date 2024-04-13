package TPALGO3.Patience;

import TPALGO3.Deck.Card;
import TPALGO3.Deck.Card.*;
import static TPALGO3.Deck.Card.Value.*;
import static TPALGO3.Deck.Card.Color.*;
import static TPALGO3.Deck.Card.Suit.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static TPALGO3.Deck.StackOfCards.totalCardsPerSuit;
import static org.junit.Assert.*;

public class SolitaireTest {

    private static final int amountOfCardsInEmptyPile = 0, maxQuantityForPile = 12;
    private static final int indexCardJ = 10, indexCardK = 12;
    private static final int pileOneIndex = 0, pileTwoIndex = 1, pileThreeIndex = 2;
    private static final int firstFoundationIndex = 0, secondFoundationIndex = 1,thirdFoundationIndex = 2, fourthFoundationIndex = 3;

    @Test
    public void testGameFinishedTrue() {
        //Arrange
        var klondike = new Klondike();

        for (int i = 0; i < totalCardsPerSuit; i++) {
            klondike.pushToSpecificFoundation(new Card(Card.Value.values()[i], RED, HEART), firstFoundationIndex);
            klondike.pushToSpecificFoundation(new Card(Card.Value.values()[i], RED, DIAMOND), secondFoundationIndex);
            klondike.pushToSpecificFoundation(new Card(Card.Value.values()[i], BLACK, CLUB), thirdFoundationIndex);
            klondike.pushToSpecificFoundation(new Card(Card.Value.values()[i], BLACK, SPADE), fourthFoundationIndex);
        }

        //Act
        boolean gameFinished = klondike.gameFinished();

        //Assert
        assertTrue(gameFinished);
    }

    @Test
    public void testGameFinishedFalse() {
        //Arrange
        var klondike = new Klondike();
        int incompleteSuit = 2;
        for (int i = 0; i < totalCardsPerSuit - incompleteSuit; i++) {
            klondike.pushToSpecificFoundation(new Card(Card.Value.values()[i], RED, HEART), firstFoundationIndex);
            klondike.pushToSpecificFoundation(new Card(Card.Value.values()[i], BLACK, SPADE), secondFoundationIndex);
        }

        //Act
        boolean gameFinished = klondike.gameFinished();

        //Assert
        assertFalse(gameFinished);
    }

    private void addValidSequenceFromBlack(Klondike klondike, int pileIndex, int cardIndex){
        if (cardIndex % 2 == 0)
            klondike.addCardToBasePile(new Card(Value.values()[cardIndex], BLACK, SPADE), pileIndex);
        else
            klondike.addCardToBasePile(new Card(Value.values()[cardIndex], RED, HEART), pileIndex);
    }

    private void addValidSequenceFromRed(Klondike klondike, int pileIndex, int cardIndex){
        if (cardIndex % 2 == 0)
            klondike.addCardToBasePile(new Card(Value.values()[cardIndex], RED, HEART), pileIndex);
        else
            klondike.addCardToBasePile(new Card(Value.values()[cardIndex], BLACK, SPADE), pileIndex);
    }

    @Test
    public void moveCardsBPToEmptyBPValid() {
        // Arrange
        Klondike klondike = new Klondike();
        int quantityOfCardsInOne = 7;

        for(int i = maxQuantityForPile; i > maxQuantityForPile - quantityOfCardsInOne; i--)
            addValidSequenceFromBlack(klondike, pileOneIndex, i);

        // Act
        boolean movedStatus = klondike.basePileToBasePile(pileOneIndex, pileTwoIndex, quantityOfCardsInOne);

        // Assert
        assertTrue(movedStatus);
        assertEquals(amountOfCardsInEmptyPile, klondike.getBasePileSize(pileOneIndex));
        assertEquals(quantityOfCardsInOne, klondike.getBasePileSize(pileTwoIndex));
    }

    @Test
    public void moveCardsBPToEmptyBPInvalid() {
        // Arrange
        Klondike klondike = new Klondike();
        int quantityOfCardsInOne = 5;
        int invalidStartingIndex = 10;

        for(int i = invalidStartingIndex; i > quantityOfCardsInOne; i--)
            addValidSequenceFromBlack(klondike, pileOneIndex, i);

        // Act
        boolean movedStatus = klondike.basePileToBasePile(pileOneIndex, pileTwoIndex, quantityOfCardsInOne);

        // Assert
        assertFalse(movedStatus);
        assertEquals(quantityOfCardsInOne, klondike.getBasePileSize(pileOneIndex));
        assertEquals(amountOfCardsInEmptyPile, klondike.getBasePileSize(pileTwoIndex));
    }

    @Test
    public void moveAllCardsBPToBPValid() {
        // Arrange
        Klondike klondike = new Klondike();
        int quantityOfCardsInOne = 5;
        int amountOfCardsInSecondPileAfterMoving = 7;

        for(int i = indexCardJ; i > quantityOfCardsInOne; i--)
            addValidSequenceFromBlack(klondike, pileOneIndex,i);

        for(int i = indexCardK; i > indexCardJ; i--)
            addValidSequenceFromBlack(klondike, pileTwoIndex, i);

        // Act
        boolean movedStatus = klondike.basePileToBasePile(pileOneIndex, pileTwoIndex, quantityOfCardsInOne);

        // Assert
        assertTrue(movedStatus);
        assertEquals(amountOfCardsInEmptyPile, klondike.getBasePileSize(pileOneIndex));
        assertEquals(amountOfCardsInSecondPileAfterMoving, klondike.getBasePileSize(pileTwoIndex));
    }

    @Test
    public void moveAllCardsBPToBPInvalid() {
        // Arrange
        Klondike klondike = new Klondike();
        int quantityOfCardsInOne = 5, quantityOfCardsInTwo = 5;
        int indexCardTen = 9;

        for(int i = indexCardJ; i > quantityOfCardsInOne; i--)
            addValidSequenceFromRed(klondike, pileOneIndex, i);

        for(int i = indexCardTen; i > quantityOfCardsInTwo - 1; i--)
            addValidSequenceFromBlack(klondike, pileTwoIndex, i);

        for(int i = indexCardK; i > indexCardJ; i--)
            addValidSequenceFromBlack(klondike, pileThreeIndex, i);

        // Act
        boolean movedStatus1 = klondike.basePileToBasePile(pileOneIndex, pileThreeIndex, quantityOfCardsInOne);
        boolean movedStatus2 = klondike.basePileToBasePile(pileTwoIndex, pileThreeIndex, quantityOfCardsInTwo);

        // Assert
        assertFalse(movedStatus1);
        assertFalse(movedStatus2);
        assertEquals(klondike.getBasePileSize(pileOneIndex), quantityOfCardsInOne);
        assertEquals(klondike.getBasePileSize(pileTwoIndex), quantityOfCardsInTwo);
    }

    @Test
    public void moveSomeCardsBPToBPValid(){
        // Arrange
        Klondike klondike = new Klondike();
        int quantityOfCardsInOne = 4, quantityOfCardsInTwo = 2, quantityToMove = 2;

        for(int i = maxQuantityForPile; i > maxQuantityForPile - quantityOfCardsInOne; i--)
            addValidSequenceFromBlack(klondike, pileOneIndex, i);

        for(int i = maxQuantityForPile; i > maxQuantityForPile - quantityOfCardsInTwo; i--)
            addValidSequenceFromBlack(klondike, pileTwoIndex, i);

        // Act
        boolean movedStatus = klondike.basePileToBasePile(pileOneIndex, pileTwoIndex, quantityToMove);

        //Assert
        assertTrue(movedStatus);
        assertEquals(klondike.getBasePileSize(pileOneIndex), 2);
        assertEquals(klondike.getBasePileSize(pileTwoIndex), 4);
    }

    @Test
    public void moveSomeCardsBPToBPInvalid(){
        // Arrange
        Klondike klondike = new Klondike();
        int quantityOfCardsInOne = 4, quantityOfCardsInTwo = 2, quantityToMove = 2;

        for(int i = maxQuantityForPile; i > maxQuantityForPile - quantityOfCardsInOne; i--)
            addValidSequenceFromBlack(klondike, pileOneIndex, i);

        for(int i = maxQuantityForPile; i > maxQuantityForPile - quantityOfCardsInTwo; i--)
            addValidSequenceFromRed(klondike, pileTwoIndex, i);

        // Act
        boolean movedStatus = klondike.basePileToBasePile(pileOneIndex, pileTwoIndex, quantityToMove);

        // Assert
        assertFalse(movedStatus);
        assertEquals(quantityOfCardsInOne, klondike.getBasePileSize(pileOneIndex));
        assertEquals(quantityOfCardsInTwo, klondike.getBasePileSize(pileTwoIndex));
    }

    @Test
    public void basePileToFoundationValid() {
        // Arrange
        Klondike klondike = new Klondike();
        Card card1 = new Card(ACE, RED, HEART);
        Card card2 = new Card(TWO, RED, HEART);

        int foundationOneIndex = 0;
        klondike.addCardToBasePile(card2, pileOneIndex);
        klondike.addCardToBasePile(card1, pileOneIndex);

        // Act
        boolean movedStatus1 = klondike.basePileToFoundation(pileOneIndex, foundationOneIndex);
        boolean movedStatus2 = klondike.basePileToFoundation(pileOneIndex, foundationOneIndex);

        // Assert
        assertTrue(movedStatus1);
        assertTrue(movedStatus2);
    }

    @Test
    public void foundationToBasePileValid(){
        // Arrange
        Klondike klondike = new Klondike();
        Card card1 = new Card(ACE, RED, HEART);
        Card card2 = new Card(TWO, BLACK, SPADE);
        int foundationIndex = 0, basePileIndex = 0;
        klondike.pushToSpecificFoundation(card1, foundationIndex);
        klondike.addCardToBasePile(card2, basePileIndex);

        // Act
        boolean movedCorrectly = klondike.foundationToBasePile(foundationIndex, basePileIndex);

        // Assert
        assertTrue(movedCorrectly);
    }

    @Test
    public void foundationToBasePileInvalid(){
        // Arrange
        Klondike klondike = new Klondike();
        Card card1 = new Card(ACE, RED, HEART);
        Card card2 = new Card(FOUR, BLACK, SPADE);
        int foundationIndex = 0, basePileIndex = 0;
        klondike.pushToSpecificFoundation(card1, foundationIndex);
        klondike.addCardToBasePile(card2, basePileIndex);

        // Act
        boolean movedCorrectly = klondike.foundationToBasePile(foundationIndex, basePileIndex);

        // Assert
        assertFalse(movedCorrectly);
    }

    @Test
    public void persistenceGenerateFile() throws IOException, ClassNotFoundException {
        // Arrange
        Freecell freecellToSerialize = new Freecell();

        // Act
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        freecellToSerialize.serialize(bytes);

        Freecell deserializedFreecell = (Freecell) Freecell.deserialize(new ByteArrayInputStream(bytes.toByteArray()));

        // Assert
        assertNotNull(deserializedFreecell);
    }

    @Test
    public void persistenceSolitaire() throws IOException, ClassNotFoundException {
        // Arrange
        Klondike klondikeToSerialize = new Klondike();
        Card card1 = new Card(ACE, RED, HEART);
        int card1Index = 0;
        klondikeToSerialize.addCardToBasePile(card1, 0);

        // Act
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        klondikeToSerialize.serialize(bytes);

        Klondike deserializedKlondike = (Klondike) Klondike.deserialize(new ByteArrayInputStream(bytes.toByteArray()));

        //Assert
        Card card3 = klondikeToSerialize.getBasePileCard(0, card1Index);
        Card card4 = deserializedKlondike.getBasePileCard(0, card1Index);
        assertEquals(card3.getSuit(), card4.getSuit());
        assertEquals(card3.getValue(), card4.getValue());
        assertEquals(card3.getColor(), card4.getColor());
    }
}