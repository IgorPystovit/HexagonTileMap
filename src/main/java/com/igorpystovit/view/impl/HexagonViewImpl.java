package com.igorpystovit.view.impl;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.entity.Hexagon;
import com.igorpystovit.resolver.api.DesignResolver;
import com.igorpystovit.resolver.api.TextPositionResolver;
import com.igorpystovit.service.api.HexagonGeneratorService;
import com.igorpystovit.service.api.PathSeekerService;
import com.igorpystovit.util.Pair;
import com.igorpystovit.view.HexagonEvent;
import com.igorpystovit.view.HexagonEventType;
import com.igorpystovit.view.api.ButtonView;
import com.igorpystovit.view.api.HexagonView;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Basic view that manages the internal state all of the elements of {@link Stage}
 * */
@Getter
@Setter
@Slf4j
@Service
public class HexagonViewImpl implements HexagonView {

    private static HexagonEventType nextEvent = HexagonEventType.BEGIN_CLICKED;
    private TextPositionResolver textPositionResolver;
    private DesignResolver designResolver;
    private Hexagon hexagon = new Hexagon();
    private Pair<HexShape> beginEndPair = new Pair<>();
    private PathSeekerService pathSeeker;
    private ButtonView buttonView;
    private HexagonGeneratorService generatorService;

    @Autowired
    public HexagonViewImpl(TextPositionResolver textPositionResolver, DesignResolver designResolver,
                           PathSeekerService pathSeeker, ButtonView buttonView, HexagonGeneratorService generatorService) {
        this.textPositionResolver = textPositionResolver;
        this.designResolver = designResolver;
        this.pathSeeker = pathSeeker;
        this.buttonView = buttonView;
        this.generatorService = generatorService;
    }

    public void launchView(Stage stage) {
        stage.setTitle("HexagonTileMap");
        drawHexagon(stage, generatorService.generate(100, 1500, 1000));
    }

    public void drawHexagon(Stage stage, Hexagon hexagon) {
        designResolver.setManagedHexagon(hexagon);
        this.hexagon = hexagon;

        Map<HexShape, Polygon> polygons = designResolver.getHexPolygonMap();
        List<Text> texts = textPositionResolver.resolveTextPosition(hexagon.getHexes());

        initPolygonEvents(polygons);

        Group root = new Group();

        Scene scene = new Scene(root, 2000, 1000);
        initSceneEvents(stage);
        root.getChildren().addAll(polygons.values());
        root.getChildren().addAll(texts);
        root.getChildren().addAll(buttonView.getGenerateButtonAndTextField(stage));
        root.getChildren().addAll(buttonView.getImportButtonAndTextField(stage));
        root.getChildren().addAll(buttonView.getExportTextFieldAndButton(stage));
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }

    private void initPolygonEvents(Map<HexShape, Polygon> hexPolygonMap) {
        hexPolygonMap.forEach((hex, polygon) -> polygon.setOnMouseClicked(event -> {
            polygon.fireEvent(new HexagonEvent<>(nextEvent.getEventType(), hex));
        }));
    }

    @SuppressWarnings("unchecked")
    private void initSceneEvents(Stage stage) {
        stage.addEventHandler(HexagonEventType.BEGIN_CLICKED.getEventType(), event -> {
            if (event.getClass().equals(HexagonEvent.class)) {
                designResolver.resetToDefault();
                hexagon.getShortestPaths().clear();
                HexagonEvent<HexShape> hexagonEvent = (HexagonEvent<HexShape>) event;
                beginEndPair.setLeft(hexagonEvent.getEventPayload());
                designResolver.highlightHexes(Collections.singletonList(hexagonEvent.getEventPayload().getUuid()));
            }
            nextEvent = HexagonEventType.END_CLICKED;
        });

        stage.addEventHandler(HexagonEventType.END_CLICKED.getEventType(), event -> {
            if (event.getClass().equals(HexagonEvent.class)) {
                HexagonEvent<HexShape> hexagonEvent = (HexagonEvent<HexShape>) event;
                beginEndPair.setRight(hexagonEvent.getEventPayload());

                List<HexShape> shortestPath = pathSeeker
                        .seekPath(hexagon, beginEndPair.getLeft().getUuid(), beginEndPair.getRight().getUuid());

                hexagon.getShortestPaths().put(beginEndPair, shortestPath
                        .stream()
                        .map(HexShape::getUuid)
                        .collect(Collectors.toSet()));

                designResolver.highlightHexes(shortestPath.stream().map(HexShape::getUuid).collect(Collectors.toSet()));
            }
            nextEvent = HexagonEventType.BEGIN_CLICKED;
        });
    }

    @Override
    public Hexagon getManagedHexagon() {
        return hexagon;
    }
}
