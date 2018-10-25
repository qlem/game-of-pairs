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
import com.example.qlem.pairsgame.game.DataGame;
import com.example.qlem.pairsgame.game.TextPosition;

import static android.graphics.Color.rgb;
import static com.example.qlem.pairsgame.game.CardState.HIDDEN;
import static com.example.qlem.pairsgame.game.CardState.PAIRED;
import static com.example.qlem.pairsgame.game.CardState.SHOWN;
import static com.example.qlem.pairsgame.game.Player.PLAYER_1;
import static com.example.qlem.pairsgame.game.Player.PLAYER_2;

public class CustomView extends View {

    private int NB_LINES = 4;
    private int NB_COLUMNS = 4;

    private int GAME_BOARD_SIZE = 0;
    private int GAME_BOARD_CELL_SIZE = 0;
    private int GAME_BOARD_X_ORIGIN = 0;
    private int GAME_BOARD_Y_ORIGIN = 0;

    private List<Integer> resList = new ArrayList<>();

    private Card[][] cards = new Card[NB_LINES][NB_COLUMNS];
    private List<Card> returnedCards = new ArrayList<>();

    private Rect card;
    private Paint paintText;

    private DataGame dataGame;
    private TextPosition textPosition;

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

        card = new Rect();

        dataGame = new DataGame();
        textPosition = new TextPosition();

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(rgb(0, 0, 0));

        fillResourceList();

        Random rand = new Random();
        int randI;
        for (int i = 0; i < NB_LINES; i++) {
            for (int j = 0; j < NB_COLUMNS; j++) {
                randI = rand.nextInt(resList.size());
                cards[i][j] = new Card(resList.get(randI));
                resList.remove(randI);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x;
        int y;
        card.set(-GAME_BOARD_CELL_SIZE / 2 + 10, -GAME_BOARD_CELL_SIZE / 2 + 10,
                GAME_BOARD_CELL_SIZE / 2 - 10, GAME_BOARD_CELL_SIZE / 2 - 10);
        for (int i = 0; i < NB_LINES; i++) {
            y = GAME_BOARD_Y_ORIGIN + (GAME_BOARD_CELL_SIZE * i) + (GAME_BOARD_CELL_SIZE / 2);
            for (int j = 0; j < NB_COLUMNS; j++) {
                x = GAME_BOARD_X_ORIGIN + (GAME_BOARD_CELL_SIZE * j) + (GAME_BOARD_CELL_SIZE / 2);
                canvas.save();
                canvas.translate(x, y);
                Drawable drawable;
                Context c = getContext();
                if (cards[i][j].state == HIDDEN) {
                    drawable = c.getDrawable(R.drawable.back_card);
                    if (drawable != null) {
                        drawable.setBounds(card);
                        drawable.draw(canvas);
                    }
                } else if (cards[i][j].state == SHOWN) {
                    drawable = c.getDrawable(cards[i][j].resId);
                    if (drawable != null) {
                        drawable.setBounds(card);
                        drawable.draw(canvas);
                    }
                }
                canvas.restore();
            }
        }

        canvas.drawText("Score", textPosition.COLUMN_1, textPosition.LINE_1, paintText);
        canvas.drawText("Player 1: " + String.valueOf(dataGame.scorePlayer1),
                textPosition.COLUMN_1, textPosition.LINE_2, paintText);
        canvas.drawText("Player 2: " + String.valueOf(dataGame.scorePlayer2),
                textPosition.COLUMN_1, textPosition.LINE_3, paintText);

        canvas.drawText("Turn", textPosition.COLUMN_2, textPosition.LINE_1, paintText);
        if (dataGame.playerTurn == PLAYER_1) {
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
                    card1.state = PAIRED;
                    card2.state = PAIRED;

                    if (dataGame.playerTurn == PLAYER_1) {
                        dataGame.scorePlayer1++;
                    } else {
                        dataGame.scorePlayer2++;
                    }

                } else {
                    card1.state = HIDDEN;
                    card2.state = HIDDEN;

                }

                if (dataGame.playerTurn == PLAYER_1) {
                    dataGame.playerTurn = PLAYER_2;
                } else {
                    dataGame.playerTurn = PLAYER_1;
                }

                returnedCards.clear();
                invalidate();
            }
        }, 1500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            float eventX = event.getX();
            float eventY = event.getY();
            for (int i = 0; i < NB_LINES; i++) {
                for (int j = 0; j < NB_COLUMNS; j++) {
                    if (eventX >= cards[i][j].fromX && eventX <= cards[i][j].toX &&
                            eventY >= cards[i][j].fromY && eventY <= cards[i][j].toY &&
                            cards[i][j].state != PAIRED) {

                        if (returnedCards.size() == 1 && cards[i][j] == returnedCards.get(0)) {
                            return false;
                        }

                        if (returnedCards.size() <= 1) {
                            cards[i][j].state = SHOWN;
                            returnedCards.add(cards[i][j]);
                            if (returnedCards.size() == 2) {
                                validateFlipping();
                            }
                        }
                    }
                }
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
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

        GAME_BOARD_CELL_SIZE = GAME_BOARD_SIZE / NB_COLUMNS;

        for (int i = 0; i < NB_LINES; i++) {
            for (int j = 0; j < NB_COLUMNS; j++) {
                cards[i][j].fromX = GAME_BOARD_X_ORIGIN + (GAME_BOARD_CELL_SIZE * j);
                cards[i][j].fromY = GAME_BOARD_Y_ORIGIN + (GAME_BOARD_CELL_SIZE * i);
                cards[i][j].toX = cards[i][j].fromX + GAME_BOARD_CELL_SIZE;
                cards[i][j].toY = cards[i][j].fromY + GAME_BOARD_CELL_SIZE;
            }
        }

        setTextPosition(landscape, width, height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Log.i("DEBUG 02", String.valueOf(w) + " " + String.valueOf(h));
    }
}
