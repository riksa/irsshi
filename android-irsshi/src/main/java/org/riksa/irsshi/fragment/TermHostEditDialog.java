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

import android.app.DialogFragment;
import android.net.sip.SipErrorCode;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.riksa.irsshi.R;
import org.riksa.irsshi.domain.AbstractHost;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.domain.TermHostFactory;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.util.IrsshiUtils;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 2/8/13
 * Time: 12:56 PM
 */
public class TermHostEditDialog extends DialogFragment {
    private int defaultPort = 22;
    private static final Logger log = LoggerFactory.getLogger(TermHostEditDialog.class);
    public static final String TAG = "dialog";
    private TermHostEditListener listener;
    private TermHost termHost;
    private View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TermHost modified = createTermHost(termHost);
            if (modified.validate()) {
                if (listener != null) {
                    listener.onOk(modified);
                }
                getDialog().dismiss();
            } else {
                for (TermHost.TermHostValidationError error : modified.getErrors()) {
                    switch (error) {
                        case HOSTNAME:
                            IrsshiUtils.findView(getView(), TextView.class, R.id.hostname).setError(getString(R.string.validation_error_hostname));
                            break;
                        case NICKNAME:
                            IrsshiUtils.findView(getView(), TextView.class, R.id.nickname).setError(getString(R.string.validation_error_nickname));
                            break;
                        case PORT:
                            IrsshiUtils.findView(getView(), TextView.class, R.id.port).setError(getString(R.string.validation_error_port));
                            break;
                        case USERNAME:
                            IrsshiUtils.findView(getView(), TextView.class, R.id.username).setError(getString(R.string.validation_error_username));
                            break;

                    }
                }
            }
        }
    };

    private TermHost createTermHost(TermHost termHost) {
        TermHost created;
        if (termHost == null) {
            // Create a new one
            Spinner spinner = IrsshiUtils.findView(getView(), Spinner.class, R.id.spinner);
            created = TermHostFactory.create((String) spinner.getSelectedItem(), null);
        } else {
            // create a copy of the existing TermHost
            created = TermHostFactory.create(termHost.getHostType(), termHost.getId());
        }
        created.setHostName(IrsshiUtils.findView(getView(), TextView.class, R.id.hostname).getText().toString());
        created.setUserName(IrsshiUtils.findView(getView(), TextView.class, R.id.username).getText().toString());
        created.setNickName(IrsshiUtils.findView(getView(), TextView.class, R.id.nickname).getText().toString());
        int port = -1;
        try {
            port = Integer.parseInt(IrsshiUtils.findView(getView(), TextView.class, R.id.port).getText().toString());
        } catch (NumberFormatException e) {
        }
        created.setPort(port);
        return created;
    }

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onCancel();
            }
            getDialog().cancel();
        }
    };

    public static DialogFragment newInstance(TermHost termHost, TermHostEditListener listener) {
        TermHostEditDialog termHostEditDialog = new TermHostEditDialog();
        termHostEditDialog.setListener(listener);
        termHostEditDialog.setTermHost(termHost);
        return termHostEditDialog;
    }

    private void setListener(TermHostEditListener listener) {
        this.listener = listener;
    }

    private void setTermHost(TermHost termHost) {
        this.termHost = termHost;
    }

    private void setTextViewText(View view, int resId, String text) {
        IrsshiUtils.findView(view, TextView.class, resId).setText(text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_host_dialog, container, false);
        getDialog().setTitle(termHost == null ? R.string.add_host : R.string.edit_host);
        IrsshiUtils.findView(v, Button.class, R.id.ok).setOnClickListener(okListener);
        IrsshiUtils.findView(v, Button.class, R.id.cancel).setOnClickListener(cancelListener);
        if (termHost != null) {
            setTextViewText(v, R.id.nickname, termHost.getNickName());
            setTextViewText(v, R.id.username, termHost.getUserName());
            setTextViewText(v, R.id.hostname, termHost.getHostName());
            setTextViewText(v, R.id.port, Integer.toString(termHost.getPort()));

            Spinner spinner = IrsshiUtils.findView(v, Spinner.class, R.id.spinner);
            String[] types = getResources().getStringArray(R.array.host_types);
            int c = 0;
            for (String type : types) {
                if (TextUtils.equals(type, termHost.getHostType().name())) {
                    spinner.setSelection(c);
                    break;
                }
                c++;
            }
        }

        final EditText nicknameTextView = IrsshiUtils.findView(v, EditText.class, R.id.nickname);
        final EditText usernameTextView = IrsshiUtils.findView(v, EditText.class, R.id.username);
        final EditText hostnameTextView = IrsshiUtils.findView(v, EditText.class, R.id.hostname);
        final EditText portTextView = IrsshiUtils.findView(v, EditText.class, R.id.port);

        Spinner spinner = IrsshiUtils.findView(v, Spinner.class, R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String hostType = (String) adapterView.getSelectedItem();
                log.debug("Selected: {}", hostType);
                TermHost host = TermHostFactory.create(hostType, null);
                defaultPort = host.getDefaultPort();
                portTextView.setText(Integer.toString(defaultPort));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                StringBuilder sb = new StringBuilder();

                if (!TextUtils.isEmpty(usernameTextView.getText())) {
                    sb.append(usernameTextView.getText()).append("@");
                }

                sb.append(hostnameTextView.getText());

                try {
                    int port = Integer.parseInt(portTextView.getText().toString());
                    if (port != defaultPort) {
                        sb.append(":").append(portTextView.getText());
                    }
                } catch (NumberFormatException e) {
                }

                nicknameTextView.setText(sb.toString());
            }
        };
        usernameTextView.addTextChangedListener(watcher);
        hostnameTextView.addTextChangedListener(watcher);
        portTextView.addTextChangedListener(watcher);


        return v;
    }

}
