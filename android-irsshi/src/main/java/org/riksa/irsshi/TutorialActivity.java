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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;
import org.riksa.irsshi.fragment.DialogListener;
import org.riksa.irsshi.fragment.KeyGenerationDialogFragment;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.util.IrsshiUtils;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 24.3.2013
 * Time: 21:31
 */
public class TutorialActivity extends FragmentActivity {
    private static final Logger log = LoggerFactory.getLogger(TutorialActivity.class);
    private static final int REQUEST_PICK_FILE = 1;
    private static final int REQUEST_GENERATE_FILE = 2;

    public void onCreate(Bundle savedInstanceState) {
        // TODO: some kind of click through welcome to IrSSHi process here...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
    }

    public void onPrevClicked(View view) {
        IrsshiUtils.findView(this, ViewFlipper.class, R.id.view_flipper).showPrevious();
    }

    public void onNextClicked(View view) {
        IrsshiUtils.findView(this, ViewFlipper.class, R.id.view_flipper).showNext();
    }

    public void onGenerateKeyClicked(View view) {
//        Intent intent = new Intent(this, KeyGenerationActivity.class);
//        startActivityForResult(intent, REQUEST_GENERATE_FILE);
        KeyGenerationDialogFragment dialogFragment = new KeyGenerationDialogFragment();
        dialogFragment.setDialogListener(
                new DialogListener() {
                    @Override
                    public void onCancel() {
                        log.debug("onCancel");
                    }

                    @Override
                    public void onOk() {
                        log.debug("onOk {}");
                        finish();
                    }
                });
        FragmentManager fm = getSupportFragmentManager();
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show(); // TODO: everything to use support library...
//        dialogFragment.show(fm, KeyGenerationDialogFragment.TAG);
    }


    public void onImportKeyClicked(View view) {
        Intent intent = new Intent("org.openintents.action.PICK_FILE");
        startActivityForResult(intent, REQUEST_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_FILE:
                Uri uri = data.getData();
                log.info("TODO: Import key {}", uri);
                break;
            case REQUEST_GENERATE_FILE:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.
    }
}