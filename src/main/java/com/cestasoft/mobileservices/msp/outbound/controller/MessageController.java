package com.cestasoft.mobileservices.msp.outbound.controller;

import com.cestasoft.mobileservices.msp.outbound.model.message.MessageDTO;
import com.cestasoft.mobileservices.msp.outbound.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manage")
public class MessageController {
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Operation(summary = "Get messages with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages fetched successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@RequestParam(required = false) String msisdn,
                                                        @RequestParam(required = false) String status,
                                                        @RequestParam(required = false) String channel) {
        logger.info("Received request to get messages with filters - MSISDN: {}, Status: {}, Channel: {}", msisdn, status, channel);
        List<MessageDTO> messages = messageService.getMessages(msisdn, status, channel);
        logger.info("Successfully fetched {} messages", messages.size());
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Get messages by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages fetched successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/messages/query/status/{status}")
    public ResponseEntity<List<MessageDTO>> getMessagesByStatus(@PathVariable String status) {
        logger.info("Received request to get messages by status: {}", status);
        List<MessageDTO> messages = messageService.getMessagesByStatus(status);
        logger.info("Successfully fetched {} messages with status: {}", messages.size(), status);
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Get messages by MSISDN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages fetched successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/messages/query/msisdn/{msisdn}")
    public ResponseEntity<List<MessageDTO>> getMessagesByMsisdn(@PathVariable String msisdn) {
        logger.info("Received request to get messages for MSISDN: {}", msisdn);
        List<MessageDTO> messages = messageService.getMessagesByMsisdn(msisdn);
        logger.info("Successfully fetched {} messages for MSISDN: {}", messages.size(), msisdn);
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Get messages by channel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages fetched successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/messages/query/channel/{channel}")
    public ResponseEntity<List<MessageDTO>> getMessagesByChannel(@PathVariable String channel) {
        logger.info("Received request to get messages for channel: {}", channel);
        List<MessageDTO> messages = messageService.getMessagesByChannel(channel);
        logger.info("Successfully fetched {} messages for channel: {}", messages.size(), channel);
        return ResponseEntity.ok(messages);
    }
}
