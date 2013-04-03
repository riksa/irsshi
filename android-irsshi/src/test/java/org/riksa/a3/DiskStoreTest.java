/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3;

import junit.framework.TestCase;

/**
 * User: riksa
 * Date: 3.4.2013
 * Time: 20:33
 */
public class DiskStoreTest extends TestCase {
    private int x = 1;
    private int y = 1;

    public void testAddition() {
        DiskStore diskStore = new DiskStore();
        int z = x + y;
        assertEquals(2, z);
    }

}
