package org.riksa.irsshi.domain;

import com.jcraft.jsch.Session;
import jackpal.androidterm.emulatorview.TermSession;

/**
 * User: riksa
 * Date: 2/15/13
 * Time: 9:30 AM
 */
public interface ConnectionStateChangeListener {
    void stateChanged(ConnectionInfo.HostState hostState, TermHost termHost, TermSession session);
}
