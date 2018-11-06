package com.example.qlem.pairsgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlem.pairsgame.game.GameState;
import com.example.qlem.pairsgame.game.Player;

/**
 * This class defines the main activity that displays the game board view and refreshes the UI.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Variable that stores the game board view.
     */
    private GameBoardView gameBoardView;

    /**
     * Variable that stores the text view of the player 1's score.
     */
    private TextView scorePlayer1View;

    /**
     * Variable that stores the text view of the player 2's score.
     */
    private TextView scorePlayer2View;

    /**
     * Variable that stores the text view of the player's turn.
     */
    private TextView turnView;

    /**
     * Variable that stores the text view of the winner.
     */
    private TextView winnerView;

    /**
     * Function that refreshes the UI.
     * @param gameState the game state
     * @param playerTurn the player's turn
     * @param player1Score the score of player 1
     * @param player2Score the score of player 2
     */
    private void refreshUI(GameState gameState, Player playerTurn, int player1Score, int player2Score) {
        String player1ScoreStr = "player 1: " + String.valueOf(player1Score);
        String player2ScoreStr = "player 2: " + String.valueOf(player2Score);
        scorePlayer1View.setText(player1ScoreStr);
        scorePlayer2View.setText(player2ScoreStr);
        if (gameState == GameState.RUNNING) {
            if (playerTurn == Player.PLAYER_1) {
                turnView.setText(R.string.turn_pl1);
            } else {
                turnView.setText(R.string.turn_pl2);
            }
        } else if (gameState == GameState.FINISHED) {
            turnView.setVisibility(View.INVISIBLE);
            winnerView.setVisibility(View.VISIBLE);
            if (player1Score > player2Score) {
                winnerView.setText(R.string.winner_pl1);
            } else if (player1Score < player2Score) {
                winnerView.setText(R.string.winner_pl2);
            } else {
                winnerView.setText(R.string.draw);
            }
        }
    }

    /**
     * Function called at the creation of the main activity, initializes variables, game
     * board's data change listener for refresh the UI and restart button.
     * @param savedInstanceState saved instance state object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameBoardView = findViewById(R.id.game_board);
        scorePlayer1View = findViewById(R.id.score_player_1);
        scorePlayer2View = findViewById(R.id.score_player_2);
        turnView = findViewById(R.id.player_turn);
        winnerView = findViewById(R.id.winner);

        gameBoardView.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChangeListener(GameState gameState, Player playerTurn, int player1Score, int player2Score) {
                refreshUI(gameState, playerTurn, player1Score, player2Score);
            }
        });

        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnView.setVisibility(View.VISIBLE);
                winnerView.setVisibility(View.INVISIBLE);
                gameBoardView.resetGame();
                Toast.makeText(MainActivity.this, "New game ready", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
