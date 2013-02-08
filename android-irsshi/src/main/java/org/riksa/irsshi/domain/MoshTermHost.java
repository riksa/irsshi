package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public class MoshTermHost extends AbstractHost implements TermHost {
    public static final int DEFAULT_MOSH_PORT = 22;

    public MoshTermHost(Integer id) {
        super(id);
        setPort(DEFAULT_MOSH_PORT);
    }

    @Override
    public HostType getHostType() {
        return HostType.MOSH;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
