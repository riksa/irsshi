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
public class SshTermHost extends AbstractHost implements TermHost {
    private static final int DEFAULT_SSH_PORT = 22;

    public SshTermHost(Integer id) {
        super(id);
        setPort(getDefaultPort());
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_SSH_PORT;
    }

    @Override
    public HostType getHostType() {
        return HostType.SSH;
    }

}
