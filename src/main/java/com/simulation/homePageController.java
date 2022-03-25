package com.simulation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class homePageController {

    @FXML
    private Button createButton, loadButton;

    @FXML
    public TextField textHolder;


    public void createGraphbtn() throws IOException {

        Stage stage = (Stage) createButton.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("createGraph.fxml"));
        Scene scene =  new Scene(root, 800,800);
        stage.setScene(scene);
        stage.show();

    }

    public void loadGraphbtn() throws IOException {
        loadButton.setText("Loading...");

        Stage stage = (Stage) createButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("simulationWindow.fxml"));

        Parent root = fxmlLoader.load();

        simulationWindowController controller = fxmlLoader.getController();
        controller.setFilename(textHolder.getText());

        Scene scene =  new Scene(root, 800,800);

        stage.setScene(scene);
        stage.show();
    }

}
