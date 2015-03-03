package com.haduart.resources;

import com.google.common.base.Optional;
import com.kjetland.dropwizard.activemq.ActiveMQSender;
import com.haduart.core.Profile;
import com.haduart.core.View;
import com.haduart.db.ProfileDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/profile/{profileId}")
@Produces(MediaType.APPLICATION_JSON)
public class UserProfileResource {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ProfileDAO peopleDAO;
    private final ActiveMQSender sender;

    public UserProfileResource(ProfileDAO peopleDAO, ActiveMQSender sender) {
        this.peopleDAO = peopleDAO;
        this.sender = sender;
    }

    /*
     One way of doing it: /profile/1?userId=6
      */
    @GET
    @UnitOfWork
    public Profile getProfile(@PathParam("profileId") LongParam profileId, @QueryParam("userId") LongParam userId) {
        dispatchMessage(profileId, userId);
        return findSafely(profileId.get());
    }

    private void dispatchMessage(LongParam profileId, LongParam userId) {
        try {
            sender.send(new View(profileId.get(), userId.get(), new Date()));
        } catch (Exception e) {
            log.error("ActiveMQ is Down or is not set up correctly", e);
        }
    }

    private Profile findSafely(long userId) {
        final Optional<Profile> profile = peopleDAO.findById(userId);
        if (!profile.isPresent()) {
            throw new NotFoundException("No profile");
        }
        return profile.get();
    }
}
