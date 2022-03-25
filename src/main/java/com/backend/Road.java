package com.backend;

//import simulation.Edge;
//import simulation.Node;

import com.simulation.Edge;
import com.simulation.Node;

import java.util.ArrayList;

public class Road {

//	public Intersection beginning;
//	public Intersection end;

	public Node start;
	public Node end;

	public ArrayList<Edge> lanes;

	FSMTrafficLight trafficLight;

	//might be an array of trafficlights - here we keep whate
	public Road roadWithSameFSM;
	public int carsAtEndOfRoad;

	private int level;
	private int speedLimit;
	private int distance;
	private int direction;
	private boolean blocked;

	public ArrayList<Integer> numberOfCarsStandingInQueue;

	private boolean existsTrafficLight = false;

	public Road(ArrayList<Edge> edges){
		this.lanes = edges;

		//just take edge number 0 as refernece
		this.start = edges.get(0).start;
		this.end = edges.get(0).end;
		this.direction = edges.get(0).direction;

		this.carsAtEndOfRoad = 0;

		int deltaX = (int)start.Xpos - (int) end.Xpos;
		int deltaY = (int)start.Ypos - (int) end.Ypos;

		this.distance = (int) Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		this.numberOfCarsStandingInQueue = new ArrayList<>();

	}


	public void addTrafficLight(int XPos, int YPos, int redtime, int greentime, int yellowtime, int currentstate){

		this.trafficLight = new FSMTrafficLight(currentstate,XPos,YPos);
		existsTrafficLight = true;

	}

	public void addLane(Edge edge){
		this.lanes.add(edge);
	}


	public void setRoadWithSameFSM(Road otherRoad){
		roadWithSameFSM = otherRoad;
	}

	public Road getRoadWithSameFSM(){
		return this.roadWithSameFSM;
	}

	public FSMTrafficLight getTrafficLight(){

		return this.trafficLight;
	}

	public boolean existsTrafficLight(){

		return existsTrafficLight;
	}

	public void updateCarsAtEndOfRoad(int number){

		this.carsAtEndOfRoad = number;

	}

	public ArrayList<Edge> getLanes() {
		return lanes;
	}

	public int getDirection(){
		return this.direction;
	}

	public void setLanes(ArrayList<Edge> lanes) {
		this.lanes = lanes;
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}


	
}
