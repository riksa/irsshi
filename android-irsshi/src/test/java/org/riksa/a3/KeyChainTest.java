/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.util.Collection;

import static org.mockito.Mockito.*;

/**
 * User: riksa
 * Date: 3.4.2013
 * Time: 21:09
 */
public class KeyChainTest extends TestCase {

    public void testGenerateRSA() throws Exception {
        KeyPair keyPair = KeyChain.generateKey(KeyChain.KeyType.RSA, 512);
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
    }

    public void testGenerateDSA() throws Exception {
        KeyPair keyPair = KeyChain.generateKey(KeyChain.KeyType.DSA, 512);
        assertEquals("DSA", keyPair.getPublic().getAlgorithm());
    }

    public void testUnlockNonexistant() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        verifyZeroInteractions(prompt);
        try {
            KeyChain keyChain = new KeyChain(new File("nonexistant.file"));
            assertTrue(keyChain.isLocked());
            keyChain.unlock(prompt);
            fail();
        } catch (FileNotFoundException e) {
        }
    }

    public void testUnlockWrongPassword() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn("wrong");
        File file = new File("pass.bks");
        KeyChain keyChain = new KeyChain(file);
        assertTrue(keyChain.isLocked());
        try {
            keyChain.unlock(prompt);
            fail();
        } catch (KeyStoreException e) {
            assertTrue(keyChain.isLocked());
        }
    }

    public void testUnlockFile() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn("pass");
        File file = new File("pass.bks");
        KeyChain keyChain = new KeyChain(file);
        assertTrue(keyChain.isLocked());
        keyChain.unlock(prompt);
        verify(prompt, times(1)).getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN);
        assertFalse(keyChain.isLocked());
    }

    public void testAliases() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn("pass");
        File file = new File("pass.bks");
        KeyChain keyChain = new KeyChain(file);
        assertTrue(keyChain.isLocked());
        keyChain.unlock(prompt);
        verify(prompt, times(1)).getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN);
        assertFalse(keyChain.isLocked());

        Collection<String> aliases = keyChain.aliases();
        assertSame(2, aliases.size());
        assertTrue(aliases.contains("pass"));
        assertTrue(aliases.contains("nopass"));
    }
}
