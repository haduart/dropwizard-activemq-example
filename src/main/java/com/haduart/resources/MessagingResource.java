package com.haduart.resources;


import com.kjetland.dropwizard.activemq.ActiveMQBundle;
import com.haduart.core.View;
import com.haduart.db.ViewJdbiDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagingResource {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ActiveMQBundle activeMQBundle;
    private final ViewJdbiDAO jdbiDAO;

    public MessagingResource(ActiveMQBundle activeMQBundle, String queueName, ViewJdbiDAO jdbiDAO) {
        this.activeMQBundle = activeMQBundle;
        this.jdbiDAO = jdbiDAO;

        registerMessageReceiver(activeMQBundle, queueName);
    }

    private void registerMessageReceiver(ActiveMQBundle activeMQBundle, String queueName) {
        activeMQBundle.registerReceiver(
                queueName,
                (view) -> createView(view),
                View.class,
                true);
    }

    public View createView(View view) {
        if (view == null)
            throw new NullViewException();

        jdbiDAO.insert(view.getProfileId(), view.getUserId(), view.getTs());
        return view;
    }

    private class NullViewException extends RuntimeException {
    }
}
