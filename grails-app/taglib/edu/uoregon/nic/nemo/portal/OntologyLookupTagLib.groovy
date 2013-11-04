package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.CollectionParser
import org.codehaus.groovy.grails.validation.routines.UrlValidator

class OntologyLookupTagLib {

    def ontologyService
    def grailsLinkGenerator
    def urlValidator = new UrlValidator()

    def renderYamlUrls = { attrs, body ->
        def delimiter = attrs.delimiter ?: " "
        def input = attrs.input
        out << ontologyService.getLabelTextForYamlUrls(input, delimiter)?.replaceAll("_", " ")
    }

    def renderUrl = { attrs, body ->
        def delimiter = attrs.delimiter ?: " "
        def failure = attrs.failure ?: ""
        def input = attrs.input
        if (!input) {
            out << ""
            return
        }

        if (input?.toString()?.startsWith("NEMO_")) {
            input = ontologyService.createFullURI(input)
        }

        if (urlValidator.isValid(input)) {
            def label = ontologyService.getLabelForUrl(input)?.replaceAll("_", " ")
            out << (label ?: failure) << delimiter
        } else {
            out << input
            out << render(template: "/common/notInOntology")
        }
    }

    def renderUrlLink = { attrs, body ->
        def delimiter = attrs.delimiter ?: " "
        def failure = attrs.failure ?: ""
        String input = attrs.input
        if (!input) {
            out << ""
            return
        }
        String label = ontologyService.getLabelForUrl(input)?.replaceAll("_", " ")
        String termId

        if (input.contains("#NEMO")) {
            termId = input.split("#")[1]
        }

        if (termId) {
            out << "<a href=\"" + grailsLinkGenerator.link([absolute: true, action: "show", id: termId, controller: "term"]) + "\">"
        }
        out << (label ?: failure) << delimiter
        if (termId) {
            out << "</a>"
        }
    }


    def renderAnnotator = { attrs, body ->
        def input = attrs.input
        if (urlValidator.isValid(input)) {
            if (!input || !input.contains("#")) {
                out << ""
                return
            }
            out << input.substring(input.indexOf("#") + 1)?.replaceAll("_", " ")?.trim()
        } else {
            out << input << render(template: "/common/notInOntology")
        }
    }

    def renderUrlOrChop = { attrs, body ->
        def delimiter = attrs.delimiter ?: " "
        def input = attrs.input
        if (!input) {
            out << ""
            return
        }
        def label = ontologyService.getLabelForUrl(input)?.replaceAll("_", " ")
        if (label) {
            out << label << delimiter
        } else {
            if (input.startsWith(OntologyService.DATA_URL)) {
                out << "<a href='" + input + "'>" << input.substring(OntologyService.DATA_URL.length()) << "</a>" << delimiter
            } else if (input.contains("#")) {
                out << "<a href='" + input + "'>" << input.substring(input.indexOf("#") + 1)?.replaceAll("_", " ") << "</a>" << delimiter
            } else {
                out << input << delimiter
            }
        }
    }


    def hasUrl = { attrs, body ->
        def input = attrs.input
        if (ontologyService.getLabelForUrl(input) != null) {
            out << body()
        }
    }

    def includeLink = { attrs, body ->
        def input = attrs.input
        def exclude = attrs.exclude
        def label = ontologyService.getLabelForUrl(input)?.replaceAll("_", " ")
        if (!label || !label.contains(exclude)) {
            out << body()
        }
//        else {
//            println "excluding: " + input
//        }
    }

    def noUrl = { attrs, body ->
        def input = attrs.input
        if (ontologyService.getLabelForUrl(input) == null) {
            out << body()
        }
    }

    def renderKey = { attrs, body ->
        def delimiter = attrs.delimiter ?: " "
        def input = attrs.input
        Map<String, List<String>> map = CollectionParser.parseStringAsMap(input)
//        println map
        // should just be a single entry
        for (m in map) {
            out << ontologyService.getLabelForUrl(m.key)?.replaceAll("_", " ")
            out << delimiter
        }
    }

    def renderValues = { attrs, body ->
        def delimiter = attrs.delimiter ?: " "
        def input = attrs.input
        Map<String, List<String>> map = CollectionParser.parseStringAsMap(input)
        // should just be a single entry
        for (m in map) {
            for (v in m.value) {
                out << ontologyService.getLabelForUrl(v)?.replaceAll("_", " ")
                out << delimiter
            }
        }
    }

    // expect an array
    def renderUrls = { attrs, body ->
        def delimiter = attrs.delimiter ?: " "
        def inputs = attrs.inputs
        if (!inputs) {
            out << ""
            return
        }
        for (input in inputs) {
            out << ontologyService.getLabelForUrl(input) << delimiter
        }
    }

    def resourceExists = { attrs, body ->
        def resFile = grailsApplication.parentContext.getResource(attrs.resource)
        if (resFile?.exists()) {
            out << body()
        }
    }

    def bioPortalLink = { attrs ->
        def uri = attrs.uri
        def description = attrs.description ?: "BioPortal"
        def nemoID
        if (uri.contains("#")) {
            try {
                nemoID = uri.split("#")[1]
            } catch (Exception e) {
                out << "problem looking up uri " + attrs.uri
            }
        } else if (uri.startsWith("NEMO")) {
            nemoID = uri
        }
        out << "<a href='"
        out << 'http://bioportal.bioontology.org/ontologies/47799/?p=terms&conceptid=' + nemoID
        out << "'>"
        out << description + "</a>"
    }

