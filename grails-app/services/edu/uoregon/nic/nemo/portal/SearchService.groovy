package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.IndividualDTO
import edu.uoregon.nemo.nic.portal.util.SearchResultDTO
import edu.uoregon.nemo.nic.portal.util.SearchResultsDTO
import edu.uoregon.nemo.nic.portal.util.TermLinkContainer
import edu.uoregon.nic.nemo.portal.client.BrainLocationEnum
import edu.uoregon.nic.nemo.portal.client.SelectedLocationEnum
import grails.converters.JSON
import org.grails.datastore.mapping.query.api.Criteria
import org.hibernate.FetchMode as FM
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.OWLReasoner
import org.uoregon.nemo.nic.QueryListEnum

import java.util.regex.Matcher
import java.util.regex.Pattern

//@CompileStatic
class SearchService {

    static expose = ['gwt:edu.uoregon.nic.nemo.portal.client']

//    static private List<SearchResultDTO> cachedSearches = new ArrayList<>()
    static private BRAIN_LOCATION_COUNT = 20

    def ontologyService
    def grailsLinkGenerator

    def runAsynchornous = true


    void cacheSearch() {
//        cachedSearches.clear()
//        Individual.deleteAll(Individual.all)


        List<ErpAnalysisResult> erpAnalysisResultList = ErpAnalysisResult.executeQuery("from ErpAnalysisResult erp where erp.inferredOntology is not null and erp.individuals.size=0")

        log.debug "# to be recached ${erpAnalysisResultList.size()}"

        for (ErpAnalysisResult erpAnalysisResult in erpAnalysisResultList) {
            log.warn "reaching ${erpAnalysisResult.artifactFileName}"
            cacheErpResultIndividuals(erpAnalysisResult)
        }

//        ErpAnalysisResult.findAllByInferredOntologyIsNotNull([sort: "artifactFileName", order: "asc"]).each { ErpAnalysisResult erpAnalysisResult ->
//            if (Individual.countByErpAnalysisResult(erpAnalysisResult) == 0) {
//                println "Recaching search: ${erpAnalysisResult.artifactFileName}"
//            }
//        }

//        return cachedSearches
    }

    def cacheErpResultIndividuals(ErpAnalysisResult erpAnalysisResult) {
        log.debug "result ${erpAnalysisResult.artifactFileName}"


        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager()
//            TODO: should use the cached reasoner
        OWLReasoner reasoner = ontologyService.getReasonerForErpAnalysisResult(erpAnalysisResult)

        SearchResultDTO searchResultDTO = new SearchResultDTO()
        searchResultDTO.erpAnalysisResultName = erpAnalysisResult.artifactFileName
        searchResultDTO.erpAnalysisResultId = erpAnalysisResult.id

        // TODO: Get instances of statistical significance only:  NEMO_9658000
        // has instances of the form: GAF-LP1_+NN-+NW_ERP_+372_mean_intensity_MFRONT_statistical_quality
        OWLDataFactory owlDataFactory = owlOntologyManager.getOWLDataFactory()
        OWLClass statisticalSignificantParent = owlDataFactory.getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.STATISTICALLY_SIGNIFICANT.url))
//            OWLClass hasNumericValueParent = owlDataFactory.getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.HAS_NUMERIC_VALUE.url))
        OWLClass meanIntensityMeasurementClass = owlDataFactory.getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.MEAN_INTENSITY_MEASUREMENT_DATUM.url))
        IRI propname = IRI.create(OntologyService.NS + QueryListEnum.HAS_NUMERIC_VALUE.url)
        OWLDataProperty owlDataProperty = owlDataFactory.getOWLDataProperty(propname)

        Set<String> significantSet = new HashSet<>()

        Set<OWLNamedIndividual> statisticallySignificantIndividuals = reasoner.getInstances(statisticalSignificantParent, false).flattened
        Set<OWLNamedIndividual> meanIntensityIndividuals = reasoner.getInstances(meanIntensityMeasurementClass, false).flattened


        for (OWLNamedIndividual owlNamedIndividual in statisticallySignificantIndividuals) {

            String url = owlNamedIndividual.toStringID()
            if (url.contains("mean_intensity") && url.contains("statistical_quality")) {
                String lookupUrl = url.substring(0, url.length() - "_statistical_quality".length())
                significantSet.add(lookupUrl)
            }
        }

