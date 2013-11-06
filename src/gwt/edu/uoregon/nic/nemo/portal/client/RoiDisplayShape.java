package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.List;

/**
 */
public class RoiDisplayShape extends RoiShape{

//     public static final String OFF_COLOR ="gray";
//     public static final String POSITIVE_COLOR ="red";
//     public static final String NEGATIVE_COLOR ="blue";
//     public static final String BOTH_COLOR ="purple";

//    private final Float opacity = 0.5f ;
//    private final Integer strokeWidth = 1 ;
//    private final BrainLocationEnum brainLocationEnum ;
//    private final BrainSearchable searchParent ;
//    private final List<Dimension> dimensionList ;

//    private Text valueText = null ;
//    private Rectangle valueBackground = null ;
//    private Path path;
    // assume that the first dimension is the


    public RoiDisplayShape(List<Dimension> dimensionList, BrainLocationEnum brainLocationEnum, BrainSearchable parent) {
        // have to delcare this by default
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
//        setStyleName("clickable");
        if(this.brainLocationEnum!=null){
            getElement().getStyle().setCursor(Style.Cursor.POINTER);

            addClickHandler(new ClickHandler() {
                @Override
                // http://localhost:8080/nemo/erpAnalysisResult/showIndividualsAtLocation/15?locationName=MFRONT
                public void onClick(ClickEvent clickEvent) {
                    Long erpAnalysisResultId = searchParent.getId();
                    String newUrl = searchParent.getBaseUrl();
                    newUrl += "/" + erpAnalysisResultId;
                    newUrl += "?locationName=" + getBrainLocationEnum().name();
                    Window.Location.replace(newUrl);
                }
            });
        }
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

        if(valueBackground==null){
            valueBackground= new Rectangle(averageX.intValue()-5,averageY.intValue()-10,50,15);
            valueBackground.setFillColor("white");
            valueBackground.setFillOpacity(0.5f);
            valueBackground.setStrokeWidth(0);
            valueBackground.setRoundedCorners(8);
            drawingArea.add(valueBackground);
        }
        String numberString = NumberFormat.getFormat("0.000").format(doubleValue);
        if(!numberString.startsWith("-")){
            numberString = "+"+ numberString;
        }
        if(valueText==null){
            valueText = new Text(averageX.intValue(),averageY.intValue(),numberString);
            drawingArea.add(valueText);
        }
        else{
            valueText.setText(numberString);
        }
        valueText.setStrokeColor("black");
        valueText.setFillColor("black");
        valueText.setFontFamily("courier");
        valueText.setFontSize(12);

        valueText.setVisible(true);
        valueBackground.setVisible(true);
    }

    public void removeValue() {
        if(valueText!=null){
            valueText.setVisible(false);
        }
        if(valueBackground!=null){
            valueBackground.setVisible(false);
        }
    }
}
