/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.jcraft.jsch.UserInfo;
import jackpal.androidterm.TermView;
import jackpal.androidterm.emulatorview.EmulatorView;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.util.TermSettings;
import org.riksa.irsshi.IrsshiService;
import org.riksa.irsshi.R;
import org.riksa.irsshi.SshTermSession;
import org.riksa.irsshi.TerminalsActivity;
import org.riksa.irsshi.util.IrsshiUtils;

/**
 * User: riksa
 * Date: 8.2.2013
 * Time: 20:22
 */
public class TerminalTabFragment extends Fragment {

    private IrsshiService irsshiService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            irsshiService = ((IrsshiService.LocalBinder) iBinder).getService();
            TermSession session = irsshiService.getSessions().get(0);
            termView.attachSession( session );
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            irsshiService = null;
        }
    };

    TermView termView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();

//        TextView view = new TextView(getActivity());
//        view.setText(bundle.getString("title", "o hi"));
//        return view;
        termView = getTermView();
        return termView;
    }

    private TermView getTermView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        TermView emulatorView = new TermView(getActivity(), null, metrics);
        return emulatorView;
    }


}
