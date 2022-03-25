package com.simulation;

//import backend.Map;
//import backend.Model;
//import backend.TrafficLightController;
import com.backend.Map;
import com.backend.Model;
import com.backend.TrafficLightController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Created by 123 on 31.05.2018.
 */
public class MainController {

    public final int NORTH = 8;
    public final int SOUTH = 2;
    public final int EAST = 6;
    public final int WEST = 4;
    public int mode;
    boolean temp;
    private TrafficLightController trafficLightController;
    private GreedyController greedyController;
    private Map map;
    private Model model;
    private double greenTime;
    private AnimationParts animationParts;

    public MainController(AnimationParts animationParts, int greenTime, int mode) {

        this.animationParts = animationParts;
        this.map = animationParts.model.map;
        this.model = animationParts.model;
        this.greenTime = greenTime;

        this.mode = mode;

        if (mode == 1) {

            for (int i = 0; i < animationParts.intersectionNodes.size(); i++) {
                this.greedyController = new GreedyController(this.animationParts.model.map, this.animationParts.model, this.animationParts.intersectionNodes.get(i),
                        this.greenTime, this);
            }

        }

        if (mode == 2) {

            System.out.println("intersection fsm size " + animationParts.model.map.intersectionFSM.size());
            for (int i = 0; i < animationParts.model.map.intersectionFSM.size(); i++) {
                this.trafficLightController = new TrafficLightController(this.animationParts.model.map, this.animationParts.model, this.animationParts.intersectionNodes.get(i), this.animationParts.model.map.intersectionFSM.get(i).intersection.index,  5000, 10000);
            }

//            this.trafficLightController = new TrafficLightController(this.animationParts.model.map, this.animationParts.model, this.animationParts.intersectionNodes.get(i), 5000, 10000);
//
//  for (int i = 0; i < animationParts.model.map.intersectionFSM.size(); i++) {
//                this.trafficLightController = new TrafficLightController(this.animationParts.model.map, this.animationParts.model, this.animationParts.intersectionNodes.get(i), 5000, 10000);
//
//
//            }
        }

        updateLeftBooleans();
    }


