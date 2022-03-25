package com.backend;

//import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;

/**
 * Created by 123 on 21.06.2018.
 */
public class intersectionState {

    public final int RED = 1;
    public final int YELLOW = 2;
    public final int GREEN = 3;

    public final int NORTH = 8;
    public final int SOUTH = 2;
    public final int EAST = 6;
    public final int WEST = 4;

    public int currentStateEast;
    public int currentStateWest;
    public int currentStateNorth;
    public int currentStateSouth;

    //format - West, East, North, South
    public intersectionState(int initialWest,int initialEast, int initialNorth, int initialSouth){

        this.currentStateWest = initialWest;
        this.currentStateEast = initialEast;
        this.currentStateNorth = initialNorth;
        this.currentStateSouth = initialSouth;
    }

    public void Set_Red_Horizontal_Green_Vertical(){
        this.currentStateWest = RED;
        this.currentStateEast = RED;
        this.currentStateNorth = GREEN;
        this.currentStateSouth = GREEN;
    }

    public void Set_Red_Horizontal_Yellow_Vertical(){
        this.currentStateWest = RED;
        this.currentStateEast = RED;
        this.currentStateNorth = YELLOW;
        this.currentStateSouth = YELLOW;
    }

    public void Set_Green_Horizontal_Red_Vertical(){
        this.currentStateWest = GREEN;
        this.currentStateEast = GREEN;
        this.currentStateNorth = RED;
        this.currentStateSouth = RED;
    }

    public void Set_Yellow_Horizontal_Red_Vertical(){
        this.currentStateWest = YELLOW;
        this.currentStateEast = YELLOW;
        this.currentStateNorth = RED;
        this.currentStateSouth = RED;
    }

    public void Set_Green_West_Only(){
        this.currentStateWest = GREEN;
        this.currentStateEast = RED;
        this.currentStateNorth = RED;
        this.currentStateSouth = RED;
    }
    public void Set_Green_East_Only(){
        this.currentStateWest = RED;
        this.currentStateEast = GREEN;
        this.currentStateNorth = RED;
        this.currentStateSouth = RED;
    }
    public void Set_Green_North_Only(){
        this.currentStateWest = RED;
        this.currentStateEast = RED;
        this.currentStateNorth = GREEN;
        this.currentStateSouth = RED;
    }
    public void Set_Green_South_Only(){
        this.currentStateWest = RED;
        this.currentStateEast = RED;
        this.currentStateNorth = RED;
        this.currentStateSouth = GREEN;
    }
    public void Set_Yellow_West_Only(){
        this.currentStateWest = YELLOW;
        this.currentStateEast = RED;
        this.currentStateNorth = RED;
        this.currentStateSouth = RED;
    }
    public void Set_Yellow_East_Only(){
        this.currentStateWest = RED;
        this.currentStateEast = YELLOW;
        this.currentStateNorth = RED;
        this.currentStateSouth = RED;
    }
    public void Set_Yellow_North_Only(){
        this.currentStateWest = RED;
        this.currentStateEast = RED;
        this.currentStateNorth = YELLOW;
        this.currentStateSouth = RED;
    }
    public void Set_Yellow_South_Only(){
        this.currentStateWest = RED;
        this.currentStateEast = RED;
        this.currentStateNorth = RED;
        this.currentStateSouth = YELLOW;
    }




    //format - West, East, North, South
    public ArrayList<Integer> getCurrentStateArray(){

        ArrayList<Integer> out = new ArrayList<>();
        out.add(currentStateWest);
        out.add(currentStateEast);
        out.add(currentStateNorth);
        out.add(currentStateSouth);

        return out;
    }

    public void Set_Green_West_Yellow_East() {

        this.currentStateWest = GREEN;
        this.currentStateEast = YELLOW;
        this.currentStateNorth = RED;
        this.currentStateSouth = RED;

    }

    public void Set_Green_East_Yellow_West() {
        this.currentStateWest = YELLOW;
        this.currentStateEast = GREEN;
        this.currentStateNorth = RED;
        this.currentStateSouth = RED;
    }

    public void Set_Green_South_Yellow_North() {
        this.currentStateWest = RED;
        this.currentStateEast = RED;
        this.currentStateNorth = YELLOW;
        this.currentStateSouth = GREEN;
    }

    public void Set_Green_North_Yellow_South() {
        this.currentStateWest = RED;
        this.currentStateEast = RED;
        this.currentStateNorth = GREEN;
        this.currentStateSouth = YELLOW;

    }
}