//            println "signicant ${significantSet.size()} example ${significantSet.iterator().next()}"
//            println "meanIntensity ${meanIntensityIndividuals.size()}"


        for (OWLNamedIndividual instance in meanIntensityIndividuals) {
            // TODO: walk through the instance to get out the correct values
            String url = instance.toStringID()
//                println "evaluating url ${url}"
//                String label = ontologyService.getLabelForUrl(url)
            Integer time = parseTimeFromLabel(url)
            if (time == null) {
                time = parseExponentTimeFromLabel(url)
            }

//            TreeSet<TermLinkContainer> mappedInstances = new TreeSet<>()
            TreeSet<String> mappedInstances = new TreeSet<>()
            BrainLocationEnum location = parseLocationFromMeanIntensity(url)
            if (location != null && time != null) {
                IndividualDTO newIndividualDTO = new IndividualDTO()
//                    String linkUrl = grailsLinkGenerator.link()
                newIndividualDTO.url = url
                newIndividualDTO.peakTime = time

                for (OWLLiteral literal in reasoner.getDataPropertyValues(instance, owlDataProperty)) {
//                    println "putting in URL [${owlNamedIndividual.toStringID()}]"
                    newIndividualDTO.meanIntensity = literal.parseFloat()
//                            meanIntensityMap.put(instance.toStringID(),literal.parseFloat())
//                    println "reasoner literal ${literal.literal}"
                }

                newIndividualDTO.significant = significantSet.contains(url)

//                        println "${url} -> meanIntensity keys ${meanIntensityMap.keySet().toArray()}"
                ontologyService.generatedMappedInstances(erpAnalysisResult).values().each{ TreeSet<TermLinkContainer> it ->
                    it.each { TermLinkContainer termLinkContainer ->
                        mappedInstances.addAll(termLinkContainer.label)
                    }
                }

                 ontologyService.generatedMappedInstances(erpAnalysisResult).values()


                Individual individual = new Individual(
                        url: url
                        , statisticallySignificant: newIndividualDTO.significant
                        , peakTime: time
                        , meanIntensity: newIndividualDTO.meanIntensity
                        , location: location
                        , erpAnalysisResult: erpAnalysisResult
                        , mappedInstances: mappedInstances.join("|")
                ).save(insert: true, flush: true)

            }
        }
