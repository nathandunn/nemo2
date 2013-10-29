package edu.uoregon.nemo.nic.portal.util

/**
 */
class StubSpringSecurityService {
    def currentUser

    def loggedInFlag

    Object getCurrentUser() {
        return currentUser
    }

    void setCurrentUser(Object newCurrentUser){
        currentUser = newCurrentUser
    }

    String encodePassword(String password, salt = null) {
        return password
    }

    Boolean isLoggedIn(){
        return loggedInFlag
    }
}
