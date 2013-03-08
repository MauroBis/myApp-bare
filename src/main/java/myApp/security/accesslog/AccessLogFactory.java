package myApp.security.accesslog;

import javax.enterprise.inject.Produces;

/**
 * Created with IntelliJ IDEA.
 * User: eluibon
 * Date: 19/02/13
 * Time: 17.22
 */
public class AccessLogFactory {

    @Produces
    @AppAccessLog
    public AccessLog getAccessLog() {
        return new LoggerAccessLog() ;
    }
}
