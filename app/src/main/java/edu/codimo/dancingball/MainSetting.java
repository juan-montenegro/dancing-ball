package edu.codimo.dancingball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import edu.codimo.dancingball.storage.StorageHandler;

public class MainSetting extends AppCompatActivity {
    StorageHandler storageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);
        storageHandler = new StorageHandler(this,getString(R.string.PREF_KEY));
    }

    public void backToMainSetting(View view) {
        finish();
    }

    public void onGreenBallClick(View view) {
        storageHandler.writePref(getString(R.string.ball_pref_key), "ball_green");
    }

    public void onBlueBallClick(View view) {
        storageHandler.writePref(getString(R.string.ball_pref_key), "ball_blue");
    }

    public void onRedBallClick(View view) {
        storageHandler.writePref(getString(R.string.ball_pref_key), "ball_red");
    }
}