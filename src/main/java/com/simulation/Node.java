package com.simulation;

//import backend.Intersection;
//import backend.SubIntersection;

import com.backend.Intersection;
import com.backend.SubIntersection;

public class Node {

    public String name;
    public double Xpos,Ypos;
    public int index;
    public int type = 0;
    public int x, y;
	public Intersection intersection;
	boolean left, up, right, down;


	public Node(String name, int Xpos, int Ypos){
        this.name = name;
        this.Xpos = Xpos;
        this.Ypos = Ypos;

    }

    public Node(double Xpos, double Ypos){
        this.Xpos = Xpos;
        this.Ypos = Ypos;

    }

	public Node(int index, double Xpos, double Ypos){
		this.Xpos = Xpos;
		this.Ypos = Ypos;

		this.index = index;
	}
    public Node(int index, double Xpos, double Ypos, int x, int y){
        this.Xpos = Xpos;
        this.Ypos = Ypos;
		this.x = x;
		this.y = y;
        this.index = index;
    }
	public Node(int index, double Xpos, double Ypos, int x, int y, int type){
		this.Xpos = Xpos;
		this.Ypos = Ypos;
		this.x = x;
		this.y = y;
		this.index = index;
		this.type = type;
		System.out.println("Type = " + type);
		createIntersections();

	}
	public void createIntersections() {
		this.intersection = new Intersection();
	}

	public void createIntersections(boolean left, boolean right, boolean up, boolean down)
	{
		this.intersection = new Intersection();

		if(left)
		{
			intersection.setLeft(new SubIntersection());
		}
		if(right)
		{
			intersection.setRight(new SubIntersection());
		}
		if(up)
		{
			intersection.setUp(new SubIntersection());
		}
		if(down)
		{
			intersection.setDown(new SubIntersection());
		}
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public double getXpos() {
		return Xpos;
	}

	public void setXpos(double xpos) {
		Xpos = xpos;
	}

	public double getYpos() {
		return Ypos;
	}

	public void setYpos(double ypos) {
		Ypos = ypos;
	}
    
    public void setIndex(int index){
		this.index = index;
	}

	public int getIndex(){
    	return  index;
	}



}
