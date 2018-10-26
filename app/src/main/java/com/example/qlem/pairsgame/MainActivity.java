package com.example.qlem.pairsgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.qlem.pairsgame.game.Player;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomView gameBoard = findViewById(R.id.game_board);
        TextView scoreTextView = findViewById(R.id.score_board);
        TextView turnTextView = findViewById(R.id.turn_board);

        int scorePlayer1 = gameBoard.getScorePlayer1();
        int scorePlayer2 = gameBoard.getScorePlayer2();
        Player playerTurn = gameBoard.getPlayerTurn();

        String score = "Score\nplayer 1:  " + String.valueOf(scorePlayer1) + "\nplayer 2:  " + String.valueOf(scorePlayer2);
        scoreTextView.setText(score);
    }
}
