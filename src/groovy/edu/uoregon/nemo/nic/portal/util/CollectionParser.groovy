package edu.uoregon.nemo.nic.portal.util

/**
 * Created with IntelliJ IDEA.
 * User: ndunn
 * Date: 7/15/12
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
class CollectionParser {

    static final String pattern = /,{0,1}(.*?)=\[(.*?)\]/

    static Map<String, List<String>> parseStringAsMap(def inputString, Closure closure = null) {
        Map<String, Set<String>> map = new HashMap<String, HashSet<String>>()

        inputString = inputString.replaceAll("[ |{|}]","")
        def matcher = (inputString =~ pattern )

        int size = matcher.count
//        println "size: " + size
        for(int i = 0 ; i < size ; i++){
            def key = matcher[i][1]
            String[] values = matcher[i][2].split(",")
            if(closure){
                key = closure.call(key)
                for(int j = 0 ; j < values.size() ; j++){
//                    v = closure.call(v)
                    values[j] = closure.call(values[j])
                }
            }
            map.put(key,values)
//            println "key["+key+"]"
//            println "values["+values+"]"
        }
        return map
    }

    static def parseArrayString(String inputString, Closure closure = null) {
        def returned = []
        if (!inputString) return []
        inputString.splitEachLine(",") { words ->
            words.each {
//                println "processing: " + it
                if (closure) {
                    returned.add(closure.call(it))
                }
                else {
                    returned.add(it)
                }
            }
        }

        return returned
    }

    static List<String> parseStringAsArray(String inputString, Closure closure = null) {
        List<String> returned = []
        if (!inputString) return []
        inputString.splitEachLine(",") { words ->
            words.each {
//                println "processing: " + it
                if (closure) {
                    returned.add(closure.call(it))
                }
                else {
                    returned.add(it)
                }
            }
        }

        return returned
    }
}
