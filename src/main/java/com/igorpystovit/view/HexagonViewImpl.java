package com.igorpystovit.view;

import com.igorpystovit.HexShape;
import com.igorpystovit.Hexagon;
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
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HexagonViewImpl implements HexagonView {

    private TextPositionResolver textPositionResolver;
    private DesignResolver designResolver;
    private Hexagon hexagon;
    private static HexagonEventType eventType;
    private Pair<HexShape> hexPair;
    private PathSeeker pathSeeker;
    private ButtonView buttonView;

    public HexagonViewImpl() {
        this.hexagon = new Hexagon();
        this.designResolver = new DesignResolverImpl();
        this.textPositionResolver = new TextPositionResolverImpl();
        this.pathSeeker = new PathSeeker();
        this.hexPair = new Pair<>();
        this.buttonView = new ButtonView(this);
    }


    public void launchView(Stage stage) {
        stage.setTitle("HexagonTileMap");
        drawHexagon(stage,new HexagonTileMapGenerator().generate(100,1500,1000));
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


    private void initPolygonEvents(Map<HexShape, Polygon> hexPolygonMap) {
        hexPolygonMap.forEach((hex, polygon) -> polygon.setOnMouseClicked(event -> {
            if (eventType == null) {
                eventType = HexagonEventType.BEGIN_CLICKED;
            } else if (eventType == HexagonEventType.BEGIN_CLICKED) {
                eventType = HexagonEventType.END_CLICKED;
            } else if (eventType == HexagonEventType.END_CLICKED) {
                designResolver.resetToDefault();
                eventType = HexagonEventType.BEGIN_CLICKED;
            }

            polygon.fireEvent(new HexagonEvent<>(eventType.getEventType(), hex));
        }));
    }

    @SuppressWarnings("unchecked")
    private void initSceneEvents(Scene scene){
        scene.addEventHandler(HexagonEventType.BEGIN_CLICKED.getEventType(), event -> {
            if (event.getClass().equals(HexagonEvent.class)) {
                HexagonEvent<HexShape> hexagonEvent = (HexagonEvent<HexShape>) event;
                hexPair.setLeft(hexagonEvent.getEventPayload());
                designResolver.playTransitionOn(hexagonEvent.getEventPayload());
            }
        });

        scene.addEventHandler(HexagonEventType.END_CLICKED.getEventType(), event -> {
            if (event.getClass().equals(HexagonEvent.class)) {
                HexagonEvent<HexShape> hexagonEvent = (HexagonEvent<HexShape>) event;
                hexPair.setRight(hexagonEvent.getEventPayload());
                designResolver.playTransitionOn(hexagonEvent.getEventPayload());
                designResolver.highlightHexes(pathSeeker.seekPath(hexagon, hexPair.getLeft(), hexPair.getRight()));
            }
        });
    }

    public void drawHexagon(Stage stage, Hexagon hexagon) {
        designResolver.setManagedHexagon(hexagon);
        this.hexagon = hexagon;

        Map<HexShape, Polygon> polygons = designResolver.getHexPolygonMap();
        List<Text> texts = textPositionResolver.resolveTextPosition(hexagon.getHexes());

        initPolygonEvents(polygons);

        Group root = new Group();

        Scene scene = new Scene(root, 2000, 1000);
        initSceneEvents(scene);
        root.getChildren().addAll(polygons.values());
        root.getChildren().addAll(texts);
        root.getChildren().addAll(buttonView.getGenerateButtonAndTextField(stage));

        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
}
