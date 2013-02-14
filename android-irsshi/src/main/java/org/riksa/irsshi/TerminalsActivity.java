/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.AnalogClock;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.emulatorview.UpdateCallback;
import jackpal.androidterm.util.SessionList;
import org.riksa.irsshi.fragment.TerminalTabFragment;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 8.2.2013
 * Time: 20:20
 */
public class TerminalsActivity extends Activity {
    private static final Logger log = LoggerFactory.getLogger(TerminalsActivity.class);

    @Deprecated
    public static final String EXTRA_USERNAME = "username";

    @Deprecated
    public static final String EXTRA_HOSTNAME = "hostname";

    @Deprecated
    public static final String EXTRA_PORT = "port";

    @Deprecated
    public static final String EXTRA_NICKNAME = "nickname";

    private IrsshiService irsshiService = null;
    private Handler handler;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            irsshiService = ((IrsshiService.LocalBinder) iBinder).getService();
            irsshiService.getSessions().addCallback( new UpdateCallback() {
                @Override
                public void onUpdate() {
                    log.debug("onUpdate");
                    handler.sendEmptyMessage( 1 );
                }
            });

            debugUpdateTabs();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            irsshiService = null;
        }
    };

    private void debugUpdateTabs() {
        final ActionBar bar = getActionBar();
        bar.removeAllTabs();

        if (irsshiService != null) {
            SessionList sessions = irsshiService.getSessions();
            int idx = 0;  // TODO: BAH
            for (TermSession termSession : sessions) {
                log.debug("session: {} : {}", termSession.getTitle(), termSession );
                final String tag = "termsession";
                final String title = termSession.getTitle();
                final int c = idx;
                bar.addTab(bar.newTab()
                        .setTag(tag)
                        .setText(title)
                        .setTabListener(new ActionBar.TabListener() {
                            @Override
                            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                                log.debug("onTabSelected");
                                Bundle bundle = new Bundle();
                                bundle.putString( "title", title );
                                bundle.putInt("idx", c );
                                Fragment fragment = Fragment.instantiate(TerminalsActivity.this, TerminalTabFragment.class.getName(), bundle);
                                fragmentTransaction.add(android.R.id.content, fragment, tag);
                            }

                            @Override
                            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                                log.debug("onTabUnselected");
                            }

                            @Override
                            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                                log.debug("onTabReselected");
                            }
                        }));
                idx ++;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                debugUpdateTabs();
  /*
                session.write("Connecting...");

                session.setFinishCallback(new TermSession.FinishCallback() {
                    @Override
                    public void onSessionFinish(TermSession session) {
                        log.debug("onSessionFinish");
                    }
                });
                session.setUpdateCallback(new UpdateCallback() {
                    @Override
                    public void onUpdate() {
                    }
                });
*/
            }
        };

            }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        bindService(new Intent(this, IrsshiService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        debugUpdateTabs();
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.

        if (irsshiService != null) {
            unbindService(serviceConnection);
        }
    }
}