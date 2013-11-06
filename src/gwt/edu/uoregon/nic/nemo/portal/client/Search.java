package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
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
public class Search implements EntryPoint, BrainSearchable {

    private static SearchServiceAsync searchServiceAsync = GWT.create(SearchService.class);
    protected static List<BrainLocationEnum> brainLocationEnums = new ArrayList<BrainLocationEnum>();

    final TextBox minTime = new TextBox();
    final TextBox maxTime = new TextBox();
    //    final FlexTable locationTable = new FlexTable();
    final VerticalPanel resultPanel = new VerticalPanel();
    final FlexTable resultTable = new FlexTable();
    final FlexTable filterTable = new FlexTable();
    //    final FlexTable.FlexCellFormatter flexCellFormatter = resultTable.getFlexCellFormatter();
    final Dictionary locations = Dictionary.getDictionary("locations");
    final Integer imageWidth = 300;
    final Button searchAllLocations = new Button("Search All Locations");
    final String baseTermUrl = locations.get("baseTermUrl");
    final BrainDrawer brainDrawer = new BrainDrawer();


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {


        DrawingArea canvas = brainDrawer.drawBrain(this);

        RootPanel.get("brainPanel").add(canvas);

        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.setWidth("100%");
        mainPanel.setStyleName("mainPanel");

//        Grid queryPanel = new Grid(2,2);
        VerticalPanel queryPanel = new VerticalPanel();
        queryPanel.setWidth("30%");
        VerticalPanel spatialPanel = new VerticalPanel();
        VerticalPanel temporalPanel = new VerticalPanel();


        queryPanel.setStyleName("queryPanel");

        HTML temporalHtml = new HTML("<h3>Temporal Filter</h3>");
        temporalHtml.setHorizontalAlignment(HTML.ALIGN_CENTER);
//        queryPanel.setWidget(0, 1, temporalHtml);
        temporalPanel.add(temporalHtml);

        HTML spatialHtml = new HTML("<h3>Spatial Filter</h3>");
        spatialHtml.setHorizontalAlignment(HTML.ALIGN_CENTER);
//        queryPanel.setWidget(0, 0, spatialHtml);
        spatialPanel.add(spatialHtml);


        Grid timesPanel = new Grid(2, 2);
//        timesPanel.setStyleName("timeAlignment");
//        timesPanel.setSize("80px","80px");


//        timesPanel.getElement().getStyle().setProperty("float", "left");
        HTML minLabel = new HTML("<strong>Min (ms)</strong>:");
        minLabel.setStylePrimaryName("htmlLabel");
//        minLabel.setHorizontalAlignment(Label.ALIGN_RIGHT);
        HTML maxLabel = new HTML("<strong>Max (ms)</strong>:");
        maxLabel.setStylePrimaryName("htmlLabel");
//        maxLabel.setHorizontalAlignment(Label.ALIGN_RIGHT);
        timesPanel.setWidget(0, 0, minLabel);
        timesPanel.setWidget(0, 1, minTime);
        minTime.setText("0");
        timesPanel.setWidget(1, 0, maxLabel);
        timesPanel.setWidget(1, 1, maxTime);
        maxTime.setText("1000");

        temporalPanel.add(timesPanel);
        queryPanel.add(temporalPanel);
        queryPanel.add(canvas);
        mainPanel.add(queryPanel);


        resultTable.setWidth("80%");
        resultTable.setStyleName("resultTable");
        resultTable.getRowFormatter().removeStyleName(0, "resultTable");
        resultTable.getRowFormatter().addStyleName(0, "resultTableHeader");
        resultTable.setHTML(0, 0, "<th>Experimental Contrast</th>");
        resultTable.setHTML(0, 1, "<th>Mean Intensity (µV)<sup>*</sup></th>");
        resultTable.setHTML(0, 2, "<th>Peak Time (ms)</th>");
        resultTable.setHTML(0, 3, "<th>Location</th>");

        HTML html = new HTML("<sup>*</sup>Mean Intensity for Contrast");
        html.setStyleName("sidenote");
        html.addStyleName("resultTable");
        resultPanel.add(html);
        resultPanel.add(resultTable);
        mainPanel.add(resultPanel);


        RootPanel.get("searchPanel").add(mainPanel);


        minTime.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                doSearch();
            }
        });

        maxTime.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                doSearch();
            }
        });

    }


    protected void updateFilterList() {
        filterTable.removeAllRows();
        int i = 0;
        for (BrainLocationEnum brainLocationEnum1 : brainLocationEnums) {
            String filterLink = "";
            filterLink = "<a href='" + baseTermUrl + "/" + brainLocationEnum1.url + "'/>";
            filterLink += brainLocationEnum1.clusterName;
//                        filterLink += " ";
//                        filterLink += brainLocationEnum1.longName;
            filterLink += "</a>";
            HTML filterLabel = new HTML(filterLink);
            filterTable.setWidget(i, 0, filterLabel);
            ++i;
        }

    }

    protected void toggleBrainLocation(BrainLocationEnum brainLocationEnum) {
        if (brainLocationEnums.contains(brainLocationEnum)) {
            brainLocationEnums.remove(brainLocationEnum);
        } else {
            brainLocationEnums.add(brainLocationEnum);
        }
    }


    int getMinTime() {
        String minValue = minTime.getValue();
        if (minValue != null && minValue.length() > 0) {
            return Integer.parseInt(minValue);
        } else {
            return 0;
        }
    }

    int getMaxTime() {
        String maxValue = maxTime.getValue();
        if (maxValue != null && maxValue.length() > 0) {
            return Integer.parseInt(maxValue);
        } else {
            return 1000;
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
        searchServiceAsync.searchErps(getMinTime(), getMaxTime(), getBrainSelectedBrainLocationMap(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("boo!" + caught.toString());
                popupPanel.hide();
            }

            @Override
            public void onSuccess(String result) {
                resultTable.removeAllRows();
//                resultTable.clear();
                GWT.log(result);
                JSONValue value = JSONParser.parseStrict(result);
                JSONObject object = value.isObject();
                Integer erpCount = Integer.valueOf((int) object.get("erpCount").isNumber().doubleValue());
                Integer individualCount = Integer.valueOf((int) object.get("instanceCount").isNumber().doubleValue());

//                countResults.setHTML("Erps: " + erpCount + " Instances: " + individualCount);
                resultTable.getRowFormatter().setStyleName(0, "resultTableHeader");
                resultTable.setHTML(0, 0, "<th>Experimental Contrast " + erpCount+"</th>");
                resultTable.setHTML(0, 1, "<th>Mean Intensity (µV)<sup>*</sup> " + individualCount+"</th>");
                resultTable.setHTML(0, 2, "<th>Peak Time (ms)</th>");
                resultTable.setHTML(0, 3, "<th>Location</th>");
                GWT.log("object " + object);
                JSONArray values = object.get("searchResultDTOList").isArray();
                GWT.log("values " + values);
                int displayRow = 1;
                for (int experimentContrast = 0; experimentContrast < values.size(); experimentContrast++) {

                    JSONObject experimentContrastValue = values.get(experimentContrast).isObject();

                    JSONArray individualList = experimentContrastValue.get("individuals").isObject().get("individualDTOList").isArray();
                    for (int j = 0; j < individualList.size(); j++) {

                        if (j == 0) {
                            String erpName = experimentContrastValue.get("erpAnalysisResultName").isString().stringValue().replaceAll("\"", "");
                            if (erpName.length() > 20) {
                                erpName = erpName.substring(0, 20) + "...";
                            }
                            String linkName = experimentContrastValue.get("link").isString().stringValue().replaceAll("\"", "");
                            String html = "<a href='" + linkName + "'>" + erpName + "</a>";

                            resultTable.setHTML(displayRow, 0, html);
                        }

                        JSONObject individualJsonObject = individualList.get(j).isObject();


                        String meanString = "";
                        Double meanIntensity = individualJsonObject.get("meanIntensity").isNumber().doubleValue();
                        meanString += " <a href='";
                        meanString += individualJsonObject.get("linkFromUrl").isString().stringValue();
                        meanString += "'>";
                        if (meanIntensity > 0) {
                            meanString += "+";
                        }
                        meanString += NumberFormat.getFormat("#0.##").format(meanIntensity);
//                        meanString += "details";
                        meanString += "</a>";
                        resultTable.setHTML(displayRow, 1, meanString);


                        String htmlString = "";
                        htmlString += "<a href='";
                        htmlString += individualJsonObject.get("peakTimeUrl").isString().stringValue();
                        htmlString += "'>";
//                        htmlString += individualList.get(j).isObject().get("nameFromUrl").isString().stringValue();
                        Double peakTime = individualJsonObject.get("peakTime").isNumber().doubleValue();
                        htmlString += NumberFormat.getFormat("#0.##").format(peakTime);
//                        htmlString += "ms ";

                        htmlString += "</a>";
                        resultTable.setHTML(displayRow, 2, htmlString);


                        String location = individualJsonObject.get("thisLocation").isObject().get("name").isString().stringValue();
                        String locationString = "";
                        locationString += "<a href='";
                        locationString += individualJsonObject.get("locationUrl").isString().stringValue();
                        locationString += "'>";
                        locationString += location;
                        locationString += "</a>";

                        resultTable.setHTML(displayRow, 3, locationString);

                        if (experimentContrast % 2 == 1) {
                            resultTable.getRowFormatter().setStyleName(displayRow, "resultTable-odd");
                        } else {
                            resultTable.getRowFormatter().setStyleName(displayRow, "resultTable-even");
                        }

                        ++displayRow;
                    }

//                    htmlString += "</ul>";
                }
                popupPanel.hide();
            }
        });
    }

    @Override
    public Long getId() {
        return null ;
    }

    @Override
    public String getBaseUrl() {
        return baseTermUrl;
    }
}
