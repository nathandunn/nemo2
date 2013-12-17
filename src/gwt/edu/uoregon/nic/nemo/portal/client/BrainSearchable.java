package edu.uoregon.nic.nemo.portal.client;

/**
 */
public interface BrainSearchable {

    void doSearch();
    Long getId();
    String getBaseUrl();
    void setExactSearch(boolean b);
}
