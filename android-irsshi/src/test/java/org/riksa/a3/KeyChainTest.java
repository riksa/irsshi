/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

import junit.framework.TestCase;

import java.security.KeyPair;

/**
 * User: riksa
 * Date: 3.4.2013
 * Time: 21:09
 */
public class KeyChainTest extends TestCase {

    public void testGenerateRSA() throws Exception {
        KeyChain keyChain = new KeyChain();
        KeyPair keyPair = keyChain.generateKey(KeyChain.KeyType.RSA, 512);
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
    }

    public void testGenerateDSA() throws Exception {
        KeyChain keyChain = new KeyChain();
        KeyPair keyPair = keyChain.generateKey(KeyChain.KeyType.DSA, 512);
        assertEquals("DSA", keyPair.getPublic().getAlgorithm());
    }
}
