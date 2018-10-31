package com.example.qlem.pairsgame;

import com.example.qlem.pairsgame.game.GameState;
import com.example.qlem.pairsgame.game.Player;

public interface OnDataChangeListener {
    void onDataChangeListener(GameState gameState, Player playerTurn, int player1Score, int player2Score);
}