//        if (searchResultDTO.individuals.individualDTOList.size() > 0) {
//            cachedSearches.add(searchResultDTO)
//        }
    }


    String searchErps(Integer minTime, Integer maxTime, Map<BrainLocationEnum, SelectedLocationEnum> brainSelectedBrainLocationMap) {
        cacheSearch()
//        log.debug "erpSearch: min ${min} max ${max}"
//        if (cachedSearches.size() == 0) {
//            log.info "RECACHING!!! ${cachedSearches.size()}"
//            cachedSearches = cacheSearch()
//            log.info "done RECACHING!!! ${cachedSearches.size()}"
//        } else {
//            log.debug "no need to reacche ${cachedSearches.size()}"
//        }

        println "searching with min ${minTime} max ${maxTime} and map ${brainSelectedBrainLocationMap?.size()}"

        Criteria individualCriteria = Individual.createCriteria()
        SelectedLocationEnum allSelected = findAllSelected(brainSelectedBrainLocationMap)

        List<Individual> individualList

        long startTime = System.currentTimeMillis()
        long stopTime = System.currentTimeMillis()

        if (allSelected == SelectedLocationEnum.OFF) {
            individualList = new ArrayList<>()
        } else {
//        List<Individual> individualList = Individual.findAllByPeakTimeGreaterThanAndPeakTimeLessThanAndStatisticallySignificant(minTime,maxTime,true)
            individualList = individualCriteria.list() {
//                join "erpAnalysisResult"
                fetchMode "erpAnalysisResult", FM.SELECT
                and {
                    eq("statisticallySignificant", true)
                    ge("peakTime", minTime)
                    le("peakTime", maxTime)
                    switch (allSelected) {
                        case SelectedLocationEnum.POSITIVE:
                            gt("meanIntensity", 0f)
                            break
                        case SelectedLocationEnum.NEGATIVE:
                            lt("meanIntensity", 0f)
                            break
                        case SelectedLocationEnum.BOTH:
                            break
                    // should include null
                        case null:
                        default:
                            or {
                                for (BrainLocationEnum brainLocationEnum in brainSelectedBrainLocationMap.keySet()) {
                                    SelectedLocationEnum selectedLocationEnum = brainSelectedBrainLocationMap.get(brainLocationEnum)
                                    switch (selectedLocationEnum) {
                                        case SelectedLocationEnum.BOTH:
                                            eq("location", brainLocationEnum)
                                            break
                                        case SelectedLocationEnum.POSITIVE:
                                            and {
                                                eq("location", brainLocationEnum)
                                                ge("meanIntensity", 0f)
                                            }
                                            break
                                        case SelectedLocationEnum.NEGATIVE:
                                            and {
                                                eq("location", brainLocationEnum)
                                                le("meanIntensity", 0f)
                                            }
                                            break
                                        case SelectedLocationEnum.OFF:
                                            break
                                        default:
                                            println "NOT SURE HOW I GOT HERE"
                                    }
                                }
                            }
                            break
                    }
                }
//                order("erpAnalysisResult", "asc")
                order("peakTime", "asc")
                order("meanIntensity", "asc")
//                order("location.name","asc")
//                order("name", "asc")
            }
        }

        stopTime = System.currentTimeMillis()
        log.debug("fetch time = ${stopTime - startTime}ms")
        startTime = System.currentTimeMillis()

        log.debug "individuals found ${individualList.size()}"

//        Map<String, SearchResultDTO> searchResultDTOMap = new HashMap<String, SearchResultDTO>()

        SearchResultsDTO results = new SearchResultsDTO()
        SearchResultDTO searchResultDTO = null

        String erpName = null

        log.debug "START formatting results"

        individualList.sort(true) { a, b ->
            a.erpAnalysisResult.artifactFileName.compareTo(b.erpAnalysisResult.artifactFileName)
        }

        individualList.each { individual ->
            ErpAnalysisResult currentErpAnalysisResult = individual.erpAnalysisResult
            String newName = currentErpAnalysisResult.artifactFileName

            if (erpName != newName) {
                erpName = newName
                searchResultDTO = new SearchResultDTO()
                searchResultDTO.erpAnalysisResultName = newName
                searchResultDTO.erpAnalysisResultId = individual.erpAnalysisResultId
                searchResultDTO.link = grailsLinkGenerator.link(controller: "erpAnalysisResult", action: "show", id: individual.erpAnalysisResultId)
                results.addToList(searchResultDTO)
            }

            IndividualDTO individualDTO = new IndividualDTO()
            individualDTO.url = individual.url
            individualDTO.meanIntensity = individual.meanIntensity
            individualDTO.peakTime = individual.peakTime
            individualDTO.significant = true
            individualDTO.thisLocation = individual.location
            String linkUrl = grailsLinkGenerator.link(controller: "erpAnalysisResult", action: "show", absolute: true, id: individual.erpAnalysisResultId + "#${individualDTO.nameFromUrl}").decodeURL()
            individualDTO.linkFromUrl = linkUrl
            String timeUrl = grailsLinkGenerator.link(controller: "erpAnalysisResult", action: "showIndividuals", params: [time: individual?.peakTime], absolute: true, id: individual.erpAnalysisResultId).decodeURL()
            individualDTO.peakTimeUrl = timeUrl
            String locationUrl = grailsLinkGenerator.link(controller: "erpAnalysisResult", action: "showIndividualsAtLocation", params: [locationName: individual?.location?.name()], absolute: true, id: individual.erpAnalysisResultId).decodeURL()
            individualDTO.locationUrl = locationUrl

            searchResultDTO.individuals.individualDTOList.add(individualDTO)
        }


        stopTime = System.currentTimeMillis()
        log.debug("conversion time = ${stopTime - startTime}ms")
        startTime = System.currentTimeMillis()

        log.debug "END formatting results"

        results.resortSearchResults()

        def returnObject = results as JSON

        stopTime = System.currentTimeMillis()
        log.debug("JSON conversion time = ${stopTime - startTime}ms")
        startTime = System.currentTimeMillis()

        log.debug "converted to JSON "

        return returnObject
    }

    SelectedLocationEnum findAllSelected(Map<BrainLocationEnum, SelectedLocationEnum> brainLocationEnumSelectedLocationEnumMap) {

        SelectedLocationEnum selectedLocationEnum1 = null

        if (brainLocationEnumSelectedLocationEnumMap.size() == 0) {
            return SelectedLocationEnum.OFF
        }

        if (brainLocationEnumSelectedLocationEnumMap.size() == BRAIN_LOCATION_COUNT) {
            // if all locations are the same . . then
            for (SelectedLocationEnum selectedLocationEnum in brainLocationEnumSelectedLocationEnumMap.values()) {
                if (selectedLocationEnum1 == null) {
                    selectedLocationEnum1 = selectedLocationEnum
                } else if (selectedLocationEnum1 != selectedLocationEnum) {
                    return null
                }
            }
        }
        return selectedLocationEnum1
    }

