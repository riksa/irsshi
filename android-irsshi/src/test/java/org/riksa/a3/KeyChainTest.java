/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public static final String KEYCHAIN_PASS = "pass";
    public static final String KEY_PASS = "pass";
    final File passFile = findFile("pass.bks");

    private KeyChain getUnlockedKeyChain() throws IOException, KeyStoreException {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(KEYCHAIN_PASS);
        KeyChain keyChain = getLockedKeyChain();
        keyChain.unlock(prompt);
        assertFalse(keyChain.isLocked());
        return keyChain;
    }

    private KeyChain getLockedKeyChain() {
        KeyChain keyChain = new KeyChain(passFile);
        assertTrue(keyChain.isLocked());
        return keyChain;
    }

    public File findFile(String name) {
        return new File(name);
    }

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
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(KEYCHAIN_PASS);
        KeyChain passKeyChain = new KeyChain(passFile);
        assertTrue(passKeyChain.isLocked());
        assertTrue(passKeyChain.unlock(prompt));
        verify(prompt, times(1)).getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN);
        assertFalse(passKeyChain.isLocked());
    }

    public void testUnlockFileCancel() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(null);
        KeyChain passKeyChain = new KeyChain(passFile);
        assertTrue(passKeyChain.isLocked());
        assertFalse(passKeyChain.unlock(prompt));
        assertTrue(passKeyChain.isLocked());
        verify(prompt, times(1)).getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN);
    }

    public void testAliases() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(KEYCHAIN_PASS);
        KeyChain keyChain = new KeyChain(passFile);
        assertTrue(keyChain.isLocked());
        keyChain.unlock(prompt);
        verify(prompt, times(1)).getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN);
        assertFalse(keyChain.isLocked());

        Collection<String> aliases = keyChain.aliases();
        assertSame(2, aliases.size());
        assertTrue(aliases.contains("pass"));
        assertTrue(aliases.contains("nopass"));
    }

    public void testLockFile() throws Exception {
        KeyChain keyChain = getUnlockedKeyChain();
        assertFalse(keyChain.isLocked());
        keyChain.lock();
        assertTrue(keyChain.isLocked());
    }

    public void testInitExisting() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        verifyZeroInteractions(prompt);
        assertTrue(passFile.exists());
        KeyChain keyChain = new KeyChain(passFile);
        try {
            keyChain.init(prompt);
            fail();
        } catch (IOException e) {
            assertTrue(passFile.exists());
        }
    }

    public void testInit() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(true, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(KEYCHAIN_PASS);

        File newFile = File.createTempFile("irsshi", null);
        newFile.delete();
        newFile.deleteOnExit();
        KeyChain keyChain = new KeyChain(newFile);
        assertTrue(keyChain.init(prompt));
        verify(prompt, times(1)).getPassword(true, PromptPasswordCallback.PasswordType.KEYCHAIN);
        assertTrue(newFile.exists());
        newFile.delete();
        assertFalse(newFile.exists());
    }

    public void testInitCancel() throws Exception {
        File newFile = File.createTempFile("irsshi", null);
        newFile.delete();
        newFile.deleteOnExit();
        KeyChain keyChain = new KeyChain(newFile);
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(true, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(null);
        assertFalse(keyChain.init(prompt));
        verify(prompt, times(1)).getPassword(true, PromptPasswordCallback.PasswordType.KEYCHAIN);
        assertFalse(newFile.exists());
        newFile.delete();
    }

    public void testGetKeyPairLocked() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        verifyZeroInteractions(prompt);
        KeyChain lockedKeyChain = getLockedKeyChain();

        try {
            lockedKeyChain.getKeyPair("pass", prompt );
            fail();
        } catch (KeyStoreLockedException e) {
        }
    }

    public void testGetKeyPairCanceled() throws Exception {
        KeyChain keyChain = getUnlockedKeyChain();

        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEY)).thenReturn(null);
        try {
            keyChain.getKeyPair("pass", prompt);
            fail();
        } catch (KeyLockedException e) {
        }
    }

    public void testGetKeyPairWrongPassword() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(KEYCHAIN_PASS);

        KeyChain unlockedKeyChain = getUnlockedKeyChain();
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEY)).thenReturn("wrong");
        try {
            unlockedKeyChain.getKeyPair("pass", prompt);
            fail();
        } catch (KeyLockedException e) {
        }
    }

    public void testGetKeyPairPass() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEY)).thenReturn(KEY_PASS);

        KeyChain unlockedKeyChain = getUnlockedKeyChain();
        KeyPair keyPair = unlockedKeyChain.getKeyPair("pass", prompt);
        verify(prompt, times(1)).getPassword(false, PromptPasswordCallback.PasswordType.KEY);
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
    }

    public void testStoreKey() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(KEYCHAIN_PASS);
        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEY)).thenReturn(KEY_PASS);

        File newFile = File.createTempFile("irsshi", null);
        newFile.delete();
        newFile.deleteOnExit();
        KeyChain keyChain = new KeyChain(newFile);
        assertTrue(keyChain.init(prompt));
        verify(prompt, times(1)).getPassword(true, PromptPasswordCallback.PasswordType.KEYCHAIN);
        assertTrue(newFile.exists());

        KeyPair keyPair = KeyChain.generateKey(KeyChain.KeyType.RSA, 512);
        keyChain.store( "alias", keyPair );

        Collection<String> aliases = keyChain.aliases();
        assertSame(1, aliases.size());
        assertTrue(aliases.contains("alias"));

        newFile.delete();
        assertFalse(newFile.exists());
    }


}
