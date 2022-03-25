package com.simulation;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XMLLoader {

    private Graph graph;
    private String fileName;
    private ArrayList<String[]> edgeList = new ArrayList<>();

    public XMLLoader(String fileName) {
        this.fileName = fileName;

        try {
            load();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {

        } catch (IOException e) {

        }

    }

    public void load() throws ParserConfigurationException, SAXException, IOException {

        graph = new Graph();
        File fxmlFile = new File(fileName + ".xml");

        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = dBuilder.parse(fxmlFile);

//        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("Node");

        System.out.println("----------------------------");

        //go through Nodes
        for (int i = 0; i < nList.getLength(); i++) {

            org.w3c.dom.Node nNode = nList.item(i);

            if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

                Element nodeElement = (Element) nNode;
                com.simulation.Node temp = new com.simulation.Node(Integer.parseInt(nodeElement.getAttribute("index")),
                        Double.parseDouble(nodeElement.getAttribute("posX")),
                        Double.parseDouble(nodeElement.getAttribute("posY")),
                        Integer.parseInt(nodeElement.getAttribute("x")),
                        Integer.parseInt(nodeElement.getAttribute("y")),
                        Integer.parseInt(nodeElement.getAttribute("type")));

                graph.addNode(temp);

            }
        }

        NodeList eList = doc.getElementsByTagName("Edge");

        for (int i = 0; i < eList.getLength(); i++) {
            org.w3c.dom.Node eNode = eList.item(i);

            if (eNode.getNodeType() == Node.ELEMENT_NODE) {
                Element nodeElement = (Element) eNode;


                graph.addEdge(graph.getNodeByIndex(Integer.parseInt(nodeElement.getAttribute("start"))),
                        graph.getNodeByIndex(Integer.parseInt(nodeElement.getAttribute("end"))));

            }

        }


    }

    public Graph getGraph() {

        return graph;
    }

}

