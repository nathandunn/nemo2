package edu.uoregon.nic.nemo.portal

class OfflineReference extends Ontological {

    static hasMany = [
            erpDataPreprocessings: ErpDataPreprocessing
    ]

    static belongsTo = [
            ErpDataPreprocessing
    ]

}
