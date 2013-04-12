package org.riksa.irsshi;/*
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.Toast;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 12.4.2013
 * Time: 17:01
 */
public class KeyGenerationActivity extends Activity {
    private static final Logger log = LoggerFactory.getLogger(KeyGenerationActivity.class);
    private Handler handler;
    Messenger messenger;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_generation);

    }

    public void onGenerateKeyClicked(View view) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case IrsshiService.STATUS_OK:
                        log.debug("Generated key " + msg.obj);
                        Toast.makeText(KeyGenerationActivity.this, "Generated key " + msg.obj, Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case IrsshiService.STATUS_ERROR:
                        log.debug("Failed to generate key " + msg.obj);
                        Toast.makeText(KeyGenerationActivity.this, "Failed to generate key " + msg.obj, Toast.LENGTH_SHORT).show();
                        setResult(RESULT_CANCELED);
                        finish();
                        break;
                    default:
                        log.warn("Unknown status code " + msg.what);
                }
                super.handleMessage(msg);    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
        messenger = new Messenger(handler);
        Message message = Message.obtain(handler, IrsshiService.GENERATE_KEYPAIR, 1 /* 1=RSA */, 512, "default");
        message.replyTo = messenger;
        IrsshiApplication.sendServiceMessage(message);

    }
}