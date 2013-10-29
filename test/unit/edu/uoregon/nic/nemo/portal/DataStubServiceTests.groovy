package edu.uoregon.nic.nemo.portal



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(DataStubService)
//@Mock([Experiment])
@Mock([DataFormat,DataSet,Experiment,ErpPatternExtraction,ErpAnalysisResult])
class DataStubServiceTests {

    @Test
    public void testFlattenParadigms(){
        service.flattenExperimentalParadigms()
    }

    void testCreateErpAnalysisResults() {
        service.createErpAnalysisResults()
    }

    void testCreateErpPatternExtractions() {
        service.createErpPatternExtractions()
    }

}
