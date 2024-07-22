package com.cestasoft.mobileservices.msp.outbound.model.smpp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * DTO for bulk messages
 * @author ezra.k@cestasoft.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispatchBulkMessageDTO extends DispatchTransactionDTO implements Serializable {
    String source;
    String [] recipients;
    String value;
}
