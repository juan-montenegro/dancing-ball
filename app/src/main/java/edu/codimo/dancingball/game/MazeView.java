package edu.codimo.dancingball.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.codimo.dancingball.R;
import edu.codimo.dancingball.maze.Ball;
import edu.codimo.dancingball.maze.Cell;
import edu.codimo.dancingball.maze.Maze;
import edu.codimo.dancingball.maze.Wall;
import edu.codimo.dancingball.storage.StorageHandler;

public class MazeView extends View {
    private final Maze maze;
    private final Bitmap ballBitMap;
    private final Ball ball;
    private final  StorageHandler storageHandler;
    private int MAZE_COLS;
    private int MAZE_ROWS;
    private final Paint wallPainter;

    private final float wallThickness;
    private final Rect rectBall;
    private final Point size;

    public MazeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        storageHandler = new StorageHandler(context, getResources().getString(R.string.PREF_KEY));
        getMazeSize();

        String ballPref = storageHandler.getString(getResources().getString(R.string.ball_pref_key));
        Display display = ((AppCompatActivity) getContext())
                .getWindowManager()
                .getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        Log.d("SIZE","SIZE(X = " + size.x + ", Y = " + size.y + ")");
        // Calcula el límite máximo en el eje X restando 100
        ball = new Ball(size.x - 100, size.y - 100);
        int width = size.x;
        wallThickness = (float) (width * 0.005);
        maze = new Maze(MAZE_COLS, MAZE_ROWS);

        wallPainter = new Paint();
        wallPainter.setColor(Color.BLACK);
        wallPainter.setStrokeWidth(wallThickness);

        final int dstWidth = (int) maze.getCellSize() + ((int) wallThickness * 15);
        final int dstHeight = (int) maze.getCellSize() + ((int) wallThickness * 15);

        Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), ball.getBallPrefId(ballPref));
        ballBitMap = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);

        rectBall = new Rect();
    }

    private void getMazeSize() {
        MAZE_ROWS = storageHandler.getInt(
                getResources().getString(R.string.rows_pref_key),5);
        MAZE_COLS = storageHandler.getInt(
                getResources().getString(R.string.cols_pref_key),5);
    }

    public void refreshMaze(){
        getMazeSize();
        maze.refreshMaze(MAZE_COLS, MAZE_ROWS);
        maze.init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        int width = getWidth();
        int height = getHeight();

        ball.setMaxSize(width,height);

        int ratio = width / height;
        if (ratio < (MAZE_COLS / MAZE_ROWS)){
            maze.setCellSize(width / ((float) MAZE_COLS + 1));
        } else {
            maze.setCellSize(height / ((float) MAZE_ROWS + 1));
        }
        float leftMargin = (width - MAZE_COLS * maze.getCellSize()) / 2;
        float topMargin = (height - MAZE_ROWS * maze.getCellSize()) / 2;
        canvas.translate(leftMargin, topMargin);
        drawMaze(canvas);
        // Dibujar la imagen de la bola en las coordenadas xPos y yPos en el canvas
        canvas.drawBitmap(ballBitMap, ball.getXPos() + 10, ball.getYPos() + 10, null);
        rectBall.set(
                (int) ball.getXPos(),
                (int) ball.getYPos(),
                  (int) ball.getXPos() + ballBitMap.getWidth(),
                (int) ball.getYPos() + ballBitMap.getHeight()
        );
        ball.setBounds(rectBall);
        invalidate();
    }
    private void drawMaze(Canvas canvas){
        for (int x = 0; x < MAZE_COLS; x++){
            for (int y = 0; y < MAZE_ROWS; y++){
                Cell cell = maze.getMaze()[x][y];
                drawWall(canvas, cell.getNorth(), x, y, x + 1, y);
                drawWall(canvas, cell.getSouth(), x, y + 1, x + 1, y + 1);
                drawWall(canvas, cell.getWest(), x, y, x, y + 1);
                drawWall(canvas, cell.getEast(), x + 1, y, x + 1, y + 1);
            }
        }
    }


    private void drawWall(Canvas canvas, Wall wall, int xInit, int yInit, int xFinal,  int yFinal) {

        if(wall.hasWall()){
//            canvas.drawLine(
//                    xInit   * maze.getCellSize(),
//                    yInit   * maze.getCellSize(),
//                    xFinal  * maze.getCellSize(),
//                    yFinal  * maze.getCellSize(),
//                    wallPainter
//            );
            Rect rectWall = new Rect(
                    (xInit      * (int) maze.getCellSize()) + ((int) wallThickness + 1),
                    (yInit      * (int) maze.getCellSize()) + ((int) wallThickness + 1),
                    xFinal     * (int) maze.getCellSize()  + ((int) wallThickness - 1),
                    yFinal   * (int) maze.getCellSize()  + ((int) wallThickness - 1)
            );
            wall.setBounds(rectWall);
            canvas.drawRect(rectWall,wallPainter);


            if (Rect.intersects(rectWall,rectBall)) {
                // Rectangles collide
                // Handle collision logic here

                // HANDLE VERTICAL COLLISION
                if ((rectBall.left >= rectWall.left) && (rectBall.left <= rectWall.right)){
                    ball.setMaxSize(
                            maze.getCellSize() * (rectBall.exactCenterX() + rectWall.right),
                            size.y - 100
                    );
//                    ball.setMinSize(
//                            maze.getCellSize() + rectBall.exactCenterX(),
//                            0
//                    );
                } else if ((rectBall.right >= rectWall.right) && (rectBall.right <= rectWall.left)){
                    ball.setMaxSize(
                            maze.getCellSize() * (rectBall.exactCenterX() + rectWall.left),
                            size.y - 100
                    );
                }
                // HANDLE HORIZONTAL COLLISION
                else if ((rectBall.top >= rectWall.top) && (rectBall.top <= rectWall.bottom)){
                    ball.setMaxSize(
                            size.x - 100,
                            maze.getCellSize() * (rectBall.exactCenterY() + rectWall.bottom)
                    );
                } else if ((rectBall.bottom >= rectWall.bottom) && (rectBall.bottom <= rectWall.top)){
                    ball.setMaxSize(
                            size.x - 100,
                            maze.getCellSize() * (rectBall.exactCenterY() + rectWall.top)
                    );
                }
                else {
                    ball.setMaxSize(size.x - 100, size.y - 100);
                    ball.setMinSize(0,0);
                }
            }
        }
    }

    public void updateBall(float xAccel, float yAccel) {
        ball.updateBall(xAccel, yAccel);
    }
    public void setBallToStart(){
        ball.setPos(0,0);
    }
}