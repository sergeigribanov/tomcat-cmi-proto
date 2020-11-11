package com.cmi;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApplication extends Application {

    private static final Logger log = LoggerFactory.getLogger(RestApplication.class);

    @Override
    public Set<Class<?>> getClasses() {
        return Collections.emptySet();
    }

}
