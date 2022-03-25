package com.backend;

//import com.simulation.AnimationParts;
//import com.simulation.Board;
//import simulation.Graph;
//import com.simulation.carAnimation;
//import sun.awt.geom.AreaOp;

import java.util.ArrayList;

public class CollisionDetection {

    public ArrayList<Car> cars;

    public CollisionDetection() {
        this.cars = new ArrayList<>();

    }

    public void addCar(Car car) {
        this.cars.add(car);
    }

    //TODO orientation of the rectangular bounding box. at the moment its just a square.
    public boolean collisionDetection(Car car) {

        if (cars.size() == 1) {
            return false;
        }

        for (int i = 0; i < cars.size(); i++) {

            if ((car.getCurentDirection() == 6 || car.getCurentDirection() == 4) &&

                    car.getLocX() <= cars.get(i).getLocX() + 25 &&
                    car.getLocX() + 25 >= cars.get(i).getLocX() &&
                    car.getLocY() <= cars.get(i).getLocY() + 12 &&
                    car.getLocY() + 12 >= cars.get(i).getLocY() &&
                    car != cars.get(i)) {

                return true;
            }

            if ((car.getCurentDirection() == 2 || car.getCurentDirection() == 8) &&

                    car.getLocX() <= cars.get(i).getLocX() + 12 &&
                    car.getLocX() + 12 >= cars.get(i).getLocX() &&
                    car.getLocY() <= cars.get(i).getLocY() + 25 &&
                    car.getLocY() + 25 >= cars.get(i).getLocY() &&
                    car!= cars.get(i)) {

                return true;
            }


        }

        return false;

    }

    public boolean frontCarCollisionDetection(Car car) {

        if (cars.size() == 1) {
            return false;
        }

        for (int i = 0; i < cars.size(); i++) {

            if (cars.get(i) != car && cars.get(i).getLocEdge() == car.getLocEdge() && returnCarInFront(car) == null && car.getLocRoad().getDirection() == 6 &&
                    car.getLocX() + 60 >= cars.get(i).getLocX() && car.getLocX() + 25 <= cars.get(i).getLocX() &&
                    car.getLocY() + 10 >= cars.get(i).getLocY() && car.getLocY() - 10 <= cars.get(i).getLocY()
                    && cars.get(i).getCurentDirection() != 6) {

                return true;

            } else if (cars.get(i) != car && cars.get(i).getLocEdge() == car.getLocEdge() && returnCarInFront(car) == null && car.getLocRoad().getDirection() == 4 &&
                    car.getLocX() - 60 <= cars.get(i).getLocX() && car.getLocX() - 25 >= cars.get(i).getLocX() &&
                    car.getLocY() + 10 >= cars.get(i).getLocY() && car.getLocY() - 10 <= cars.get(i).getLocY()
                    && cars.get(i).getCurentDirection() != 4) {
                return true;
            } else if (cars.get(i) != car && cars.get(i).getLocEdge() == car.getLocEdge() && returnCarInFront(car) == null && car.getLocRoad().getDirection() == 8 &&
                    car.getLocY() - 60 <= cars.get(i).getLocY() && car.getLocY() - 25 >= cars.get(i).getLocY() &&
                    car.getLocX() + 10 >= cars.get(i).getLocX() && car.getLocX() - 10 <= cars.get(i).getLocX() &&
                    cars.get(i).getCurentDirection() != 8) {
                return true;
            } else if (cars.get(i) != car && cars.get(i).getLocEdge() == car.getLocEdge() && returnCarInFront(car) == null && car.getLocRoad().getDirection() == 2 &&
                    car.getLocY() + 60 >= cars.get(i).getLocY() && car.getLocY() + 25 <= cars.get(i).getLocY() &&
                    car.getLocX() + 10 >= cars.get(i).getLocX() && car.getLocX() - 10 <= cars.get(i).getLocX() &&
                    cars.get(i).getCurentDirection() != 2) {
                return true;
            }

        }

        return false;
    }


    //return the velocity of the car that is in front of the current car (return that car object)
    //if there is a car on current road && the percentageOnRoad is larger than the current one => return the car in the front, get its velocity.

    public Car returnCarInFront(Car currentCar) {

        int returnIndex = 10000000;
        double distanceDiff = 1000000000;

        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i) != currentCar) {
                if (currentCar.getLocEdge() == cars.get(i).getLocEdge()
                        && currentCar.getPercentageOnCurrentRoad() < cars.get(i).getPercentageOnCurrentRoad()) {

                    double tempDistDiff = cars.get(i).getPercentageOnCurrentRoad() - currentCar.getPercentageOnCurrentRoad();

                    if (distanceDiff > tempDistDiff) {
                        distanceDiff = tempDistDiff;
                        returnIndex = i;
                    }

                }

            }
        }

        if (returnIndex == 10000000) {
            return null;
        } else {

            return cars.get(returnIndex);
        }

    }


}
