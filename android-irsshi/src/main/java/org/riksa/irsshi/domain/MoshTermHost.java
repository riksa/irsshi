package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public class MoshTermHost implements TermHost {
    public static final int DEFAULT_MOSH_PORT = 22;
    private String hostName;
    private int port;
    private String userName;

    public MoshTermHost() {
        this(null, null, DEFAULT_MOSH_PORT);
    }

    public MoshTermHost(String hostName, String userName) {
        this(hostName, userName, DEFAULT_MOSH_PORT);
    }

    public MoshTermHost(String hostName, String userName, int port) {
        this.hostName = hostName;
        this.port = port;
        this.userName = userName;
    }

    @Override
    public HostType getHostType() {
        return HostType.MOSH;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getHostName() {
        return hostName;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getUserName() {
        return userName;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getPort() {
        return port;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return String.format("mosh://%s@%s:%d", getUserName(), getHostName(), getPort());
    }
}