package com.cestasoft.mobileservices.msp.outbound.config;


import com.cestasoft.mobileservices.framework.data.DataStore;
import com.cestasoft.mobileservices.msp.outbound.config.context.SMPPContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for SMPP client transactions
 * @author ezra.k@cestasoft.com
 */
@Configuration
public class SMPPConfig {
    final Logger logger = LoggerFactory.getLogger(SMPPConfig.class);

    @Value(value = "${datastore.mongodb.uri}")
    private String dbUri;

    @Bean
    public SMPPContext processorContext() {
        return new SMPPContext(dataStoreFactory());
    }

    @Bean
    public DataStore dataStoreFactory() {
        return new DataStore(dbUri);
    }
}
