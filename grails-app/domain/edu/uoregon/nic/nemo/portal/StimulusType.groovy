package edu.uoregon.nic.nemo.portal

class StimulusType extends Ontological{

    static hasMany = [
            stimuli: Stimulus
    ]

    static mappedBy = [
            stimuli: "targetType"
            , stimuli: "primeType"
    ]

    static belongsTo = [
            Stimulus
    ]

}
