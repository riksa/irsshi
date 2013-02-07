package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 2/7/13
 * Time: 11:56 AM
 */
public abstract class AbstractHost implements TermHost {
    private Integer id;
    private String nickName;
    private String hostName;
    private int port;
    private String userName;

    protected AbstractHost(String hostName, String userName, int port) {
        this.hostName = hostName;
        this.port = port;
        this.userName = userName;
    }

    @Override
    public abstract HostType getHostType();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
        if (getNickName() == null || getNickName().isEmpty()) {
            setNickName(hostName);
        }
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
        return String.format("%s://%s@%s:%d", getHostType(), getUserName(), getHostName(), getPort());
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
