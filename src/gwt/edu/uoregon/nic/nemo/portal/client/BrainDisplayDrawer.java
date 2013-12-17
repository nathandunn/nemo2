package edu.uoregon.nic.nemo.portal.client;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 */
public class BrainDisplayDrawer extends BrainDrawer{

    final Integer brainSize = 400;
    BrainSearchable parent  ;
    Integer headRadius = Math.round(brainSize / 2.5f);
    Integer brainCenter = brainSize / 2;
    private Map<BrainLocationEnum,RoiDisplayShape> roiShapeTreeMap = new TreeMap<BrainLocationEnum,RoiDisplayShape>();
    DrawingArea canvas ;

    protected DrawingArea drawBrain(BrainSearchable search) {
        this.parent = search ;

        canvas = new DrawingArea(brainSize,brainSize) ;

        Circle circle = new Circle(brainCenter, brainCenter, headRadius);
        canvas.add(circle);

        Circle innerCircle = new Circle(brainCenter, brainCenter, headRadius-40);
        canvas.add(innerCircle);

        // nose
        Integer noseWidth = 40;
        Integer noseHeigth = 30;
        Path nosePath = new Path(brainCenter - noseWidth / 2, brainCenter - headRadius);
        nosePath.lineTo(brainCenter, brainCenter - headRadius - noseHeigth);
        nosePath.lineTo(brainCenter + noseWidth / 2, brainCenter - headRadius);
        canvas.add(nosePath);

        drawCircles(canvas);



        createRois();


        for(RoiDisplayShape roiShape : roiShapeTreeMap.values()){
            canvas.add(roiShape) ;
        }


        return canvas ;
    }


    public void highlightRegion(BrainLocationEnum brainLocation, Double doubleValue, Boolean significant) {
        RoiDisplayShape roiShape =  roiShapeTreeMap.get(brainLocation);
        if(significant){
            if(doubleValue>0){
                roiShape.setFillColor(RoiDisplayShape.POSITIVE_COLOR);
            }
            else{
                roiShape.setFillColor(RoiDisplayShape.NEGATIVE_COLOR);
            }
        }
        else{
            roiShape.setFillColor(RoiDisplayShape.OFF_COLOR);
        }
        roiShape.drawMeanIntensity(canvas,doubleValue);
    }

    public void clearRegion(BrainLocationEnum brainLocationEnum) {
        RoiDisplayShape roiShape =  roiShapeTreeMap.get(brainLocationEnum);
        roiShape.removeValue();
    }


