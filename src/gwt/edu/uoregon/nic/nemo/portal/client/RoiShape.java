package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

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
    private final BrainSearchable searchParent ;
    private final List<Dimension> dimensionList ;
//    private Path path;
    // assume that the first dimension is the
    public RoiShape(List<Dimension> dimensionList,BrainLocationEnum brainLocationEnum,BrainSearchable parent) {
        // have to delcare this by default
        super(0,0);
        this.brainLocationEnum = brainLocationEnum ;
        this.searchParent = parent ;
        this.dimensionList = dimensionList ;
        Dimension startPoint = this.dimensionList.get(0);
        moveTo(startPoint.getX(), startPoint.getY());
        for(int i = 1 ; i < this.dimensionList.size() ; i++){
            Dimension dimension = this.dimensionList.get(i);
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

    public void drawMeanIntensity(DrawingArea drawingArea,Double doubleValue) {
        // take average X and Y and try?
        Float averageX = 0f ;
        Float averageY = 0f;
        for(Dimension dimension : dimensionList){
            averageX += dimension.getX();
            averageY += dimension.getY();
        }
        averageX = averageX / (float) dimensionList.size();
        averageX -= 15 ;
        averageY = averageY / (float) dimensionList.size();

        Rectangle rectangle = new Rectangle(averageX.intValue()-5,averageY.intValue()-10,50,15);
        rectangle.setFillColor("white");
        rectangle.setFillOpacity(0.5f);
        rectangle.setStrokeWidth(0);
        rectangle.setRoundedCorners(8);
        drawingArea.add(rectangle);
        Text text = new Text(averageX.intValue(),averageY.intValue(),NumberFormat.getFormat("0.000").format(doubleValue));
        text.setStrokeColor("black");
        text.setFillColor("black");
        text.setFontFamily("courier");
        text.setFontSize(12);
        drawingArea.add(text);

    }
}
