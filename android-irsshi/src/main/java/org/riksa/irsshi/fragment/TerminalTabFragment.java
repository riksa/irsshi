/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        TextView view = new TextView(getActivity());
        view.setText(bundle.getString("TAG"));
        return view;
    }

}
