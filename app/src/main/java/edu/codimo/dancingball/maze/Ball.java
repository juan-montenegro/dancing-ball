package edu.codimo.dancingball.maze;

import edu.codimo.dancingball.R;

public class Ball {
    private float xPos, xVel = 0.0f;
    private float yPos, yVel = 0.0f;
    private float xMax, yMax;
    public Ball(float xMax, float yMax){
        setMaxSize(xMax,yMax);
    }

    public void setMaxSize(float xMax, float yMax){
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public void updateBall(float xAccel, float yAccel) {
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

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
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
