package com.example.qlem.pairsgame.game;

/**
 * Class that stores the game's data.
 */
public class GameData {

    /**
     * The player's turn, indicates the expected player to play: PLAYER 1 / PLAYER 2.
     */
    public Player playerTurn;

    /**
     * Score of the player 1.
     */
    public int scorePlayer1;

    /**
     * Score of the player 2.
     */
    public int scorePlayer2;

    /**
     * The status of the game: RUNNING / FINISHED.
     */
    public GameState gameState;

    /**
     *  Constructor of the game's data class, initializes a new object.
     */
    public GameData() {
        this.playerTurn = Player.PLAYER_1;
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
        this.gameState = GameState.RUNNING;
    }
}
