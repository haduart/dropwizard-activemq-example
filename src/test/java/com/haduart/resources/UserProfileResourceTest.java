package com.haduart.resources;

import com.google.common.base.Optional;
import com.kjetland.dropwizard.activemq.ActiveMQSender;
import com.haduart.core.Profile;
import com.haduart.core.View;
import com.haduart.db.ProfileDAO;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Matchers;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserProfileResource}.
 */
public class UserProfileResourceTest {
    private static final ProfileDAO DAO = mock(ProfileDAO.class);
    private static final ActiveMQSender ACTIVEMQ_SENDER = mock(ActiveMQSender.class);

    private static final UserProfileResource userProfileResource = new UserProfileResource(DAO, ACTIVEMQ_SENDER);

    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addResource(userProfileResource)
            .build();

    private Profile profile;

    @Before
    public void setup() {
        profile = new Profile();
        profile.setId(1L);
    }

    @After
    public void tearDown() {
        reset(DAO);
        reset(ACTIVEMQ_SENDER);
    }

    @Test
    public void getPersonSuccess() {
        when(DAO.findById(1L)).thenReturn(Optional.of(profile));

        Profile found = RULE.client().target("/profile/1?userId=6").request().get(Profile.class);

        assertThat(found.getId()).isEqualTo(profile.getId());
        verify(DAO).findById(1L);
    }

    @Test
    public void getProfileSendsActiveMQMessage() {
        when(DAO.findById(1L)).thenReturn(Optional.of(profile));

        RULE.client().target("/profile/1?userId=6").request().get(Profile.class);

        verify(ACTIVEMQ_SENDER).send(Matchers.<View>anyObject());
    }

    @Test
    public void getPersonNotFound() {
        when(DAO.findById(2L)).thenReturn(Optional.<Profile>absent());
        final Response response = RULE.client().target("/profile/2?userId=6").request().get();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
        verify(DAO).findById(2L);
    }

    @Test
    public void getProfileEvenIfActiveMQIsDown() {
        doThrow(new NullPointerException()).when(ACTIVEMQ_SENDER).send(Matchers.<View>anyObject());

        when(DAO.findById(1L)).thenReturn(Optional.of(profile));

        RULE.client().target("/profile/1?userId=6").request().get(Profile.class);

    }
}
