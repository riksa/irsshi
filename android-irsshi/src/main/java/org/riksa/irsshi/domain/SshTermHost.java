package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public class SshTermHost extends AbstractHost implements TermHost {
    public static final int DEFAULT_SSH_PORT = 22;

    public SshTermHost(Integer id) {
        super(id);
        setPort(DEFAULT_SSH_PORT);
    }

    @Override
    public HostType getHostType() {
        return HostType.SSH;
    }

}
