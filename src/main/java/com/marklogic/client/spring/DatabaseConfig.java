package com.marklogic.client.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import com.marklogic.client.helper.DatabaseClientConfig;
import com.marklogic.client.helper.DatabaseClientProvider;
import com.marklogic.xcc.template.XccTemplate;

@Configuration
public class DatabaseConfig {
	
	@Autowired
	private Environment env;

	protected String getHost() {
		return env.getProperty("marklogic.host", "localhost");
	}
	
	protected int getPort() {
		return Integer.parseInt(env.getProperty("marklogic.port", "8000"));
	}
	
	protected String getUser() {
		return env.getProperty("marklogic.user", "admin");
	}
	
	protected String getPassword() {
		return env.getProperty("marklogic.password", "admin");
	}
	
	protected String getDatabaseName() {
		return env.getProperty("marklogic.database", "Documents");
	}
	
	/**
     * Has to be static so that Spring instantiates it first.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setIgnoreResourceNotFound(true);
        return c;
    }

    @Bean
    public DatabaseClientConfig databaseClientConfig() {
        return new DatabaseClientConfig(getHost(), getPort(), getUser(), getPassword());
    }

    @Bean
    public XccTemplate xccTemplate() {
        return new XccTemplate(String.format("xcc://%s:%s@%s:8000/%s", getUser(), getPassword(), getHost(), getDatabaseName()));
    }

    @Bean
    public DatabaseClientProvider databaseClientProvider() {
        return new SimpleDatabaseClientProvider();
    }

}
