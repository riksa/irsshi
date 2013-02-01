package org.riksa.irsshi.logger;

import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 2/1/13
 * Time: 10:11 AM
 */
public class LoggerFactory {
    public static Logger getLogger(Class<?> clazz) {
        return org.slf4j.LoggerFactory.getLogger("irsshi_" + clazz.getSimpleName());
    }
}
