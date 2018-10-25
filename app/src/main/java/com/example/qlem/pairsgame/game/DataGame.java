package com.example.qlem.pairsgame.game;

import static com.example.qlem.pairsgame.game.Player.PLAYER_1;

public class DataGame {
    public Player playerTurn;
    public int scorePlayer1;
    public int scorePlayer2;

    public DataGame() {
        this.playerTurn = PLAYER_1;
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
    }
}
