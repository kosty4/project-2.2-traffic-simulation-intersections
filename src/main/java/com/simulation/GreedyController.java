package com.simulation;

//import backend.Map;
//import backend.Model;
//import backend.Road;

import com.backend.Map;
import com.backend.Model;
import com.backend.Road;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 123 on 31.05.2018.
 */
public class GreedyController {

    private final Node intersection;
    MainController parentController;
    Map map;
    Model model;
    double greenTime;

    int horizontalWeight;
    int verticalWeight;

    int fullcycleTime = 28000;

    //list of roads that are confronting the intersection. (share same endpoint at intersection)
    ArrayList<Road> roadArrayList;

    boolean temp = true;

    public GreedyController(Map map, Model model, Node intersection, double greenTime, MainController parentController) {

        this.parentController = parentController;

        this.map = map;
        this.model = model;
        this.intersection = intersection;
        this.greenTime = greenTime;

        this.horizontalWeight = 0;
        this.verticalWeight = 0;

        this.roadArrayList = model.map.getIncomingRoads(this.intersection);
        greedyTimer();
        updateFSMtimer();
    }

    private void manageTrafficLightSequences() {

        //vertical - more time
        if (verticalWeight > horizontalWeight) {
            //compare nubers??
            System.out.println("vertical more! ");
            int difference = verticalWeight - horizontalWeight;

            //TODO TEST DIFFERENT COMBOS
//            parentController.getAnimationParts().model.map.getCorrespondingFSM(this.intersection).moreGreenVertical(difference*1000);
//            parentController.getAnimationParts().model.map.getCorrespondingFSM(this.intersection).lessGreenHorizontal(difference*1000);
            parentController.getAnimationParts().model.map.getCorrespondingFSM(this.intersection).moreGreenLessRedVertical(difference * 1000);


            fullcycleTime = fullcycleTime + difference * 1000;
            horizontalWeight = -1;
            verticalWeight = 0;

        }

        //horizontal - more time
        if (verticalWeight < horizontalWeight) {
            System.out.println("horizontal more! ");
            //compare nubers??
            int difference = horizontalWeight - verticalWeight;

            //TODO TEST DIFFERENT COMBOS
//           parentController.getAnimationParts().model.map.getCorrespondingFSM(this.intersection).moreGreenHorizontal(difference*1000);
//            parentController.getAnimationParts().model.map.getCorrespondingFSM(this.intersection).lessGreenVertical(difference*1000);
            parentController.getAnimationParts().model.map.getCorrespondingFSM(this.intersection).moreGreenLessRedHorizontal(difference * 1000);


            fullcycleTime = fullcycleTime + difference * 1000;
            horizontalWeight = -1;
            verticalWeight = 0;

        }

        if (horizontalWeight == verticalWeight) {
            parentController.getAnimationParts().model.map.getCorrespondingFSM(this.intersection).setHorizontalRed(12000, false);
            fullcycleTime = 28000;
            System.out.println("Deafault set!");
        }
        else {
            horizontalWeight = 0;
            verticalWeight = 0;
        }

    }

