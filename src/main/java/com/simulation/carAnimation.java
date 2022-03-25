package com.simulation;

import com.backend.Car;
//import backend.CollisionDetection;
//import backend.Model;
import com.backend.CollisionDetection;
import com.backend.Model;
import javafx.animation.*;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.image.ImageView;


import java.util.ArrayList;

//separate animation class for each car.

public class carAnimation {


    final LongProperty lastUpdateTime = new SimpleLongProperty();
    double carVelocity;
    ArrayList<Integer> IntPath;
    ImageView imgView;
    simulationPath simPath;
    AnimationTimer animationTimer;
    PathConstructor pathConstructor;
    Car car;
    int lane0X = 10, lane0Y = 10, lane1X = 20, lane1Y = 20;
    int pathIterator;
    int previousPosition;
    int dir, carsizeWidth, carsizeHight;
    boolean first = true;

    public carAnimation(Graph graph, Board board, Model model, Car car, CollisionDetection collisionDetection) {


        this.car = car;
        this.IntPath = car.getPath();
        carVelocity = car.getDesVel();

        pathIterator = 1;

        //TODO use roads instead of edges

//        car.setLocEdge(graph.getEdge(graph.getNodeByIndex(IntPath.get(pathIterator - 1)), graph.getNodeByIndex(IntPath.get(pathIterator))), car.StartingLane);

        car.setLocRoad(model.map.getRoadByEdge(graph.getEdge((graph.getNodeByIndex(IntPath.get(pathIterator - 1))), graph.getNodeByIndex(IntPath.get(pathIterator)))), car.StartingLane);

        carsizeWidth = 25;
        carsizeHight = 13;

        javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/car.PNG", carsizeWidth, carsizeHight, true, false);
        imgView = new ImageView(img);


        this.pathConstructor = new PathConstructor(IntPath, graph, board.SIM_SIZE, car);
        simPath = pathConstructor.constructPath();

        car.setLocRoad(model.map.getRoadByEdge(graph.getEdge((graph.getNodeByIndex(IntPath.get(pathIterator - 1))), graph.getNodeByIndex(IntPath.get(pathIterator)))), car.StartingLane);

        car.setLocX(simPath.startX + carsizeWidth / 2);
        car.setLocY(simPath.startY + carsizeHight / 2);

        imgView.setTranslateX(simPath.startX);
        imgView.setTranslateY(simPath.startY);

        //prerotate
        if (simPath.directions.get(0) == 8) {
            imgView.setRotate(270);
        }

        if (simPath.directions.get(0) == 4) {
            imgView.setRotate(180);
        }

        if (simPath.directions.get(0) == 2) {
            imgView.setRotate(90);
        }

        if (simPath.directions.get(0) == 6) {
            imgView.setRotate(360);
        }

        if(car.StartingLane == 0){
            //dont displace car
        }
        else if(car.StartingLane == 1){
            //changeLane(1,car.getCurentDirection());
        }

        animationTimer = new AnimationTimer() {


            @Override
            public void handle(long now) {

                // insert in the if statement to make the care move only on green : && car.getLocRoad().getTrafficLight().getCurrentstate() == 3
                if (lastUpdateTime.get() > 0) {

//                    System.out.println("I am currently driving on lane number " + car.getLocEdge().getLaneIndex() + "  XPOS "+ car.getLocX()+ "  YPOS "+ car.getLocY());

                    int xCoord = simPath.path.get(pathIterator).get(0);
                    int yCoord = simPath.path.get(pathIterator).get(1);

                    final double elapsedSeconds = (now - lastUpdateTime.get()) / 1_000_000_000.0;

                    final double delta = elapsedSeconds * car.getVel() * 2;

                    final double oldX = imgView.getTranslateX();
                    final double oldY = imgView.getTranslateY();
                    double newX = oldX + delta;
                    double newY = oldY + delta;

                    double dist = 100000;
                    double carFrontVelocity = 0;

//                    FOR TESTING LANE CHANGING
//                    if (car.getLocX() > 100 && first) {
//
//                        System.out.println("SWIIITTCH" + simPath.directions.get(pathIterator - 1));
//                        dir = simPath.directions.get(pathIterator - 1);
//                        changeLane(1, dir);
//
//                        first = false;
//                    }

                    xCoord = simPath.path.get(pathIterator).get(0);
                    yCoord = simPath.path.get(pathIterator).get(1);

                    if (simPath.directions.get(pathIterator - 1) == 4) {
                        newX = oldX - delta;
                    }

                    if (simPath.directions.get(pathIterator - 1) == 8) {
                        newY = oldY - delta;
                    }


                    if (simPath.directions.get(pathIterator - 1) == 6 && newX < xCoord) {
                        imgView.setTranslateX(newX);
                        car.setLocX(newX + carsizeWidth / 2);

                    } else if (simPath.directions.get(pathIterator - 1) == 4 && newX > xCoord) {
                        imgView.setTranslateX(newX);
                        car.setLocX(newX + carsizeWidth / 2);

                    } else if (simPath.directions.get(pathIterator - 1) == 2 && newY < yCoord) {
                        imgView.setTranslateY(newY);
                        car.setLocY(newY + carsizeHight / 2);

                    } else if (simPath.directions.get(pathIterator - 1) == 8 && newY > yCoord) {
                        imgView.setTranslateY(newY);
                        car.setLocY(newY + carsizeHight / 2);

                    } else {

                        if (pathIterator < simPath.path.size() - 1) {


                            car.setLocRoad(model.map.getRoadByEdge(graph.getEdge((graph.getNodeByIndex(IntPath.get(pathIterator - 1))), graph.getNodeByIndex(IntPath.get(pathIterator)))), simPath.laneIndices.get(pathIterator-1));


                            imgView.setTranslateX(Math.round(xCoord));
                            imgView.setTranslateY(Math.round(yCoord));


                            int oldDir = simPath.directions.get(pathIterator - 1);

                            pathIterator++;

                            int newDir = simPath.directions.get(pathIterator - 1);


//                            car.setLocEdge(graph.getEdge(graph.getNodeByIndex(IntPath.get(pathIterator - 1)),
//                                    graph.getNodeByIndex(IntPath.get(pathIterator))),0);
//
//                            car.setLocRoad(model.map.getRoadByEdge(graph.getEdge((graph.getNodeByIndex(IntPath.get(pathIterator - 1))), graph.getNodeByIndex(IntPath.get(pathIterator)))), car.StartingLane);

                            car.setLocRoad(model.map.getRoadByEdge(graph.getEdge((graph.getNodeByIndex(IntPath.get(pathIterator - 1))), graph.getNodeByIndex(IntPath.get(pathIterator)))), simPath.laneIndices.get(pathIterator-1));


                            if (oldDir == 6) {
                                if (newDir == 8) {
                                    imgView.setRotate(270);
                                }
                                if (newDir == 2) {
                                    imgView.setRotate(90);
                                }
                            }
                            if (oldDir == 4) {
                                if (newDir == 8) {
                                    imgView.setRotate(270);
                                }
                                if (newDir == 2) {
                                    imgView.setRotate(90);
                                }
                            }

                            if (oldDir == 2) {
                                if (newDir == 4) {
                                    imgView.setRotate(180);
                                }
                                if (newDir == 6) {
                                    imgView.setRotate(0);
                                }
                            }
                            if (oldDir == 8) {
                                if (newDir == 4) {
                                    imgView.setRotate(180);
                                }
                                if (newDir == 6) {
                                    imgView.setRotate(0);
                                }
                            }


                            System.out.println(simPath.directions);

                        } else {

                            stop();
                            car.stopTime = System.currentTimeMillis();
//                            System.out.println("Time taken for car to reach destination " + car.getElapsedTimeTotal());
//                            System.out.println("Total time waiting at intersection " + car.totalTimeAtIntersections/60);
//                            System.out.println("delete car! ");
                            car.destinationReached = true;
                        }

                    }


                    //if there is a car in the front, on current road
                    if (collisionDetection.returnCarInFront(car) != null) {
                        //calculate the distance between current car and the car in the front .

                        dist = (Math.sqrt(Math.pow((car.getLocX() - collisionDetection.returnCarInFront(car).getLocX()), 2) + (Math.pow(car.getLocY() - collisionDetection.returnCarInFront(car).getLocY(), 2)))) - 25 - 25;

                        carFrontVelocity = collisionDetection.returnCarInFront(car).getVel();
                        //round a small number
                        if (carFrontVelocity < 0.1) {
                            carFrontVelocity = 0;
                        }

                        if(car.timeAtIntersectionStart == 0 && car.getVel() < 0.2 &&
                                (pathIterator != simPath.path.size() - 1 )){
                            System.out.println("time start at intersection");
                            car.timeAtIntersectionStart = System.currentTimeMillis();
                        }


                    } else {
                        //else check the distance in the front node (...)
                        if (car.getPercentageOnCurrentRoad() > 30 && collisionDetection.returnCarInFront(car) == null) {

                            if (car.getLocRoad().existsTrafficLight() == false) {
                                //continue going full speed
                            }
                            else if (car.getLocRoad().getTrafficLight().getCurrentstate() != 3) {

                                if (car.getCurentDirection() == 4 && simPath.directions.get(pathIterator) == 2) {
                                    dist = Math.sqrt(Math.pow((car.getLocX() - simPath.getX(pathIterator)), 2) + (Math.pow(car.getLocY() - simPath.getY(pathIterator), 2))) - 40 - (car.getLocRoad().lanes.size() * 15) - 10;
                                    carFrontVelocity = 0;

                                }
                                if (car.getCurentDirection() == 8 && simPath.directions.get(pathIterator) == 4) {
                                    dist = Math.sqrt(Math.pow((car.getLocX() - simPath.getX(pathIterator)), 2) + (Math.pow(car.getLocY() - simPath.getY(pathIterator), 2))) - 40 - (car.getLocRoad().lanes.size() * 15) ;
                                    carFrontVelocity = 0;
                                } else {
                                    dist = Math.sqrt(Math.pow((car.getLocX() - simPath.getX(pathIterator)), 2) + (Math.pow(car.getLocY() - simPath.getY(pathIterator), 2))) - 40;
                                    carFrontVelocity = 0;

                                }

                                if (car.getVel() < 1 && car.getVel() >= 0 ) {

                                    car.setVel(car.getVel() - 0.1);
//

                                }


                                if(car.timeAtIntersectionStart == 0 && car.getVel() < 0.2 &&
                                        (pathIterator != simPath.path.size() - 1 )){

                                    System.out.println("Time start at intersection");
                                    car.timeAtIntersectionStart = System.currentTimeMillis();
                                }

                            }
                            else if (car.getLocRoad().getTrafficLight().getCurrentstate() == 3 ) {

                                if(car.timeAtIntersectionStart != 0 ){

                                    car.timeAtIntersectionEnd = System.currentTimeMillis();

                                    car.totalTimeAtIntersections += car.timeAtIntersectionEnd - car.timeAtIntersectionStart;

                                    car.timeAtIntersectionStart = 0;
                                    car.timeAtIntersectionEnd = 0;

                                }


//                                System.out.println("Car stood at intersection " + car.totalTimeAtIntersections);
                            }
                        }

                        //decelerate before doing a turn untill "10 meters per second" .
                        if (car.getPercentageOnCurrentRoad() > 60 && (pathIterator < simPath.path.size() - 1) &&
                                nextActionIsTurn(simPath.directions.get(pathIterator - 1), simPath.directions.get(pathIterator))) {

                            if (car.getVel() > 10) {

                                car.setVel(car.getVel() - 0.1);

                            }
                        }

                        //rough draft of side collision detection.... its carried by the collisionDetection class without IDM.
                        //just by increasing the margins of normal collisiondetection.
                        if (collisionDetection.frontCarCollisionDetection(car) ) {

                            if (car.getVel() > 0) {

                                car.setVel(car.getVel() - 0.2);
                            }
                        }
//                        System.out.println("X " + car.getLocX() + "  Y: "+ car.getLocY());

                        //stop the car properly before the goal
                        if (pathIterator == simPath.path.size() - 1 && car.getPercentageOnCurrentRoad() > 70) {
//                            dist = Math.sqrt(Math.pow((imgView.getTranslateX() - simPath.getX(pathIterator)), 2) + (Math.pow(imgView.getTranslateY() - simPath.getY(pathIterator), 2)));

                            if (dist < 0.1 && car.destinationReached == false) {
                                car.stopTime = System.currentTimeMillis();
                                car.destinationReached = true;


                            }
                        }
                    }


                    //"Dist " + dist + "  CarFrontVelocity " + "  local edge : " + car.getLocEdge() + "   " +  carFrontVelocity +

                    car.setVel((car.getVel() + (model.acceleration(car, dist, carFrontVelocity) * 0.016)));

                    //try to detect a collision here
                    if (collisionDetection.collisionDetection(car) && car.getPercentageOnCurrentRoad() > 5 && car.getPercentageOnCurrentRoad() < 95 ) {

                        stopCarAnimation();

                        System.out.println("COLLISION");
                    }
                }

                lastUpdateTime.set(now);

            }
        };
    }


