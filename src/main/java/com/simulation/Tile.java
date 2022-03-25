package com.simulation;

import javafx.geometry.Pos;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {

    public Tile(int SIZE, boolean TRANSPARENT){

        Rectangle border = new Rectangle(SIZE, SIZE);


        if (!TRANSPARENT) {
            //Essential to represent the grid in road creation mode
            border.setFill(null);
            border.setStroke(Color.BLACK);
        }



        setAlignment(Pos.CENTER);
        getChildren().add(border);
    }

    public void addImage(ImageView img){

        getChildren().add(img);
    }


}
