package com.cmi.config;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * JAX-RS Provider for Jackson's {@link ObjectMapper}.
 */
@Provider
@Produces("application/json")
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private static final Logger log = LoggerFactory.getLogger(ObjectMapperContextResolver.class);

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        log.debug("init : {} (JAX-RS @Provider)", this.getClass().getSimpleName());

        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
