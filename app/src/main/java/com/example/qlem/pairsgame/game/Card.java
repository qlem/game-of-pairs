package com.example.qlem.pairsgame.game;

public class Card {
    public int resId;
    public CardState state;
    /* public int fromX;
    public int fromY;
    public int toX;
    public int toY; */

    public Card(int resId) {
        this.resId = resId;
        this.state = CardState.HIDDEN;
    }
}
