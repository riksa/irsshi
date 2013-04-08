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
    void succeeded(String alias);

    void failed(String alias, String message);
}
