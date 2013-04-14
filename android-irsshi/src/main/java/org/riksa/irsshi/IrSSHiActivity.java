/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.riksa.irsshi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.fragment.HostListFragment;
import org.riksa.irsshi.fragment.TerminalTabFragment;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 14.4.2013
 * Time: 10:25
 */
public class IrSSHiActivity extends Activity {
    public static final String EXTRA_HOST_ID = "host_id";
    private static final Logger log = LoggerFactory.getLogger(IrSSHiActivity.class);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        addHostListTab();
    }

    private void addHostListTab() {
        ActionBar actionBar = getActionBar();
        final String tag = "hostList";
        actionBar.addTab(actionBar.newTab()
                .setTag(tag)
                .setText(R.string.host_list_title)
                .setTabListener(new ActionBar.TabListener() {
                    Fragment fragment;

                    @Override
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        if (fragment == null) {
                            Bundle bundle = new Bundle();
                            fragment = Fragment.instantiate(IrSSHiActivity.this, HostListFragment.class.getName(), bundle);
                            fragmentTransaction.add(android.R.id.content, fragment, tag);
                        } else {
                            fragmentTransaction.attach(fragment);
                        }
                    }

                    @Override
                    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        fragmentTransaction.detach(fragment);
                    }

                    @Override
                    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        log.debug("onTabReselected");
                    }
                }));
    }

    public void connectToHost(long hostId) {
        ActionBar.Tab tab = addHostTab(hostId);
        if (tab != null) {
            getActionBar().selectTab(tab);
        }
    }

    private ActionBar.Tab addHostTab(final long hostId) {
        log.debug("addHostTab {}", hostId);
        if (hostId == -1) {
            return null;
        }

        ActionBar actionBar = getActionBar();
        TermHost termHost = IrsshiApplication.getIrsshiService().getTermHostDao().findHostById(hostId);

        final String tag = "termHost";
        final String title = termHost.getNickName();
        ActionBar.Tab tab = actionBar.newTab()
                .setTag(tag)
                .setText(title)
                .setTabListener(new ActionBar.TabListener() {
                    Fragment fragment;

                    @Override
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        log.debug("onTabSelected");
                        if (fragment == null) {
                            Bundle bundle = new Bundle();
                            bundle.putLong(EXTRA_HOST_ID, hostId);
                            fragment = Fragment.instantiate(IrSSHiActivity.this, TerminalTabFragment.class.getName(), bundle);
                            fragmentTransaction.add(android.R.id.content, fragment, tag);
                        } else {
                            fragmentTransaction.attach(fragment);
                        }
                    }

                    @Override
                    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        fragmentTransaction.detach(fragment);
                    }

                    @Override
                    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        log.debug("onTabReselected");
                    }
                });
        actionBar.addTab(tab);
        return tab;
    }

}