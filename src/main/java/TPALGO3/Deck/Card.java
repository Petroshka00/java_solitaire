package TPALGO3.Deck;

import java.io.Serializable;

public class Card implements Serializable {

    public enum Value {ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, J, Q, K}
    public enum Color {BLACK, RED}
    public enum Suit {HEART, DIAMOND, CLUB, SPADE}

    private final Value value;
    private final Color color;
    private final Suit suit;
    private boolean faceUp;

    public Card(Value value, Color color, Suit suit) {
        this.value = value;
        this.color = color;
        this.suit = suit;
        this.faceUp = false;
    }

    /***
     * @return el valor de la carta.
     */
    public Value getValue() { return this.value; }

    /***
     * @return el color de la carta.
     */
    public Color getColor() { return this.color; }

    /***
     * @return el palo de la carta.
     */
    public Suit getSuit() { return this.suit; }

    /***
     * @return true si la carta está boca arriba, y false en caso contrario.
     */
    public boolean getFaceUp(){ return this.faceUp; }

    /***
     * setea el valor faceUp de la carta.
     * @param status valor a setear faceUp.
     */
    public void setFaceUp(boolean status){ faceUp = status; }

    /***
     * @return en mayúsculas, el valor de la carta seguido inmediatamente por su palo.
     */
    public String toString(){
        return (this.value.toString() + this.suit.toString());
    }

}
