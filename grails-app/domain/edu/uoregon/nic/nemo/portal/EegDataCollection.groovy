package edu.uoregon.nic.nemo.portal

class EegDataCollection {

    static constraints = {
        experiment nullable: false
        identifier nullable: false,unique: true,blank: false
        samplingRateSetting nullable: false

        ground nullable: true
        reference nullable: true
        electrodeArrayLayout nullable: false

    }

    static belongsTo = [
            Experiment
    ]

    Experiment experiment
//    String electrodeArrayManufacturer

    ElectrodeArrayManufacturer manufacturer


//    String sensorNetMontage
    ElectrodeArrayLayout electrodeArrayLayout

//    String referenceElectrode
    ReferenceElectrode reference

//    String groundElectrode
    GroundElectrode ground


    Float scalpElectrodeImpedanceThreshold
    Float gainMeasurementDatum
    Float amplifierInputImpedance
    Float samplingRateSetting
    Float voltageAmplifierHighpassFilterSetting
    Float voltageAmplifierLowpassFilterSetting

//    String eegDataCollectionSoftware
    Software software

    Boolean unverifiedCopy
    Date createdAt
    Date updatedAt
    String identifier
//    version

}
