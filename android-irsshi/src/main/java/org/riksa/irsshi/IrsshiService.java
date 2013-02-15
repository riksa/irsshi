/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.EditText;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.emulatorview.UpdateCallback;
import jackpal.androidterm.util.SessionList;
import jackpal.androidterm.util.TermSettings;
import org.riksa.irsshi.domain.ConnectionInfo;
import org.riksa.irsshi.domain.ConnectionStateChangeListener;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.JSchLogger;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 20:25
 */
public class IrsshiService extends Service {
    private static final Logger log = LoggerFactory.getLogger(IrsshiService.class);
    private final IBinder mBinder = new LocalBinder();
    private TermHostDao termHostDao;
    private Handler handler;
    private static final List<ConnectionInfo> connections = new LinkedList<ConnectionInfo>();

    // TODO: delegate
    public TermHostDao getTermHostDao() {
        return termHostDao;
    }

//    public SessionList getSessions() {
//        return sessions;
//    }

//    private SessionList sessions = new SessionList();

    public void connectToHostById(final Activity activity, long hostId, final ConnectionStateChangeListener connectionStateChangeListener) {
        final TermHost host = termHostDao.findHostById(hostId);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TermSettings termSettings = new TermSettings(getResources(), preferences);
//        TermSession session = new SshTermSession(host, userInfo, termSettings);
        final TermSession termSession = new TermSession();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final ConnectionInfo connectionInfo = new ConnectionInfo( ConnectionInfo.HostState.CONNECTING, termSession, host, connectionStateChangeListener );
                connections.add( connectionInfo );
                JSchLogger logger = new JSchLogger();
                JSch.setLogger(logger);

                try {
                    final Object promptMutex = new Object();
                    JSch jsch = new JSch();
                    JSch.setConfig("StrictHostKeyChecking", "no");

                    Session session = jsch.getSession(host.getUserName(), host.getHostName(), host.getPort());

                    // username and password will be given via UserInfo interface.
                    //            UserInfo ui = new TestUserInfo();
                    UserInfo userInfo = new UserInfo() {
                        private String password;
                        boolean ret = false;
                        boolean waiting = true;

                        @Override
                        public String getPassphrase() {
//            Toast.makeText(getApplicationContext(), "getPassphrase", Toast.LENGTH_LONG).show();
                            return "";  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public String getPassword() {
//            Toast.makeText(getApplicationContext(), "getPassword", Toast.LENGTH_LONG).show();
                            return password;  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public boolean promptPassword(final String s) {
                            synchronized (promptMutex) {
                                waiting = true;
                                password = null;
                                ret = false;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        final EditText editText = new EditText(activity);
//                editText.setInputType( 0 );
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setTitle(R.string.dialog_password_title);
                                        builder.setView(editText);
                                        builder.setMessage(s);
                                        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                log.debug("OK");
                                                waiting = false;
                                                ret = true;
                                                password = editText.getText().toString();
                                            }
                                        };
                                        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                log.debug("Cancel");
                                                waiting = false;
                                                ret = false;
                                            }
                                        };
                                        builder.setPositiveButton(R.string.dialog_password_ok, okListener);
                                        builder.setNegativeButton(R.string.dialog_password_cancel, cancelListener);
                                        builder.create().show();
                                    }
                                });
                                while (waiting) {
                                    Thread.yield();
                                }
                            }

                            log.debug( "Password entered {}", ret);
                            return ret;  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public boolean promptPassphrase(String s) {
//            Toast.makeText(getApplicationContext(), "promptPassphrase", Toast.LENGTH_LONG).show();
                            return false;
                        }

                        @Override
                        public boolean promptYesNo(String s) {
                            return true;  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public void showMessage(String s) {
                        }

                    };

                    session.setUserInfo(userInfo);

                    session.connect();

                    Channel channel = session.openChannel("shell");

                    //            channel.setInputStream( getTermIn() );
                    //            channel.setOutputStream( getTermOut() );
                    termSession.setTermOut(channel.getOutputStream());
                    termSession.setTermIn(channel.getInputStream());

                    channel.connect();
//                    notifyUpdate();
                    termSession.setTitle(host.getNickName());
                    connectionInfo.setHostState(ConnectionInfo.HostState.CONNECTED);
                    termSession.setFinishCallback( new TermSession.FinishCallback() {
                        @Override
                        public void onSessionFinish(TermSession session) {
                            connectionInfo.setHostState(ConnectionInfo.HostState.DISCONNECTED);
                        }
                    });

                } catch (Exception e) {
                    logger.log(com.jcraft.jsch.Logger.ERROR, e.getMessage());
                    connectionInfo.setHostState(ConnectionInfo.HostState.DISCONNECTED);
                }
            }
        };

        new Thread(runnable).start();
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
        handler = new Handler();
        termHostDao = new ContentProviderTermHostDao(this);
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
