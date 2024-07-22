package com.cestasoft.mobileservices.msp.outbound.service.impl;

import com.cestasoft.mobileservices.framework.data.DataStore;
import com.cestasoft.mobileservices.msp.outbound.model.message.MessageDTO;
import com.cestasoft.mobileservices.msp.outbound.service.MessageService;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final DataStore _ds;

    @Autowired
    public MessageServiceImpl(DataStore ds) {
        this._ds = ds;
    }

    @Override
    public List<MessageDTO> getMessages(String msisdn, String status, String channel) {
        logger.info("Fetching all messages with filters - MSISDN: {}, Status: {}, Channel: {}", msisdn, status, channel);
        ArrayList<Document> fetched = _ds.fetch("bpe-mobileservices-outbound:messageDTO");

        List<MessageDTO> messageDTOS = fetched.stream().map(doc -> {
            Long id = doc.get("id") instanceof Integer ? ((Integer) doc.get("id")).longValue() : doc.getLong("id");
            long timestamp = doc.get("timestamp") instanceof Integer ? ((Integer) doc.get("timestamp")).longValue() : doc.getLong("timestamp");

            return new MessageDTO(
                    id,
                    doc.getString("msisdn"),
                    doc.getString("status"),
                    doc.getString("channel"),
                    doc.getString("content"),
                    timestamp
            );
        }).collect(Collectors.toList());

        logger.info("Fetched {} messages", messageDTOS.size());
        return messageDTOS;
    }


    @Override
    public List<MessageDTO> getMessagesByStatus(String status) {
        logger.info("Fetching messages with status: {}", status);
        Bson filter = Filters.eq("status", status);
        ArrayList<Document> fetched = _ds.query(filter, "bpe-mobileservices-outbound:messageDTO");

        List<MessageDTO> messageDTOS = fetched.stream().map(doc -> {
            Long id = doc.get("id") instanceof Integer ? ((Integer) doc.get("id")).longValue() : doc.getLong("id");
            long timestamp = doc.get("timestamp") instanceof Integer ? ((Integer) doc.get("timestamp")).longValue() : doc.getLong("timestamp");

            return new MessageDTO(
                    id,
                    doc.getString("msisdn"),
                    doc.getString("status"),
                    doc.getString("channel"),
                    doc.getString("content"),
                    timestamp
            );
        }).collect(Collectors.toList());

        logger.info("Fetched {} messages with status: {}", messageDTOS.size(), status);
        return messageDTOS;
    }


    @Override
    public List<MessageDTO> getMessagesByMsisdn(String msisdn) {
        logger.info("Fetching messages for MSISDN: {}", msisdn);
        Bson filter = Filters.eq("msisdn", msisdn);
        ArrayList<Document> fetched = _ds.query(filter, "bpe-mobileservices-outbound:messageDTO");

        List<MessageDTO> messageDTOS = fetched.stream().map(doc -> {
            Long id = doc.get("id") instanceof Integer ? ((Integer) doc.get("id")).longValue() : doc.getLong("id");
            long timestamp = doc.get("timestamp") instanceof Integer ? ((Integer) doc.get("timestamp")).longValue() : doc.getLong("timestamp");

            return new MessageDTO(
                    id,
                    doc.getString("msisdn"),
                    doc.getString("status"),
                    doc.getString("channel"),
                    doc.getString("content"),
                    timestamp
            );
        }).collect(Collectors.toList());

        logger.info("Fetched {} messages for MSISDN: {}", messageDTOS.size(), msisdn);
        return messageDTOS;
    }


    @Override
    public List<MessageDTO> getMessagesByChannel(String channel) {
        logger.info("Fetching messages for channel: {}", channel);
        Bson filter = Filters.eq("channel", channel);
        ArrayList<Document> fetched = _ds.query(filter, "bpe-mobileservices-outbound:messageDTO");

        List<MessageDTO> messageDTOS = fetched.stream().map(doc -> {
            Long id = doc.get("id") instanceof Integer ? ((Integer) doc.get("id")).longValue() : doc.getLong("id");
            long timestamp = doc.get("timestamp") instanceof Integer ? ((Integer) doc.get("timestamp")).longValue() : doc.getLong("timestamp");

            return new MessageDTO(
                    id,
                    doc.getString("msisdn"),
                    doc.getString("status"),
                    doc.getString("channel"),
                    doc.getString("content"),
                    timestamp
            );
        }).collect(Collectors.toList());

        logger.info("Fetched {} messages for channel: {}", messageDTOS.size(), channel);
        return messageDTOS;
    }

}
