package com.backend;

//import simulation.TrafficLight;

import java.util.ArrayList;

public class TrafficLightsModel {

    ArrayList<FSMTrafficLight> trafficLights;

    public TrafficLightsModel(){

        trafficLights = new ArrayList<>();

    }

    public void addTrafficLightsToModel(FSMTrafficLight light){
        this.trafficLights.add(light);
    }




}
