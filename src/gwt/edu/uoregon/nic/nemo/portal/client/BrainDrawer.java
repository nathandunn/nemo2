package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 */
public class BrainDrawer {

    final Integer brainSize = 400;
    Search parent  ;
    Integer headRadius = Math.round(brainSize / 2.5f);
    Integer brainCenter = brainSize / 2;
    private Map<BrainLocationEnum,RoiShape> roiShapeTreeMap = new TreeMap<BrainLocationEnum,RoiShape>();

    protected DrawingArea drawBrain(Search search) {
        this.parent = search ;

        DrawingArea canvas = new DrawingArea(brainSize,brainSize) ;

        Circle circle = new Circle(brainCenter, brainCenter, headRadius);
        canvas.add(circle);

        // nose
        Integer noseWidth = 40;
        Integer noseHeigth = 30;
        Path nosePath = new Path(brainCenter - noseWidth / 2, brainCenter - headRadius);
        nosePath.lineTo(brainCenter, brainCenter - headRadius - noseHeigth);
        nosePath.lineTo(brainCenter + noseWidth / 2, brainCenter - headRadius);
        canvas.add(nosePath);

        drawLegend(canvas) ;

        drawCircles(canvas);




        createRois();


        for(RoiShape roiShape : roiShapeTreeMap.values()){
            canvas.add(roiShape) ;
        }


        return canvas ;
    }

