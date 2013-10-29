package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import org.vaadin.gwtgraphics.client.shape.Path;

import java.util.List;

/**
 */
public class RoiShape extends Path{

     public static final String OFF_COLOR ="gray";
     public static final String POSITIVE_COLOR ="red";
     public static final String NEGATIVE_COLOR ="blue";
     public static final String BOTH_COLOR ="purple";

    private final Float opacity = 0.5f ;
    private final Integer strokeWidth = 1 ;
    private final BrainLocationEnum brainLocationEnum ;
    private final Search searchParent ;
//    private Path path;
    // assume that the first dimension is the
    public RoiShape(List<Dimension> dimensionList,BrainLocationEnum brainLocationEnum,Search parent) {
        // have to delcare this by default
        super(0,0);
        this.brainLocationEnum = brainLocationEnum ;
        this.searchParent = parent ;
        Dimension startPoint = dimensionList.get(0);
        moveTo(startPoint.getX(), startPoint.getY());
        for(int i = 1 ; i < dimensionList.size() ; i++){
            Dimension dimension = dimensionList.get(i);
            lineTo(dimension.getX(), dimension.getY());
        }
        setFillOpacity(opacity);
        setStrokeWidth(strokeWidth);
        setFillColor(OFF_COLOR);

        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                String color = getFillColor();
                if (color.equals(OFF_COLOR)) {
                    setFillColor(POSITIVE_COLOR);
                } else if (color.equals(POSITIVE_COLOR)) {
                    setFillColor(NEGATIVE_COLOR);
                } else if (color.equals(NEGATIVE_COLOR)) {
                    setFillColor(BOTH_COLOR);
                } else if (color.equals(BOTH_COLOR)) {
                    setFillColor(OFF_COLOR);
                }
                searchParent.doSearch();
            }
        });
    }
}
