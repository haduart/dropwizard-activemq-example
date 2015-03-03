package com.haduart;

import com.kjetland.dropwizard.activemq.ActiveMQBundle;
import com.kjetland.dropwizard.activemq.ActiveMQSender;
import com.haduart.core.Profile;
import com.haduart.core.View;
import com.haduart.db.ProfileDAO;
import com.haduart.db.ViewJdbiDAO;
import com.haduart.resources.MessagingResource;
import com.haduart.resources.ProfilesResource;
import com.haduart.resources.UserProfileResource;
import com.haduart.resources.ViewsResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DropwizardExampleApplication extends Application<DropwizardExampleConfiguration> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private ActiveMQBundle activeMQBundle;

    public static void main(String[] args) throws Exception {
        new DropwizardExampleApplication().run(args);
    }

    private final HibernateBundle<DropwizardExampleConfiguration> hibernateBundle =
            new HibernateBundle<DropwizardExampleConfiguration>(Profile.class, View.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(DropwizardExampleConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "dropwizard-example";
    }

    @Override
    public void initialize(Bootstrap<DropwizardExampleConfiguration> bootstrap) {
        this.activeMQBundle = new ActiveMQBundle();

        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<DropwizardExampleConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(DropwizardExampleConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);

        bootstrap.addBundle(activeMQBundle);
    }

    @Override
    public void run(DropwizardExampleConfiguration configuration, Environment environment) throws Exception {
        final ViewJdbiDAO jdbiDAO = createViewJdbiDAO(configuration, environment);

        final ProfileDAO profileDAO = new ProfileDAO(hibernateBundle.getSessionFactory());

        final String queueName = configuration.getQueueName();

        // Set up the sender for our queue
        ActiveMQSender sender = activeMQBundle.createSender(queueName, false);

        environment.jersey().register(new ProfilesResource(profileDAO));
        environment.jersey().register(new UserProfileResource(profileDAO, sender));
        environment.jersey().register(new ViewsResource(jdbiDAO));
        environment.jersey().register(new MessagingResource(activeMQBundle, queueName, jdbiDAO));

    }

    private ViewJdbiDAO createViewJdbiDAO(DropwizardExampleConfiguration configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2");
        return jdbi.onDemand(ViewJdbiDAO.class);
    }
}
