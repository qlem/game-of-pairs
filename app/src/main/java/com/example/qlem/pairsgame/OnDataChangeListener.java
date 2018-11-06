package com.example.qlem.pairsgame;

import com.example.qlem.pairsgame.game.GameState;
import com.example.qlem.pairsgame.game.Player;

/**
 * This is the interface that implements the event listener in the game board.
 */
public interface OnDataChangeListener {

    /**
     * This function is the event listener of game board's data change.
     * @param gameState state of the game: RUNNING / FINISHED
     * @param playerTurn player's turn: PLAYER 1 / PLAYER 2
     * @param player1Score player 1's score: corresponds to the number of pairs of cards
     * @param player2Score player 2's score: corresponds to the number of pairs of cards
     */
    void onDataChangeListener(GameState gameState, Player playerTurn, int player1Score, int player2Score);
}
