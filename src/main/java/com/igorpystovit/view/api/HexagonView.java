package com.igorpystovit.view.api;

import com.igorpystovit.entity.Hexagon;
import javafx.stage.Stage;

public interface HexagonView {
    void launchView(Stage stage);

    void drawHexagon(Stage stage, Hexagon hexagon);

    Hexagon getManagedHexagon();
}
