package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.vaadin.gwtgraphics.client.DrawingArea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BrainDisplay implements EntryPoint, BrainSearchable {

    private static SearchServiceAsync searchServiceAsync = GWT.create(SearchService.class);
    protected static List<BrainLocationEnum> brainLocationEnums = new ArrayList<BrainLocationEnum>();

    //    final FlexTable locationTable = new FlexTable();
    //    final FlexTable.FlexCellFormatter flexCellFormatter = resultTable.getFlexCellFormatter();
    final Dictionary locations = Dictionary.getDictionary("locations");
    final Integer imageWidth = 300;
    final String baseUrl = locations.get("baseUrl");
    final Long erpAnalysisId = Long.parseLong(locations.get("erpAnalysisResultId"));
    final BrainDisplayDrawer brainDrawer = new BrainDisplayDrawer();
    final List<Integer> times = new ArrayList<Integer>();
    final FlexTable timesTable = new FlexTable();

    private Integer selectedTime;
    private BrainLocationEnum selectedLocation;

    public BrainDisplay() {
//        for (String s : locations.keySet()) {
//            GWT.log(s);
//        }
        if (locations.keySet().contains("initialLocation")) {
            selectedLocation = BrainLocationEnum.getEnumForString(locations.get("initialLocation"));
        } else if (locations.keySet().contains("initialTime")) {
            selectedTime = Integer.parseInt(locations.get("initialTime"));
            String timeString = locations.get("times");
//            GWT.log(timeString);
            for (String timeA : timeString.split(",")) {
                times.add(Integer.parseInt(timeA));
            }
        } else {
            Window.alert("Error . . . initial conditions not set");
        }
    }


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {


        DrawingArea canvas = brainDrawer.drawBrain(this);

        RootPanel.get("brainPanel").add(canvas);

//        HorizontalPanel mainPanel = new HorizontalPanel();
//        mainPanel.setWidth("100%");
//        mainPanel.setStyleName("mainPanel");

//        Grid queryPanel = new Grid(2,2);
        VerticalPanel queryPanel = new VerticalPanel();
//        queryPanel.setWidth("30%");
        HorizontalPanel timePanel = new HorizontalPanel();
        timePanel.setStyleName("queryPanel");
        HTML peakTimeLabel = new HTML("Peak Time (ms):");
        peakTimeLabel.setStyleName("peak-label");
        timePanel.add(peakTimeLabel);
        timePanel.add(timesTable);
        queryPanel.add(timePanel);
        timesTable.setStyleName("time-selector");


        redrawTimes();

        queryPanel.setStyleName("queryPanel");

//        HorizontalPanel searchPanel = new HorizontalPanel();
//        searchPanel.add(new HTML("&nbsp;"));
//        searchPanel.add(canvas);

        canvas.setStyleName("brain-display");

        queryPanel.add(canvas);
//        mainPanel.add(queryPanel);


        RootPanel.get("searchPanel").add(queryPanel);


        doSearch();

    }

    private void redrawTimes() {

        timesTable.clear();
        for (int i = 0; i < times.size(); i++) {
            final HTML timeButton;
            final Integer aTime = times.get(i);
            if (aTime.equals(selectedTime)) {
                timeButton = new HTML("<div class='selected-time'>" + aTime +"</div>");
            } else {
                timeButton = new HTML("<div class='unselected-time'>" + aTime +"</div>");
            }
            timeButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    selectedTime = aTime ;
                    redrawTimes();
                    doSearch();
                }
            });

            timesTable.setWidget(0, i+1, timeButton);
        }
    }


    Map<BrainLocationEnum, SelectedLocationEnum> getBrainSelectedBrainLocationMap() {
        Map<BrainLocationEnum, SelectedLocationEnum> map = new TreeMap<BrainLocationEnum, SelectedLocationEnum>();
        Map<BrainLocationEnum, RoiShape> shapeMap = brainDrawer.getRoiShapeTreeMap();
        for (BrainLocationEnum brainLocationEnum : brainDrawer.getRoiShapeTreeMap().keySet()) {
            RoiShape roiShape = shapeMap.get(brainLocationEnum);
//            if (!roiShape.getFillColor().equals(RoiShape.OFF_COLOR)) {
//                brainLocationEnumList.put(brainLocationEnum);
//            }
            if (roiShape.getFillColor().equals(RoiShape.POSITIVE_COLOR)) {
                map.put(brainLocationEnum, SelectedLocationEnum.POSITIVE);
            } else if (roiShape.getFillColor().equals(RoiShape.NEGATIVE_COLOR)) {
                map.put(brainLocationEnum, SelectedLocationEnum.NEGATIVE);
            } else if (roiShape.getFillColor().equals(RoiShape.BOTH_COLOR)) {
                map.put(brainLocationEnum, SelectedLocationEnum.BOTH);
            }
        }

        return map;
    }

    public void doSearch() {

        final PopupPanel popupPanel = new PopupPanel();
        popupPanel.setGlassEnabled(true);
        popupPanel.setModal(true);
        Label searchingLabel = new Label("Searching ...");
        searchingLabel.setStyleName("searchingLabel");
        popupPanel.add(searchingLabel);
        popupPanel.center();
        popupPanel.show();
        searchServiceAsync.findPeakIntensities(erpAnalysisId, selectedTime, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("boo!" + caught.toString());
                popupPanel.hide();
            }

            @Override
            public void onSuccess(String result) {
//                resultTable.clear();
                GWT.log(result);
                JSONValue value = JSONParser.parseStrict(result);
                JSONObject rootObject = value.isObject();

                JSONObject individualObject;
                for (BrainLocationEnum brainLocationEnum : BrainLocationEnum.values()) {
                    if (rootObject.containsKey(brainLocationEnum.name())) {
                        individualObject = rootObject.get(brainLocationEnum.name()).isObject();
                        displayMeanIntensity(brainLocationEnum, individualObject);
                    } else {
                        brainDrawer.clearRegion(brainLocationEnum);
                    }
                }

                popupPanel.hide();
            }

        });
    }

    @Override
    public Long getId() {
        return erpAnalysisId ;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    private void displayMeanIntensity(BrainLocationEnum brainLocationEnum, JSONObject individualObject) {
        Boolean significant = individualObject.get("statisticallySignificant").isBoolean().booleanValue();
        Double doubleValue = individualObject.get("meanIntensity").isNumber().doubleValue();
//        GWT.log("sig: "+significant + " double: "+doubleValue);
        brainDrawer.highlightRegion(brainLocationEnum,doubleValue,significant);
    }

}
