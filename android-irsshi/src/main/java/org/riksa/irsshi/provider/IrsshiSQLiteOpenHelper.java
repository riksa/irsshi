package org.riksa.irsshi.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.riksa.irsshi.TermHostDao;
import org.riksa.irsshi.domain.*;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * User: riksa
 * Date: 2/7/13
 * Time: 11:12 AM
 */
public class IrsshiSQLiteOpenHelper extends SQLiteOpenHelper implements TermHostDao {
    private static final Logger log = LoggerFactory.getLogger(IrsshiSQLiteOpenHelper.class);

    // Database
    private static final String DB_NAME = "irsshidb";
    private static final int DB_VERSION = 2;

    // Tables
    private static final String TABLE_HOSTS = "hosts";

    // Columns
    public static interface HOSTS_COLUMNS {
        public static final String _ID_ = "_id";
        public static final String TYPE = "hosttype";
        public static final String NICKNAME = "nickname";
        public static final String HOSTNAME = "hostname";
        public static final String PORT = "port";
        public static final String USERNAME = "username";
    }

    public IrsshiSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_HOSTS + "("
                + HOSTS_COLUMNS._ID_ + " INTEGER PRIMARY KEY,"
                + HOSTS_COLUMNS.TYPE + " TEXT,"
                + HOSTS_COLUMNS.NICKNAME + " TEXT,"
                + HOSTS_COLUMNS.HOSTNAME + " TEXT,"
                + HOSTS_COLUMNS.PORT + " INTEGER,"
                + HOSTS_COLUMNS.USERNAME + " TEXT"
                + ")";

        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_HOSTS);

        onCreate(sqLiteDatabase);

        addHost(new SshTermHost("some.host.ssh", "someone"));
        addHost(new SshTermHost("another.host.ssh", "someoneelse", 2222));
        addHost(new MoshTermHost("some.host.mosh", "foomosh"));
        addHost(new MoshTermHost("another.host.mosh", "barmosh", 1111));
        addHost(new LocalTermHost());
        addHost(new LocalTermHost("android"));

//        log.error("TODO: onUpgrade", new Throwable());
//        assert false;
    }


    @Override
    public List<TermHost> getHosts() {
        List<TermHost> hosts = new ArrayList<TermHost>();
        // Select All Query
        Cursor cursor = getCursor();

        if (cursor.moveToFirst()) {
            do {
                String hosttype = getString(cursor, HOSTS_COLUMNS.TYPE);
                TermHost host = TermHostFactory.create(hosttype);
                host.setId(getInt(cursor, HOSTS_COLUMNS._ID_));
                host.setNickName(getString(cursor, HOSTS_COLUMNS.NICKNAME));
                host.setHostName(getString(cursor, HOSTS_COLUMNS.HOSTNAME));
                host.setPort(getInt(cursor, HOSTS_COLUMNS.PORT));
                host.setUserName(getString(cursor, HOSTS_COLUMNS.USERNAME));
                hosts.add(host);
            } while (cursor.moveToNext());
        }

        return hosts;
    }

    private String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    private int getInt(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    @Override
    public void addHost(TermHost termHost) {
        if (termHost != null) {
            ContentValues values = new ContentValues();
            values.put(HOSTS_COLUMNS.HOSTNAME, termHost.getHostName());
            values.put(HOSTS_COLUMNS.TYPE, termHost.getHostType().name());
            values.put(HOSTS_COLUMNS.NICKNAME, termHost.getNickName());
            values.put(HOSTS_COLUMNS.PORT, termHost.getPort());
            values.put(HOSTS_COLUMNS.USERNAME, termHost.getUserName());

            SQLiteDatabase writableDatabase = this.getWritableDatabase();
            writableDatabase.insert(TABLE_HOSTS, null, values);
            writableDatabase.close();
        }
    }

    @Override
    public void removeHost(TermHost termHost) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Cursor getCursor() {
        String selectQuery = "SELECT  * FROM " + TABLE_HOSTS;

        // TODO: leaked reference?...
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_HOSTS, null, null, null, null, null, HOSTS_COLUMNS._ID_);
    }
}
