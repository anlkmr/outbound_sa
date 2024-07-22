package com.cestasoft.mobileservices.msp.outbound.model.smpp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * DTO for single message
 * @author ezra.k@cestasoft.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispatchMessageDTO extends DispatchTransactionDTO implements Serializable {
    String source;
    String recipient;
    String value;
}
