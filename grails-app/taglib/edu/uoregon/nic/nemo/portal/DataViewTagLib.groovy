package edu.uoregon.nic.nemo.portal

import edu.uoregon.nic.nemo.portal.client.BrainLocationEnum
import org.apache.commons.validator.UrlValidator

import java.util.regex.Pattern

class DataViewTagLib {

    def grailsLinkGenerator
    def searchService
    def urlValidator = new UrlValidator()

    def tableEntry = { attrs, body ->
        def key = attrs.key
        def term = attrs.term
        def number = attrs.number
        def value = attrs.value
        def ontological = attrs.ontological
        def delimiter = attrs.delimiter ?: "&nbsp;&bull;&nbsp;"
        def yamlValue = attrs.yamlValue
        def yamlDelimiter = attrs.yamlDelimiter
        def url = attrs.url
        def urlOrChop = attrs.urlOrChop
        def annotator = attrs.annotator
        def noLink = attrs.noLink
        def suffix = attrs.suffix
        def related = attrs.related
        def hideIfNull = attrs.hideIfNull ?: false
        Publication publication = attrs.publication

        if (hideIfNull && value == null && ontological == null) {
            return
        }

        out << '<tr>'
        out << '            <td class="table-uri" nowrap>'
        out << renderLabel([term: term, number: number])
        out << '        </td>'
        out << '<td class="table-key">'
        out << key
        out << '        </td>'
        out << '            <td class="table-value">'
        out << '                <span class="property-value" aria-labelledby="identifier-label">'
        boolean hadValue = false
        if (yamlValue) {
            out << renderYamlUrls([input: yamlValue, delimiter: yamlDelimiter])
            hadValue = true
        } else if (url) {
            if (urlValidator.isValid(url)) {
                if (noLink) {
                    out << renderUrl([input: url])
                } else {
                    out << renderUrlLink([input: url])
                }
            } else {
                out << url
                out << render(template: "/common/notInOntology")
            }
            hadValue = true
        } else if (publication?.digitalObjectIdentifier) {
            out << publication.digitalObjectIdentifier
            out << "&nbsp;["
            out << "<a href='"
            out << grailsLinkGenerator.link([
                    target: "_blank"
                    , class: "external-link"
                    , url: "http://dx.doi.org/${publication.digitalObjectIdentifier}"
            ])
            out << "'>"
            out << "Get "
            out << renderAnnotator([input: publication.type.url])
            out << "</a>]"

        } else if (urlOrChop) {
            out << renderUrlOrChop([input: urlOrChop])
            hadValue = true
        } else if (annotator) {
            if (urlValidator.isValid(annotator)) {
                out << renderAnnotator([input: annotator])
            } else {
                out << annotator << render(template: "/common/notInOntology")
            }
            hadValue = true
        } else if (ontological) {
            if (ontological instanceof Collection) {
                ontological.sort().eachWithIndex { it, status ->
                    createOntologyLink(it)
                    if (related) {
                        out << " "
                        createRelatedLink(related, it)
                    }
                    if (status < ontological.size() - 1) {
                        out << delimiter
                    }
                }
//                for(it in ontological.sort()){
//                    createOntologyLink(it)
//                    out << delimiter
//                }
            } else {
                createOntologyLink(ontological)
                if (related) {
                    out << " "
                    createRelatedLink(related, ontological)
                }
            }
            hadValue = true
        } else if (value instanceof Collection) {
            value = value.sort()
            value.eachWithIndex { it, status ->
                out << it
                if (status < value.size() - 1) {
                    out << delimiter
                }
                hadValue = true
            }
        } else {
            out << value
            if (value) {
                hadValue = true
            }
        }

        if (hadValue && suffix) {
            out << suffix
        }
        out << '                </span>'
        out << '            </td>'
        out << '        </tr>'
    }

    def createOntologyLink(Ontological ontological) {
        out << "<a href=\"" + grailsLinkGenerator.link([absolute: true, action: "show", id: ontological.suffix, controller: "term"]) + "\">"
        out << ontological.name
        out << "</a>"
    }

