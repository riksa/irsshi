/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

/**
 * User: riksa
 * Date: 21.10.2012
 * Time: 21:50
 */
public interface PromptPasswordCallback {
    /**
     * Type of password, keyhchain or a single key
     */
    enum PasswordType {KEYCHAIN, KEY}

    /**
     * Prompt user for a password
     * @param lock password is requested for locking/unlocking (might want to double check the password if it's for locking keys)
     * @param passwordType type of password requested, keychain or a single key.
     * @return password from the user, null=cancel
     */
    String getPassword(boolean lock, PasswordType passwordType);
}
