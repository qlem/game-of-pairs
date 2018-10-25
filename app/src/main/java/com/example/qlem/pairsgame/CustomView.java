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

public class CustomView extends View {

    private int GAME_BOARD_SIZE = 0;
    private int GAME_BOARD_CELL_SIZE = 0;
    private int GAME_BOARD_X_ORIGIN = 0;
    private int GAME_BOARD_Y_ORIGIN = 0;
    private int NB_LINES = 4;
    private int NB_COLUMNS = 4;
    private List<Integer> resList = new ArrayList<>();
    private Card[][] cardsMatrix = new Card[NB_LINES][NB_COLUMNS];
    private List<Card> returnedCards = new ArrayList<>();

    private Rect card;

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

    private void initResourceList() {
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

        initResourceList();

        Random rand = new Random();
        int randI;
        for (int i = 0; i < NB_LINES; i++) {
            for (int j = 0; j < NB_COLUMNS; j++) {
                randI = rand.nextInt(resList.size());
                cardsMatrix[i][j] = new Card(resList.get(randI));
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
                if (cardsMatrix[i][j].state == CardState.HIDDEN) {
                    drawable = c.getDrawable(R.drawable.back_card);
                    if (drawable != null) {
                        drawable.setBounds(card);
                        drawable.draw(canvas);
                    }
                } else if (cardsMatrix[i][j].state == CardState.SHOWN) {
                    drawable = c.getDrawable(cardsMatrix[i][j].resId);
                    if (drawable != null) {
                        drawable.setBounds(card);
                        drawable.draw(canvas);
                    }
                }
                canvas.restore();
            }
        }
    }

    private void foo() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                Card card1 = returnedCards.get(0);
                Card card2 = returnedCards.get(1);
                if (card1.resId == card2.resId) {
                    card1.state = CardState.PAIRED;
                    card2.state = CardState.PAIRED;
                } else {
                    card1.state = CardState.HIDDEN;
                    card2.state = CardState.HIDDEN;
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
                    if (eventX >= cardsMatrix[i][j].fromX && eventX <= cardsMatrix[i][j].toX &&
                            eventY >= cardsMatrix[i][j].fromY && eventY <= cardsMatrix[i][j].toY &&
                            cardsMatrix[i][j].state != CardState.PAIRED) {

                        if (returnedCards.size() == 1 && cardsMatrix[i][j] == returnedCards.get(0)) {
                            return false;
                        }

                        if (returnedCards.size() <= 1) {
                            cardsMatrix[i][j].state = CardState.SHOWN;
                            returnedCards.add(cardsMatrix[i][j]);
                            if (returnedCards.size() == 2) {
                                foo();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width < height) {
            GAME_BOARD_SIZE = width;
            GAME_BOARD_X_ORIGIN = 0;
            GAME_BOARD_Y_ORIGIN = (height / 2) - (GAME_BOARD_SIZE / 2);
        } else {
            GAME_BOARD_SIZE = height;
            GAME_BOARD_X_ORIGIN = (width / 2) - (GAME_BOARD_SIZE / 2);
            GAME_BOARD_Y_ORIGIN = 0;
        }

        GAME_BOARD_CELL_SIZE = GAME_BOARD_SIZE / NB_COLUMNS;

        for (int i = 0; i < NB_LINES; i++) {
            for (int j = 0; j < NB_COLUMNS; j++) {
                cardsMatrix[i][j].fromX = GAME_BOARD_X_ORIGIN + (GAME_BOARD_CELL_SIZE * j);
                cardsMatrix[i][j].fromY = GAME_BOARD_Y_ORIGIN + (GAME_BOARD_CELL_SIZE * i);
                cardsMatrix[i][j].toX = cardsMatrix[i][j].fromX + GAME_BOARD_CELL_SIZE;
                cardsMatrix[i][j].toY = cardsMatrix[i][j].fromY + GAME_BOARD_CELL_SIZE;
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Log.i("DEBUG 02", String.valueOf(w) + " " + String.valueOf(h));
    }
}
