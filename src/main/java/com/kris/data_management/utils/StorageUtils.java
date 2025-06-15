package com.kris.data_management.utils;

import org.springframework.stereotype.Component;

@Component
public class StorageUtils {
    public static String createPhysicalName(String prefix, String displayName, Integer randomPartLen) {
        return prefix + "_" +
            displayName.replace(' ', '_') +
            "_" +
            StringUtil.generateRandomString(randomPartLen);
    }
}
