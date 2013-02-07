package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public class LocalTermHost extends AbstractHost implements TermHost {
    private static final String TERM_DEFAULT_USER = "root";
    private static final String TERM_DEFAULT_HOST = "localhost";

    public LocalTermHost() {
        super(TERM_DEFAULT_HOST, TERM_DEFAULT_USER, 0);
    }

    public LocalTermHost(String userName) {
        super(TERM_DEFAULT_HOST, userName, 0);
    }

    @Override
    public HostType getHostType() {
        return HostType.TERM;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
