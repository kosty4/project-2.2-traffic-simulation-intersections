package com.backend;

//import simulation.Graph;

import com.simulation.Graph;

public class Model {

    public Graph graph;
    public Map map;



    public Model(Graph graph) {

        this.graph = graph;
        this.map = new Map(graph);

    }

    public void connectFSM() {

        int DefaultRedTime = 15000;

        int DefaultGreenTime = 8000;

        int DefaultYellowTime = 2000;


        for (int i = 0; i < map.roads.size(); i++) {
            for (int j = 0; j < map.roads.size(); j++) {

                //if 2 roads share the same vertex at their ends AND there exists a parrallel edge going out of that edge,
                // AND they are PARALLEL to each other, then let them share the same traffic light cycle.

                if (i != j && map.roads.get(i).existsTrafficLight() == true && map.roads.get(j).existsTrafficLight() == true &&
                        map.roads.get(i).end == map.roads.get(j).end
                        && map.exsistParallelOutgoingRoad(map.roads.get(i).getDirection(), map.roads.get(i).end) != null
                        && map.exsistParallelOutgoingRoad(map.roads.get(j).getDirection(), map.roads.get(j).end) != null
                        && map.RoadsFacingEachOther(map.roads.get(i), map.roads.get(j))) {

//                    map.roads.get(i).getTrafficLight().setTimingSequences(DefaultRedTime, DefaultGreenTime, DefaultYellowTime);
//                    map.roads.get(j).getTrafficLight().setTimingSequences(DefaultRedTime, DefaultGreenTime, DefaultYellowTime);

                    map.roads.get(i).setRoadWithSameFSM(map.roads.get(j));
                    map.roads.get(j).setRoadWithSameFSM(map.roads.get(i));

                }
                // if 2 edges share the same vertex at their ends AND one of them is perpendicular to another, then the FSM cycle of one edge will change
                //depending on the other

            }
        }

        for (int i = 0; i < graph.nodes.size(); i++) {

                if (map.getIncomingRoads(graph.nodes.get(i)).size() == 4) {

                    intersectionFSM crossSection = new intersectionFSM(map.getIncomingRoads(graph.nodes.get(i)));
                    map.intersectionFSM.add(crossSection);
                }

                if(map.getIncomingRoads(graph.nodes.get(i)).size() == 3){

                    intersectionFSM TSection = new intersectionFSM(map.getIncomingRoads(graph.nodes.get(i)));
                    map.intersectionFSM.add(TSection);

                }
        }

    }


    public double desiredVelocity(double maxSpeed, int speedLimit, double obeyFactor) {

        return Math.min(maxSpeed, speedLimit * obeyFactor);
    }

    public double desiredVelocity(Car car) {

        car.setDesVel(desiredVelocity(car.getMaxVel(), 80, car.getObeyFactor()));
        return desiredVelocity(car.getMaxVel(), 80, car.getObeyFactor());
    }

    public double acceleration(Car car, double distCarFront, double carFrontVel) {

        car.setAcc(acceleration(car.getMinimumSpacing(), desiredVelocity(car), car.getVel(), car.getExponent(), car.getTimeHeadway(), car.getVel() - carFrontVel, distCarFront, car.getMaxAcc(), car.getDesDec()));

        return acceleration(car.getMinimumSpacing(), desiredVelocity(car), car.getVel(), car.getExponent(), car.getTimeHeadway(), car.getVel() - carFrontVel, distCarFront, car.getMaxAcc(), car.getDesDec());
    }

    private double acceleration(int s0, double v0, double v, int e, int t, double dV, double s, double a, double b) {

        return a * (1 - Math.pow(v / v0, e) - Math.pow((s0 + Math.max(0, v * t + (v * dV) / (2 * Math.sqrt(a * b)))) / s, 2));
    }


    public void startCycle() {
        for (int i = 0; i < map.roads.size(); i++) {

            if (map.roads.get(i).existsTrafficLight() == true) {

//                map.roads.get(i).getTrafficLight().runRed();
            }
        }
    }
}
