package com.igorpystovit.resolvers;

import com.igorpystovit.HexShape;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.List;

public interface TextPositionResolver {
    List<Text> resolveTextPosition(Collection<HexShape> shapes);
}
