package edu.uoregon.nemo.nic.portal.util

import groovy.transform.CompileStatic

/**
 //                Term (ordered T--> B)    RDF term    Graphics term    Meaning (ontology labels)    NEMO URI    Values
 //                1    GAF-LP1    GAF-LP1    <Lab_ID> - <Experiment_ID>    TBA    user-specified (under onto control)
 //                2    _+NN-+NW    _NW.NN    _ <condition_of_interest> - <baseline_condition>    TBA    user-specified (idiosynchratic to each dataset)
 //                3    _ERP    _Decomposition    _<electrophysiological_pattern_extraction_method>    NEMO_5263000    _Decomposition; _Segmentation
 //                4    _+004    _Factor0004ms    _ <pattern_type> <4-digit number> <unit>    TBA    _Factor0001ms; _Microstate0001ms

 e.g. DLM-LDT_word.nonword_Decomposition_SummedFactors0696ms.png
 e.g., GF-LEAP-part1_Laugh_Pos.Laugh_Neg_Decomposition_SummedFactors0332ms.png
 e.g., GF-LEAP-part1_PosIADS_Neg-PosIADS_Pos_ERP_+108 ->
 GF-LEAP-part1_PosIADS_Pos.PosIADS_Neg_Decomposition_SummedFactors0884ms.png ->

 // first underscrore
 <Experiment>_<condition>_Decomposition_<Factor/SummedFactor><NNNNms>.png
 //
 //                Note that this is reversed
 //                SummeredFactor is reconstructed
 //                Factor is not
 */
@CompileStatic
class NemoFileHandler {
    public final static String NEMO_SUFFIX_TYPE = "nemo"

    public static String convertImageName(String inputName){
        boolean isRawImage = isImageRaw(inputName)
        List<String> tokens = inputName.tokenize("_")
        int indexOfDecomposition = inputName.indexOf("_Decomposition")
        String experiment = tokens.get(0)
        int experimentLength = experiment.length()
        String originalCondition = inputName.substring(experimentLength+1,indexOfDecomposition)
        int indexOfFactor = inputName.indexOf("Factor")
        int indexOfMs = inputName.indexOf("ms.png")
        int factorLength = isRawImage ? "Factors".length() :"Factor".length()
        String timeString = inputName.substring(indexOfFactor+factorLength,indexOfMs)
        Integer time = Integer.parseInt(timeString)
        time += 4
        String convertedTimeString = time.toString().padLeft(3)

        String returnString =  experiment
        returnString += "_"
        returnString += convertCondition(originalCondition)
        returnString += "_"
        returnString += "ERP"
        returnString += "_"
        returnString += "+"
        returnString += convertedTimeString

        return returnString

    }

    static String convertCondition(String inputString) {
        List<String> tokens = inputString.tokenize(".")

        String returnString = ""
//        returnString += "+"
        returnString += tokens[1]
        returnString += "-"
//        returnString += "+"
        returnString += tokens[0]

        return returnString;
    }

    public static Boolean isImageRaw(String inputName){
        if(inputName.contains("Factor")){
            return inputName.contains("SummedFactors")
        }
        else{
            throw new RuntimeException("Unable to handle file name ${inputName}")
        }
    }
}
