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
import android.os.Bundle;
import android.widget.AnalogClock;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        final String tag = getIntent().getStringExtra(TerminalsActivity.EXTRA_NICKNAME);
        final Bundle args = new Bundle();
        bar.addTab(bar.newTab()
                .setText(tag)
                .setTabListener(new ActionBar.TabListener() {
                    @Override
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        log.debug("onTabSelected");
                        Bundle bundle = new Bundle();
                        bundle.putString("TAG", tab.getTag().toString() );
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

        // TODO: read from intent if we want to switch to some certain tab (opened a new tab)
        // TODO: sessions from service, create views...

    }
}