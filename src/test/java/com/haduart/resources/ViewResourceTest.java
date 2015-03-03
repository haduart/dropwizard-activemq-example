package com.haduart.resources;

import com.haduart.core.View;
import com.haduart.db.ViewJdbiDAO;
import com.haduart.dto.ViewDTO;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ViewsResource}.
 */
public class ViewResourceTest {

    private static final ViewJdbiDAO JDBI_DAO = mock(ViewJdbiDAO.class);
    private static final ViewsResource viewsResource = new ViewsResource(JDBI_DAO);
    private List views;

    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addResource(viewsResource)
            .build();

    @Before
    public void setup() {
        List views = new ArrayList<ViewDTO>();
        views.add(generateViewDTO());
    }

    private ViewDTO generateViewDTO() {
        long userId = 5;
        return new View(userId, new Date());
    }

    @After
    public void tearDown() {
        reset(JDBI_DAO);
    }

    @Test
    public void getViewsForAProfile() {
        RULE.client().target("/profile/1/Views").request().get(List.class);

        verify(JDBI_DAO).findViewsByProfile(anyLong(), Matchers.<DateTime>anyObject(), Matchers.<DateTime>anyObject());
    }


//    @Test
//    public void getPersonNotFound() {
//        when(DAO.findById(2L)).thenReturn(Optional.<Profile>absent());
//        final Response response = RULE.client().target("/profile/2?userId=6").request().get();
//
//        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
//        verify(DAO).findById(2L);
//    }
}
