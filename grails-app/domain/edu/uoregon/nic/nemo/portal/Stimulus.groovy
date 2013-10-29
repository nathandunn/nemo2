package edu.uoregon.nic.nemo.portal

class Stimulus {

    static belongsTo = [
            condition: Condition
    ]

    static hasMany = [
            targetQualities: StimulusQuality
    ]

    static constraints = {
        identifier nullable: false,blank: false,unique: true
        stimulusOnsetAsynchrony nullable: false


//        presentationDevice nullable: true
//        presentationSoftware nullable: true
//        primeType nullable: true
//        primeModality nullable: true
////        targetQuality nullable: true
//        primeQuality nullable: true

        targetType nullable: false
        targetModality nullable: false

    }

    Condition condition

    Float interStimulusInterval
    Float stimulusOnsetAsynchrony
    Date createdAt
    Date updatedAt

//    String targetStimulusType
    StimulusType targetType
//    String targetStimulusModality
    StimulusModality targetModality
    Float targetStimulusDuration
//    String targetStimulusQuality
//    StimulusQuality targetQuality


//    String primeStimulusType
    StimulusType primeType
//    String primeStimulusModality
    StimulusModality primeModality
    String primeStimulusDuration
//    String primeStimulusQuality
    StimulusQuality primeQuality

//    String stimulusPresentationDevice
    StimulusPresentationDevice presentationDevice
//    String stimulusPresentationSoftware
    Software presentationSoftware

    String identifier
}
