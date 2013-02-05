package org.riksa.irsshi;

import org.riksa.irsshi.domain.LocalTermHost;
import org.riksa.irsshi.domain.MoshTermHost;
import org.riksa.irsshi.domain.SshTermHost;
import org.riksa.irsshi.domain.TermHost;

import java.util.ArrayList;
import java.util.List;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 23:18
 */
public class MockTermHostDao implements TermHostDao {
    List<TermHost> hosts;

    public MockTermHostDao() {
        hosts = new ArrayList<TermHost>();
        hosts.add(new SshTermHost("some.host.ssh", "someone"));
        hosts.add(new SshTermHost("another.host.ssh", "someoneelse", 2222));
        hosts.add(new MoshTermHost("some.host.mosh", "foomosh"));
        hosts.add(new MoshTermHost("another.host.mosh", "barmosh", 1111));
        hosts.add(new LocalTermHost());
        hosts.add(new LocalTermHost("android"));
    }

    @Override
    public List<TermHost> getHosts() {
        return hosts;
    }
}
