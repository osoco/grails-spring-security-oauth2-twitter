package grails.plugin.springsecurity.oauth2.twitter

import com.github.scribejava.core.model.Token
import grails.plugin.springsecurity.oauth2.token.OAuth2SpringToken

class TwitterOauth2SpringToken extends OAuth2SpringToken {

    private String email
    private String providerId

    TwitterOauth2SpringToken(Token accessToken, String email, String providerId) {
        super(accessToken)
        this.email = email
        this.providerId = providerId
    }

    @Override
    String getProviderName() {
        return providerId
    }

    @Override
    String getSocialId() {
        return email
    }

    @Override
    String getScreenName() {
        return email
    }
}