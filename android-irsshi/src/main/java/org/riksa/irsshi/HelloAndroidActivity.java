package org.riksa.irsshi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.ViewFlipper;
import jackpal.androidterm.TermView;
import jackpal.androidterm.emulatorview.EmulatorView;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.util.TermSettings;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

public class HelloAndroidActivity extends Activity {
    private static final Logger log = LoggerFactory.getLogger(HelloAndroidActivity.class);

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log.debug("Hello {}", "world");

        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        TermSession session = createTermSession();
        EmulatorView emulatorView = createEmulatorView(session);
        ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.addView(emulatorView);
    }

    private TermSession createTermSession() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TermSettings termSettings = new TermSettings(getResources(), preferences);

        TermSession session = createTermSession(this, termSettings, termSettings.getInitialCommand());
//        session.setFinishCallback(mTermService);
        return session;
    }

    protected static TermSession createTermSession(Context context, TermSettings settings, String initialCommand) {
        TermSession session = new SshTermSession(settings, initialCommand);
        // XXX We should really be able to fetch this from within TermSession
//        session.setProcessExitMessage(context.getString(jackpal.androidterm.R.string.process_exit_message));

        return session;
    }


    private TermView createEmulatorView(TermSession session) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        TermView emulatorView = new TermView(this, session, metrics);

//        emulatorView.setExtGestureListener(new EmulatorViewGestureListener(emulatorView));
//        emulatorView.setOnKeyListener(mBackKeyListener);
        registerForContextMenu(emulatorView);

        return emulatorView;
    }

}

