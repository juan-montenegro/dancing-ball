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
import android.graphics.Paint;
import android.graphics.Color;


import edu.codimo.dancingball.storage.StorageHandler;


public class MainGame extends AppCompatActivity implements SensorEventListener {
    StorageHandler storageHandler; // Manejador de almacenamiento para el juego
    private float xPos, xAccel, xVel = 0.0f; // Posición, aceleración y velocidad en el eje X
    private float yPos, yAccel, yVel = 0.0f; // Posición, aceleración y velocidad en el eje Y
    private float xMax, yMax; // Límites máximos en los ejes X e Y
    private Bitmap ball; // Imagen de la pelota
    private SensorManager sensorManager; // Manejador de sensores para capturar los datos del acelerómetro

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageHandler = new StorageHandler(this,getString(R.string.PREF_KEY)); // Inicializa el manejador de almacenamiento con el contexto actual y una clave de preferencia
        setContentView(R.layout.activity_maingame); // Establece el diseño de la actividad principal
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Establece la orientación de la pantalla en modo retrato

        BallView ballView = new BallView(this); // Crea una instancia de la vista de la pelota
        setContentView(ballView); // Establece la vista de la pelota como contenido de la actividad

        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        xMax = (float) size.x - 100; // Calcula el límite máximo en el eje X restando 100
        yMax = (float) size.y - 100; // Calcula el límite máximo en el eje Y restando 100

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // Obtiene el manejador de sensores del sistema
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Registra el listener del acelerómetro con el sensor manager, estableciendo una demora específica
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        // Desregistra el listener del acelerómetro del sensor manager
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = sensorEvent.values[0]; // Asigna el valor de aceleración en el eje X
            yAccel = -sensorEvent.values[1]; // Asigna el valor de aceleración en el eje Y con una inversión

            updateBall(); // Llama al método para actualizar la posición de la pelota
        }
    }


    private void updateBall() {
        float frameTime = 0.666f;
        xVel += (xAccel * frameTime); // Actualizar la velocidad en el eje X según la aceleración
        yVel += (yAccel * frameTime); // Actualizar la velocidad en el eje Y según la aceleración

        float xS = (xVel / 2) * frameTime; // Calcular el desplazamiento en el eje X
        float yS = (yVel / 2) * frameTime; // Calcular el desplazamiento en el eje Y

        float newXPos = xPos - xS; // Calcular la nueva posición en el eje X
        float newYPos = yPos - yS; // Calcular la nueva posición en el eje Y

        // Rebotar en los bordes del eje X
        if (newXPos > xMax || newXPos < 0) {
            xVel = -xVel * 0.4f; // Invertir y reducir la velocidad en el eje X (factor de amortiguamiento: 0.8f)
        }

        // Rebotar en los bordes del eje Y
        if (newYPos > yMax || newYPos < 0) {
            yVel = -yVel * 0.4f; // Invertir y reducir la velocidad en el eje Y (factor de amortiguamiento: 0.8f)
        }

        xPos = xPos - xVel * frameTime; // Actualizar la posición en el eje X considerando la velocidad
        yPos = yPos - yVel * frameTime; // Actualizar la posición en el eje Y considerando la velocidad
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Este método se llama cuando la precisión del sensor cambia.
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
            // Dibujar la imagen de la bola en las coordenadas xPos y yPos en el canvas
            canvas.drawBitmap(ball, xPos, yPos, null);

            // Obtener las dimensiones del canvas
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();

            // Dibujar las líneas en la parte inferior del canvas
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(20);

            int lineWidth = 200; // Grosor de las líneas

            int topLineY = (canvasHeight / 2) - (lineWidth / 2); // Coordenada Y de la línea superior
            int bottomLineY1 = ((3 * canvasHeight) / 13) - (lineWidth / 4); // Coordenada Y de la primera línea inferior
            int bottomLineY2 = bottomLineY1 + (lineWidth * 5); // Coordenada Y de la segunda línea inferior
            int topLineY2 = bottomLineY1 + (lineWidth * 7);

            int segmentStartX = 0; // Coordenada X inicial del primer segmento
            int segmentEndX = (3 * canvasWidth) / 4; // Coordenada X final del primer segmento

            int segment2StartX = canvasWidth; // Coordenada X inicial del segundo segmento
            int segment2EndX = canvasWidth / 4; // Coordenada X final del segundo segmento

            // Dibujar el primer segmento en la línea superior
            canvas.drawLine(segmentStartX, topLineY, segmentEndX, topLineY, paint);

            // Dibujar el segundo segmento en la primera línea inferior
            canvas.drawLine(segment2StartX, bottomLineY1, segment2EndX, bottomLineY1, paint);

            // Dibujar el tercer segmento en la segunda línea inferior
            canvas.drawLine(segment2StartX, bottomLineY2, segment2EndX, bottomLineY2, paint);

            // Dibujar el cuarto segmento en la línea inferior
            canvas.drawLine(segmentStartX, topLineY2, segmentEndX, topLineY2, paint);

            // Redibujar la vista
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