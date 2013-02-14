/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi.fragment;

import android.app.Fragment;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import jackpal.androidterm.emulatorview.UpdateCallback;
import jackpal.androidterm.util.TermSettings;
import org.riksa.irsshi.IrsshiService;
import org.riksa.irsshi.R;
import org.riksa.irsshi.SshTermSession;
import org.riksa.irsshi.TerminalsActivity;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.util.IrsshiUtils;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 8.2.2013
 * Time: 20:22
 */
public class TerminalTabFragment extends Fragment {
    private static final Logger log = LoggerFactory.getLogger(TerminalTabFragment.class);

    private IrsshiService irsshiService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            irsshiService = ((IrsshiService.LocalBinder) iBinder).getService();
            handler.sendEmptyMessage(-1) ;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            irsshiService = null;
        }
    };

    EmulatorView termView;
    private int termIdx;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                TermSession session = irsshiService.getSessions().get(termIdx);
                termView.attachSession(session);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        termIdx = bundle.getInt("idx", 0);

        View layout = inflater.inflate(R.layout.fragment_terminal_tab, container, false);

        termView = (EmulatorView) layout.findViewById(R.id.term_view);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        termView.setDensity(metrics);

        return layout;
//        TextView view = new TextView(getActivity());
//        view.setText(bundle.getString("title", "o hi"));
//        return view;

    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        getActivity().bindService(new Intent(getActivity(), IrsshiService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.

        if (irsshiService != null) {
            getActivity().unbindService(serviceConnection);
        }
    }

}
