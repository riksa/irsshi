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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.jcraft.jsch.UserInfo;
import jackpal.androidterm.TermView;
import jackpal.androidterm.emulatorview.EmulatorView;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.util.TermSettings;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

public class HelloAndroidActivity extends Activity {
    private static final Logger log = LoggerFactory.getLogger(HelloAndroidActivity.class);

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log.debug("Hello {}", "world");

        setContentView(R.layout.main);
    }

    UserInfo userInfo = new UserInfo() {
        @Override
        public String getPassphrase() {
//            Toast.makeText(getActivity(), "getPassphrase", Toast.LENGTH_LONG).show();
            return "moro";  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getPassword() {
//            Toast.makeText(getActivity(), "getPassword", Toast.LENGTH_LONG).show();
            return "moro";  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean promptPassword(String s) {
            Toast.makeText(getApplicationContext(), "promptPassword", Toast.LENGTH_LONG).show();
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean promptPassphrase(String s) {
            Toast.makeText(getApplicationContext(), "promptPassphrase", Toast.LENGTH_LONG).show();
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


    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.

        Bundle arguments = getIntent().getExtras();
        if (arguments == null) {
            arguments = new Bundle();
        }

        /*
        final SshTermSession.HostInfo hostInfo = new SshTermSession.HostInfo(
                arguments.getString(TerminalsActivity.EXTRA_HOSTNAME, "lakka.kapsi.fi"),
                arguments.getInt(TerminalsActivity.EXTRA_PORT, 22),
                arguments.getString(TerminalsActivity.EXTRA_USERNAME, "riksa"));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TermSettings termSettings = new TermSettings(getResources(), preferences);
        TermSession session = createTermSession(hostInfo, userInfo, termSettings);

        EmulatorView emulatorView = createEmulatorView(session);
        ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.addView(emulatorView);
          */

        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_password_title);
        builder.setView(inflater.inflate(R.layout.dialog_password, null));
        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                log.debug("OK");
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                log.debug("Cancel");
            }
        };
        builder.setPositiveButton(R.string.dialog_password_ok, okListener);
        builder.setNegativeButton(R.string.dialog_password_cancel, cancelListener);
        builder.create().show();
        log.debug("Moving on");

    }
                                              /*
    protected static TermSession createTermSession(SshTermSession.HostInfo hostInfo, UserInfo userInfo, TermSettings settings) {
        TermSession session = new SshTermSession(hostInfo, userInfo, settings);
        return session;
    }

    private TermView createEmulatorView(TermSession session) {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        TermView emulatorView = new TermView(this, session, metrics);

        registerForContextMenu(emulatorView);
        return emulatorView;
    }
                                                */
}

