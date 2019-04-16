package GUI;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import Observer.Observable;
import Observer.Observer;
import GUI.Dialog.*;
import javafx.scene.text.Font;

import java.io.*;
import java.util.ArrayList;

public class Map extends Canvas implements Observer {


    public enum Cell {EMPTY, OBSTACLE, VISITED, START, DESTINATION}

    public enum MouseMode {OBSTACLE, START, DESTINATION}


    private double rectWidth;
    private double rectHeight;
    private final int dimension;
    private Cell[][] map;
    private short p[][];
    private ArrayList<Robot> robots = new ArrayList<>();
    Image robotImage;

    public Map(StackPane stackPane, int dimension) {
        this.dimension = dimension;
        stackPane.getChildren().add(this);
        widthProperty().bind(stackPane.widthProperty());
        heightProperty().bind(stackPane.heightProperty());
        map = new Cell[dimension][dimension];
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
        setFocusTraversable(true);
        newMap();
        setMouseMode(MouseMode.DESTINATION);
        System.out.println(this.getClass().getClassLoader().getResource("robot.png").getPath());
        robotImage = new Image(this.getClass().getClassLoader().getResourceAsStream("robot.png"));
    }

    public Map(StackPane stackPane, int dimension,String mapFile) {
        this.dimension = dimension;
        stackPane.getChildren().add(this);
        widthProperty().bind(stackPane.widthProperty());
        heightProperty().bind(stackPane.heightProperty());
        map = new Cell[dimension][dimension];
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
        setFocusTraversable(true);
//        newMap();
        loadMap(new File(mapFile));
        setMouseMode(MouseMode.DESTINATION);
        System.out.println(this.getClass().getClassLoader().getResource("robot.png").getPath());
        robotImage = new Image(this.getClass().getClassLoader().getResourceAsStream("robot.png"));
    }

    public void newMap() {
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                map[i][j] = Cell.EMPTY;
        mapChanged();
        Platform.runLater(this::draw);
    }

    public void setDestination(int node) {
        map[node / dimension][node % dimension] = Cell.DESTINATION;
    }

    public void setStart(int node) {
        map[node / dimension][node % dimension] = Cell.START;
    }

