package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("rpc")
public interface SearchService extends RemoteService {
    String searchErps(Integer min, Integer max, List<BrainLocationEnum> locations);
    String searchErps(Integer minTime, Integer maxTime, List<BrainLocationEnum> locations,Integer minPeak,Integer maxPeak);
    String searchErps(Integer minTime, Integer maxTime, Map<BrainLocationEnum, SelectedLocationEnum> brainSelectedBrainLocationMap);
}
