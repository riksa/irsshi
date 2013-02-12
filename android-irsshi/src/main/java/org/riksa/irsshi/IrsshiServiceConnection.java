/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 11.2.2013
 * Time: 21:21
 */
public class IrsshiServiceConnection implements ServiceConnection {
    private static final Logger log = LoggerFactory.getLogger(IrsshiServiceConnection.class);

    private IrsshiService irsshiService = null;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        log.debug("onServiceConnected");
        irsshiService = ((IrsshiService.LocalBinder) iBinder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        log.debug("onServiceDisconnected");
        irsshiService = null;
    }

    public boolean isBound() {
        return irsshiService != null;
    }

    public IrsshiService getIrsshiService() {
        return irsshiService;
    }


}
