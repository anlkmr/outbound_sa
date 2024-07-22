package com.cestasoft.mobileservices.msp.outbound.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String msisdn;
    private String status;
    private String channel;
    private String content;
    private long timestamp;
}