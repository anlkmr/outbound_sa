package com.cestasoft.mobileservices.msp.outbound.component.amqp;

import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchBulkMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchErrorMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMultiMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.component.smpp.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Publishes messages to RabbitMq queues
 * @author ezra.k@cestasoft.com
 */
@Component
public class MessageSender {

    final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Value(value = "${outbound.smpp.messaging.queue}")
    private String amqpQueue;

    @Value(value = "${outbound.smpp.messaging.bulk}")
    private String amqpBulkQueue;

    @Value(value = "${outbound.smpp.messaging.multi}")
    private String amqpMultiQueue;

    @Value(value = "${outbound.processor.error.queue}")
    private String amqpErrorQueue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    MessageProcessor _processor;

    public void sendMessage(DispatchMessageDTO messageDTO) {
        messageDTO = _processor.processMessage(messageDTO);
        rabbitTemplate.convertAndSend(amqpQueue, messageDTO);
    }

    public void sendBulkMessages(final DispatchBulkMessageDTO bulkMessageDTO) {
        List<DispatchBulkMessageDTO> bulkMessages = _processor.processBulkMessages(bulkMessageDTO);
        AtomicInteger count = new AtomicInteger(0);
        for (DispatchBulkMessageDTO bulkMsg : bulkMessages) {
            bulkMsg.setPartition(count.addAndGet(1) + "/" + bulkMessages.size());
            rabbitTemplate.convertAndSend(amqpBulkQueue, bulkMsg);
        }
    }

    public void sendMultiMessages(DispatchMultiMessageDTO multiMessageDTO) {
        List<DispatchMultiMessageDTO> multiMessages = _processor.processMultiMessages(multiMessageDTO);
        AtomicInteger count = new AtomicInteger(0);
        for (DispatchMultiMessageDTO multiMsg : multiMessages) {
            multiMsg.setPartition(count.addAndGet(1) + "/" + multiMessages.size());
            rabbitTemplate.convertAndSend(amqpMultiQueue, multiMsg);
        }
    }

    public void sendErrorMessages(DispatchErrorMessageDTO errorMessageDTO) {
        rabbitTemplate.convertAndSend(amqpErrorQueue, errorMessageDTO);
    }
}
