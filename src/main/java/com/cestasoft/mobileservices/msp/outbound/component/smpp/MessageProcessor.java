package com.cestasoft.mobileservices.msp.outbound.component.smpp;

import com.cestasoft.mobileservices.msp.outbound.config.context.SMPPContext;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchBulkMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.smpp.DispatchMultiMessageDTO;
import com.cestasoft.mobileservices.msp.outbound.model.ChannelDTO;
import com.cestasoft.mobileservices.msp.outbound.util.Uid;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import java.util.Arrays;
import java.util.List;

/**
 * Processes consumed messages before dispatching them
 * @author ezra.k@cestasoft.com
 */
@Component
public class MessageProcessor {

    // TODO - log transaction starts with complete messages

    // TODO - map dispatch messages with channels

    @Autowired
    private Uid _uid;

    @Autowired
    private SMPPContext _context;

    public String tag() {
        return _uid.gen(Uid.TAG, 6) + "-" + _uid.gen(Uid.ID, 10);
    }

    public String getRecipientChannelName(String recipient) {
        Document config = _context.context();
        List<Document> channels = config.getList("channels", Document.class);
        return channels.stream()
                .filter(c -> {
                    List<String> prefixes = c.getList("filters", String.class);
                    return prefixes.contains(recipient.substring(0, 5));
                })
                .map(c -> c.get("name").toString())
                .findFirst()
                .orElse("");
    }

    public ChannelDTO parseChanelDoc(Document channelDoc) {
        if (!channelDoc.isEmpty()) {
            ChannelDTO channel = new ChannelDTO();
            channel.setChannelId(channelDoc.get("channelId").toString());
            channel.setName(channelDoc.get("name").toString());
            channel.setHost(channelDoc.get("host").toString());
            channel.setPort(Integer.parseInt(channelDoc.get("port").toString()));
            channel.setPriority(Integer.parseInt(channelDoc.get("priority").toString()));
            channel.setSystemId(channelDoc.get("systemId").toString());
            channel.setServiceType(channelDoc.get("serviceType").toString());
            channel.setPassword(channelDoc.get("password").toString());
            channel.setSourceAddress(channelDoc.get("sourceAddress").toString());
            channel.setStatus(channelDoc.get("status").toString());
            channel.setFilters(channelDoc.getList("filters", String.class).toArray(new String[0]));
            channel.setModified(Long.parseLong(channelDoc.get("modified").toString()));
            return channel;
        }
        return null;
    }

    public ChannelDTO getSMPPChannelConfig(String channelName) {
        Document config = _context.context();
        List<Document> channels = config.getList("channels", Document.class);
        Document channelDoc = channels.stream()
                .filter(c -> c.get("name").toString().equals(channelName))
                .findFirst()
                .orElse(new Document());
        if (!channelDoc.isEmpty()) {
            return parseChanelDoc(channelDoc);
        }
        return null;
    }

    public List<ChannelDTO> getSMPPChannels() {
        Document config = _context.context();
        List<Document> channelsDoc = config.getList("channels", Document.class);
        return channelsDoc.stream()
                .map(this::parseChanelDoc)
                .toList();
    }

    public List<String> getFilteredRecipients(Document channel, DispatchBulkMessageDTO bulkMessageDTO) {
        return Arrays.stream(bulkMessageDTO.getRecipients())
                .filter(r -> {
                    List<String> prefixes = channel.getList("filters", String.class);
                    return prefixes.contains(r.substring(0, 5));
                })
                .toList();
    }

    public List<DispatchBulkMessageDTO> mapBulkChannels(DispatchBulkMessageDTO bulkMessageDTO) {
        Document config = _context.context();
        return config.getList("channels", Document.class)
                .stream()
                .map(channelDoc -> {
                    DispatchBulkMessageDTO _bmdto = (DispatchBulkMessageDTO) SerializationUtils.clone(bulkMessageDTO);
                    List<String> recipients = getFilteredRecipients(channelDoc, bulkMessageDTO);
                    _bmdto.setSource(channelDoc.get("sourceAddress").toString());
                    _bmdto.setRecipients(recipients.toArray(new String[0]));
                    _bmdto.setChannelName(channelDoc.get("name").toString());
                    _bmdto.setPriority(Integer.parseInt(channelDoc.get("priority").toString()));
                    return _bmdto;
                }) // TODO - filter messages with no recipients
                .toList();
    }

    public List<DispatchMultiMessageDTO> mapMultiChannels(DispatchMultiMessageDTO multiMessageDTO) {
        Document config = _context.context();
        return config.getList("channels", Document.class)
                .stream()
                .map(channelDoc -> {
                    DispatchMultiMessageDTO _mmdto = (DispatchMultiMessageDTO) SerializationUtils.clone(multiMessageDTO);
                    List<DispatchMessageDTO> messages = getFilteredMessages(channelDoc, multiMessageDTO);
                    _mmdto.setMessages(messages);
                    _mmdto.setChannelName(channelDoc.get("name").toString());
                    _mmdto.setPriority(Integer.parseInt(channelDoc.get("priority").toString()));
                    _mmdto.setTransactionId(multiMessageDTO.getTransactionId());
                    return _mmdto;
                }) // TODO - filter messages with no recipients
                .toList();
    }

    private List<DispatchMessageDTO> getFilteredMessages(Document channelDoc, DispatchMultiMessageDTO multiMessageDTO) {
        return multiMessageDTO.getMessages().stream()
                .filter(r -> {
                    List<String> prefixes = channelDoc.getList("filters", String.class);
                    return prefixes.contains(r.getRecipient().substring(0, 5));
                })
                .toList();
    }

    public DispatchMessageDTO processMessage(DispatchMessageDTO messageDTO) {
        messageDTO.setTransactionId(tag());
        messageDTO.setTimestamp(System.currentTimeMillis());
        messageDTO.setPartition("1/1");
        messageDTO.setChannelName(getRecipientChannelName(messageDTO.getRecipient()));
        ChannelDTO channel = getSMPPChannelConfig(messageDTO.getChannelName());
        messageDTO.setSource(channel.getSourceAddress());
        messageDTO.setPriority(channel.getPriority());
        return messageDTO;
    }

    public List<DispatchBulkMessageDTO> processBulkMessages(DispatchBulkMessageDTO bulkMessageDTO) {
        bulkMessageDTO.setTransactionId(tag());
        bulkMessageDTO.setTimestamp(System.currentTimeMillis());
        return mapBulkChannels(bulkMessageDTO);
    }

    public List<DispatchMultiMessageDTO> processMultiMessages(DispatchMultiMessageDTO multiMessageDTO) {
        multiMessageDTO.setTimestamp(System.currentTimeMillis());
        multiMessageDTO.setTransactionId(tag());
        return mapMultiChannels(multiMessageDTO);
    }
}
