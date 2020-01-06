package com.igorpystovit.resolver.api;

import com.igorpystovit.entity.HexShape;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.List;

public interface TextPositionResolver {
    List<Text> resolveTextPosition(Collection<HexShape> shapes);
}
