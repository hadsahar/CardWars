package com.example.cardwars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GameManager gameManager;
    private Button main_btn_draw_cards;
    private TextView main_lbl_player1Score;
    private TextView main_lbl_player2Score;
    private TextView main_lbl_cardCounterPlayer1;
    private TextView main_lbl_cardCounterPlayer2;
    private ImageView main_img_player1Card;
    private ImageView main_img_player2Card;
    private int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        gameManager = new GameManager();
        init();
        main_btn_draw_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt("" + main_lbl_cardCounterPlayer1.getText()) != 0) {
                    gameManager.DrawCard();
                    main_lbl_player1Score.setText("" +gameManager.getPlayer1().getPoints());
                    main_lbl_player2Score.setText("" +gameManager.getPlayer2().getPoints());
                    main_lbl_cardCounterPlayer1.setText("" +(Integer.parseInt("" +main_lbl_cardCounterPlayer1.getText()) - 1));
                    main_lbl_cardCounterPlayer2.setText("" + (Integer.parseInt("" +main_lbl_cardCounterPlayer2.getText()) - 1));
                    if(Integer.parseInt("" +main_lbl_cardCounterPlayer1.getText()) == 25 || Integer.parseInt("" +main_lbl_cardCounterPlayer2.getText()) == 25) {
                        main_img_player1Card.setVisibility(View.VISIBLE);
                        main_img_player2Card.setVisibility(View.VISIBLE);
                    }
                    main_img_player1Card.setImageResource(getCardFileInt(gameManager.getPlayer1().getCurrentCard()));
                    main_img_player2Card.setImageResource(getCardFileInt(gameManager.getPlayer2().getCurrentCard()));
                    if(Integer.parseInt("" + main_lbl_cardCounterPlayer1.getText()) == 0 || Integer.parseInt("" + main_lbl_cardCounterPlayer2.getText()) == 0){
                        main_btn_draw_cards.setText("Tap to Winner Page");
                        main_lbl_cardCounterPlayer1.setTextColor(Color.RED);
                        main_lbl_cardCounterPlayer2.setTextColor(Color.RED);
                        main_lbl_cardCounterPlayer1.setBackgroundResource(0);
                        main_lbl_cardCounterPlayer2.setBackgroundResource(0);
                    }
                }else {
                    Intent intent = new Intent(MainActivity.this, WinnerPageActivity.class);
                    intent.putExtra("WINNER_POINTS", gameManager.checkGameWinner() == 1 ? gameManager.getPlayer1().getPoints() : gameManager.getPlayer2().getPoints());
                    intent.putExtra("WINNER_PLAYER",gameManager.checkGameWinner() == 1? 1:2);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void init() {
        main_btn_draw_cards = findViewById(R.id.main_btn_draw);
        main_lbl_player1Score = findViewById(R.id.main_lbl_player1Score);
        main_lbl_player2Score = findViewById(R.id.main_lbl_player2Score);
        main_lbl_cardCounterPlayer1 = findViewById(R.id.main_lbl_cardCounterPlayer1);
        main_lbl_cardCounterPlayer2 = findViewById(R.id.main_lbl_cardCounterPlayer2);
        main_img_player1Card = findViewById(R.id.main_img_player1Card);
        main_img_player2Card = findViewById(R.id.main_img_player2Card);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public int getCardFileInt(Card card){
        Resources res = getResources();
        int sign = card.getSign();
        String cSign ="";
        switch (sign){
            case 1: {
                cSign = "d";
                break;
            }
            case 2: {
                cSign = "h";
                break;
            }
            case 3: {
                cSign = "s";
                break;
            }
            case 4: {
                cSign = "c";
                break;
            }
            default: break;
        }
        String result = "card" + card.getValue()+cSign;
        return  res.getIdentifier(result , "drawable", getPackageName());
    }
}