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
import org.riksa.irsshi.domain.TermHost;
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
    public static final String EXTRA_HOST_ID = "host_id";

//    private Handler handler;

    /*
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
                  */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        long hostId = intent.getLongExtra(EXTRA_HOST_ID, -1);
        log.debug("onCreate {}", hostId);
        addHostTab(hostId);
    }

    private void addHostTab(final long hostId) {
        log.debug("addHostTab {}", hostId);
        if (hostId != -1) {
//            IrsshiApplication.getIrsshiService().connectToHostById(this, hostId);

            ActionBar actionBar = getActionBar();
            TermHost termHost = IrsshiApplication.getIrsshiService().getTermHostDao().findHostById(hostId);

            final String tag = "termHost";
            final String title = termHost.getNickName();
            actionBar.addTab(actionBar.newTab()
                    .setTag(tag)
                    .setText(title)
                    .setTabListener(new ActionBar.TabListener() {
                        @Override
                        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                            log.debug("onTabSelected");
                            Bundle bundle = new Bundle();
                            bundle.putLong(EXTRA_HOST_ID, hostId);
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
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        long hostId = getIntent().getLongExtra(EXTRA_HOST_ID, -1);
        log.debug("onCreate {}", hostId);
        addHostTab(hostId);

    }

}