    public void changeLane(int laneIndex, int dire) {


        int index = pathIterator;
        ArrayList<Double> point = new ArrayList<>();
        double curX = imgView.getTranslateX();
        double curY = imgView.getTranslateY();
        double diffX = 0;
        double diffY = 0;
        double nextX = simPath.getX(index);
        double nextY = simPath.getY(index);

        //If it's changing to outer road
        if (dire == 8 && laneIndex == 1) {
            diffX += lane0Y;
            this.car.setLocRoad(this.car.getLocRoad(),1);
            //diffY -= 10;
        }

        if (dire == 2 && laneIndex == 1) {
            diffX -= lane0Y;
            this.car.setLocRoad(this.car.getLocRoad(),1);
            //diffY += 10;
        }

        if (dire == 4 && laneIndex == 1) {
            diffY -= lane0Y;
            this.car.setLocRoad(this.car.getLocRoad(),1);
            //diffX -= 10;
        }

        if (dire == 6 && laneIndex == 1) {
            diffY += lane0Y;
            this.car.setLocRoad(this.car.getLocRoad(),1);
            //diffX += 10;
        }

        //If it's changing back to middle road
        if (dire == 8 && laneIndex == 0) {
            diffX -= lane0Y;
            this.car.setLocRoad(this.car.getLocRoad(),0);
            //diffY -= 10;
        }

        if (dire == 2 && laneIndex == 0) {
            diffX += lane0Y;
            this.car.setLocRoad(this.car.getLocRoad(),0);
            //diffY += 10;
        }

        if (dire == 4 && laneIndex == 0) {
            diffY += lane0Y;
            this.car.setLocRoad(this.car.getLocRoad(),0);
            //diffX -= 10;
        }

        if (dire == 6 && laneIndex == 0) {
            diffY -= lane0Y;
            this.car.setLocRoad(this.car.getLocRoad(),0);
            //diffX += 10;
        }

        simPath.remove(index);
        //simPath.addtoPath((int)(curX + diffX), (int)(curY + diffY), dire, index);
        System.out.println("Difference: " + diffX + ", " + diffY);

        imgView.setTranslateY(curY + diffY);
        imgView.setTranslateX(curX + diffX);
        simPath.addtoPathAt((int) (nextX + diffX), (int) (nextY + diffY), dire, index);
        if (index <= IntPath.size()) {
            int counter = 1;
            while (index + counter < IntPath.size() && simPath.directions.get(index + counter) == simPath.directions.get(index + counter - 1)) {
                System.out.println("Path edited");
                int pathX = simPath.getX(index + counter);
                int pathY = simPath.getY(index + counter);
                simPath.remove(index + counter);
                simPath.addtoPathAt((int) (pathX + diffX), (int) (pathY + diffY), dire, index + counter);
                counter++;
            }

        }


    }


