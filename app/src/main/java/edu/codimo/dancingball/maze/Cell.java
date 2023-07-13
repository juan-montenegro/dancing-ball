package edu.codimo.dancingball.maze;

public class Cell {
    private final Wall north;
    private final Wall east;
    private final Wall south;
    private final Wall west;
    private boolean visited = false;
    private final int[] position;

    public Cell(){
        north = new Wall();
        east = new Wall();
        south = new Wall();
        west = new Wall();
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

    public boolean isVisited() {
        return visited;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position[0] = x;
        position[1] = y;
    }

}
