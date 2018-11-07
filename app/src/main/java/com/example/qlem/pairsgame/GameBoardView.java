package com.example.qlem.pairsgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.qlem.pairsgame.game.Card;
import com.example.qlem.pairsgame.game.CardState;
import com.example.qlem.pairsgame.game.gameData;
import com.example.qlem.pairsgame.game.GameState;
import com.example.qlem.pairsgame.game.Player;

/**
 * Class that defines the custom view of the game board.
 */
public class GameBoardView extends View {

    /**
     * Variable that stores the number of cards.
     */
    private int NB_CARD = 16;

    /**
     * Variable that stores the cell's size of the game board.
     */
    private int GAME_BOARD_CELL_SIZE = 0;

    /**
     * List that stores two times the id of each resource (animal pictures).
     */
    private List<Integer> resList;

    /**
     * List of the cards.
     */
    private List<Card> cards;

    /**
     * List that stores the returned cards (maximum two cards).
     */
    private List<Card> returnedCards;

    /**
     * Variable that stored the game's data object.
     */
    private gameData gameData;

    /**
     * The rectangle (graphic shape) used to draw a card.
     */
    private Rect card;

    /**
     * Card that is targeted by the user input.
     */
    private Card targetedCard;

    /**
     * Stores the event listener for the game data changes.
     */
    private OnDataChangeListener onDataChangeListener;

    /**
     * Stores the thread that performs the flipping of two card.
     */
    private Runnable flipping;

    /**
     * Constructor of the game board view class.
     * @param c context
     */
    public GameBoardView(Context c) {
        super(c);
        init();
    }

    /**
     * Constructor of the game board view class.
     * @param c context
     * @param as attribute set
     */
    public GameBoardView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    /**
     * Constructor of the game board view class.
     * @param c context
     * @param as attribute set
     * @param default_style default style
     */
    public GameBoardView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    /**
     * Function that fills the resource list by adding two times each picture id of each animal.
     */
    private void fillResourceList() {
        resList.add(R.drawable.chicken);
        resList.add(R.drawable.chicken);
        resList.add(R.drawable.cow);
        resList.add(R.drawable.cow);
        resList.add(R.drawable.crocodile);
        resList.add(R.drawable.crocodile);
        resList.add(R.drawable.elephant);
        resList.add(R.drawable.elephant);
        resList.add(R.drawable.lion);
        resList.add(R.drawable.lion);
        resList.add(R.drawable.parrot);
        resList.add(R.drawable.parrot);
        resList.add(R.drawable.tiger);
        resList.add(R.drawable.tiger);
        resList.add(R.drawable.turtle);
        resList.add(R.drawable.turtle);
    }

    /**
     * Function that fills the card list. Creates one card for each item picked up randomly
     * in the resource list.
     */
    private void fillCardList() {
        Random rand = new Random();
        int randI;
        for (int i = 0; i < NB_CARD; i++) {
            randI = rand.nextInt(resList.size());
            cards.add(new Card(resList.get(randI), i));
            resList.remove(randI);
        }
    }

    /**
     * Function that initializes all variables used by the game board view.
     */
    private void init() {
        resList = new ArrayList<>();
        cards = new ArrayList<>();
        returnedCards = new ArrayList<>();
        card = new Rect();
        gameData = new gameData();
        fillResourceList();
        fillCardList();
        flipping = null;
    }

    /**
     * Function called to draw the cards in the view.
     * @param canvas corresponds to the drawing area
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int x;
        int y;
        Drawable drawable;
        Context c = getContext();
        int offset = (int)(GAME_BOARD_CELL_SIZE * 0.92) / 2;
        int contentOffset = (int)(GAME_BOARD_CELL_SIZE * 0.7) / 2;
        for (int i = 0; i < NB_CARD; i++) {
            int line = i / 4;
            int column = - line * 4 + i;
            x = (GAME_BOARD_CELL_SIZE * column) + (GAME_BOARD_CELL_SIZE / 2);
            y = (GAME_BOARD_CELL_SIZE * line) + (GAME_BOARD_CELL_SIZE / 2);
            card.set(-offset, -offset, offset, offset);
            canvas.save();
            canvas.translate(x, y);
            switch (cards.get(i).state) {
                case HIDDEN:
                    drawable = c.getDrawable(R.drawable.card_face_down);
                    if (drawable != null) {
                        drawable.setBounds(card);
                        drawable.draw(canvas);
                    }
                    break;
                case SHOWN:
                    drawable = c.getDrawable(R.drawable.card_face_up);
                    if (drawable != null) {
                        drawable.setBounds(card);
                        drawable.draw(canvas);
                    }
                    card.set(-contentOffset, -contentOffset, contentOffset, contentOffset);
                    drawable = c.getDrawable(cards.get(i).resId);
                    if (drawable != null) {
                        drawable.setBounds(card);
                        drawable.draw(canvas);
                    }
                    break;
                default:
                    break;
            }
            canvas.restore();
        }
    }

    /**
     * Function that returns true if the end game have been reached or false.
     * @return TRUE / FALSE
     */
    private boolean isEndGame() {
        int count = 0;
        for (int i = 0; i < NB_CARD; i++) {
            if (cards.get(i).state == CardState.PAIRED) {
                count++;
            }
        }
        return count == NB_CARD;
    }

