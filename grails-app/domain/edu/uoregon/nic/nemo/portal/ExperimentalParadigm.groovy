package edu.uoregon.nic.nemo.portal

class ExperimentalParadigm extends Ontological {

    static hasMany = [
            experiments: Experiment
    ]

    static belongsTo = [
            Experiment
    ]
}
