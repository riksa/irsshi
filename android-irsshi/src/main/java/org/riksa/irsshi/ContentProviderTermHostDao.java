package org.riksa.irsshi;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.domain.TermHostFactory;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.provider.HostProviderMetaData;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: riksa
 * Date: 2/8/13
 * Time: 11:57 AM
 */
public class ContentProviderTermHostDao implements TermHostDao {
    private static final Logger log = LoggerFactory.getLogger(ContentProviderTermHostDao.class);
    private final Context context;

    public ContentProviderTermHostDao(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public List<TermHost> getHosts() {
        return getHosts(-1);
    }

    private List<TermHost> getHosts(long hostId) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri;

        if (hostId == -1) {
            // Get all hosts
            uri = HostProviderMetaData.HostTableMetaData.CONTENT_URI;
        } else {
            // Find one with Id = hostId
            uri = ContentUris.withAppendedId(HostProviderMetaData.HostTableMetaData.CONTENT_URI, hostId);
        }
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return parseCursor(cursor);
            }
        } finally {
            cursor.close();
        }

        return Collections.emptyList();
    }

    @Override
    public void insertHost(TermHost termHost) {
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = parseContentValues(termHost);

        Uri uri = contentResolver.insert(HostProviderMetaData.HostTableMetaData.CONTENT_URI, contentValues);
        log.debug("Inserted {}", uri);
    }

    private ContentValues parseContentValues(TermHost termHost) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HostProviderMetaData.HostTableMetaData.NICKNAME, termHost.getNickName());
        contentValues.put(HostProviderMetaData.HostTableMetaData.HOSTNAME, termHost.getHostName());
        contentValues.put(HostProviderMetaData.HostTableMetaData.TYPE, termHost.getHostType().name());
        contentValues.put(HostProviderMetaData.HostTableMetaData.PORT, termHost.getPort());
        contentValues.put(HostProviderMetaData.HostTableMetaData.USERNAME, termHost.getUserName());
        return contentValues;
    }

    @Override
    public int deleteHost(long hostId) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(HostProviderMetaData.HostTableMetaData.CONTENT_URI, hostId);
        return contentResolver.delete(uri, null, null);
    }

    @Override
    public int updateHost(TermHost termHost) {
        assert termHost != null;
        assert termHost.getId() != null;
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(HostProviderMetaData.HostTableMetaData.CONTENT_URI, termHost.getId());
        return contentResolver.update(uri, parseContentValues(termHost), null, null);
    }

    @Override
    public TermHost findHostById(long hostId) {
        List<TermHost> termHosts = getHosts(hostId);

        if (termHosts.size() > 0)
            return termHosts.get(0);
        return null;

    }

    private List<TermHost> parseCursor(Cursor cursor) {
        List<TermHost> hosts = new ArrayList<TermHost>(cursor.getCount());

        final int typeIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.TYPE);
        final int idIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData._ID);
        final int nickNameIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.NICKNAME);
        final int hostNameIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.HOSTNAME);
        final int portIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.PORT);
        final int userNameIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.USERNAME);
        do {
            String hosttype = cursor.getString(typeIdx);
            TermHost host = TermHostFactory.create(hosttype, cursor.getInt(idIdx));
            host.setNickName(cursor.getString(nickNameIdx));
            host.setHostName(cursor.getString(hostNameIdx));
            host.setPort(cursor.getInt(portIdx));
            host.setUserName(cursor.getString(userNameIdx));
            hosts.add(host);
        } while (cursor.moveToNext());

        return hosts;
    }

//    @Override
//    public Cursor getCursor() {
//        Uri uri = HostProviderMetaData.HostTableMetaData.CONTENT_URI;
//        return context.getContentResolver().query(uri, null, null, null, null);
//    }
}
