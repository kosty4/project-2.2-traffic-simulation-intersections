package com.simulation;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class CreateGraphController {

    @FXML
    public Button backMenuButton, interSectbtn, joinbtn, deletebtn, saveConfigbtn;
    @FXML
    private BorderPane createGraphPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Group drawSceneElements;
    @FXML
    private TextField saveConfigName;
    private int control = 0;
    private int indexCount = 0;
    private boolean release = false;

    public Graph graph;
    Board board;
    //adjust the size of the ammount of zoom in the board
    int CELL_SIZE;

    public void initialize() {
        //drawSceneElements is going to hold all the board gui components, including the board itself.

        board = new Board(15, 15);
        CELL_SIZE = 100;

        graph = new Graph();

        for (int i = 0; i < board.getBoardSizeX(); i++) {
            for (int j = 0; j < board.getBoardSizeY(); j++) {
                Tile tile = new Tile(CELL_SIZE, false);
                board.setTileAtCoordinates(tile, i, j);
            }
        }

        drawSceneElements.getChildren().add(board);
//        drawSceneElements.getChildren().get().get

        scrollPane.setContent(drawSceneElements);
        scrollPane.setOnMouseClicked(mouseHandler);

    }



    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent mouseEvent) {


            if (control == 1 && mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {

                System.out.println("Scene XY are: " + mouseEvent.getSceneX() + ", " + mouseEvent.getSceneY());
                double[] gridXY = board.getGridXY(mouseEvent.getSceneX(), mouseEvent.getSceneY(), CELL_SIZE);
                //TODO FIX THIS!

//                double gridX = gridXY[0];
//                double gridY = gridXY[1];

                int gridX = (int) gridXY[2] * (CELL_SIZE + 1) + CELL_SIZE/2;
                int gridY = (int) gridXY[3] * (CELL_SIZE + 1) + CELL_SIZE/2;

                int x = (int) gridXY[2];
                int y = (int) gridXY[3];


                Circle vertex = new Circle(gridX, gridY, 12);
                System.out.println("NEW VERT BEEN PLACED " + gridX + "   " + gridY);

                vertex.setFill(Color.BLUE);
                vertex.setStroke(Color.BLACK);

                if (mouseEvent.isShiftDown()) {
                    graph.addNodeV2(indexCount, gridX, gridY, x, y, 1);
                } else graph.addNodeV2(indexCount, gridX, gridY, x, y, 0);

                System.out.println("Node added  " + indexCount + " x,y :" + graph.getNodeByIndex(indexCount).x + " " + graph.getNodeByIndex(indexCount).y);

                indexCount++;

                drawSceneElements.getChildren().add(vertex);
                System.out.println(Arrays.toString(gridXY));

                //Add a listener to a vertex that will trigger if its being pressed to delete it
                vertex.addEventHandler(MouseEvent.MOUSE_CLICKED, arg0 -> {

                    if (control == 3) {

                        //remove edges
                        if (graph.getAdjecents(graph.getNodeAtCoord(vertex.getCenterX(), vertex.getCenterY())).size() > 0) {

                            System.out.println("Removing edge(es) ");

                            for (int i = 0; i < drawSceneElements.getChildren().size(); i++) {
                                System.out.println(drawSceneElements.getChildren().get(i));
                                if (drawSceneElements.getChildren().get(i) instanceof Path) {

                                    if (drawSceneElements.getChildren().get(i).contains(vertex.getCenterX(), vertex.getCenterY())) {

                                        drawSceneElements.getChildren().remove(drawSceneElements.getChildren().get(i));
                                        System.out.println(drawSceneElements.getChildren().get(i));
                                    }

                                }
                            }
                        }

                        System.out.println("Node removed " + graph.getNodeAtCoord(vertex.getCenterX(), vertex.getCenterY()));
                        graph.removeNode(graph.getNodeAtCoord(vertex.getCenterX(), vertex.getCenterY()));
                        drawSceneElements.getChildren().remove(vertex);
                    }

                    if (control == 2) {

                        //reset strokes
                        for (int i = 0; i < drawSceneElements.getChildren().size() - 1; i++) {
                            if (drawSceneElements.getChildren().get(i) instanceof Circle) {
                                ((Circle) drawSceneElements.getChildren().get(i)).setStrokeWidth(1.0);
                                ((Circle) drawSceneElements.getChildren().get(i)).setStroke(Color.BLACK);
                            }
                        }

                        Arrow Arrow;

                        if (!release) {
                            //Create new Line object and set the start of it
                            graph.addLineStart(vertex);

                            //highlighting
                            vertex.setStrokeWidth(3.0);
                            vertex.setStroke(Color.RED);

                            release = true;
                        } else {
                            //get the last line object and set the end of this line
                            //highlighting
                            vertex.setStrokeWidth(3.0);
                            vertex.setStroke(Color.GREEN);
                            graph.addLineEnd(vertex);

                            double stX = graph.lines.get(graph.lines.size() - 1).getStartX();
                            double stY = graph.lines.get(graph.lines.size() - 1).getStartY();
                            double ndX = graph.lines.get(graph.lines.size() - 1).getEndX();
                            double ndY = graph.lines.get(graph.lines.size() - 1).getEndY();

                            if (stX != ndX || stY != ndY) {

                                Arrow = new Arrow(stX, stY, ndX, ndY);

                                graph.addEdge(graph.getNodeAtCoord(stX, stY), graph.getNodeAtCoord(ndX, ndY));
                                drawSceneElements.getChildren().add(Arrow);
//                                scrollPane.getScene().
                                release = false;
                            }
                        }
                    }
                });
            }


        }
    };


    public void intersectButtonClicked() {
        control = 1;
    }

    public void joinButtonClicked() {
        control = 2;
    }

    public void deleteButtonClicked() {
        control = 3;
    }

    public void saveConfigButtonClicked() {

        try {
            System.out.println("Exporting the graph");
            graph.export(saveConfigName.getText());
        } catch (ParserConfigurationException parse) {
            System.out.println("ParserConfigurationException");
        } catch (FileNotFoundException notFound) {
            System.out.println("ParserConfigurationException");
        } catch (IOException io) {
            System.out.println("IOException");
        }
    }

    public void goToMainMenu() throws IOException {

        backMenuButton.setText("back To Menu...");

        BorderPane pane = FXMLLoader.load(getClass().getResource("homePage.fxml"));
        createGraphPane.getChildren().setAll(pane);

    }

}
