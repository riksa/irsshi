package org.riksa.irsshi.domain;

import android.content.Loader;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public interface TermHost {
    void setId(Integer id);

    Integer getId();

    void setNickName(String string);

    String getNickName();

    public enum HostType {SSH, TERM, MOSH}

    HostType getHostType();

    String getHostName();

    void setHostName(String hostName);

    String getUserName();

    void setUserName(String userName);

    int getPort();

    void setPort(int port);
}
