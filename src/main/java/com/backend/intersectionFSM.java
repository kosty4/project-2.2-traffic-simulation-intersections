package com.backend;

import com.simulation.Node;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
//import simulation.Main;
//import simulation.MainController;
//import simulation.Node;

import java.util.ArrayList;

public class intersectionFSM {

    public final int CROSS_SECTION = 1;
    public final int T_SECTION = 2;

    public final int NORTH = 8;
    public final int SOUTH = 2;
    public final int EAST = 6;
    public final int WEST = 4;

    public final int YELLOW_TIME = 2000;
    final int MAX_ALLOWED_GREEN = 16000;
    final int MIN_ALLOWED_GREEN = 8000;
    final int MAX_ALLOWED_RED = 18000;
    final int MIN_ALLOWED_RED = 10000;

    final int RED = 1;
    final int YELLOW = 2;
    final int GREEN = 3;

    public Node intersection;
    public int intersectionType;
    public int RED_HORIZONTAL_TIME;
    public int GREEN_HORIZONTAL_TIME;
    public int RED_VERTICAL_TIME;
    public int GREEN_VERTICAL_TIME;

    public boolean LeftTurn_East;
    public boolean LeftTurn_West;
    public boolean LeftTurn_North;
    public boolean LeftTurn_South;

    public int LeftTurn_West_Time;
    public int LeftTurn_East_Time;
    public int LeftTurn_North_Time;
    public int LeftTurn_South_Time;

    Road horizontal1, horizontal2;
    Road vertical1, vertical2;
    Map map;
    ArrayList<Road> unstructured;

    intersectionState intersectionState;

    Timeline timeCheckBeforeDecision;
    Timeline timeCheckBeforeDecision_2;


    boolean passedToNextState = false;
    boolean passedToNextState_2 = false;


    public intersectionFSM(ArrayList<Road> input) {

        this.LeftTurn_East = false;
        this.LeftTurn_West = false;
        this.LeftTurn_North = false;
        this.LeftTurn_South = false;

        this.LeftTurn_West_Time = 0;
        this.LeftTurn_East_Time = 0;
        this.LeftTurn_North_Time = 0;
        this.LeftTurn_South_Time = 0;

        this.unstructured = input;
        this.intersection = input.get(0).end;

        //regular cross-intersection
        if (unstructured.size() == 4) {

            for (int i = 0; i < 4; i++) {

                System.out.println("DIRECTION " + input.get(i).getDirection());

                if (unstructured.get(i).getDirection() == 6) {
                    this.horizontal1 = unstructured.get(i);
                }
                if (unstructured.get(i).getDirection() == 4) {
                    this.horizontal2 = unstructured.get(i);
                }
                if (unstructured.get(i).getDirection() == 2) {
                    this.vertical1 = unstructured.get(i);
                }
                if (unstructured.get(i).getDirection() == 8) {
                    this.vertical2 = unstructured.get(i);
                }
            }
            this.intersectionType = CROSS_SECTION;
        }
        //T-Section
        if (unstructured.size() == 3) {

            for (int i = 0; i < 3; i++) {
                if (unstructured.get(i).getDirection() == 6) {
                    this.horizontal1 = unstructured.get(i);
                }
                if (unstructured.get(i).getDirection() == 4) {
                    this.horizontal2 = unstructured.get(i);
                }
                if (unstructured.get(i).getDirection() == 2) {
                    this.vertical1 = unstructured.get(i);
                }
                if (unstructured.get(i).getDirection() == 8) {
                    this.vertical2 = unstructured.get(i);
                }
            }
            this.intersectionType = T_SECTION;
        }

        this.intersectionState = new intersectionState(RED, RED, GREEN, GREEN);


        this.RED_HORIZONTAL_TIME = 10000;
        this.RED_VERTICAL_TIME = 10000;

        this.GREEN_HORIZONTAL_TIME = 12000;
        this.GREEN_VERTICAL_TIME = 12000;

        //initial start state.
        Red_H_Green_V();

        //run the automatic updater
        automaticStateUpdater();
    }

    public intersectionFSM(Node intersection, Map map) {

        this.intersection = intersection;
        this.map = map;
        unstructured = new ArrayList<>();

        connect();
    }

