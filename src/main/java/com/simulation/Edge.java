package com.simulation;

//import backend.FSMTrafficLight;
//import backend.Intersection;
//import backend.Road;

public class Edge {

    public Node start, end;
    public int type;

    public int direction;
//    public int index;
    public boolean addedToRoad;
    private int speedLimit;
    private int distance;
    private boolean blocked;
    private int laneIndex;


    public Edge(Node start, Node end) {

        this.start = start;
        this.end = end;

        int deltaX = (int) start.Xpos - (int) end.Xpos;
        int deltaY = (int) start.Ypos - (int) end.Ypos;

        this.distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        this.direction = checkDirection();

        addedToRoad = false;

        this.laneIndex = -1;

    }

    public int getLaneIndex() {
        return laneIndex;

    }

    public void setLaneIndex(int index) {
        this.laneIndex = index;
    }

    public void setAddedToRoad(boolean bool) {
        this.addedToRoad = bool;
    }


    public int checkDirection() {
        if (this.getStartNode().x < this.getEndNode().x && this.getStartNode().y == this.getEndNode().y) {
            return 6;
        }
        if (this.getStartNode().x > this.getEndNode().x && this.getStartNode().y == this.getEndNode().y) {
            return 4;
        }
        if (this.getStartNode().x == this.getEndNode().x && this.getStartNode().y > this.getEndNode().y) {
            return 8;
        }
        if (this.getStartNode().x == this.getEndNode().x && this.getStartNode().y < this.getEndNode().y) {
            return 2;
        }

        return -1;
    }

    public Node getStartNode() {
        return this.start;
    }

    public Node getEndNode() {
        return this.end;
    }

    public int getSpeedLimit() {
        return this.speedLimit;
    }

    public void setSpeedLimit(int speed) {
        this.speedLimit = speed;
    }

}
