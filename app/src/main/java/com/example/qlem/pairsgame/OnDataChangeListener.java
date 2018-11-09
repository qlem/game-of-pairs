package com.example.qlem.pairsgame;

import com.example.qlem.pairsgame.game.GameData;

/**
 * This interface implements the data changes event listener for the game board view.
 */
public interface OnDataChangeListener {

    /**
     * This function is the event listener for the game board data changes.
     * @param gameData provides information about the current game
     */
    void onDataChangeListener(GameData gameData);
}
