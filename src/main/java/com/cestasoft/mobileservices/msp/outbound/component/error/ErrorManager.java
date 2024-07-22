package com.cestasoft.mobileservices.msp.outbound.component.error;

import com.cestasoft.mobileservices.msp.outbound.component.amqp.MessageSender;
import com.cestasoft.mobileservices.msp.outbound.model.ChannelDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchErrorMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorManager {

    final Logger logger = LoggerFactory.getLogger(ErrorManager.class);

    public void processError(final ChannelDTO channel, DispatchTransactionDTO dispatchMessage, final Exception x, final Logger srcLogger, String description) {
        srcLogger.error(description, x);
        DispatchErrorMessageDTO errorMessage = new DispatchErrorMessageDTO();
        errorMessage.setTimestamp(System.currentTimeMillis());
        errorMessage.setErrorMessage(x.getMessage() + ": " + description);
        errorMessage.setErrorType(x.getClass().getTypeName());
        errorMessage.setTransactionId(dispatchMessage.getTransactionId());
        errorMessage.setPartition(dispatchMessage.getPartition());
        errorMessage.setErrorPayload(dispatchMessage);
        if (channel != null) {
            errorMessage.setChannel(channel);
            errorMessage.setChannelName(channel.getName());
        }
        logger.debug("error dispatching message(s) [{} ({})]: {}", errorMessage.getTransactionId(), errorMessage.getPartition(), errorMessage.getErrorType());
        // TODO - send error to error processor: errorProcessor.sendErrorMessages(errorMessage);
    }
}
