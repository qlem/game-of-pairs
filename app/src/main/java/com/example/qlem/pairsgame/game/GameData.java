package com.example.qlem.pairsgame.game;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that stores the game's data.
 */
public class GameData implements Parcelable {

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

    private GameData(Parcel in) {
        scorePlayer1 = in.readInt();
        scorePlayer2 = in.readInt();
    }

    public static final Creator<GameData> CREATOR = new Creator<GameData>() {
        @Override
        public GameData createFromParcel(Parcel in) {
            return new GameData(in);
        }

        @Override
        public GameData[] newArray(int size) {
            return new GameData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(scorePlayer1);
        dest.writeInt(scorePlayer2);
    }
}
