package edu.codimo.dancingball.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.codimo.dancingball.R;
import edu.codimo.dancingball.maze.Ball;
import edu.codimo.dancingball.storage.StorageHandler;

public class BallView extends View {
    private final Bitmap ballBitMap;
    private final Ball ball;

    public BallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Point size = new Point();
        Display display = ((AppCompatActivity) getContext())
                .getWindowManager()
                .getDefaultDisplay();
        display.getSize(size);
        StorageHandler storageHandler = new StorageHandler(context, getResources().getString(R.string.PREF_KEY));
        // Calcula el límite máximo en el eje X restando 100
        ball = new Ball(size.x - 100, size.y - 100);
        String ballPref = storageHandler.getString(getResources().getString(R.string.ball_pref_key));

        Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), ball.getBallPrefId(ballPref));
        MazeView mazeView = findViewById(R.id.MazeView);

        final int dstWidth = (int) mazeView.maze.getCellSize();
        final int dstHeight = (int) mazeView.maze.getCellSize();

        ballBitMap = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // Dibujar la imagen de la bola en las coordenadas xPos y yPos en el canvas
        canvas.drawBitmap(ballBitMap, ball.getXPos(), ball.getYPos(), null);

        // Redibujar la vista
        invalidate();
    }

}
