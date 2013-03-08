package myApp.service;

import myApp.model.user.Role;
import myApp.model.user.User;
import myApp.util.AppLog;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: eluibon
 * Date: 11/12/12
 * Time: 14.06
 */
@Stateless
public class UserService implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private static final long serialVersionUID = 2332677310929733841L;

    @Inject private EntityManager em;

    @Inject @AppLog
    private Logger log ;

// -------------------------- OTHER METHODS --------------------------

    public List<User> findAll() {

        // if not permitted to list superadmin users add superadmin = false
        //if ( ! loggedSubject.isPermitted("user:list_superadmin") )
        //    bb.and(user.superadmin.isFalse()) ;

        return em.createQuery("select u from User u", User.class).getResultList() ;
    }

    public List<Role> findAllRoles() {

        return em.createQuery("select r from Role r", Role.class).getResultList() ;
    }

    public User findByUsername(String username) {

        return em.createQuery("select u from User u where u.username = :username", User.class).
                setParameter("username",username).getSingleResult() ;
    }

    public User findByEmail(String email) {

        return em.createQuery("select u from User u where u.email = :email", User.class).
                setParameter("email",email).getSingleResult() ;
    }

    public AuthorizationInfo getAuthorizationInfoForUser(String username) {

        User user = findByUsername(username) ;
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(user.getRoleNames());
        info.setStringPermissions(user.getPermissionNames());
        log.debug("Found Roles : [{}], Permissions : [{}]",user.getRoleNames(),user.getPermissionNames());
        return info ;
    }

    public User trackSuccessfulLoginAttempt(String username) {

        User user = findByUsername(username) ;
        user.trackSuccessfulLoginAttempt();
        log.debug("Successful login attempt by username : {}");
        return user ;
    }

    public int trackFailedLoginAttempt(String username) {

        User user = findByUsername(username) ;
        int count = user.trackFailedLoginAttempt();
        log.debug("Failed login attempt by username : {}, current attempt count : {}", username, user.getFailedLoginAttemptCount());
        return count ;
    }

    public void save(User user) throws UserConstraintException {

        Set<UserConstraintException.Violations> cv = new HashSet<UserConstraintException.Violations>() ;

        if ( ! checkUsernameUniqueness(user) )
            cv.add(UserConstraintException.Violations.USERNAME_NOT_UNIQUE) ;

        if ( ! checkEmailUniqueness(user) )
            cv.add(UserConstraintException.Violations.EMAIL_NOT_UNIQUE) ;

        if (cv.size() > 0)
            throw new UserConstraintException(cv) ;

        try {
            em.merge(user);
        }
        catch (RuntimeException rte) {

            cv.add(UserConstraintException.Violations.UNKNOWN_ERROR) ;
            throw new UserConstraintException(cv) ;
        }
    }

    private boolean checkUsernameUniqueness(User user) {

        User u = findByUsername(user.getUsername()) ;
        return  ( u == null ) || u.getId().equals(user.getId()) ;
    }

    private boolean checkEmailUniqueness(User user) {

        User u = findByEmail(user.getEmail()) ;
        return  ( u == null ) || u.getId().equals(user.getId()) ;
    }

    public void delete(User user) {

        em.remove(user);
    }
}
