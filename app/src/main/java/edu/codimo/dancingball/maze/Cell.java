package edu.codimo.dancingball.maze;

public class Cell {
    private final Wall north;
    private final Wall east;
    private final Wall south;
    private final Wall west;
    private boolean visited;
    Cell(){
        this.north = new Wall();
        this.east = new Wall();
        this.south = new Wall();
        this.west = new Wall();
        this.visited = false;
    }
    public void setVisited(boolean state){
        this.visited = state;
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
}
