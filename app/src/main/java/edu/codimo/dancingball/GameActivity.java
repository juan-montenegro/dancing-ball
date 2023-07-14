package edu.codimo.dancingball;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.View;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.SensorEventListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;



import edu.codimo.dancingball.game.MazeView;
import edu.codimo.dancingball.storage.StorageHandler;


public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private static final String[] options = { "5x5 ", "8x8", "10x10", "5x10", "8x15", "10x20"};
    private StorageHandler storageHandler;
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
        statusListening();
    }

    @Override
    protected void onStop() {
        statusListening();
        super.onStop();
    }


    private void statusListening() {
        if (startButton.isEnabled()){
            // Desregistra el listener del acelerómetro del sensor manager
            sensorManager.unregisterListener(this);
        } else {
            // Registra el listener del acelerómetro con el sensor manager, estableciendo una demora específica
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xAccel = sensorEvent.values[0]; // Asigna el valor de aceleración en el eje X
            float yAccel = -sensorEvent.values[1]; // Asigna el valor de aceleración en el eje Y con una inversión
            mazeView.updateBall(xAccel, yAccel);// Llama al método para actualizar la posición de la pelota
        }
    }

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
            MazeView mazeView = findViewById(R.id.MazeView);
            mazeView.setBallToStart();
            mazeView.invalidate();
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