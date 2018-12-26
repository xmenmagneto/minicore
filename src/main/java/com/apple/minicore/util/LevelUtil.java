package com.apple.minicore.util;


import org.apache.commons.lang3.StringUtils;


/**
 * Util class to calculate next level based on current level and current id
 */
public class LevelUtil {

    private final static String SEPARATOR = ".";

    public static String calculateLevel(String parentLevel, int parentId) {
        return StringUtils.join(parentLevel, SEPARATOR, parentId);
    }
}
