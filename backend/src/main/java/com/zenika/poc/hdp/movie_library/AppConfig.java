package com.zenika.poc.hdp.movie_library;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
@ComponentScan({"com.zenika.poc.hdp.movie_library.web", "com.zenika.poc.hdp.movie_library.service", "com.zenika.poc.hdp.movie_library.repository"})
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    public static final String INDEX = "library";
    public static final String MOVIE = "movie";
    public static final String RECOMMENDATION = "recommendation";
    public static final String RATING = "rating";

    @Value("${es.host:localhost}")
    private String host;

    @Value("${es.port:9300}")
    private Integer port;

    @Bean
    public static PropertyPlaceholderConfigurer configurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setIgnoreResourceNotFound(true);
        ppc.setSearchSystemEnvironment(true);
        ppc.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return ppc;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    public Client client() {
        logger.info("Creating ES client on node " + host + ":" + port);
        TransportClient client = new TransportClient();

        TransportAddress address = new InetSocketTransportAddress(host, port);
        client.addTransportAddress(address);

        return client;
    }

}
