package com.cestasoft.mobileservices.msp.outbound.controller;

import com.cestasoft.mobileservices.msp.outbound.component.amqp.MessageSender;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchBulkMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMultiMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.config.context.SMPPContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with endpoints for sending mes single message, bulk messages and multi messages
 * @author ezra.k@cestasoft.com
 */
@RestController
@RequestMapping("/api/v1/sms")
public class DispatchController {

    final Logger logger = LoggerFactory.getLogger(DispatchController.class);

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private SMPPContext _context;

    @Operation(summary = "Dispatch a single message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message dispatched successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "500", description = "Error dispatching message",
                    content = @Content)
    })
    @PostMapping("/dispatch/message")
    Document dispatchMessage(@RequestBody DispatchMessageDTO messageDTO) {
        boolean status = false;
        try {
            _context.load();
            messageSender.sendMessage(messageDTO);
            status = true;
            logger.debug("message queued: {}", messageDTO.getTransactionId());
        } catch(Exception ex) {
            logger.error("error queueing message", ex);
        }
        return new Document()
            .append("success", status)
            .append("txid", messageDTO.getTransactionId());
    }

    @Operation(summary = "Dispatch bulk messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bulk messages dispatched successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "500", description = "Error dispatching bulk messages",
                    content = @Content)
    })
    @PostMapping("/dispatch/bulk/message")
    Document dispatchBulkMessage(@RequestBody DispatchBulkMessageDTO bulkMessageDTO) {
        boolean status = false;
        try {
            _context.load();
            messageSender.sendBulkMessages(bulkMessageDTO);
            status = true;
            logger.debug("bulk messages queued: {}", bulkMessageDTO.getTransactionId());
        } catch(Exception ex) {
            logger.error("error queueing bulk messages", ex);
        }
        return new Document()
                .append("success", status)
                .append("txid", bulkMessageDTO.getTransactionId());
    }

    @Operation(summary = "Dispatch multi messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Multi messages dispatched successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "500", description = "Error dispatching multi messages",
                    content = @Content)
    })
    @PostMapping("/dispatch/multi/message")
    Document dispatchMultiMessage(@RequestBody DispatchMultiMessageDTO multiMessageDTO) {
        boolean status = false;
        try {
            _context.load();
            messageSender.sendMultiMessages(multiMessageDTO);
            status = true;
            logger.debug("multi messages queued: {}", multiMessageDTO.getTransactionId());
        } catch(Exception ex) {
            logger.error("error queueing multi messages", ex);
        }
        return new Document()
                .append("success", status)
                .append("txid", multiMessageDTO.getTransactionId());
    }
}
