package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;

/**
 */
public class BrainLocationCheckBox extends CheckBox{

    private ParentSearch parentSearch ;
    private String internalName ;

    public BrainLocationCheckBox(ParentSearch parentSearch,BrainLocationEnum brainLocationEnum){
        super();
        internalName = brainLocationEnum.name();
        this.parentSearch = parentSearch;
        addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                getParentSearch().doSearch();
            }
        });
    }

    public ParentSearch getParentSearch() {
        return parentSearch;
    }

    public void setParentSearch(ParentSearch parentSearch) {
        this.parentSearch = parentSearch;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }
}
