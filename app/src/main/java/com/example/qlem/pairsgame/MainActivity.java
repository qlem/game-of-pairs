package com.example.qlem.pairsgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlem.pairsgame.game.GameState;
import com.example.qlem.pairsgame.game.Player;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView scorePlayer1View = findViewById(R.id.score_player_1);
        final TextView scorePlayer2View = findViewById(R.id.score_player_2);
        final TextView turnView = findViewById(R.id.player_turn);

        final CustomView gameBoard = findViewById(R.id.game_board);
        gameBoard.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChangeListener(GameState gameState, Player playerTurn, int player1Score, int player2Score) {
                String player1ScoreStr = "player 1: " + String.valueOf(player1Score);
                String player2ScoreStr = "player 2: " + String.valueOf(player2Score);
                scorePlayer1View.setText(player1ScoreStr);
                scorePlayer2View.setText(player2ScoreStr);
                String turn;
                if (playerTurn == Player.PLAYER_1) {
                    turn = "player 1's turn";
                } else {
                    turn = "player 2's turn";
                }
                turnView.setText(turn);
            }
        });

        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoard.resetGame();
                Toast.makeText(MainActivity.this, "New game ready", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
