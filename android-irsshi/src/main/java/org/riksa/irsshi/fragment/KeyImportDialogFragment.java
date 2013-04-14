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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.apache.commons.io.IOUtils;
import org.riksa.a3.KeyChain;
import org.riksa.irsshi.IrsshiApplication;
import org.riksa.irsshi.R;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.util.IrsshiUtils;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: riksa
 * Date: 13.4.2013
 * Time: 11:09
 */
public class KeyImportDialogFragment extends DialogFragment {
    private static final Logger log = LoggerFactory.getLogger(KeyImportDialogFragment.class);
    private static final int REQUEST_PICK_FILE = 1;
    public static final String TAG = "KeyImportDialogFragment";
    private DialogListener dialogListener;
    private byte[] privateKeyBytes;
    private byte[] publicKeyBytes; // TODO: if needed
    private EditText privateKeyEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_key_import, container);

        privateKeyEdit = IrsshiUtils.findView(view, EditText.class, R.id.private_key_path);

        IrsshiUtils.findView(view, Button.class, R.id.button_browse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("org.openintents.action.PICK_FILE");
                startActivityForResult(intent, REQUEST_PICK_FILE);
            }
        });

        IrsshiUtils.findView(view, Button.class, R.id.button_import_key).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImportKeyClicked(view);
            }
        });

        getDialog().setTitle(R.string.key_import_dialog_title);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_FILE:
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    KeyChain.load(bytes, null);
                    privateKeyBytes = bytes;
                    privateKeyEdit.setError(null);
                    privateKeyEdit.setText(uri.toString());

                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(), R.string.error_file_not_found, Toast.LENGTH_SHORT).show();
                    log.error(e.getMessage(), e);
                } catch (IOException e) {
                    Toast.makeText(getActivity(), R.string.error_cannot_read_file, Toast.LENGTH_SHORT).show();
                    log.error(e.getMessage(), e);
                } catch (JSchException e) {
                    privateKeyBytes = null;
                    Toast.makeText(getActivity(), R.string.error_key_import, Toast.LENGTH_LONG).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.
    }


    public void onImportKeyClicked(View view) {
        if (!validateInputs()) {
            return;
        }

        String alias = IrsshiUtils.findView(getView(), EditText.class, R.id.alias).getText().toString();
        if (TextUtils.isEmpty(alias)) {
            alias = "default";
        }

        String comment = alias;

        try {
            IrsshiApplication.getIrsshiService().importPrivateKey(alias, privateKeyBytes, publicKeyBytes, comment);
            if (dialogListener != null) {
                dialogListener.onOk();
            }
            dismiss();
        } catch (JSchException e) {
            Toast.makeText(getActivity(), R.string.error_key_import, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getActivity(), R.string.error_io_exception, Toast.LENGTH_LONG).show();
        }


    }

    private boolean validateInputs() {
        boolean valid = true;

        if (privateKeyBytes == null) {
            valid = false;
            privateKeyEdit.setError(getString(R.string.validation_error_keyfile_missing));
        }

        try {
            KeyChain.load(privateKeyBytes, publicKeyBytes);
        } catch (JSchException e) {
            valid = false;
            privateKeyEdit.setError(getString(R.string.validation_error_keyfile_invalid));
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
