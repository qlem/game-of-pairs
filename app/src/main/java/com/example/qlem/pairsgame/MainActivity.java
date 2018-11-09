package com.example.qlem.pairsgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlem.pairsgame.game.GameData;
import com.example.qlem.pairsgame.game.GameState;
import com.example.qlem.pairsgame.game.Player;

/**
 * This class defines the main activity that displays the game board view and refreshes the UI.
 */
public class MainActivity extends AppCompatActivity {

    private Player playerTurn;
    private int scorePl1;
    private int scorePl2;
    private GameState gameState;

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
     */
    private void refreshUI() {
        String player1ScoreStr = "player 1: " + String.valueOf(scorePl1);
        String player2ScoreStr = "player 2: " + String.valueOf(scorePl2);
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
            if (scorePl1 > scorePl2) {
                winnerView.setText(R.string.winner_pl1);
            } else if (scorePl1 < scorePl2) {
                winnerView.setText(R.string.winner_pl2);
            } else {
                winnerView.setText(R.string.draw);
            }
        }
    }

    /**
     * Function called at the creation of the main activity, initializes the variables,
     * the event listener for the game data changes and the restart button.
     * @param savedInstanceState saved instance state object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("DEBUG", "CREATE");

        playerTurn = Player.PLAYER_1;
        scorePl1 = 0;
        scorePl2 = 0;
        gameState = GameState.RUNNING;

        gameBoardView = findViewById(R.id.game_board);
        scorePlayer1View = findViewById(R.id.score_player_1);
        scorePlayer2View = findViewById(R.id.score_player_2);
        turnView = findViewById(R.id.player_turn);
        winnerView = findViewById(R.id.winner);

        gameBoardView.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChangeListener(GameData gameData) {
                MainActivity.this.gameState = gameData.gameState;
                MainActivity.this.playerTurn = gameData.playerTurn;
                MainActivity.this.scorePl1 = gameData.scorePlayer1;
                MainActivity.this.scorePl2 = gameData.scorePlayer2;
                refreshUI();
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

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i("DEBUG", "SAVE MAIN ACTIVITY");
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("turn", playerTurn.name());
        savedInstanceState.putInt("scorePl1", scorePl1);
        savedInstanceState.putInt("scorePl2", scorePl2);
        savedInstanceState.putString("gameState", gameState.name());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("DEBUG", "RESTORE MAIN ACTIVITY");
        super.onRestoreInstanceState(savedInstanceState);
        playerTurn = Player.valueOf(savedInstanceState.getString("turn"));
        scorePl1 = savedInstanceState.getInt("scorePl1");
        scorePl2 = savedInstanceState.getInt("scorePl2");
        gameState = GameState.valueOf(savedInstanceState.getString("gameState"));
        refreshUI();
    }
}
