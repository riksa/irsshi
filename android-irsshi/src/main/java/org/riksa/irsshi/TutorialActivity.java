/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.security.KeyPair;

/**
 * User: riksa
 * Date: 24.3.2013
 * Time: 21:31
 */
public class TutorialActivity extends Activity {
    private static final Logger log = LoggerFactory.getLogger(TutorialActivity.class);
    private Handler handler;
    Messenger messenger;

    public void onCreate(Bundle savedInstanceState) {
        // TODO: some kind of click through welcome to IrSSHi process here...
        super.onCreate(savedInstanceState);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case IrsshiService.STATUS_OK:
                        log.debug("Generated key " + msg.obj);
                        Toast.makeText(TutorialActivity.this, "Generated key " + msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    case IrsshiService.STATUS_ERROR:
                        log.debug("Failed to generate key " + msg.obj);
                        Toast.makeText(TutorialActivity.this, "Failed to generate key " + msg.obj, Toast.LENGTH_SHORT).show();
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