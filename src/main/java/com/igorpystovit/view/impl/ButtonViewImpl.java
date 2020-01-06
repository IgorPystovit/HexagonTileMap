package com.igorpystovit.view.impl;

import com.igorpystovit.converter.HexagonDtoConverter;
import com.igorpystovit.dto.HexagonDto;
import com.igorpystovit.entity.HexShape;
import com.igorpystovit.entity.Hexagon;
import com.igorpystovit.service.ObjectUnmarshallerService;
import com.igorpystovit.service.api.HexagonGeneratorService;
import com.igorpystovit.util.Pair;
import com.igorpystovit.view.HexagonEvent;
import com.igorpystovit.view.HexagonEventType;
import com.igorpystovit.view.api.ButtonView;
import com.igorpystovit.view.api.HexagonView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ButtonViewImpl implements ButtonView {
    private HexagonGeneratorService hexagonGenerator;
    private HexagonView hexagonView;

    @Autowired
    public ButtonViewImpl(HexagonGeneratorService hexagonGenerator) {
        this.hexagonGenerator = hexagonGenerator;
    }

    @Autowired
    public void setHexagonView(HexagonView hexagonView) {
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
            int mapSize = 100;

            if (!textField.getText().isEmpty()) {
                try {
                    mapSize = Integer.parseInt(textField.getText());
                } catch (RuntimeException e) {
                    textField.setStyle("-fx-text-fill: red; -fx-border-width: 2; -fx-border-color: red;");
                }
            }
            Hexagon hexagon = hexagonGenerator.generate(mapSize, 1500, 1000);
            hexagonView.drawHexagon(stage, hexagon);
        });

        textFieldButton.add(button);
        textFieldButton.add(textField);
        return textFieldButton;
    }

    private TextField getHexagonSizeTextField(Button generateButton) {
        TextField textField = new TextField();
        textField.setLayoutY(generateButton.getLayoutY() - 50);
        textField.setLayoutX(generateButton.getLayoutX() - 20);

        textField.setPrefSize(
                generateButton.getPrefWidth() + 40,
                generateButton.getPrefHeight() + 10);
        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                generateButton.getOnMouseClicked().handle(null);
            }
        });

        textField.setPromptText("Hexagon size");
        return textField;
    }

    public List<Node> getImportButtonAndTextField(Stage stage) {
        List<Node> textFieldButton = new ArrayList<>();

        Button importButton = new Button("Import");
        importButton.setPrefSize(100, 20);
        importButton.setLayoutX(1700);
        importButton.setLayoutY(500);

        TextField importTextField = getImportTextField(importButton);
        importButton.setOnMouseClicked(event -> {
            ObjectUnmarshallerService objectUnmarshallerService = new ObjectUnmarshallerService();
            try {
                Hexagon deserializedHexagon = HexagonDtoConverter
                        .convertFromDto(objectUnmarshallerService.fromJson(new File(importTextField.getText()), HexagonDto.class));

                Pair<HexShape> beginEndPair = deserializedHexagon.getShortestPaths().keySet().iterator().next();

                hexagonView.drawHexagon(stage, deserializedHexagon);
                stage.fireEvent(new HexagonEvent<>(HexagonEventType.BEGIN_CLICKED.getEventType(), beginEndPair.getLeft()));
                stage.fireEvent(new HexagonEvent<>(HexagonEventType.END_CLICKED.getEventType(), beginEndPair.getRight()));

            } catch (RuntimeException e) {
                e.printStackTrace();
                importTextField.setStyle("-fx-text-fill: red; -fx-border-width: 2; -fx-border-color: red;");
            }
        });

        textFieldButton.add(importButton);
        textFieldButton.add(importTextField);
        return textFieldButton;
    }

    private TextField getImportTextField(Button importButton) {
        TextField importTextField = new TextField();


        importTextField.setLayoutY(importButton.getLayoutY() - 50);
        importTextField.setLayoutX(importButton.getLayoutX() - 20);

        importTextField.setPrefSize(
                importButton.getPrefWidth() + 40,
                importButton.getPrefHeight() + 10);
        importTextField.setPromptText("Import file path");
        return importTextField;
    }

    public List<Node> getExportTextFieldAndButton(Stage stage) {
        List<Node> textFieldButton = new ArrayList<>();

        Button exportButton = new Button("Export");
        exportButton.setPrefSize(100, 20);
        exportButton.setLayoutX(1700);
        exportButton.setLayoutY(600);

        TextField importTextField = getExportTextField(exportButton);
        exportButton.setOnMouseClicked(event -> {
            ObjectUnmarshallerService objectUnmarshallerService = new ObjectUnmarshallerService();
            try {
                objectUnmarshallerService.toJson(new File(importTextField.getText()),
                        HexagonDtoConverter.convertToDto(hexagonView.getManagedHexagon()));
            } catch (RuntimeException e) {
                importTextField.setStyle("-fx-text-fill: red; -fx-border-width: 2; -fx-border-color: red;");
            }
        });

        textFieldButton.add(exportButton);
        textFieldButton.add(importTextField);
        return textFieldButton;
    }

    private TextField getExportTextField(Button importButton) {
        TextField exportTextField = new TextField();

        exportTextField.setLayoutY(importButton.getLayoutY() - 50);
        exportTextField.setLayoutX(importButton.getLayoutX() - 20);

        exportTextField.setPrefSize(
                importButton.getPrefWidth() + 40,
                importButton.getPrefHeight() + 10);
        exportTextField.setPromptText("Export file path");
        return exportTextField;
    }

}
