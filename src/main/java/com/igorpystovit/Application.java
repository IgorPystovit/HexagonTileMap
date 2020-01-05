package com.igorpystovit;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Application extends javafx.application.Application {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Hexagon hexagon = new Hexagon();
        HexagonService hexagonService = new HexagonServiceImpl(hexagon);
        HexShape rootHex = new HexShape(400,500,11);
        rootHex.setRoot(true);
        HexShape hex1 = new HexShape(5);
        HexShape hex2 = new HexShape(12);
        HexShape hex3 = new HexShape(60);
        HexShape hex4 = new HexShape(80);
        hexagonService.addRootHex(rootHex);
        hexagonService.addHex(rootHex,hex1);
        hexagonService.addHex(rootHex,hex2);
        hexagonService.addHex(hex1,hex3);
        hexagonService.addHex(hex1,hex4);
        objectMapper.writeValue(new File("/home/wage/JavaGIT/HexagonTileMap/HexagonTileMap/src/main/resources/hexagon.json"),HexShapeDtoConverter.convertToDto(hex1));
//        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Hexagon hexagon = new Hexagon();
        HexagonService hexagonService = new HexagonServiceImpl(hexagon);
        HexShape rootHex = new HexShape(400,500,11);
        rootHex.setRoot(true);
        HexShape hex1 = new HexShape(5);
        HexShape hex2 = new HexShape(12);
        HexShape hex3 = new HexShape(60);
        HexShape hex4 = new HexShape(80);
        hexagonService.addRootHex(rootHex);
        hexagonService.addHex(rootHex,hex1);
        hexagonService.addHex(rootHex,hex2);
        hexagonService.addHex(hex1,hex3);
        hexagonService.addHex(hex1,hex4);

//        new HexagonViewImpl().drawHexagon(primaryStage,hexagon);
    }
}
