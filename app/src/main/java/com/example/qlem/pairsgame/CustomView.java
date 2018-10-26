package com.example.qlem.pairsgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import com.example.qlem.pairsgame.game.TextPosition;

import static android.graphics.Color.rgb;


public class CustomView extends View {

    private int GAME_BOARD_SIZE = 0;
    private int GAME_BOARD_CELL_SIZE = 0;
    private int GAME_BOARD_X_ORIGIN = 0;
    private int GAME_BOARD_Y_ORIGIN = 0;

    private List<Integer> resList;
    private List<Card> cards;
    private List<Card> returnedCards;

    private DataGame dataGame;
    private TextPosition textPosition;

    private Rect card;
    private Paint paintText;

    private Card targetedCard;

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

    private void init() {
        resList = new ArrayList<>();
        cards = new ArrayList<>();
        returnedCards = new ArrayList<>();
        card = new Rect();
        dataGame = new DataGame();
        textPosition = new TextPosition();
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(rgb(0, 0, 0));
        fillResourceList();
        Random rand = new Random();
        int randI;
        for (int i = 0; i < 16; i++) {
            randI = rand.nextInt(resList.size());
            cards.add(new Card(resList.get(randI)));
            resList.remove(randI);
        }
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
            x = GAME_BOARD_X_ORIGIN + (GAME_BOARD_CELL_SIZE * column) + (GAME_BOARD_CELL_SIZE / 2);
            y = GAME_BOARD_Y_ORIGIN + (GAME_BOARD_CELL_SIZE * line) + (GAME_BOARD_CELL_SIZE / 2);
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

        canvas.drawText("Score", textPosition.COLUMN_1, textPosition.LINE_1, paintText);
        canvas.drawText("Player 1: " + String.valueOf(dataGame.scorePlayer1),
                textPosition.COLUMN_1, textPosition.LINE_2, paintText);
        canvas.drawText("Player 2: " + String.valueOf(dataGame.scorePlayer2),
                textPosition.COLUMN_1, textPosition.LINE_3, paintText);
        canvas.drawText("Turn", textPosition.COLUMN_2, textPosition.LINE_1, paintText);
        if (dataGame.playerTurn == Player.PLAYER_1) {
            canvas.drawText("player 1", textPosition.COLUMN_2, textPosition.LINE_2, paintText);
        } else {
            canvas.drawText("player 2", textPosition.COLUMN_2, textPosition.LINE_2, paintText);
        }
    }

    private void validateFlipping() {
        postDelayed(new Runnable() {
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
                returnedCards.clear();
                invalidate();
            }
        }, 1500);
    }

    private Card getTargetedCard(float eventX, float eventY) {
        int column = (Math.round(eventX) - GAME_BOARD_X_ORIGIN) / GAME_BOARD_CELL_SIZE;
        int line = (Math.round(eventY) - GAME_BOARD_Y_ORIGIN) / GAME_BOARD_CELL_SIZE;
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
                if (eventX >= GAME_BOARD_X_ORIGIN && eventY >= GAME_BOARD_Y_ORIGIN &&
                        eventX <= GAME_BOARD_X_ORIGIN + GAME_BOARD_SIZE &&
                        eventY <= GAME_BOARD_Y_ORIGIN + GAME_BOARD_SIZE) {
                    targetedCard = getTargetedCard(eventX, eventY);
                    if (targetedCard.state == CardState.PAIRED) {
                        targetedCard = null;
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (eventX >= GAME_BOARD_X_ORIGIN && eventY >= GAME_BOARD_Y_ORIGIN &&
                        eventX <= GAME_BOARD_X_ORIGIN + GAME_BOARD_SIZE &&
                        eventY <= GAME_BOARD_Y_ORIGIN + GAME_BOARD_SIZE) {
                    if (targetedCard == null || returnedCards.size() == 2) {
                        targetedCard = null;
                        return false;
                    }
                    Card targetedCardUp = getTargetedCard(eventX, eventY);
                    if (targetedCard != targetedCardUp) {
                        return false;
                    }
                    if (returnedCards.size() == 1 && targetedCard == returnedCards.get(0)) {
                        return false;
                    }
                    if (returnedCards.size() <= 1) {
                        targetedCard.state = CardState.SHOWN;
                        returnedCards.add(targetedCard);
                        if (returnedCards.size() == 2) {
                            validateFlipping();
                        }
                    }
                }
                targetedCard = null;
                performClick();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                /* if (targetedCard != null && (eventX < targetedCard.fromX ||
                        eventX > targetedCard.toX || eventY < targetedCard.fromY ||
                        eventY > targetedCard.toY)) {
                    targetedCard = null;
                    return false;
                } */
                break;
        }
        return true;
    }

    private void setTextPosition(boolean isLandscape, int widthScreen, int heightScreen) {
        if (!isLandscape) {
            float lineHeight = GAME_BOARD_Y_ORIGIN / 3;
            paintText.setTextSize(lineHeight * 0.5f);
            textPosition.COLUMN_1 = 10;
            textPosition.COLUMN_2 = widthScreen / 2;
            textPosition.LINE_1 = lineHeight - (lineHeight / 2);
            textPosition.LINE_2 = lineHeight * 2 - (lineHeight / 2);
            textPosition.LINE_3 = lineHeight * 3 - (lineHeight / 2);
        } else {
            float lineHeight = heightScreen / 6;
            paintText.setTextSize(lineHeight * 0.5f);
            textPosition.COLUMN_1 = 10;
            textPosition.COLUMN_2 = GAME_BOARD_X_ORIGIN + GAME_BOARD_SIZE + 10;
            textPosition.LINE_1 = lineHeight - (lineHeight / 2);
            textPosition.LINE_2 = lineHeight * 2 - (lineHeight / 2);
            textPosition.LINE_3 = lineHeight * 3 - (lineHeight / 2);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        boolean landscape = false;
        if (width < height) {
            GAME_BOARD_SIZE = width;
            GAME_BOARD_X_ORIGIN = 0;
            GAME_BOARD_Y_ORIGIN = (height / 2) - (GAME_BOARD_SIZE / 2);
        } else {
            GAME_BOARD_SIZE = height;
            GAME_BOARD_X_ORIGIN = (width / 2) - (GAME_BOARD_SIZE / 2);
            GAME_BOARD_Y_ORIGIN = 0;
            landscape = true;
        }
        GAME_BOARD_CELL_SIZE = GAME_BOARD_SIZE / 4;
        setTextPosition(landscape, width, height);
        setMeasuredDimension(width, height);
    }
}
