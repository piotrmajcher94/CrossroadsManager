package simulation;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import simulationmodels.*;
import util.CanvasPane;
import util.SimpleShapePainter;


import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;


public class Controller implements Initializable{

    private @FXML
    StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /**
         * Create the Canvas and add it to the window
         */
        CanvasPane canvasPane = new CanvasPane(800,600);
        Canvas canvas = canvasPane.getCanvas();
        AnchorPane anchorPane = new AnchorPane();
        stackPane.getChildren().addAll(canvasPane,anchorPane);

        /**
         * Prepare background
         */
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.CHOCOLATE);
        context.fillRect(0,0,800,600);

        /**
         * Draw the crossroads
         */
        CrossroadsView crossroadsView = new CrossroadsView(new Point2D(380.0,280.0),
                2,40.0,260,360,250,350);
        SimpleShapePainter.drawShape(crossroadsView,context);
        /**
         * Add traffic lights to the crossroads
         */
        RoadModel roadN = new RoadModel(crossroadsView.getRoadNORTH());
        roadN.addLightsEndA(5000,3000);
        roadN.addLightsEndB(5000,3000);

        RoadModel roadE = new RoadModel(crossroadsView.getRoadEAST());
        roadE.addLightsEndA(5000,3000);
        roadE.addLightsEndB(5000,3000);

        RoadModel roadS = new RoadModel(crossroadsView.getRoadSOUTH());
        roadS.addLightsEndA(5000,3000);
        roadS.addLightsEndB(5000,3000);

        RoadModel roadW = new RoadModel(crossroadsView.getRoadWEST());
        roadW.addLightsEndA(5000,3000);
        roadW.addLightsEndB(5000,3000);

        canvasPane.getChildren().add(roadN.getTrafficLightsModelEndA().getTrafficLightsView());
        canvasPane.getChildren().add(roadN.getTrafficLightsModelEndB().getTrafficLightsView());

        canvasPane.getChildren().add(roadE.getTrafficLightsModelEndA().getTrafficLightsView());
        canvasPane.getChildren().add(roadE.getTrafficLightsModelEndB().getTrafficLightsView());

        canvasPane.getChildren().add(roadS.getTrafficLightsModelEndA().getTrafficLightsView());
        canvasPane.getChildren().add(roadS.getTrafficLightsModelEndB().getTrafficLightsView());

        canvasPane.getChildren().add(roadW.getTrafficLightsModelEndA().getTrafficLightsView());
        canvasPane.getChildren().add(roadW.getTrafficLightsModelEndB().getTrafficLightsView());

        try {
            new Thread(roadN.getTrafficLightsModelEndA()).start();
            Thread.sleep(1000);
            new Thread(roadN.getTrafficLightsModelEndB()).start();
            Thread.sleep(1000);
            new Thread(roadS.getTrafficLightsModelEndA()).start();
            Thread.sleep(1000);
            new Thread(roadS.getTrafficLightsModelEndB()).start();
            Thread.sleep(1000);
            new Thread(roadE.getTrafficLightsModelEndA()).start();
            Thread.sleep(1000);
            new Thread(roadE.getTrafficLightsModelEndB()).start();
            Thread.sleep(1000);
            new Thread(roadW.getTrafficLightsModelEndA()).start();
            Thread.sleep(1000);
            new Thread(roadW.getTrafficLightsModelEndB()).start();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<RoadModel> roadModels = new ArrayList<>();
        roadModels.add(roadE);
        roadModels.add(roadN);
        roadModels.add(roadS);
        roadModels.add(roadW);

        /**
         * Create cars
         */
        CopyOnWriteArrayList<CarModel> carModels = new CopyOnWriteArrayList<>();

        CarModelGenerator carModelGenerator = new CarModelGenerator(anchorPane,carModels,roadModels);

        List<Point3D> roadERoutes = new LinkedList<>();
        roadERoutes.add(new Point3D(-3*roadE.getRoadView().getRoadLength(),0,40));
        carModelGenerator.addRoadTraffic(new Point2D(
                roadE.getRoadView().getLeftUpperCorner().getX()+roadE.getRoadView().getRoadLength(),
                roadE.getRoadView().getLeftUpperCorner().getY()+roadE.getRoadView().getLaneWidth()/3),
                1000, roadERoutes, roadE.getRoadView().getLaneWidth()/3, roadE.getRoadView().getLaneWidth()/3);
        List<Point3D> roadNRoutes = new LinkedList<>();
        roadNRoutes.add(new Point3D(0,3*roadN.getRoadView().getRoadLength(),40));
        carModelGenerator.addRoadTraffic(new Point2D(
                roadN.getRoadView().getLeftUpperCorner().getX() + roadN.getRoadView().getLaneWidth()/3,
                roadN.getRoadView().getLeftUpperCorner().getY()),
                1000,roadNRoutes,roadN.getRoadView().getLaneWidth()/3, roadN.getRoadView().getLaneWidth()/3);
        List<Point3D> roadWRoutes = new LinkedList<>();
        roadWRoutes.add(new Point3D(3*roadW.getRoadView().getRoadLength(),0,40));
        carModelGenerator.addRoadTraffic(new Point2D(
                        roadW.getRoadView().getLeftUpperCorner().getX(),
                        roadW.getRoadView().getLeftUpperCorner().getY() + roadW.getRoadView().getLaneWidth()+roadW.getRoadView().getLaneWidth()/3),
                1000,roadWRoutes,roadW.getRoadView().getLaneWidth()/3, roadW.getRoadView().getLaneWidth()/3);
        List<Point3D> roadSRoutes = new LinkedList<>();
        roadSRoutes.add(new Point3D(0,-3*roadS.getRoadView().getRoadLength(),40));
        carModelGenerator.addRoadTraffic(new Point2D(
                        roadS.getRoadView().getLeftUpperCorner().getX()+roadS.getRoadView().getLaneWidth()+roadS.getRoadView().getLaneWidth()/3,
                        roadS.getRoadView().getLeftUpperCorner().getY()+roadS.getRoadView().getRoadLength()),
                1000,roadSRoutes,roadS.getRoadView().getLaneWidth()/3, roadS.getRoadView().getLaneWidth()/3);

        new Thread(carModelGenerator).start();

        TrafficManager manager = new TrafficManager(carModels, roadModels,anchorPane);
        new Thread(manager).start();

        }
}
