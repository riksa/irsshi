package org.riksa.irsshi;

import jackpal.androidterm.emulatorview.TermSession;

/**
 * User: riksa
 * Date: 2/14/13
 * Time: 3:28 PM
 */
public interface SessionViewListener {
    void onSessionConnected(TermSession session);

    void onSessionDisconnected(TermSession session);
}
