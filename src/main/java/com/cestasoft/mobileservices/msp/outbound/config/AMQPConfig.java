package com.cestasoft.mobileservices.msp.outbound.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

/**
 * Springboot auto configuration for AMQP provider - RabbitMQ
 * @author ezra.k@cestasoft.com
 */
@Configuration
public class AMQPConfig {

    final Logger logger = LoggerFactory.getLogger(AMQPConfig.class);

    @Value(value = "${outbound.smpp.messaging.queue}")
    private String amqpQueue;

    @Value(value = "${outbound.smpp.messaging.bulk}")
    private String amqpBulkQueue;

    @Value(value = "${outbound.smpp.messaging.multi}")
    private String amqpMultiQueue;

    @Value(value = "${outbound.processor.error.queue}")
    private String amqpErrorQueue;

    @Bean
    public Queue queue() {
        logger.debug("amqp message queue: {}", amqpQueue);
        return new Queue(amqpQueue, false);
    }

    @Bean
    public Queue bulkQueue() {
        logger.debug("amqp bulk messages queue: {}", amqpBulkQueue);
        return new Queue(amqpBulkQueue, false);
    }

    @Bean
    public Queue multiQueue() {
        logger.debug("amqp multi messages queue: {}", amqpMultiQueue);
        return new Queue(amqpMultiQueue, false);
    }

    @Bean
    public Queue error() {
        logger.debug("amqp error queue: {}", amqpErrorQueue);
        return new Queue(amqpErrorQueue, false);
    }

    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("com.cestasoft.mobileservices.msp.outbound.model.*", "java.util.*"));
        return converter;
    }
}

