package com.cestasoft.mobileservices.msp.outbound.config;

import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchBulkMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMultiMessageDTO;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Spring configuration for MongoDB datastore
 * @author ezra.k@cestasoft.com
 */
@Configuration
public class DataStoreConfig {

    final Logger logger = LoggerFactory.getLogger(DataStoreConfig.class);

    @Value(value = "${datastore.mongodb.uri}")
    private String mongoDBUri;

    @Bean
    public MongoClient mongoClient() {
        logger.debug("creating mongo client: {}", mongoDBUri);
        return MongoClients.create(mongoDBUri);
    }

    @Bean
    public CodecRegistry pojoCodecRegistry() {
        logger.debug("loading pojo codec provider ..");
        List<Convention> conventions = Conventions.DEFAULT_CONVENTIONS;
        ClassModel<DispatchMessageDTO> dispatchMessageDTOClassModel
                = ClassModel.builder(DispatchMessageDTO.class).conventions(conventions).build();
        logger.debug("class model: {}", dispatchMessageDTOClassModel.getName());
        ClassModel<DispatchBulkMessageDTO> dispatchBulkMessageDTOClassModel
                = ClassModel.builder(DispatchBulkMessageDTO.class).conventions(conventions).build();
        logger.debug("class model: {}", dispatchBulkMessageDTOClassModel.getName());
        ClassModel<DispatchMultiMessageDTO> dispatchMultiMessageDTOClassModel
                = ClassModel.builder(DispatchMultiMessageDTO.class).conventions(conventions).build();
        logger.debug("class model: {}", dispatchMultiMessageDTOClassModel.getName());
        CodecProvider pojoCodecProvider
                = PojoCodecProvider.builder()
                .register(dispatchMessageDTOClassModel)
                .register(dispatchBulkMessageDTOClassModel)
                .register(dispatchMultiMessageDTOClassModel)
                .build();
        logger.debug("pojo codec provider registered: {}", pojoCodecProvider);
        return fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
    }
}
