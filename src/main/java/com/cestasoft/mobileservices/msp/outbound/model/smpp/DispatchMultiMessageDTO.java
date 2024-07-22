package com.cestasoft.mobileservices.msp.outbound.model.smpp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for multi messages
 * @author ezra.k@cestasoft.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispatchMultiMessageDTO extends DispatchTransactionDTO implements Serializable {
    List<DispatchMessageDTO> messages;
}
