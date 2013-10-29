package edu.uoregon.nic.nemo.portal

class ErpEvent extends Ontological {


    static hasMany = [
            erpDataPreprocessings: ErpDataPreprocessing
    ]

    static belongsTo = [
            ErpDataPreprocessing
    ]
}
