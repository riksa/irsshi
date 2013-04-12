/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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