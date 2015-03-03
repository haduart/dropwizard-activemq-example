package com.haduart;

import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.testing.junit.DropwizardAppRule;

import org.junit.ClassRule;


public class IntegrationTest {
	@ClassRule
	public static final DropwizardAppRule<DropwizardExampleConfiguration> RULE =
		new DropwizardAppRule<DropwizardExampleConfiguration>(DropwizardExampleApplication.class, "example.yml");
	
//	@Test
//	public void testHelloWorld() throws Exception {
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target("http://localhost:" + RULE.getLocalPort() + "/hello-world");
//
//		final Optional<String> NAME = Optional.fromNullable("Dr. IntegrationTest");
//
//		Saying saying = target.queryParam("name", NAME.get()).request().get(Saying.class);
//
//		assertThat(saying.getContent()).isEqualTo(RULE.getConfiguration().buildTemplate().render(NAME));
//	}
}
