import grails.util.Environment

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.gorm.default.constraints = {
    '*'(nullable: true)
}

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html: ['text/html', 'application/xhtml+xml'],
        xml: ['text/xml', 'application/xml'],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        all: '*/*',
        json: ['application/json', 'text/json'],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
// grails.enable.native2ascii = true
grails.enable.native2ascii = false
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// set per-environment serverURL stem for creating absolute links
grails.nemo.data = "./application-data"

environments {
    development {
        grails.logging.jul.usebridge = true
        // grails.serverURL = "http://localhost:8080/nemo"
//        grails.serverURL = "https://cas-vm-nemo.uoregon.edu/nemo"
//        grails.serverURL = "http://cas-vm-nemo.uoregon.edu/nemo"

//        uiperformance.enabled = false
    }
    staging {
        grails.logging.jul.usebridge = false
        grails.nemo.data = "/var/data"
        grails.serverURL = "https://cas-vm-nemo.uoregon.edu/nemo"
    }
    production {
        grails.logging.jul.usebridge = false
// not enough room on "/" so using "./application-data "
        // grails.nemo.data = "/storage/users/data"
        grails.nemo.data = "/pgsql/data"
        grails.serverURL = "https://portal.nemo.nic.uoregon.edu"
    }
}

String logDirectory = "${System.getProperty('catalina.base') ?: '.'}/logs"

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //

    appenders {
        def shortPatternLayout = new org.apache.log4j.PatternLayout()
        shortPatternLayout.setConversionPattern("%d %c{3} [%m]%n")
        console name: 'stdout', layout: shortPatternLayout
//        console name: "stdout", layout: pattern(conversionPattern: "%d{yyyy-MMM-dd HH:mm:ss,SSS} [%t] %c %x%n %-5p %m%n")
        rollingFile name: 'file', file: "${logDirectory}/nemo-output.log", maxFileSize: 2048
        if (grails.util.Environment.current == grails.util.Environment.PRODUCTION
                ||
                grails.util.Environment.current.name == "staging"
        ) {
            def mailAppender = new org.apache.log4j.net.SMTPAppender()
            mailAppender.setFrom("ndunn@cas.uoregon.edu")
            mailAppender.setTo("ndunn@cas.uoregon.edu")
            mailAppender.setSubject("NEMO - An log4j error has been generated in the ${Environment.current.name} environment")
            mailAppender.setSMTPHost("smtp.uoregon.edu")
            // using long as should only be executed in the case of an error
            mailAppender.setLayout(shortPatternLayout)
            appender name: 'mail', mailAppender
        }
    }

    root {
        warn 'stdout', 'file', 'mail'
    }

//    appenders {
//        console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
//    }

    error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

//    trace 'org.hibernate.type'
//    debug 'org.hibernate.SQL'

    info 'grails.app'
//    debug 'grails.app.jobs'
//    debug 'grails.app.taglib'
//    debug 'grails.app.taglib.edu.uoregon.nic.nemo.portal'
//    debug 'grails.app.controllers'
    debug 'grails.app.services'
//    debug 'grails.app.services.edu.uoregon.nic.nemo.portal.OntologyService'
//    debug 'grails.app.services.edu.uoregon.nic.nemo.portal.DataStubService'
//    debug 'grails.app.services.edu.uoregon.nic.nemo.portal.UserService'
//    debug 'grails.app.controllers.edu.uoregon.nic.nemo.portal'
//    debug 'grails.app.controllers.edu.uoregon.nic.nemo.portal.TermController'
}


grails {
    mail {
//        host = "smtp.gmail.com"
        host = "smtp.uoregon.edu"
//        port = 465
//        username = "ndunn@uoregon.edu"
//        password = "yourpassword"
        props = [
                "mail.smtp.auth": "false"
//                "mail.smtp.socketFactory.port":"465",
//                "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                // "mail.smtp.socketFactory.fallback":"false"
        ]

    }
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'edu.uoregon.nic.nemo.portal.SecUser'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'edu.uoregon.nic.nemo.portal.SecUserRole'
grails.plugins.springsecurity.authority.className = 'edu.uoregon.nic.nemo.portal.Role'
grails.plugins.springsecurity.roleHierarchy = '''
    ROLE_ADMIN > ROLE_VERIFIED
    ROLE_VERIFIED > ROLE_UNVERIFIED
'''
grails.plugins.springsecurity.ui.password.minLength = 8
grails.plugins.springsecurity.ui.password.maxLength = 64
//grails.plugins.springsecurity.ui.password.validationRegex='^.*(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&]).*$'
grails.plugins.springsecurity.ui.password.validationRegex = '^.*(?=.*\\d)(?=.*[a-zA-Z]).*$'

grails.plugins.springsecurity.ui.register.defaultRoleNames = ['ROLE_VERIFIED']

// this is handled on the domain!!! . . .not needed here
grails.plugins.springsecurity.ui.encodePassword = false

grails.resources.adhoc.excludes = ['**/gwt/**']



