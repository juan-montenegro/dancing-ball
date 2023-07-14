package edu.codimo.dancingball.maze;

public class Cell {
    private final Wall north;
    private final Wall east;
    private final Wall south;
    private final Wall west;
    private boolean visited;
    private final int[] position;

    private boolean hasPlayer;
    private boolean isStart;
    private boolean isEnd;

    public Cell(){
        north = new Wall();
        east = new Wall();
        south = new Wall();
        west = new Wall();
        visited = false;
        hasPlayer = false;
        isStart = false;
        isEnd = false;
        position = new int[2];
    }

    public void setVisited(boolean state){
        visited = state;
    }

    public void setNorth(boolean north) {
        this.north.setWall(north);
    }

    public void setEast(boolean east) {
        this.east.setWall(east);
    }

    public void setSouth(boolean south) {
        this.south.setWall(south);
    }

    public void setWest(boolean west) {
        this.west.setWall(west);
    }

    public Wall getNorth() {
        return north;
    }

    public Wall getEast() {
        return east;
    }

    public Wall getSouth() {
        return south;
    }

    public Wall getWest() {
        return west;
    }

    public boolean setVisited() {
        return !this.visited;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position[0] = x;
        position[1] = y;
    }

    public boolean isHasPlayer() {
        return hasPlayer;
    }

    public void setHasPlayer(boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}
