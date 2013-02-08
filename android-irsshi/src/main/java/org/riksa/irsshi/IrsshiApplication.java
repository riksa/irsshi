/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
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
        Collection<TermHost> mockHosts = new ArrayList<TermHost>();

//        mockHosts.add(new SshTermHost("some.host.ssh", "someone"));
//        mockHosts.add(new SshTermHost("another.host.ssh", "someoneelse", 2222));
//        mockHosts.add(new MoshTermHost("some.host.mosh", "foomosh"));
//        mockHosts.add(new MoshTermHost("another.host.mosh", "barmosh", 1111));
//        mockHosts.add(new LocalTermHost());
//        mockHosts.add(new LocalTermHost("android"));

//        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        for (TermHost termHost : mockHosts) {
            termHostDao.insertHost(termHost);

//            ContentValues contentValues = new ContentValues();
//            contentValues.put(HostProviderMetaData.HostTableMetaData.NICKNAME, termHost.getNickName());
//            contentValues.put(HostProviderMetaData.HostTableMetaData.HOSTNAME, termHost.getHostName());
//            contentValues.put(HostProviderMetaData.HostTableMetaData.TYPE, termHost.getHostType().name());
//            contentValues.put(HostProviderMetaData.HostTableMetaData.PORT, termHost.getPort());
//            contentValues.put(HostProviderMetaData.HostTableMetaData.USERNAME, termHost.getUserName());
//
//            Uri uri = contentResolver.insert(HostProviderMetaData.HostTableMetaData.CONTENT_URI, contentValues);
//            log.debug("Inserted {}", uri);
//
        }

    }

    public TermHostDao getTermHostDao() {
        return termHostDao;
    }

    public static IrsshiApplication getInstance() {
        return instance;
    }
}
