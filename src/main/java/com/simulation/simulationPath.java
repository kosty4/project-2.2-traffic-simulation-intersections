package com.simulation;

import java.util.ArrayList;


public class simulationPath {
    int startX,startY;
    ArrayList<ArrayList<Integer>> path;
    ArrayList<Integer> directions;
    ArrayList<Integer> laneIndices = new ArrayList<>();

    public simulationPath(int startX, int startY){
        this.startX = startX;
        this.startY = startY;

        ArrayList<Integer> startpoint = new ArrayList<>(2);

        startpoint.add(startX);
        startpoint.add(startY);

        path = new ArrayList<>();
        directions = new ArrayList<>();
        path.add(startpoint);
        System.out.println("startpoint " +startpoint);
    }
    public simulationPath(int startX, int startY, int dir){
        this.startX = startX;
        this.startY = startY;

        ArrayList<Integer> startpoint = new ArrayList<>(2);

        startpoint.add(startX);
        startpoint.add(startY);

        path = new ArrayList<>();
        directions = new ArrayList<>();
        directions.add(dir);
        path.add(startpoint);
        System.out.println("startpoint " +startpoint);
    }
    public void setLanes()
    {
        for(int i = 0; i< path.size()-1; i++)
        {
            laneIndices.add(0);
        }
    }

    public void setLaneIndex(int laneIndex, int index)
    {
        laneIndices.remove(index);
        laneIndices.add(index, laneIndex);
    }

    public void addtoPath(int x, int y, int direction){
        ArrayList<Integer> point = new ArrayList<>(2);
        point.add(x);
        point.add(y);

        directions.add(direction);

        path.add(point);
    }

    public void remove(int index)
    {
        path.remove(index);
    }

    public void addtoPath(int x, int y, int direction, int index){
        ArrayList<Integer> point = new ArrayList<>(2);
        point.add(x);
        point.add(y);

        directions.add(direction);

        path.add(index, point);
    }

    public void addtoPathAt(int x, int y, int direction, int index){
        ArrayList<Integer> point = new ArrayList<>(2);
        point.add(x);
        point.add(y);

//        directions.remove(index);
//        directions.add(index, direction);
        path.remove(index);
        path.add(index, point);
    }

    public int getX(int intex){
        return this.path.get(intex).get(0);
    }

    public int getY(int intex){
        return this.path.get(intex).get(1);
    }


}
