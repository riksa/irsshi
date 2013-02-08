/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import org.riksa.irsshi.domain.TermHost;

import java.util.List;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 23:18
 */
public interface TermHostDao {
    List<TermHost> getHosts();

    void insertHost(TermHost termHost);

    int deleteHost(long hostId);

    int updateHost(TermHost termHost);

    TermHost findHostById(long hostId);

//    Cursor getCursor();
}
