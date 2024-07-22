package com.cestasoft.mobileservices.msp.outbound.component.amqp;

import com.cestasoft.mobileservices.msp.outbound.component.smpp.MessageProcessor;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchBulkMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMultiMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.ChannelDTO;
import com.cestasoft.mobileservices.msp.outbound.service.SMPPDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumes messages from the RabbitMQ message queues
 * @author ezra.k@cestasoft.com
 */
@Component
public class MessageConsumer {

    final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private MessageProcessor processor;

    @Autowired
    private SMPPDispatcher dispatcher;

    @RabbitListener(queues = "#{'${outbound.smpp.messaging.queue}'}")
    public void receiveMessage(@Payload DispatchMessageDTO messageDTO) {
        logger.info("received message: {}", messageDTO.getTransactionId());
        ChannelDTO channel = processor.getSMPPChannelConfig(messageDTO.getChannelName());
        dispatcher.dispatchMessage(channel, messageDTO);
    }

    @RabbitListener(queues = "#{'${outbound.smpp.messaging.bulk}'}")
    public void receiveBulkMessages(@Payload DispatchBulkMessageDTO bulkMessageDTO) {
        logger.info("consumer bulk message: {} [{}], size {}", bulkMessageDTO.getTransactionId(), bulkMessageDTO.getPartition(), bulkMessageDTO.getRecipients().length);
        ChannelDTO channel = processor.getSMPPChannelConfig(bulkMessageDTO.getChannelName());
        dispatcher.dispatchBulkMessages(channel, bulkMessageDTO);
    }

    @RabbitListener(queues = "#{'${outbound.smpp.messaging.multi}'}")
    public void receiveMultiMessages(@Payload DispatchMultiMessageDTO multiMessageDTO) {
        logger.info("received multi message: {}, size: {}", multiMessageDTO.getTransactionId(), multiMessageDTO.getMessages().size());
        ChannelDTO channel = processor.getSMPPChannelConfig(multiMessageDTO.getChannelName());
        dispatcher.dispatchMultiMessages(channel, multiMessageDTO);
    }
}
