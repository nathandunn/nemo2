package edu.uoregon.nic.nemo.portal

class GroundElectrode extends Ontological{

    static hasMany = [
            eegDataCollections: EegDataCollection
    ]

    static belongsTo = [
            EegDataCollection
    ]
}
