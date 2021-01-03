package com.example.cardwars;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private final int DELAY = 2000;
    private GameManager gameManager;
    private Button main_btn_draw_cards;
    private Button main_btn_auto_draw;
    private TextView main_lbl_player1Score;
    private TextView main_lbl_player2Score;
    private TextView main_lbl_cardCounterPlayer1;
    private TextView main_lbl_cardCounterPlayer2;
    private ImageView main_img_player1Card;
    private ImageView main_img_player2Card;
    private ProgressBar main_progressBar;
    private Timer carousalTimer;
    private boolean isTimerOn;
    private boolean isTimerCanceled;
    private MediaPlayer mp1;
    private MediaPlayer mp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
        clicks();
    }

    // A function that sets a timer to indicate when the user wants to auto draw.
    // This function will draw a new card every DELAY milliseconds while the isTimerOn flag is on
    private void startDrawing(){
        if (isTimerCanceled) {
            carousalTimer = new Timer();
            isTimerCanceled = false;
        }
        isTimerOn = true;
        carousalTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread((new Runnable() {
                    @Override
                    public void run() {
                        drawCard();
                    }
                }));
            }
        }, 0, DELAY);
    }

    // A function that sets the isTimerOn flag off which indicate when the user wants
    // to stop the auto draw or when the system need to stop auto drawing.
    private void stopDrawing(boolean flag){
        if (carousalTimer != null) {
            carousalTimer.cancel();
            isTimerCanceled = true;
        }
        if (flag) {
            isTimerOn = false;
        }
    }

    // checks if the auto draw needs to be active, if so - auto draws.
    @Override
    protected void onResume() {
        super.onResume();
        if (isTimerOn){
            startDrawing();
        }
    }

    // checks if the auto draw needs to be active when resumed.
    @Override
    protected void onPause() {
        super.onPause();
        if(isTimerOn) {
            stopDrawing(false);
        }
    }

    // checks if the auto draw needs to be active when resumed.
    @Override
    protected void onStop() {
        super.onStop();
        if(isTimerOn) {
            stopDrawing(false);
        }
    }

    // This function is used to return the "ID" of the image file of the current card by its name.
    public int getCardFileInt(Card card){
        Resources res = getResources();
        int sign = card.getSign();
        String cSign ="";
        switch (sign){
            case 1: {
                cSign = getString(R.string.diamond);
                break;
            }
            case 2: {
                cSign = getString(R.string.heart);
                break;
            }
            case 3: {
                cSign = getString(R.string.spade);
                break;
            }
            case 4: {
                cSign = getString(R.string.club);
                break;
            }
            default: break;
        }
        String result = "card" + card.getValue()+cSign;
        return  res.getIdentifier(result , "drawable", getPackageName());
    }

    private void init() {
        isTimerOn = false;
        isTimerCanceled = true;
        gameManager = new GameManager(new Player(),new Player());
        main_btn_draw_cards = findViewById(R.id.main_btn_draw);
        main_btn_auto_draw = findViewById(R.id.main_btn_auto_draw);
        main_lbl_player1Score = findViewById(R.id.main_lbl_player1Score);
        main_lbl_player2Score = findViewById(R.id.main_lbl_player2Score);
        main_lbl_cardCounterPlayer1 = findViewById(R.id.main_lbl_cardCounterPlayer1);
        main_lbl_cardCounterPlayer2 = findViewById(R.id.main_lbl_cardCounterPlayer2);
        main_img_player1Card = findViewById(R.id.main_img_player1Card);
        main_img_player2Card = findViewById(R.id.main_img_player2Card);
        main_progressBar = findViewById(R.id.progressBar);
        main_progressBar.setProgress(0);
    }

    private void clicks(){
        // When clicked a card will be drawn, if all cards are drawn, it will open the winner page.
        main_btn_draw_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawCard()) {
                    openWinnerPage();
                }
            }
        });

        // When clicked it will start the auto draw process. when clicked again, it will stop the auto draw process
        main_btn_auto_draw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!isTimerOn) {
                    startDrawing();
                    main_btn_draw_cards.setEnabled(false);
                    main_btn_auto_draw.setText(R.string.manual_drawing);
                }else{
                    stopDrawing(true);
                    main_btn_draw_cards.setEnabled(true);
                    main_btn_auto_draw.setText(R.string.auto_draw);
                }
            }
        });
    }

    // Call the WinnerPageActivity and send the relevant information
    private void openWinnerPage(){
        Intent intent = new Intent(GameActivity.this, WinnerPageActivity.class);
        intent.putExtra(getString(R.string.winner_points), gameManager.checkGameWinner() == 1 ? gameManager.getPlayer1().getPoints() : gameManager.getPlayer2().getPoints());
        intent.putExtra(getString(R.string.winner_player),gameManager.checkGameWinner() == 1? 1:2);
        startActivity(intent);
        finish();
    }

    // This function get a Media Player object and plays it.
    private void playSound(MediaPlayer mp, int rawId) {
        if(mp != null && mp.isPlaying()){
            mp.reset();
            mp.release();
            mp = null;
        }

        mp = MediaPlayer.create(this ,rawId);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }
        });
        mp.start();
    }

    //Return true if a card was drawn. Return false otherwise. changes the view accordingly.
    private boolean drawCard(){
        if(Integer.parseInt("" + main_lbl_cardCounterPlayer1.getText()) != 0) {
            playSound(mp1,R.raw.card_draw);//1st player card draw sound
            playSound(mp2,R.raw.card_draw);//2nd player card draw sound
            gameManager.DrawCard(); // call the drawCard function
            main_progressBar.setProgress(main_progressBar.getProgress() + 1); // update the progressBar
            main_lbl_player1Score.setText(getString(R.string.player_points,gameManager.getPlayer1().getPoints())); // set the player score.
            main_lbl_player2Score.setText(getString(R.string.player_points,gameManager.getPlayer2().getPoints())); // set the player score.
            main_lbl_cardCounterPlayer1.setText(getString(R.string.card_counters,Integer.parseInt("" + main_lbl_cardCounterPlayer1.getText()) - 1)); // set the player number of cards in the deck.
            main_lbl_cardCounterPlayer2.setText(getString(R.string.card_counters,Integer.parseInt("" + main_lbl_cardCounterPlayer2.getText()) - 1)); // set the player number of cards in the deck.

            // Check if there are cards in the deck - if not - set the image visibility to invisible
            if (Integer.parseInt("" + main_lbl_cardCounterPlayer1.getText()) == 25 || Integer.parseInt("" + main_lbl_cardCounterPlayer2.getText()) == 25) {
                main_img_player1Card.setVisibility(View.VISIBLE);
                main_img_player2Card.setVisibility(View.VISIBLE);
            }
            main_img_player1Card.setImageResource(getCardFileInt(gameManager.getPlayer1().getCurrentCard())); // show the current card of player 1
            main_img_player2Card.setImageResource(getCardFileInt(gameManager.getPlayer2().getCurrentCard())); // show the current card of player 2

            // Check if there are no more cards to draw. if so - end the game and update this activity view.
            if (Integer.parseInt("" + main_lbl_cardCounterPlayer1.getText()) == 0 || Integer.parseInt("" + main_lbl_cardCounterPlayer2.getText()) == 0) {
                if(isTimerOn) {
                    stopDrawing(true); // stop the auto draw
                }
                main_btn_auto_draw.setEnabled(false);
                main_btn_auto_draw.setVisibility(View.GONE);
                main_btn_draw_cards.setText(R.string.tap_to_winner_page);
                main_btn_draw_cards.setEnabled(true);
                main_lbl_cardCounterPlayer1.setTextColor(Color.RED);
                main_lbl_cardCounterPlayer2.setTextColor(Color.RED);
                main_lbl_cardCounterPlayer1.setBackgroundResource(0);
                main_lbl_cardCounterPlayer2.setBackgroundResource(0);
            }
            return true;
        }else {
            return false;
        }
    }
}