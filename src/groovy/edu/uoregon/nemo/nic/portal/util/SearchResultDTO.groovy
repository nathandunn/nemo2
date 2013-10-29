package edu.uoregon.nemo.nic.portal.util

import edu.uoregon.nic.nemo.portal.client.BrainLocationEnum
import edu.uoregon.nic.nemo.portal.client.SelectedLocationEnum

/**
 */
class SearchResultDTO implements Comparable<SearchResultDTO>{

    Long erpAnalysisResultId
    String erpAnalysisResultName
    IndividualsDTO individuals = new IndividualsDTO()
    String link


    @Override
    int compareTo(SearchResultDTO t) {
        erpAnalysisResultName.compareToIgnoreCase(t.erpAnalysisResultName)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        SearchResultDTO that = (SearchResultDTO) o

        if (erpAnalysisResultName != that.erpAnalysisResultName) return false
        if (link != that.link) return false

        return true
    }

    int hashCode() {
        int result
        result = (erpAnalysisResultName != null ? erpAnalysisResultName.hashCode() : 0)
        result = 31 * result + (link != null ? link.hashCode() : 0)
        return result
    }



    @Override
    public String toString() {
        return "SearchResultDTO{" +
                "erpAnalysisResultId=" + erpAnalysisResultId +
                ", erpAnalysisResultName='" + erpAnalysisResultName + '\'' +
                ", link='" + link + '\'' +
                ", individuals=" + individuals.individualDTOList.size() +
                '}';
    }

    IndividualsDTO searchIndividuals(Integer minTime, Integer maxTime, Map<BrainLocationEnum, SelectedLocationEnum> brainSelectedBrainLocationMap){
        IndividualsDTO individualDTOs = new IndividualsDTO()

        for (IndividualDTO individualDTO in individuals.individualDTOList) {
            if (individualDTO.peakTime >= minTime && individualDTO.peakTime <= maxTime
            ) {
                if(individualDTO.hasAnyLocationIntensity(brainSelectedBrainLocationMap)){
                    individualDTOs.individualDTOList.add(individualDTO)
                }
            }
        }
        Collections.sort(individualDTOs.individualDTOList)
        return individualDTOs
    }

    IndividualsDTO searchIndividuals(int minTime, int maxTime, List<BrainLocationEnum> brainLocationEnums,int minIntensity,int maxIntensity) {
        IndividualsDTO individualDTOs = new IndividualsDTO()

        for (IndividualDTO individualDTO in individuals.individualDTOList) {
            if (individualDTO.peakTime >= minTime && individualDTO.peakTime <= maxTime
            && individualDTO.meanIntensity >= minIntensity && individualDTO.meanIntensity <= maxIntensity
            ) {
                if(individualDTO.hasAnyLocation(brainLocationEnums)){
                    individualDTOs.individualDTOList.add(individualDTO)
                }
            }
        }
        Collections.sort(individualDTOs.individualDTOList)
        return individualDTOs
    }
}