//    String searchErps(Integer min, Integer max, List<BrainLocationEnum> locations, Integer minPeak, Integer maxPeak) {
//        log.debug "erpSearch: min ${min} max ${max}"
////        if (cachedSearches.size() == 0) {
////            log.info "RECACHING!!! ${cachedSearches.size()}"
////            cachedSearches = cacheSearch()
////            log.info "done RECACHING!!! ${cachedSearches.size()}"
////        } else {
////            log.debug "no need to reacche ${cachedSearches.size()}"
////        }
//        SearchResultsDTO results = new SearchResultsDTO()
//
//        log.debug "locations ${locations}"
//
////        for (SearchResultDTO searchResultDTO in cachedSearches) {
////
////            SearchResultDTO searchResultDTONew = new SearchResultDTO()
////            searchResultDTONew.individuals = searchResultDTO.searchIndividuals(min, max, locations, minPeak, maxPeak)
//////            searchResultDTONew.individuals = searchResultDTO.searchIndividuals(min, max, locations)
//////            println "searchResults individiusl ${searchResultDTONew.individuals.individualDTOList}"
////
////            if (searchResultDTONew.individuals.individualDTOList.size() > 0) {
////                searchResultDTONew.erpAnalysisResultId = searchResultDTO.erpAnalysisResultId
////                searchResultDTONew.erpAnalysisResultName = searchResultDTO.erpAnalysisResultName
////                searchResultDTONew.link = grailsLinkGenerator.link(controller: "erpAnalysisResult", action: "show", id: searchResultDTONew.erpAnalysisResultId)
////
////                println "a non-null hit ${searchResultDTONew} of size ${searchResultDTONew.individuals.individualDTOList.size()}"
////
////                // TODO: compare locations next
////                results.addToList(searchResultDTONew)
////            }
////        }
//
//        return results as JSON
//    }

