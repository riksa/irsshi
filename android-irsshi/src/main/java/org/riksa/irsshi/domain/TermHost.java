package org.riksa.irsshi.domain;

import android.content.Loader;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public interface TermHost {
    public enum HostType {SSH, TERM, MOSH}

    HostType getHostType();

    String getHostName();

    void setHostName(String hostName);

    String getUserName();

    void setUserName(String userName);

    int getPort();

    void setPort(int port);
}
