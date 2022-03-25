package com.backend;


import com.simulation.TrafficLight;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.util.Duration;
//import simulation.TrafficLight;

import java.util.Timer;
import java.util.TimerTask;

public class FSMTrafficLight {


    public final int RED = 1;
    public final int YELLOW = 2;
    public final int GREEN = 3;

    public TrafficLight trafficLightGui;

    public int currentstate;

    //1 - red, 2 - yellow, 3- green

    //1 > 3 > 2 > 1 > 3 > 2
    // Red > Green > Yellow > Red > Green > Yellow > ...


    public FSMTrafficLight(int currentstate, int XPos, int YPos) {

        this.trafficLightGui = new TrafficLight(XPos, YPos);

        this.currentstate = currentstate;
    }

    public void setCurrentstate(int newstate){

        this.currentstate = newstate;
        if(newstate == RED){
            setRed();
        }

        if(newstate == YELLOW){
            setYellow();
        }

        if(newstate == GREEN){
            setGreen();
        }

    }


    public void setRed() {
        //check...
        currentstate = RED;
        this.trafficLightGui.changeTrafficLightColor(RED);

    }

    public void setGreen() {
        //check...
        currentstate = GREEN;
        this.trafficLightGui.changeTrafficLightColor(GREEN);

    }


    public void setYellow() {

        currentstate = YELLOW;
        this.trafficLightGui.changeTrafficLightColor(YELLOW);
    }

    public int getCurrentstate() {
        return currentstate;
    }


    public Group getTrafficLightGui() {
        return this.trafficLightGui.getTrafficlight();
    }


}
