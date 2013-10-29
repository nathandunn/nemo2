package edu.uoregon.nic.nemo.portal

import edu.uoregon.nic.nemo.portal.client.BrainLocationEnum
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(SearchService)
class SearchServiceTests {

    String testLabel1 = "http://purl.bioontology.org/NEMO/data/GAF-LP2_+NN-+NW_ERP_+108_mean_intensity_LPAR_statistical_quality"
    String testLabel2 = "http://purl.bioontology.org/NEMO/data/JFC-Linna_S4-S1_ERP_+2.109375e+02_mean_intensity_RPTEMP"
    SearchService searchService = new SearchService()

    void testTime() {
        assert 108==searchService.parseTimeFromLabel(testLabel1)
    }

    void testLocation() {
        assert BrainLocationEnum.LPAR==searchService.parseLocationFromStatisticalQuality(testLabel1)
    }


    void testExponentTime() {
        assert 210==searchService.parseExponentTimeFromLabel(testLabel2)
    }

    void testParseInstance(){
        String instantString = "DLM-Dataset2_+ingr-+cngr_ERP_+096"
        Integer value = searchService.parseExponentTimeFromLabel2(instantString)
        println "value ${value}"
        assert 96==value

    }

}
