package com.healthatlas.core.bloodtests.adapters.inbound.rest;

import com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.request.BloodTestDto;
import com.healthatlas.core.bloodtests.ports.inbound.BloodTestUseCase;
import com.healthatlas.core.common.security.JwtUserUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.UUID;

@Path("api/v1/blood-tests")
public class BloodTestResource {

    @Inject
    BloodTestUseCase bloodTestUseCase;

    @Inject
    JwtUserUtil jwtUserUtil;

    @GET
    @Path("mine")
    @RolesAllowed({"USER", "DOCTOR", "ADMIN"})
    public Response getMine(@Context SecurityContext ctx) {
        UUID userId = jwtUserUtil.getCurrentUserUuid(ctx);
        var response = bloodTestUseCase.getBloodTestsPerUser(userId);
        return Response.status(Response.Status.OK)
                .entity(response)
                .build();
    }

    @POST
    @RolesAllowed("USER")
    public Response create(@Context SecurityContext ctx, @Valid BloodTestDto bloodTestDto) {
        UUID userId = jwtUserUtil.getCurrentUserUuid(ctx);
        var bloodTest = bloodTestUseCase.createBloodTest(userId, bloodTestDto);
        return Response.status(Response.Status.CREATED)
                .entity(bloodTest)
                .build();
    }
}
