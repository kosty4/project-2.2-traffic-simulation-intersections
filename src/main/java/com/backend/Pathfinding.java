package com.backend;

//import simulation.Graph;

import com.backend.AStarNode;
import com.backend.Car;
import com.simulation.Graph;

import java.util.ArrayList;



public class Pathfinding {


    private ArrayList<AStarNode> newGraph = new ArrayList<AStarNode>();

    public Pathfinding(Graph graph) {

        for (int i = 0; i < graph.getNodes().size(); i++) {

            newGraph.add(new AStarNode(graph.getNodes().get(i), i));
        }

        for (int i = 0; i < graph.getNodes().size(); i++) {

            for (int j = 0; j < graph.getAdjecents(graph.getNodes().get(i)).size(); j++) {

                newGraph.get(i).getNeighbours().add(new AStarNode(graph.getAdjecents(graph.getNodes().get(i)).get(j), graph.getAdjecents(graph.getNodes().get(i)).get(j).getIndex()));
            }
        }
    }

    public ArrayList<Integer> Astar(Car car, Graph graph) {

        ArrayList<AStarNode> closedSet = new ArrayList<AStarNode>();
        ArrayList<AStarNode> openSet = new ArrayList<AStarNode>();

        int start = 0;

        for (int i = 0; i < graph.getNodes().size(); i++) {

            if (graph.getNodes().get(i) == car.getStart()) {

                start = i;
            }
        }

        openSet.add(newGraph.get(start));

        int end = 0;

        for (int i = 0; i < graph.getNodes().size(); i++) {

            if (graph.getNodes().get(i) == car.getEnd()) {

                end = i;
            }
        }

        newGraph.get(start).setG_score(0);
        newGraph.get(start).setF_score(calcPytho(newGraph.get(start), newGraph.get(end)));

        AStarNode current;

        while (!openSet.isEmpty()) {

            System.out.println("open set size " + openSet.size());
            int index = 0;
            double score = Double.MAX_VALUE;

            for (int i = 0; i < openSet.size(); i++) {

                if (openSet.get(i).getF_score() <= score) {
                    index = i;
                    score = openSet.get(i).getF_score();
                }
            }

            current = openSet.get(index);
            int in = current.getIndex();
            System.out.println("Neighbours of node: " + newGraph.get(in).getNeighbours().size() + "  index: " + current.getIndex());

            if (current.getIndex() == newGraph.get(end).getIndex()) {

                // return the path
                System.out.println("A* path found");
                return constructPath(current, newGraph.get(start));

            }

            openSet.remove(index);
            closedSet.add(current);

            for (int i = 0; i < newGraph.get(in).getNeighbours().size(); i++) {


                boolean closed = false;

                for (int j = 0; j < closedSet.size(); j++) {

                    if (closedSet.get(j) == newGraph.get(in).getNeighbours().get(i)) {

                        closed = true;
                    }
                }

                boolean exists = false;

                for (int j = 0; j < openSet.size(); j++) {

                    if (openSet.get(j) == newGraph.get(in).getNeighbours().get(i)) {

                        exists = true;
                    }
                }

                if (!exists && !closed) {

                    System.out.println("FOUND A NODE not in open or closed set");
                    openSet.add(newGraph.get(in).getNeighbours().get(i));

                    double tentative_gscore = current.getG_score() + calcPytho(current, newGraph.get(in).getNeighbours().get(i));

                    if (tentative_gscore < newGraph.get(in).getNeighbours().get(i).getG_score()) {

                        newGraph.get(in).getNeighbours().get(i).setCameFrom(current);
                        newGraph.get(in).getNeighbours().get(i).setG_score(tentative_gscore);
                        newGraph.get(in).getNeighbours().get(i).setF_score(newGraph.get(in).getNeighbours().get(i).getG_score() + calcPytho(newGraph.get(in).getNeighbours().get(i), newGraph.get(end)));
                    }
                }
            }
        }

        System.out.println("No A* path found");
        return null;
    }

    public ArrayList<Integer> constructPath(AStarNode current, AStarNode start) {

        ArrayList<Integer> path = new ArrayList<>();

        path.add(current.getIndex());


        while (current.getCameFrom() != start) {
            current = current.getCameFrom();
            path.add(0, current.getIndex());
        }

        path.add(0, start.getIndex());

        return path;
    }


    public double calcPytho(AStarNode start, AStarNode end) {

        return Math.sqrt(Math.pow((start.getX() - end.getX()), 2) + (Math.pow(start.getY() - end.getY(), 2)));
    }
}