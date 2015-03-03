package com.haduart.db;

import com.haduart.core.Profile;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class ProfileDAO extends AbstractDAO<Profile> {

    public ProfileDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Profile> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Profile create(Profile profile) {
        return persist(profile);
    }

    public List<Profile> findAll() {
        return list(namedQuery("com.haduart.core.Profile.findAll"));
    }
}
