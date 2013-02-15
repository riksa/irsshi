package org.riksa.irsshi.domain;

import jackpal.androidterm.emulatorview.TermSession;

import java.util.LinkedList;
import java.util.List;

/**
 * User: riksa
 * Date: 2/15/13
 * Time: 9:29 AM
 */
public class ConnectionInfo {
    public enum HostState {
        CONNECTING,
        CONNECTED,
        DISCONNECTED
    }

    private HostState hostState;
    private TermHost termHost;
    private TermSession session;

    private final List<ConnectionStateChangeListener> listeners = new LinkedList<ConnectionStateChangeListener>();

    public ConnectionInfo(HostState hostState, TermSession session, TermHost termHost, ConnectionStateChangeListener listener) {
        this.hostState = hostState;
        this.listeners.add(listener);
        this.session = session;
        this.termHost = termHost;
        notifyStateChange();
    }

    public HostState getHostState() {
        return hostState;
    }

    public void setHostState(HostState hostState) {
        this.hostState = hostState;
        notifyStateChange();
    }

    public TermSession getSession() {
        return session;
    }

    public void setSession(TermSession session) {
        this.session = session;
        notifyStateChange();
    }

    public TermHost getTermHost() {
        return termHost;
    }

    public void setTermHost(TermHost termHost) {
        this.termHost = termHost;
        notifyStateChange();
    }

    private void notifyStateChange() {
        for (ConnectionStateChangeListener listener : listeners) {
            listener.stateChanged(hostState, termHost, session);
        }
    }
}
