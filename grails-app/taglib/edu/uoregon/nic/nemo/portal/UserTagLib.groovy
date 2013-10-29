package edu.uoregon.nic.nemo.portal

class UserTagLib {

    def springSecurityService
    def userService

    def editable = { attrs, body ->
        def currentUser = springSecurityService.currentUser
        log.debug "has a current user ${currentUser}"
        if(!currentUser) return

        if(userService.isAdmin(currentUser)){
            log.debug "is admin ${currentUser}"
            out << body()
            return
        }

        def users = attrs.users

        if(users instanceof SecUser){
            if (currentUser == users){
                out << body()
            }
            return
        }


        def user = attrs.user ?: currentUser

        log.debug "user ${currentUser}"

        if(users){
            println "users - ${users}"
            for(u in users){
                println "u ${u}"
                if(u.id == user.id){
                    out << body()
                    return
                }
            }
        }

    }
}
