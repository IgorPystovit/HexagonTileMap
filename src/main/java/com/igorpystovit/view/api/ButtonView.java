package com.igorpystovit.view.api;

import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.List;

public interface ButtonView {
    List<Node> getGenerateButtonAndTextField(Stage stage);

    List<Node> getImportButtonAndTextField(Stage stage);

    List<Node> getExportButtonAndTextField(Stage stage);
}
