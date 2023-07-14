package edu.codimo.dancingball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.codimo.dancingball.storage.StorageHandler;

public class SettingsActivity extends AppCompatActivity {
    private StorageHandler storageHandler;
    private Button ballRedBtn;
    private Button ballBlueBtn;
    private Button ballGreenBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);
        storageHandler = new StorageHandler(this,getString(R.string.PREF_KEY));
        ballBlueBtn = findViewById(R.id.ButtonBallBlue);
        ballGreenBtn = findViewById(R.id.ButtonBallGreen);
        ballRedBtn = findViewById(R.id.ButtonBallRed);
        getBallPref();
    }

    public void backToMainSetting(View view) {
        finish();
    }

    public void goToGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onGreenBallClick(View view) {
        storageHandler.writePref(getString(R.string.ball_pref_key), "ball_green");
        getBallPref();
    }

    public void onBlueBallClick(View view) {
        storageHandler.writePref(getString(R.string.ball_pref_key), "ball_blue");
        getBallPref();
    }

    public void onRedBallClick(View view) {
        storageHandler.writePref(getString(R.string.ball_pref_key), "ball_red");
        getBallPref();
    }
    public void getBallPref(){
        Drawable redBallBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ball_red, null);
        Drawable blueBallBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ball_blue, null);
        Drawable greenBallBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ball_green, null);
        String ballPref = storageHandler.getString(getString(R.string.ball_pref_key));
        if (ballPref.equals("ball_blue")){
            blueBallBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ball_blue_selected, null);
        } else if (ballPref.equals("ball_green")) {
            greenBallBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ball_green_selected, null);
        } else  {
            redBallBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ball_red_selected, null);
        }
        ballBlueBtn.setForeground(blueBallBtnDrawable);
        ballGreenBtn.setForeground(greenBallBtnDrawable);
        ballRedBtn.setForeground(redBallBtnDrawable);
    }

}