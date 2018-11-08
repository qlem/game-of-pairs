package com.example.qlem.pairsgame;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.example.qlem.pairsgame.game.Card;
import com.example.qlem.pairsgame.game.GameState;
import com.example.qlem.pairsgame.game.Player;

import java.util.List;

/**
 * This class creates the state of the game board view to be restored.
 */
public class SavedState extends View.BaseSavedState {

    /**
     * The current list of the cards.
     */
    List<Card> cards;

    /**
     * The current list of cards face up.
     */
    List<Card> returnedCards;

    /**
     * The current player's turn.
     */
    Player playerTurn;

    /**
     * The current value of the player 1's score.
     */
    int scorePlayer1;

    /**
     * The current value of the player 2's score.
     */
    int scorePlayer2;

    /**
     * The current state of the game.
     */
    GameState gameState;

    /**
     * Constructor of the class, used while restoring.
     * @param source parcel to read from
     */
    private SavedState(Parcel source) {
        super(source);
    }

    /**
     * Constructor of the class, used while saving.
     * @param superState the state of the superclass of this view
     */
    SavedState(Parcelable superState) {
        super(superState);
    }

    /**
     * This function writes the values that compose the state into a parcel.
     * @param out the parcel in which the state should be written
     * @param flags flags about how the state should be written
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeList(cards);
        out.writeList(returnedCards);
        out.writeString(playerTurn.name());
        out.writeInt(scorePlayer1);
        out.writeInt(scorePlayer2);
        out.writeString(gameState.name());
    }

    /**
     * This initializes the parcelable that will contain the state.
     */
    public static final Parcelable.Creator<SavedState> CREATOR
            = new Parcelable.Creator<SavedState>() {
        public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };
}
