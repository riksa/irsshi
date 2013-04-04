/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;
import org.spongycastle.jce.provider.X509CertificateObject;
import org.spongycastle.util.io.pem.PemWriter;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;

/**
 * User: riksa
 * Date: 18.10.2012
 * Time: 18:33
 */
public class KeyChain {
    private static final String JKS_TYPE = "BKS";
    private static final Logger log = LoggerFactory.getLogger(KeyChain.class);
    private final File keystoreFile;
    private KeyStore keystore;

    public KeyChain(File keystoreFile) {
        this.keystoreFile = keystoreFile;
    }

    public boolean isLocked() {
        return keystore == null;
    }

    static {
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
    }

    public Collection<String> aliases() throws KeyStoreException {
        Enumeration<String> aliases = keystore.aliases();
        Collection<String> ret = new LinkedList<String>();
        while (aliases.hasMoreElements()) {
            ret.add(aliases.nextElement());
        }
        return ret;
    }

    public void store(String alias, KeyPair keyPair, PromptPasswordCallback callback) throws KeyStoreLockedException {
        if (isLocked()) {
            throw new KeyStoreLockedException();
        }

        String password = callback.getPassword(true, PromptPasswordCallback.PasswordType.KEY);
        if (password != null) {
            final KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(password.toCharArray());
            Certificate[] certificateChain;
//            keystore.setKeyEntry(alias, key, password.toCharArray(), certificateChain );
        }
    }

    public KeyPair getKeyPair(String alias, PromptPasswordCallback callback) throws KeyStoreLockedException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {
        if (isLocked()) {
            throw new KeyStoreLockedException();
        }

        String password = callback.getPassword(false, PromptPasswordCallback.PasswordType.KEY);
        if (password == null) {
            throw new UnrecoverableKeyException();
        }

        final KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(password.toCharArray());
//        KeyStore.Entry entry = keystore.getEntry(alias, pp);
//        log.debug("class={}", entry.getClass());
        X509CertificateObject certificate = (X509CertificateObject) keystore.getCertificate(alias);
//        KeyStore.Entry entry = keystore.getEntry(alias, pp);
            log.debug("class={}", certificate.getPublicKey().getEncoded() );
//            PemWriter pemWriter = new PemWriter(new PrintWriter(System.out));
//            X509Certificate cert = null;
//            pemWriter.( cert );
        return null;
    }

    public enum KeyType {RSA, DSA}

    //    private Collection<KeyChainListener> listeners = new HashSet<KeyChainListener>();
//    private ExecutorService executorService;
//    private KeyStoreStore keystoreStore;
//    private ArrayList<A3Key> unfinishedKeys = new ArrayList<A3Key>();

