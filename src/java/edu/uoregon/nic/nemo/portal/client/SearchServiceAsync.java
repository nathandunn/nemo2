package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;
import java.util.Map;

public interface SearchServiceAsync {
    void searchErps(Integer min,Integer max,List<BrainLocationEnum> locations,AsyncCallback<String> callback);
    void searchErps(Integer minTime, Integer maxTime, List<BrainLocationEnum> locations, Integer minPeak, Integer maxPeak, AsyncCallback<String> async);

    void searchErps(Integer minTime, Integer maxTime, Map<BrainLocationEnum, SelectedLocationEnum> brainSelectedBrainLocationMap, AsyncCallback<String> callback);

    void findPeakIntensities(Long erpAnalysisId, Integer selectedTime, AsyncCallback<String> asyncCallback);
}