    def relatedLink = { attrs, body ->
        def relatedController = attrs.controller
        def ontological = attrs.ontological

        // should come in as "ClasName_ID"
        log.debug "related ontological [${ontological}]"

        if (!relatedController) {
//            GrailsDomainClass domainClass = ontological.domainClass
//            println "domainClass ${domainClass}"
//            switch (domainClass.toString().split("> ")[1]) {
            def ontoVar = ontological.toString().split("_")[0]
            switch (ontoVar) {
                case "ExperimentalParadigm":
                    relatedController = "experiment"
                    break;
                case "ConditionType":
                case "TaskInstruction":
                    relatedController = "condition"
                    break;
                case "StimulusType":
                case "StimulusModality":
//                case "StimulusQuality":
                    relatedController = "stimulus"
                    break;
                case "ResponseType":
                case "ResponseModality":
                    relatedController = "response"
                    break;
                case "ElectrodeArrayLayout":
                    relatedController = "eegDataCollection"
                    break;
                case "ErpEvent":
                case "OfflineReference": // for some reason this comes out as ReferenceElectrode . . . may be more correct
                case "CleaningTransformation":
                    relatedController = "erpDataPreprocessing"
                    break;
                case "DataSet":
                case "DataFormat":
                    relatedController = "dataFile"
                    break;
                case "AnalysisMethod":
                case "AnalysisVariable":
                    relatedController = "erpAnalysisResult"
                    break;
                case "PatternExtractionMethod":
                case "PatternExtractionCondition":
                    relatedController = "erpPatternExtraction"
                    break;
                default:
                    log.debug "not found ${ontoVar}"
            }

//            println "related controllerlist ${relatedControllerList}"
        }

        if (relatedController) {
            out << "<a href=\"" + grailsLinkGenerator.link([absolute: true, action: "list", controller: relatedController, params: [related: ontological]]) + "\">"
            out << relatedController.capitalize()
            out << "<img height='20px;' src=" << resource([dir: 'images/icon', file: 'filter2.png']) << ">"
            out << "</a>"
        }
    }

    def createRelatedLink(String relatedController, Ontological related) {
//        %{--<g:link controller="condition" action="list" params="[related:conditionInstance?.taskInstruction]"--}%
//                %{--> [related] </g:link>--}%
        out << "<a href=\"" + grailsLinkGenerator.link([absolute: true, action: "list", controller: relatedController, params: [related: related.relatedLookup]]) + "\">"
        out << "<img height='12px;' src=" << resource([dir: 'images/icon', file: 'filter2.png']) << ">"
        out << "</a>"
    }


    def propertyEntry = { attrs, body ->
        def required = attrs.required
        def key = attrs.key
        def term = attrs.term
        def number = attrs.number

        out << '<label>'
        out << key
        out << ' '
        out << renderLabel([term: term, number: number])
        if (required && required != "false") {
            out << "<span class='required-indicator'>*</span>"
        }
        out << '</label>'
    }


    def showIdentifier = { attrs, body ->

        if (attrs.instance) {
            attrs.id = attrs.instance.id
            String className = attrs.instance.getClass()
            attrs.controller = attrs.controller ?: grailsApplication.getControllerClass(className + "Controller")
            if (!attrs.controller) {
                className = className.substring(className.lastIndexOf(".") + 1)
                className = className.replaceFirst(className[0], className[0].toLowerCase())
                attrs.controller = className
            }
        }
        def id = attrs.id
        def controller = attrs.controller
        if ((controller && id) || attrs.instance) {
            out << "<a href=\"" + grailsLinkGenerator.link([absolute: true, action: "show", id: id, controller: controller]) + "\">"
            out << attrs.instance.identifier ?: formatNumber([number: id])
            out << "</a>"
        }
    }

    def renderOntological = { attrs, body ->
        def delimiter = attrs.delimiter ?: "&nbsp;&bull;&nbsp;"
        def related = attrs.related
        def input = attrs.input
        // default should be true
        def sort = attrs.sort ? Boolean.valueOf(attrs.sort.toString()) : true
        if (input) {
            if (input instanceof Collection) {
                if (sort) {
                    input = input.sort(false)
                }

                input.eachWithIndex { it, index ->
                    createOntologyLink(it)
                    if (related) {
                        out << " "
                        createRelatedLink(related, it)
                    }
                    if (index < input.size() - 1) {
                        out << delimiter
                    }
                }
            } else {
                createOntologyLink(input)
                if (related) {
                    out << " "
                    createRelatedLink(related, input)
                }
            }
        }
    }

