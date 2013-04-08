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
     * Prompt user for locking password
     *
     * @return password from the user, null=cancel
     */
    String getLockingPassword();

    /**
     * Prompt user for unlocking password (might want to ask it twice)
     *
     * @return password from the user, null=cancel
     */
    String getUnlockingPassword();
}
