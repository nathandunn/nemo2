modules = {
    application {
        resource url: 'js/application.js'
    }

    core {
        dependsOn 'jquery-ui'
    }
    overrides {
        'jquery-theme' {
            resource id: 'theme', url: '/css/custom-theme/jquery-ui-1.8.24.custom.css'
        }
    }
}