    public void automaticStateUpdater() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(250), ev -> {
            updateTrafficLights();
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void Red_H_Green_V() {
        System.out.println("Currently running Red H Green V");

        passedToNextState_2 = false;
        Timeline mainLine;

        this.intersectionState.Set_Red_Horizontal_Green_Vertical();

        this.timeCheckBeforeDecision_2 = new Timeline(new KeyFrame(Duration.millis(1000), ev -> {

            if (LeftTurn_South) {
                LeftTurn_South = false;
                passedToNextState_2 = true;

                Green_South_Yellow_North();

                this.timeCheckBeforeDecision_2.stop();
                this.timeCheckBeforeDecision_2 = null;
            }

            else if (LeftTurn_North) {
                LeftTurn_North = false;
                passedToNextState_2 = true;

                Green_North_Yellow_South();

                this.timeCheckBeforeDecision_2.stop();
                this.timeCheckBeforeDecision_2 = null;

            }
        }));

        this.timeCheckBeforeDecision_2.setCycleCount(Math.round(RED_HORIZONTAL_TIME/1000));
        this.timeCheckBeforeDecision_2.play();


        mainLine = new Timeline(new KeyFrame(Duration.millis(RED_HORIZONTAL_TIME), ev -> {

            if (!passedToNextState_2) {
                Red_H_Yellow_V();

                if (timeCheckBeforeDecision_2 != null) {
                    timeCheckBeforeDecision_2.stop();
                    this.timeCheckBeforeDecision_2 = null;
                }
            }
        }));

        mainLine.setCycleCount(1);
        mainLine.play();
    }

    public void Red_H_Yellow_V() {

        System.out.println("Currently running Red H Yellow V");

        this.intersectionState.Set_Red_Horizontal_Yellow_Vertical();

        System.out.println("car comming from west wants to turn left? " + LeftTurn_West);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {
            //a cr that turns left
            if (LeftTurn_West) {

                Green_West_Red_All();
                LeftTurn_West = false;
            }
            else if (LeftTurn_East) {

                Green_East_Red_All();
                LeftTurn_East = false;
            }

            else {
                //else go continue normal cycle
                Green_H_Red_V();
            }

        }));

