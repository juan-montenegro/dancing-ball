package edu.codimo.dancingball.maze;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Maze {
    private Cell[][] maze;
    private int i,j;
    private float cellSize;
    private final Random seed;
    public Maze(int i, int j){
        seed = new Random();
        this.i = i;
        this.j = j;
        init();
    }

    public void init(){
        maze = new Cell[i][j];
        for (int x = 0; x<i; x++){
            for (int y = 0; y<j; y++){
                maze[x][y] = new Cell();
                maze[x][y].setPosition(x,y);
            }
        }
        generateMaze();
    }

    public void generateMaze(){
        Stack<Cell> stack = new Stack<>();
        Cell current, next;
        current = maze[0][0];
        current.setStart(true);
        current.setHasPlayer(true);
        current.setVisited(true);
        Log.d("generateMaze", "Generating maze....");
        while (true) {
            next = getNextCell(current);
            if (next != null) {
                removeWall(current, next);
                stack.push(current);
                current = next;
                current.setVisited(true);
            } else {
                current = stack.pop();
            }
            if (stack.empty()) {
                current.setEnd(true);
                Log.d("generateMaze", "Maze Generated");
                break;
            }
        }
    }

    private void removeWall(Cell current, Cell next) {
        int[] currentIndexes = current.getPosition();
        int[] nextIndexes = next.getPosition();
        Log.d("REMOVING WALLS", "REMOVING WALLS....");
        if (currentIndexes[0] == nextIndexes[0] && currentIndexes[1] == nextIndexes[1] + 1){
            Log.d("REMOVING WALLS", "Removing NORTH wall");
            current.setNorth(false);
            next.setSouth(false);
        }
        if (currentIndexes[0] == nextIndexes[0] && currentIndexes[1] == nextIndexes[1] - 1){
            Log.d("REMOVING WALLS", "Removing SOUTH wall");
            current.setSouth(false);
            next.setNorth(false);
        }
        if (currentIndexes[0] == nextIndexes[0] + 1 && currentIndexes[1] == nextIndexes[1]){
            Log.d("REMOVING WALLS", "Removing WEST wall");
            current.setWest(false);
            next.setEast(false);
        }
        if (currentIndexes[0] == nextIndexes[0] - 1 && currentIndexes[1] == nextIndexes[1]){
            Log.d("REMOVING WALLS", "Removing EAST wall");
            current.setEast(false);
            next.setWest(false);
        }
    }

    private Cell getNextCell(Cell current) {
        ArrayList<Cell> cells = new ArrayList<>();
        int col = current.getPosition()[0];
        int row = current.getPosition()[1];
        Log.d("CELL_POSITION", "CELL[COL][ROW] = cell[" + col + "][" + row + "]");
        if (row > 0){
            Log.d("TOP_CELL", "Cell topCell = maze["+col+"]["+ (row - 1) +"] ");
            Cell topCell = maze[col][row - 1];
            if(topCell.setVisited()){
                cells.add(topCell);
            }
        }
        if (row < j - 1){
            Log.d("BOTTOM_CELL", "Cell bottomCell = maze["+col+"]["+ (row + 1) +"] ");
            Cell bottomCell = maze[col][row + 1];
            if(bottomCell.setVisited()){
                cells.add(bottomCell);
            }
        }
        if (col > 0){
            Log.d("LEFT_CELL", "Cell leftCell = maze["+(col-1)+"]["+ (row) +"] ");
            Cell leftCell = maze[col - 1][row];
            if(leftCell.setVisited()){
                cells.add(leftCell);
            }
        }
        if (col < i - 1){
            Log.d("RIGHT_CELL", "Cell rightCell = maze["+(col+1)+"]["+ (row) +"] ");
            Cell rightCell = maze[col + 1][row];
            if(rightCell.setVisited()){
                cells.add(rightCell);
            }
        }
        if (cells.size() > 0){
            int nextCellIndex = seed.nextInt(cells.size());
            return cells.get(nextCellIndex);
        }
        return  null;
    }

    public float getCellSize() {
        return cellSize;
    }

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
    }

    public Cell[][] getMaze() {
        return maze;
    }
    public void refreshMaze(int i, int j){
        this.i = i;
        this.j = j;
    }

    @NonNull
    public String toString(){
        StringBuilder string = new StringBuilder();
        for (int x = 0; x<i; x++){
            for (int y = 0; y<j; y++){
                string.append(maze[x][y]).append(",");
            }
        }
        return "Maze = {" + string + "}";
    }
}
