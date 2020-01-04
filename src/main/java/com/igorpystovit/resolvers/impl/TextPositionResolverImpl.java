package com.igorpystovit.resolvers.impl;

import com.igorpystovit.HexShape;
import com.igorpystovit.resolvers.TextPositionResolver;
import com.igorpystovit.util.Pair;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextPositionResolverImpl implements TextPositionResolver {

    public List<Text> resolveTextPosition(Collection<HexShape> shapes) {
        List<Text> texts = new ArrayList<>();

        for (HexShape tempShape : shapes) {
            Pair<Double> centerPair = calculateTextCenter(tempShape);
            Text text = new Text();
            text.setFill(Color.BLACK);
            text.setFont(Font.font("Times New Roman", HexShape.SPOT_SIZE - 5 - (HexShape.SPOT_SIZE / 2.0)));
            text.setText(String.valueOf(tempShape.getValue()));
            text.setX(centerPair.getLeft() - ((text.getText().length() - 1) * 6));
            text.setY(centerPair.getRight());
            texts.add(text);
        }
        return texts;
    }

    private Pair<Double> calculateTextCenter(HexShape shape) {
        Pair<Double> thirdPoint = shape.getCoordinateMap().get(3);
        Pair<Double> sixthPoint = shape.getCoordinateMap().get(6);

        return new Pair<>(
                (sixthPoint.getLeft() + (thirdPoint.getLeft() - sixthPoint.getLeft()) / 2) - (HexShape.SPOT_SIZE / 10.0),
                thirdPoint.getRight() + (HexShape.SPOT_SIZE / 5.0));
    }
}
