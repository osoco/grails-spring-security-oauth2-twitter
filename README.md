Spring Security OAuth2 Twitter Plugin
====================================

Add a Twitter OAuth2 provider to the [Spring Security OAuth2 Plugin](https://github.com/grails-plugins/grails-spring-security-oauth2)
for Grails 3.0 to 3.2 applications.

Installation
------------
Add the following dependencies in `build.gradle`
```
dependencies {
...
    compile 'org.grails.plugins:spring-security-oauth2:1.+'
    compile 'org.grails.plugins:spring-security-oauth2-twitter:1.0.+'
...
}
```

Usage
-----
Add this to your application.yml
```
grails:
    plugin:
        springsecurity:
            oauth2:
                providers:
                    twitter:
                        api_key: 'twitter-api-key'               #needed
                        api_secret: 'twitter-api-secret'         #needed
                        successUri: "/oauth2/twitter/success"    #optional
                        failureUri: "/oauth2/twitter/failure"    #optional
                        callback: "/oauth2/twitter/callback"     #optional
                        scopes: "some_scope"                     #optional, see https://developers.google.com/identity/protocols/googlescopes#monitoringv3
```
You can replace the URIs with your own controller implementation.

In your view you can use the taglib exposed from this plugin and from OAuth plugin to create links and to know if the user is authenticated with a given provider:
```xml
<oauth2:connect provider="twitter">Twitter</oauth2:connect>

Logged with Twitter?
<oauth2:ifLoggedInWith provider="twitter">yes</oauth2:ifLoggedInWith>
<oauth2:ifNotLoggedInWith provider="twitter">no</oauth2:ifNotLoggedInWith>
```

License
-------
Apache 2