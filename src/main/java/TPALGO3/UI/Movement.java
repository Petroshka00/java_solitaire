package TPALGO3.UI;

public class Movement {

    private String sourceMovementClass;
    private int sourceIndex;
    private int amountOfCardsToMove;
    private boolean isOnGoing;
    private static final int initSourceIndex = -1;
    private static final int initAmountOfCardsToMove = -1;
    private static final String initSourceMovementClass = "";

    public Movement() {
        sourceMovementClass = initSourceMovementClass;
        sourceIndex = initSourceIndex;
        amountOfCardsToMove = initAmountOfCardsToMove;
        isOnGoing = false;
    }

    public void resetMovement(){
        sourceMovementClass = initSourceMovementClass;
        sourceIndex = initSourceIndex;
        amountOfCardsToMove = initAmountOfCardsToMove;
        isOnGoing = false;
    }

    public String getSourceMovementClass() {
        return sourceMovementClass;
    }

    public void setSourceMovementClass(String sourceMovementClass) {
        this.sourceMovementClass = sourceMovementClass;
    }

    public int getSourceIndex() {
        return sourceIndex;
    }

    public void setSourceIndex(int sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    public int getAmountOfCardsToMove() {
        return amountOfCardsToMove;
    }

    public void setAmountOfCardsToMove(int amountOfCardsToMove) {
        this.amountOfCardsToMove = amountOfCardsToMove;
    }

    public boolean isOnGoing() {
        return isOnGoing;
    }

    public void setOnGoing(boolean onGoing) {
        isOnGoing = onGoing;
    }
}
