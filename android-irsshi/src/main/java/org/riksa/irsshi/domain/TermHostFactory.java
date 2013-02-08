/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 2/7/13
 * Time: 11:43 AM
 */
public class TermHostFactory {
    public static TermHost create(String hostType, Integer id) {
        assert hostType != null;
        TermHost termHost = create(TermHost.HostType.valueOf(hostType), id);
        assert hostType.equals(termHost.getHostType().toString());
        return termHost;
    }

    public static TermHost create(TermHost.HostType hostType, Integer id) {
        assert hostType != null;
        TermHost termHost = null;

        switch (hostType) {
            case MOSH:
                termHost = new MoshTermHost(id);
                break;
            case SSH:
                termHost = new SshTermHost(id);
                break;
            case TERM:
                termHost = new LocalTermHost(id);
                break;
            default:
                assert false;
        }

        assert termHost.getHostType() == hostType;

        return termHost;
    }
}
