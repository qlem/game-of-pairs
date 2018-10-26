package com.example.qlem.pairsgame.game;

public class DataGame {
    public Player playerTurn;
    public int scorePlayer1;
    public int scorePlayer2;
    public GameState gameState;

    public DataGame() {
        this.playerTurn = Player.PLAYER_1;
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
        this.gameState = GameState.RUNNING;
    }
}
