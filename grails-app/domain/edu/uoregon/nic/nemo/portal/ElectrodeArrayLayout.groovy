package edu.uoregon.nic.nemo.portal

class ElectrodeArrayLayout extends Ontological{

    static hasMany = [
            eegDataCollections: EegDataCollection
    ]

    static belongsTo = [
            EegDataCollection
    ]
}
