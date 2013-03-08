package myApp.security;

import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.AuthenticatingSecurityManager;
import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.WebEnvironment;

import javax.inject.Inject;
import javax.servlet.ServletContext;

/**
 * User: eluibon
 * Date: 11/12/12
 * Time: 19.29
 * CDI Listener to inject a JPA Realm into Shiro for authentication via JPA instead of JDBC
 * basically it replaces the whole [main] part of shiro.ini
 */
public class CdiEnvironmentLoaderListener extends EnvironmentLoaderListener {

    @Inject JpaRealm jpaRealm;

    @Override
    protected WebEnvironment createEnvironment(ServletContext sc) {

        WebEnvironment environment = super.createEnvironment(sc);

        AuthenticatingSecurityManager asm = (AuthenticatingSecurityManager) environment.getSecurityManager();

        // set memory cache
        asm.setCacheManager(new MemoryConstrainedCacheManager());

        // set my custom authenticator
        asm.setAuthenticator(new CustomAuthenticator());

        jpaRealm.setCredentialsMatcher(new PasswordMatcher());

        asm.setRealm(jpaRealm);
        ((DefaultWebEnvironment) environment).setSecurityManager(asm);

        return environment;
    }
}
