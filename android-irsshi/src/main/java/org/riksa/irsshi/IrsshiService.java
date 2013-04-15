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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.jcraft.jsch.*;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.util.TermSettings;
import org.riksa.a3.KeyChain;
import org.riksa.a3.KeyGeneratorCallback;
import org.riksa.a3.PromptPasswordCallback;
import org.riksa.irsshi.domain.ConnectionInfo;
import org.riksa.irsshi.domain.ConnectionStateChangeListener;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.JSchLogger;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.util.IrsshiUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 20:25
 */
public class IrsshiService extends Service {
    private static final Logger log = LoggerFactory.getLogger(IrsshiService.class);
    /**
     * Alias of the key, default "default"
     */
    public static final String EXTRA_ALIAS = "EXTRA_ALIAS";
    /**
     * Type of the key. 1=DSA, 2=RSA. (default 2 (RSA)), {@see com.jcraft.jsch.KeyPair.RSA} {@see com.jcraft.jsch.KeyPair.DSA}
     */
    public static final String EXTRA_KEYTYPE = "EXTRA_KEYTYPE";
    /**
     * Strength of key in bits (default 2048)
     */
    public static final String EXTRA_BITS = "EXTRA_BITS";
    /**
     * Comment for the public key (the text appended to the public key in the file), e.g. "IrSSHi on Nexus7". Default="irSSHi"
     */
    public static final String EXTRA_COMMENT = "EXTRA_COMMENT";
    public static final int GENERATE_KEYPAIR = 1;

    /**
     * Positive response codes are, well, positive. Negative are error codes
     */
    public static final int STATUS_OK = 0;

    /**
     * Positive response codes are, well, positive. Negative are error codes
     */
    public static final int STATUS_ERROR = -1;
    private static final String KEY_FILE_DIR = ".ssh";


    private final IBinder binder = new LocalBinder();
    private TermHostDao termHostDao;
    private Handler handler;
    private static final List<ConnectionInfo> connections = new LinkedList<ConnectionInfo>();
    private KeyChain keyChain;
    private Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case GENERATE_KEYPAIR:
                    log.debug("GENERATE_KEYPAIR");
                    String alias = "default";
                    if (msg.obj != null && msg.obj instanceof String) {
                        alias = (String) msg.obj; //getData().getString(EXTRA_ALIAS, "default");
                    }
                    int type = msg.arg1; //.getData().getInt(EXTRA_KEYTYPE, RSA);
                    int bits = msg.arg2; //getData().getInt(EXTRA_BITS, 2048);
                    String comment = msg.getData().getString(EXTRA_COMMENT, "irSSHi");
//                    msg.getData().getString(EXTRA_COMMENT, "default");

                    KeyGeneratorCallback keyGeneratorCallback = new KeyGeneratorCallback() {
                        final Messenger replyTo = msg.replyTo;

                        @Override
                        public void succeeded(String alias) {
                            log.debug("Generate key {}", alias);
                            if (replyTo != null) {
                                try {
                                    replyTo.send(Message.obtain(null, STATUS_OK, alias));
                                } catch (RemoteException e) {
                                    log.error(e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void failed(String alias, String message) {
                            log.debug("Failed to generate key {}, {}", alias, message);
                            if (replyTo != null) {
                                try {
                                    replyTo.send(Message.obtain(null, STATUS_ERROR, alias));
                                } catch (RemoteException e) {
                                    log.error(e.getMessage());
                                }
                            }
                        }
                    };
                    PromptPasswordCallback promptPasswordCallback = new PromptPasswordCallback() {
                        @Override
                        public String getLockingPassword() {
                            // TODO: Prompt from user when needed? or get it from the msg?
                            return "";
                        }

                        @Override
                        public String getUnlockingPassword() {
                            return null;
                        }
                    };
                    keyChain.generateKeyAsync(keyGeneratorCallback, promptPasswordCallback, alias, type, bits, comment);
                    break;
                default:
                    log.warn("Unknown msg.what({}), message={} : ", msg.what, msg);
            }
            super.handleMessage(msg);    //To change body of overridden methods use File | Settings | File Templates.
        }
    });

    // TODO: delegate
    public TermHostDao getTermHostDao() {
        return termHostDao;
    }


//    public SessionList getSessions() {
//        return sessions;
//    }

//    private SessionList sessions = new SessionList();

