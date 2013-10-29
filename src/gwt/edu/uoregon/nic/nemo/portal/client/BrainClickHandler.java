package edu.uoregon.nic.nemo.portal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;

/**
 */
public class BrainClickHandler implements ClickHandler {

    private Search parent ;

    public BrainClickHandler(Search parent){
        this.parent = parent ;
    }


    @Override
    public void onClick(ClickEvent clickEvent) {
//        BrainLocationEnum brainLocationEnum = BrainLocationEnum.getEnumForLocation(clickEvent.getX(), clickEvent.getY());
//        Window.alert("clicked at "+clickEvent.getX()+" "+clickEvent.getY());
        BrainLocationEnum brainLocationEnum = BrainLocationEnum.getEnumForLocation(clickEvent.getX(), clickEvent.getY(), parent.imageWidth);
        if (brainLocationEnum != null) {
            GWT.log("got brainLocation "+ brainLocationEnum.name()+ " for locatin "+clickEvent.getY()+ " "+clickEvent.getY());
            parent.toggleBrainLocation(brainLocationEnum);
            parent.updateFilterList();
            parent.doSearch();
        }
        else{
            GWT.log("NO brainLocation at locatin "+clickEvent.getY()+ " "+clickEvent.getY());
        }
    }
}
