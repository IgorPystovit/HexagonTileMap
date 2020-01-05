package com.igorpystovit.view;

import com.igorpystovit.Hexagon;
import javafx.stage.Stage;

public interface HexagonView {
    void launchView(Stage stage);
    void drawHexagon(Stage stage, Hexagon hexagon);
}
