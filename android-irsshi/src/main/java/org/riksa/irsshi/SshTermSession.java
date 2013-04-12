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

import android.content.Context;
import android.os.Handler;
import com.jcraft.jsch.*;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.util.TermSettings;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.JSchLogger;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * User: riksa
 * Date: 2/1/13
 * Time: 9:39 AM
 */
public class SshTermSession extends TermSession {
    public SshTermSession(final TermHost host, final UserInfo ui, final TermSettings settings) {
        super();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                JSchLogger logger = new JSchLogger();
                JSch.setLogger(logger);

                try {
                    JSch jsch = new JSch();

                    Session session = jsch.getSession(host.getUserName(), host.getHostName(), host.getPort());

                    // username and password will be given via UserInfo interface.
                    //            UserInfo ui = new TestUserInfo();
                    session.setUserInfo(ui);

                    session.connect();

                    Channel channel = session.openChannel("shell");

                    //            channel.setInputStream( getTermIn() );
                    //            channel.setOutputStream( getTermOut() );
                    setTermOut(channel.getOutputStream());
                    setTermIn(channel.getInputStream());

                    channel.connect();
                    notifyUpdate();
                } catch (Exception e) {
                    logger.log(Logger.ERROR, e.getMessage());
                }
            }
        };

        new Thread(runnable).start();

//        setTermOut(new FileOutputStream(mTermFd));
//        setTermIn(new FileInputStream(mTermFd));


//        write("screen -DR\n");
//        write("Hello from terminal\n");
//        write("Hello from terminal\n");
//        write("Hello from terminal\n");
    }

}