    def renderLabel = { attrs, body ->
        def number = attrs.number
        String term = attrs.term.toString()
        Boolean doBreak = attrs.doBreak ?: false
        String nemoString = "NEMO_"

        def fullTerm
        def shortTerm
        if (term.startsWith(nemoString)) {
            shortTerm = term.substring(nemoString.length())
            fullTerm = term
        } else if (term.contains("_")) {
            shortTerm = term
            fullTerm = term
        } else {
            shortTerm = term
            fullTerm = nemoString + term
        }

        if (doBreak) {
            out << "<br/>"
        }
        out << "<div class='detail-label'>"
        out << "( "
        if(number){
            out << number << " / "
        }
        out << link([action: 'show', controller: 'term', id: fullTerm]) { fullTerm }
        out << " )"
        out << "</div>"
    }

    // get one or more labs
    // get one or more experiments
    // get all dataFile artifact-filenames
//    Add a comment to this line
    def renderLabExperimentForDataFiles = { attrs, body ->
        Collection<ErpAnalysisResult> analysisResultCollection = attrs.input // accept a collection of DataFile's

        Set<Laboratory> labs = []
        Set<Experiment> experiments = []
        Set<ErpAnalysisResult> erpAnalysisResultSet = []
        // get one or more labs
        // get one or more experiments
        // get all dataFile artifact-filenames
        for (erpAnalsysisResult in analysisResultCollection) {
            erpAnalysisResultSet.add(erpAnalsysisResult)
            experiments.add(erpAnalsysisResult.experiment)
            if (erpAnalsysisResult.experiment.laboratory) {
                labs.add(erpAnalsysisResult.experiment.laboratory)
            }
        }

        out << "<div class='pattern-table'>"
        out << "<table>"
        out << "<tr class='pattern-table'><td class='pattern-table'>"
        out << "<strong>Research Lab</strong> "
        out << "</td>"
        out << "<td class='pattern-table'>"

        for (lab in labs) {
            def linkMap = [action: 'show', controller: 'laboratory', id: lab.id]
            out << "<a href='" << createLink(linkMap) << "'>"
            out << lab.identifier << "</a> "
        }
        out << "</td></tr>"
        out << "<tr><td class='pattern-table'>"
        out << "<strong>Experiment</strong>"
        out << "</td><td class='pattern-table'>"
        for (experiment in experiments) {
            def linkMap = [action: 'show', controller: 'experiment', id: experiment.id]
            out << "<a href='" << createLink(linkMap) << "'>"
            out << experiment.identifier << "</a> "
        }
        out << "</td></tr>"

        out << "<tr><td class='pattern-table'>"
        out << "<strong>Data (Results) File</strong>"
        out << "</td><td  class='pattern-table'>"
        for (erpAnalsysisResult in erpAnalysisResultSet) {
            def linkMap = [action: 'show', controller: 'erpAnalysisResult', id: erpAnalsysisResult.id]
            out << "<a href='" << createLink(linkMap) << "'>"
            out << erpAnalsysisResult.artifactFileName << "</a> <br/> "
        }
        out << "</td></tr>"
        out << "<tr><td class='pattern-table pattern-header'>"
//        out << "<strong>Significant Results</strong>"
        out << "<strong>Peak Time (ms) / # locations"
//        out << "<strong>Results (ms) <br/>(significant&nbsp;locations)</strong>"
        out << "</td><td  class='pattern-table'>"
        List<Individual> individualList = Individual.findAllByStatisticallySignificantAndErpAnalysisResultInList(true, erpAnalysisResultSet as List, [sort: "peakTime", order: "asc"])
        int counter =0
        if (individualList) {
            Integer peakTime = -1
            Integer countAtTime = 0
//            Integer oldTime = -1
            for (Individual individual in individualList) {
                if (individual.peakTime != peakTime) {

                    if(counter>0){
                        out << "/${countAtTime}  "
//                        out << "<br/>"
                    }

                    countAtTime = 1
                    peakTime = individual.peakTime
                    def linkMapTime = [action: 'showIndividuals', controller: 'erpAnalysisResult', id: individual.erpAnalysisResult.id, params: [time: individual.peakTime]]
                    out << "<a href='" << createLink(linkMapTime) << "'>"
//                    out << "<div class='emphasize-link'>${individual.peakTime}</div></a>&nbsp;"
                    out << "${individual.peakTime}</a>"
                }
//                def linkMapLoc = [action: 'showIndividualsAtLocation', controller: 'erpAnalysisResult', id: individual.erpAnalysisResult.id, params: [locationName: individual.location]]
////                out << " &nbsp;&nbsp;"
//                out << " &nbsp;&nbsp;<a href='" << createLink(linkMapLoc) << "'>"
//                out << "${individual.location}</a>"
//                out << "&nbsp;${individual.meanIntensity}&nbsp;(µV)"
//                if (oldTime != peakTime) {
//                    oldTime = peakTime
//                }
                ++counter
                ++countAtTime
            }
            out << "/${countAtTime} "

        }
        out << "</td></tr>"
        out << "</table>"
        out << "</div>"

    }
}
