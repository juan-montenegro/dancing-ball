package edu.codimo.dancingball;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.view.Display;
import android.hardware.SensorEventListener;
import android.graphics.BitmapFactory;

import edu.codimo.dancingball.storage.StorageHandler;


public class MainGame extends AppCompatActivity implements SensorEventListener {
    StorageHandler storageHandler;
    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
    private float xMax, yMax;
    private Bitmap ball;
    private SensorManager sensorManager;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageHandler = new StorageHandler(this,getString(R.string.PREF_KEY));
        setContentView(R.layout.activity_maingame);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BallView ballView = new BallView(this);
        setContentView(ballView);

        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        xMax = (float) size.x - 100;
        yMax = (float) size.y - 100;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = sensorEvent.values[0];
            yAccel = -sensorEvent.values[1];
            updateBall();
        }
    }

    private void updateBall() {
        float frameTime = 0.666f;
        xVel += (xAccel * frameTime);
        yVel += (yAccel * frameTime);

        float xS = (xVel / 2) * frameTime;
        float yS = (yVel / 2) * frameTime;

        float newXPos = xPos - xS;
        float newYPos = yPos - yS;

        // Rebotar en los bordes del eje X
        if (newXPos > xMax || newXPos < 0) {
            xVel = -xVel * 0.8f; // Invertir y reducir la velocidad en el eje X (factor de amortiguamiento: 0.8f)
        }

        // Rebotar en los bordes del eje Y
        if (newYPos > yMax || newYPos < 0) {
            yVel = -yVel * 0.8f; // Invertir y reducir la velocidad en el eje Y (factor de amortiguamiento: 0.8f)
        }

        xPos = xPos - xVel * frameTime; // Actualizar la posición en el eje X considerando la velocidad
        yPos = yPos - yVel * frameTime; // Actualizar la posición en el eje Y considerando la velocidad
    }


        @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class BallView extends View {

        public BallView(Context context) {
            super(context);
            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), getBallPref());
            final int dstWidth = 100;
            final int dstHeight = 100;
            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(ball, xPos, yPos, null);
            invalidate();
        }
        public int getBallPref(){
            String ballPref = storageHandler.getString(getString(R.string.ball_pref_key));
            switch (ballPref) {
                case "ball_blue":
                    return R.drawable.ball_blue;
                case "ball_green":
                    return R.drawable.ball_green;
                default:
                    return R.drawable.ball_red;
            }
        }
    }
}