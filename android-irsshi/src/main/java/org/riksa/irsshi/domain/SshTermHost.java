package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public class SshTermHost implements TermHost {
    public static final int DEFAULT_SSH_PORT = 22;
    private String hostName;
    private int port;
    private String userName;

    public SshTermHost() {
        this(null, null, DEFAULT_SSH_PORT);
    }

    public SshTermHost(String hostName, String userName) {
        this(hostName, userName, DEFAULT_SSH_PORT);
    }

    public SshTermHost(String hostName, String userName, int port) {
        this.hostName = hostName;
        this.port = port;
        this.userName = userName;
    }

    @Override
    public HostType getHostType() {
        return HostType.SSH;  //To change body of implemented methods use File | Settings | File Templates.
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
        return String.format("ssh://%s@%s:%d", getUserName(), getHostName(), getPort());
    }
}
