package com.igorpystovit;

import com.igorpystovit.resolvers.DesignResolver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class ClickHandler {
    private DesignResolver designResolver;
    private Set<HexShape> hexagon;
    private BeginEndPair beginEndPair;
    private PathSeeker pathSeeker = new PathSeeker();

    public ClickHandler(Set<HexShape> hexagon, DesignResolver designResolver) {
        this.designResolver = designResolver;
        this.hexagon = hexagon;
        this.beginEndPair = new BeginEndPair();
    }

    public void listen(int numberOfClicks, HexShape clickedHex) {
        switch (numberOfClicks) {
            case 0:
                designResolver.resetToDefault();
                beginEndPair.setBegin(clickedHex);
                designResolver.playTransitionOn(clickedHex);
                break;
            case 1:
                beginEndPair.setEnd(clickedHex);
                Set<HexShape> path = pathSeeker.seekPath(hexagon, beginEndPair.getBegin(), beginEndPair.getEnd());
                designResolver.playTransitionOn(clickedHex);
                designResolver.highlightHexes(path);
                break;
            default:
                throw new RuntimeException();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    private class BeginEndPair {
        private HexShape begin;
        private HexShape end;
    }
}
