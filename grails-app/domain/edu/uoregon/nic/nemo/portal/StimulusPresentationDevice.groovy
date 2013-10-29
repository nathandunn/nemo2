package edu.uoregon.nic.nemo.portal

class StimulusPresentationDevice extends Ontological{

    static hasMany = [
            stimuli: Stimulus
    ]

    static belongsTo = [
            Stimulus
    ]

}