    private void drawCircles(DrawingArea canvas) {
        // cluster radius
//        Integer clusterRadius = 10 ;
        new ClusterCircle(brainCenter, brainCenter - headRadius, "Nz", canvas);
        new ClusterCircle(brainCenter, brainCenter - headRadius + 40, "Fpz", canvas,"black");
        new ClusterCircle(brainCenter, brainCenter - 70, "Afz", canvas);
        new ClusterCircle(brainCenter-20, brainCenter - 70, "AF1", canvas);
        new ClusterCircle(brainCenter+20, brainCenter - 70, "AF2", canvas);
        new ClusterCircle(brainCenter-45, brainCenter - 70, "AF3", canvas);
        new ClusterCircle(brainCenter+45, brainCenter - 70, "AF4", canvas);
        new ClusterCircle(brainCenter-45, brainCenter - 100, "Fp1", canvas,"black");
        new ClusterCircle(brainCenter+45, brainCenter - 100, "Fp2", canvas,"black");
        new ClusterCircle(brainCenter-65, brainCenter - 70, "AF5", canvas);
        new ClusterCircle(brainCenter+65, brainCenter - 70, "AF6", canvas);
        new ClusterCircle(brainCenter-85, brainCenter - 80, "AF7", canvas);
        new ClusterCircle(brainCenter+85, brainCenter - 80, "AF8", canvas);
        new ClusterCircle(brainCenter, brainCenter - 40, "Fz", canvas,"black");
        new ClusterCircle(brainCenter-30, brainCenter - 40, "F1", canvas);
        new ClusterCircle(brainCenter+30, brainCenter - 40, "F2", canvas);

        new ClusterCircle(brainCenter-55, brainCenter - 40, "F3", canvas,"black");
        new ClusterCircle(brainCenter+55, brainCenter - 40, "F4", canvas,"black");

        new ClusterCircle(brainCenter-75, brainCenter - 45, "F5", canvas);
        new ClusterCircle(brainCenter+75, brainCenter - 45, "F6", canvas);

        new ClusterCircle(brainCenter-100, brainCenter - 50, "F7", canvas,"black");
        new ClusterCircle(brainCenter+100, brainCenter - 50, "F8", canvas,"black");

        new ClusterCircle(brainCenter-130, brainCenter - 75, "F9", canvas);
        new ClusterCircle(brainCenter+130, brainCenter - 75, "F10", canvas);


        new ClusterCircle(brainCenter, brainCenter -10, "FCz", canvas);
        new ClusterCircle(brainCenter, brainCenter +10, "Cz", canvas,"black");
        new ClusterCircle(brainCenter, brainCenter +40, "CPz", canvas);
        new ClusterCircle(brainCenter, brainCenter +60, "Pz", canvas,"black");
        new ClusterCircle(brainCenter, brainCenter +90, "POz", canvas);

        new ClusterCircle(brainCenter-20, brainCenter +90, "PO1", canvas);
        new ClusterCircle(brainCenter+20, brainCenter +90, "PO2", canvas);

        new ClusterCircle(brainCenter-40, brainCenter +90, "PO3", canvas);
        new ClusterCircle(brainCenter+40, brainCenter +90, "PO4", canvas);

        new ClusterCircle(brainCenter-60, brainCenter +90, "PO5", canvas);
        new ClusterCircle(brainCenter+60, brainCenter +90, "PO6", canvas);

        new ClusterCircle(brainCenter-75, brainCenter +100, "PO7", canvas);
        new ClusterCircle(brainCenter+75, brainCenter +100, "PO8", canvas);

        new ClusterCircle(brainCenter-105, brainCenter +115, "PO9", canvas);
        new ClusterCircle(brainCenter+105, brainCenter +115, "PO10", canvas);

        new ClusterCircle(brainCenter, brainCenter +115, "Oz", canvas,"black");
        new ClusterCircle(brainCenter-35, brainCenter +115, "O1", canvas,"black");
        new ClusterCircle(brainCenter+35, brainCenter +115, "O2", canvas,"black");

        new ClusterCircle(brainCenter, brainCenter +140, "Iz", canvas);
        new ClusterCircle(brainCenter-40, brainCenter +140, "I1", canvas);
        new ClusterCircle(brainCenter+40, brainCenter +140, "I2", canvas);


        new ClusterCircle(brainCenter-30, brainCenter -10, "FC1", canvas);
        new ClusterCircle(brainCenter+30, brainCenter -10, "FC2", canvas);
        new ClusterCircle(brainCenter-30, brainCenter +10, "C1", canvas);
        new ClusterCircle(brainCenter+30, brainCenter +10, "C2", canvas);


        new ClusterCircle(brainCenter-55, brainCenter -10, "FC3", canvas);
        new ClusterCircle(brainCenter+55, brainCenter -10, "FC4", canvas);
        new ClusterCircle(brainCenter-55, brainCenter +10, "C3", canvas,"black");
        new ClusterCircle(brainCenter+55, brainCenter +10, "C4", canvas,"black");

        new ClusterCircle(brainCenter-80, brainCenter -10, "FC5", canvas);
        new ClusterCircle(brainCenter+80, brainCenter -10, "FC6", canvas);
        new ClusterCircle(brainCenter-85, brainCenter +10, "C5", canvas);
        new ClusterCircle(brainCenter+85, brainCenter +10, "C6", canvas);


        new ClusterCircle(brainCenter-105, brainCenter -10, "FT7", canvas);
        new ClusterCircle(brainCenter+105, brainCenter -10, "FT8", canvas);
        new ClusterCircle(brainCenter-110, brainCenter +10, "T7", canvas,"black");
        new ClusterCircle(brainCenter+110, brainCenter +10, "T8", canvas,"black");

        new ClusterCircle(brainCenter-140, brainCenter -25, "FT9", canvas);
        new ClusterCircle(brainCenter+140, brainCenter -25, "FT10", canvas);
        new ClusterCircle(brainCenter-145, brainCenter +5, "T9", canvas);
        new ClusterCircle(brainCenter+145, brainCenter +5, "T10", canvas);



        new ClusterCircle(brainCenter-30, brainCenter +40, "CP1", canvas);
        new ClusterCircle(brainCenter+30, brainCenter +40, "CP2", canvas);
        new ClusterCircle(brainCenter-30, brainCenter +60, "P1", canvas);
        new ClusterCircle(brainCenter+30, brainCenter +60, "P2", canvas);

        new ClusterCircle(brainCenter-55, brainCenter +40, "CP3", canvas);
        new ClusterCircle(brainCenter+55, brainCenter +40, "CP4", canvas);
        new ClusterCircle(brainCenter-55, brainCenter +60, "P3", canvas,"black");
        new ClusterCircle(brainCenter+55, brainCenter +60, "P4", canvas,"black");


        new ClusterCircle(brainCenter-85, brainCenter +40, "CP5", canvas);
        new ClusterCircle(brainCenter+85, brainCenter +40, "CP6", canvas);
        new ClusterCircle(brainCenter-75, brainCenter +60, "P5", canvas);
        new ClusterCircle(brainCenter+75, brainCenter +60, "P6", canvas);


        new ClusterCircle(brainCenter-105, brainCenter +40, "TP7", canvas);
        new ClusterCircle(brainCenter+105, brainCenter +40, "TP8", canvas);
        new ClusterCircle(brainCenter-95, brainCenter +65, "P7", canvas,"black");
        new ClusterCircle(brainCenter+95, brainCenter +65, "P8", canvas,"black");

        new ClusterCircle(brainCenter-140, brainCenter +45, "TP9", canvas);
        new ClusterCircle(brainCenter+140, brainCenter +45, "TP10", canvas);
        new ClusterCircle(brainCenter-130, brainCenter +70, "P9", canvas);
        new ClusterCircle(brainCenter+130, brainCenter +70, "P10", canvas);


    }

