package com.cestasoft.mobileservices.msp.outbound.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for smpp channel configuration
 * @author ezra.k@cestasoft.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO implements Serializable {
    String channelId;
    String name;
    String host;
    int port;
    String systemId;
    String password;
    String serviceType;
    String sourceAddress;
    long modified;
    String status;
    String [] filters;
    int priority;
}