    /**
     * Thread that performs the flipping of two cards and proceeds to some actions
     * according to the game logic.
     */
    private Runnable newFlipping = new Runnable() {
        @Override
        public void run() {
            Card card1 = returnedCards.get(0);
            Card card2 = returnedCards.get(1);
            if (card1.resId == card2.resId) {
                card1.state = CardState.PAIRED;
                card2.state = CardState.PAIRED;
                if (gameData.playerTurn == Player.PLAYER_1) {
                    gameData.scorePlayer1++;
                } else {
                    gameData.scorePlayer2++;
                }
            } else {
                card1.state = CardState.HIDDEN;
                card2.state = CardState.HIDDEN;
            }
            if (gameData.playerTurn == Player.PLAYER_1) {
                gameData.playerTurn = Player.PLAYER_2;
            } else {
                gameData.playerTurn = Player.PLAYER_1;
            }
            if (isEndGame()) {
                gameData.gameState = GameState.FINISHED;
            }
            onDataChangeListener.onDataChangeListener(gameData.gameState, gameData.playerTurn,
                    gameData.scorePlayer1, gameData.scorePlayer2);
            returnedCards.clear();
            invalidate();
            flipping = null;
        }
    };

    /**
     * Function that return true if the user touches a card and moves out the card area or false.
     * @param cardIndex card index in the list
     * @param eventX coordinate X of the move event
     * @param eventY coordinate Y of the move event
     * @return TRUE / FALSE
     */
    private boolean isMovedOutTargetedCard(int cardIndex, float eventX, float eventY) {
        int line = cardIndex / 4;
        int column = - line * 4 + cardIndex;
        int fromX = GAME_BOARD_CELL_SIZE * column;
        int fromY = GAME_BOARD_CELL_SIZE * line;
        int toX = (GAME_BOARD_CELL_SIZE * column) + GAME_BOARD_CELL_SIZE;
        int toY = (GAME_BOARD_CELL_SIZE * line) + GAME_BOARD_CELL_SIZE;
        return eventX <= fromX || eventX >= toX || eventY <= fromY || eventY >= toY;
    }

    /**
     * Function that return the targeted card by the user.
     * @param eventX coordinate X of the touch event
     * @param eventY coordinate Y of the touch event
     * @return targeted card
     */
    private Card getTargetedCard(float eventX, float eventY) {
        int column = Math.round(eventX) / GAME_BOARD_CELL_SIZE;
        int line = Math.round(eventY) / GAME_BOARD_CELL_SIZE;
        int index = line * 4 + column;
        return cards.get(index);
    }

    /**
     * Function that indicates the system to perform a click.
     * @return TRUE
     */
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    /**
     * Function called when the user touches the game board view, performs some actions
     * according to the game logic and the event type (down, up or move action).
     * @param event the event object
     * @return TRUE / FALSE
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                targetedCard = getTargetedCard(eventX, eventY);
                if (targetedCard.state != CardState.HIDDEN || returnedCards.size() == 2) {
                    targetedCard = null;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (targetedCard == null) {
                    return false;
                }
                Card targetedCardUp = getTargetedCard(eventX, eventY);
                if (targetedCard != targetedCardUp) {
                    targetedCard = null;
                    return false;
                }
                targetedCard.state = CardState.SHOWN;
                returnedCards.add(targetedCard);
                if (returnedCards.size() == 2) {
                    flipping = newFlipping;
                    postDelayed(flipping, 1500);
                }
                performClick();
                invalidate();
                targetedCard = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (targetedCard != null &&
                        isMovedOutTargetedCard(targetedCard.position, eventX, eventY)) {
                    targetedCard = null;
                    return false;
                }
                break;
        }
        return true;
    }

    /**
     * Function that initializes the width and the height of the game board view.
     * @param widthMeasureSpec width of the screen
     * @param heightMeasureSpec height of the screen
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int gameBoardSize;
        if (width < height) {
            gameBoardSize = width;
        } else {
            gameBoardSize = height;
        }
        GAME_BOARD_CELL_SIZE = gameBoardSize / 4;
        setMeasuredDimension(gameBoardSize, gameBoardSize);
    }

    /**
     * This function initializes the data changes event listener.
     * @param onDataChangeListener listener transmitted by the main activity.
     */
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    /**
     * Function that resets all game's data for start a new game.
     */
    public void resetGame() {
        removeCallbacks(flipping);
        targetedCard = null;
        cards.clear();
        returnedCards.clear();
        gameData = new gameData();
        fillResourceList();
        fillCardList();
        onDataChangeListener.onDataChangeListener(gameData.gameState, gameData.playerTurn,
                gameData.scorePlayer1, gameData.scorePlayer2);
        invalidate();
    }
}