    public static KeyPair generateKey(final KeyType keyType, final int keyBits) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType.name());
            keyPairGenerator.initialize(keyBits, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            log.debug("Public key format {}", keyPair.getPublic().getFormat());
            log.debug("Private key format {}", keyPair.getPrivate().getFormat());
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
//            keystore.store(new FileOutputStream(keystoreFile), promptSavePassword.getPassword().toCharArray());

    public boolean init(PromptPasswordCallback callback) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        if (keystoreFile.exists()) {
            throw new IOException(keystoreFile.getAbsolutePath());
        }

        String password = callback.getPassword(true, PromptPasswordCallback.PasswordType.KEYCHAIN);
        if (password != null) {
            final KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(password.toCharArray());
            KeyStore.Builder builder = KeyStore.Builder.newInstance(JKS_TYPE, new org.spongycastle.jce.provider.BouncyCastleProvider(), pp);
            keystore = builder.getKeyStore();
            keystore.store(new FileOutputStream(keystoreFile), password.toCharArray());
            return true;
        }
        return false;
    }

    public boolean unlock(PromptPasswordCallback callback) throws IOException, KeyStoreException {
        if (!keystoreFile.exists()) {
            throw new FileNotFoundException(keystoreFile.getAbsolutePath());
        }

        if (isLocked()) {
            String password = callback.getPassword(false, PromptPasswordCallback.PasswordType.KEYCHAIN);
            if (password != null) {
                final KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(password.toCharArray());
                KeyStore.Builder builder = KeyStore.Builder.newInstance(JKS_TYPE, new org.spongycastle.jce.provider.BouncyCastleProvider(), keystoreFile, pp);
                keystore = builder.getKeyStore();
                return true;
            }

        }
        return false;
    }

    public void lock() {
        keystore = null;
    }


    public void generateKeyAsync(final PromptPasswordCallback passwordCallback, final String keyName, final String keyType, final int keyBits) {
        throw new RuntimeException("TODO");
    }



    /*
    public KeyChain(final Context context) throws KeyStoreException {
        keystoreStore = new DiskStore(context);
    }

    public PublicKey getPublicKey(String alias) {
        log.error("TODO: getPublicKey");
        return null;
    }

    public void unlock(PromptPasswordCallback passwordCallback) throws IOException, KeyStoreException {
        keystoreStore.unlock(passwordCallback);
        notifyKeysChanged();
    }

    public void lock() {
        keystoreStore.lock();
        notifyKeysChanged();
    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }

    public boolean isLocked() {
        return keystoreStore.isLocked();
    }

    public void generateKeyAsync(final PromptPasswordCallback passwordCallback, final String keyName, final String keyType, final int keyBits) {
        ExecutorService executorService = getExecutorService();

        final String password = passwordCallback.getPassword(true, PromptPasswordCallback.PasswordType.KEYCHAIN);
        if (password != null) {
            executorService.execute(new Runnable() {
                public void run() {
                    A3Key.Type type = A3Key.Type.valueOf(keyType);
                    A3Key key = new A3KeyImpl(keyName, type, keyBits);
                    unfinishedKeys.add(key);
                    log.debug("Generating key {}", keyName);
                    try {
                        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType);
                        keyPairGenerator.initialize(keyBits, new SecureRandom());
                        KeyPair keyPair = keyPairGenerator.genKeyPair();
                        log.debug("Public key format {}", keyPair.getPublic().getFormat());
                        log.debug("Private key format {}", keyPair.getPrivate().getFormat());
                        keystoreStore.addKey(key, keyPair);
                        keystoreStore.save(new PromptPasswordCallback() {
                            public String getPassword(boolean lock, PasswordType passwordType) {
                                return password;
                            }
                        });
                        notifyKeysChanged();
                        unfinishedKeys.remove(key);
                    } catch (NoSuchAlgorithmException e) {
                        log.error(e.getMessage(), e);
                        removeKey(key.getAlias());
                    } catch (KeyStoreException e) {
                        log.error(e.getMessage(), e);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                    log.debug("Generation of key {} finished", keyName);
                }
            });
        }

    }

    public void addKey(A3Key key, KeyPair keyPair) {
        try {
            if (keystoreStore.addKey(key, keyPair)) {
                notifyKeysChanged();
            }
        } catch (KeyStoreException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void removeKey(String key) {
        try {
            if (keystoreStore.removeKey(key)) {
                notifyKeysChanged();
            }
        } catch (KeyStoreException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void addListener(KeyChainListener listener) {
        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    public void removeListener(KeyChainListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private void notifyKeysChanged() {
        log.debug("notifyKeysChanged");
        for (KeyChainListener keyChainListener : listeners) {
            keyChainListener.keyChainChanged();
        }

    }

    public List<A3Key> getUnmodifiableKeys() throws KeyStoreException {
        return keystoreStore.getUnmodifiableKeys();
    }

    public interface KeyChainListener {
        void keyChainChanged();
    }

    class A3KeyImpl implements A3Key {

        private String alias;
        private Type keyType;
        private int keyStrength;

        A3KeyImpl(String alias, Type keyType, int keyStrength) {
            this.alias = alias;
            this.keyType = keyType;
            this.keyStrength = keyStrength;
        }

        public PublicKey getPublicKey() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getAlias() {
            return alias;
        }

        public Type getKeyType() {
            return keyType;
        }

        public int getKeyStrength() {
            return keyStrength;
        }

        public boolean isLocked() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean lock() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean unlock(char[] password) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    */
}
