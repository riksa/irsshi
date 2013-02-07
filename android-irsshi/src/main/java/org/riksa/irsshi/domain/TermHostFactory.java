package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 2/7/13
 * Time: 11:43 AM
 */
public class TermHostFactory {
    public static TermHost create(String hostType) {
        assert hostType != null;
        TermHost termHost = create(TermHost.HostType.valueOf(hostType));
        assert hostType.equals(termHost.getHostType().toString());
        return termHost;
    }

    private static TermHost create(TermHost.HostType hostType) {
        assert hostType != null;
        TermHost termHost = null;

        switch (hostType) {
            case MOSH:
                termHost = new MoshTermHost();
                break;
            case SSH:
                termHost = new MoshTermHost();
                break;
            case TERM:
                termHost = new MoshTermHost();
                break;
            default:
                assert false;
        }

        assert termHost.getHostType() == hostType;

        return termHost;
    }
}
