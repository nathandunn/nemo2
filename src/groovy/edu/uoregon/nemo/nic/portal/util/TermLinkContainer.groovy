package edu.uoregon.nemo.nic.portal.util

/**
 * Created with IntelliJ IDEA.
 * User: NathanDunn
 * Date: 10/8/12
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
class TermLinkContainer implements Comparable<TermLinkContainer>{

    String label
    String url

    void setUrl(String newUrl){
        if(newUrl.contains("#")){
            newUrl = newUrl.split("#")[1]
        }
        url = newUrl
    }

    @Override
    public String toString() {
        return "TermLinkContainer{" +
                "label='" + label + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    int compareTo(TermLinkContainer t) {
        if(label && t.label){
            return label.compareTo(t.label)
        }
        if(label) return 1
        if(t.label) return -1
        return url.compareTo(t.url)
    }
}
