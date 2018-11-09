package com.example.qlem.pairsgame.game;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that represents the card object.
 */
public class Card implements Parcelable {

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

    // TODO add headers for parcelable implementation

    private Card(Parcel in) {
        resId = in.readInt();
        position = in.readInt();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resId);
        dest.writeInt(position);
    }
}