    public void updateLeftBooleans() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), ev -> {

            checkCase();

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    public void checkCase() {

        for (int i = 0; i < model.map.intersectionFSM.size(); i++) {
            if (leftCheck().length > 2 && temp) {

                if (model.map.intersectionFSM.get(leftCheck()[0]).intersection == model.map.intersectionFSM.get(i).intersection) {

                    //leftcheck method returns following: [0,2,0]
                    //return 3 values: 1) intersection number 2) intersection side that will work green 3) number of cars that want to turn left?

                    if (leftCheck()[1] == WEST) {

                        model.map.intersectionFSM.get(i).LeftTurn_West = true;
//                        model.map.intersectionFSM.get(i).LeftTurn_West_Time = getCarsNumberTurningLeft(leftCheck()[0],WEST);
                        model.map.intersectionFSM.get(i).LeftTurn_West_Time = getCarsNumberPassingIntersection(leftCheck()[0],WEST);

                    }
                    if (leftCheck()[1] == EAST) {

                        model.map.intersectionFSM.get(i).LeftTurn_East = true;
//                        model.map.intersectionFSM.get(i).LeftTurn_East_Time = getCarsNumberTurningLeft(leftCheck()[0],EAST);
                        model.map.intersectionFSM.get(i).LeftTurn_East_Time = getCarsNumberPassingIntersection(leftCheck()[0],EAST);

                    }
                    if (leftCheck()[1] == SOUTH) {
                        model.map.intersectionFSM.get(i).LeftTurn_South = true;
//                        model.map.intersectionFSM.get(i).LeftTurn_South_Time = getCarsNumberTurningLeft(leftCheck()[0],SOUTH);
                        model.map.intersectionFSM.get(i).LeftTurn_South_Time = getCarsNumberPassingIntersection(leftCheck()[0],SOUTH);
                    }
                    if (leftCheck()[1] == NORTH) {
                        model.map.intersectionFSM.get(i).LeftTurn_North = true;
//                        model.map.intersectionFSM.get(i).LeftTurn_North_Time = getCarsNumberTurningLeft(leftCheck()[0],NORTH);
                        model.map.intersectionFSM.get(i).LeftTurn_North_Time = getCarsNumberPassingIntersection(leftCheck()[0],NORTH);
                    }


                    temp = false;

                }
            } else {
                temp = true;
            }
        }
    }

    //TODO WRITE A METHOD TO RETURN A CAR AT THE END OF THE ROAD AT GIVEN INTERSECTION
    //return 3 values: 1) intersection number 2) intersection side that will work green 3) number of cars that want to turn left?
    public int[] leftCheck() {

        for (int i = 0; i < model.map.intersectionFSM.size(); i++) {
            for (int j = 0; j < animationParts.carElements.size(); j++) {

                if (model.map.intersectionFSM.get(i).intersection == animationParts.carElements.get(j).car.getLocRoad().end &&
                        animationParts.carElements.get(j).car.getPercentageOnCurrentRoad() >= 50) {

                    if (isALeftTurn(animationParts.carElements.get(j).car.getCurentDirection(),
                            getDirection2Nodes(animationParts.carElements.get(j).car.getPath().get(animationParts.carElements.get(j).pathIterator),
                                    animationParts.carElements.get(j).car.getPath().get(animationParts.carElements.get(j).pathIterator + 1)))) {

                        if (animationParts.carElements.get(j).car.getCurentDirection() == 6) {
                            return new int[]{i, WEST, 0};
                        }
                        if (animationParts.carElements.get(j).car.getCurentDirection() == 4) {
                            return new int[]{i, EAST, 0};
                        }
                        if (animationParts.carElements.get(j).car.getCurentDirection() == 2) {
                            return new int[]{i, NORTH, 0};
                        }
                        if (animationParts.carElements.get(j).car.getCurentDirection() == 8) {
                            return new int[]{i, SOUTH, 0};
                        }

                    }

                }

            }
        }

        int[] out = new int[]{};

        return out;
    }
// different version checking only left turning cars...
    private int getCarsNumberTurningLeft(int intersectionIndex, int position) {
        int increment = 0;

        for (int i = 0; i < animationParts.carElements.size(); i++) {

            if (model.map.intersectionFSM.get(intersectionIndex).intersection == animationParts.carElements.get(i).car.getLocRoad().end &&
                    animationParts.carElements.get(i).car.getPercentageOnCurrentRoad() >= 50) {

                if (isALeftTurn(animationParts.carElements.get(i).car.getCurentDirection(),
                        getDirection2Nodes(animationParts.carElements.get(i).car.getPath().get(animationParts.carElements.get(i).pathIterator),
                                animationParts.carElements.get(i).car.getPath().get(animationParts.carElements.get(i).pathIterator + 1)))
                        && position == getOppositeDirection(animationParts.carElements.get(i).car.getCurentDirection())) {
                    increment++;
                }
            }
        }

        return increment;
    }

    private int getCarsNumberPassingIntersection (int intersectionIndex, int position) {
        int increment = 0;
        for (int i = 0; i < animationParts.carElements.size(); i++) {

            if (model.map.intersectionFSM.get(intersectionIndex).intersection == animationParts.carElements.get(i).car.getLocRoad().end &&
                    animationParts.carElements.get(i).car.getPercentageOnCurrentRoad() >= 50) {

                if ( position == getOppositeDirection(animationParts.carElements.get(i).car.getCurentDirection())) {
                    increment++;
                }

            }
        }

        return increment;
    }



    private int getOppositeDirection(int temp){
        if(temp == 6){
            return 4;
        }
        if(temp == 4){
            return 6;
        }
        if(temp == 8) {
            return 2;
        }
        if(temp == 2){
            return 8;
        }
        return -1;
    }

    private boolean isALeftTurn(int previousDirection, int newDirection) {

        if (previousDirection == 2 && newDirection == 6) {
            return true;
        } else if (previousDirection == 4 && newDirection == 2) {
            return true;
        } else if (previousDirection == 6 && newDirection == 8) {
            return true;
        } else if (previousDirection == 8 && newDirection == 4) {
            return true;
        }

        return false;
    }

    private int getDirection2Nodes(int one, int two) {
        return model.graph.getEdgeByIndexes(one, two).direction;
    }

    public AnimationParts getAnimationParts() {
        return this.animationParts;
    }

    public TrafficLightController getTLCcontroller() {
        return this.trafficLightController;
    }

    public GreedyController getGreedyController() {
        return this.greedyController;
    }

}
