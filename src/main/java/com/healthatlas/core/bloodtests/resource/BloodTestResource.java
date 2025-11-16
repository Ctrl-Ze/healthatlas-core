package com.healthatlas.core.bloodtests.resource;

import com.healthatlas.core.bloodtests.service.BloodTestService;
import com.healthatlas.core.bloodtests.dto.request.BloodTestDto;
import com.healthatlas.core.bloodtests.dto.response.BloodTestResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("api/v1/blood-tests")
public class BloodTestResource {

    @Inject
    BloodTestService bloodTestService;

    @GET
    @Path("mine")
    @RolesAllowed({"user", "doctor", "admin"})
    public List<BloodTestResponseDto> getMine(@Context SecurityContext ctx) {
        return bloodTestService.getBloodTestsPerUser(ctx);
    }

    @POST
    @RolesAllowed("user")
    public BloodTestResponseDto create(@Context SecurityContext ctx, BloodTestDto bloodTestDto) {
        return bloodTestService.createBloodTest(ctx, bloodTestDto);
    }
}
