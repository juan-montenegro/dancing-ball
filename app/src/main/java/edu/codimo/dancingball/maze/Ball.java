package edu.codimo.dancingball.maze;

import android.graphics.Rect;

import edu.codimo.dancingball.R;

public class Ball {
    private float xPos, xVel = 0.0f;
    private float yPos, yVel = 0.0f;
    private float xMax, yMax;
    private float xMin = 0, yMin = 0;
    private static final float rho = 0.2f;
    private static final float frameTime = 0.5f;
    private Rect bounds;
    public Ball(float xMax, float yMax){
        setMaxSize(xMax,yMax);
    }

    public void setMaxSize(float xMax, float yMax){
        this.xMax = xMax;
        this.yMax = yMax;
    }
    public void setMinSize(float xMin, float yMin){
        this.xMin = xMin;
        this.yMin = yMin;
    }

    public void updateBall(float xAccel, float yAccel) {
        xVel += (xAccel * frameTime); // Actualizar la velocidad en el eje X según la aceleración
        yVel += (yAccel * frameTime); // Actualizar la velocidad en el eje Y según la aceleración

        float xS = (xVel / 2) * frameTime; // Calcular el desplazamiento en el eje X
        float yS = (yVel / 2) * frameTime; // Calcular el desplazamiento en el eje Y

        float newXPos = xPos - xS; // Calcular la nueva posición en el eje X
        float newYPos = yPos - yS; // Calcular la nueva posición en el eje Y
//        xMax = xMax - bounds.exactCenterX();
        // Rebotar en los bordes del eje X
        if (newXPos > xMax || newXPos < xMin) {
            xVel = -xVel * rho; // Invertir y reducir la velocidad en el eje X (factor de amortiguamiento: rho)
        }
//        yMax = yMax - bounds.exactCenterY();
        // Rebotar en los bordes del eje Y
        if (newYPos > yMax || newYPos < yMin) {
            yVel = -yVel * rho; // Invertir y reducir la velocidad en el eje Y (factor de amortiguamiento: rho)
        }

        xPos = xPos - xVel * frameTime; // Actualizar la posición en el eje X considerando la velocidad
        yPos = yPos - yVel * frameTime; // Actualizar la posición en el eje Y considerando la velocidad
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }
    public void setPos(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public Rect getBounds() {
        return bounds;
    }

    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }

    public int getBallPrefId(String ballPref){
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