    def cancelToHome = { attrs, body ->
        out << "<a href=\"" + grailsLinkGenerator.link([absolute: true, action: "list", controller: "experiment"]) + "\">"
        if (body()) {
            out << body()
        } else {
            out << "Cancel"
        }
        out << "</a>"
    }

//    <g:link name="cancel" class="cancel" id="${experimentHeader?.id}" action="show">Cancel</g:link>
//    def cancelToParent = {
//        out << "<a name='cancel' class='cancel' href=\"" + grailsLinkGenerator.link([absolute: true, action: "show",id: ]) + "\">"
//
//    }

    def addAfterCreating = {
        out << '<div style="display: inline-block;color: #bbbbbb;">Add After Creating</div>'
    }

    def ifImage = { attrs, body ->
        def key = attrs.key?.decodeURL()
        if (key) {
            PatternImage patternImage = PatternImage.findByPatternName(key)
            if (patternImage?.image) {
                out << body()
            }
        }
    }


    def ifRawImage = { attrs, body ->
        def key = attrs.key?.decodeURL()
        if (key) {
            PatternImage patternImage = PatternImage.findByPatternName(key)
            if (patternImage?.rawImage) {
                out << body()
            }
        }
    }

    def createIndividualLink = { attrs, body ->
        String timeString = attrs.time
//        Set<ErpAnalysisResult> erpAnalysisResults = attrs.value?.decodeURL()
//        String idString = ((String) instance.value.iterator().next().id).trim()
//        idString = idString.replaceAll("\\%5B","")
//        idString = idString.replaceAll("\\%5D","")
//
//
//        String timeString = (instance.key as String)


        for (ErpAnalysisResult erpAnalysisResult in attrs.value) {
//            println "in Erp loop: ${erpAnalysisResult?.id}"

            Integer time

            Pattern regex = Pattern.compile(".*([0-9]{3,5}).*")
//            Matcher regexMatcher = regex.matcher(subjectString);

//            println "timeString ${timeString} contains 3 digits ${regex.matcher(timeString).matches()}"
//            if(timeString?.indexOf("ingr-+cngr")>=0){
//                println "IN A BAD PLACE"
//                time = null
//            }
//            else
            // if there are not 3 numbers is a row, then ignore
            if (timeString ? regex.matcher(timeString).matches() : false) {
                if (timeString.findAll("\\+").size() > 1) {
//                    println "should be here ${timeString}"
                    // to support testing
                    if (searchService == null) {
                        searchService = new SearchService()
                    }
                    time = searchService.parseExponentTimeFromLabel2(timeString)
                } else {
                    timeString = timeString.substring(timeString.lastIndexOf("+") + 1)
                    try {
                        if (!Pattern.matches("[a-zA-Z]+", timeString)) {
                            time = timeString as Integer
                        }
                    } catch (e) {
                        log.warn "not a valid string to convert ${timeString} from ${time}", e
                        time = null
                    }
                }
            } else {
                log.debug "Number not found for ${timeString}"
            }

            if (time != null && grailsLinkGenerator != null && erpAnalysisResult != null) {
                out << "<a href=\""
                out << grailsLinkGenerator.link([controller: "erpAnalysisResult", action: "showIndividuals", id: erpAnalysisResult.id, params: ["time": time]])
                out << "\">"
                out << attrs.time
                out << "</a>"
                out << "&nbsp;"
            } else {
                out << timeString
            }
        }
    }

    def createLocationLink = { attrs, body ->
        BrainLocationEnum location = attrs.location
        BrainLocationEnum selectedLocation = attrs.selectedLocation
        ErpAnalysisResult erpAnalysisResult = attrs.result

        if (location == selectedLocation) {
            out << "<td class='selected-location'>"
        } else {
            out << "<td class='unselected-location'>"
        }

        out << "<a href=\""
        out << grailsLinkGenerator.link([controller: "erpAnalysisResult", action: "showIndividualsAtLocation", id: erpAnalysisResult.id, params: ["locationName": location.name()]])
        out << "\">"
        out << "<strong>"
        out << location.name()
        out << "</strong>"
        out << "</a>"
        out << "</td>"


    }

}
