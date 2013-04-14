/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.riksa.a3;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    private final File path = new File(System.getProperty("user.dir"));

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

        KeyPair loaded = keyChain.load("encrypted");
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

        KeyPair loaded = keyChain.load("unencrypted");
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

    private File findFile(String filename) throws FileNotFoundException {
        File file = new File(path, filename);
        if (!file.exists())
            throw new FileNotFoundException(file.getAbsolutePath());
        return file;
    }

    public void testImportDsaNopass() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getUnlockingPassword()).thenReturn("");
        File privateFile = findFile("/linux-generated/dsa_nopass");
        File publicFile = findFile("/linux-generated/dsa_nopass.pub");
        KeyPair keyPair = KeyChain.load(privateFile.getAbsolutePath(), publicFile.getAbsolutePath());
        assertFalse(keyPair.isEncrypted());
        assertEquals(KeyPair.DSA, keyPair.getKeyType());
    }

    public void testImportDsaPass() throws Exception {
        File privateFile = findFile("/linux-generated/dsa_password");
        File publicFile = findFile("/linux-generated/dsa_password.pub");
        KeyPair keyPair = KeyChain.load(privateFile.getAbsolutePath(), publicFile.getAbsolutePath());
        assertTrue(keyPair.isEncrypted());
        assertEquals(KeyPair.DSA, keyPair.getKeyType());
        assertTrue(keyPair.decrypt(KEY_PASS));
    }

    public void testImportDsaWrongPass() throws Exception {
        File privateFile = findFile("/linux-generated/dsa_password");
        File publicFile = findFile("/linux-generated/dsa_password.pub");
        KeyPair keyPair = KeyChain.load(privateFile.getAbsolutePath(), publicFile.getAbsolutePath());
        assertTrue(keyPair.isEncrypted());
        assertEquals(KeyPair.DSA, keyPair.getKeyType());
        assertFalse(keyPair.decrypt("wrong"));
    }

    public void testImportRsaNopass() throws Exception {
        PromptPasswordCallback prompt = mock(PromptPasswordCallback.class);
        when(prompt.getUnlockingPassword()).thenReturn("");
        File privateFile = findFile("/linux-generated/rsa_nopass");
        File publicFile = findFile("/linux-generated/rsa_nopass.pub");
        KeyPair keyPair = KeyChain.load(privateFile.getAbsolutePath(), publicFile.getAbsolutePath());
        assertFalse(keyPair.isEncrypted());
        assertEquals(KeyPair.RSA, keyPair.getKeyType());
    }

    public void testImportRsaPass() throws Exception {
//        Path irsshi = Files.createTempDirectory("irsshi");
//        KeyChain keyChain = new KeyChain(irsshi.toFile());
        File privateFile = findFile("/linux-generated/rsa_password");
        File publicFile = findFile("/linux-generated/rsa_password.pub");
        KeyPair keyPair = KeyChain.load(privateFile.getAbsolutePath(), publicFile.getAbsolutePath());
        assertTrue(keyPair.isEncrypted());
        assertEquals(KeyPair.RSA, keyPair.getKeyType());
        assertTrue(keyPair.decrypt(KEY_PASS));
    }

    public void testImportPrivatePublicBytes() throws Exception {
        File privateFile = findFile("/linux-generated/rsa_password");
        byte[] privateBytes = IOUtils.toByteArray(new FileReader(privateFile));
        File publicFile = findFile("/linux-generated/rsa_password.pub");
        byte[] publicBytes = IOUtils.toByteArray(new FileReader(publicFile));
        KeyPair keyPair = KeyChain.load(privateBytes, publicBytes);
        assertTrue(keyPair.isEncrypted());
        assertEquals(KeyPair.RSA, keyPair.getKeyType());
        assertTrue(keyPair.decrypt(KEY_PASS));
    }

    public void testImportPrivateBytes() throws Exception {
        File privateFile = findFile("/linux-generated/rsa_password");
        byte[] privateBytes = IOUtils.toByteArray(new FileReader(privateFile));
        byte[] publicBytes = null;
        KeyPair keyPair = KeyChain.load(privateBytes, publicBytes);
        assertTrue(keyPair.isEncrypted());
        assertEquals(KeyPair.RSA, keyPair.getKeyType());
        assertTrue(keyPair.decrypt(KEY_PASS));
    }

}
