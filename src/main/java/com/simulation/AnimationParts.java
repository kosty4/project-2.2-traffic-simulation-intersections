package com.simulation;

import com.backend.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimationParts {


    ArrayList<carAnimation> carElements;

    ArrayList<TrafficLight> trafficLights;

    ArrayList<FSMTrafficLight> FSMTrafficLights;

    ArrayList<Node> intersectionNodes;


    //controls the speed changes of each car and traffic light controll.
    Model model;
    Graph graph;
    Board board;

    CollisionDetection collisionDetection;

    public AnimationParts(Graph graph, Board board) {
        this.intersectionNodes = new ArrayList<>();

        this.carElements = new ArrayList<>();
        this.trafficLights = new ArrayList<>();

        this.graph = graph;
        this.board = board;

        this.model = new Model(graph);

        this.collisionDetection = new CollisionDetection();
    }


    public ArrayList<carAnimation> getCarElements(){
        return this.carElements;
    }

    //input a car object, comute the path here.
    public void addCarToAnimation(int start, int end, int pathFindingMode) {

        if (pathFindingMode == 1) {
            ArrayList<Integer> IntPath = getRouteGreedy(start, end);

            System.out.println("dir 1" );
            Car car = new Car(graph.getNodeByIndex(IntPath.get(0)), graph.getNodeByIndex(IntPath.get(IntPath.size() - 1)),0, this.model.map);
            car.setPath(IntPath);

            collisionDetection.addCar(car);

            carAnimation carAnim = new carAnimation(this.graph, this.board, this.model, car, collisionDetection);
            carElements.add(carAnim);
        }

        if (pathFindingMode == 2) {
            Car car = new Car(graph.getNodeByIndex(start), graph.getNodeByIndex(end),0, this.model.map);

            ArrayList<Integer> IntPath = getRouteAStar(car);
            car.setPath(IntPath);

            collisionDetection.addCar(car);

            carAnimation carAnim = new carAnimation(this.graph, this.board, this.model, car, collisionDetection);
            carElements.add(carAnim);
        }

    }

    public void simulate() {

        for (int i = 0; i < this.carElements.size(); i++) {
            this.carElements.get(i).animationTimer.start();

            if(this.carElements.get(i).car.startTime == 0){

                this.carElements.get(i).car.startTime = System.currentTimeMillis();
            }
        }

    }
    public void simulateSingleCar(Car car){

        for (int i = 0; i < this.carElements.size(); i++) {
            if(carElements.get(i).car == car){
                this.carElements.get(i).animationTimer.start();
                this.carElements.get(i).car.startTime = System.currentTimeMillis();
            }

        }

    }

    public void stopSimulate() {
        for (int i = 0; i < this.carElements.size(); i++) {
            this.carElements.get(i).animationTimer.stop();
        }
    }

    public int getWeightOnGivenRoad(Road road, int percentage) {

        int temp = 0;
        for (int i = 0; i < this.carElements.size(); i++) {

            if (this.carElements.get(i).getBackendCar().getLocRoad().start == road.start &&
                    this.carElements.get(i).getBackendCar().getLocRoad().end == road.end &&
                    this.carElements.get(i).getBackendCar().getPercentageOnCurrentRoad() >= percentage) {

                temp++;

            }
        }

        return temp;
    }

    //startSegment and endSegment are start and end of road percentage segment
    public int getWeightOnGivenRoadSegment(Road road, int startSegment, int endSegment){

        int temp = 0;
        for (int i = 0; i < this.carElements.size(); i++) {

            if (this.carElements.get(i).getBackendCar().getLocRoad().start == road.start &&
                    this.carElements.get(i).getBackendCar().getLocRoad().end == road.end &&

                    this.carElements.get(i).getBackendCar().getPercentageOnCurrentRoad() >= startSegment &&
                    this.carElements.get(i).getBackendCar().getPercentageOnCurrentRoad() <= endSegment) {

                temp++;

            }
        }

        return temp;
    }

    public HashMap<Integer, Integer> getRoadWeights(int percentage) {

        // Create a hashmap and add the amount of cars to the corresponding index of the road + add the weight to the road itself to a variable

        //key is edge.gei(i), value is weight on those edges at current moment
        HashMap<Integer, Integer> hmap = new HashMap<>();

        for (int i = 0; i < model.map.roads.size(); i++) {
            hmap.put(i, 0);

            model.map.roads.get(i).carsAtEndOfRoad = 0;
        }


        for (int i = 0; i < this.carElements.size(); i++) {
            for (int j = 0; j < model.map.roads.size(); j++) {

                if (this.carElements.get(i).getBackendCar().getLocRoad().start == model.map.roads.get(j).start &&
                        this.carElements.get(i).getBackendCar().getLocRoad().end == model.map.roads.get(j).end &&
                        this.carElements.get(i).getBackendCar().getPercentageOnCurrentRoad() >= percentage) {

                    hmap.put(j, hmap.get(j) + 1);

                    // add it to the reoads as well
                    model.map.roads.get(j).carsAtEndOfRoad = hmap.get(j);

                }
            }
        }

        return hmap;

    }


    public ArrayList<Integer> getRouteGreedy(int start, int end) {
        Greedy greedy = new Greedy(graph.getNodeByIndex(start), graph.getNodeByIndex(end), graph);

        return greedy.getIntPath();
    }

    public ArrayList<Integer> getRouteAStar(Car car) {
        Pathfinding path = new Pathfinding(graph);
        return path.Astar(car, graph);
    }


    public ArrayList<TrafficLight> getTrafficLights() {
        return this.trafficLights;
    }

    public ArrayList<FSMTrafficLight> getFSMTrafficLights() {
        return this.FSMTrafficLights;
    }


    //return a list of cars that are driving at a given road
    public ArrayList<Car> carsOnRoad(Road road) {
        return null;
    }

    //return the arraylist of roads from map
    public ArrayList<Road> getRoads() {
        return this.model.map.roads;
    }

}
