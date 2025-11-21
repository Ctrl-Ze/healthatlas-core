package com.healthatlas.core.bloodtests.exception;

import com.healthatlas.core.bloodtests.config.TraceIdFilter;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Context
    ContainerRequestContext requestContext;

    @Override
    public Response toResponse(NotFoundException e) {
        String traceId = (String) requestContext.getProperty(TraceIdFilter.TRACE_ID);
        return Response.status(Response.Status.NOT_FOUND)
                .entity(ErrorResponse.of(e, Response.Status.NOT_FOUND.getStatusCode(),traceId))
                .build();
    }
}
