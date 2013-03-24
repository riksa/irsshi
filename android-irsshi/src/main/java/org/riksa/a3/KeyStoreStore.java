/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

/**
 * User: riksa
 * Date: 21.10.2012
 * Time: 18:50
 * interface for storing/retrieving keys
 */
public interface KeyStoreStore {
    /**
     * Returns a key for specified keyId
     *
     * @param id
     * @return
     */
    A3Key getKeyWithAlias(String id) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException;

    /**
     * Add a new key to store
     *
     *
     * @param key
     * @param keyPair
     * @return true if key was added (false if it already existed)
     */
    boolean addKey(A3Key key, KeyPair keyPair) throws KeyStoreException;

    /**
     * Remove given key from store
     *
     *
     * @param key
     * @return true if key was removed (false if key was not in the store)
     */
    boolean removeKey(String key) throws KeyStoreException;

    /**
     * Get an unmodifiable list of keys
     *
     * @return
     */
    List<A3Key> getUnmodifiableKeys() throws KeyStoreException;

    /**
     * check to see if keystore is locked
     *
     * @return true if locked
     */
    boolean isLocked();

    /**
     * Unlocks the KeyStore
     *
     * @param callback @PromptPasswordCallback for prompting unlocking password
     */
    void unlock(PromptPasswordCallback callback) throws KeyStoreException, IOException;

    /**
     * Locks the keystore
     */
    void lock();

    /**
     * Saves the keystore
     *
     * @param promptSavePassword callback for requesting save password
     * @throws java.security.KeyStoreException
     * @throws java.io.IOException
     */
    void save(PromptPasswordCallback promptSavePassword) throws KeyStoreException, IOException;

}