    public void connectToHostById(final Activity activity, long hostId, final ConnectionStateChangeListener connectionStateChangeListener) {
        final TermHost host = termHostDao.findHostById(hostId);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TermSettings termSettings = new TermSettings(getResources(), preferences);
//        TermSession session = new SshTermSession(host, userInfo, termSettings);
        final TermSession termSession = new TermSession();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final ConnectionInfo connectionInfo = new ConnectionInfo(ConnectionInfo.HostState.CONNECTING, termSession, host, connectionStateChangeListener);
                connections.add(connectionInfo);
                JSchLogger logger = new JSchLogger();
                JSch.setLogger(logger);

                try {
                    final Object promptMutex = new Object();
                    JSch jsch = new JSch();
                    String alias = host.getIdentityAlias();
                    if (!TextUtils.isEmpty(alias)) {
                        try {
                            KeyPair keyPair = keyChain.load(host.getIdentityAlias());
                            jsch.addIdentity(alias, keyPair.forSSHAgent(), null, null);
                        } catch (JSchException e) {
                            log.error(e.getMessage(), e);
                        }
                    }

                    JSch.setConfig("StrictHostKeyChecking", "no");

                    Session session = jsch.getSession(host.getUserName(), host.getHostName(), host.getPort());

                    // username and password will be given via UserInfo interface.
                    //            UserInfo ui = new TestUserInfo();
                    UserInfo userInfo = new UserInfo() {
                        private String password;
                        boolean ret = false;
                        boolean waiting = true;

                        @Override
                        public String getPassphrase() {
//            Toast.makeText(getApplicationContext(), "getPassphrase", Toast.LENGTH_LONG).show();
                            return "";  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public String getPassword() {
//            Toast.makeText(getApplicationContext(), "getPassword", Toast.LENGTH_LONG).show();
                            return password;  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public boolean promptPassword(final String s) {
                            synchronized (promptMutex) {
                                waiting = true;
                                password = null;
                                ret = false;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_password, null, false);
//                editText.setInputType( 0 );
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setTitle(R.string.dialog_password_title);
                                        builder.setView(view);
                                        builder.setMessage(s);
                                        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                waiting = false;
                                                ret = true;
                                                EditText editText = IrsshiUtils.findView(view, EditText.class, R.id.password);
                                                password = editText.getText().toString();
                                            }
                                        };
                                        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                waiting = false;
                                                ret = false;
                                            }
                                        };
                                        builder.setPositiveButton(R.string.dialog_password_ok, okListener);
                                        builder.setNegativeButton(R.string.dialog_password_cancel, cancelListener);
                                        builder.create().show();
                                    }
                                });
                                while (waiting) {
                                    Thread.yield();
                                }
                            }

                            return ret;  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public boolean promptPassphrase(String s) {
//            Toast.makeText(getApplicationContext(), "promptPassphrase", Toast.LENGTH_LONG).show();
                            return false;
                        }

                        @Override
                        public boolean promptYesNo(String s) {
                            return true;  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public void showMessage(String s) {
                        }

                    };

                    session.setUserInfo(userInfo);

                    session.connect();

                    Channel channel = session.openChannel("shell");

                    //            channel.setInputStream( getTermIn() );
                    //            channel.setOutputStream( getTermOut() );
                    termSession.setTermOut(channel.getOutputStream());
                    termSession.setTermIn(channel.getInputStream());

                    channel.connect();
//                    notifyUpdate();
                    termSession.setTitle(host.getNickName());
                    connectionInfo.setHostState(ConnectionInfo.HostState.CONNECTED);
                    termSession.setFinishCallback(new TermSession.FinishCallback() {
                        @Override
                        public void onSessionFinish(TermSession session) {
                            connectionInfo.setHostState(ConnectionInfo.HostState.DISCONNECTED);
                        }
                    });

                } catch (Exception e) {
                    logger.log(com.jcraft.jsch.Logger.ERROR, e.getMessage());
                    connectionInfo.setHostState(ConnectionInfo.HostState.DISCONNECTED);
                }
            }
        };

        new Thread(runnable).start();
    }

    public void generateKeyPair(final KeyGeneratorCallback keyGeneratorCallback, final PromptPasswordCallback promptPasswordCallback, String alias, int keyType, int keyBits, String comment) {
        keyChain.generateKeyAsync(keyGeneratorCallback, promptPasswordCallback, alias, keyType, keyBits, comment);
    }

    public void importPrivateKey(String alias, byte[] privateKeyBytes, byte[] publicKeyBytes, String comment) throws JSchException, IOException {
        KeyPair keyPair = KeyChain.load(privateKeyBytes, publicKeyBytes);
        keyChain.save(alias, keyPair, comment);
    }

    public List<String> aliases() {
        return keyChain.aliases();
    }

    public class LocalBinder extends Binder {
        public IrsshiService getService() {
            return IrsshiService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return binder;
//        return messenger.getBinder(); //mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.
        log.debug("onCreate");
        handler = new Handler();
        termHostDao = new ContentProviderTermHostDao(this);
        File filesDir = new File(getApplicationContext().getFilesDir(), KEY_FILE_DIR);
        filesDir.mkdirs();
        keyChain = new KeyChain(filesDir);
//            Intent tutorial = new Intent(getBaseContext(), TutorialActivity.class);
//            tutorial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getApplication().startActivity(tutorial);
//            PromptPasswordCallback callback = new PromptPasswordCallback() {
//                @Override
//                public String getPassword(boolean lock, PasswordType passwordType) {
//                    log.warn("Might want to prompt password from user ;)");
//                    return "foo";
//                }
//            };
//            keyChain.generateKeyAsync(callback, "default", "RSA", 2048);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        log.debug("onDestroy");
    }

}
