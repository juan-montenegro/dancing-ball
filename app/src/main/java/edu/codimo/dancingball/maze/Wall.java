package edu.codimo.dancingball.maze;

import android.graphics.Rect;

public class Wall {
    private boolean hasWall = true;
    private Rect bounds;
    public Wall() {

    }

    public boolean hasWall() {
        return hasWall;
    }

    public void setWall(boolean hasWall) {
        this.hasWall = hasWall;
    }

    public boolean isHasWall() {
        return hasWall;
    }

    public void setHasWall(boolean hasWall) {
        this.hasWall = hasWall;
    }

    public Rect getBounds() {
        return bounds;
    }

    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }
}
