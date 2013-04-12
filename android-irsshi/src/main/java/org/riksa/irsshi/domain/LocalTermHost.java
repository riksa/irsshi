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

package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public class LocalTermHost extends AbstractHost implements TermHost {
    private static final int DEFAULT_TELNET_PORT = 23;
    private static final String TERM_DEFAULT_USER = "root";
    private static final String TERM_DEFAULT_HOST = "localhost";

    public LocalTermHost(Integer id) {
        super(id);
        setHostName(TERM_DEFAULT_HOST);
        setUserName(TERM_DEFAULT_USER);
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_TELNET_PORT;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HostType getHostType() {
        return HostType.LOCAL;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected boolean validateHostName() {
        return true;
    }

    @Override
    protected boolean validatePort() {
        return true;
    }
}