    private class SelectAllClickHandler implements ClickHandler{

        SelectedLocationEnum selectedLocationEnum ;

        public SelectAllClickHandler(SelectedLocationEnum selectedLocationEnum){
            this.selectedLocationEnum = selectedLocationEnum ;
        }

        @Override
        public void onClick(ClickEvent clickEvent) {
            selectAllLocations(this.selectedLocationEnum);
        }
    }

    private void drawLegend(DrawingArea canvas) {
        Text text = new Text(0,15,"Select Polarity:");
        text.setFillColor("black");
        text.setStrokeWidth(0);
        text.setStrokeColor("black");
        text.setFontSize(14);
        canvas.add(text);

        Integer legendY = 20 ;
        Integer textLegendy= legendY+15 ;
        Rectangle legendOff = new Rectangle(0,legendY,40,20);
        legendOff.setFillColor(RoiShape.OFF_COLOR);
        legendOff.setStrokeWidth(0);
        legendOff.setFillOpacity(0.5);
//        legendOff.setStylePrimaryName("clickable");
        canvas.add(legendOff) ;
        Text textOff = new Text(3,textLegendy,"None");
        textOff.setFillColor("white");
        textOff.setStrokeWidth(0);
        textOff.setFontSize(12);
        canvas.add(textOff) ;
        legendOff.addClickHandler(new SelectAllClickHandler(SelectedLocationEnum.OFF));
        textOff.addClickHandler(new SelectAllClickHandler(SelectedLocationEnum.OFF));

        Rectangle legendPositive = new Rectangle(50,legendY,13,20);
        legendPositive.setFillColor(RoiShape.POSITIVE_COLOR);
        legendPositive.setStrokeWidth(0);
        legendPositive.setFillOpacity(0.5);
        canvas.add(legendPositive) ;
        Text textPositive = new Text(53,textLegendy,"+");
        textPositive.setFillColor("white");
        textPositive.setStrokeWidth(0);
        textPositive.setFontSize(12);
        canvas.add(textPositive) ;
        legendPositive.addClickHandler(new SelectAllClickHandler(SelectedLocationEnum.POSITIVE));
        textPositive.addClickHandler(new SelectAllClickHandler(SelectedLocationEnum.POSITIVE));

        Rectangle legendNegative = new Rectangle(70,legendY,13,20);
        legendNegative.setFillColor(RoiShape.NEGATIVE_COLOR);
        legendNegative.setStrokeWidth(0);
        legendNegative.setFillOpacity(0.5);
        canvas.add(legendNegative) ;
        Text textNegative = new Text(73,textLegendy,"-");
        textNegative.setFillColor("white");
        textNegative.setStrokeWidth(0);
        textNegative.setFontSize(12);
        canvas.add(textNegative) ;
        legendNegative.addClickHandler(new SelectAllClickHandler(SelectedLocationEnum.NEGATIVE));
        textNegative.addClickHandler(new SelectAllClickHandler(SelectedLocationEnum.NEGATIVE));

        Rectangle legendBoth = new Rectangle(90,legendY,23,20);
        legendBoth.setFillColor(RoiShape.BOTH_COLOR);
        legendBoth.setStrokeWidth(0);
        legendBoth.setFillOpacity(0.5);
        canvas.add(legendBoth) ;
        Text textBoth = new Text(93,textLegendy,"+/-");
        textBoth.setFillColor("white");
        textBoth.setStrokeWidth(0);
        textBoth.setFontSize(12);
        canvas.add(textBoth) ;
        legendBoth.addClickHandler(new SelectAllClickHandler(SelectedLocationEnum.BOTH));
        textBoth.addClickHandler(new SelectAllClickHandler(SelectedLocationEnum.BOTH));
    }

