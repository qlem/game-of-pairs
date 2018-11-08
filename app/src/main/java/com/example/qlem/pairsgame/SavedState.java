package com.example.qlem.pairsgame;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.example.qlem.pairsgame.game.Card;
import com.example.qlem.pairsgame.game.GameState;
import com.example.qlem.pairsgame.game.Player;

import java.util.List;

public class SavedState extends View.BaseSavedState {

    List<Card> cards;
    List<Card> returnedCards;
    Player playerTurn;
    int scorePlayer1;
    int scorePlayer2;
    GameState gameState;


    private SavedState(Parcel source) {
        super(source);
    }

    SavedState(Parcelable superState) {
        super(superState);
    }

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
