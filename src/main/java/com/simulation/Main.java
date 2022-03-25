package com.simulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("homePage.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("homePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        primaryStage.setTitle("Intelligent Traffic Simulation");

//        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
