package com.healthatlas.core.bloodtests.resource;

import com.healthatlas.core.bloodtests.dto.request.BloodTestDto;
import com.healthatlas.core.bloodtests.service.BloodTestService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("api/v1/blood-tests")
public class BloodTestResource {

    @Inject
    BloodTestService bloodTestService;

    @GET
    @Path("mine")
    @RolesAllowed({"user", "doctor", "admin"})
    public Response getMine(@Context SecurityContext ctx) {
        var response = bloodTestService.getBloodTestsPerUser(ctx);
        return Response.status(Response.Status.OK)
                .entity(response)
                .build();
    }

    @POST
    @RolesAllowed("user")
    public Response create(@Context SecurityContext ctx, @Valid BloodTestDto bloodTestDto) {
        var bloodTest = bloodTestService.createBloodTest(ctx, bloodTestDto);
        return Response.status(Response.Status.CREATED)
                .entity(bloodTest)
                .build();
    }
}
