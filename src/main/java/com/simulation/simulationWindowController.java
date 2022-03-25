package com.simulation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import java.io.FileInputStream;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.swing.*;
//import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class simulationWindowController {

    Board simulationBoard;
    ArrayList<Double> roadsAverages = new ArrayList<>();
    ArrayList<Double> intersectionAverage = new ArrayList<>();
    ArrayList<String> carData = new ArrayList<>();
    int carIter = 0;
    ArrayList<Double> totalTimeArray = new ArrayList<>();
    ArrayList<Double> timeAtIntersection = new ArrayList<>();
    ArrayList<Double> averageSpeedsArray = new ArrayList<>();
    ArrayList<Double> proportionOfTimeAtIntersectionOverTravelTime = new ArrayList<>();
    @FXML
    private ScrollPane simulationScrollpane;
    @FXML
    private Group simulationElements;
    @FXML
    private Button createCarbtn, runSimbtn, stopSimbtn, debugbtn, launchCars;
    @FXML
    private TextField StartInput, EndInput, numberCarsForSim;
    @FXML
    private CheckBox GreedySelector, AStarselector;
    private AnimationTimer animationTimer;
    private Graph graph;

    //TODO PASSING THE FILENAME
    private String filename;
    private Scene infoPanelScene;
    private Pane infoPanePane;
    private MainController mainController;
    private int PathfindingMode;
    private AnimationParts animationParts;
    private int carStart;


    //This class creates the traffic lights and adds them to gui,
    // then we connect the traffic lights FSM cycles via connectFSM() method called in animationParts class
    private int carEnd;

    public static int getPoissonRandom(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }

    public void initialize() {
        PathfindingMode = 1;

        XMLLoader graphLoader = new XMLLoader("configs/graph5");
        graph = graphLoader.getGraph();

        javafx.scene.image.Image img = new Image("file:static/background.JPG", 800, 800, false, false);
        ImageView imgView = new ImageView(img);
        simulationElements.getChildren().add(imgView);

        simulationBoard = new Board(15, 15);

        simulationBoard.setBoard(graph);

        simulationElements.getChildren().add(simulationBoard);


        //===============================ANIMATION==========================

        //create the parent class for animation of all cars and traffic lights
        animationParts = new AnimationParts(this.graph, this.simulationBoard);

        //create and connect traffic lights (add to gui + backend)
        createTrafficLights();

        //refresh the value of cars on each road after specified time
        roadStatusUpdater(1000);

        //update the speed array
        carSpeedUpdater(500);

        //mode 1 - Greedy, mode 2 - TLC
        this.mainController = new MainController(this.animationParts, 10000, 1);

        this.animationParts.model.map.runAllConnectedFSMS();

        if (this.mainController.mode == 2) {
            updateCycleSander();

        }

        simulationScrollpane.setContent(simulationElements);


        handleMouseClickOnCars();

    }

    private void handleMouseClickOnCars() {

        this.simulationElements.setOnMouseClicked(event -> {

            System.out.println("Clicked on position " + event.getSceneX() + "   " + event.getSceneY());
            for (int i = 0; i < animationParts.carElements.size(); i++) {

                if (event.getTarget() == animationParts.carElements.get(i).getAnimatedCar()) {
                    String infoPanel;
                    infoPanel = "Car number: " + i + " Direction: " + animationParts.carElements.get(i).car.getCurentDirection()
                            + "   Velocity: " + animationParts.carElements.get(i).car.getVel() + "   Local road: " + animationParts.carElements.get(i).car.getLocRoad();
                    JOptionPane.showMessageDialog(null, infoPanel);
                }

            }

        });
    }

    @FXML
    public void debugbtnaction() {

//        System.out.println("Currently running on lane " + this.animationParts.carElements.get(0).car.getLocEdge());

//        this.animationParts.carElements.get(0).changeLane(1, this.animationParts.carElements.get(0).getBackendCar().getCurentDirection());

//        ArrayList<Double> avgQueueAtIntersection = new ArrayList<>();
//
//        for (int i = 0; i < 4; i++) {
//
//            avgQueueAtIntersection.add(calculateAverageINT(this.animationParts.model.map.intersectionFSM.get(0).getAllFSMRoads().get(i).numberOfCarsStandingInQueue));
//
//        }

        ArrayList<Node> startEndPoints = new ArrayList<>();

        for (int i = 0; i < animationParts.model.map.roads.size(); i++) {

            if (animationParts.model.map.getIncomingRoads(animationParts.getRoads().get(i).start).size() <= 1 &&
                    animationParts.model.map.getOutgoingRoads(animationParts.getRoads().get(i).start).size() <= 1) {

                startEndPoints.add(animationParts.getRoads().get(i).start);
            }
        }




        for (int i = 0; i < startEndPoints.size(); i++) {

            System.out.println("Start endPoints "+ startEndPoints.get(i).index);
            launchCarsSimIndividualIntersection(startEndPoints.get(i).index, 6, startEndPoints);
        }


    }

    public void updateCycleSander() {


        Timeline timeline = new Timeline(new KeyFrame(Duration.millis((int) (mainController.getTLCcontroller().getGTime() * 2) + 4000), ev -> {

            mainController.getTLCcontroller().updateCycle();

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    private double calculateAverage(ArrayList<Double> speeds) {
        Double sum = 0.0;
        if (!speeds.isEmpty()) {
            for (int i = 0; i < speeds.size(); i++) {
                if (speeds.get(i).toString() != "NaN") {
                    sum += speeds.get(i);
                }


            }

            return sum / speeds.size();
        }
        return sum;
    }

    private double calculateAverageLong(ArrayList<Long> speeds) {

        Long sum = 0L;

        if (!speeds.isEmpty()) {
            for (int i = 0; i < speeds.size(); i++) {
                sum += speeds.get(i);

            }

            return sum / speeds.size();
        }
        return sum;
    }

    private double calculateAverageINT(ArrayList<Integer> speeds) {
        Double sum = 0.0;

        if (!speeds.isEmpty()) {
            for (int i = 0; i < speeds.size(); i++) {
                sum += speeds.get(i);

            }

            return sum / speeds.size();
        }
        return sum;
    }

    public void createTrafficLights() {

        //cases: T- Section, Cross-Intersection
        for (int i = 0; i < animationParts.getRoads().size(); i++) {

            //check if more than one road is directed towards an intersection => create traffic lights at the end
            //Cross-intersection...
            if (animationParts.model.map.getIncomingRoads(animationParts.getRoads().get(i).end).size() == 4) {

                if (!animationParts.intersectionNodes.contains(animationParts.getRoads().get(i).end)) {
                    animationParts.intersectionNodes.add(animationParts.getRoads().get(i).end);
                }

                if (animationParts.getRoads().get(i).getDirection() == 6) {

                    animationParts.getRoads().get(i).addTrafficLight(graph.edges.get(i).end.x * simulationBoard.SIM_SIZE - 70, graph.edges.get(i).end.y * simulationBoard.SIM_SIZE + 70,
                            5000, 3000, 1500, 1);
                    this.simulationElements.getChildren().add(animationParts.getRoads().get(i).getTrafficLight().getTrafficLightGui());
                } else if (animationParts.getRoads().get(i).getDirection() == 4) {
                    animationParts.getRoads().get(i).addTrafficLight(graph.edges.get(i).end.x * simulationBoard.SIM_SIZE + 130, graph.edges.get(i).end.y * simulationBoard.SIM_SIZE - 50,
                            5000, 3000, 1500, 1);
                    this.simulationElements.getChildren().add(animationParts.getRoads().get(i).getTrafficLight().getTrafficLightGui());
                } else if (animationParts.getRoads().get(i).getDirection() == 8) {

                    animationParts.getRoads().get(i).addTrafficLight(graph.edges.get(i).end.x * simulationBoard.SIM_SIZE + 70, graph.edges.get(i).end.y * simulationBoard.SIM_SIZE + 130,
                            5000, 3000, 1500, 1);
                    this.simulationElements.getChildren().add(animationParts.getRoads().get(i).getTrafficLight().getTrafficLightGui());
                } else if (animationParts.getRoads().get(i).getDirection() == 2) {
                    animationParts.getRoads().get(i).addTrafficLight(graph.edges.get(i).end.x * simulationBoard.SIM_SIZE, graph.edges.get(i).end.y * simulationBoard.SIM_SIZE - 130,
                            5000, 3000, 1500, 1);
                    this.simulationElements.getChildren().add(animationParts.getRoads().get(i).getTrafficLight().getTrafficLightGui());

                }
            }

            //T-Section
            if (animationParts.model.map.getIncomingRoads(animationParts.getRoads().get(i).end).size() == 3) {

                if (animationParts.getRoads().get(i).getDirection() == 6) {
                    animationParts.getRoads().get(i).addTrafficLight(graph.edges.get(i).end.x * simulationBoard.SIM_SIZE, graph.edges.get(i).end.y * simulationBoard.SIM_SIZE,
                            5000, 3000, 1500, 1);
                    this.simulationElements.getChildren().add(animationParts.getRoads().get(i).getTrafficLight().getTrafficLightGui());
                } else if (animationParts.getRoads().get(i).getDirection() == 4) {
                    animationParts.getRoads().get(i).addTrafficLight(graph.edges.get(i).end.x * simulationBoard.SIM_SIZE, graph.edges.get(i).end.y * simulationBoard.SIM_SIZE,
                            5000, 3000, 1500, 1);
                    this.simulationElements.getChildren().add(animationParts.getRoads().get(i).getTrafficLight().getTrafficLightGui());
                } else if (animationParts.getRoads().get(i).getDirection() == 8) {

                    animationParts.getRoads().get(i).addTrafficLight(graph.edges.get(i).end.x * simulationBoard.SIM_SIZE + 70, graph.edges.get(i).end.y * simulationBoard.SIM_SIZE + 130,
                            5000, 3000, 1500, 1);
                    this.simulationElements.getChildren().add(animationParts.getRoads().get(i).getTrafficLight().getTrafficLightGui());
                } else if (animationParts.getRoads().get(i).getDirection() == 2) {
                    animationParts.getRoads().get(i).addTrafficLight(graph.edges.get(i).end.x * simulationBoard.SIM_SIZE, graph.edges.get(i).end.y * simulationBoard.SIM_SIZE - 130,
                            5000, 3000, 1500, 1);
                    this.simulationElements.getChildren().add(animationParts.getRoads().get(i).getTrafficLight().getTrafficLightGui());

                }

            }
        }

        this.animationParts.model.connectFSM();


    }

    public void createTrafficLightsManual() {

        animationParts.model.map.roads.get(0).addTrafficLight(graph.edges.get(0).end.x * 100 - 50, graph.edges.get(0).end.y * 100 + 50, 15000, 6000, 1000, 1);
        this.simulationElements.getChildren().add(animationParts.model.map.roads.get(0).getTrafficLight().getTrafficLightGui());

    }

    @FXML
    public void createCar() {

        System.out.println("Car created");
        this.carStart = Integer.parseInt(StartInput.getText());
        this.carEnd = Integer.parseInt(EndInput.getText());
        this.animationParts.addCarToAnimation(carStart, carEnd, PathfindingMode);

        int lastCar = this.animationParts.carElements.size() - 1;

        this.simulationElements.getChildren().add(this.animationParts.carElements.get(lastCar).getAnimatedCar());
    }

    public void launchCarsSimIndividualIntersection(int index, int numberCarsToGenerate, ArrayList<Node> startEndPoints) {

        System.out.println("Launching cars from " + index);

        int fixedStart = index;

        //determine the range for the random selector

        //int numberOfCarsToSimulate = Integer.valueOf(numberCarsForSim.getText());

        ArrayList<Integer> endPositionsIndexes = new ArrayList<>();
        ArrayList<Integer> interArrivals = new ArrayList<>();

        double lambda = 5;

        //will return a value in index of the array of start/ending points
        Random random = new Random();

        int counter = 0;

        int min = 0;
        int max = startEndPoints.size() -1;

        for (int i = 0; i < numberCarsToGenerate; i++) {

            int randEnd = random.nextInt(max - min + 1) + min;

            if (fixedStart != startEndPoints.get(randEnd).index) {

                counter++;

                endPositionsIndexes.add(startEndPoints.get(randEnd).index);

                //inter arrival times...
                int rand = getPoissonRandom(lambda);


                int temp = 60000 / (rand);

                if (temp < 8000) {
                    temp = 8000;
                }

                if(temp > 20000){
                    temp = 20000;
                }

                interArrivals.add(temp);
                System.out.println("TIME IS:  " + temp);

            }
            else if (i != 0) {
                i--;
            }
        }

        System.out.println("EndPositionsIndexes :" + endPositionsIndexes);

        if (counter < numberCarsToGenerate) {

            int randEnd = random.nextInt(max - min + 1) + min;
            if (fixedStart != randEnd) {

                endPositionsIndexes.add(startEndPoints.get(randEnd).index);
                //inter arrival times...
                int rand = getPoissonRandom(lambda);
                int temp = 60000 / (rand);

                if (temp < 5000) {
                    temp = 5000;
                }

                interArrivals.add(temp);
            }

        }

        System.out.println("Inter arivals size " + interArrivals.size());



        //launch timer to start cars each 2 seconds....
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(interArrivals.get(interArrivals.size() -1)), ev -> {


            if(endPositionsIndexes.size() - 1 >= 0 && fixedStart != endPositionsIndexes.get(endPositionsIndexes.size() - 1)){

                this.animationParts.addCarToAnimation(fixedStart, endPositionsIndexes.get(endPositionsIndexes.size() - 1), PathfindingMode);

                int lastCar = this.animationParts.carElements.size() - 1;
                this.simulationElements.getChildren().add(this.animationParts.carElements.get(lastCar).getAnimatedCar());

                animationParts.simulateSingleCar(this.animationParts.carElements.get(lastCar).car);

                interArrivals.remove(interArrivals.size() - 1);
                endPositionsIndexes.remove(endPositionsIndexes.size() - 1);
            }

        }));

        timeline.setCycleCount(numberCarsToGenerate);
        timeline.play();

    }


    public void launchCarsSim() {

        ArrayList<Node> startEndPoints = new ArrayList<>();

        for (int i = 0; i < animationParts.model.map.roads.size(); i++) {

            if (animationParts.model.map.getIncomingRoads(animationParts.getRoads().get(i).start).size() <= 1 &&
                    animationParts.model.map.getOutgoingRoads(animationParts.getRoads().get(i).start).size() <= 1) {

                startEndPoints.add(animationParts.getRoads().get(i).start);

            }
        }

        //determine the range for the random selector
        int min = 0;
        int max = startEndPoints.size() - 1;

        int numberOfCarsToSimulate = Integer.valueOf(numberCarsForSim.getText());

        ArrayList<Integer> startPositionsIndexes = new ArrayList<>();
        ArrayList<Integer> endPositionsIndexes = new ArrayList<>();
        ArrayList<Integer> interArrivals = new ArrayList<>();
        double lambda = 11;

        //will return a value in index of the array of start/ending points
        Random random = new Random();

        for (int i = 0; i < numberOfCarsToSimulate; i++) {

            int randStart = random.nextInt(max - min + 1) + min;
            int randEnd = random.nextInt(max - min + 1) + min;

            if (randStart != randEnd) {

                startPositionsIndexes.add(startEndPoints.get(randStart).index);
                endPositionsIndexes.add(startEndPoints.get(randEnd).index);

            } else if (i != 0) {
                i--;
            }
            int rand = getPoissonRandom(lambda);
            int temp = 60000 / (rand);

            if (temp < 5000) {
                temp = 5000;
            }

            interArrivals.add(temp);
            System.out.println("TIME IS:  " + temp);

        }

        if (startPositionsIndexes.size() == endPositionsIndexes.size()) {
            //launch timer to start cars each 2 seconds....
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(interArrivals.get(interArrivals.size() - 1)), ev -> {

                System.out.println("Creating a new car with start at " + startPositionsIndexes.get(startPositionsIndexes.size() - 1) + " and end at : " +
                        endPositionsIndexes.get(startPositionsIndexes.size() - 1));
                this.animationParts.addCarToAnimation(startPositionsIndexes.get(startPositionsIndexes.size() - 1), endPositionsIndexes.get(startPositionsIndexes.size() - 1)
                        , PathfindingMode);
                int lastCar = this.animationParts.carElements.size() - 1;
                this.simulationElements.getChildren().add(this.animationParts.carElements.get(lastCar).getAnimatedCar());
                startPositionsIndexes.remove(startPositionsIndexes.size() - 1);
                animationParts.simulate();
                interArrivals.remove(interArrivals.size() - 1);


            }));

            timeline.setCycleCount(numberOfCarsToSimulate);
            timeline.play();

        }

    }

    @FXML
    public void runSimulation() {
        animationParts.simulate();
    }

    @FXML
    public void stopSimulation() {
        double temp1 = 0;
        double temp2 = 0;
        double temp3 = 0;
        double temp4 = 0;

        animationParts.stopSimulate();

        System.out.println("========================SIMULATION RESULTS=====================");

        for (int i = 0; i < carData.size(); i++) {
            System.out.println(carData.get(i));
        }

        System.out.println("=============================Avrages===========================");
        System.out.println("Average total travel time for all cars : " + calculateAverage(totalTimeArray));
        System.out.println("Average time at intersection : " + calculateAverage(timeAtIntersection));
        System.out.println("Average travel speed for all cars : " + calculateAverage(averageSpeedsArray));
        System.out.println("Average waiting time to total travel time ratio : " + calculateAverage(proportionOfTimeAtIntersectionOverTravelTime));


        for (int i = 0; i < this.animationParts.model.map.intersectionFSM.size(); i++) {

            for (int j = 0; j < 4; j++) {

                roadsAverages.add(calculateAverageINT(this.animationParts.model.map.intersectionFSM.get(i).getAllFSMRoads().get(j).numberOfCarsStandingInQueue));

            }

            intersectionAverage.add(calculateAverage(roadsAverages));
            roadsAverages = new ArrayList<>();

        }

        System.out.println("Average queue size for intersection " + calculateAverage(intersectionAverage));


    }

    @FXML
    public void selectGreedy() {
        AStarselector.setSelected(false);
        GreedySelector.setSelected(true);
        PathfindingMode = 1;
    }

    @FXML
    public void selectAstar() {
        GreedySelector.setSelected(false);
        AStarselector.setSelected(true);
        PathfindingMode = 2;
    }

    public void roadStatusUpdater(int period) {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(period), ev -> {

            deleteCarAtDestination();
            roadWeightUpdate();

            //TODO For all intersections....
            //given a node index, where the intersection is dislocated, update the variable in each road which stores
//            the amount of cars at the end of those roads(intersections)
            for (int i = 0; i < this.animationParts.model.map.intersectionFSM.size(); i++) {

                updateQueueAtIntersection(this.animationParts.model.map.intersectionFSM.get(i).intersection.index);
            }


            for (int i = 0; i < this.animationParts.model.map.intersectionFSM.size(); i++) {
                for (int j = 0; j < 4; j++) {

                    this.animationParts.model.map.intersectionFSM.get(i).getAllFSMRoads().get(j).numberOfCarsStandingInQueue.add(
                            this.animationParts.model.map.intersectionFSM.get(i).getAllFSMRoads().get(j).carsAtEndOfRoad);

                }
            }

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public void updateQueueAtIntersection(int intersectionIndex) {

        for (int i = 0; i < animationParts.model.map.roads.size(); i++) {

            int temp = 0;

            for (int j = 0; j < animationParts.carElements.size(); j++) {

                if (this.animationParts.model.map.roads.get(i).end.index == intersectionIndex &&
                        animationParts.carElements.get(j).car.getVel() < 0.2
                        && animationParts.model.map.roads.get(i) == animationParts.carElements.get(j).car.getLocRoad()) {

                    temp++;

                }
            }

            animationParts.model.map.roads.get(i).updateCarsAtEndOfRoad(temp);
        }
    }

    public void carSpeedUpdater(int period) {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(period), ev -> {

            for (int i = 0; i < animationParts.carElements.size(); i++) {

                animationParts.carElements.get(i).car.speedsArray.add(animationParts.carElements.get(i).car.getVel());
            }

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    private void deleteCarAtDestination() {

        for (int i = 0; i < animationParts.carElements.size(); i++) {

            if (animationParts.carElements.get(i).car.destinationReached) {

                carIter++;

                this.simulationElements.getChildren().remove(animationParts.carElements.get(i).getAnimatedCar());
                this.animationParts.collisionDetection.cars.remove(animationParts.carElements.get(i).getBackendCar());


                double elapsed = animationParts.carElements.get(i).car.getElapsedTimeTotal() / 1000;
                carData.add("Car number " + carIter + " :  Seconds to reach the goal: " + animationParts.carElements.get(i).car.getElapsedTimeTotal() / 1000);
                totalTimeArray.add(elapsed);

                double intersectTime = animationParts.carElements.get(i).car.totalTimeAtIntersections / 1000;
                carData.add("Total time spent at an intersection : " + animationParts.carElements.get(i).car.totalTimeAtIntersections / 1000);
                timeAtIntersection.add(intersectTime);

                carData.add("Average speed of that car is : " + calculateAverage(animationParts.carElements.get(i).car.speedsArray));
                averageSpeedsArray.add(calculateAverage(animationParts.carElements.get(i).car.speedsArray));

                double proportion = intersectTime / elapsed;

                carData.add("Proportion of waiting time to intersection to total travel time is " + proportion * 100 + "  % ");
                carData.add("------------------------------------------------------------");


                proportionOfTimeAtIntersectionOverTravelTime.add(proportion * 100);

                //System.out.println("Removing a car that took " + animationParts.carElements.get(i).car.getElapsedTimeTotal() + "  seconds to reach destination");
//                System.out.println("It took time at intersection " + animationParts.carElements.get(i).car.totalTimeAtIntersections);
//                System.out.println("Average speed of that car is " + calculateAverage(animationParts.carElements.get(i).car.speedsArray));

                animationParts.carElements.remove(i);

            }
        }
    }


    public void roadWeightUpdate() {
        this.animationParts.getRoadWeights(30);
    }


    //doesent work, the initilize method always triggers first.
    public void setFilename(String filename) {
        this.filename = filename;
    }

   /* public void speedLabelUpdate(double newVal){
        speedVariable.setText(handAnimation.getCarSpeed() + " km/h");
    }

    */



}

