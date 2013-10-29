package edu.uoregon.nemo.nic.portal.util

/**
 */
class SearchResultsDTO {
    List<SearchResultDTO> searchResultDTOList = new ArrayList<>()

    Integer getErpCount(){
        return searchResultDTOList.size()
    }

    Integer getInstanceCount(){
        int instanceCount = 0
        for(SearchResultDTO searchResultDTO in searchResultDTOList){
            instanceCount += searchResultDTO.individuals.individualDTOList.size()
        }
        return instanceCount
    }

    def addToList(SearchResultDTO searchResultDTO) {
        if(!searchResultDTOList.contains(searchResultDTO)){
            searchResultDTOList.add(searchResultDTO)
        }
    }

    List<SearchResultDTO> getSortedSearchResults(){
        Collections.sort(searchResultDTOList)
        return searchResultDTOList
    }


    @Override
    public java.lang.String toString() {
        return "SearchResultsDTO{" +
                "searchResultDTOList=" + searchResultDTOList.size() +
                '}';
    }

    def resortSearchResults() {
        Collections.sort(searchResultDTOList)
    }
}
