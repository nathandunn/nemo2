package edu.uoregon.nic.nemo.portal

class UserService {

    boolean isAdmin(SecUser secUser) {
        if(!secUser) return false
        Set<String> roles = SecUserRole.findAllBySecUser(secUser).collect { it?.role?.authority } as Set
        for (r in roles){
            if(r.equals("ROLE_ADMIN")) return true
        }
        return false
//        (SecUserSecRole.findAllBySecUser(this).collect { it.secRole.authority } as Set)?.contains(SecRole.ROLE_ADMIN)
    }


    def getUser(SecUser secUser) {
        return SecUser.findByUsername(secUser.username)
    }

    def isAdminOrCurrent(SecUser testUser,SecUser currentUser) {
        List<SecUser> secUsers = []
        secUsers.add(testUser)
        return isAdminOrCurrent(secUsers,currentUser)
    }

    def isAdminOrCurrent(Collection<SecUser> testUsers,SecUser currentUser) {
        if (!currentUser){
            return false
        }
        if (isAdmin(currentUser)) {
            return true
        }

        log.debug "testUsers ${testUsers} currentUser ${currentUser}"
        log.debug "current user in testUsers ${currentUser in testUsers}"

        if(testUsers){
            for(tu in testUsers){
                if (currentUser.id == tu.id){
                    return true
                }
            }
        }

        return false ;
    }

}
