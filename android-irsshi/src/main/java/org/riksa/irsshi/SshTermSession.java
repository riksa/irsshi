/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
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
                    Thread.sleep(30000);
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
