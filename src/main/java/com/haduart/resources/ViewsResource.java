package com.haduart.resources;

import com.haduart.db.ViewJdbiDAO;
import com.haduart.dto.ViewDTO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/profile/{profileId}/Views")
@Produces(MediaType.APPLICATION_JSON)
public class ViewsResource {

    private final ViewJdbiDAO jdbiDAO;

    public ViewsResource(ViewJdbiDAO jdbiDAO) {
        this.jdbiDAO = jdbiDAO;
    }

    // /profile/1/views
    //  [uid: 6, ts: 20141012T10:00:00Z]
    @GET
    @UnitOfWork
    public List<ViewDTO> getViews(@PathParam("profileId") LongParam profileId) {
        System.out.println("GET VIEWS for profileId:" + profileId);
        DateTime today = new DateTime();
        return jdbiDAO.findViewsByProfile(profileId.get(), today, today.minusDays(5));
    }
}
