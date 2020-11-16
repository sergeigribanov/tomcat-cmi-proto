package com.cmi.security;
 
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
 
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
 
import java.util.Base64;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.mindrot.jbcrypt.BCrypt;

import com.cmi.database.DataBaseException;
 
/**
 * This filter verify the access permissions for a user
 * based on username and passowrd provided in request
 * */
@Provider
public class AuthentificationFilter implements javax.ws.rs.container.ContainerRequestFilter {
    
    @Context
    private ResourceInfo resourceInfo;
     
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
      
    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        //Access allowed for all
        if(method.isAnnotationPresent(PermitAll.class)) {
	    return;
	}
	//Access denied for all
	if(method.isAnnotationPresent(DenyAll.class)) {
	    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
				     .entity("Access blocked for all users !!").build());
	    return;
	}
              
	//Get request headers
	final MultivaluedMap<String, String> headers = requestContext.getHeaders();
              
	//Fetch authorization header
	final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
              
	//If no authorization information present; block access
	if(authorization == null || authorization.isEmpty()) {
	    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				     .entity("You cannot access this resource").build());
	    return;
	}
              
	//Get encoded username and password
	final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
              
	//Decode username and password
	String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));;
  
	//Split username and password tokens
	final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
	final String username = tokenizer.nextToken();
	final String password = tokenizer.nextToken();
              
	//Verify user access
	if(!method.isAnnotationPresent(RolesAllowed.class)) {
	    return;
	}
	RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
	Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
	
	//Is user valid?
	try {
	    if( ! isUserAllowed(username, password, rolesSet)) {
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
					 .entity("You cannot access this resource").build());
		return;
	    }
	} catch (DataBaseException e) {
	    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				     .entity("You cannot access this resource").build());
	} catch (SQLException e) {
	    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				     .entity("You cannot access this resource").build());
	    return;
	}
    }
    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) throws SQLException, DataBaseException {
	String dbUserRole = null;
	String dbHash = null;
	String sql = "SELECT userrole, hash FROM users WHERE username = ?";
	Connection conn = DriverManager.getConnection("jdbc:postgresql://172.16.238.11/cmi", "postgres", "1234");
	PreparedStatement pstmt  = conn.prepareStatement(sql);
	pstmt.setString(1, username);
	ResultSet rs  = pstmt.executeQuery();
	while (rs.next()) {
	    dbUserRole = rs.getString("userrole");
	    dbHash = rs.getString("hash");
	}
	if (dbHash == null || dbUserRole == null) {
	    throw new DataBaseException("Can not find user / userrole / hash!");
	}
	conn.close();
	return BCrypt.checkpw(password, dbHash) && rolesSet.contains(dbUserRole);
    }
}
