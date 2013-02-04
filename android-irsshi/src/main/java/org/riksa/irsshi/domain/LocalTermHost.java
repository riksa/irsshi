package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public class LocalTermHost implements TermHost {
    private static final String TERM_DEFAULT_USER = "root";
    private static final String hostName = "localhost";
    private static final int port = 0;
    private String userName;

    public LocalTermHost() {
        this(TERM_DEFAULT_USER);
    }

    public LocalTermHost(String userName) {
        this.userName = userName;
    }

    @Override
    public HostType getHostType() {
        return HostType.TERM;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setHostName(String hostName) {
    }

    @Override
    public void setPort(int port) {
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
        return String.format("term://%s@%s", getUserName(), getHostName());
    }
}
