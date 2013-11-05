package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;

/**
 */
public class BrainLocationCheckBox extends CheckBox{

    private BrainSearchable brainSearchable;
    private String internalName ;

    public BrainLocationCheckBox(BrainSearchable brainSearchable,BrainLocationEnum brainLocationEnum){
        super();
        internalName = brainLocationEnum.name();
        this.brainSearchable = brainSearchable;
        addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                getBrainSearchable().doSearch();
            }
        });
    }

    public BrainSearchable getBrainSearchable() {
        return brainSearchable;
    }

    public void setBrainSearchable(BrainSearchable brainSearchable) {
        this.brainSearchable = brainSearchable;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }
}
