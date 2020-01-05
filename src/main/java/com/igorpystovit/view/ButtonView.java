package com.igorpystovit.view;

import com.igorpystovit.Hexagon;
import com.igorpystovit.HexagonTileMapGenerator;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ButtonView {
    private HexagonTileMapGenerator hexagonGenerator;
    private HexagonView hexagonView;

    public ButtonView(HexagonView hexagonView) {
        this.hexagonGenerator = new HexagonTileMapGenerator();
        this.hexagonView = hexagonView;
    }

    public List<Node> getGenerateButtonAndTextField(Stage stage) {
        List<Node> textFieldButton = new ArrayList<>();

        Button button = new Button("Generate");
        button.setPrefSize(100, 20);
        button.setLayoutX(1700);
        button.setLayoutY(400);

        TextField textField = getHexagonSizeTextField(button);

        button.setOnMouseClicked(mouseClicked -> {
            int mapSize;

            if (textField.getText().isEmpty()){
                mapSize = 100;
            }
            else{
                mapSize = Integer.parseInt(textField.getText());
            }

            Hexagon hexagon = hexagonGenerator.generate(mapSize, 1500, 1000);
            hexagonView.drawHexagon(stage,hexagon);
        });

        textFieldButton.add(button);
        textFieldButton.add(textField);
        return textFieldButton;
    }

    private TextField getHexagonSizeTextField(Button generateButton){
        TextField textField = new TextField();
        textField.setLayoutY(generateButton.getLayoutY() - 50);
        textField.setLayoutX(generateButton.getLayoutX() - 20);

        textField.setPrefSize(
                generateButton.getPrefWidth() + 40,
                generateButton.getPrefHeight() + 10);
        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)){
                generateButton.getOnMouseClicked().handle(null);
            };
        });

        textField.setPromptText("Import file path");
        return textField;
    }
}
