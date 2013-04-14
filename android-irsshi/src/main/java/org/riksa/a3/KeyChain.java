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

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.Security;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: riksa
 * Date: 18.10.2012
 * Time: 18:33
 */
public class KeyChain {
    private final File rootDirectory;
    private ExecutorService executorService;
    private static JSch jsch;

    private static final Logger log = LoggerFactory.getLogger(KeyChain.class);

    public KeyChain(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        log.debug("Storing keys in {}", rootDirectory.getAbsolutePath());
    }

    static {
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
    }

    public List<String> aliases() {
        String[] list = rootDirectory.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return !s.endsWith(".pub");
            }
        });
        return Arrays.asList(list);
    }

    /**
     * Set passphrase prior to save (if needed)
     *
     * @param alias
     * @param keyPair
     */
    public void save(String alias, KeyPair keyPair, String comment) throws IOException {
        keyPair.writePrivateKey(fileForAlias(alias, true));
        keyPair.writePublicKey(fileForAlias(alias, false), comment);
    }

    private String fileForAlias(String alias, boolean privateKey) {
        final String privateName = new File(rootDirectory, alias).getAbsolutePath();
        if (privateKey) {
            return privateName;
        } else {
            return privateName + ".pub";
        }
    }

    public static KeyPair load(byte[] privateBytes, byte[] publicBytes) throws JSchException {
        return KeyPair.load(getJsch(), privateBytes, publicBytes);
    }

    public static KeyPair load(String privateFile, String publicFile) throws JSchException {
        return KeyPair.load(getJsch(), privateFile, publicFile);
    }

    public KeyPair load(String alias) throws JSchException {
        return load(fileForAlias(alias, true), fileForAlias(alias, false));
    }

    public static KeyPair generateKey(final int keyType, final int keyBits, PromptPasswordCallback passwordCallback) throws JSchException {
        KeyPair keyPair = KeyPair.genKeyPair(getJsch(), keyType, keyBits);
        if (keyPair == null) {
            throw new JSchException();
        }
        String lockingPassword = passwordCallback.getLockingPassword();
        if (lockingPassword != null) {
            keyPair.setPassphrase(lockingPassword);
        }
        return keyPair;
    }

    public void generateKeyAsync(final KeyGeneratorCallback keyGeneratorCallback, final PromptPasswordCallback passwordCallback, final String alias, final int keyType, final int keyBits, final String comment) {
        ExecutorService executorService = getExecutorService();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    KeyPair keyPair = generateKey(keyType, keyBits, passwordCallback);
                    save(alias, keyPair, comment);
                    keyPair.dispose();
                    keyGeneratorCallback.succeeded(alias);
                } catch (JSchException e) {
                    log.error("" + e.getMessage());
                    keyGeneratorCallback.failed(alias, e.getMessage());
                } catch (IOException e) {
                    log.error(e.getMessage());
                    keyGeneratorCallback.failed(alias, e.getMessage());
                }

            }
        });

    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(2);
        }
        return executorService;
    }

    public static JSch getJsch() {
        if (jsch == null) {
            jsch = new JSch();
        }
        return jsch;
    }
}