        timeline.setCycleCount(1);
        timeline.play();

    }


    public void Yellow_West_Red_All() {

        System.out.println("Currently running Yellow West Red all");

        LeftTurn_West = false;

        this.intersectionState.Set_Yellow_West_Only();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {

            Red_H_Green_V();

        }));

        timeline.setCycleCount(1);
        timeline.play();
    }

    public void Yellow_East_Red_All() {

        System.out.println("Currently running Yellow East Red all");

        LeftTurn_East = false;

        this.intersectionState.Set_Yellow_East_Only();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {

            Red_H_Green_V();

        }));

        timeline.setCycleCount(1);
        timeline.play();
    }

    public void Yellow_South_Red_All() {

        System.out.println("Currently running Yellow South Red all");

        LeftTurn_South = false;

        this.intersectionState.Set_Yellow_South_Only();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {

            Green_H_Red_V();

        }));

        timeline.setCycleCount(1);
        timeline.play();
    }

    public void Yellow_North_Red_All() {

        System.out.println("Currently running Yellow North Red all");

        LeftTurn_North = false;

        this.intersectionState.Set_Yellow_North_Only();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {

            Green_H_Red_V();
        }));

        timeline.setCycleCount(1);
        timeline.play();
    }


    public void Green_H_Red_V() {

        System.out.println("Currently running Green H Red V");

        passedToNextState = false;
        Timeline mainLine;

        this.intersectionState.Set_Green_Horizontal_Red_Vertical();

        //loop to triger the next state as soon as it sees a car turning left
        this.timeCheckBeforeDecision = new Timeline(new KeyFrame(Duration.millis(1000), ev -> {

            if (LeftTurn_West) {
                LeftTurn_West = false;
                passedToNextState = true;

                Green_West_Yellow_East();
                this.timeCheckBeforeDecision.stop();
                this.timeCheckBeforeDecision = null;
            }

            else if (LeftTurn_East) {

                LeftTurn_East = false;
                passedToNextState = true;

                Green_East_Yellow_West();

                this.timeCheckBeforeDecision.stop();
                this.timeCheckBeforeDecision = null;
            }
        }));

        this.timeCheckBeforeDecision.setCycleCount(Math.round(GREEN_HORIZONTAL_TIME/1000));
        this.timeCheckBeforeDecision.play();


        mainLine = new Timeline(new KeyFrame(Duration.millis(GREEN_HORIZONTAL_TIME), ev -> {

            if (!passedToNextState) {

                Yellow_H_Red_V();

                if (timeCheckBeforeDecision != null) {
                    timeCheckBeforeDecision.stop();
                    this.timeCheckBeforeDecision = null;
                }
            }

        }));

        mainLine.setCycleCount(1);
        mainLine.play();

    }


    public void Green_West_Yellow_East() {

        System.out.println("Currently running Green West Yellow East");

        this.intersectionState.Set_Green_West_Yellow_East();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {

            Green_West_Red_All();

        }));

        timeline.setCycleCount(1);
        timeline.play();

    }

    public void Green_East_Yellow_West() {

        System.out.println("Currently running Green East Yellow West");

        this.intersectionState.Set_Green_East_Yellow_West();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {

            Green_East_Red_All();

        }));

        timeline.setCycleCount(1);
        timeline.play();

    }

    //this
    public void Green_South_Yellow_North() {

        System.out.println("Currently running Green South Yellow north");

        this.intersectionState.Set_Green_South_Yellow_North();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {

            Green_South_Red_All();

        }));

        timeline.setCycleCount(1);
        timeline.play();

    }

    public void Green_North_Yellow_South() {

        System.out.println("Currently running Green North Yellow South");

        this.intersectionState.Set_Green_North_Yellow_South();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {

            Green_North_Red_All();

        }));

        timeline.setCycleCount(1);
        timeline.play();

    }

    public void Green_West_Red_All() {

        System.out.println("Currently running Green West Red all");

        this.intersectionState.Set_Green_West_Only();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(LeftTurn_West_Time * 5000), ev -> {
            Yellow_West_Red_All();
        }));

        timeline.setCycleCount(1);
        timeline.play();

    }

    public void Green_East_Red_All() {
        System.out.println("Currently running Green East Red all");

        this.intersectionState.Set_Green_East_Only();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(LeftTurn_East_Time * 5000), ev -> {

            Yellow_East_Red_All();

        }));

        timeline.setCycleCount(1);
        timeline.play();

    }

    public void Green_South_Red_All() {

        System.out.println("Currently running Green South Red all");

        this.intersectionState.Set_Green_South_Only();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(LeftTurn_South_Time * 5000), ev -> {
            Yellow_South_Red_All();
        }));

        timeline.setCycleCount(1);
        timeline.play();

    }

    public void Green_North_Red_All() {

        System.out.println("Currently running Green North Red all");

        this.intersectionState.Set_Green_North_Only();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(LeftTurn_North_Time * 5000), ev -> {
            Yellow_North_Red_All();
        }));

        timeline.setCycleCount(1);
        timeline.play();
    }


    public void Yellow_H_Red_V() {

        System.out.println("Currently running Yellow H Red V");

        this.intersectionState.Set_Yellow_Horizontal_Red_Vertical();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(YELLOW_TIME), ev -> {

            if (LeftTurn_South) {
                Green_South_Red_All();
                LeftTurn_South = false;

            }
            else if(LeftTurn_North){
                Green_North_Red_All();
                LeftTurn_North = false;
            }

            else {
                Red_H_Green_V();
            }

        }));

        timeline.setCycleCount(1);
        timeline.play();
    }

    public void updateTrafficLights() {

        this.horizontal1.getTrafficLight().setCurrentstate(this.intersectionState.currentStateWest);
        this.horizontal2.getTrafficLight().setCurrentstate(this.intersectionState.currentStateEast);
        this.vertical1.getTrafficLight().setCurrentstate(this.intersectionState.currentStateNorth);
        this.vertical2.getTrafficLight().setCurrentstate(this.intersectionState.currentStateSouth);


    }

