package edu.uoregon.nic.nemo.portal

class UserTagLib {

    def springSecurityService
    def userService

    def editable = { attrs, body ->
        def currentUser = springSecurityService.currentUser
        log.debug "has a current user ${currentUser}"
        log.debug "attrs: ${attrs}"
        if(!currentUser) return

        if(userService.isAdmin(currentUser)){
            log.debug "is admin ${currentUser}"
            out << body()
            return
        }

        def users = attrs.users

        log.debug "users: ${users}"

        if(users instanceof SecUser){
            if (currentUser == users){
                out << body()
            }
            return
        }


        def user = attrs.user ?: currentUser

        log.debug "A user! user ${attrs.user}"
        log.debug "A user2! user ${user}"

        if(users){
            log.debug "users - ${users}"
            for(u in users){
                log.debug "U ${u?.id} vs ${user.id}"
                if(u.id == user.id){
                    out << body()
                    return
                }
            }
        }

    }
}