//    String searchErps(Integer min, Integer max, List<BrainLocationEnum> locations) {
//        return searchErps(min, max, locations, Integer.MIN_VALUE, Integer.MAX_VALUE)
//    }


    String searchErpsRaw(Integer min, Integer max, List<BrainLocationEnum> locations) {
        SearchResultsDTO results = new SearchResultsDTO()

        ErpAnalysisResult.findAllByInferredOntologyIsNotNull().each { ErpAnalysisResult erpAnalysisResult ->
//            ontologyService.findInstancesAndLocations(result)
            OWLReasoner reasoner = ontologyService.getReasonerForErpAnalysisResult(erpAnalysisResult)
//            OWLOntology localOntology = ontologyService.getOntologyFromErpAnalysisResult(erpAnalysisResult)

            SearchResultDTO searchResultDTO = new SearchResultDTO()
            searchResultDTO.erpAnalysisResultName = erpAnalysisResult.artifactFileName
            searchResultDTO.erpAnalysisResultId = erpAnalysisResult.id

            OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager()
            // TODO: Get instances of statistical significance only:  NEMO_9658000
            // has instances of the form: GAF-LP1_+NN-+NW_ERP_+372_mean_intensity_MFRONT_statistical_quality

            // quality_of GAF-LP1_+NN-+NW_ERP_+372_mean_intensity_MFRONT
            // inheres_in GAF-LP1_+NN-+NW_ERP_+372_mean_intensity_MFRONT
            OWLClass parent = owlOntologyManager.getOWLDataFactory().getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.STATISTICALLY_SIGNIFICANT.url))

            Set<OWLNamedIndividual> individuals = reasoner.getInstances(parent, false).flattened
//            println "# of individuals ${individuals.size()}"
//            Set<OWLNamedIndividual> subclasses = reasoner.getSubClasses(parent, false).flattened
//            println "# of subcalsses ${subclasses.size()}"

            for (OWLNamedIndividual instance in individuals) {

                // TODO: walk through the instance to get out the correct values
                String url = instance.toStringID()
//            String label = ontologyService.getLabelForUrl(url)
                if (url.contains("mean_intensity") && url.contains("statistical_quality")) {
//                if(label == null || !label.startsWith("TESTEXPT")){
//                    println "instance ${instance.toStringID()}"
                    Integer time = parseTimeFromLabel(url)
//                    BrainLocationEnum location = parseLocationFromStatisticalQuality(url)
                    if (time >= min && time <= max) {

                        // also compare locations, as well

                        IndividualDTO individualDTO = new IndividualDTO()
//                        individualDTO.name = label
                        individualDTO.url = url

                        searchResultDTO.individuals.individualDTOList.add(individualDTO)

                    }
//                    else{
//                        println "excluded ${instance.toStringID()}"
//                    }
                }
//                else{
//                    println "bad url ${url}"
//                    println "bad label ${label}"
//                }

            }
            if (searchResultDTO.individuals.individualDTOList.size() > 0) {
                results.addToList(searchResultDTO)
            }
//            else{
//                println "no individuals for search result ${searchResultDTO as JSON}"
//            }
        }
        return results as JSON
    }

    BrainLocationEnum parseLocationFromMeanIntensity(String s) {
        Pattern pattern = Pattern.compile(".*mean_intensity_(.*)")
//        Pattern pattern = Pattern.compile(".*\(\\+108")
        Matcher matcher = pattern.matcher(s)
        if (matcher.matches()) {
            try {
                return matcher.group(1) as BrainLocationEnum
            } catch (Exception e) {
                log.debug "bad location ${e}"
                return null
            }
        } else {
//            log.debug "location found!! ${s}"
            return null;
        }
    }

