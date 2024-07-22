package com.cestasoft.mobileservices.msp.outbound.service;

import com.cestasoft.mobileservices.msp.outbound.model.message.MessageDTO;

import java.util.List;

public interface MessageService {
    List<MessageDTO> getMessages(String msisdn, String status, String channel);
    List<MessageDTO> getMessagesByStatus(String status);
    List<MessageDTO> getMessagesByMsisdn(String msisdn);
    List<MessageDTO> getMessagesByChannel(String channel);
}