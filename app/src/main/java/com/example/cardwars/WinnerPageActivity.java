package com.example.cardwars;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import java.util.ArrayList;


public class WinnerPageActivity extends AppCompatActivity {
    private static final int MAX_HIGHSCORES = 10;
    private static final int TIMES_TO_SHOW_LOCATION_DIALOG = 3;
    private String winnerName;
    private Button winner_page_btn_main_menu;
    private TextView winner_page_points;
    private ImageView winner_page_img_winner;
    private int winner_player;
    private TopTen highScoreList;
    private int winner_points;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double lat;
    private double lon;
    private int settingsCounter;
    private boolean isPermissionDialogAppeared;
    private Task<Location> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_page);
        init();
        winner_page_points.setText(getString(R.string.winner_txt,winner_points));
        if (winner_player == 1) {
            winner_page_img_winner.setImageResource(R.drawable.img_bob_minion);
        } else {
            winner_page_img_winner.setImageResource(R.drawable.img_minion);
        }

        // When clicked - finish this activity.
        winner_page_btn_main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("highscores", "onCreate exit button: Array size = "+ highScoreList.getHighscores().size());
                finish();
            }
        });
    }

    private void init() {
        //writeToPref(TOP_TEN_KEY, new ArrayList<Player_Winner>()); // - use this to clear the highscorers array list.
        isPermissionDialogAppeared = false; // A flag to indicate if the permission dialog appeared
        readFromPref(getString(R.string.LOCATION_DENY)); // reading the counter of the number of times that we manually opened the settings for permission.
        readFromPref(getString(R.string.TOP_TEN_KEY)); // reading the highscorers array list.
        winnerName = ""; // the winner name will be used if a high score is achieved.
        winner_points = getIntent().getIntExtra(getString(R.string.winner_points), 0); // get the points of the current match's winner.
        winner_player = getIntent().getIntExtra(getString(R.string.winner_player), 0); // get the player who won the current match.
        winner_page_btn_main_menu = findViewById(R.id.winner_page_btn_main_Menu);
        winner_page_img_winner = findViewById(R.id.winner_page_img_winner);
        winner_page_points = findViewById(R.id.winner_page_points);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); // Init the location fused client.
        checkLocationPermission(); // Check for location permission.

        if(!checkForHighScore()){ // play sound "You Win" if there was no high score.
            MediaPlayer mp = MediaPlayer.create(this,R.raw.you_win);
            mp.start();
        }
    }

    // Check if a new high score was achieved. a new high score can be achieved if the high score
    // list has less than 10 members, or if the last game result is better than the last member of
    // the high score list.
    // if a high score is achieved - play the high score sound.
    private boolean checkForHighScore() {
        MediaPlayer mp = MediaPlayer.create(this,R.raw.highscore_sound);
        if (highScoreList.getHighscores().size() < MAX_HIGHSCORES) {
            mp.start();
            showHighScoreInput(false);
            return true;
        } else
            if (winner_points > highScoreList.getHighscores().get(MAX_HIGHSCORES-1).getMax_score()) {
                mp.start();
                showHighScoreInput(true);
                return true;
        }
            return false;
    }

    // create an alert dialog that asks the user for his/her name.
    // and get the location of the phone(if available)
    private void showHighScoreInput(boolean isNeedDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.highscore_msg));
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("LOACTIONS", "onClick: this is my time");
                if (Strings.isEmptyOrWhitespace(input.getText().toString())) { // in case no name was entered
                    winnerName = getString(R.string.unknown_player);
                } else {
                    winnerName = input.getText().toString();
                }
                if (isNeedDelete) { // check if we need to delete the last high scorer before we enter the new one instead.
                    highScoreList.getHighscores().remove(MAX_HIGHSCORES-1);
                }
                Log.d("pttt", "onClick: winnerName =" + winnerName);
                if (task != null && !task.isSuccessful()) { // checks if we succeded with getting the phone location
                    Log.d("LOACTIONS", "task is not successful");
                    setLatlon(false, null);
                }
                highScoreList.getHighscores().add(new Player_Winner(winnerName, winner_points,
                        lat, lon)); // add the high scorer to the array list.
                highScoreList.getHighscores().sort(new CompareByPoints()); // sorts the highs core list by points.
                writeToPref(getString(R.string.TOP_TEN_KEY), highScoreList);
            }
        }).setNegativeButton(getString(R.string.do_not_save), new DialogInterface.OnClickListener() { // don't save the highscorer.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // This function read data from shared preferences. (data in JSON format)
    // The file that stores the data is always HIGHSCORE and the data that we want to get
    // is a variable that this function gets
    private void readFromPref(String key) {
        SharedPreferences pref = getSharedPreferences(getString(R.string.HIGHSCORE_KEY), MODE_PRIVATE);
        String fromPref = pref.getString(key, getString(R.string.NA));
        if (key.equals(getString(R.string.TOP_TEN_KEY))) {
            if (fromPref.equals(getString(R.string.NA)) || fromPref.equals(getString(R.string.emptyArray))) {
                highScoreList = new TopTen(new ArrayList<Player_Winner>());
            } else {
                highScoreList = new Gson().fromJson(fromPref, TopTen.class);
            }
        } else {
            if (key.equals(getString(R.string.LOCATION_DENY))) {
                if (fromPref.equals(getString(R.string.NA))) {
                    settingsCounter = 0;
                } else {
                    settingsCounter = new Gson().fromJson(fromPref, Integer.class);
                }
            }
        }
    }

    // This function writes to shared preferance file named "HIGHSCORE".
    // The saved data will be written in JSON format.
    // This function gets an identifier key for the value we want to save to it.
    private void writeToPref(String key, Object value) {
        SharedPreferences pref = getSharedPreferences(getString(R.string.HIGHSCORE_KEY), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String toPref = new Gson().toJson(value);
        editor.putString(key, toPref);
        editor.apply();
    }

    // This function sets the location by the permission status (permitted or denied) and if the
    // location is available. If true it saves the location, else it will set a default location as (0,0)
    private void setLatlon(boolean permissionStatus, Location location) {
        lat = (permissionStatus && location != null)? location.getLatitude() : 0;
        lon = (permissionStatus && location != null)? location.getLongitude() : 0;
    }

    /*------------------------------- Location Permissions ---------------------------------------*/

    // Return's true when both ACCESS_COARSE_LOCATION & ACCESS_FINE_LOCATION
    // permissions are denied, and returns false if at least one of them is granted.
    private boolean checkSelfPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED;
    }

    // This function checks if the location permission is granted and handles the permission request
    // if it is not permitted.
    private void checkLocationPermission() {
        if (checkSelfPermissions()) {
            Log.d("pttt", "No Permission granted");
            // No permission granted. we will now check if we are
            // able to send the user a request for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // We are able to send the request, thus we send a request
                Log.d("pttt", "shouldShowRequestPermissionRationale");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            } else {
                openSettingsHelper();
            }
        } else {
            // Permission is granted. We can initialize location services
            initLocationServices();
        }
    }

    // This function checks once more if the permission for location is granted
    // If it is granted, we will set the location settings and save the latitude and longitude.
    private void initLocationServices() {
        if (checkSelfPermissions()) {
            checkLocationPermission();
            return;
        }

        if (!checkSelfPermissions()) {
            task = fusedLocationProviderClient.getLastLocation();
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setSmallestDisplacement(1.0f);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d("LOCATIONS!", "init: " + location.getLatitude());
                Log.d("LOCATIONS!", "init: " + location.getLongitude());
                Log.d("pttt", "Fetching current location");
                setLatlon(true, location);
            }
        });
    }

    // This function will inform the user that the location setting is recommended.
    private void openLocationPermissionDialog() {
        String message = getString(R.string.msg_location_recommendation);
        AlertDialog alertDialog = new AlertDialog.Builder(this, 0)
                .setMessage(message)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkLocationPermission();
                            }
                        })
                .show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    // This function will inform the user that the location setting is recommended.
    // Than it  will switch him to the settings menu in his android phone where he will be able to
    // permit the permission.
    // Every time this function is called we add a counter to ensure that this function will not be
    // called more than 3 times.
    private void openSettingsToManuallyPermission() {
        writeToPref(getString(R.string.LOCATION_DENY), ++settingsCounter);
        String message = getString(R.string.msg_location_recommendation_dont_show_again,(TIMES_TO_SHOW_LOCATION_DIALOG-settingsCounter));
        AlertDialog alertDialog = new AlertDialog.Builder(this, 0)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 126);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    // This function will inform the application when the user returns to it after
    // being sent to the setting menu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("pttt", "onActivityResult");

        if (requestCode == 126) {
            // we've returned. now we will check if the location permission is permitted.
            checkLocationPermission();
        }
    }

    // This function manages the if and how many times to open the settings for the location
    // permission request.
    public void openSettingsHelper(){
        // We are unable to send the request,
        // hence we will ask the user to permit it (and open the specific permission
        // page in the settings app), but this will not occur more than 3 times (for UX,
        // this will shown up to 3 times once the app is installed without resetting the
        // counter.)
        if (settingsCounter < TIMES_TO_SHOW_LOCATION_DIALOG) {
            openSettingsToManuallyPermission();
        } else {
            // once we cannot interrupt the user with the settings menu, we will make a
            // toast message that alerts the user that the default location will be applied
            Toast.makeText(this, getString(R.string.GPS_error_toast_msg), Toast.LENGTH_LONG).show();
            setLatlon(false,null);
        }
        Log.d("pttt", "No shouldShowRequestPermissionRationale");
    }

    // This function is called when the user decides if to approve or deny the location services.
    // if the permission was permitted we will initialize location services
    // else, we will handle the denial
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("pttt", "onRequestPermissionsResult");
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. We can initialize location services
                    Log.d("pttt", "PERMISSION_GRANTED");
                    initLocationServices();
                } else {
                    Log.d("pttt", "PERMISSION_DENIED");

                    // No permission granted. we will now check if we are
                    // able to send the user a request for permission
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Permission was denied. we will show this dialog only once asking the
                        // user to permit the location permission
                        if(!isPermissionDialogAppeared) {
                            isPermissionDialogAppeared = true;
                            openLocationPermissionDialog();
                        }else{
                            // We have shown this window once. we don't want to interrupt the UX so
                            // we will alert the user that the location permission is denied and
                            // default location will be applied.
                            Toast.makeText(this, getString(R.string.GPS_error_toast_msg), Toast.LENGTH_LONG).show();
                            setLatlon(false,null);
                        }
                    } else {
                        openSettingsHelper();
                    }
                }
            }
        }
    }
}