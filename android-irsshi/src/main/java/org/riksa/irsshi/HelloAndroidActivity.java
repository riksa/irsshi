package org.riksa.irsshi;

import android.app.Activity;
import android.os.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloAndroidActivity extends Activity {
    private static String TAG = "irsshi-";
    private static final Logger log = LoggerFactory.getLogger(TAG + HelloAndroidActivity.class.getSimpleName());

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

}

