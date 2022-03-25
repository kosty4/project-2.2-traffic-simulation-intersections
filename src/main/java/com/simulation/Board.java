package com.simulation;

//import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class Board extends GridPane {

    int SIM_SIZE = 100;
    int xSize;
    int ySize;
    Graph graph;
    ArrayList<double[]> grid;


    Tile[][] board;
    private boolean empty;

    public Board(int xSize, int ySize) {

        this.xSize = xSize;
        this.ySize = ySize;
        board = new Tile[xSize][ySize];
        empty = true;

        grid = new ArrayList<>();
        doGrid(xSize, ySize);

        //initBoard();
    }

    public void addIntersectionGUI(int x, int y, int squareSize){

        javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/intersection.png",  squareSize, squareSize,true,false);
        ImageView imgView = new ImageView(img);

        this.getTileAtCoordinates(x,y).getChildren().add(imgView);
    }



    public int getBoardSizeX() {
        return xSize;
    }

    public int getBoardSizeY() {
        return ySize;
    }

    public boolean setTileAtCoordinates(Tile tile, int x, int y) {
        board[x][y] = tile;
        GridPane.setConstraints(tile, x, y);
        getChildren().add(tile);
        empty = false;
        return true;
    }

    public Tile getTileAtCoordinates(int x, int y) {
        return this.board[x][y];
    }

    public void setBoard(Graph graph) {

        this.graph = graph;
        graph.setSubIntersections();

        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {

                javafx.scene.image.Image bgImage = new javafx.scene.image.Image("file:static/background.JPG", SIM_SIZE, SIM_SIZE, false, true);
                ImageView background = new ImageView(bgImage);

                Tile tempTile = new Tile(SIM_SIZE, true);
                tempTile.getChildren().add(background);
                setTileAtCoordinates(tempTile, j, i);

            }
        }


        for(Node node : graph.nodes) {
            int roads = getAmountOfRoads(node);
            System.out.println("Nodes X & Y positions respectively are: " + node.x + "x " + node.y + "y ");
            Tile tile = new Tile(SIM_SIZE, true);
            ImageView imgView;

            //intersections that have only one edge will show as a road!! (type 4)


            if(roads == 4 || roads == 0)
            {
                javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/Intersection.png", SIM_SIZE, SIM_SIZE, true, false);

                imgView = new ImageView(img);
                tile.getChildren().add(imgView);
            }

            if(roads == 2 || roads == 1)
            {
                javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/Road.png",  SIM_SIZE, SIM_SIZE,true,false);
                if(roads == 1)
                {
                    Node m = getNeighbour(node);
                    if(m!=node)
                    {
                        int amountOfLanes = getAmountOfLanes(node, m);

                        if(amountOfLanes == 3) img = new javafx.scene.image.Image("file:static/three.png",  SIM_SIZE, SIM_SIZE,true,false);
                        if(amountOfLanes == 4) img = new javafx.scene.image.Image("file:static/four.png",  SIM_SIZE, SIM_SIZE,true,false);
                    }
                }
                imgView = new ImageView(img);

                if((node.up && !node.down) || (node.down && !node.up) || (node.up && node.down))
                {
                    imgView.setRotate(90);
                }
                if(node.left && node.up)
                {
                    img = new javafx.scene.image.Image("file:static/Corner.png",  SIM_SIZE, SIM_SIZE,true,false);
                    imgView = new ImageView(img);
                    imgView.setRotate(90);
                }
                if(node.up && node.right)
                {
                    img = new javafx.scene.image.Image("file:static/Corner.png",  SIM_SIZE, SIM_SIZE,true,false);
                    imgView = new ImageView(img);
                    imgView.setRotate(180);
                }
                if(node.right && node.down)
                {
                    img = new javafx.scene.image.Image("file:static/Corner.png",  SIM_SIZE, SIM_SIZE,true,false);
                    imgView = new ImageView(img);
                    imgView.setRotate(270);
                }
                if(node.left && node.down)
                {
                    img = new javafx.scene.image.Image("file:static/Corner.png",  SIM_SIZE, SIM_SIZE,true,false);
                    imgView = new ImageView(img);
                    imgView.setRotate(360);
                }

                tile.getChildren().add(imgView);
            }

            if(roads == 3)
            {
//                javafx.scene.image.Image img = new javafx.scene.image.Image("Tsection.png",  SIM_SIZE, SIM_SIZE,true,false);
//                imgView = new ImageView(img);
//                if(!node.left)
//                {
//                    imgView.setRotate(270);
//                }
//                if(!node.down)
//                {
//                    imgView.setRotate(180);
//                }
//                if(!node.up)
//                {
//                    imgView.setRotate(360);
//                }
//                if(!node.right)
//                {
//                    imgView.setRotate(90);
//                }

                imgView = analyseTsection(node);
                tile.getChildren().add(imgView);
            }

            setTileAtCoordinates(tile, node.x, node.y);
        }



        initiateLanes();

    }

    public ImageView analyseTsection(Node n)
    {
        System.out.println("T Section checked");
        javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/Tsection.png", SIM_SIZE, SIM_SIZE, true, false);
        ImageView imgView = new ImageView(img);


            ArrayList<Edge> edg = new ArrayList<>();
            for(Edge e : graph.edges)
            {
                if (e.start == n || e.end == n) {



                            edg.add(e);


                }
            }

            int six = 0, two = 0, eight = 0, four = 0;

            for(Edge e : edg)
            {
                if(e.direction == 6)
                {
                    if(e.start == n )
                    {
                        six++;
                    }
                    if(e.end == n)
                    {
                        four++;
                    }

                }
                if(e.direction == 4)
                {
                    if(e.start == n )
                    {
                        four++;
                    }
                    if(e.end == n)
                    {
                        six++;
                    }
                }
                if(e.direction == 8)
                {
                    if(e.start == n )
                    {
                        eight++;
                    }
                    if(e.end == n)
                    {
                        two++;
                    }
                }
                if(e.direction == 2)
                {
                    if(e.start == n )
                    {
                        two++;
                    }
                    if(e.end == n)
                    {
                        eight++;
                    }
                }
            }

        System.out.println("lanes in each direction: six " + six + " four " + four + " two " + two + " eight " + eight );

            if(two == eight)
            {
                if(two == 2)
                {
                    img = new javafx.scene.image.Image("file:static/Tsection.png", SIM_SIZE, SIM_SIZE, true, false);
                    imgView = new ImageView(img);
                    if(four!=0)
                    {
                        imgView.setRotate(90);
                    }
                    if(six!=0)
                    {
                        imgView.setRotate(270);
                    }
                }
                if(two == 3)
                {
                    img = new javafx.scene.image.Image("file:static/3Tsection.png", SIM_SIZE, SIM_SIZE, true, false);
                    imgView = new ImageView(img);
                    if(four!=0)
                    {
                        imgView.setRotate(90);
                    }
                    if(six!=0)
                    {
                        imgView.setRotate(270);
                    }
                }
                if(two == 4)
                {
                    img = new javafx.scene.image.Image("file:static/4Tsection.png", SIM_SIZE, SIM_SIZE, true, false);
                    imgView = new ImageView(img);
                    if(four!=0)
                    {
                        imgView.setRotate(90);
                    }
                    if(six!=0)
                    {
                        imgView.setRotate(270);
                    }
                }
            }

            if(four == six)
            {
                if(six == 2)
                {
                    img = new javafx.scene.image.Image("file:static/Tsection.png", SIM_SIZE, SIM_SIZE, true, false);
                    imgView = new ImageView(img);
                    if(eight!=0)
                    {
                        imgView.setRotate(180);
                    }
                }
                if(six == 3)
                {
                    img = new javafx.scene.image.Image("file:static/4Tsection.png", SIM_SIZE, SIM_SIZE, true, false);
                    imgView = new ImageView(img);
                    if(eight!=0)
                    {
                        imgView.setRotate(180);
                    }
                }
                if(six == 4)
                {
                    img = new javafx.scene.image.Image("file:static/4Tsection.png", SIM_SIZE, SIM_SIZE, true, false);
                    imgView = new ImageView(img);
                    if(eight!=0)
                    {
                        imgView.setRotate(180);
                    }
                }
            }




        return imgView;
    }

    public Node getNeighbour(Node n)
    {
        for(Edge e : graph.edges)
        {
            if(e.start == n)
            {
                return e.end;
            }
            if(e.end == n)
            {
                return e.start;
            }
        }

        return n;
    }

    public void initiateLanes()
    {
        for(Node n: graph.nodes)
        {
            List<Node> l = graph.getAdjecents(n);
               for(Node m : l)
               {
                   if(m!=n)
                   {
                       int amount = getAmountOfLanes(n, m);
                       if(amount > 0) drawLanes(amount, n, m);
                   }

               }
        }
    }

    public void drawIntersectionAddition(int x, int y, int dir, int lanes)
    {
        if(lanes == 3)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/threelaneadd.png", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            //rotate to a direction (6=right, 4=left, 2=down & 8=up). Standard direction is 6
            if(dir == 4)
            {
                imgView.setRotate(180);
            }
            if(dir == 8)
            {
                imgView.setRotate(270);
            }
            if(dir == 2)
            {
                imgView.setRotate(90);
            }
            board[x][y].getChildren().add(imgView);
        }

        if(lanes == 4)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/fourlaneadd.png", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            //rotate to a direction (6=right, 4=left, 2=down & 8=up). Standard direction is 6
            if(dir == 4)
            {
                imgView.setRotate(180);
            }
            if(dir == 8)
            {
                imgView.setRotate(270);
            }
            if(dir == 2)
            {
                imgView.setRotate(90);
            }
            board[x][y].getChildren().add(imgView);
        }

        if(lanes == 5)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/fivelaneadd.png", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            //rotate to a direction (6=right, 4=left, 2=down & 8=up). Standard direction is 6
            if(dir == 4)
            {
                imgView.setRotate(180);
            }
            if(dir == 8)
            {
                imgView.setRotate(270);
            }
            if(dir == 2)
            {
                imgView.setRotate(90);
            }
            board[x][y].getChildren().add(imgView);
        }

    }

    //Amount:
    //23 -> two to three lanes
    //24 -> two to four lanes
    //34 -> three to four lanes
    public void drawLaneAddition(int x, int y, int dir, int amount)
    {
        if(amount == 23)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/twotothree.png", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            ImageView imgView2 = new ImageView(img);
            //vertical
            if(dir == 2 || dir == 8)
            {
                imgView.setRotate(90);
                imgView2.setRotate(270);
            }

            //horizontal
            if(dir == 6 || dir == 4)
            {
                imgView2.setRotate(180);
            }
            board[x][y].getChildren().add(imgView);
            board[x][y].getChildren().add(imgView2);
        }

        if(amount == 24)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/twotofour.png", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            ImageView imgView2 = new ImageView(img);
            //vertical
            if(dir == 2 || dir == 8)
            {
                imgView.setRotate(90);
                imgView2.setRotate(270);
            }

            //horizontal
            if(dir == 6 || dir == 4)
            {
                imgView2.setRotate(180);
            }
            board[x][y].getChildren().add(imgView);
            board[x][y].getChildren().add(imgView2);
        }

        if(amount == 34)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/threetofour.png", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            ImageView imgView2 = new ImageView(img);
            //vertical
            if(dir == 2 || dir == 8)
            {
                imgView.setRotate(90);
                imgView2.setRotate(270);
            }

            //horizontal
            if(dir == 6 || dir == 4)
            {
                imgView2.setRotate(180);
            }
            board[x][y].getChildren().add(imgView);
            board[x][y].getChildren().add(imgView2);
        }
    }

    public void checkMultiLanes()
    {
        for(Node n : graph.nodes)
        {
            for(Node m : graph.nodes)
            {
                int lanes = getAmountOfLanes(n, m);
                int dir = 0;
                //check which side of the "n" intersection needs the overlay
                if(n.x>m.x) dir = 4;
                if(n.x<m.x) dir = 6;
                if(n.y>m.y) dir = 8;
                if(n.y<m.y) dir = 2;
                //one exception. When an intersection only has two roads it needs a different image
                if(getAmountOfRoads(n) == 2 || getAmountOfRoads(n) == 1)
                {
                    checkRoadIntersections();
                }
                else
                {
                    drawIntersectionAddition(n.x, n.y, dir, lanes);
                }

                //same but for the "m" intersection -> opposite direction
                if(n.x>m.x) dir = 6;
                if(n.x<m.x) dir = 4;
                if(n.y>m.y) dir = 2;
                if(n.y<m.y) dir = 8;
                if(getAmountOfRoads(m) == 2 || getAmountOfRoads(m) == 1)
                {
                    checkRoadIntersections();
                }
                else
                {
                    drawIntersectionAddition(m.x, m.y, dir, lanes);
                }


            }
        }

        //checkMultiLaneDestinations();
    }

    public void checkRoadIntersections()
    {

    }

    public void checkMultiLaneDestinations()
    {
//        int smallestX = 1000;
//        int smallestY = 1000;
//        int biggestX = 0;
//        int biggestY = 0;
//        for(Node n : graph.nodes)
//        {
//            if(n.x<smallestX)
//            {
//                smallestX = n.x;
//            }
//            if(n.y<smallestY)
//            {
//                smallestY = n.y;
//            }
//            if(n.x>biggestX)
//            {
//                biggestX = n.x;
//            }
//            if(n.y>biggestY)
//            {
//                biggestY = n.y;
//            }
//        }
        for(Node n : graph.nodes)
        {
            //checks if theyre a single road ending
            if(getAmountOfRoads(n) == 1)
            {
                if(n.right||n.left)
                {
                    int amount = 2;
                    for(Edge e : graph.edges)
                    {
                        if(e.start == n)
                        {

                            amount = getAmountOfLanes(n,e.end);

                           drawIntersection(n.x, n.y, amount, false);
                        }
                        if(e.end == n)
                        {
                            amount = getAmountOfLanes(n, e.start);
                            drawIntersection(n.x, n.y, amount, false);
                        }
                    }

                }
                if(n.up||n.down)
                {
                    int amount = 2;
                    for(Edge e : graph.edges)
                    {
                        if(e.start == n)
                        {

                            amount = getAmountOfLanes(n,e.end);

                            drawIntersection(n.x, n.y, amount, true);
                        }
                        if(e.end == n)
                        {
                            amount = getAmountOfLanes(n, e.start);
                            drawIntersection(n.x, n.y, amount, true);
                        }
                    }
                }
                //checks if they are on the edges of the map
//                if(n.x == smallestX)
//                {
//
//                }
//                else if(n.y == smallestY)
//                {
//
//                }
//                else if(n.x == biggestX)
//                {
//
//                }
//                else if(n.y == biggestY)
//                {
//
//                }
            }

        }
    }

    public void drawIntersection(int x, int y, int amount, boolean rotate)
    {
        if(amount == 2)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/Road.PNG", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            if(rotate) imgView.setRotate(90);
            board[x][y].getChildren().add(imgView);
        }
        if(amount == 3)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/Three.png", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            if(rotate) imgView.setRotate(90);
            board[x][y].getChildren().add(imgView);
        }
        if(amount == 4)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/four.png", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            board[x][y].getChildren().add(imgView);
        }
        if(amount == 5)
        {
            javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/five.png", SIM_SIZE, SIM_SIZE, true, false);
            ImageView imgView = new ImageView(img);
            if(rotate) imgView.setRotate(90);
            board[x][y].getChildren().add(imgView);
        }

    }
    public void drawLanes(int amount, Node from, Node to)
    {
        int gridX = (from.x + to.x)/2;
        int gridY = (from.y + to.y)/2;

        int xRoads = Math.abs(to.x-from.x)-1;
        int yRoads = Math.abs(to.y-from.y)-1;
        javafx.scene.image.Image img = new javafx.scene.image.Image("file:static/Road.PNG", SIM_SIZE, SIM_SIZE, true, false);
        for(int i = 0; i<xRoads ; i++)
        {

            if(amount == 1)
            {
                img = new javafx.scene.image.Image("file:static/one.png", SIM_SIZE, SIM_SIZE, true, false);

            }
            else if(amount == 2)
            {
                img = new javafx.scene.image.Image("file:static/Road.PNG", SIM_SIZE, SIM_SIZE, true, false);

            }
            else if(amount == 3)
            {
                img = new javafx.scene.image.Image("file:static/three.png", SIM_SIZE, SIM_SIZE, true, false);

            }
            else if(amount == 4)
            {
                img = new javafx.scene.image.Image("file:static/four.png", SIM_SIZE, SIM_SIZE, true, false);

            }
            else if(amount >= 5)
            {
                img = new javafx.scene.image.Image("file:static/five.png", SIM_SIZE, SIM_SIZE, true, false);

            }

            ImageView imgView = new ImageView(img);

            if(from.y != to.y)
            {
                imgView.setRotate(90);
                if(amount == 1 && graph.getAdjecents(from).contains(to) && from.y>to.y) imgView.setRotate(180);
                if(amount == 1 && graph.getAdjecents(to).contains(from) && from.y<to.y) imgView.setRotate(180);
            }

            else
            {
                if(amount == 1 && graph.getAdjecents(from).contains(to) && from.x>to.x) imgView.setRotate(180);
                if(amount == 1 && graph.getAdjecents(to).contains(from) && from.x<to.x) imgView.setRotate(180);
            }
            if(from.x<to.x)
            {
                gridX = from.x+1;
            }
            else if(from.x>to.x)
            {
                gridX = to.x+1;
            }
            board[gridX+i][gridY].getChildren().add(imgView);
        }

        for(int i = 0; i<yRoads ; i++)
        {

            if(amount == 1)
            {
                img = new javafx.scene.image.Image("file:static/one.png", SIM_SIZE, SIM_SIZE, true, false);

            }
            else if(amount == 2)
            {
                img = new javafx.scene.image.Image("file:static/Road.PNG", SIM_SIZE, SIM_SIZE, true, false);

            }
            else if(amount == 3)
            {
                img = new javafx.scene.image.Image("file:static/three.png", SIM_SIZE, SIM_SIZE, true, false);

            }
            else if(amount == 4)
            {
                img = new javafx.scene.image.Image("file:static/four.png", SIM_SIZE, SIM_SIZE, true, false);

            }
            else if(amount >= 5)
            {
                img = new javafx.scene.image.Image("file:static/five.png", SIM_SIZE, SIM_SIZE, true, false);

            }

            ImageView imgView = new ImageView(img);

            if(from.y != to.y)
            {
                imgView.setRotate(90);
                if(amount == 1 && graph.getAdjecents(from).contains(to) && from.y>to.y) imgView.setRotate(180);
                if(amount == 1 && graph.getAdjecents(to).contains(from) && from.y<to.y) imgView.setRotate(180);
            }

            else
            {
                if(amount == 1 && graph.getAdjecents(from).contains(to) && from.x>to.x) imgView.setRotate(180);
                if(amount == 1 && graph.getAdjecents(to).contains(from) && from.x<to.x) imgView.setRotate(180);
            }


            if(from.y<to.y)
            {
                gridY = from.y+1;
            }
            else if(from.y>to.y)
            {
                gridY = to.y+1;
            }
            board[gridX][gridY+i].getChildren().add(imgView);
        }

        //adjust the intersections to the # of lanes
        checkMultiLanes();
    }

    public int getAmountOfLanes(Node n, Node m)
    {
        int counter = 0;

        for(Edge e : graph.edges)
        {
            if(e.start == n && e.end == m) counter ++;
            if(e.start == m && e.end == n) counter++;
        }

        return counter;
    }



    //NOT THE SAME AS AMOUNT OF LANES
    public int getAmountOfRoads(Node n)
    {
        List<Edge> tempList = new ArrayList<>();
        for(Edge e : graph.edges)
        {
            tempList.add(e);
        }

        int counter = 0;

        //Deletes all duplicate edges, then counts how many point to Node n (max 5)
        ArrayList<Edge> visited = new ArrayList<>();

        for(Edge e : graph.edges)
        {
            visited.add(e);
            for(Edge f : graph.edges)
            {   // if f is not e, e has a connection with Node n and f is  the direction of e or f is the opposite direction of e -> counter --
                if(!visited.contains(f) && ((f.start == e.start && f.end == e.end) || (f.start == e.end && f.end == e.start))) {tempList.remove(f);}
            }
        }

        for(Edge e : tempList)
        {
            if(e.start == n || e.end == n) counter++;
        }

        System.out.println("Node " + n.getIndex() + " has " + counter + " different roads");
        return counter;
    }

    public void doGrid(int xSize, int ySize) {

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                double x = j * (SIM_SIZE + 1) + 50;
                double y = i * (SIM_SIZE + 1) + 100;
                double[] xy = new double[4];
                xy[0] = x;
                xy[1] = y;
                xy[2] = j;
                xy[3] = i;
                grid.add(xy);
            }
        }
    }


    public double[] getGridXY(double x, double y, int CELL_SIZE) {

        double[] closest = new double[4];
        double minimum = 9999999;

        System.out.println("x " + x + " y" + y);

        for (int i = 0; i < grid.size(); i++) {

            double diff = abs(grid.get(i)[0] - x) + abs(grid.get(i)[1] - y);

            if (diff < minimum) {
                minimum = diff;
                closest = grid.get(i);
            }
        }

        System.out.println(" closest "+  Arrays.toString(closest));
        return closest;

    }

}
