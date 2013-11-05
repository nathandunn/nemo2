package edu.uoregon.nic.nemo.portal

import grails.web.RequestParameter
import org.uoregon.nemo.nic.QueryListEnum

class PatternController {

    def ontologyService
    def searchService

    def index() {
        [instances: ontologyService.getInstanceMap()]
    }


    def show(@RequestParameter('id') String url) {
//        def filter = params.hasProperty("filter") ? getProperty("filter") : ""

        // should map onto instance (with image, etc.) and experiment / data_file on right
        if (url == null) {
            log.debug "id is null so not found . . .showing default"
            flash.message = "not ID passed so showing default ${QueryListEnum.MFN_LEXICALITY_EFFECT.url} "
            redirect(controller: "pattern", action: "show", id: QueryListEnum.MFN_LEXICALITY_EFFECT.url.substring(1))
            return
        }

        Map<String, Set<ErpAnalysisResult>> instancesMap = new TreeMap<String, Set<ErpAnalysisResult>>()
        String label = ontologyService.getLabelForUrl(url)
        if (label == null) {
            [instances: instancesMap, label: "Not Found", url: url, availableInstances: ontologyService.getInstanceMap(), id: url]
        } else {
            instancesMap = ontologyService.getErpsFromErpAnalysisResults(url)

            [instances: instancesMap, label: label, url: url, availableInstances: ontologyService.getInstanceMap(), id: url]
        }

//        println "keymap size: " + mfnLexicalityEffectsMap.size()
//        for (key in mfnLexicalityEffectsMap.keySet()) {
//            println "key: " + key + " values[]" + mfnLexicalityEffectsMap.get(key)
//        }

//        render(view: "instances", model: [instances: mfnLexicalityEffectsMap])
//        render(view: "instances", model: [instances: mfnLexicalityEffectsMap])
    }

    def looseSearch() {

    }

    def generateSummary(@RequestParameter('id') String url) {
//    def generateSummary(Long id) {
//        println "!!!!!!!!!!!!!!!URL CALLED ${id}"
//        String url = String.valueOf(id)
//        if (url == null) {
//            log.debug "id is null so not found . . .showing default"
//            flash.message = "not ID passed so showing default ${QueryListEnum.MFN_LEXICALITY_EFFECT.url} "
//            redirect(controller: "pattern", action: "show", id: QueryListEnum.MFN_LEXICALITY_EFFECT.url.substring(1))
//            return
//        }

        Map<String, Set<ErpAnalysisResult>> instancesMap = new TreeMap<String, Set<ErpAnalysisResult>>()
        String label = ontologyService.getLabelForUrl(url)
        if (label == null) {
            render "NO Instances Found"
//            [instances: instancesMap, label: "Not Found", url: url, availableInstances: ontologyService.getInstanceMap(), id: url]
        } else {

            instancesMap = ontologyService.getErpsFromErpAnalysisResults(url)
            Integer maxTime = 0
            Integer minTime = Integer.MAX_VALUE
            Map<Integer, Long> instances = new TreeMap<>()
            for (String key in instancesMap.keySet()) {
                println "trying to parse ${key}"
                if (key.matches(".*\\d.*") && key.indexOf("_object")<0) {
                    println "contains number ${key}"

                    Integer time = searchService.parseTimeFromLabel(key)

                    if (time == null) {
                        time = searchService.parseExponentTimeFromLabel(key)
                    }

                    if (time == null) {
                        time = searchService.parseExponentTimeFromLabel2(key)
                    }

                    if (time) {
                        instances.put(time, instancesMap.get(key).iterator().next().id)
                    }
                }

            }
//
//            [instances: instancesMap, label: label, url: url, availableInstances: ontologyService.getInstanceMap(), id: url]

            render view: "summary", model: [instances: instances]
        }
    }

}
