/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
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
