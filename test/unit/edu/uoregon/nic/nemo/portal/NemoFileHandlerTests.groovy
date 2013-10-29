package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.NemoFileHandler
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin

/**
 //                Term (ordered T--> B)    RDF term    Graphics term    Meaning (ontology labels)    NEMO URI    Values
 //                1    GAF-LP1    GAF-LP1    <Lab_ID> - <Experiment_ID>    TBA    user-specified (under onto control)
 //                2    _+NN-+NW    _NW.NN    _ <condition_of_interest> - <baseline_condition>    TBA    user-specified (idiosynchratic to each dataset)
 //                3    _ERP    _Decomposition    _<electrophysiological_pattern_extraction_method>    NEMO_5263000    _Decomposition; _Segmentation
 //                4    _+004    _Factor0004ms    _ <pattern_type> <4-digit number> <unit>    TBA    _Factor0001ms; _Microstate0001ms
 //
 //                Note that this is reversed
 //                SummeredFactor is reconstructed
 //                Factor is not
 */
@TestMixin(NemoFileHandler)
class NemoFileHandlerTests implements  GroovyObject{

//    e.g. DLM-LDT_word.nonword_Decomposition_SummedFactors0696ms.png
    void test1() {
        String testInputString = "DLM-LDT_word.nonword_Decomposition_SummedFactors0696ms.png"
        assertTrue(NemoFileHandler.isImageRaw(testInputString))
        assertEquals "DLM-LDT_nonword-word_ERP_+700" , NemoFileHandler.convertImageName(testInputString)
    }

//    e.g. DLM-LDT_word.nonword_Decomposition_Factors0696ms.png
    void test1a() {
        String testInputString = "DLM-LDT_word.nonword_Decomposition_Factor0696ms.png"
        assertFalse(NemoFileHandler.isImageRaw(testInputString))
        assertEquals "DLM-LDT_nonword-word_ERP_+700" , NemoFileHandler.convertImageName(testInputString)
    }

//    e.g., GF-LEAP-part1_Laugh_Pos.Laugh_Neg_Decomposition_SummedFactors0332ms.png
    void test2() {
        String testInputString = "GF-LEAP-part1_Laugh_Pos.Laugh_Neg_Decomposition_SummedFactors0332ms.png"
        assertTrue(NemoFileHandler.isImageRaw(testInputString))
        assertEquals "GF-LEAP-part1_Laugh_Neg-Laugh_Pos_ERP_+336" , NemoFileHandler.convertImageName(testInputString)
    }
//    GF-LEAP-part1_PosIADS_Pos.PosIADS_Neg_Decomposition_SummedFactors0884ms.png ->
    void test3() {
        String testInputString = "GF-LEAP-part1_PosIADS_Pos.PosIADS_Neg_Decomposition_SummedFactors0884ms.png"
        assertTrue(NemoFileHandler.isImageRaw(testInputString))
        assertEquals "GF-LEAP-part1_PosIADS_Neg-PosIADS_Pos_ERP_+888" , NemoFileHandler.convertImageName(testInputString)
    }
}
