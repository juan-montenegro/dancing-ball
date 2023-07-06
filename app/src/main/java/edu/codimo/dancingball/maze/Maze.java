package edu.codimo.dancingball.maze;

public class Maze {
//    private int[][] map;
    private Cell[][] maze;
    private int i,j;
    Maze(int i, int j){
        this.i = i;
        this.j = j;
        this.maze = new Cell[this.i][this.j];
    }

    public void generateMaze(){

    }

}
