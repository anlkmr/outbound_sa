package com.cestasoft.mobileservices.msp.outbound.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Uid {

    public final static String CODE = "uid.type.code";
    public final static String TAG = "uid.type.tag";
    public final static String TX = "uid.type.tx";
    public final static String ID = "uid.type.id";

    public String gen(final String type, final int size) {
        char [] alphabet;
        switch (type) {
            case CODE -> alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
            case TAG -> alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            case TX -> alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            case ID -> alphabet = "1234567890".toCharArray();
            default -> alphabet = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        }
        return NanoIdUtils.randomNanoId(new Random(), alphabet, size);
    }
}
