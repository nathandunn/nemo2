package edu.uoregon.nic.nemo.portal

class StimulusModality extends Ontological{

    static hasMany = [
            stimuli: Stimulus
    ]

    static mappedBy = [
            stimuli: "targetModality"
            , stimuli: "primeModality"
    ]

    static belongsTo = [
            Stimulus
    ]

}
