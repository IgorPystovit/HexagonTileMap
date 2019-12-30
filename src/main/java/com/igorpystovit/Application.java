package com.igorpystovit;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(1000,1000);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.RED);
        context.setStroke(Color.BLUE);
        context.setLineWidth(5);

        Set<HexShape> shapes = new HashSet<>();
//
        HexShape startHex = new HexShape(400,450);
        HexShape hex = new HexShape();
        hex.getNearShapes().put(2,new HexShape());
        startHex.getNearShapes().put(1,hex);
        startHex.getNearShapes().put(2,new HexShape());
        startHex.getNearShapes().put(4,new HexShape());
        startHex.getNearShapes().put(3,new HexShape());
        startHex.getNearShapes().put(5,new HexShape());
        startHex.getNearShapes().put(6,new HexShape());
        shapes.add(startHex);
        shapes.addAll(hex.getNearShapes().values());
        shapes.addAll(startHex.getNearShapes().values());
        resolveInitPairs(shapes);
        draw(shapes,context);
//        Map<Integer,Pair> coordinateMap = startHex.getCoordinates();
//
//        double[] x = coordinateMap.values().stream().mapToDouble(Pair::getX).toArray();
//        double[] y = coordinateMap.values().stream().mapToDouble(Pair::getY).toArray();
//
//        context.strokePolygon(x,y,6);
//        context.fillPolygon(x,y, 6);
////
//        HexShape hex2 = new HexShape(400,350);
//        Map<Integer,Pair> coordinateMap2 = hex2.getCoordinates();
//
//        double[] x2 = coordinateMap2.values().stream().mapToDouble(Pair::getX).toArray();
//        double[] y2 = coordinateMap2.values().stream().mapToDouble(Pair::getY).toArray();
//
//        context.strokePolygon(x2,y2,6);
//        context.fillPolygon(x2,y2, 6);


//        drawShapes(context);
//
////        context.setStroke(Color.RED);
////        context.strokeLine(50,50,100,100);
//        Pane root = new Pane();
//        root.getChildren().add(canvas);
//        Polygon hexagon = new Polygon();
//
//        //spotsize 100
//        hexagon.getPoints().addAll(new Double[]{
//                100.0, 50.0,
//                150.0, 50.0,
//                175.0, 100.0,
//                150.0, 150.0,
//                100.0, 150.0,
//                75.0, 100.0,
//        });
//        hexagon.getPoints()
//        hexagon.setStrokeWidth(5);
////        hexagon.setStroke(Color.RED);
//        hexagon.setRotate(90);
//        hexagon.setFill(Color.valueOf("#0061ff"));

//        StrokeTransition strokeTransition = new StrokeTransition(Duration.millis(1111));
//        strokeTransition.setShape(hexagon);

//        strokeTransition.setFromValue(Color.BLACK);
//        strokeTransition.setToValue(Color.WHITE);
//
//        ParallelTransition parallelTransition = new ParallelTransition(strokeTransition);
//        parallelTransition.setCycleCount(Timeline.INDEFINITE);
//        parallelTransition.setAutoReverse(true);
//        parallelTransition.play();
//
        Group root = new Group();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void resolveInitPairs(Collection<HexShape> shapes){
        InitPointPositionResolver pointPositionResolver = new InitPointPositionResolver();
        shapes.forEach(pointPositionResolver::resolveNear);
    }

    private void draw(Collection<HexShape> shapes,GraphicsContext context){
        for(HexShape tempShape : shapes){
            Map<Integer,Pair> coordinateMap = tempShape.getCoordinates();
            double[] x = coordinateMap.values().stream().mapToDouble(Pair::getX).toArray();
            double[] y = coordinateMap.values().stream().mapToDouble(Pair::getY).toArray();
            context.strokePolygon(x,y,6);
            context.fillPolygon(x,y, 6);
        }
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);

        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);
    }
}
