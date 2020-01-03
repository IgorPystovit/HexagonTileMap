package com.igorpystovit;

import com.igorpystovit.resolvers.InitPointPositionResolver;
import com.igorpystovit.resolvers.TextPositionResolver;
import com.igorpystovit.util.Pair;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class Application extends javafx.application.Application {
    private InitPointPositionResolver positionResolver = new InitPointPositionResolver();
    private List<HexShape> beginEndList = new ArrayList<>();
    private Set<HexShape> hexagon;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

        hexagon = new HexagonTileMapGenerator().generate(100,1600,800);
//          hexagon = HardcodedHexagons.getHexagon();
//          resolveInitPairs(hexagon);
//        Set<HexShape> hexagon = HardcodedHexagonMap.getHexagonMap();

//        hexagon.forEach(h -> System.out.println(h.getNearShapes().size()));

//        resolveInitPairs(hexagon);
//
//        HexShape hex1 = (HexShape) hexagon.toArray()[1];
//        HexShape startHex = hex1.getConnections().get(4);
//
//        hex1.removeConnection(startHex);
//        hexagon.remove(startHex);

//        System.out.println(hexagon);

        List<Polygon> hexagons = createHexagons(hexagon);
        List<Text> texts = resolveTextPositions(hexagon);
//        HexShape hex = new HexShape(400,500,4);
//
//        Polygon polygon = createHexagon(hex);
//        Text text = resolveTextPosition(hex);

        Group root = new Group();
//        root.getChildren().add(polygon);
//        root.getChildren().add(text);
        root.getChildren().addAll(hexagons);
        root.getChildren().addAll(texts);
        root.getChildren().add(getGenerateButton(primaryStage));
        Scene scene = new Scene(root, 2000, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private Button getGenerateButton(Stage stage){
        Button button = new Button("Generate");
        button.setPrefSize(100,20);
        button.setLayoutX(1700);
        button.setLayoutY(400);
        button.setOnMouseClicked(mouseClicked -> {
            beginEndList = new ArrayList<>();
            hexagon = new HexagonTileMapGenerator().generate(100,1600,1000);
            List<Polygon> hexagons = createHexagons(hexagon);
            List<Text> texts = resolveTextPositions(hexagon);

            Group root = new Group();
            root.getChildren().addAll(hexagons);
            root.getChildren().addAll(texts);
            root.getChildren().add(button);
            Scene scene = new Scene(root, 2000, 1000);
            stage.setScene(scene);
            stage.show();
        });
        return button;
    }
    private void resolveInitPairs(Collection<HexShape> shapes) {
        InitPointPositionResolver pointPositionResolver = new InitPointPositionResolver();

        shapes.forEach(pointPositionResolver::resolveConnectionsInitPoints);

        //resolution of isolated hexes
        Random random = new Random();
        double maxX = shapes.stream().mapToDouble(shape -> shape.getInitPair().getX()).max().orElseThrow(NoSuchElementException::new) + HexShape.SPOT_SIZE * 3;
        double minY = shapes.stream().mapToDouble(shape -> shape.getInitPair().getY()).min().orElseThrow(NoSuchElementException::new);
        Pair prev = new Pair(maxX, minY);
        for (HexShape isolatedHexes : pointPositionResolver.getIsolatedHexes()) {
            isolatedHexes.setInitPair(new Pair(
                    prev.getX() + random.nextInt(100) + HexShape.SPOT_SIZE,
                    prev.getY() + random.nextInt(100) + HexShape.SPOT_SIZE));
            prev.setX(prev.getX() + (HexShape.SPOT_SIZE * 2));
            prev.setY(prev.getY() + (HexShape.SPOT_SIZE * 3));
        }
    }

    private List<Text> resolveTextPositions(Collection<HexShape> shapes) {
        return new TextPositionResolver().resolveTextPositions(shapes);
    }

    private void draw(Collection<HexShape> shapes, GraphicsContext context) {
        for (HexShape tempShape : shapes) {
            Map<Integer, Pair> coordinateMap = tempShape.getCoordinateMap();
            double[] x = coordinateMap.values().stream().mapToDouble(Pair::getX).toArray();
            double[] y = coordinateMap.values().stream().mapToDouble(Pair::getY).toArray();
            context.strokePolygon(x, y, 6);
            context.fillPolygon(x, y, 6);
        }
    }

    private List<Polygon> createHexagons(Collection<HexShape> shapes) {
        List<Polygon> polygons = new ArrayList<>();

        for (HexShape tempShape : shapes) {
            Polygon polygon = new Polygon();
            tempShape
                    .getCoordinateMap()
                    .values()
                    .forEach(pair -> polygon.getPoints().addAll(pair.getX(), pair.getY()));
            polygon.setStrokeWidth(5);
            polygon.setStroke(Color.BLACK);
            polygon.setFill(Color.valueOf("#ab8c1c"));

            polygon.setOnMouseClicked(mouseEvent -> {
                beginEndList.add(tempShape);
                if (beginEndList.size() == 2){
                    PathSeeker pathSeeker = new PathSeeker();
                    System.out.println(pathSeeker.seekShortestPath(hexagon, beginEndList.get(0),beginEndList.get(1)).get(beginEndList.get(1)));
                    pathSeeker.seekPath(hexagon, beginEndList.get(0),beginEndList.get(1)).forEach(hexShape -> System.out.print(hexShape.getValue()+" - "));
                    beginEndList.clear();
                }

                FillTransition fillTransition = new FillTransition(Duration.millis(800),polygon);
                fillTransition.setFromValue(Color.valueOf(polygon.getFill().toString()));
                fillTransition.setToValue(Color.WHITE);
                fillTransition.setAutoReverse(true);
                fillTransition.setCycleCount(Animation.INDEFINITE);
                fillTransition.play();
            });
            polygons.add(polygon);
        }
        return polygons;
    }

    private Polygon createHexagon(HexShape hex){
        Polygon polygon = new Polygon();
        hex
                .getCoordinateMap()
                .values()
                .forEach(pair -> polygon.getPoints().addAll(pair.getX(), pair.getY()));
        polygon.setStrokeWidth(5);
        polygon.setStroke(Color.BLACK);
        polygon.setFill(Color.valueOf("#ab8c1c"));
        polygon.setOnMouseClicked(mouseEvent -> {
            StrokeTransition strokeTransition = new StrokeTransition(Duration.millis(400));
            strokeTransition.setShape(polygon);
            strokeTransition.setAutoReverse(true);
//                strokeTransition.setFromValue(Color.valueOf(polygon.getStroke().toString()));
            strokeTransition.setFromValue(Color.BLACK);
            strokeTransition.setToValue(Color.RED);
            strokeTransition.setCycleCount(Animation.INDEFINITE);
            strokeTransition.play();
        });
        return polygon;
    }

    private Text resolveTextPosition(HexShape hexShape){
        return new TextPositionResolver().resolveTextPositions(Collections.singletonList(hexShape)).get(0);
    }

}
