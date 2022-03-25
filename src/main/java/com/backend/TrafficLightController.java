package com.backend;

import com.simulation.Node;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
//import simulation.Graph;
//import simulation.Node;

import java.util.ArrayList;

/**
 * Created by sandersalahmadibapost on 29/05/2018.
 */

public class TrafficLightController {

    private double g;
    private double gTime;
    private int rangeFactor;
    private Map map;
    private Model model;
    private int bestQ;
    private ArrayList<Integer> bestQueueDiff;
    private ArrayList<Integer> fixedRange;
    public Node intersection;
    private int cycleCounter;
    private int rangeCounter;
    private int determineCycle;
    private double bestGTime;

    private int intersectionFSMIndex;

    public TrafficLightController(Map map, Model model, Node node, int intersectionFSMIndex, int rangeFactor, double greenTime) {
        this.map = map;
        this.intersectionFSMIndex = intersectionFSMIndex;

        this.model = model;
        this.rangeFactor = rangeFactor;
        this.intersection = node;
        cycleCounter = 0;
        g = greenTime;
        gTime = 10000;
        bestQueueDiff = new ArrayList<>();
        fixedRange = new ArrayList<>();
        fixedRange.add(-5000);
        fixedRange.add(-4000);
        fixedRange.add(-3000);
        fixedRange.add(-2000);
        fixedRange.add(-1000);
        fixedRange.add(1000);
        fixedRange.add(2000);
        fixedRange.add(3000);
        fixedRange.add(4000);
        fixedRange.add(5000);
        bestQueueDiff.add(-1);
        bestQueueDiff.add(-1);
        bestQueueDiff.add(-1);
        bestQueueDiff.add(-1);
        bestQueueDiff.add(-1);
        rangeCounter = -5;
        this.determineCycle = 0;

    }

    public void updateCycle() {
        System.out.println("Update cycle Method exec");
        gTime = g;
//        random??
        double gDiff =(int) ((rangeFactor * 2) *( Math.random())) - rangeFactor;
//         fixed
//        double gDiff = fixedRange.get(determineCycle);
        System.out.println("G before " + g);
       // double gDiff = rangeCounter*1000;
        System.out.println("G Diff " + gDiff);
        rangeCounter++;

        // update green time
        if(determineCycle < 11) {
            gTime = gTime + gDiff;
            System.out.println("G + G this " + gTime);
        }

        //calculate the queue before the cycle
        ArrayList<Integer> qBefore = caLculateQueue(intersection);

        //run cycle here

//        this.model.map.intersectionFSM.get(0).setHorizontalRed((int) gTime, false);

//        this.model.map.intersectionFSM.get(0).runFSM_Horizontal_Red();

        this.model.map.intersectionFSM.get(intersectionFSMIndex).setHorizontalRed((int)g, false);

//
        System.out.println("DELAY START ");

//        Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(1000), ev -> {
//
//            System.out.println("Currently cars on the intersection " + caLculateQueue(model.graph.getNodeByIndex(1)));
//
//        }));

//        timeline2.setCycleCount(Animation.INDEFINITE);
//        timeline2.play();


        Timeline timeline = new Timeline(new KeyFrame(Duration.millis((int) gTime * 2 + 4000), ev -> {

            System.out.println("IN The Delay ");

            //calculate the queue after the cycle
            ArrayList<Integer> qAfter = caLculateQueue(intersection);

            ArrayList<Integer> qDiff = new ArrayList();
            //store cycle reference
            qDiff.add(cycleCounter);
            cycleCounter++;
            if (qAfter.size()!= 0||qBefore.size()!=0){
                //calculate the difference in the queues
                for (int i = 1; i < qAfter.size(); i++) {
                    qDiff.add(qAfter.get(i) - qBefore.get(i));
                }
            }

            if (qDiff.size() != 1) {
                //check if new cycle is more optimal than current
                optimumChooser(qDiff);
            }

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        System.out.println("AFTER DELAY");

    }

    // in this method we should calculate the queues on every road at the intersection
    public ArrayList<Integer> caLculateQueue(Node intersection) {

        //int j = 0;
        ArrayList<Integer> q = new ArrayList();

        //calculate the queues on each road at an intersection
        for (int i = 0; i < map.roads.size(); i++) {
            if (map.roads.get(i).end == intersection) {

                System.out.println("QUEUE SIZE " + map.roads.get(i).carsAtEndOfRoad);
                q.add(map.roads.get(i).carsAtEndOfRoad);
                // j++;
            }
        }
        return q;
    }

    public void optimumChooser(ArrayList<Integer> contestant) {

        determineCycle++;
        double chooser = 0;
        System.out.println("Contestant " );
        if(determineCycle < 11) {
            if (bestQueueDiff.size() != 0 && bestQueueDiff.size() != 1) {
                for (int i = 0; i < contestant.size()-1; i++) {
                    chooser = chooser + (contestant.get(i + 1) - bestQueueDiff.get(i + 1));
                    System.out.println("IN THE FOR LOOP NOW!!!!!!!!!!!!");
                }

                if (chooser > 0) {
                    bestQueueDiff = contestant;
                    bestQ = contestant.get(0);
                    bestGTime = gTime;
                    System.out.println("GTIME FOR TLC CONTROLLER: " + gTime);

                }
            }
        }
        else{
            gTime = bestGTime;
            System.out.println("GTIME FOR TLC CONTROLLER: " + gTime);
        }


    }

    public int getRangeFactor() {
        return rangeFactor;
    }

    public void setRangeFactor(int i) {
        rangeFactor = i;
    }

    public double getGTime() {
        return gTime;
    }
}
