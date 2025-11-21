package com.healthatlas.core.bloodtests.exception;

import com.healthatlas.core.bloodtests.config.TraceIdFilter;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    ContainerRequestContext requestContext;

    @Override
    public Response toResponse(ConstraintViolationException e) {
        String traceId = (String) requestContext.getProperty(TraceIdFilter.TRACE_ID);
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(ErrorResponse.of(e, Response.Status.BAD_REQUEST.getStatusCode(), traceId))
                .build();
    }
}