//    public void runFSMforLeftTurn(int place, int time) {
//
//        if (intersectionType == CROSS_SECTION) {
//
//            if (place == NORTH) {
//
//                this.horizontal1.getTrafficLight().runYellow();
//                this.horizontal2.getTrafficLight().runYellow();
//                this.vertical1.getTrafficLight().runYellow();
//
//                this.vertical2.getTrafficLight().runGreen();
//            }
//            if (place == SOUTH) {
//
//                this.horizontal1.getTrafficLight().runYellow();
//                this.horizontal2.getTrafficLight().runRed();
//                this.vertical1.getTrafficLight().runGreen();
//
//                this.vertical2.getTrafficLight().runRed();
//            }
//            if (place == WEST) {
//
////                //if the others sides have currently green - make sure they first turn yellow and then red! when red -> then green
//                if (this.horizontal1.getTrafficLight().currentstate == RED && this.horizontal2.getTrafficLight().currentstate == RED) {
//
//                    this.horizontal1.getTrafficLight().runGreenAfterDelay(this.horizontal1.getTrafficLight().yellowTime);
//                    this.horizontal2.getTrafficLight().runRed();
//                    this.vertical1.getTrafficLight().runYellow();
//                    this.vertical2.getTrafficLight().runYellow();
//
//                    runFSM_Horizontal_Red_Delay(4000);
//
//                }
//
////                 if (this.horizontal1.getTrafficLight().currentstate == GREEN && this.horizontal2.getTrafficLight().currentstate == GREEN) {
////
////                    this.horizontal1.getTrafficLight().runGreen();
////                    this.horizontal2.getTrafficLight().runYellow();
////
////                    this.vertical1.getTrafficLight().runRed();
////                    this.vertical2.getTrafficLight().runRed();
////
////
////                    runFSM_Horizontal_Red_Delay(4000);
////                }
//
//
//
//            }
//
//            if (place == EAST) {
//
//                if (this.horizontal1.getTrafficLight().currentstate == RED && this.horizontal2.getTrafficLight().currentstate == RED) {
//
//                    this.horizontal1.getTrafficLight().runRed();
//                    this.horizontal2.getTrafficLight().runGreenAfterDelay(this.horizontal1.getTrafficLight().yellowTime);
//                    this.vertical1.getTrafficLight().runYellow();
//                    this.vertical2.getTrafficLight().runYellow();
//
//                    runFSM_Horizontal_Red_Delay(4000);
//
//                }
//
////                else if (this.horizontal1.getTrafficLight().currentstate == GREEN && this.horizontal2.getTrafficLight().currentstate == GREEN) {
////
////                    this.horizontal1.getTrafficLight().runYellow();
////                    this.horizontal2.getTrafficLight().runGreen();
////
////                    this.vertical1.getTrafficLight().runRed();
////                    this.vertical2.getTrafficLight().runRed();
////
////                    runFSM_Horizontal_Red_Delay(4000);
////                }
//            }
//        }
//
//    }


//    private void runFSM_Horizontal_Red_Delay(int delay) {
//
//            System.out.println("Gonna run a method in 3 seconds");
//
//            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delay), ev -> {
//                System.out.println("Setting back to default!");
//
//
//            this.horizontal1.getTrafficLight().stopTimeLines();
//            this.horizontal2.getTrafficLight().stopTimeLines();
//
//                setHorizontalRed(12000, true);
//                this.runFSM_Horizontal_Red();
//            }));
//
//            timeline.setCycleCount(1);
//            timeline.play();
//
//    }

    public void runFSM_Horizontal_Red() {

        if (intersectionType == CROSS_SECTION) {

            this.horizontal1.getTrafficLight().setRed();
            this.horizontal2.getTrafficLight().setRed();

            this.vertical1.getTrafficLight().setGreen();
            this.vertical2.getTrafficLight().setGreen();
        }

    }

    public void runFSM_Vertical_Red() {

        if (intersectionType == CROSS_SECTION) {
            this.vertical1.getTrafficLight().setRed();
            this.vertical2.getTrafficLight().setRed();

            this.horizontal1.getTrafficLight().setGreen();
            this.horizontal2.getTrafficLight().setGreen();

        }

    }

    public void setHorizontalRed(int Green, boolean newCurrentState) {

        if (intersectionType == CROSS_SECTION) {

            this.RED_HORIZONTAL_TIME = Green + YELLOW_TIME;
            this.RED_VERTICAL_TIME = Green + YELLOW_TIME;

            this.GREEN_HORIZONTAL_TIME = Green;
            this.GREEN_VERTICAL_TIME = Green;


            if (newCurrentState) {

                //1 - red, 2 - yellow, 3- green

                this.intersectionState.currentStateEast = RED;
                this.intersectionState.currentStateWest = RED;

                this.intersectionState.currentStateSouth = GREEN;
                this.intersectionState.currentStateNorth = GREEN;

            }
        }

        if (intersectionType == T_SECTION) {

        }


    }


