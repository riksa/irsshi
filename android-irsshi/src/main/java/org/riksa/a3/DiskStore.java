/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * User: riksa
 * Date: 21.10.2012
 * Time: 18:50
 */
public class DiskStore implements KeyStoreStore {
    private static final String JKS_TYPE = "BKS";
    private static final String KEYSTORE_FILE = "/sdcard/password.bks";
    private static final Logger log = LoggerFactory.getLogger(DiskStore.class);
    private KeyStore keystore;
    //    private Context context;
    private File keystoreFile;

    static {
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
    }

    public DiskStore() {
        super();
        keystoreFile = new File(KEYSTORE_FILE);
    }

    public void save(PromptPasswordCallback promptSavePassword) throws KeyStoreException, IOException {
        if (isLocked()) {
            throw new IOException();
        }

        try {
            /*
            KeyStore.LoadStoreParameter pp = new KeyStore.LoadStoreParameter() {
                public KeyStore.ProtectionParameter getProtectionParameter() {
                    String password = promptSavePassword.getPassword();
                    final KeyStore.PasswordProtection saveStoreParameter = new KeyStore.PasswordProtection( password.toCharArray() );
                    return saveStoreParameter;
                }
            };
            keystore.store(pp);
            */
            keystore.store(new FileOutputStream(keystoreFile), promptSavePassword.getPassword(true, PromptPasswordCallback.PasswordType.KEYCHAIN).toCharArray());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new KeyStoreException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            throw new KeyStoreException(e.getMessage());
        } catch (CertificateException e) {
            log.error(e.getMessage(), e);
            throw new KeyStoreException(e.getMessage());
        }
    }

    public void unlock(PromptPasswordCallback promptUnlockPassword) throws KeyStoreException, IOException {
        String password = promptUnlockPassword.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN);
        final KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(password.toCharArray());

        if (keystoreFile.exists()) {
            log.debug("Keystore {} exists", keystoreFile.getAbsolutePath());
            KeyStore.Builder builder = KeyStore.Builder.newInstance(JKS_TYPE, new org.spongycastle.jce.provider.BouncyCastleProvider(), keystoreFile, pp);
            keystore = builder.getKeyStore();
            Enumeration<String> aliases = keystore.aliases();
//            keys.clear();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
//                keys.add(new A3Key(alias, null));
                log.debug("Alias {}", alias);
                /*
                log.debug("Alias {}", alias);
                try {
                    Key key = keystore.getKey(alias, "password".toCharArray());
                    log.debug("Key {}", key);
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getMessage(), e );
                } catch (UnrecoverableKeyException e) {
                    log.error(e.getMessage(), e);
                }
                try {
                    Key key = keystore.getKey(alias, null);
                    log.debug("Key {}", key);
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getMessage(), e );
                } catch (UnrecoverableKeyException e) {
                    log.error(e.getMessage(), e);
                } */
            }
        } else {
            log.debug("Keystore {} does not exist", keystoreFile.getAbsolutePath());
            KeyStore.Builder builder = KeyStore.Builder.newInstance(JKS_TYPE, new org.spongycastle.jce.provider.BouncyCastleProvider(), pp);
            keystore = builder.getKeyStore();
        }
    }

    public void lock() {
        keystore = null;
    }

    public A3Key getKeyWithAlias(String alias) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        keystore.getKey(alias, null);
        return new A3KeyImpl(alias);
    }

    public boolean addKey(A3Key key, KeyPair keyPair) throws KeyStoreException {
        log.error("TODO: addKey");
        return false;
    }

    public boolean removeKey(String key) throws KeyStoreException {
        log.error("TODO: removeKey");
        return false;
    }

    public List<A3Key> getUnmodifiableKeys() throws KeyStoreException {
        List<A3Key> keys = new ArrayList<A3Key>();
        Enumeration<String> aliases = keystore.aliases();
//            keys.clear();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            keys.add(new A3KeyImpl(alias));
        }
        return Collections.unmodifiableList(keys);
    }

    public boolean isLocked() {
        return keystore == null;
    }
/*
    public boolean exists() {
        return keystoreFile.exists();
    }

    public void initialize(PromptPasswordCallback promptSavePassword) throws FileNotFoundException, KeyStoreException {
        if (exists()) {
            return;
        }

        keystore = KeyStore.getInstance(JKS_TYPE, new org.spongycastle.jce.provider.BouncyCastleProvider());
        try {
            keystore.store(new FileOutputStream(keystoreFile), promptSavePassword.getPassword().toCharArray());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new KeyStoreException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            throw new KeyStoreException(e.getMessage());
        } catch (CertificateException e) {
            log.error(e.getMessage(), e);
            throw new KeyStoreException(e.getMessage());
        }
    }
    */

    private class A3KeyImpl implements A3Key {
        private String alias;

        public A3KeyImpl(String alias) {
            this.alias = alias;
        }

        public String getAlias() {
            return alias;
        }

        public Type getKeyType() {
            return Type.UNKNOWN;
        }

        public int getKeyStrength() {
            return 666;
        }

        public boolean isLocked() {
            return true;
        }

        public boolean lock() {
            return false;
        }

        public boolean unlock(char[] password) {
            return false;
        }
    }
}
