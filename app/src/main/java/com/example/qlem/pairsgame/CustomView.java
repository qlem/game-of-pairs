package com.example.qlem.pairsgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
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
    private Card[][] matrix = new Card[NB_LINES][NB_COLUMNS];

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
                matrix[i][j] = new Card(resList.get(randI));
                resList.remove(randI);
            }
        }
    }

    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        int x;
        int y;
        int cellSize = GAME_BOARD_SIZE / NB_COLUMNS;
        card.set(-cellSize / 2 + 10, -cellSize / 2 + 10, cellSize / 2 - 10, cellSize / 2 - 10);
        for (int i = 0; i < NB_LINES; i++) {
            y = GAME_BOARD_Y_ORIGIN + (cellSize * i) + (cellSize / 2);
            for (int j = 0; j < NB_COLUMNS; j++) {
                x = GAME_BOARD_X_ORIGIN + (cellSize * j) + (cellSize / 2);
                canvas.save();
                canvas.translate(x, y);
                Drawable drawable;
                Context c = getContext();
                if (matrix[i][j].state == CardState.HIDDEN) {
                    drawable = c.getDrawable(R.drawable.back_card);
                    if (drawable != null) {
                        drawable.setBounds(card);
                        drawable.draw(canvas);
                    }
                } else if (matrix[i][j].state == CardState.SHOWN) {
                    drawable = c.getDrawable(matrix[i][j].resId);
                    if (drawable != null) {
                        drawable.setBounds(card);
                        drawable.draw(canvas);
                    }
                }
                canvas.restore();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            float eventX = event.getX();
            float eventY = event.getY();
            for (int i = 0; i < NB_LINES; i++) {
                for (int j = 0; j < NB_COLUMNS; j++) {
                    if (eventX >= matrix[i][j].fromX && eventX <= matrix[i][j].toX &&
                            eventY >= matrix[i][j].fromY && eventY <= matrix[i][j].toY) {
                        matrix[i][j].state = CardState.SHOWN;
                        break;
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
                matrix[i][j].fromX = GAME_BOARD_X_ORIGIN + (GAME_BOARD_CELL_SIZE * j);
                matrix[i][j].fromY = GAME_BOARD_Y_ORIGIN + (GAME_BOARD_CELL_SIZE * i);
                matrix[i][j].toX = matrix[i][j].fromX + GAME_BOARD_CELL_SIZE;
                matrix[i][j].toY = matrix[i][j].fromY + GAME_BOARD_CELL_SIZE;
            }
        }

        Log.i("DEBUG", "WIDTH: " + String.valueOf(width));
        Log.i("DEBUG", "HEIGHT: " + String.valueOf(height));

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Log.i("DEBUG 02", String.valueOf(w) + " " + String.valueOf(h));
    }
}
