package org.riksa.irsshi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import org.riksa.irsshi.domain.LocalTermHost;
import org.riksa.irsshi.domain.MoshTermHost;
import org.riksa.irsshi.domain.SshTermHost;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 20:25
 */
public class IrsshiService extends Service {
    private static final Logger log = LoggerFactory.getLogger(IrsshiService.class);
    private final IBinder mBinder = new LocalBinder();

    public static List<TermHost> getHosts() {
        List<TermHost> hosts = new ArrayList<TermHost>();
        hosts.add(new SshTermHost("some.host.ssh", "someone"));
        hosts.add(new SshTermHost("another.host.ssh", "someoneelse", 2222));
        hosts.add(new MoshTermHost("some.host.mosh", "foomosh"));
        hosts.add(new MoshTermHost("another.host.mosh", "barmosh", 1111));
        hosts.add(new LocalTermHost());
        hosts.add(new LocalTermHost("android"));
        return hosts;
    }

    public class LocalBinder extends Binder {
        public IrsshiService getService() {
            return IrsshiService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.
        log.debug("onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        log.debug("onDestroy");
    }
}
