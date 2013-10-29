package edu.uoregon.nic.nemo.portal

class ReferenceElectrode extends Ontological{

    static hasMany = [
            eegDataCollections: EegDataCollection
    ]

    static belongsTo = [
            EegDataCollection
    ]
}
