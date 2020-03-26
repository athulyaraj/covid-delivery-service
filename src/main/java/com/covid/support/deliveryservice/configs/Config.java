package com.covid.support.deliveryservice.configs;

import com.authy.AuthyApiClient;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Key;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class Config {

    @Value(("${connect.timeout:5000}"))
    int connectTimeout;

    @Value("${request.timeout:5000}")
    int requestTimeout;

    @Value("${socket.timeout:5000}")
    int socketTimeout;

    @Value("${datasource.jdbcUrl}")
    String jdbcUrl;

    @Value("${datasource.username}")
    String userName;

    @Value("${datasource.password}")
    String password;

    @Value("${datasource.poolsize}")
    int poolSize;

    @Value("${datasource.autocommit}")
    boolean autoCommit;

    @Value("${db.driver}")
    String driver;

    @Value("${hibernate.dialect}")
    String hibernateDialect;

    @Value("${datasource.port:5432}")
    int port;

    @Value("${datasource.hostname}")
    String hostName;

    @Value("${datasource.databasename}")
    String databaseName;

    @Value("${twilio.key}")
    String twilioToken;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public DataSource dataSource(HikariConfig config) {
        return new HikariDataSource(config);
    }

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("springHikariCP");
        config.setConnectionTestQuery("SELECT 1");
        config.setDataSourceClassName(driver);
        config.setDataSourceProperties(dataSourceProperties());
        config.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(10));
        config.setMaximumPoolSize(15);
        // Auto commit
        config.setAutoCommit(autoCommit);
        return config;
    }

    @Bean
    public ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(requestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        restTemplate.getMessageConverters().add(jsonHttpMessageConverter);
        return restTemplate;
    }

    @Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan(
                new String[]{"com.covid.support.deliveryservice.entities"});
        return sessionFactory;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "validate");
        hibernateProperties.setProperty(
                "hibernate.dialect", hibernateDialect);
        return hibernateProperties;
    }

    private Properties dataSourceProperties() {

        Properties properties = new Properties();

        properties.setProperty("user", userName);
        properties.setProperty("portNumber",String.valueOf(port));
        properties.setProperty("serverName", hostName);
        properties.setProperty("password", password);
        properties.setProperty("databaseName", databaseName);

        log.info("Trying to connect. Connection details are as follows - ");
        return properties;
    }

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

    @Bean
    public AuthyApiClient authyApiClient() {
        return new AuthyApiClient(twilioToken);
    }

}
