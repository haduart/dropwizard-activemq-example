package com.haduart.resources;

import com.haduart.core.Profile;
import com.haduart.db.ProfileDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
public class ProfilesResource {

    private final ProfileDAO peopleDAO;

    public ProfilesResource(ProfileDAO peopleDAO) {
        this.peopleDAO = peopleDAO;
    }

    @POST
    @UnitOfWork
    public Profile createPerson(Profile profile) {
        return peopleDAO.create(profile);
    }

    @GET
    @UnitOfWork
    public List<Profile> listPeople(@QueryParam("user") Integer user) {
        return peopleDAO.findAll();
    }

}
