/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
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
    private static IrsshiApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.
        instance = this;

        termHostDao = new ContentProviderTermHostDao(getApplicationContext());
        startService(new Intent(this, IrsshiService.class));

    }

    // TODO: ger rid of this coupling... singleton or something
    public TermHostDao getTermHostDao() {
        return termHostDao;
    }

    public static IrsshiApplication getInstance() {
        return instance;
    }
}
