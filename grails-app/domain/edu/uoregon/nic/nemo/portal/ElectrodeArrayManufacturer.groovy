package edu.uoregon.nic.nemo.portal

class ElectrodeArrayManufacturer extends Ontological{

    static hasMany = [
            eegDataCollections: EegDataCollection
    ]

    static belongsTo = [
            EegDataCollection
    ]

}
