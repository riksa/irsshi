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
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.riksa.a3.KeyGeneratorCallback;
import org.riksa.a3.PromptPasswordCallback;
import org.riksa.irsshi.IrsshiApplication;
import org.riksa.irsshi.R;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.util.IrsshiUtils;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 13.4.2013
 * Time: 11:09
 */
public class KeyGenerationDialogFragment extends DialogFragment {
    private static final Logger log = LoggerFactory.getLogger(KeyGenerationDialogFragment.class);
    public static final String TAG = "KeyGenerationDialogFragment";
    private DialogListener dialogListener;
    private Spinner keyTypeSpinner;
    private Spinner keyBitsSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_key_generation, container);

        keyTypeSpinner = IrsshiUtils.findView(view, Spinner.class, R.id.key_type);
        keyBitsSpinner = IrsshiUtils.findView(view, Spinner.class, R.id.key_bits);

        keyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                keyBitsSpinner.setEnabled(true);
                log.debug("Selected {}", keyTypeSpinner.getSelectedItem());
                String[] strengthList;
                int defaultSelection;
                if ("DSA".equals(keyTypeSpinner.getSelectedItem())) {
                    strengthList = getResources().getStringArray(R.array.dsa_bits);
                    defaultSelection = getResources().getInteger(R.integer.dsa_strength_default_index);
                } else {
                    strengthList = getResources().getStringArray(R.array.rsa_bits);
                    defaultSelection = getResources().getInteger(R.integer.rsa_strength_default_index);
                }
                keyBitsSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, strengthList));
                keyBitsSpinner.setSelection(defaultSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                keyBitsSpinner.setEnabled(false);
            }
        }

        );

        IrsshiUtils.findView(view, Button.class, R.id.button_generate_key).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGenerateKeyClicked(view);
            }
        });

        getDialog().setTitle(R.string.key_generation_dialog_title);
        return view;
    }

    public void onGenerateKeyClicked(View view) {
        if (!validateInputs()) {
            return;
        }

        int keyType = 1; // 1=RSA, 2=DSA
        if ("DSA".equals(keyTypeSpinner.getSelectedItem())) {
            keyType = 2;
        }

        int keyBits = 2048;
        try {
            keyBits = Integer.parseInt(String.valueOf(keyBitsSpinner.getSelectedItem()));
        } catch (NumberFormatException e) {
            log.debug(e.getMessage(), e);
        }

        log.debug("Generating {}bits {} key pair", keyBits, keyType);


        String alias = IrsshiUtils.findView(getView(), EditText.class, R.id.alias).getText().toString();
        if (TextUtils.isEmpty(alias)) {
            alias = "default";
        }

        final String password = IrsshiUtils.findView(getView(), EditText.class, R.id.password_1).getText().toString();
        KeyGeneratorCallback keyGeneratorCallback = new KeyGeneratorCallback() {

            @Override
            public void succeeded(String alias) {
                log.debug("Generated key {}", alias);
            }

            @Override
            public void failed(String alias, String message) {
                log.debug("Failed to generate key {}, {}", alias, message);
            }
        };
        PromptPasswordCallback promptPasswordCallback = new PromptPasswordCallback() {
            @Override
            public String getLockingPassword() {
                return password;
            }

            @Override
            public String getUnlockingPassword() {
                return null;
            }
        };

        IrsshiApplication.getIrsshiService().generateKeyPair(keyGeneratorCallback, promptPasswordCallback, alias, keyType, keyBits, "irSSHi-generated");
//        Message message = Message.obtain(null, IrsshiService.GENERATE_KEYPAIR, keyType, keyBits, "default");
//        IrsshiApplication.sendServiceMessage(message);

        if (dialogListener != null) {
            dialogListener.onOk();
        }

        dismiss();
    }

    private boolean validateInputs() {
        boolean valid = true;
        EditText passwordEdit1 = IrsshiUtils.findView(getView(), EditText.class, R.id.password_1);
        EditText passwordEdit2 = IrsshiUtils.findView(getView(), EditText.class, R.id.password_2);
        if (!passwordEdit1.getText().toString().equals(passwordEdit2.getText().toString())) {
            valid = false;
            passwordEdit2.setError(getString(R.string.validation_error_password));
        }

        return valid;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);    //To change body of overridden methods use File | Settings | File Templates.
        if (dialogListener != null) {
            dialogListener.onCancel();
        }
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}
