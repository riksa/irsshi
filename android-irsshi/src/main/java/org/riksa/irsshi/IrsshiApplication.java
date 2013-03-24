/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import android.app.Application;
import android.content.*;
import android.net.Uri;
import android.os.IBinder;
import android.os.StrictMode;
import org.riksa.irsshi.domain.LocalTermHost;
import org.riksa.irsshi.domain.MoshTermHost;
import org.riksa.irsshi.domain.SshTermHost;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.provider.HostProviderMetaData;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 23:15
 */
public class IrsshiApplication extends Application {
    private static final Logger log = LoggerFactory.getLogger(IrsshiApplication.class);
    private TermHostDao termHostDao;
    private static IrsshiService irsshiService;

    public static IrsshiService getIrsshiService() {
        return irsshiService;
    }

    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.

        startService(new Intent(this, IrsshiService.class));

        bindService(new Intent(this, IrsshiService.class), new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                irsshiService = ((IrsshiService.LocalBinder) iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                irsshiService = null;
            }
        }, Context.BIND_AUTO_CREATE);

    }

    public static boolean isFirstLaunch() {
        return true;
    }
}
