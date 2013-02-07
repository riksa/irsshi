package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public class SshTermHost extends AbstractHost implements TermHost {
    public static final int DEFAULT_SSH_PORT = 22;

    public SshTermHost() {
        super(null, null, DEFAULT_SSH_PORT);
    }

    public SshTermHost(String hostName, String userName) {
        super(hostName, userName, DEFAULT_SSH_PORT);
    }

    public SshTermHost(String hostName, String userName, int port) {
        super(hostName, userName, port);
    }

    @Override
    public HostType getHostType() {
        return HostType.SSH;
    }

}
