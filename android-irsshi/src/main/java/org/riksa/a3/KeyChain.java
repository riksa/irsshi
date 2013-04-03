/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * User: riksa
 * Date: 18.10.2012
 * Time: 18:33
 */
public class KeyChain {
    private static final Logger log = LoggerFactory.getLogger(KeyChain.class);

    public enum KeyType {RSA, DSA}

    //    private Collection<KeyChainListener> listeners = new HashSet<KeyChainListener>();
//    private ExecutorService executorService;
//    private KeyStoreStore keystoreStore;
//    private ArrayList<A3Key> unfinishedKeys = new ArrayList<A3Key>();

    public KeyPair generateKey(final KeyType keyType, final int keyBits) {
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

    public void unlock(PromptPasswordCallback callback) {
        throw new RuntimeException("TODO");
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
