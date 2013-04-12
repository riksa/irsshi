/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
