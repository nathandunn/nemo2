package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.TermLinkContainer
import edu.uoregon.nic.nemo.portal.client.BrainLocationEnum

/**
 * This is the stored version of IndividualDTO.  For each rdf file, should be
 * 20 locations X number of peakTimes
 */
class Individual {

    static constraints = {
    }


    ErpAnalysisResult erpAnalysisResult

    String url
    String name

    // values
    Integer peakTime
    Float meanIntensity
    BrainLocationEnum location
    Boolean statisticallySignificant
    String mappedInstances

}
