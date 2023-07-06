package edu.codimo.dancingball.maze;

public class Wall {
    private boolean hasWall;
    Wall(){
        this.hasWall = true;
    }

    public boolean isWall() {
        return hasWall;
    }

    public void setWall(boolean isWall) {
        this.hasWall = isWall;
    }
}
