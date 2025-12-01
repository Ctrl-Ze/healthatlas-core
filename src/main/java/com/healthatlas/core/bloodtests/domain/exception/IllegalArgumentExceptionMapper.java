package com.healthatlas.core.bloodtests.domain.exception;

import com.healthatlas.core.bloodtests.config.TraceIdFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    @Context
    ContainerRequestContext requestContext;

    @Override
    public Response toResponse(IllegalArgumentException e) {
        String traceId = (String) requestContext.getProperty(TraceIdFilter.TRACE_ID);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponse.of(e, Response.Status.BAD_REQUEST.getStatusCode(),traceId))
                .build();
    }
}