    public void loadMap(File f) {
        if (f.exists()) {
            FileInputStream is = null;
            try {
                is = new FileInputStream(f);
                for (int i = 0; i < dimension; i++)
                    for (int j = 0; j < dimension; j++)
                        map[i][j] = is.read() == 1 ? Cell.OBSTACLE : Cell.EMPTY;
                System.out.println("map loaded !");
                mapChanged();
                Platform.runLater(() -> draw());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("file does not exist");
        }
    }

    private void calculateShortestPaths() {

        short[][] s = new short[dimension * dimension][dimension * dimension];
        p = new short[dimension * dimension][dimension * dimension];
        for (int i = 0; i < dimension * dimension; i++)
            for (int j = 0; j < dimension * dimension; j++) {
                s[i][j] = Short.MAX_VALUE;
                p[i][j] = -2;
            }


        for (int i = 0; i < dimension * dimension; i++) {
            if (map[i / dimension][i % dimension] == Map.Cell.OBSTACLE)
                continue;
            for (int j = 0; j < dimension * dimension; j++) {
                if (map[j / dimension][j % dimension] == Map.Cell.OBSTACLE)
                    continue;
                else if (j == i)
                    s[i][j] = 0;
//                else if (j == i + dimension || j == i - dimension || (j == i + 1 && !(j % dimension == 0)) || (j == i - 1 && !(j % dimension == dimension - 1)) || (j == i - dimension + 1 && !(j % dimension == 0)) || (j == i - dimension - 1 && !(j % dimension == dimension - 1)) || (j == i + dimension +1 && !(j % dimension == 0 )) || (j == i + dimension - 1 && !(j % dimension == dimension - 1)))
                else if (j == i + dimension || j == i - dimension || (j == i + 1 && !(j % dimension == 0)) || (j == i - 1 && !(j % dimension == dimension - 1))) {
                    s[i][j] = 1;
                    p[i][j] = -1;
                }
            }
        }
        LoadDialog.show(null);
        long start = System.currentTimeMillis();
        System.out.println("starting for loops .. ");
        for (short k = 0; k < dimension * dimension; k++) {
            if (map[k / dimension][k % dimension] == Cell.OBSTACLE)
                continue;
            for (int i = 0; i < dimension * dimension; i++) {
                if (map[i / dimension][i % dimension] == Cell.OBSTACLE || s[i][k] == Short.MAX_VALUE || i==k)
                    continue;
                for (int j = i + 1; j < dimension * dimension; j++) {
             /*       if (map[j / dimension][j % dimension] == Cell.OBSTACLE || i == j)
                        continue;
                */    if ((long) s[i][j] > (long) s[i][k] + s[k][j]) {
//                        System.out.println("k="+k+":: updating s["+i+"]["+j+"] and s["+j+"]["+i+"]" );
                        s[i][j] = (short) (s[i][k] + s[k][j]);
                        p[i][j] = k;
                        s[j][i] = (short) (s[j][k] + s[k][i]);
                        p[j][i] = k;
                    }
                }

            }

        }
        System.out.println("Map.calculateShortestPaths :: DONE " + (System.currentTimeMillis() - start));
        LoadDialog.close();
/*
// print s[][] and p[][]
        System.out.println("value of s[][] ::");
        for (int i = 0; i < dimension * dimension; i++){
            for (int j = 0; j < dimension * dimension; j++)
                System.out.print(s[i][j]+", ");
            System.out.println(";");
        }
        System.out.println("value of path[][] ::");
        for (int i = 0; i < dimension * dimension; i++){
            System.out.print(i+" :: ");
            for (int j = 0; j < dimension * dimension; j++)
                System.out.print(p[i][j]+", ");
            System.out.println(";");
        }
*/

    }

    public void saveMap(File f) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(f);
            for (int i = 0; i < dimension; i++)
                for (int j = 0; j < dimension; j++)
                    os.write(map[i][j] == Cell.OBSTACLE ? 1 : 0);
            os.flush();
            System.out.println("map saved !");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {

        double w = getWidth();
        double h = getHeight();
        rectHeight = h / dimension;
        rectWidth = w / dimension;
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.ALICEBLUE);
        gc.fillRect(0, 0, w, h);
        gc.setFill(Color.AQUA);
        gc.setFont(Font.font(8.5));
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
//                System.out.println("Drawing node ::: "+map[i][j]);
                if (map[i][j] == Cell.OBSTACLE)
                    gc.fillRect(j * rectWidth, i * rectHeight, rectWidth, rectHeight);
                else if (map[i][j] == Cell.VISITED) {
                    gc.setFill(Color.RED);
                    gc.fillRect(j * rectWidth, i * rectHeight, rectWidth, rectHeight);
                    gc.setFill(Color.AQUA);
                } else if (map[i][j] == Cell.DESTINATION) {
                    gc.setFill(Color.YELLOW);
                    gc.fillRect(j * rectWidth, i * rectHeight, rectWidth, rectHeight);
                    gc.setFill(Color.AQUA);
                } else if (map[i][j] == Cell.START) {
                    gc.setFill(Color.GREENYELLOW);
                    gc.fillRect(j * rectWidth, i * rectHeight, rectWidth, rectHeight);
                    gc.setFill(Color.AQUA);
                }

                gc.setFill(Color.BLACK);
                gc.fillText(""+(i*dimension+j),j*rectWidth+rectWidth/2,i*rectHeight+rectHeight/2);
                gc.setFill(Color.AQUA);
            }
        }

        gc.setFill(Color.BLACK);
        for (Robot r : robots)
            gc.drawImage(robotImage, r.getX() * rectWidth, r.getY() * rectHeight, rectWidth, rectHeight);
        //            gc.fillRect(r.getX() * rectWidth, r.getY() * rectHeight, rectWidth, rectHeight);


    }

    public int getDimension() {
        return dimension;
    }

    public void setVisitedNode(int node) {
        if (map[node / dimension][node % dimension] == Cell.EMPTY) {
            map[node / dimension][node % dimension] = Cell.VISITED;
            Platform.runLater(() -> draw());
        }
    }

    public void clearVisitedNodes() {
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                if (map[i][j] == Cell.VISITED || map[i][j] == Cell.DESTINATION || map[i][j] == Cell.START)
                    map[i][j] = Cell.EMPTY;
        Platform.runLater(() -> draw());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    public Cell[][] getMap() {
        return map;
    }

    public void addRobot(Robot robot) {
        robots.add(robot);
        robot.setMap(this);
        robot.addObserver(this);
        Platform.runLater(() -> draw());
    }

    public int getNode(double x, double y) {
        return dimension * (int) (y / rectHeight) + (int) (x / rectWidth);
    }

    public short[][] getP() {
        return p;
    }

    public ArrayList<Robot> getRobots() {
        return robots;
    }

    @Override
    public void update(Observable o) {
        Platform.runLater(() -> draw());
    }

    public void setMouseMode(MouseMode mode) {
        if (mode == MouseMode.DESTINATION) {
            setOnMouseDragged(null);
            setOnMouseClicked(event -> {
                Robot robot = robots.get(0);
                synchronized (robot) {
                    int dest = getNode(event.getX(), event.getY());
//                new Thread(() -> robot.aStar(robot.getNode(), dest)).start();
                    if (map[dest / dimension][dest % dimension] != Cell.OBSTACLE)
                        for (Robot r : robots)
                            new Thread(() -> r.moveTo(dest)).start();
                }
            });
        } else if (mode == MouseMode.START) {
            setOnMouseClicked(event -> {
                int node = getNode(event.getX(), event.getY());
                if (map[node / dimension][node % dimension] != Cell.OBSTACLE) {
                    robots.get(0).setPosition(node);
                    clearVisitedNodes();
                }
            });
            setOnMouseDragged(null);
        } else if (mode == MouseMode.OBSTACLE) {
            System.out.println("mode = [" + mode + "]");
            clearVisitedNodes();
            setOnMouseDragged(event -> {
                int y = (int) (event.getY() / rectHeight);
                int x = (int) (event.getX() / rectWidth);
                if (event.getButton() == MouseButton.PRIMARY && map[y][x] == Cell.EMPTY) {
                    map[y][x] = Cell.OBSTACLE;
                } else if (event.getButton() == MouseButton.SECONDARY && map[y][x] == Cell.OBSTACLE)
                    map[y][x] = Cell.EMPTY;
                draw();
            });
            setOnMouseClicked(event -> {
                int y = (int) (event.getY() / rectHeight);
                int x = (int) (event.getX() / rectWidth);
                if (event.getButton() == MouseButton.PRIMARY && map[y][x] == Cell.EMPTY) {
                    map[y][x] = Cell.OBSTACLE;
                    System.out.println(event.getX());
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    map[y][x] = Cell.EMPTY;
                }
                draw();
            });
        }
    }

    public void mapChanged() {
        new Thread(() -> {
            calculateShortestPaths();
            for (Robot r : robots)
                r.setMap(this);
        }).start();
    }

}

