package edu.uoregon.nic.nemo.portal.client;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Text;

/**
 */
public class ClusterCircle {

    private final static Integer clusterRadius = 10;
    private final static String fillColor = "gray";
    private final static Integer textHeight = 4;
    private final static Integer characterWidth = 2;
    private final static Integer fontSize = 9;
    private final static String textColor = "white";



    public ClusterCircle(int x, int y, String clusterName,DrawingArea canvas) {
        this(x,y,clusterName,canvas,fillColor);
    }

    public ClusterCircle(int x, int y, String clusterName,DrawingArea canvas,String color) {
        Circle noseCluster = new Circle(x,y,clusterRadius);
        noseCluster.setFillColor(color);
        noseCluster.setStrokeWidth(0);
        canvas.add(noseCluster);
//        Text text = new Text(x-characterWidth*clusterName.length(),y+textHeight,clusterName);
        Text text = new Text(0,0,clusterName);
        text.setStrokeWidth(0);
        text.setFillColor(textColor);
        text.setFontSize(fontSize);
        text.setX(x-text.getTextWidth()/2);
        text.setY(y+text.getTextHeight()/2);
        canvas.add(text);
    }
}
