package TPALGO3.Deck;

import org.junit.Test;
import static TPALGO3.Deck.Card.Value.*;
import static TPALGO3.Deck.Card.Color.*;
import TPALGO3.Deck.Card.*;
import static TPALGO3.Deck.Card.Suit.*;
import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void getValue() {
        // Arrange
        Card card = new Card(ACE, BLACK, CLUB);

        // Act
        Value testValue = card.getValue();

        // Assert
        assertEquals(ACE, testValue);
    }

    @Test
    public void getColor() {
        // Arrange
        Card card = new Card(ACE, BLACK, CLUB);

        // Act
        Color testColor = card.getColor();

        // Assert
        assertEquals(BLACK, testColor);
    }

    @Test
    public void getSuit() {
        // Arrange
        Card card = new Card(ACE, BLACK, CLUB);

        // Act
        Suit testSuit = card.getSuit();

        // Assert
        assertEquals(CLUB, testSuit);
    }
}