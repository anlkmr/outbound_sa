package com.cestasoft.mobileservices.msp.outbound.model.smpp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Base DTO for dispatch transactions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispatchTransactionDTO implements Serializable {
    String transactionId;
    long priority;
    long timestamp;
    String channelName;
    String partition;
}
