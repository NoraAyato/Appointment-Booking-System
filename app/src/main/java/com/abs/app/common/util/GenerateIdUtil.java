package com.abs.app.common.util;

import java.util.UUID;

public class GenerateIdUtil {
    public static String GenerateId(){
        return UUID.randomUUID().toString();
    }
}
