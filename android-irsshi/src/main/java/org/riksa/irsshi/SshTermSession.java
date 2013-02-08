/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi;

import com.jcraft.jsch.*;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.util.TermSettings;
import org.riksa.irsshi.logger.JSchLogger;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * User: riksa
 * Date: 2/1/13
 * Time: 9:39 AM
 */
public class SshTermSession extends TermSession {
    public SshTermSession(TermSettings settings, String initialCommand) {
        super();

        JSchLogger logger = new JSchLogger();
        JSch.setLogger(logger);

        HostInfo host = new HostInfo("hostname", 22, "username");
        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(host.username, host.host, host.port);

            // username and password will be given via UserInfo interface.
            UserInfo ui = new TestUserInfo();
            session.setUserInfo(ui);

            session.connect();

            Channel channel = session.openChannel("shell");

//            channel.setInputStream( getTermIn() );
//            channel.setOutputStream( getTermOut() );
            setTermOut( channel.getOutputStream() );
            setTermIn( channel.getInputStream() );

            channel.connect();
        } catch (Exception e) {
            logger.log(Logger.ERROR, e.getMessage());
        }


//        setTermOut(new FileOutputStream(mTermFd));
//        setTermIn(new FileInputStream(mTermFd));


        write("screen -DR\n");
//        write("Hello from terminal\n");
//        write("Hello from terminal\n");
//        write("Hello from terminal\n");
    }

    public static class TestUserInfo implements UserInfo {

        @Override
        public String getPassphrase() {
            return "password";  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getPassword() {
            return "password";  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean promptPassword(String s) {
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean promptPassphrase(String s) {
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean promptYesNo(String s) {
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void showMessage(String s) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public class HostInfo {
        public String host;
        public int port = 22;
        public String username;

        public HostInfo(String host, int port, String username) {
            this.host = host;
            this.port = port;
            this.username = username;
        }
    }

}
