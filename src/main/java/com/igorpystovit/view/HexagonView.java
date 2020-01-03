package com.igorpystovit.view;

import com.igorpystovit.ClickHandler;
import com.igorpystovit.HexShape;
import com.igorpystovit.HexagonTileMapGenerator;
import com.igorpystovit.resolvers.DesignResolver;
import com.igorpystovit.resolvers.TextPositionResolver;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HexagonView {

    private TextPositionResolver textPositionResolver;
    private DesignResolver designResolver;
    private ClickHandler clickHandler;
    private Set<HexShape> hexagon;
    private static int clickCounter;


    public HexagonView(Set<HexShape> hexagon) {
        this.hexagon = hexagon;
        this.designResolver = new DesignResolver(hexagon);
        this.clickHandler = new ClickHandler(hexagon, designResolver);
        this.textPositionResolver = new TextPositionResolver();
    }

    public void launchView(Stage stage) {
        Map<HexShape, Polygon> polygons = designResolver.getHexPolygonMap();
        List<Text> texts = textPositionResolver.resolveTextPositions(hexagon);
        initPolygonEvents(polygons);
        Group root = new Group();
        root.getChildren().addAll(polygons.values());
        root.getChildren().addAll(texts);
        root.getChildren().add(getGenerateButton(stage));
        Scene scene = new Scene(root, 2000, 1000);
        stage.setScene(scene);
        stage.show();
    }

    private Button getGenerateButton(Stage stage) {
        Button button = new Button("Generate");
        button.setPrefSize(100, 20);
        button.setLayoutX(1700);
        button.setLayoutY(400);
        button.setOnMouseClicked(mouseClicked -> {
            hexagon = new HexagonTileMapGenerator().generate(100, 1600, 1000);
            designResolver.setManagedHexagon(hexagon);
            clickHandler.setHexagon(hexagon);
            Map<HexShape, Polygon> polygons = designResolver.getHexPolygonMap();
            List<Text> texts = textPositionResolver.resolveTextPositions(hexagon);


            initPolygonEvents(polygons);
            Group root = new Group();
            root.getChildren().addAll(polygons.values());
            root.getChildren().addAll(texts);
            root.getChildren().add(button);
            Scene scene = new Scene(root, 2000, 1000);
            stage.setScene(scene);
            stage.show();
        });
        return button;
    }

    private void initPolygonEvents(Map<HexShape, Polygon> hexPolygonMap) {
        hexPolygonMap.forEach((hex, polygon) -> polygon.setOnMouseClicked(event -> {
            if (clickCounter >= 2) {
                clickCounter = 0;
            }
            clickHandler.listen(clickCounter, hex);
            clickCounter++;
        }));
    }
}
