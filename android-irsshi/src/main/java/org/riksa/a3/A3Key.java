/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

/**
 * User: riksa
 * Date: 10/18/12
 * Time: 2:55 PM
 */

public interface A3Key {
    public enum Type {RSA, DSA, UNKNOWN};

    public String getAlias();
    public Type getKeyType();
    public int getKeyStrength();
    public boolean isLocked();
    public boolean lock();
    public boolean unlock(char[] password);
}
