package edu.uoregon.nic.nemo.portal

class TextManglerTagLib {

    def splitWordString(String input, int length) {
        if(!input){
            return ""
        }
        int index = input.indexOf(" ",length)
        if (index > 0 && index < 2*length){
            return input.substring(0,index)
        }
        else{
            return input.substring(0,length)
        }
    }

    def toggleTextLength = { attrs, body ->
        def input = attrs.input
        if(!input){
            out << ""
            return
        }
        def maxLength = (attrs.maxLength ?: Integer.MAX_VALUE) as int
        if(maxLength>input.size()){
            out << input
            return
        }

        def randomId = Math.round(Math.random() * 10000)

        out << '<span id="notesS_' + randomId + '">'
        out << splitWordString(input,maxLength)
        out << '&nbsp;'
//        out << '&nbsp;'
        out << '<a href="javascript:toggleVersion(\'' + randomId + '\',true)"  title="Show full text">'
//        out << '&nbsp;... '
        out << '...'
        out << "<img src='" << resource(dir: '/images', file: 'right_arrow.gif') << "'>"
        out << '</a>'
        out << '</span>'
        out << '<span style="display:none;" id="notesL_' + randomId + '">'
        out << input
        out << '&nbsp;'
        out << "\n"
        out << '<a href="javascript:toggleVersion(\'' + randomId + '\',false)"  title="Collapse">'
        out << "<img src='" << resource(dir: '/images', file: 'left_arrow.gif') << "'>"
        out << '</a>'
        out << '</span>'
        out << "\n"

        out << "\n"
        out << '<script type="text/javascript">'
        out << "\n"
        out << ' function toggleVersion(index, isLong) {'
        out << "\n"
        out << '            if (isLong) {'
        out << "\n"
        out << '                document.getElementById("notesS_" + index).style.display = "none";'
        out << "\n"
        out << '                document.getElementById("notesL_" + index).style.display = "inline";'
        out << "\n"
        out << '            }'
        out << "\n"
        out << '            else {'
        out << "\n"
        out << '                document.getElementById("notesS_" + index).style.display = "inline";'
        out << "\n"
        out << '                document.getElementById("notesL_" + index).style.display = "none";'
        out << "\n"
        out << '            }'
        out << "\n"
        out << '        }'
        out << "\n"
        out << '        </script>'
        out << "\n"


    }
}
