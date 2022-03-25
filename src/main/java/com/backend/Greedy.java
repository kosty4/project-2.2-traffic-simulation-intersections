package com.backend;


import com.simulation.Graph;
import com.simulation.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandersalahmadibapost on 29/03/2018.
 */
public class Greedy {

    private ArrayList<Node> path = new ArrayList<Node>();
    private ArrayList<Boolean> visited = new ArrayList<Boolean>();

    public Greedy(Node start, Node end, Graph graph)
    {
        for (int i = 0; i < graph.getNodes().size(); i++) {

            visited.add(false);
        }

        System.out.println("path size before: " + path.size());
        path.add(start);

        System.out.println("path size after: " + path.size());
        visited.set(start.getIndex(), true);

        List<Node> neighbours = graph.getAdjecents(start);
        System.out.println("adjecents for start node " + getInt(neighbours));

        int index = 0;
        double max = 1000000;
        boolean loop = true;

        while (loop) {

            System.out.println("path size after: " + path.size());
            if(path.size() > 0){
                if(path.get(path.size() - 1) != end){

                    for (int i = 0; i < neighbours.size(); i++) {
                        if (calcPytho(neighbours.get(i), end) < max && !visited.get(neighbours.get(i).getIndex())) {
                            max = calcPytho(neighbours.get(i), end);
                            index = i;

                        }
                    }

                    if(!visited.get(neighbours.get(index).getIndex())) {
                        path.add(neighbours.get(index));
                        visited.set(neighbours.get(index).getIndex(), true);
                        neighbours = graph.getAdjecents(neighbours.get(index));
                    } else {

                        if(path.size() > 0) {
                            visited.set(path.get(path.size() - 1).getIndex(), true);
                            path.remove(path.get(path.size() - 1));
                        } else {
                            return;
                        }
                    }
                } else{
                    System.out.println("Route found to end node");
                    loop = false;
                }
            } else {
                System.out.println("start node doesnt exist");
                return;
            }

        }
    }

    public ArrayList<Node> getPath(){
        return path;
    }


    public ArrayList<Integer> getIntPath(){

        ArrayList<Integer> output = new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {
            output.add(path.get(i).getIndex());
        }

        return output;
    }

    public double calcPytho(Node start, Node end) {

        return Math.sqrt(Math.pow((start.getXpos() - end.getXpos()), 2) + (Math.pow(start.getYpos() - end.getYpos(), 2)));
    }

    public ArrayList<Integer> getInt(List<Node> current){
        ArrayList<Integer> integers = new ArrayList<>();

        for (int i = 0; i < current.size(); i++) {
            integers.add(current.get(i).index);
        }

        return integers;
    }
}

