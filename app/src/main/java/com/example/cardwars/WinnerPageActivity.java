package com.example.cardwars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WinnerPageActivity extends AppCompatActivity {
    private Button winner_page_btn_new_game;
    private TextView winner_page_points;
    private ImageView winner_page_img_winner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_page);
        init();
        int winner_points = getIntent().getIntExtra("WINNER_POINTS", 0);
        int winner_player = getIntent().getIntExtra("WINNER_PLAYER", 0);
        winner_page_points.setText(winner_points + " points!");
        if(winner_player == 1){
            winner_page_img_winner.setImageResource(R.drawable.img_bob_minion);
        }else{
            winner_page_img_winner.setImageResource(R.drawable.img_minion);
        }
        winner_page_btn_new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WinnerPageActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void init() {
        winner_page_btn_new_game = findViewById(R.id.winner_page_btn_new_game);
        winner_page_img_winner = findViewById(R.id.winner_page_img_winner);
        winner_page_points = findViewById(R.id.winner_page_points);
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
}