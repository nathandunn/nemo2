package edu.uoregon.nemo.nic.portal.util

/**
 * Created with IntelliJ IDEA.
 * User: NathanDunn
 * Date: 9/6/12
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
class TermView {

    // basic info
    String label
    String definition
    List<String> synonyms = new ArrayList<String>()
    String uri

    // detailed info
    String detailedInfoDump

    // curation status
    String preferredLabel
    Date creationDate
    Date modifiedDate
    String curationStatus
    List<String> definitionSources = new ArrayList<String>()
    String evidenceCode
    List<String> contributors = new ArrayList<String>()

    def addContributor(String s) {
        contributors.add(s)
    }
    def addSynonym(String s) {
        synonyms.add(s)
    }

    def addSource(String s) {
        definitionSources.add(s)
    }

    def getNemoId(){
        if(uri?.contains("#")){
            return uri.split("#")[1]
        }
        return uri
    }
}
