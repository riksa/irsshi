/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

/**
 * User: riksa
 * Date: 8.4.2013
 * Time: 18:55
 */
public interface KeyGeneratorCallback {
    /**
     * Called after a key generation finishes successfully
     * @param alias of the key that was generated
     */
    void succeeded(String alias);

    /**
     * Called if the key generation fails for some reason
     * @param alias alias of the key that was requested
     * @param message the error message
     */
    void failed(String alias, String message);
}
