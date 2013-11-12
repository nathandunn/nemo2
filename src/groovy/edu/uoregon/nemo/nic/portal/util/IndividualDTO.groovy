package edu.uoregon.nemo.nic.portal.util

import edu.uoregon.nic.nemo.portal.client.BrainLocationEnum
import edu.uoregon.nic.nemo.portal.client.SelectedLocationEnum

/**
 */
class IndividualDTO implements Serializable,Comparable{


    Integer peakTime;
//    String name
    String url
    BrainLocationEnum thisLocation
    List<BrainLocationEnum> locations = new ArrayList<BrainLocationEnum>()
    String linkFromUrl
    Float meanIntensity
    Boolean significant = false
    String peakTimeUrl
    String locationUrl
    String mappedInstances

//    #GAF-EEGcloze_Unexpected-Expected_ERP_+240
    String getNameFromUrl(){
        String PREFIX = "http://purl.bioontology.org/NEMO/data/"
//        return url.substring(38)
        String nameString = url.substring(PREFIX.length())
//        return nameString.replaceAll("_statistical_quality","")
        return nameString.replaceAll("_mean_intensity.*","")
    }

    @Override
    int compareTo(Object t) {
        if(t instanceof IndividualDTO){
            return url.compareToIgnoreCase(t.url)
        }
        else {
            return 0
        }
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        IndividualDTO that = (IndividualDTO) o

        if (peakTime != that.peakTime) return false
//        if (url != that.url) return false

        return true
    }

    int hashCode() {
        int result
        result = (peakTime != null ? peakTime.hashCode() : 0)
//        result = 31 * result + (url != null ? url.hashCode() : 0)
        return result
    }

    boolean hasAnyLocation(List<BrainLocationEnum> brainLocationEnums) {
        for(BrainLocationEnum brainLocationEnum in brainLocationEnums){
            for(BrainLocationEnum myLocation in locations){
                if(myLocation==brainLocationEnum){
                    return true
                }
            }
        }
        return false;
    }


    @Override
    public java.lang.String toString() {
        return "IndividualDTO{" +
                "peakTime=" + peakTime +
                ", url='" + url + '\'' +
                ", locations=" + locations.size() +
                ", linkFromUrl='" + linkFromUrl + '\'' +
                '}';
    }

    Boolean hasAnyLocationIntensity(Map<BrainLocationEnum, SelectedLocationEnum> brainSelectedBrainLocationMap) {
        for(BrainLocationEnum brainLocationEnum in brainSelectedBrainLocationMap.keySet()){
            for(BrainLocationEnum myLocation in locations){
                SelectedLocationEnum selectedLocationEnum = brainSelectedBrainLocationMap.get(brainLocationEnum)
                if( myLocation==brainLocationEnum ){
                    if(selectedLocationEnum==SelectedLocationEnum.BOTH){
                        return true
                    }
                    else
                    if(selectedLocationEnum==SelectedLocationEnum.POSITIVE && meanIntensity>0){
                        return true
                    }
                    else
                    if(selectedLocationEnum==SelectedLocationEnum.NEGATIVE && meanIntensity<0){
                        return true
                    }
                    else{
                        return false
                    }
                }
            }
        }
        return false;
    }
}
