/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.jcraft.jsch.UserInfo;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.util.SessionList;
import jackpal.androidterm.util.TermSettings;
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
    private TermHostDao termHostDao = IrsshiApplication.getInstance().getTermHostDao();

    public SessionList getSessions() {
        return sessions;
    }

    private SessionList sessions = new SessionList();

    UserInfo userInfo = new UserInfo() {
        @Override
        public String getPassphrase() {
//            Toast.makeText(getApplicationContext(), "getPassphrase", Toast.LENGTH_LONG).show();
            return "irsshi";  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getPassword() {
//            Toast.makeText(getApplicationContext(), "getPassword", Toast.LENGTH_LONG).show();
            return "irsshi";  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean promptPassword(String s) {
//            Toast.makeText(getApplicationContext(), "promptPassword", Toast.LENGTH_LONG).show();
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean promptPassphrase(String s) {
//            Toast.makeText(getApplicationContext(), "promptPassphrase", Toast.LENGTH_LONG).show();
            return true;
        }

        @Override
        public boolean promptYesNo(String s) {
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void showMessage(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    };

    public void connectToHostById(long hostId) {
        TermHost host = termHostDao.findHostById(hostId);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TermSettings termSettings = new TermSettings(getResources(), preferences);
        TermSession session = new SshTermSession(host, userInfo, termSettings);
        session.setTitle( host.getNickName() );
        sessions.add( session );
    }

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
