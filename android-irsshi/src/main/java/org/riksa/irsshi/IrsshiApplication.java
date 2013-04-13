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

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 23:15
 */
public class IrsshiApplication extends Application {
    private static final Logger log = LoggerFactory.getLogger(IrsshiApplication.class);
    private static IrsshiService irsshiService;
    private static Messenger messenger;
    private static final Queue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();

    @Deprecated
    /**
     * Todo: Messaging based communication with the service...
     */
    public static IrsshiService getIrsshiService() {
        log.error( "This is broken, use messages");
        return irsshiService;
    }

//    /**
//     * Send message to service. Queues message if it cannot be sent immediately (can happen if the service is not yet started)
//     *
//     * @param message message to send
//     * @return true if message was sent, false if it was queued for sending later
//     */
//    public static boolean sendServiceMessage(Message message) {
//        if (messenger != null) {
//            try {
//                messenger.send(message);
//                return true;
//            } catch (RemoteException e) {
//                log.error(e.getMessage());
//            }
//        }
//        messageQueue.add(message);
//        return false;
//    }

    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.

        startService(new Intent(this, IrsshiService.class));

        bindService(new Intent(this, IrsshiService.class), new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                irsshiService = ((IrsshiService.LocalBinder) iBinder).getService();
//                messenger = new Messenger(iBinder);
                while (!messageQueue.isEmpty()) {
                    try {
                        Message message = messageQueue.remove();
                        messenger.send(message);
                    } catch (RemoteException e) {
                        log.error(e.getMessage());
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
//                irsshiService = null;
                messenger = null;
            }
        }, Context.BIND_AUTO_CREATE);

    }

    public static boolean isFirstLaunch() {
        return true;
    }
}
