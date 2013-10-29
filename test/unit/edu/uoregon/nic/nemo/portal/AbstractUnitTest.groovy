package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.StubSpringSecurityService
import org.apache.commons.lang.RandomStringUtils

/**
 */
abstract class AbstractUnitTest implements GroovyObject {

    SecUser injectSecurityUser(def securityServiceTarget) {
        SecUser secUser = new SecUser(
                username: getRandomEmail()
                , password: "secret"
                , level: "Commercial"
                , firstname: "George"
                , lastname: "TheMonkey"
        )
        securityServiceTarget?.springSecurityService = new StubSpringSecurityService()
        securityServiceTarget?.springSecurityService?.setCurrentUser(secUser)
        return secUser
    }

    SecUser makeUserAdmin(SecUser secUser) {
        Role role = new Role(authority: "ROLE_ADMIN").save()
        SecUserRole secUserRole = new SecUserRole(
                secUser: secUser
                , role: role
        )
                .save(failOnError: true, flush: true)
        return secUser
    }

    SecUser injectSecurityUserAsAdmin(def securityServiceTarget) {
        SecUser secUser = injectSecurityUser(securityServiceTarget)
        return makeUserAdmin(secUser)
    }

    String getRandomString(params) {
        def length = params?.length ?: 5
        String randomString = org.apache.commons.lang.RandomStringUtils.random(length, true, true)
        return randomString
    }

    String getRandomEmail(params) {
        return "${getRandomString(params)}@${getRandomString(params)}.${getRandomString(params)}"
    }

    String getRandomUrl(params) {
        return "http://${getRandomString(params)}.edu"
    }

    String getRandomNumbers(Integer length){
       return RandomStringUtils.random(length,false,true)
    }

    String getRandomNemoURL() {
        return OntologyService.NS_BASE +"#"+getRandomNumbers(8)
    }
}
