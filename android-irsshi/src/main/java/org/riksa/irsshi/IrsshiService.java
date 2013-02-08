/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import org.riksa.irsshi.domain.LocalTermHost;
import org.riksa.irsshi.domain.MoshTermHost;
import org.riksa.irsshi.domain.SshTermHost;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 20:25
 */
public class IrsshiService extends Service {
    private static final Logger log = LoggerFactory.getLogger(IrsshiService.class);
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public IrsshiService getService() {
            return IrsshiService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.
        log.debug("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        log.debug("onDestroy");
    }

}
