/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi.domain;

import android.content.Loader;

import java.util.Collection;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public interface TermHost {

    public enum TermHostValidationError {
        NICKNAME, HOSTNAME, PORT, USERNAME
    }

    Integer getId();

    void setNickName(String string);

    String getNickName();

    boolean validate();

    Collection<TermHostValidationError> getErrors();

    public enum HostType {SSH, TERM, MOSH}

    HostType getHostType();

    String getHostName();

    void setHostName(String hostName);

    String getUserName();

    void setUserName(String userName);

    int getPort();

    void setPort(int port);
}
