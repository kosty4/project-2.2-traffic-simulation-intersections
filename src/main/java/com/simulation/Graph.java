package com.simulation;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

//for exporting to XML file
import org.w3c.dom.*;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Graph {

    public List<Node> nodes;
    public List<Edge> edges;

    ArrayList<Line> lines;

    //Constructor with no input parameters.
    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();

        lines = new ArrayList<>();
    }

    public void addNode(Node node) {

        this.nodes.add(node);
    }

    public void addNodeV2(int index, double Xpos, double Ypos, int x, int y, int type) {

        Node temp = new Node(index, Xpos, Ypos, x, y, type);

        this.nodes.add(temp);

        System.out.println(this.nodes);
    }

    public void addMultipleNodes(ArrayList<Node> nodes) {
        this.nodes.addAll(nodes);
    }

    public void removeNode(Node node) {
        this.nodes.remove(node);
    }

    public int[] maxXY() {
        int[] max = new int[2];
        max[0] = 0;
        max[1] = 0;


        for (Node n : nodes) {
            if (n.x > max[0]) max[0] = n.x;
            if (n.y > max[1]) max[1] = n.y;
        }
        return max;
    }

    public boolean isBorder(Node n) {
        int[] temp = maxXY();

        if (n.x == 0 || n.y == 0) return true;
        if (n.x == temp[0] || n.y == temp[1]) return true;

        return false;
    }

    public int determineType(Node n) {
        System.out.println("# of edges" + this.getAdjecents(n).size());
        if (n.type == 1) return 1;

        else if (n.type == 0 && this.getAdjecents(n).size() == 2) return 2;
        else if (n.type == 0 && this.getAdjecents(n).size() == 3) return 3;
        else if (n.type == 0 && this.getAdjecents(n).size() == 1) return 4;
        return 0;
    }

    public void setSubIntersections() {
        for (int i = 0; i < nodes.size(); i++) {
            List<Node> adj = getAdjecents(nodes.get(i));

            for (int j = 0; j < adj.size(); j++) {
                if (adj.get(j).x > nodes.get(i).x) nodes.get(i).right = true;
                if (adj.get(j).x < nodes.get(i).x) nodes.get(i).left = true;
                if (adj.get(j).y > nodes.get(i).y) nodes.get(i).down = true;
                if (adj.get(j).y < nodes.get(i).y) nodes.get(i).up = true;
            }

//            System.out.println("Adjacent nodes: " + nodes.get(i).left + " " + nodes.get(i).up + " " + nodes.get(i).right + " " + nodes.get(i).down);
            nodes.get(i).createIntersections();
        }
    }


    public void export(String name) throws ParserConfigurationException, FileNotFoundException, IOException {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Graph");
            doc.appendChild(rootElement);

            Element nodes = doc.createElement("Nodes");
            rootElement.appendChild(nodes);

            for (int i = 0; i < this.nodes.size(); i++) {

                // node elements
                Element node = doc.createElement("Node");
                nodes.appendChild(node);

                // set attribute to node element
                node.setAttribute("index", (String.valueOf(this.nodes.get(i).index)));
                node.setAttribute("posX", String.valueOf(this.nodes.get(i).Xpos));
                node.setAttribute("posY", String.valueOf(this.nodes.get(i).Ypos));
                node.setAttribute("x", String.valueOf(this.nodes.get(i).x));
                node.setAttribute("y", String.valueOf(this.nodes.get(i).y));
                node.setAttribute("type", String.valueOf(determineType(this.nodes.get(i))));

            }

            //Edges group
            Element edges = doc.createElement("Edges");
            rootElement.appendChild(edges);

            //edges elements
            for (int j = 0; j < this.edges.size(); j++) {
                Element edge = doc.createElement("Edge");
                edge.setAttribute("start", String.valueOf(this.edges.get(j).start.index));
                edge.setAttribute("end", String.valueOf(this.edges.get(j).end.index));
                edge.setAttribute("type", String.valueOf(this.edges.get(j).type));
                edges.appendChild(edge);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(name + ".xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }


    }


    public void addEdge(Node start, Node end) {
        edges.add(new Edge(start, end));
//        System.out.println("Edge added " + start.Xpos + "  " + start.Ypos + "  " + end.Xpos + "  " +end.Ypos);
    }

    public boolean existsEdge(Node start, Node end) {
        Edge temp = new Edge(start, end);
        boolean out = false;
        for (int i = 0; i < this.edges.size(); i++) {
            if (this.edges.get(i).start == temp.start && this.edges.get(i).end == temp.end) {
                out = true;
            }
        }
        return out;
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public void showGraph(Group group) {

        //Show the Vertexes in GUI
        for (int i = 0; i < this.nodes.size(); i++) {
            Circle vertex = new Circle(this.nodes.get(i).Xpos, this.nodes.get(i).Ypos, 12);
            vertex.setFill(Color.BLUE);
            vertex.setStroke(Color.BLACK);


            group.getChildren().add(vertex);
        }

        //Show the edges between vertexes in GUI
        System.out.println("amount of edges " + this.edges.size());

        for (int i = 0; i < this.edges.size(); i++) {
            Arrow Arrow = new Arrow(this.edges.get(i).start.Xpos, this.edges.get(i).start.Ypos,
                    this.edges.get(i).end.Xpos, this.edges.get(i).end.Ypos);
            group.getChildren().add(Arrow);

        }
    }

    public void printAdjecency() {

        System.out.println("Number of nodes " + this.nodes.size());

        for (int i = 0; i < this.nodes.size(); i++) {
            System.out.println("Current node " + i + "  " + this.nodes.get(i).Xpos + "  " + this.nodes.get(i).Ypos);

        }

        System.out.println("Number of edges " + this.edges.size());

        for (int j = 0; j < this.edges.size(); j++) {
            System.out.println("Edge :" + j + " start  " + this.edges.get(j).start.index + "  " + this.edges.get(j).end.index);

        }

    }

    public int getNumberOfSameLanes(Edge edge){
        int number = 0;

        for (int i = 0; i < this.edges.size(); i++) {
            if(this.edges.get(i).start == edge.start && this.edges.get(i).end == edge.end){
                number ++;
            }
        }

        return number;
    }



    //given a node, return the ammount of incoming edges
    public int getNumberOfIncomingRoads(Node node) {
        int count = 0;

        for (int i = 0; i < this.edges.size(); i++) {

                if (this.edges.get(i).end == node && getNumberOfSameLanes(this.edges.get(i)) < 2) {
                    count++;
                }

        }

        return count;

    }

    public List<Node> getAdjecents(Node node) {

        List<Node> out = new ArrayList<>();

        for (int i = 0; i < this.edges.size(); i++) {
            if (this.edges.get(i).start == node) {
                out.add(this.edges.get(i).end);
            }
        }

        return out;
    }

    public Node getNodeByIndex(int index) {

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).index == index) {
                return nodes.get(i);
            }
        }
        return null;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public Node convertCircleToNode(Circle circle) {
        Node node = new Node(circle.getCenterX(), circle.getCenterY());

        return node;
    }

    public void addLineStart(Circle start) {

        Line line = new Line();


        line.setStartX(start.getCenterX());
        line.setStartY(start.getCenterY());
        System.out.println("added new line start " + start.getCenterX() + "  " + start.getCenterY());

        lines.add(line);
    }

    //This method is used to create an edge in both graph class and visual graphics
    public void addLineEnd(Circle end) {


        lines.get(lines.size() - 1).setEndX(end.getCenterX());
        lines.get(lines.size() - 1).setEndY(end.getCenterY());

        System.out.println("added new line end " + end.getCenterX() + "  " + end.getCenterY());

    }

    public Node getNodeAtCoord(double x, double y) {
        for (int i = 0; i < this.nodes.size(); i++) {
            if (this.nodes.get(i).Xpos == x && this.nodes.get(i).Ypos == y) {
                return this.nodes.get(i);
            }
        }

        return null;
    }

    public Edge getEdge(Node start, Node end) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).start == start && edges.get(i).end == end) {
                return edges.get(i);
            }
        }
        return null;
    }
    public ArrayList<Edge> getLanes(Node start, Node end){
        ArrayList<Edge> out = new ArrayList<>();

        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).start == start && edges.get(i).end == end) {
                out.add(edges.get(i));
            }
        }
        if(out.size()>1){
            return out;
        }
        else return null;
    }

    public Edge getEdgeByIndexes(int start, int end){

        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).start.index == start && edges.get(i).end.index == end) {
                return edges.get(i);
            }
        }
        return null;
    }

}
