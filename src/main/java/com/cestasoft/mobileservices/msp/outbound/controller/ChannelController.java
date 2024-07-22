package com.cestasoft.mobileservices.msp.outbound.controller;

import com.cestasoft.mobileservices.framework.data.DataStore;
import com.cestasoft.mobileservices.msp.outbound.component.smpp.MessageProcessor;
import com.cestasoft.mobileservices.msp.outbound.model.ChannelDTO;
import com.mongodb.client.model.Filters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manage")
public class ChannelController {
    private final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    private final DataStore _ds;

    @Autowired
    private MessageProcessor _processor;

    @Autowired
    public ChannelController(DataStore ds) {
        this._ds = ds;
    }

    @Operation(summary = "Create a new channel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Channel created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChannelDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/channels")
    public ResponseEntity<ChannelDTO> createChannel(@RequestBody ChannelDTO channelDTO) {
        logger.info("Creating new channel: {}", channelDTO);

        // Implement your logic to convert ChannelDTO to Document and store it
        Document document = new Document();
        document.put("channelId", channelDTO.getChannelId());
        document.put("name", channelDTO.getName());
        document.put("host", channelDTO.getHost());
        document.put("port", channelDTO.getPort());
        document.put("systemId", channelDTO.getSystemId());
        document.put("password", channelDTO.getPassword());
        document.put("serviceType", channelDTO.getServiceType());
        document.put("sourceAddress", channelDTO.getSourceAddress());
        document.put("modified", channelDTO.getModified());
        document.put("status", channelDTO.getStatus());
        document.put("filters", List.of(channelDTO.getFilters()));
        document.put("priority", channelDTO.getPriority());

        Document result = _ds.store(document, "bpe-mobileservices-outbound:channel-config");
        if (result.getBoolean("success", false)) {
            logger.info("Channel created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(channelDTO);
        } else {
            logger.error("Failed to create channel");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get a specific channel by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Channel found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChannelDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Channel not found",
                    content = @Content)
    })
    @GetMapping("/channels/{id}")
    public ResponseEntity<ChannelDTO> getChannelById(@PathVariable String id) {
        logger.info("Fetching channel with ID: {}", id);
        Bson filter = Filters.eq("channelId", id);
        Document fetched = _ds.ask(filter, "bpe-mobileservices-outbound:channel-config");

        if (fetched != null) {
            logger.info("Channel document found: {}", fetched.toJson());
            ChannelDTO channelDTO = _processor.parseChanelDoc(fetched);
            return ResponseEntity.ok(channelDTO);
        } else {
            logger.warn("No channel found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get the list of all channels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Channels found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChannelDTO.class)) }),
            @ApiResponse(responseCode = "204", description = "No channels found",
                    content = @Content)
    })
    @GetMapping("/channels")
    public ResponseEntity<List<ChannelDTO>> getAllChannels() {
        logger.info("Fetching all channels");
        ArrayList<Document> fetched = _ds.fetch("bpe-mobileservices-outbound:channel-config");
        List<ChannelDTO> channelDTOs = new ArrayList<>();

        for (Document doc : fetched) {
            ChannelDTO channelDTO = _processor.parseChanelDoc(doc);
            channelDTOs.add(channelDTO);
        }

        if (channelDTOs.isEmpty()) {
            logger.warn("No channels found");
            return ResponseEntity.noContent().build();
        } else {
            logger.info("Found {} channels", channelDTOs.size());
            return ResponseEntity.ok(channelDTOs);
        }
    }

    @Operation(summary = "Update an existing channel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Channel updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChannelDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Channel not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/channel/{id}")
    public ResponseEntity<ChannelDTO> updateChannel(@PathVariable String id, @RequestBody ChannelDTO updatedChannelDTO) {
        logger.info("Updating channel with ID: {}", id);

        Document filter = new Document("channelId", id);
        Document existingChannel = _ds.ask(filter, "bpe-mobileservices-outbound:channel-config");

        if (existingChannel != null) {
            // Update fields in the existing channel document
            //existingChannel.put("channelId", updatedChannelDTO.getChannelId());
            existingChannel.put("name", updatedChannelDTO.getName());
            existingChannel.put("host", updatedChannelDTO.getHost());
            existingChannel.put("port", updatedChannelDTO.getPort());
            existingChannel.put("systemId", updatedChannelDTO.getSystemId());
            existingChannel.put("password", updatedChannelDTO.getPassword());
            existingChannel.put("serviceType", updatedChannelDTO.getServiceType());
            existingChannel.put("sourceAddress", updatedChannelDTO.getSourceAddress());
            existingChannel.put("modified", updatedChannelDTO.getModified());
            existingChannel.put("status", updatedChannelDTO.getStatus());
            existingChannel.put("filters", List.of(updatedChannelDTO.getFilters()));
            existingChannel.put("priority",updatedChannelDTO.getPriority());

            Document result = _ds.upsert(filter, existingChannel, "bpe-mobileservices-outbound:channel-config");
            if (result.getBoolean("success", false)) {
                logger.info("Channel updated successfully");
                return ResponseEntity.ok(updatedChannelDTO);
            } else {
                logger.error("Failed to update channel");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            logger.warn("Channel with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a channel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Channel deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Channel not found",
                    content = @Content)
    })
    @DeleteMapping("/channel/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable String id) {
        logger.info("Deleting channel with ID: {}", id);

        Bson filter = Filters.eq("channelId", id);
        Document result = _ds.ask(filter, "bpe-mobileservices-outbound:channel-config");
        if (result != null) {
            // Perform deletion logic
            _ds.delete(filter, "bpe-mobileservices-outbound:channel-config");

            logger.info("Channel deleted successfully");
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Channel with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
