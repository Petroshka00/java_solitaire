package TPALGO3.Deck;

import static TPALGO3.Deck.Card.Color.*;
import static TPALGO3.Deck.Card.Suit.*;
import static TPALGO3.Deck.Card.Value.Q;
import static TPALGO3.Deck.StackOfCards.totalCardsPerSuit;
import org.junit.Test;
import static org.junit.Assert.*;

public class StackOfCardsTest {

    public final static int totalCardsOnDeck = 52;
    private final static int totalCardsPerColor = 26;
    private final static int randomSeed = 1234;
    private final static int emptyDeck = 0;
    private final static int maxIndexCard = 51;

    @Test
    public void initDeck() {
        // Arrange

        // Act
        StackOfCards stackOfCards = new StackOfCards(randomSeed);

        int amountOfHearts = 0, amountOfDiamonds = 0, amountOfSpades = 0,
                amountOfClubs = 0, amountOfReds = 0, amountOfBlacks = 0;
        for (int i = 0; i < totalCardsOnDeck; i++) {
            Card card = stackOfCards.getCard(i);
            if (card.getColor().equals(RED))
                amountOfReds++;
            else
                amountOfBlacks++;

            if (card.getSuit().equals(HEART))
                amountOfHearts++;
            else if (card.getSuit().equals(SPADE))
                amountOfSpades++;
            else if (card.getSuit().equals(CLUB))
                amountOfClubs++;
            else
                amountOfDiamonds++;
        }

        // Assert
        assertEquals(totalCardsPerColor, amountOfReds);
        assertEquals(totalCardsPerColor, amountOfBlacks);
        assertEquals(totalCardsPerSuit, amountOfHearts);
        assertEquals(totalCardsPerSuit, amountOfSpades);
        assertEquals(totalCardsPerSuit, amountOfClubs);
        assertEquals(totalCardsPerSuit, amountOfDiamonds);
    }

    @Test
    public void removeOneTopCard(){
        // Arrange
        StackOfCards stackOfCards = new StackOfCards(randomSeed);
        Card card = stackOfCards.getCard(maxIndexCard);

        // Act
        Card topCard = stackOfCards.removeTopCard();

        // Assert
        assertEquals(topCard.getValue(), card.getValue());
        assertEquals(topCard.getColor(), card.getColor());
        assertEquals(topCard.getSuit(), card.getSuit());
    }

    @Test
    public void removeAllCardsByTopCard(){
        //Arrange
        StackOfCards stackOfCards = new StackOfCards(randomSeed);

        //Act
        for(int i = 0; i < totalCardsOnDeck; i++)
            stackOfCards.removeTopCard();

        //Assert
        assertEquals(emptyDeck,stackOfCards.size());
    }

    @Test
    public void removeUntilSpecificCard(){
        //Arrange
        StackOfCards stackOfCards = new StackOfCards(randomSeed);
        Card specificCard = new Card(Q,RED,HEART);
        Card topCard = stackOfCards.removeTopCard();
        int finalDeckSize = 40;

        //Act
        while(!(topCard.getValue() == specificCard.getValue() &&
                topCard.getColor() == specificCard.getColor() &&
                topCard.getSuit() == specificCard.getSuit())){
            topCard = stackOfCards.removeTopCard();
        }

        //Assert
        assertEquals(specificCard.getValue(), topCard.getValue());
        assertEquals(specificCard.getColor(), topCard.getColor());
        assertEquals(specificCard.getSuit(), topCard.getSuit());
        assertEquals(finalDeckSize, stackOfCards.size());
    }
}