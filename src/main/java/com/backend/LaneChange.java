package com.backend;

/**
 * Created by sandersalahmadibapost on 21/06/2018.
 */
public class LaneChange {

    private double bSafe;
    private Model model;

    public LaneChange(double bSafe, Model model){
        this.bSafe = bSafe;
        this.model = model;

    }

    public LaneChange(){
        this.bSafe = 4;
    }

    public boolean safetyCriterion(Car car){
        boolean isSafe = false;

        //Get car behind in target lane
//        Car neighbour = car.getLocRoad().getLanes().;
        //Get distance of car behind in target lane to current car if car was to change lanes
        //Needs to be different if a car is driving vertically or horizontally
       // double dist = Math.sqrt(Math.pow(car.getLocX() - neighbour.getLocX(), 2) + (Math.pow(car.getLocY() - neighbour.getLocY(), 2)));
        //Get acceleration of current car
        double vel = car.getVel();

        //double accNewNeighbour = model.acceleration(neighbour, dist, vel);


        //if(accNewNeighbour >= -bSafe){
          //  isSafe = true;
       // }
        return isSafe;
    }
}
