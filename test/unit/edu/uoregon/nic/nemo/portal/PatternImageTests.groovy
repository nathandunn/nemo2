package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(PatternImage)
@Mock([DataSet,DataFormat])
class PatternImageTests extends AbstractUnitTest{

    void testSomething() {
        ErpAnalysisResult erpAnalysisResult = new ErpAnalysisResult(
                artifactFileName: getRandomString()
                , set: new DataSet()
                , format: new DataFormat()
        ).save(failOnError: true)
        new PatternImage(
                patternName: getRandomString()
                ,image:  new byte[12]
                ,rawImage:  new byte[12]
                ,erpAnalysisResult: erpAnalysisResult
        )
        .save(failOnError: true)
    }
}