//
//    public void setVerticalSequence(int Red, int Green) {
//
//        this.vertical1.getTrafficLight().setRedTime(Red);
//        this.vertical2.getTrafficLight().setRedTime(Red);
//        this.vertical1.getTrafficLight().setGreenTime(Green);
//        this.vertical2.getTrafficLight().setGreenTime(Green);
//
//    }
//
//    //TODO limit maximum green time , min green time, max red time, min red time
    public void moreGreenHorizontal(int increment) {

        int oldGreenTimeHorizontal = this.GREEN_HORIZONTAL_TIME;
        int oldRedTimeVertical = this.RED_VERTICAL_TIME;

        this. GREEN_HORIZONTAL_TIME = oldGreenTimeHorizontal + increment;

        this.RED_VERTICAL_TIME = oldRedTimeVertical + increment;
    }

    public void moreGreenVertical(int increment) {

        int oldGreenTimeHorizontal = this.GREEN_HORIZONTAL_TIME;
        int oldRedTimeVertical = this.RED_VERTICAL_TIME;
        int oldRedTimeHorizontal = this.RED_HORIZONTAL_TIME;
        int oldRedVertical = this.RED_VERTICAL_TIME;

        this.GREEN_VERTICAL_TIME = oldGreenTimeHorizontal + increment;
        this.RED_HORIZONTAL_TIME = oldRedTimeHorizontal + increment;

    }

    public void lessGreenHorizontal(int decrement) {

        int oldGreenTimeHorizontal = this.GREEN_HORIZONTAL_TIME;
        int oldRedTimeVertical = this.RED_VERTICAL_TIME;

        this.GREEN_HORIZONTAL_TIME = oldGreenTimeHorizontal - decrement;

        this.RED_VERTICAL_TIME = oldRedTimeVertical - decrement;

    }

    public void lessGreenVertical(int decrement) {

        int oldGreenTimeVertical = this.GREEN_VERTICAL_TIME;
        int oldRedTimeHorizontal = this.RED_HORIZONTAL_TIME;

        this.GREEN_VERTICAL_TIME = oldGreenTimeVertical - decrement;
        this.RED_HORIZONTAL_TIME = oldRedTimeHorizontal - decrement;

    }

    public void moreGreenLessRedHorizontal(int increment) {

        int oldGreenTimeVertical = this.GREEN_VERTICAL_TIME;
        int oldRedTimeHorizontal = this.RED_HORIZONTAL_TIME;
        int oldGreenTimeHorizontal = this.GREEN_HORIZONTAL_TIME;
        int oldRedTimeVertical = this.RED_VERTICAL_TIME;

        if (oldGreenTimeHorizontal + increment <= MAX_ALLOWED_GREEN) {
            this.GREEN_HORIZONTAL_TIME = oldGreenTimeHorizontal + increment;
//            this.horizontal1.getTrafficLight().setGreenTime(oldGreenTimeHorizontal + increment);
//            this.horizontal2.getTrafficLight().setGreenTime(oldGreenTimeHorizontal + increment);
        } else {
//            this.horizontal1.getTrafficLight().setGreenTime(MAX_ALLOWED_GREEN);
//            this.horizontal2.getTrafficLight().setGreenTime(MAX_ALLOWED_GREEN);
            this.GREEN_HORIZONTAL_TIME = MAX_ALLOWED_GREEN;
        }

        if (oldRedTimeHorizontal - increment >= MIN_ALLOWED_RED) {
            this.RED_HORIZONTAL_TIME = (oldRedTimeHorizontal - increment);
        } else {
            this.RED_HORIZONTAL_TIME = MIN_ALLOWED_RED;
        }

        if (oldGreenTimeVertical - increment >= MIN_ALLOWED_GREEN) {
            this.GREEN_VERTICAL_TIME = oldGreenTimeVertical - increment;
//            this.vertical1.getTrafficLight().setGreenTime(oldGreenTimeVertical - increment);
//            this.vertical2.getTrafficLight().setGreenTime(oldGreenTimeVertical - increment);
        } else {
            this.GREEN_VERTICAL_TIME = MIN_ALLOWED_GREEN;
//            this.vertical1.getTrafficLight().setGreenTime(MIN_ALLOWED_GREEN);
//            this.vertical2.getTrafficLight().setGreenTime(MIN_ALLOWED_GREEN);
        }

        if (oldRedTimeVertical + increment <= MAX_ALLOWED_RED) {
            this.RED_VERTICAL_TIME = oldRedTimeVertical + increment;
//            this.vertical1.getTrafficLight().setRedTime(oldRedTimeVertical + increment);
//            this.vertical2.getTrafficLight().setRedTime(oldRedTimeVertical + increment);
        } else {
            this.RED_VERTICAL_TIME = MAX_ALLOWED_RED;
//            this.vertical1.getTrafficLight().setRedTime(MAX_ALLOWED_RED);
//            this.vertical2.getTrafficLight().setRedTime(MAX_ALLOWED_RED);
        }


    }


    public void moreGreenLessRedVertical(int increment) {

        int oldGreenTimeVertical = this.GREEN_VERTICAL_TIME;
        int oldRedTimeHorizontal = this.RED_HORIZONTAL_TIME;
        int oldGreenTimeHorizontal = this.GREEN_HORIZONTAL_TIME;
        int oldRedTimeVertical = this.RED_VERTICAL_TIME;

//        this.horizontal1.getTrafficLight().setGreenTime(oldGreenTimeHorizontal - increment);
//        this.horizontal2.getTrafficLight().setGreenTime(oldGreenTimeHorizontal - increment);

        this.GREEN_HORIZONTAL_TIME = oldGreenTimeHorizontal - increment;

//        this.horizontal1.getTrafficLight().setRedTime(oldRedTimeHorizontal + increment);
//        this.horizontal2.getTrafficLight().setRedTime(oldRedTimeHorizontal + increment);

        this.RED_HORIZONTAL_TIME = oldRedTimeHorizontal + increment;

//        this.vertical1.getTrafficLight().setGreenTime(oldGreenTimeVertical + increment);
//        this.vertical2.getTrafficLight().setGreenTime(oldGreenTimeVertical + increment);
        this.GREEN_VERTICAL_TIME = oldGreenTimeVertical + increment;

//        this.vertical1.getTrafficLight().setRedTime(oldRedTimeVertical - increment);
//        this.vertical2.getTrafficLight().setRedTime(oldRedTimeVertical - increment);
        this.RED_VERTICAL_TIME = oldRedTimeVertical - increment;
    }


    public void connect() {

        unstructured = map.getIncomingRoads(intersection);


        for (int i = 0; i < unstructured.size(); i++) {

            if (unstructured.get(i).getDirection() == 6) {
                this.horizontal1 = unstructured.get(i);
            }
            if (unstructured.get(i).getDirection() == 4) {
                this.horizontal2 = unstructured.get(i);
            }
            if (unstructured.get(i).getDirection() == 2) {
                this.vertical1 = unstructured.get(i);
            }
            if (unstructured.get(i).getDirection() == 8) {
                this.vertical2 = unstructured.get(i);
            }
        }


    }

    public void connect2HorizontalRoads(Road a, Road b) {

        if ((a.getDirection() == 6 && b.getDirection() == 4) || (a.getDirection() == 4 && b.getDirection() == 6)) {
            this.horizontal1 = a;
            this.horizontal2 = b;
        }

        a.setRoadWithSameFSM(b);
        b.setRoadWithSameFSM(a);

    }

    public void connect2VerticalRoads(Road a, Road b) {

        if ((a.getDirection() == 8 && b.getDirection() == 2) || (a.getDirection() == 2 && b.getDirection() == 8)) {
            this.vertical1 = a;
            this.vertical2 = b;
        }

        a.setRoadWithSameFSM(b);
        b.setRoadWithSameFSM(a);
    }

    public ArrayList<Road> getAllFSMRoads() {

        ArrayList<Road> out = new ArrayList<>();

        if (horizontal1 != null && horizontal2 != null && vertical1 != null && vertical2 != null) {
            out.add(horizontal1);
            out.add(horizontal2);
            out.add(vertical1);
            out.add(vertical2);
        }

        return out;
    }

    public Node getIntersection() {
        return this.intersection;
    }

}
