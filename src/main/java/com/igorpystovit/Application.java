package com.igorpystovit;

import com.igorpystovit.view.HexagonViewImpl;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class Application extends javafx.application.Application {
    private Set<HexShape> hexagon;

    private final int WIDTH = 1600;
    private final int HEIGHT = 800;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        hexagon = new HexagonTileMapGenerator().generate(100, WIDTH, HEIGHT);
        new HexagonViewImpl(hexagon).launchView(primaryStage);
    }
}
