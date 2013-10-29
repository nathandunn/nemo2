package edu.uoregon.nic.nemo.portal



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(DataViewTagLib)
@Mock([ErpAnalysisResult])
class DataViewTagLibTests {

    void testCamelCase() {
        def source = "CamelCase"
        def target = "Camel Case"

        def finalString  = source.replaceAll(/([A-Z])/,' $1').trim()
        assertEquals(target,finalString)
    }

    void testCreateIndividualLink(){
        // time is name
        ErpAnalysisResult erpAnalysisResult = new ErpAnalysisResult(id:7).save(insert: true,flush:true)
        List<ErpAnalysisResult> erpAnalysisResultList = new ArrayList<ErpAnalysisResult>()
        erpAnalysisResultList.add(erpAnalysisResult)

        String instantString = "DLM-Dataset2_+ingr-+cngr_ERP_+096"
        String result = applyTemplate('<g:createIndividualLink time="${instantString}" value="${erpAnalysisResultList}"/>'
                ,[instantString: instantString,erpAnalysisResultList:erpAnalysisResultList])
        assert result == instantString

        instantString = "ingr-+cngr"
        result = applyTemplate('<g:createIndividualLink time="${instantString}" value="${erpAnalysisResultList}"/>'
                ,[instantString: instantString,erpAnalysisResultList:erpAnalysisResultList])
        println "result2 [${result}]"
        assert result == instantString
//        assert applyTemplate('<g:createIndividualLink time="${instantString}" value="${erpAnalysisResult}"/>') == 'Hello World'
    }
}
