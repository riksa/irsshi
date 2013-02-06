package org.riksa.irsshi;

import org.riksa.irsshi.domain.TermHost;

import java.util.List;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 23:18
 */
public interface TermHostDao {
    List<TermHost> getHosts();

    void addHost(TermHost termHost);

    void removeHost(TermHost termHost);
}
