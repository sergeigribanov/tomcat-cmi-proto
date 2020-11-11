package com.howtodoinjava.jersey;
 
import org.glassfish.jersey.server.ResourceConfig;
import com.cmi.security.AuthentificationFilter;
 
public class LoginApplication extends ResourceConfig 
{
    public LoginApplication() 
    {
        packages("com.cmi");
        //Register Auth Filter here
        register(AuthentificationFilter.class);
    }
}
