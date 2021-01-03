package com.example.cardwars;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import java.util.ArrayList;

public class HighScoresActivity extends AppCompatActivity {
    private final LatLng DEFAULT_POINT = new LatLng(32.185710746577215, 34.86942052928289);
    private TopTen highScoreList;
    private Button returnBTN;
    private Fragment_List fragment_list;
    private Fragment_Map fragment_map;
    private LatLng firstPoint;
    private CallBack_top callBack_Top;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        init();
        activityManager();
        clicks();
    }

    // This function retrieves the location of the best highscorer. If there are no highscorers,
    // it will set the first location on the map as the default location (32.185710746577215, 34.86942052928289) (Tel-Aviv)
    private void getFirstPoint() {
        if(highScoreList.getHighscores().size()!= 0) {
            firstPoint = new LatLng(highScoreList.getHighscores().get(0).getLat(), highScoreList.getHighscores().get(0).getLon());
        }else{
            firstPoint = DEFAULT_POINT;
        }
    }

    private void activityManager(){
        getSupportFragmentManager().beginTransaction().add(R.id.highScores_LAY_list,fragment_list).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.highScores_LAY_map,fragment_map).commit();
    }


    private void init(){
        readFromPref(); // reads the highscore list from the shared preferences.
        getFirstPoint();
        returnBTN = findViewById(R.id.highscores_btn_back);
        fragment_list = new Fragment_List(highScoreList.getHighscores());
        fragment_map = new Fragment_Map(firstPoint);
        callBack_Top = new CallBack_top(){
            @Override
            public void displayLocationInMap(double lat, double lon) {
                fragment_map.updateMap(new LatLng(lat, lon));
            }
        };
        fragment_list.setCallBack_top(callBack_Top);
    }

    // reads the highscore list from the shared preferences.
    private void readFromPref(){
        SharedPreferences pref = getSharedPreferences(getString(R.string.HIGHSCORE_KEY),MODE_PRIVATE);
        String fromPref = pref.getString(getString(R.string.TOP_TEN_KEY), "NA");
        if (fromPref.equals(getString(R.string.NA))||fromPref.equals(getString(R.string.emptyArray))) {
            highScoreList = new TopTen(new ArrayList<Player_Winner>());
        }else{
            highScoreList = new Gson().fromJson(fromPref, TopTen.class);
        }
    }

    // A function that finishes this activity.
    private void clicks(){
        returnBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}