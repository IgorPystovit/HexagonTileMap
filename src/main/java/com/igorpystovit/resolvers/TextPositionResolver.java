package com.igorpystovit.resolvers;

import com.igorpystovit.HexShape;
import com.igorpystovit.util.Pair;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextPositionResolver {

    public List<Text> resolveTextPositions(Collection<HexShape> shapes){
        List<Text> texts = new ArrayList<>();

        for (HexShape tempShape : shapes){
            Pair centerPair = calculateTextCenter(tempShape);
            Text text = new Text();
            text.setFill(Color.BLACK);
            text.setFont(Font.font("Times New Roman",HexShape.SPOT_SIZE - (HexShape.SPOT_SIZE / 2.0)));
            text.setText(String.valueOf(tempShape.getValue()));
            text.setX(centerPair.getX() - ((text.getText().length() - 1) * 6));
            text.setY(centerPair.getY());
            texts.add(text);
        }
        return texts;
    }

    private Pair calculateTextCenter(HexShape shape){
        Pair thirdPoint = shape.getCoordinateMap().get(3);
        Pair sixthPoint = shape.getCoordinateMap().get(6);

        return new Pair(
                (sixthPoint.getX() + (thirdPoint.getX() - sixthPoint.getX()) / 2)-(HexShape.SPOT_SIZE / 10.0),
                thirdPoint.getY()+(HexShape.SPOT_SIZE / 5.0));
    }
}
