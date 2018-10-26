package com.example.qlem.pairsgame.game;

public class Card {
    public int resId;
    public CardState state;
    public int position;

    public Card(int resId, int position) {
        this.resId = resId;
        this.state = CardState.HIDDEN;
        this.position = position;
    }
}
