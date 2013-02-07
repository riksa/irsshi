package org.riksa.irsshi;

import android.app.Application;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.provider.IrsshiSQLiteOpenHelper;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 23:15
 */
public class IrsshiApplication extends Application {
    private static final Logger log = LoggerFactory.getLogger(IrsshiApplication.class);
    private TermHostDao termHostDao;
    private static IrsshiApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.
        instance = this;
        termHostDao = new IrsshiSQLiteOpenHelper(this);
    }

    public TermHostDao getTermHostDao() {
        return termHostDao;
    }

    public static IrsshiApplication getInstance() {
        return instance;
    }
}