//     String testLabel1 = "http://purl.bioontology.org/NEMO/data/GAF-LP2_+NN-+NW_ERP_+108_mean_intensity_LPAR_statistical_quality"
    BrainLocationEnum parseLocationFromStatisticalQuality(String s) {
        Pattern pattern = Pattern.compile(".*mean_intensity_(.*)_statistical_quality.*")
//        Pattern pattern = Pattern.compile(".*\(\\+108")
        Matcher matcher = pattern.matcher(s)
        if (matcher.matches()) {
            try {
                return matcher.group(1) as BrainLocationEnum
            } catch (Exception e) {
                log.debug "bad location ${e}"
                return null
            }
        } else {
//            log.debug "location found!! ${s}"
            return null;
        }
    }

    /**
     *  http://purl.bioontology.org/NEMO/data/JFC-Linna_S4-S1_ERP_+2.109375e+02_mean_intensity_RPTEMP
     * @param s
     * @return
     */
    Integer parseExponentTimeFromLabel(String s) {
        Pattern pattern = Pattern.compile(".*\\+(\\d.\\d\\d).*\\+(\\d\\d)_.*")
//        Pattern pattern = Pattern.compile(".*\(\\+108")
        Matcher matcher = pattern.matcher(s)
        if (matcher.matches()) {
            String stringValue = matcher.group(1) + matcher.group(2)
            Float floatValue = (matcher.group(1) as Float) * Math.pow(10, matcher.group(2) as Float)
            return floatValue.round()
        } else {
//            log.debug "time not found!! ${s}"
            return null;
        }
    }

    Integer parseExponentTimeFromLabel2(String s) {
        String originalString = s
        Pattern pattern1 = Pattern.compile(".*\\+(\\d.\\d\\d).*\\+(\\d\\d)")
//        Pattern pattern = Pattern.compile(".*\(\\+108")
        Matcher matcher = pattern1.matcher(s)
        if (matcher.matches()) {
            String stringValue = matcher.group(1) + matcher.group(2)
            Float floatValue = (matcher.group(1) as Float) * Math.pow(10, matcher.group(2) as Float)
            return floatValue.round()
        } else if (s.lastIndexOf("_+") >= 0) {
            s = s.substring(s.lastIndexOf("_+") + 2)
            if (s.indexOf("_") > 0) {
                s = s.substring(0, s.indexOf("_"))
            }
            if (s.indexOf("e+") > 0) {
                String[] splitString = s.split("e\\+")
                Float floatValue = (splitString[0] as Float) * Math.pow(10, splitString[1] as Float)
                return floatValue.round()
            } else {
                Integer returnInteger = -1
                try {
                    returnInteger = s as Integer
                } catch (e) {
                    log.error "Error converting ${s} from ${originalString} due to ${e}"
                }
                return returnInteger
            }
//            log.debug "time not found!! ${s}"
//            return null;
        } else if (s.lastIndexOf("_") >= 0) {
            s = s.substring(s.lastIndexOf("_") + 1)
            if (s.indexOf("e+") > 0) {
                String[] splitString = s.split("e\\+")
                Float floatValue = (splitString[0] as Float) * Math.pow(10, splitString[1] as Float)
                return floatValue.round()
            } else {
                return s as Integer
            }
        } else {
            return 0
        }
    }

//     String testLabel1 = "http://purl.bioontology.org/NEMO/data/GAF-LP2_+NN-+NW_ERP_+108_mean_intensity_LPAR_statistical_quality"
// find last instance of "+NNN
    Integer parseTimeFromLabel(String s) {
        Pattern pattern = Pattern.compile(".*\\+(\\d\\d\\d).*")
//        Pattern pattern = Pattern.compile(".*\(\\+108")
        Matcher matcher = pattern.matcher(s)
        if (matcher.matches()) {
            return matcher.group(1) as Integer
        } else {
//            log.debug "time not found!! ${s}"
            return null;
        }
    }

    def cacheSearchAsync() {
        runAsync {
            cacheSearch()
        }
    }

    String findPeakIntensities(Long id, Integer time){
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)
        if (!erpAnalysisResultInstance) {
            return 404
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
//            redirect(action: "list")
//            return
        }
        List<Integer> times = (List<Integer>) Individual.executeQuery("select i.peakTime from Individual i where i.erpAnalysisResult = :erpAnalysisResult  group by i.peakTime order by i.peakTime asc"
                , ["erpAnalysisResult": erpAnalysisResultInstance])
        List<Individual> individualList = Individual.findAllByErpAnalysisResultAndPeakTime(erpAnalysisResultInstance, time)
        Map<BrainLocationEnum, Individual> individualMap = new HashMap<>()

        for (Individual individual in individualList) {
            individualMap.put(individual.location, individual)
        }
        return individualMap as JSON
    }
}

