package com.haduart.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.haduart.core.Profile;
import com.haduart.db.ProfileDAO;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ProfilesResource}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfilesResourceTest {
    private static final ProfileDAO dao = mock(ProfileDAO.class);


    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ProfilesResource(dao))
            .build();
    @Captor
    private ArgumentCaptor<Profile> personCaptor;
    private Profile profile;

    @Before
    public void setUp() {
        profile = new Profile();
        profile.setFullName("Full Name");
        profile.setJobTitle("Job Title");
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void createPerson() throws JsonProcessingException {
        when(dao.create(any(Profile.class))).thenReturn(profile);
        final Response response = resources.client().target("/profile")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(profile, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(dao).create(personCaptor.capture());
        assertThat(personCaptor.getValue()).isEqualTo(profile);
    }

    @Test
    public void listPeople() throws Exception {
        final ImmutableList<Profile> people = ImmutableList.of(profile);
        when(dao.findAll()).thenReturn(people);

        final List<Profile> response = resources.client().target("/profile")
                .request().get(new GenericType<List<Profile>>() {});

        verify(dao).findAll();
        assertThat(response).containsAll(people);
    }
}
