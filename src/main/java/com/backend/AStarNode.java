package com.backend;

import com.simulation.Node;

import java.util.ArrayList;
//import simulation.Node;

public class AStarNode {

	private ArrayList<AStarNode> neighbours = new ArrayList<AStarNode>();
	private AStarNode cameFrom;
	private double g_score;
	private double f_score;
	private double x;
	private double y;
	private int index;
	
	public AStarNode(Node n, int index){
	
		g_score = Double.MAX_VALUE;
		f_score = Double.MAX_VALUE;
		x = n.getXpos();
		y = n.getYpos();
		this.index = index;
	}


	public AStarNode getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(AStarNode cameFrom) {
		this.cameFrom = cameFrom;
	}
	
	public ArrayList<AStarNode> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(ArrayList<AStarNode> neighbours) {
		this.neighbours = neighbours;
	}

	public double getG_score() {
		return g_score;
	}

	public void setG_score(double g_score) {
		this.g_score = g_score;
	}

	public double getF_score() {
		return f_score;
	}

	public void setF_score(double f_score) {
		this.f_score = f_score;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setIndex(int index){
		this.index = index;
	}

	public int getIndex(){
		return index;
	}
	
}
