package TPALGO3.Patience;

import TPALGO3.Deck.Card;
import TPALGO3.Deck.Card.*;
import static TPALGO3.Deck.Card.Value.*;
import static TPALGO3.Deck.StackOfCards.indexCorrector;

import java.io.Serializable;
import java.util.Stack;

public class Foundation implements Serializable {

    private final Stack<Card> pile;

    public Foundation() { this.pile = new Stack<>(); }

    /***
     * @return true si está vacía, false en caso contrario.
     */
    public boolean isEmpty(){ return pile.isEmpty(); }


    /***
     * Agrega la carta recibida al foundation.
     * Considerando como valores válidos (cada foundation empieza con un ACE, incrementando uno con cada push)
     * y palos válidos (debe ser el mismo a lo largo del foundation).
     * @param card carta a agregar al foundation.
     * @return true si el push fue exitoso, o false en caso contrario.
     */
    public boolean push(Card card) {
        if (card == null)
            return false;

        if(isEmpty() && card.getValue() == ACE){
            pile.push(card);
            return true;
        } else {
            var cardValues = Value.values();
            var givenCardIndex = card.getValue().ordinal();

            if(!isEmpty() && card.getSuit() == peek().getSuit() &&
                peek().getValue() == cardValues[givenCardIndex - indexCorrector]){
                    pile.push(card);
                    return true;
                }
        }
        return false;
    }

    /***
     * Elimina la última carta del foundation.
     * @return la última carta del foundation.
     */
    public Card pop() { return pile.pop(); }

    /***
     *Mira la última carta del foundation.
     * @return la última carta del foundation.
     */
    public Card peek() { return pile.peek(); }

    /***
     * @return true si el foundation está completo, o false en caso contrario.
     */
    public boolean isComplete(){
        if(isEmpty())
            return false;

        Card card = peek();

        return card.getValue() == K;

    }
}
