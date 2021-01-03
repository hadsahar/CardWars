package com.example.cardwars;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    private Button btn_new_game;
    private Button btn_highscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
        clicks();
    }

    // Approve only 1 click. when clicked, set all buttons as not clickable.
    // Calls the intended Activity for the appropriate button
    // and waits for a result from the onResultActivity function.
    private void clicks() {
        btn_new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_new_game.setClickable(false);
                btn_highscores.setClickable(false);
                startActivityForResult(new Intent(MainMenuActivity.this, GameActivity.class), 126);
            }
        });
        btn_highscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_new_game.setClickable(false);
                btn_highscores.setClickable(false);
                startActivityForResult(new Intent(MainMenuActivity.this, HighScoresActivity.class), 125);
            }
        });
    }

    // When this activity returns to the screen - enable clickable to all buttons.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btn_new_game.setClickable(true);
        btn_highscores.setClickable(true);
    }

    private void init() {
        btn_new_game = findViewById(R.id.btn_New_Game);
        btn_highscores = findViewById(R.id.btn_HighScores);
    }
}