package edu.codimo.dancingball.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.codimo.dancingball.R;
import edu.codimo.dancingball.maze.Maze;
import edu.codimo.dancingball.maze.Wall;
import edu.codimo.dancingball.storage.StorageHandler;

public class MazeView extends View {
    public Maze maze;
    private final int MAZE_COLS;
    private final int MAZE_ROWS;
    private float wallThickness;
    private final Paint wallPainter;

    private float topMargin;
    private float leftMargin;

    public MazeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        StorageHandler storageHandler = new StorageHandler(context, getResources().getString(R.string.PREF_KEY));
        MAZE_COLS = storageHandler.getInt(
                getResources().getString(R.string.cols_pref_key),5);
        MAZE_ROWS = storageHandler.getInt(
                getResources().getString(R.string.rows_pref_key),5);
        Point size = new Point();
        Display display = ((AppCompatActivity) getContext())
                .getWindowManager()
                .getDefaultDisplay();
        display.getSize(size);
        int width = size.x;
        wallThickness = (float) (width * 0.005);
        maze = new Maze(MAZE_COLS, MAZE_ROWS);
        wallPainter = new Paint();
        wallPainter.setColor(Color.BLACK);
        wallPainter.setStrokeWidth(wallThickness);
        maze.generateMaze();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        int width = getWidth();
        int height = getHeight();

        int ratio = width / height;
        if (ratio < (MAZE_COLS / MAZE_ROWS)){
            maze.setCellSize(width / ((float) MAZE_COLS + 1));
        } else {
            maze.setCellSize(height / ((float) MAZE_ROWS + 1));
        }
        leftMargin  = (width - MAZE_COLS * maze.getCellSize())/2;
        topMargin   = (height - MAZE_ROWS * maze.getCellSize())/2;
        canvas.translate(leftMargin, topMargin);
        drawMaze(canvas);
        invalidate();
    }
    private void drawMaze(Canvas canvas){
        for (int x = 0; x < MAZE_COLS; x++){
            for (int y = 0; y < MAZE_ROWS; y++){
                drawWall(canvas, maze.getMaze()[x][y].getNorth(),
                        x, y, x + 1, y);
                drawWall(canvas, maze.getMaze()[x][y].getSouth(),
                        x, y + 1, x + 1, y + 1);
                drawWall(canvas, maze.getMaze()[x][y].getWest(),
                        x, y, x, y + 1);
                drawWall(canvas, maze.getMaze()[x][y].getEast(),
                        x + 1, y, x + 1, y + 1);
            }
        }
    }


    private void drawWall(Canvas canvas, Wall wall, int xInit, int yInit, int xFinal,  int yFinal) {
        if(wall.isWall()){
            canvas.drawLine(
                    xInit   * maze.getCellSize(),
                    yInit   * maze.getCellSize(),
                    xFinal  * maze.getCellSize(),
                    yFinal  * maze.getCellSize(),
                    wallPainter
            );
        }
    }

}