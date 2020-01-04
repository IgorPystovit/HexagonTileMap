package com.igorpystovit.view;

import com.igorpystovit.HexShape;
import com.igorpystovit.HexagonTileMapGenerator;
import com.igorpystovit.PathSeeker;
import com.igorpystovit.resolvers.DesignResolver;
import com.igorpystovit.resolvers.TextPositionResolver;
import com.igorpystovit.resolvers.impl.DesignResolverImpl;
import com.igorpystovit.resolvers.impl.TextPositionResolverImpl;
import com.igorpystovit.util.Pair;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HexagonViewImpl implements HexagonView {

    private TextPositionResolver textPositionResolver;
    private DesignResolver designResolverImpl;
    private Set<HexShape> hexagon;
    private static HexagonEventType eventType;
    private Pair<HexShape> hexPair;
    private PathSeeker pathSeeker;


    public HexagonViewImpl(Set<HexShape> hexagon) {
        this.hexagon = hexagon;
        this.designResolverImpl = new DesignResolverImpl(hexagon);
        this.textPositionResolver = new TextPositionResolverImpl();
        this.pathSeeker = new PathSeeker();
        this.hexPair = new Pair<>();
    }

    @SuppressWarnings("unchecked")
    public void launchView(Stage stage) {
        Map<HexShape, Polygon> polygons = designResolverImpl.getHexPolygonMap();
        List<Text> texts = textPositionResolver.resolveTextPosition(hexagon);
        initPolygonEvents(polygons);
        Group root = new Group();

        Scene scene = new Scene(root, 2000, 1000);
        root.getChildren().addAll(polygons.values());
        root.getChildren().addAll(texts);
        root.getChildren().add(getGenerateButton(polygons.values(), root, stage, scene));
        TextField textField = getImportTextField();
        root.getChildren().add(textField);
        root.getChildren().add(getImportButton(textField));


        scene.addEventHandler(HexagonEventType.BEGIN_CLICKED.getEventType(), event -> {
            if (event.getClass().equals(HexagonEvent.class)) {
                HexagonEvent<HexShape> hexagonEvent = (HexagonEvent<HexShape>) event;
                hexPair.setLeft(hexagonEvent.getEventPayload());
                designResolverImpl.playTransitionOn(hexagonEvent.getEventPayload());
            }
        });

        scene.addEventHandler(HexagonEventType.END_CLICKED.getEventType(), event -> {
            if (event.getClass().equals(HexagonEvent.class)) {
                HexagonEvent<HexShape> hexagonEvent = (HexagonEvent<HexShape>) event;
                hexPair.setRight(hexagonEvent.getEventPayload());
                designResolverImpl.playTransitionOn(hexagonEvent.getEventPayload());
                designResolverImpl.highlightHexes(pathSeeker.seekPath(hexagon, hexPair.getLeft(), hexPair.getRight()));
            }
        });
        stage.setTitle("HexagonTileMap");
        ;
        stage.setScene(scene);
        stage.show();
    }

    private Button getImportButton(TextField textField) {
        Button importButton = new Button("Import");
        importButton.setPrefSize(100, 20);
        importButton.setOnMouseClicked(event -> {
            System.out.println(textField.getText());
        });
        importButton.setLayoutX(1700);
        importButton.setLayoutY(500);
        return importButton;
    }

    private TextField getImportTextField() {
        TextField textField = new TextField();
        textField.setLayoutX(1680);
        textField.setLayoutY(450);
        textField.setPrefSize(140, 30);
        textField.setPromptText("Import file path");
        return textField;
    }

    private Button getGenerateButton(Collection<Polygon> oldPolygons, Group group, Stage stage, Scene scene) {
        Button button = new Button("Generate");
        button.setPrefSize(100, 20);
        button.setLayoutX(1700);
        button.setLayoutY(400);
        button.setOnMouseClicked(mouseClicked -> {
            group.getChildren().removeAll(oldPolygons);

            hexagon = new HexagonTileMapGenerator().generate(100, 1300, 1000);
            designResolverImpl.setManagedHexagon(hexagon);

            Map<HexShape, Polygon> polygons = designResolverImpl.getHexPolygonMap();
            List<Text> texts = textPositionResolver.resolveTextPosition(hexagon);

            initPolygonEvents(polygons);

            group.getChildren().addAll(polygons.values());
            group.getChildren().addAll(texts);
            group.getChildren().add(button);
            scene.setRoot(group);

            stage.setScene(scene);
            stage.show();
            System.out.println("here");

        });
        return button;
    }


    private void initPolygonEvents(Map<HexShape, Polygon> hexPolygonMap) {
        hexPolygonMap.forEach((hex, polygon) -> polygon.setOnMouseClicked(event -> {
            if (eventType == null) {
                eventType = HexagonEventType.BEGIN_CLICKED;
            } else if (eventType == HexagonEventType.BEGIN_CLICKED) {
                eventType = HexagonEventType.END_CLICKED;
            } else if (eventType == HexagonEventType.END_CLICKED) {
                designResolverImpl.resetToDefault();
                eventType = HexagonEventType.BEGIN_CLICKED;
            }

            polygon.fireEvent(new HexagonEvent<>(eventType.getEventType(), hex));
        }));
    }


}