    private void selectAllLocations(SelectedLocationEnum selectedLocationEnum) {
        for(RoiShape roiShape : roiShapeTreeMap.values()){
            switch (selectedLocationEnum){
                case OFF: roiShape.setFillColor(RoiShape.OFF_COLOR);
                    break;
                case BOTH: roiShape.setFillColor(RoiShape.BOTH_COLOR);
                    break;
                case POSITIVE: roiShape.setFillColor(RoiShape.POSITIVE_COLOR);
                    break;
                case NEGATIVE: roiShape.setFillColor(RoiShape.NEGATIVE_COLOR);
                    break;
                default:
                    Window.alert("A problem with your selection");
                    break ;
            }
        }

        parent.doSearch();
    }

    private void createRois() {

        List<Dimension> midFrontalDimensions = new ArrayList<Dimension>();
        midFrontalDimensions.add(new Dimension(brainCenter - 50, brainCenter - 30));
        midFrontalDimensions.add(new Dimension(brainCenter, brainCenter - headRadius+10));
        midFrontalDimensions.add(new Dimension(brainCenter + 50, brainCenter - 30));
        midFrontalDimensions.add(new Dimension(brainCenter - 50, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.MFRONT, new RoiShape(midFrontalDimensions, BrainLocationEnum.MFRONT, parent));

        List<Dimension> leftFrontalDimensions = new ArrayList<Dimension>();
        leftFrontalDimensions.add(new Dimension(brainCenter - 50, brainCenter - 30));
        leftFrontalDimensions.add(new Dimension(brainCenter - 90, brainCenter - 30));
        leftFrontalDimensions.add(new Dimension(brainCenter - 70, brainCenter - 80));
        leftFrontalDimensions.add(new Dimension(brainCenter - 30, brainCenter - 80));
        leftFrontalDimensions.add(new Dimension(brainCenter - 50, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.LFRONT, new RoiShape(leftFrontalDimensions, BrainLocationEnum.LFRONT, parent));


        List<Dimension> leftFrontalTemporalDimensions = new ArrayList<Dimension>();
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 90, brainCenter - 30));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 70, brainCenter - 80));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 30, brainCenter - 80));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 40, brainCenter - 150));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 140, brainCenter - 80));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 90, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.LFTEMP, new RoiShape(leftFrontalTemporalDimensions, BrainLocationEnum.LFTEMP, parent));


        List<Dimension> rightFrontalTemporalDimensions = new ArrayList<Dimension>();
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 90, brainCenter - 30));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 70, brainCenter - 80));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 30, brainCenter - 80));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 40, brainCenter - 150));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 140, brainCenter - 80));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 90, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.RFTEMP, new RoiShape(rightFrontalTemporalDimensions, BrainLocationEnum.RFTEMP, parent));


        List<Dimension> rightFrontalDimensions = new ArrayList<Dimension>();
        rightFrontalDimensions.add(new Dimension(brainCenter + 50, brainCenter - 30));
        rightFrontalDimensions.add(new Dimension(brainCenter + 90, brainCenter - 30));
        rightFrontalDimensions.add(new Dimension(brainCenter + 70, brainCenter - 80));
        rightFrontalDimensions.add(new Dimension(brainCenter + 30, brainCenter - 80));
        rightFrontalDimensions.add(new Dimension(brainCenter + 50, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.RFRONT, new RoiShape(rightFrontalDimensions, BrainLocationEnum.RFRONT, parent));

        List<Dimension> midCentralDimensions = new ArrayList<Dimension>();
        midCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter - 20));
        midCentralDimensions.add(new Dimension(brainCenter + 40, brainCenter - 20));
        midCentralDimensions.add(new Dimension(brainCenter + 40, brainCenter + 20));
        midCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter + 20));
        midCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter - 20));
        roiShapeTreeMap.put(BrainLocationEnum.MCENT, new RoiShape(midCentralDimensions, BrainLocationEnum.MCENT, parent));

        List<Dimension> leftCentralDimensions = new ArrayList<Dimension>();
        leftCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter - 20));
        leftCentralDimensions.add(new Dimension(brainCenter - 90, brainCenter - 20));
        leftCentralDimensions.add(new Dimension(brainCenter - 100, brainCenter + 20));
        leftCentralDimensions.add(new Dimension(brainCenter - 45, brainCenter + 20));
        leftCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter - 20));
        roiShapeTreeMap.put(BrainLocationEnum.LCENT, new RoiShape(leftCentralDimensions, BrainLocationEnum.LCENT, parent));


        List<Dimension> leftCentroTemporalDimensions = new ArrayList<Dimension>();
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 150, brainCenter - 40));
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 90, brainCenter - 20));
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 100, brainCenter + 20));
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 160, brainCenter + 15));
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 150, brainCenter - 40));
        roiShapeTreeMap.put(BrainLocationEnum.LCTEMP, new RoiShape(leftCentroTemporalDimensions, BrainLocationEnum.LCTEMP, parent));

        List<Dimension> rightCentroTemporalDimensions = new ArrayList<Dimension>();
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 150, brainCenter - 40));
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 90, brainCenter - 20));
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 100, brainCenter + 20));
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 160, brainCenter + 15));
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 150, brainCenter - 40));
        roiShapeTreeMap.put(BrainLocationEnum.RCTEMP, new RoiShape(rightCentroTemporalDimensions, BrainLocationEnum.RCTEMP, parent));

        List<Dimension> rightCentralDimensions = new ArrayList<Dimension>();
        rightCentralDimensions.add(new Dimension(brainCenter + 40, brainCenter - 20));
        rightCentralDimensions.add(new Dimension(brainCenter + 90, brainCenter - 20));
        rightCentralDimensions.add(new Dimension(brainCenter + 100, brainCenter + 20));
        rightCentralDimensions.add(new Dimension(brainCenter + 45, brainCenter + 20));
        rightCentralDimensions.add(new Dimension(brainCenter + 40, brainCenter - 20));
        roiShapeTreeMap.put(BrainLocationEnum.RCENT, new RoiShape(rightCentralDimensions, BrainLocationEnum.RCENT, parent));

        List<Dimension> midParietalDimensions = new ArrayList<Dimension>();
        midParietalDimensions.add(new Dimension(brainCenter - 50, brainCenter + 30));
        midParietalDimensions.add(new Dimension(brainCenter + 50, brainCenter + 30));
        midParietalDimensions.add(new Dimension(brainCenter + 40, brainCenter + 70));
        midParietalDimensions.add(new Dimension(brainCenter - 40, brainCenter + 70));
        midParietalDimensions.add(new Dimension(brainCenter - 50, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.MPAR, new RoiShape(midParietalDimensions, BrainLocationEnum.MPAR, parent));


        List<Dimension> leftParietalDimensions = new ArrayList<Dimension>();
        leftParietalDimensions.add(new Dimension(brainCenter - 50, brainCenter + 30));
        leftParietalDimensions.add(new Dimension(brainCenter - 100, brainCenter + 30));
        leftParietalDimensions.add(new Dimension(brainCenter - 80, brainCenter + 70));
        leftParietalDimensions.add(new Dimension(brainCenter - 40, brainCenter + 70));
        leftParietalDimensions.add(new Dimension(brainCenter - 50, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.LPAR, new RoiShape(leftParietalDimensions, BrainLocationEnum.LFRONT, parent));

        List<Dimension> leftParetoTemporalDimensions = new ArrayList<Dimension>();
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 100, brainCenter + 30));
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 160, brainCenter + 40));
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 130, brainCenter + 90));
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 80, brainCenter + 70));
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 100, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.LPTEMP, new RoiShape(leftParetoTemporalDimensions, BrainLocationEnum.LPTEMP, parent));

        List<Dimension> rightParetoTemporalDimensions = new ArrayList<Dimension>();
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 100, brainCenter + 30));
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 160, brainCenter + 40));
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 130, brainCenter + 90));
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 80, brainCenter + 70));
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 100, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.RPTEMP, new RoiShape(rightParetoTemporalDimensions, BrainLocationEnum.RPTEMP, parent));

        List<Dimension> rightParietalDimensions = new ArrayList<Dimension>();
        rightParietalDimensions.add(new Dimension(brainCenter + 50, brainCenter + 30));
        rightParietalDimensions.add(new Dimension(brainCenter + 100, brainCenter + 30));
        rightParietalDimensions.add(new Dimension(brainCenter + 80, brainCenter + 70));
        rightParietalDimensions.add(new Dimension(brainCenter + 40, brainCenter + 70));
        rightParietalDimensions.add(new Dimension(brainCenter + 50, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.RPAR, new RoiShape(rightParietalDimensions, BrainLocationEnum.RFRONT, parent));

        List<Dimension> midOccipitalDimensions = new ArrayList<Dimension>();
        midOccipitalDimensions.add(new Dimension(brainCenter - 30, brainCenter  + 80));
        midOccipitalDimensions.add(new Dimension(brainCenter + 30, brainCenter  + 80));
        midOccipitalDimensions.add(new Dimension(brainCenter , brainCenter + headRadius ));
        midOccipitalDimensions.add(new Dimension(brainCenter - 30, brainCenter  + 80));
        roiShapeTreeMap.put(BrainLocationEnum.MOCC, new RoiShape(midOccipitalDimensions, BrainLocationEnum.MOCC, parent));

        List<Dimension> leftOccipitalDimensions = new ArrayList<Dimension>();
        leftOccipitalDimensions.add(new Dimension(brainCenter - 30, brainCenter  + 80));
        leftOccipitalDimensions.add(new Dimension(brainCenter - 70, brainCenter  + 85));
        leftOccipitalDimensions.add(new Dimension(brainCenter - 40, brainCenter  + 120));
        leftOccipitalDimensions.add(new Dimension(brainCenter - 20, brainCenter  + 110));
        leftOccipitalDimensions.add(new Dimension(brainCenter - 30, brainCenter  + 80));
        roiShapeTreeMap.put(BrainLocationEnum.LOCC, new RoiShape(leftOccipitalDimensions, BrainLocationEnum.LOCC, parent));

        List<Dimension> leftOccipitalTemporalDimensions = new ArrayList<Dimension>();
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 70, brainCenter  + 85));
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 25, brainCenter  + 140));
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 35, brainCenter  + 160));
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 130, brainCenter  + 100));
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 70, brainCenter  + 85));
        roiShapeTreeMap.put(BrainLocationEnum.LOTEMP, new RoiShape(leftOccipitalTemporalDimensions, BrainLocationEnum.LOTEMP, parent));

        List<Dimension> rightOccipitalDimensions = new ArrayList<Dimension>();
        rightOccipitalDimensions.add(new Dimension(brainCenter + 30, brainCenter  + 80));
        rightOccipitalDimensions.add(new Dimension(brainCenter + 70, brainCenter  + 85));
        rightOccipitalDimensions.add(new Dimension(brainCenter + 40, brainCenter  + 120));
        rightOccipitalDimensions.add(new Dimension(brainCenter + 20, brainCenter  + 110));
        rightOccipitalDimensions.add(new Dimension(brainCenter + 30, brainCenter  + 80));
        roiShapeTreeMap.put(BrainLocationEnum.ROCC, new RoiShape(rightOccipitalDimensions, BrainLocationEnum.ROCC, parent));

        List<Dimension> rightOccipitalTemporalDimensions = new ArrayList<Dimension>();
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 70, brainCenter  + 85));
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 25, brainCenter  + 140));
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 35, brainCenter  + 160));
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 130, brainCenter  + 100));
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 70, brainCenter  + 85));
        roiShapeTreeMap.put(BrainLocationEnum.ROTEMP, new RoiShape(rightOccipitalTemporalDimensions, BrainLocationEnum.ROTEMP, parent));

    }

    public Map<BrainLocationEnum, RoiShape> getRoiShapeTreeMap() {
        return roiShapeTreeMap;
    }

    public void setRoiShapeTreeMap(Map<BrainLocationEnum, RoiShape> roiShapeTreeMap) {
        this.roiShapeTreeMap = roiShapeTreeMap;
    }
}
