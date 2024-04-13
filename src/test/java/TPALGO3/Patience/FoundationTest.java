package TPALGO3.Patience;

import TPALGO3.Deck.Card;
import TPALGO3.Deck.Card.*;
import static TPALGO3.Deck.Card.Value.*;
import static TPALGO3.Deck.Card.Color.*;
import static TPALGO3.Deck.Card.Suit.*;
import static TPALGO3.Deck.StackOfCards.totalCardsPerSuit;
import org.junit.Test;

import static org.junit.Assert.*;

public class FoundationTest {

    @Test
    public void pushValidEmptyStack() {
        // Arrange
        var foundation = new Foundation();
        var cardToPush = new Card(ACE, RED, HEART);

        // Act
        boolean pushStatus = foundation.push(cardToPush);

        // Assert
        assertTrue(pushStatus);
    }

    @Test
    public void pushValid() {
        // Arrange
        var foundation = new Foundation();
        var spadeAce = new Card(ACE, BLACK, SPADE);
        foundation.push(spadeAce);
        var cardToPush = new Card(TWO, BLACK, SPADE);

        // Act
        boolean pushStatus = foundation.push(cardToPush);

        // Assert
        assertTrue(pushStatus);
    }

    @Test
    public void pushInvalidValueEmptyStack() {
        // Arrange
        var foundation = new Foundation();
        var card = new Card(TWO, RED, HEART);

        // Act
        boolean pushStatus = foundation.push(card);

        // Assert
        assertFalse(pushStatus);
    }

    @Test
    public void pushInvalidSuit(){
        // Arrange
        var foundation = new Foundation();
        var spadeAce = new Card(ACE, BLACK, SPADE);
        foundation.push(spadeAce);
        var clubTwo = new Card(TWO, BLACK, CLUB);

        // Act
        boolean pushStatus = foundation.push(clubTwo);

        // Assert
        assertFalse(pushStatus);
    }

    @Test
    public void pushInvalidColor(){
        // Arrange
        var foundation = new Foundation();
        var spadeAce = new Card(ACE, BLACK, SPADE);
        foundation.push(spadeAce);
        var heartTwo = new Card(TWO, RED, HEART);

        // Act
        boolean pushStatus = foundation.push(heartTwo);

        // Assert
        assertFalse(pushStatus);
    }

    @Test
    public void pushInvalidSuitAndColor(){
        // Arrange
        var foundation = new Foundation();
        var spadeAce = new Card(ACE, BLACK, SPADE);
        foundation.push(spadeAce);
        var diamondTwo = new Card(TWO, RED, DIAMOND);

        // Act
        boolean pushStatus = foundation.push(diamondTwo);

        // Assert
        assertFalse(pushStatus);
    }

    @Test
    public void isCompleteInvalid(){
        // Arrange
        var foundation = new Foundation();
        var spadeAce = new Card(ACE, BLACK, SPADE);
        foundation.push(spadeAce);

        // Act
        boolean isComplete = foundation.isComplete();

        // Assert
        assertFalse(isComplete);
    }

    @Test
    public void isCompleteInvalidEmpty(){
        // Arrange
        var foundation = new Foundation();

        // Act
        boolean isComplete = foundation.isComplete();

        // Assert
        assertFalse(isComplete);
    }

    @Test
    public void isCompleteValid(){
        // Arrange
        var foundation = new Foundation();
        for (int i = 0; i < totalCardsPerSuit; i++) {
            Card card = new Card(Value.values()[i], RED, HEART);
            foundation.push(card);
        }

        boolean isComplete = foundation.isComplete();

        // Assert
        assertTrue(isComplete);
    }

    @Test
    public void pop() {
        // Arrange
        var foundation = new Foundation();
        var spadeAce = new Card(ACE, BLACK, SPADE);
        foundation.push(spadeAce);

        // Act
        Card card = foundation.pop();

        // Assert
        assertEquals(ACE, card.getValue());
        assertEquals(BLACK, card.getColor());
        assertEquals(SPADE, card.getSuit());
    }

    @Test
    public void combinationOfPushAndPopUntilComplete(){
        // Arrange
        var foundation = new Foundation();
        int amountOfCardsInFirstPush = 5, amountOfCardsInPop = 3;
        int firstIndexOfLastPush = amountOfCardsInFirstPush - amountOfCardsInPop;
        int amountOfCardsInLastPush = totalCardsPerSuit - amountOfCardsInFirstPush + amountOfCardsInPop;

        // Act
        for (int i = 0; i < amountOfCardsInFirstPush; i++)
            foundation.push(new Card(Value.values()[i], RED, HEART));

        for(int i = 0; i < amountOfCardsInPop; i++)
            foundation.pop();

        for(int i = firstIndexOfLastPush; i < firstIndexOfLastPush + amountOfCardsInLastPush; i++)
            foundation.push(new Card(Value.values()[i], RED, HEART));

        // Assert
        assertTrue(foundation.isComplete());
    }
}