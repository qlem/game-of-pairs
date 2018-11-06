package com.example.qlem.pairsgame.game;

/**
 * Class that represents the card object.
 */
public class Card {

    /**
     * Resource id of the card (picture id).
     */
    public int resId;

    /**
     * State of the card: HIDDEN / SHOWN / PAIRED.
     */
    public CardState state;

    /**
     * Index of the card in the card list of the game board view.
     */
    public int position;

    /**
     * Constructor of the card class, initializes a new card.
     * @param resId resource id
     * @param position card index
     */
    public Card(int resId, int position) {
        this.resId = resId;
        this.state = CardState.HIDDEN;
        this.position = position;
    }
}
