package edu.uoregon.nic.nemo.portal

class StimulusQuality extends Ontological{

    static hasMany = [
            stimuli: Stimulus
    ]

    static mappedBy = [
            stimuli: "targetQuality"
            , stimuli: "primeQuality"
    ]

    static belongsTo = [
            Stimulus
    ]
}
