package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.ProcessingStatus
import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ErpPatternExtraction)
class ErpPatternExtractionTests {

    void testProcessing1() {
        ErpPatternExtraction dataFile = new ErpPatternExtraction(
                artifactFileName: RandomStringGenerator.getRandomString()
                , set: new DataSet()
                , format: new DataFormat()
        ).save(failOnError: true)
    }

//    void testSort(){
////        TC-RGLD2_+LN0-+LW0_ERP_+1016
////        TC-RGLD2_+LN0-+LW0_ERP_+1072
////        TC-RGLD2_+LN0-+LW0_ERP_+120
////        TC-RGLD2_+LN0-+LW0_ERP_+1544
////        TC-RGLD2_+LN0-+LW0_ERP_+160
////        TC-RGLD2_+LN0-+LW0_ERP_+224
////        TC-RGLD2_+LN0-+LW0_ERP_+296
////        TC-RGLD2_+LN0-+LW0_ERP_+356
//
//    }
}
