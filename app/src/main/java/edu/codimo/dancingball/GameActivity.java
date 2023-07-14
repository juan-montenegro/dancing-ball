package edu.codimo.dancingball;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.graphics.Paint;
import android.graphics.Color;


import edu.codimo.dancingball.game.MazeView;
import edu.codimo.dancingball.storage.StorageHandler;


public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private static final String[] options = { "5x5 ", "8x8", "10x10", "5x10", "8x15", "10x20"};
    private StorageHandler storageHandler;
    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
    private MazeView mazeView;

    private SensorManager sensorManager;
    private Button stopButton;
    private Button startButton;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageHandler = new StorageHandler(this,getString(R.string.PREF_KEY));
        setContentView(R.layout.activity_main_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        BallView ballView = new BallView(this); // Crea una instancia de la vista de la pelota
//        setContentView(ballView); // Establece la vista de la pelota como contenido de la actividad
//
//        Point size = new Point();
//        Display display = getWindowManager().getDefaultDisplay();
//        display.getSize(size);
//        xMax = (float) size.x - 100; // Calcula el límite máximo en el eje X restando 100
//        yMax = (float) size.y - 100; // Calcula el límite máximo en el eje Y restando 100

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // Obtiene el manejador de sensores del sistema
        stopButton = findViewById(R.id.StopGameBtn);
        startButton = findViewById(R.id.StartGameBtn);
        mazeView = findViewById(R.id.MazeView);
        mazeView.post(this::statusListening);
        setButtonText();
    }

    private void setButtonText() {
        if (startButton.isEnabled()){
            stopButton.setText(R.string.settings_back_btn);
        } else {
            stopButton.setText(R.string.stop_game_btn_text);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Registra el listener del acelerómetro con el sensor manager, estableciendo una demora específica
        statusListening();
    }

    private void statusListening() {
        if (startButton.isEnabled()){
            sensorManager.unregisterListener(this);
        } else {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onStop() {
        // Desregistra el listener del acelerómetro del sensor manager
        statusListening();
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = sensorEvent.values[0]; // Asigna el valor de aceleración en el eje X
            yAccel = -sensorEvent.values[1]; // Asigna el valor de aceleración en el eje Y con una inversión
            mazeView.ball.updateBall(xAccel, yAccel);// Llama al método para actualizar la posición de la pelota
        }
    }
//
//    private void updateBall(float xAccel, float yAccel) {
//        float frameTime = 0.666f;
//        xVel += (xAccel * frameTime); // Actualizar la velocidad en el eje X según la aceleración
//        yVel += (yAccel * frameTime); // Actualizar la velocidad en el eje Y según la aceleración
//
//        float xS = (xVel / 2) * frameTime; // Calcular el desplazamiento en el eje X
//        float yS = (yVel / 2) * frameTime; // Calcular el desplazamiento en el eje Y
//
//        float newXPos = xPos - xS; // Calcular la nueva posición en el eje X
//        float newYPos = yPos - yS; // Calcular la nueva posición en el eje Y
//
//        // Rebotar en los bordes del eje X
//        if (newXPos > xMax || newXPos < 0) {
//            xVel = -xVel * 0.4f; // Invertir y reducir la velocidad en el eje X (factor de amortiguamiento: 0.8f)
//        }
//
//        // Rebotar en los bordes del eje Y
//        if (newYPos > yMax || newYPos < 0) {
//            yVel = -yVel * 0.4f; // Invertir y reducir la velocidad en el eje Y (factor de amortiguamiento: 0.8f)
//        }
//
//        xPos = xPos - xVel * frameTime; // Actualizar la posición en el eje X considerando la velocidad
//        yPos = yPos - yVel * frameTime; // Actualizar la posición en el eje Y considerando la velocidad
//    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Este método se llama cuando la precisión del sensor cambia.
    }

    public void onStartGameBtnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select maze size:");
        int cols = storageHandler.getInt(
                getResources().getString(R.string.cols_pref_key),5);
        int rows = storageHandler.getInt(
                getResources().getString(R.string.rows_pref_key),5);
        final Spinner spinner = new Spinner(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, options);
        spinner.setLayoutParams(params);
        spinner.setAdapter(adapter);

        builder.setView(spinner);

        spinner.setSelection(adapter.getPosition(cols + "x" + rows));
        builder.setPositiveButton(getString(R.string.start_game_btn_text), (dialog, id) -> {
            int selectedCols, selectedRows;
            // START THE GAME!
            String spinnerChoice = spinner.getSelectedItem().toString();
            String[] choice = spinnerChoice.trim().split("x");
            selectedCols = Integer.parseInt(choice[0]);
            selectedRows = Integer.parseInt(choice[1]);
            storageHandler.writePref(getString(R.string.rows_pref_key), selectedRows);
            storageHandler.writePref(getString(R.string.cols_pref_key), selectedCols);
            dialog.cancel();
            MazeView mazeView = findViewById(R.id.MazeView);
            startButton.setEnabled(false);
            setButtonText();
            mazeView.refreshMaze();
            mazeView.invalidate();
            statusListening();
        });
        builder.setNegativeButton(getString(R.string.cancel_dialog_text), (dialog, id) -> {
            dialog.dismiss();
            // User cancelled the dialog
            });
        builder.show();
    }


    public void onStopGameClickBtn(View view) {
        if (!startButton.isEnabled()){
            startButton.setEnabled(true);
            setButtonText();
            statusListening();
        } else {
            finish();
        }
    }
}