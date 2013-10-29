package edu.uoregon.nic.nemo.portal

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

/**
 * Created with IntelliJ IDEA.
 * User: ndunn
 * Date: 2/5/13
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
class RegisterCommand {

    String username
//        String email
    String password
    String password2


    String fullName
    String laboratoryId


    def grailsApplication

    static constraints = {
        username email: true, blank: false, nullable: false, validator: { value, command ->
            if (value) {
                def User = command.grailsApplication.getDomainClass(
                        SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
                if (User.findByUsername(value)) {
                    return 'registerCommand.username.unique'
                }
            }
        }
        fullName blank: false, nullable: false, email: false
//            email blank: false, nullable: false, email: true
        password blank: false, nullable: false, validator: RegisterController.passwordValidator
        password2 validator: RegisterController.password2Validator
        laboratoryId nullable: true
    }
}