    private void createRois() {

        List<Dimension> midFrontalDimensions = new ArrayList<Dimension>();
        midFrontalDimensions.add(new Dimension(brainCenter - 50, brainCenter - 30));
        midFrontalDimensions.add(new Dimension(brainCenter - 30, brainCenter - 80));
        midFrontalDimensions.add(new Dimension(brainCenter + 30, brainCenter - 80));
        midFrontalDimensions.add(new Dimension(brainCenter + 50, brainCenter - 30));
        midFrontalDimensions.add(new Dimension(brainCenter - 50, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.MFRONT, new RoiDisplayShape(midFrontalDimensions, BrainLocationEnum.MFRONT, parent));

        List<Dimension> leftFrontalDimensions = new ArrayList<Dimension>();
        leftFrontalDimensions.add(new Dimension(brainCenter - 50, brainCenter - 30));
        leftFrontalDimensions.add(new Dimension(brainCenter - 90, brainCenter - 30));
        leftFrontalDimensions.add(new Dimension(brainCenter - 70, brainCenter - 80));
        leftFrontalDimensions.add(new Dimension(brainCenter - 30, brainCenter - 80));
        leftFrontalDimensions.add(new Dimension(brainCenter - 50, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.LFRONT, new RoiDisplayShape(leftFrontalDimensions, BrainLocationEnum.LFRONT, parent));


        List<Dimension> leftFrontalTemporalDimensions = new ArrayList<Dimension>();
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 90, brainCenter - 30));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 70, brainCenter - 80));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 30, brainCenter - 80));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 40, brainCenter - 150));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 140, brainCenter - 80));
        leftFrontalTemporalDimensions.add(new Dimension(brainCenter - 90, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.LFTEMP, new RoiDisplayShape(leftFrontalTemporalDimensions, BrainLocationEnum.LFTEMP, parent));


        List<Dimension> rightFrontalTemporalDimensions = new ArrayList<Dimension>();
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 90, brainCenter - 30));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 70, brainCenter - 80));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 30, brainCenter - 80));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 40, brainCenter - 150));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 140, brainCenter - 80));
        rightFrontalTemporalDimensions.add(new Dimension(brainCenter + 90, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.RFTEMP, new RoiDisplayShape(rightFrontalTemporalDimensions, BrainLocationEnum.RFTEMP, parent));


        List<Dimension> rightFrontalDimensions = new ArrayList<Dimension>();
        rightFrontalDimensions.add(new Dimension(brainCenter + 50, brainCenter - 30));
        rightFrontalDimensions.add(new Dimension(brainCenter + 90, brainCenter - 30));
        rightFrontalDimensions.add(new Dimension(brainCenter + 70, brainCenter - 80));
        rightFrontalDimensions.add(new Dimension(brainCenter + 30, brainCenter - 80));
        rightFrontalDimensions.add(new Dimension(brainCenter + 50, brainCenter - 30));
        roiShapeTreeMap.put(BrainLocationEnum.RFRONT, new RoiDisplayShape(rightFrontalDimensions, BrainLocationEnum.RFRONT, parent));

        List<Dimension> midCentralDimensions = new ArrayList<Dimension>();
        midCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter - 20));
        midCentralDimensions.add(new Dimension(brainCenter + 40, brainCenter - 20));
        midCentralDimensions.add(new Dimension(brainCenter + 40, brainCenter + 20));
        midCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter + 20));
        midCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter - 20));
        roiShapeTreeMap.put(BrainLocationEnum.MCENT, new RoiDisplayShape(midCentralDimensions, BrainLocationEnum.MCENT, parent));

        List<Dimension> leftCentralDimensions = new ArrayList<Dimension>();
        leftCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter - 20));
        leftCentralDimensions.add(new Dimension(brainCenter - 90, brainCenter - 20));
        leftCentralDimensions.add(new Dimension(brainCenter - 100, brainCenter + 20));
        leftCentralDimensions.add(new Dimension(brainCenter - 45, brainCenter + 20));
        leftCentralDimensions.add(new Dimension(brainCenter - 40, brainCenter - 20));
        roiShapeTreeMap.put(BrainLocationEnum.LCENT, new RoiDisplayShape(leftCentralDimensions, BrainLocationEnum.LCENT, parent));


        List<Dimension> leftCentroTemporalDimensions = new ArrayList<Dimension>();
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 150, brainCenter - 40));
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 90, brainCenter - 20));
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 100, brainCenter + 20));
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 160, brainCenter + 15));
        leftCentroTemporalDimensions.add(new Dimension(brainCenter - 150, brainCenter - 40));
        roiShapeTreeMap.put(BrainLocationEnum.LCTEMP, new RoiDisplayShape(leftCentroTemporalDimensions, BrainLocationEnum.LCTEMP, parent));

        List<Dimension> rightCentroTemporalDimensions = new ArrayList<Dimension>();
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 150, brainCenter - 40));
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 90, brainCenter - 20));
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 100, brainCenter + 20));
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 160, brainCenter + 15));
        rightCentroTemporalDimensions.add(new Dimension(brainCenter + 150, brainCenter - 40));
        roiShapeTreeMap.put(BrainLocationEnum.RCTEMP, new RoiDisplayShape(rightCentroTemporalDimensions, BrainLocationEnum.RCTEMP, parent));

        List<Dimension> rightCentralDimensions = new ArrayList<Dimension>();
        rightCentralDimensions.add(new Dimension(brainCenter + 40, brainCenter - 20));
        rightCentralDimensions.add(new Dimension(brainCenter + 90, brainCenter - 20));
        rightCentralDimensions.add(new Dimension(brainCenter + 100, brainCenter + 20));
        rightCentralDimensions.add(new Dimension(brainCenter + 45, brainCenter + 20));
        rightCentralDimensions.add(new Dimension(brainCenter + 40, brainCenter - 20));
        roiShapeTreeMap.put(BrainLocationEnum.RCENT, new RoiDisplayShape(rightCentralDimensions, BrainLocationEnum.RCENT, parent));

        List<Dimension> midParietalDimensions = new ArrayList<Dimension>();
        midParietalDimensions.add(new Dimension(brainCenter - 50, brainCenter + 30));
        midParietalDimensions.add(new Dimension(brainCenter + 50, brainCenter + 30));
        midParietalDimensions.add(new Dimension(brainCenter + 40, brainCenter + 70));
        midParietalDimensions.add(new Dimension(brainCenter - 40, brainCenter + 70));
        midParietalDimensions.add(new Dimension(brainCenter - 50, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.MPAR, new RoiDisplayShape(midParietalDimensions, BrainLocationEnum.MPAR, parent));


        List<Dimension> leftParietalDimensions = new ArrayList<Dimension>();
        leftParietalDimensions.add(new Dimension(brainCenter - 50, brainCenter + 30));
        leftParietalDimensions.add(new Dimension(brainCenter - 100, brainCenter + 30));
        leftParietalDimensions.add(new Dimension(brainCenter - 80, brainCenter + 70));
        leftParietalDimensions.add(new Dimension(brainCenter - 40, brainCenter + 70));
        leftParietalDimensions.add(new Dimension(brainCenter - 50, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.LPAR, new RoiDisplayShape(leftParietalDimensions, BrainLocationEnum.LFRONT, parent));

        List<Dimension> leftParetoTemporalDimensions = new ArrayList<Dimension>();
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 100, brainCenter + 30));
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 160, brainCenter + 40));
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 130, brainCenter + 90));
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 80, brainCenter + 70));
        leftParetoTemporalDimensions.add(new Dimension(brainCenter - 100, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.LPTEMP, new RoiDisplayShape(leftParetoTemporalDimensions, BrainLocationEnum.LPTEMP, parent));

        List<Dimension> rightParetoTemporalDimensions = new ArrayList<Dimension>();
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 100, brainCenter + 30));
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 160, brainCenter + 40));
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 130, brainCenter + 90));
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 80, brainCenter + 70));
        rightParetoTemporalDimensions.add(new Dimension(brainCenter + 100, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.RPTEMP, new RoiDisplayShape(rightParetoTemporalDimensions, BrainLocationEnum.RPTEMP, parent));

        List<Dimension> rightParietalDimensions = new ArrayList<Dimension>();
        rightParietalDimensions.add(new Dimension(brainCenter + 50, brainCenter + 30));
        rightParietalDimensions.add(new Dimension(brainCenter + 100, brainCenter + 30));
        rightParietalDimensions.add(new Dimension(brainCenter + 80, brainCenter + 70));
        rightParietalDimensions.add(new Dimension(brainCenter + 40, brainCenter + 70));
        rightParietalDimensions.add(new Dimension(brainCenter + 50, brainCenter + 30));
        roiShapeTreeMap.put(BrainLocationEnum.RPAR, new RoiDisplayShape(rightParietalDimensions, BrainLocationEnum.RFRONT, parent));

        List<Dimension> midOccipitalDimensions = new ArrayList<Dimension>();
        midOccipitalDimensions.add(new Dimension(brainCenter - 30, brainCenter  + 80));
        midOccipitalDimensions.add(new Dimension(brainCenter + 30, brainCenter  + 80));
        midOccipitalDimensions.add(new Dimension(brainCenter + 20, brainCenter  + 130));
        midOccipitalDimensions.add(new Dimension(brainCenter - 20, brainCenter  + 130));
//        midOccipitalDimensions.add(new Dimension(brainCenter , brainCenter + headRadius ));
        midOccipitalDimensions.add(new Dimension(brainCenter - 30, brainCenter  + 80));
        roiShapeTreeMap.put(BrainLocationEnum.MOCC, new RoiDisplayShape(midOccipitalDimensions, BrainLocationEnum.MOCC, parent));

        List<Dimension> leftOccipitalDimensions = new ArrayList<Dimension>();
        leftOccipitalDimensions.add(new Dimension(brainCenter - 30, brainCenter  + 80));
        leftOccipitalDimensions.add(new Dimension(brainCenter - 70, brainCenter  + 85));
        leftOccipitalDimensions.add(new Dimension(brainCenter - 30, brainCenter  + 135));
        leftOccipitalDimensions.add(new Dimension(brainCenter - 20, brainCenter  + 130));
        leftOccipitalDimensions.add(new Dimension(brainCenter - 30, brainCenter  + 80));
        roiShapeTreeMap.put(BrainLocationEnum.LOCC, new RoiDisplayShape(leftOccipitalDimensions, BrainLocationEnum.LOCC, parent));

        List<Dimension> leftOccipitalTemporalDimensions = new ArrayList<Dimension>();
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 70, brainCenter  + 85));
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 25, brainCenter  + 140));
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 35, brainCenter  + 160));
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 130, brainCenter  + 100));
        leftOccipitalTemporalDimensions.add(new Dimension(brainCenter - 70, brainCenter  + 85));
        roiShapeTreeMap.put(BrainLocationEnum.LOTEMP, new RoiDisplayShape(leftOccipitalTemporalDimensions, BrainLocationEnum.LOTEMP, parent));

        List<Dimension> rightOccipitalDimensions = new ArrayList<Dimension>();
        rightOccipitalDimensions.add(new Dimension(brainCenter + 30, brainCenter  + 80));
        rightOccipitalDimensions.add(new Dimension(brainCenter + 70, brainCenter  + 85));
        rightOccipitalDimensions.add(new Dimension(brainCenter + 30, brainCenter  + 135));
        rightOccipitalDimensions.add(new Dimension(brainCenter + 20, brainCenter  + 130));
        rightOccipitalDimensions.add(new Dimension(brainCenter + 30, brainCenter  + 80));
        roiShapeTreeMap.put(BrainLocationEnum.ROCC, new RoiDisplayShape(rightOccipitalDimensions, BrainLocationEnum.ROCC, parent));

        List<Dimension> rightOccipitalTemporalDimensions = new ArrayList<Dimension>();
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 70, brainCenter  + 85));
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 25, brainCenter  + 140));
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 35, brainCenter  + 160));
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 130, brainCenter  + 100));
        rightOccipitalTemporalDimensions.add(new Dimension(brainCenter + 70, brainCenter  + 85));
        roiShapeTreeMap.put(BrainLocationEnum.ROTEMP, new RoiDisplayShape(rightOccipitalTemporalDimensions, BrainLocationEnum.ROTEMP, parent));
    }

}
