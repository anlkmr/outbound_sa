package com.cestasoft.mobileservices.msp.outbound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Mobile service platform outbound services for SMS, email (TODO anc WhatsApp)
 * @author ezra.k@cestasoft.com
 */
@SpringBootApplication
public class OutboundProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(OutboundProcessorApplication.class, args);
	}
}
