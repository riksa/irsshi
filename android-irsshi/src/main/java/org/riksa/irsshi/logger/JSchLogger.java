/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi.logger;

import com.jcraft.jsch.Logger;

/**
 * User: riksa
 * Date: 2/1/13
 * Time: 10:10 AM
 */
public class JSchLogger implements Logger {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(JSchLogger.class);

    public boolean isEnabled(int level) {
        return true;
    }

    public void log(int level, String message) {
        if (!isEnabled(level)) {
            return;
        }
        switch (level) {
            case com.jcraft.jsch.Logger.DEBUG:
                log.debug(message);
                break;
            case com.jcraft.jsch.Logger.INFO:
                log.info(message);
                break;
            case com.jcraft.jsch.Logger.WARN:
                log.warn(message);
                break;
            case com.jcraft.jsch.Logger.ERROR:
                log.error(message);
                break;
            case com.jcraft.jsch.Logger.FATAL:
                log.error(message);
                break;
        }
    }

}