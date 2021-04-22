package grails.plugin.springsecurity.oauth2.twitter

import grails.plugin.springsecurity.ReflectionUtils
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.oauth2.SpringSecurityOauth2BaseService
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugins.Plugin
import org.slf4j.LoggerFactory

class SpringSecurityOauth2TwitterGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.10 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    List loadAfter = ['spring-security-oauth2']

    // TODO Fill in these fields
    def title = "Spring Security Oauth2 Twitter provider" // Headline display name of the plugin
    def author = "Rafael Luque (OSOCO)"
    def authorEmail = "rafael.luque@osoco.es"
    def description = '''\
This plugin provides the capability to authenticate via Twitter OAuth1 provider for Grails 3.0 to 3.2 applications.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://github.com/osoco/grails-spring-security-oauth2-twitter"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "OSOCO", url: "https://osoco.es/" ]

    // Any additional developers beyond the author specified above.
    def developers = [ [ name: "Rafael Luque", email: "rafael.luque@osoco.es" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    def log

    Closure doWithSpring() {      
        { ->
            ReflectionUtils.application = grailsApplication
            if (grailsApplication.warDeployed) {
                SpringSecurityUtils.resetSecurityConfig()
            }
            SpringSecurityUtils.application = grailsApplication

            // Check if there is an SpringSecurity configuration
            def coreConf = SpringSecurityUtils.securityConfig
            boolean printStatusMessages = (coreConf.printStatusMessages instanceof Boolean) ? coreConf.printStatusMessages : true
            if (!coreConf || !coreConf.active) {
                if (printStatusMessages) {
                    println("ERROR: There is no SpringSecurity configuration or SpringSecurity is disabled")
                    println("ERROR: Stopping configuration of SpringSecurity Oauth2")
                }
                return
            }

            if (!hasProperty('log')) {
                log = LoggerFactory.getLogger(SpringSecurityOauth2TwitterGrailsPlugin)
            }

            if (printStatusMessages) {
                println("Configuring Spring Security OAuth2 Twitter plugin...")
            }
            SpringSecurityUtils.loadSecondaryConfig('DefaultOAuth2TwitterConfig')
            if (printStatusMessages) {
                println("... finished configuring Spring Security OAuth2 Twitter\n")
            }
        }

    }

    void doWithApplicationContext() {
        log.trace("doWithApplicationContext")
        println("doWithApplicationContext")
        def SpringSecurityOauth2BaseService oAuth2BaseService = grailsApplication.mainContext.getBean('springSecurityOauth2BaseService') as SpringSecurityOauth2BaseService
        def TwitterOAuth2Service twitterOAuth2Service = grailsApplication.mainContext.getBean('twitterOAuth2Service') as TwitterOAuth2Service
        try {
            println "registering ${twitterOAuth2Service}..."
            oAuth2BaseService.registerProvider(twitterOAuth2Service)
        } catch (Exception exception) {
            println "There was an oAuth2Exception ${exception}"
            log.error("There was an oAuth2Exception", exception)
            log.error("OAuth2 Twitter not loaded")
        }
    }

}
