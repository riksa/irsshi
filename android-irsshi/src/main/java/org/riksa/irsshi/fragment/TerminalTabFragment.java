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

package org.riksa.irsshi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ViewFlipper;
import jackpal.androidterm.emulatorview.EmulatorView;
import jackpal.androidterm.emulatorview.TermSession;
import org.riksa.irsshi.IrSSHiActivity;
import org.riksa.irsshi.IrsshiApplication;
import org.riksa.irsshi.R;
import org.riksa.irsshi.domain.ConnectionInfo;
import org.riksa.irsshi.domain.ConnectionStateChangeListener;
import org.riksa.irsshi.domain.TermHost;
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


    EmulatorView termView;
    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        long hostId = bundle.getLong(IrSSHiActivity.EXTRA_HOST_ID);

        View layout = inflater.inflate(R.layout.fragment_terminal_tab, container, false);

        termView = (EmulatorView) layout.findViewById(R.id.term_view);
        DisplayMetrics metrics = new DisplayMetrics();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        termView.setDensity(metrics);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        termView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                log.debug("onFocus {}", hasFocus);
                if (hasFocus) {
                    inputMethodManager.showSoftInput(view, 0);
//                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        termView.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                log.debug("onTouch");
                inputMethodManager.showSoftInput(view, 0);
                return false;
            }
        });
        termView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log.debug("onClick");
                inputMethodManager.showSoftInput(view, 0);
            }
        });

        final ViewFlipper viewFlipper = IrsshiUtils.findView(layout, ViewFlipper.class, R.id.view_flipper);

        IrsshiApplication.getIrsshiService().connectToHostById(getActivity(), hostId, new ConnectionStateChangeListener() {
            @Override
            public void stateChanged(final ConnectionInfo.HostState hostState, final TermHost termHost, final TermSession session) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (hostState) {
                            case CONNECTING:
                                viewFlipper.setDisplayedChild(0);
                                break;
                            case CONNECTED:
                                viewFlipper.setDisplayedChild(1);
                                termView.attachSession(session);
                                break;
                            case DISCONNECTED:
                                viewFlipper.setDisplayedChild(2);
                                break;
                        }
                    }
                });

            }
        });

        return layout;
//        TextView view = new TextView(getActivity());
//        view.setText(bundle.getString("title", "o hi"));
//        return view;

    }

}
