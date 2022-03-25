package com.simulation;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by 123 on 13.06.2018.
 */
public class SpecialCircle{

    public final int RED = 1;
    public final int YELLOW = 2;
    public final int GREEN = 3;
    double proportion = 0.7;
    public int color;
    public Circle circle;
    public int PosX, PosY;

    public SpecialCircle(int color, int PosX, int PosY){
        this.color = color;

        this.circle = new Circle((int)(proportion*12));

        this.PosX = PosX;
        this.PosY = PosY;

        circle.setStroke(Color.BLACK);
        circle.setFill(Color.GRAY);

        circle.setCenterX(PosX);
        circle.setCenterY(PosY);

    }

    public void setColor(){

        if(color == RED){
            this.circle.setFill(Color.RED);
        }
        else if(color == YELLOW){
            this.circle.setFill(Color.YELLOW);
        }
        else if(color == GREEN){
            this.circle.setFill(Color.GREEN);
        }
        else {
            System.out.println("INVALID COLOR TRAFFIC LIGHT");
        }
    }

    public void greyOut(){

        this.circle = new Circle(12);
        this.circle.setStroke(Color.BLACK);
        this.circle.setFill(Color.GRAY);
        this.circle.setCenterX(PosX);
        this.circle.setCenterY(PosY);

    }
}
