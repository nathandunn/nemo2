package edu.uoregon.nic.nemo.portal

import grails.web.RequestParameter
import org.uoregon.nemo.nic.QueryListEnum

class PatternController {

    def ontologyService

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

}
