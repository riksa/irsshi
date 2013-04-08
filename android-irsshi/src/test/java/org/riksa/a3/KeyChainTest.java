/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import junit.framework.TestCase;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;


/**
 * User: riksa
 * Date: 3.4.2013
 * Time: 21:09
 */
public class KeyChainTest extends TestCase {
    private static final Logger log = LoggerFactory.getLogger(KeyChainTest.class);
    private static final String KEY_PASS = "password";

    public void testGenerateRSA() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getLockingPassword()).thenReturn(KEY_PASS);
        KeyPair keyPair = KeyChain.generateKey(KeyPair.RSA, 512, prompt);
        assertEquals(KeyPair.RSA, keyPair.getKeyType());
        verify(prompt, times(1)).getLockingPassword();
    }

    public void testGenerateUnknownAlgo() throws Exception {
        try {
            KeyChain.generateKey(12345, 512, null);
            fail();
        } catch (JSchException e) {
        }
    }

    public void testGenerateInvalidUnknown() throws Exception {
        try {
            KeyChain.generateKey(KeyPair.DSA, 5123, null);
            fail();
        } catch (JSchException e) {
        }
    }

    public void testGenerateDSA() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getLockingPassword()).thenReturn(KEY_PASS);
        KeyPair keyPair = KeyChain.generateKey(KeyPair.DSA, 512, prompt);
        assertEquals(KeyPair.DSA, keyPair.getKeyType());
        verify(prompt, times(1)).getLockingPassword();
    }

    public void testSaveLoadEncrypted() throws Exception {
        Path irsshi = Files.createTempDirectory("irsshi");
        KeyChain keyChain = new KeyChain(irsshi.toFile());
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getUnlockingPassword()).thenReturn(KEY_PASS);
        when(prompt.getLockingPassword()).thenReturn(KEY_PASS);
        KeyPair keyPair = KeyChain.generateKey(KeyPair.DSA, 512, prompt);
        keyChain.save("encrypted", keyPair, "comment");
        assertTrue(keyChain.aliases().contains("encrypted"));
        assertEquals(1, keyChain.aliases().size());

        KeyPair loaded = keyChain.load("encrypted", prompt);
        assertTrue(loaded.isEncrypted());
    }

    public void testSaveLoadUnencrypted() throws Exception {
        Path irsshi = Files.createTempDirectory("irsshi");
        KeyChain keyChain = new KeyChain(irsshi.toFile());
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getLockingPassword()).thenReturn("");
        KeyPair keyPair = KeyChain.generateKey(KeyPair.DSA, 512, prompt);
        keyChain.save("unencrypted", keyPair, "comment");
        assertTrue(keyChain.aliases().contains("unencrypted"));
        assertEquals(1, keyChain.aliases().size());

        KeyPair loaded = keyChain.load("unencrypted", prompt);
        assertFalse(loaded.isEncrypted());
        verify(prompt, times(1)).getLockingPassword();
    }

    public void testGenerateAsync() throws Exception {
        Path irsshi = Files.createTempDirectory("irsshi");
        KeyChain keyChain = new KeyChain(irsshi.toFile());
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getLockingPassword()).thenReturn("");
        when(prompt.getUnlockingPassword()).thenReturn("");
        KeyGeneratorCallback keyGeneratorCallback = mock(KeyGeneratorCallback.class);
        keyChain.generateKeyAsync(keyGeneratorCallback, prompt, "alias", KeyPair.RSA, 512, "comment");
        Thread.sleep(1000);
        verify(keyGeneratorCallback, times(1)).succeeded("alias");
    }

    public void testGenerateUnknownAlgoAsync() throws Exception {
        Path irsshi = Files.createTempDirectory("irsshi");
        KeyChain keyChain = new KeyChain(irsshi.toFile());
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getLockingPassword()).thenReturn("");
        when(prompt.getUnlockingPassword()).thenReturn("");
        KeyGeneratorCallback keyGeneratorCallback = mock(KeyGeneratorCallback.class);
        keyChain.generateKeyAsync(keyGeneratorCallback, prompt, "alias", 12345, 512, "comment");
        Thread.sleep(1000);
        verify(keyGeneratorCallback, times(1)).failed(matches("alias"), anyString());
    }

    public void testGenerateInvalidUnknownAsync() throws Exception {
        Path irsshi = Files.createTempDirectory("irsshi");
        KeyChain keyChain = new KeyChain(irsshi.toFile());
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getLockingPassword()).thenReturn("");
        when(prompt.getUnlockingPassword()).thenReturn("");
        KeyGeneratorCallback keyGeneratorCallback = mock(KeyGeneratorCallback.class);
        keyChain.generateKeyAsync(keyGeneratorCallback, prompt, "alias", KeyPair.DSA, 5121, "comment");
        Thread.sleep(1000);
        verify(keyGeneratorCallback, times(1)).failed(matches("alias"), anyString());
    }

    public void testUnwritable() throws Exception {
        KeyChain keyChain = new KeyChain(new File("ö:\\"));
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getLockingPassword()).thenReturn("");
        when(prompt.getUnlockingPassword()).thenReturn("");
        KeyGeneratorCallback keyGeneratorCallback = mock(KeyGeneratorCallback.class);
        keyChain.generateKeyAsync(keyGeneratorCallback, prompt, "alias", KeyPair.RSA, 512, "comment");
        Thread.sleep(1000);
        verify(keyGeneratorCallback, times(1)).failed(matches("alias"), anyString());
    }

    /*
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

    public void testStoreKeyPair() throws Exception {
        KeyPair keyPair = KeyChain.generateKey(KeyChain.KeyType.RSA, 512);
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());

        KeyChain keyChain = new KeyChain(null);

        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getPassword(true, PromptPasswordCallback.PasswordType.KEY)).thenReturn(KEY_PASS);
        keyChain.store("test", keyPair, prompt);

    }

    public void testPublicKeyAuth() throws Exception {
        // just to figure out how it works on Jsch...
        JSch jsch = new JSch();

        Session session = jsch.getSession("irsshi", "htpc-pc.local", 22);

        UserInfo ui = mock(UserInfo.class);
        when(ui.promptPassphrase(anyString())).thenReturn(true);
        when(ui.promptPassword(anyString())).thenReturn(false);
        when(ui.getPassphrase()).thenReturn("password");
        when(ui.promptYesNo(startsWith("The authenticity"))).thenReturn(true);
//        when(ui.promptYesNo(anyString())).then(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//                Object[] arguments = invocationOnMock.getArguments();
//                System.out.println("args: " + arguments[0]);
//                return Boolean.TRUE;
//            }
//        });

        session.setUserInfo(ui);
        String prvKey = "src/test/resources/keys/linux-generated/rsa_password";

        jsch.addIdentity(prvKey);

        session.connect();

        Channel channel = session.openChannel("shell");

        channel.setInputStream(System.in);
        channel.setOutputStream(System.out);

        channel.connect();
        Thread.sleep(5000);
    }

    public void testImportLinuxRsaPass() throws Exception {
        InputStream privateFile = getInputStream("/keys/linux-generated/rsa_nopass");
        InputStream publicFile = getInputStream("/keys/linux-generated/rsa_nopass.pub");
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        verifyZeroInteractions(prompt);
        KeyPair keyPair = KeyChain.readKeyPair(privateFile, publicFile, prompt);
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
    }

    private InputStream getInputStream(String path) throws FileNotFoundException {
        InputStream resourceAsStream = getClass().getResourceAsStream(path);
        if (resourceAsStream == null) {
            throw new FileNotFoundException(path);
        }
        return resourceAsStream;
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
//        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
//        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(KEYCHAIN_PASS);
//        KeyChain keyChain = new KeyChain(passFile);
//        assertTrue(keyChain.isLocked());
//        keyChain.unlock(prompt);
//        verify(prompt, times(1)).getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN);
//        assertFalse(keyChain.isLocked());
        KeyChain unlockedKeyChain = getUnlockedKeyChain();

        Collection<String> aliases = unlockedKeyChain.aliases();
        for (String alias : aliases) {
            log.debug("key alias={}", alias);
        }
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
            lockedKeyChain.getKeyPair("pass", prompt);
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
        } catch (UnrecoverableKeyException e) {
        }
    }

//    public void testGetKeyPairWrongPassword() throws Exception {
//        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
//        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(KEYCHAIN_PASS);
//
//        KeyChain unlockedKeyChain = getUnlockedKeyChain();
//        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEY)).thenReturn("wrong");
//        try {
//            unlockedKeyChain.getKeyPair("pass", prompt);
//            fail();
//        } catch (UnrecoverableKeyException e) {
//        }
//    }

//    public void testGetKeyPairPass() throws Exception {
//        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
//        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEY)).thenReturn(KEY_PASS);
//
//        KeyChain unlockedKeyChain = getUnlockedKeyChain();
//        Collection<String> aliases = unlockedKeyChain.aliases();
//        for( String alias : aliases ) {
//            log.debug( "key alias={}", alias );
//        }
//        KeyPair keyPair = unlockedKeyChain.getKeyPair("rsa2048-password", prompt);
//        verify(prompt, times(1)).getPassword(false, PromptPasswordCallback.PasswordType.KEY);
//        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
//    }

//    public void testStoreKeyPass() throws Exception {
//        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
//        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN)).thenReturn(KEYCHAIN_PASS);
//        when(prompt.getPassword(false, PromptPasswordCallback.PasswordType.KEY)).thenReturn(KEY_PASS);
//
//        File newFile = File.createTempFile("irsshi", null);
//        newFile.delete();
//        newFile.deleteOnExit();
//        KeyChain keyChain = new KeyChain(newFile);
//        assertTrue(keyChain.init(prompt));
//        verify(prompt, times(1)).getPassword(true, PromptPasswordCallback.PasswordType.KEYCHAIN);
//        assertTrue(newFile.exists());
//
//        KeyPair keyPair = KeyChain.generateKey(KeyChain.KeyType.RSA, 512);
//        keyChain.store("alias", keyPair, prompt);
//
//        Collection<String> aliases = keyChain.aliases();
//        assertSame(1, aliases.size());
//        assertTrue(aliases.contains("alias"));
//
//        newFile.delete();
//        assertFalse(newFile.exists());
//    }

*/
}