    private void greedyTimer() {

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (parentController.getAnimationParts().carElements.size() > 0) {

                    GreedyCheckIntersection();

                    parentController.leftCheck();


                }
                ///!!!!!!!!!!!!!!!
//                GreedyCheckSensors();

//                System.out.println("Traffic light at Node " + intersection.index + " : Horizontal Green time-" + parentController.getAnimationParts().model.map.getCorrespondingFSM(intersection).getAllFSMRoads().get(0).getTrafficLight().greenTime +
//                        "  Red time-" + parentController.getAnimationParts().model.map.getCorrespondingFSM(intersection).getAllFSMRoads().get(0).getTrafficLight().redTime);
//
//                System.out.println("Traffic light at Node " + intersection.index + " : Vertical: Green time-" + parentController.getAnimationParts().model.map.getCorrespondingFSM(intersection).getAllFSMRoads().get(2).getTrafficLight().greenTime +
//                        "  Red time-" + parentController.getAnimationParts().model.map.getCorrespondingFSM(intersection).getAllFSMRoads().get(2).getTrafficLight().redTime);
//
            }
        }, 0, 1000);

    }

    private void updateFSMtimer() {

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                manageTrafficLightSequences();
            }
        }, 0, fullcycleTime);


    }

    private void GreedyCheckSensors() {

        int temp0 = this.parentController.getAnimationParts().getWeightOnGivenRoad(roadArrayList.get(0), 50);
        int temp1 = this.parentController.getAnimationParts().getWeightOnGivenRoad(roadArrayList.get(1), 50);
        int temp2 = this.parentController.getAnimationParts().getWeightOnGivenRoad(roadArrayList.get(2), 50);
        int temp3 = this.parentController.getAnimationParts().getWeightOnGivenRoad(roadArrayList.get(3), 50);

        boolean otherRoadsEmpty = true;

        if (temp0 > 0 || temp1 > 0) {
            parentController.getAnimationParts().model.map.getCorrespondingFSM(intersection).runFSM_Vertical_Red();
        } else if (temp2 > 0 || temp3 > 0) {
            parentController.getAnimationParts().model.map.getCorrespondingFSM(intersection).runFSM_Horizontal_Red();
        }


    }


    private void GreedyCheckIntersection() {

        int temp = 0;
        int temp1 = 0;
        int temp2 = 0;
        int temp3 = 0;

        for (int i = 0; i < roadArrayList.size(); i++) {

            if (this.parentController.getAnimationParts().getWeightOnGivenRoad(roadArrayList.get(i), 30) > 0) {


                if (this.roadArrayList.get(i).getDirection() == 6) {

                    temp = this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 30, 50) +
                            this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 50, 70) * 2 +
                            this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 70, 100) * 3;


                    for (int j = 0; j < roadArrayList.size(); j++) {

                        if (i != j && roadArrayList.get(j).getDirection() == 4 &&
                                roadArrayList.get(j).end == roadArrayList.get(i).end) {


                            temp1 = this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 30, 50) +
                                    this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 50, 70) * 2 +
                                    this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 70, 100) * 3;


                        }

                    }
                    horizontalWeight = temp + temp1;
//                    System.out.println("Horizontal weight " + horizontalWeight);

                } else if (this.roadArrayList.get(i).getDirection() == 4) {

                    temp = this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 30, 50) +
                            this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 50, 70) * 2 +
                            this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 70, 100) * 3;


                    for (int j = 0; j < roadArrayList.size(); j++) {

                        if (i != j && roadArrayList.get(j).getDirection() == 6 &&
                                roadArrayList.get(j).end == roadArrayList.get(i).end) {

                            temp1 = this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 30, 50) +
                                    this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 50, 70) * 2 +
                                    this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 70, 100) * 3;

                        }

                    }

                    horizontalWeight = temp + temp1;
//                    System.out.println("Horizontal weight " + horizontalWeight);

                } else if (this.roadArrayList.get(i).getDirection() == 2) {

                    temp2 = this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 30, 50) +
                            this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 50, 70) * 2 +
                            this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 70, 100) * 3;


                    for (int j = 0; j < roadArrayList.size(); j++) {

                        if (i != j && roadArrayList.get(j).getDirection() == 8 &&
                                roadArrayList.get(j).end == roadArrayList.get(i).end) {

                            temp3 = this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 30, 50) +
                                    this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 50, 70) * 2 +
                                    this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 70, 100) * 3;

                        }

                    }
                    verticalWeight = temp2 + temp3;
//                    System.out.println("Vertical weight " + verticalWeight);

                } else if (this.roadArrayList.get(i).getDirection() == 8) {

                    temp2 = this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 30, 50) +
                            this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 50, 70) * 2 +
                            this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(i), 70, 100) * 3;

                    for (int j = 0; j < roadArrayList.size(); j++) {

                        if (i != j && roadArrayList.get(j).getDirection() == 2 &&
                                roadArrayList.get(j).end == roadArrayList.get(i).end) {

                            temp3 = this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 30, 50) +
                                    this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 50, 70) * 2 +
                                    this.parentController.getAnimationParts().getWeightOnGivenRoadSegment(roadArrayList.get(j), 70, 100) * 3;

                        }

                    }
                    verticalWeight = temp2 + temp3;
//                    System.out.println("Vertical weight " + verticalWeight);

                }

            }
        }

    }

}
