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
public class MoshTermHost extends AbstractHost implements TermHost {
    private static final int DEFAULT_MOSH_PORT = 60606;

    public MoshTermHost(Integer id) {
        super(id);
        setPort(getDefaultPort());
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_MOSH_PORT;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HostType getHostType() {
        return HostType.MOSH;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
