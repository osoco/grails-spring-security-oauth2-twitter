package grails.plugin.springsecurity.oauth2.twitter

import com.github.scribejava.apis.TwitterApi
import com.github.scribejava.core.builder.api.DefaultApi10a
import com.github.scribejava.core.model.Token
import grails.converters.JSON
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugin.springsecurity.oauth2.service.OAuth2AbstractProviderService
import grails.plugin.springsecurity.oauth2.token.OAuth2SpringToken
import grails.transaction.Transactional
import groovy.util.logging.Slf4j

@Slf4j
@Transactional
class TwitterOAuth2Service extends OAuth2AbstractProviderService {

    @Override
    String getProviderID() {
        return 'twitter'
    }

    @Override
    Class<? extends DefaultApi10a> getApiClass() {
        TwitterApi.class
    }

    @Override
    String getProfileScope() {
        return 'https://api.twitter.com/1.1/account/verify_credentials.json?include_email=true'
    }


    @Override
    String getScopes() {
        return ''
    }

    @Override
    String getScopeSeparator() {
        return ' '
    }

    @Override
    OAuth2SpringToken createSpringAuthToken(Token accessToken) {
        def user
        def response = getResponse(accessToken)
        try {
            log.debug("JSON response body: " + accessToken.rawResponse)
            user = JSON.parse(response.body)
        } catch (Exception exception) {
            log.error("Error parsing response from " + getProviderID() + ". Response:\n" + response.body)
            throw new OAuth2Exception("Error parsing response from " + getProviderID(), exception)
        }
        if (!user?.email) {
            log.error("No user email from " + getProviderID() + ". Response was:\n" + response.body)
            throw new OAuth2Exception("No user id from " + getProviderID())
        }
        new TwitterOauth2SpringToken(accessToken, user?.email, providerID)
    }

    @Override
    String getAuthUrl(Map<String, String> params) {
        log.debug "service's callback is: ${authService.getCallback()}"
        def requestToken = authService.getRequestToken()
        log.debug "Request token: ${requestToken}"
        authService.getAuthorizationUrl(requestToken)
    }

}