    public boolean nextActionIsTurn(int oldDir, int newDir) {

        if (oldDir == 6) {
            if (newDir == 8) {
                return true;
            }
            if (newDir == 2) {
                return true;
            }
        }
        if (oldDir == 4) {
            if (newDir == 8) {
                return true;
            }
            if (newDir == 2) {
                return true;
            }
        }

        if (oldDir == 2) {
            if (newDir == 4) {
                return true;
            }
            if (newDir == 6) {
                return true;
            }
        }
        if (oldDir == 8) {
            if (newDir == 4) {
                return true;
            }
            if (newDir == 6) {
                return true;
            }
        }
        return false;

    }


    //TODO Fix this. (when hitting a stop button this doesent stop the car
    public void stopCarAnimation() {

        System.out.println("CAR ANIMATION STOPPED!");
        car.setVel(0);
        animationTimer.stop();

    }

    private int checkDirection(int oldX, int oldY, int newX, int newY) {

        //north
        if (oldX == newX && oldY < newY) {
            System.out.println("Direction is: " + 2);
            return 2;
        }
        //south
        if (oldX == newX && oldY > newY) {
            System.out.println("Direction is: " + 8);
            return 8;
        }
        //east
        if (oldX > newX && oldY == newY) {
            //turn east
            System.out.println("Direction is: " + 4);
            return 4;
        }
        //west
        if (oldX < newX && oldY == newY) {
            System.out.println("Direction is: " + 6);
            return 6;
        }


        return 0;

    }

    public ImageView getAnimatedCar() {
        return this.imgView;
    }

    public Car getBackendCar() {
        return this.car;
    }

}
