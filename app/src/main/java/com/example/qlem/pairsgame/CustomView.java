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
import com.example.qlem.pairsgame.game.DataGame;
import com.example.qlem.pairsgame.game.Player;


public class CustomView extends View {

    private int GAME_BOARD_CELL_SIZE = 0;
    private List<Integer> resList;
    private List<Card> cards;
    private List<Card> returnedCards;
    private DataGame dataGame;
    private Rect card;
    private Card targetedCard;
    private OnDataChangeListener onDataChangeListener;
    private Runnable flipping;

    public CustomView(Context c) {
        super(c);
        init();
    }

    public CustomView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    public CustomView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

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

    private void distributeCards() {
        Random rand = new Random();
        int randI;
        for (int i = 0; i < 16; i++) {
            randI = rand.nextInt(resList.size());
            cards.add(new Card(resList.get(randI), i));
            resList.remove(randI);
        }
    }

    private void init() {
        resList = new ArrayList<>();
        cards = new ArrayList<>();
        returnedCards = new ArrayList<>();
        card = new Rect();
        dataGame = new DataGame();
        fillResourceList();
        distributeCards();
        flipping = null;
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        card.set(-GAME_BOARD_CELL_SIZE / 2 + 10, -GAME_BOARD_CELL_SIZE / 2 + 10,
                GAME_BOARD_CELL_SIZE / 2 - 10, GAME_BOARD_CELL_SIZE / 2 - 10);
        int x;
        int y;
        for (int i = 0; i < 16; i++) {
            int line = i / 4;
            int column = - line * 4 + i;
            x = (GAME_BOARD_CELL_SIZE * column) + (GAME_BOARD_CELL_SIZE / 2);
            y = (GAME_BOARD_CELL_SIZE * line) + (GAME_BOARD_CELL_SIZE / 2);
            canvas.save();
            canvas.translate(x, y);
            Drawable drawable;
            Context c = getContext();
            if (cards.get(i).state == CardState.HIDDEN) {
                drawable = c.getDrawable(R.drawable.back_card);
                if (drawable != null) {
                    drawable.setBounds(card);
                    drawable.draw(canvas);
                }
            } else if (cards.get(i).state == CardState.SHOWN) {
                drawable = c.getDrawable(cards.get(i).resId);
                if (drawable != null) {
                    drawable.setBounds(card);
                    drawable.draw(canvas);
                }
            }
            canvas.restore();
        }
    }

    private Runnable newFlipping = new Runnable() {
        @Override
        public void run() {
            Card card1 = returnedCards.get(0);
            Card card2 = returnedCards.get(1);
            if (card1.resId == card2.resId) {
                card1.state = CardState.PAIRED;
                card2.state = CardState.PAIRED;
                if (dataGame.playerTurn == Player.PLAYER_1) {
                    dataGame.scorePlayer1++;
                } else {
                    dataGame.scorePlayer2++;
                }
            } else {
                card1.state = CardState.HIDDEN;
                card2.state = CardState.HIDDEN;
            }
            if (dataGame.playerTurn == Player.PLAYER_1) {
                dataGame.playerTurn = Player.PLAYER_2;
            } else {
                dataGame.playerTurn = Player.PLAYER_1;
            }
            onDataChangeListener.onDataChangeListener(dataGame.gameState, dataGame.playerTurn,
                    dataGame.scorePlayer1, dataGame.scorePlayer2);
            returnedCards.clear();
            invalidate();
            flipping = null;
        }
    };

    private boolean isMovedOutTargetedCard(int cardIndex, float eventX, float eventY) {
        int line = cardIndex / 4;
        int column = - line * 4 + cardIndex;
        int fromX = GAME_BOARD_CELL_SIZE * column;
        int fromY = GAME_BOARD_CELL_SIZE * line;
        int toX = (GAME_BOARD_CELL_SIZE * column) + GAME_BOARD_CELL_SIZE;
        int toY = (GAME_BOARD_CELL_SIZE * line) + GAME_BOARD_CELL_SIZE;
        return eventX <= fromX || eventX >= toX || eventY <= fromY || eventY >= toY;
    }

    private Card getTargetedCard(float eventX, float eventY) {
        int column = Math.round(eventX) / GAME_BOARD_CELL_SIZE;
        int line = Math.round(eventY) / GAME_BOARD_CELL_SIZE;
        int index = line * 4 + column;
        return cards.get(index);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                targetedCard = getTargetedCard(eventX, eventY);
                if (targetedCard.state == CardState.PAIRED || (returnedCards.size() == 1 &&
                        targetedCard == returnedCards.get(0))) {
                    targetedCard = null;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (targetedCard == null || returnedCards.size() == 2) {
                    targetedCard = null;
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
                    postDelayed(newFlipping, 1500);
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

    public void resetGame() {
        removeCallbacks(flipping);
        targetedCard = null;
        cards.clear();
        returnedCards.clear();
        dataGame = new DataGame();
        fillResourceList();
        distributeCards();
        onDataChangeListener.onDataChangeListener(dataGame.gameState, dataGame.playerTurn,
                dataGame.scorePlayer1, dataGame.scorePlayer2);
        invalidate();
    }
}
