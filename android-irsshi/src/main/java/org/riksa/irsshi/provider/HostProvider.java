package org.riksa.irsshi.provider;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;

/**
 * User: riksa
 * Date: 2/7/13
 * Time: 11:05 AM
 */
public class HostProvider extends ContentProvider {
    private SQLiteOpenHelper sqLiteOpenHelper;

    private static HashMap<String, String> projectionMap;

    static {
        projectionMap = new HashMap<String, String>();
        projectionMap.put(HostProviderMetaData.HostTableMetaData._ID, HostProviderMetaData.HostTableMetaData._ID);
        projectionMap.put(HostProviderMetaData.HostTableMetaData.TYPE, HostProviderMetaData.HostTableMetaData.TYPE);
        projectionMap.put(HostProviderMetaData.HostTableMetaData.NICKNAME, HostProviderMetaData.HostTableMetaData.NICKNAME);
        projectionMap.put(HostProviderMetaData.HostTableMetaData.HOSTNAME, HostProviderMetaData.HostTableMetaData.HOSTNAME);
        projectionMap.put(HostProviderMetaData.HostTableMetaData.PORT, HostProviderMetaData.HostTableMetaData.PORT);
        projectionMap.put(HostProviderMetaData.HostTableMetaData.USERNAME, HostProviderMetaData.HostTableMetaData.USERNAME);
    }

    private static final UriMatcher uriMatcher;
    private static final int URI_DIR = 1;
    private static final int URI_ITEM = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(HostProviderMetaData.AUTHORITY, "hosts", URI_DIR);
        uriMatcher.addURI(HostProviderMetaData.AUTHORITY, "hosts/#", URI_ITEM);
    }

    @Override
    public boolean onCreate() {
        sqLiteOpenHelper = new HostDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(HostProviderMetaData.HostTableMetaData.TABLE_NAME);
        builder.setProjectionMap(projectionMap);
        switch (uriMatcher.match(uri)) {
            case URI_DIR:
                break;
            case URI_ITEM:
                builder.appendWhere(HostProviderMetaData.HostTableMetaData._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = HostProviderMetaData.HostTableMetaData.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, orderBy);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_DIR:
                return HostProviderMetaData.HostTableMetaData.CONTENT_TYPE;
            case URI_ITEM:
                return HostProviderMetaData.HostTableMetaData.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (!contentValues.containsKey(HostProviderMetaData.HostTableMetaData.TYPE)) {
            throw new SQLException("Missing value for " + HostProviderMetaData.HostTableMetaData.TYPE);
        }
        if (!contentValues.containsKey(HostProviderMetaData.HostTableMetaData.PORT)) {
            throw new SQLException("Missing value for " + HostProviderMetaData.HostTableMetaData.PORT);
        }

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        long rowId = db.insert(HostProviderMetaData.HostTableMetaData.TABLE_NAME, null, contentValues);
        if (rowId > 0) {
            Uri insertedUri = ContentUris.withAppendedId(HostProviderMetaData.HostTableMetaData.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(insertedUri, null);
            return insertedUri;
        }

        throw new SQLException("Insert failed for " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case URI_DIR:
                count = db.delete(HostProviderMetaData.HostTableMetaData.TABLE_NAME, where, whereArgs);
                break;
            case URI_ITEM:
                String rowId = uri.getPathSegments().get(1);
                String idWhere = HostProviderMetaData.HostTableMetaData._ID + "=" + rowId;
                if (!TextUtils.isEmpty(where)) {
                    idWhere += " AND (" + where + ")";
                }
                count = db.delete(HostProviderMetaData.HostTableMetaData.TABLE_NAME, idWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String where, String[] whereArgs) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case URI_DIR:
                count = db.update(HostProviderMetaData.HostTableMetaData.TABLE_NAME, contentValues, where, whereArgs);
                break;
            case URI_ITEM:
                String rowId = uri.getPathSegments().get(1);
                String idWhere = HostProviderMetaData.HostTableMetaData._ID + "=" + rowId;
                if (!TextUtils.isEmpty(where)) {
                    idWhere += " AND (" + where + ")";
                }
                count = db.update(HostProviderMetaData.HostTableMetaData.TABLE_NAME, contentValues, idWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    private static class HostDbHelper extends SQLiteOpenHelper {
        private static final Logger log = LoggerFactory.getLogger(HostDbHelper.class);

        public HostDbHelper(Context context) {
            super(context, HostProviderMetaData.DB_NAME, null, HostProviderMetaData.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            log.debug("Creating table");
            String CREATE_TABLE = "CREATE TABLE " + HostProviderMetaData.HostTableMetaData.TABLE_NAME + "("
                    + HostProviderMetaData.HostTableMetaData._ID + " INTEGER PRIMARY KEY,"
                    + HostProviderMetaData.HostTableMetaData.TYPE + " TEXT,"
                    + HostProviderMetaData.HostTableMetaData.NICKNAME + " TEXT,"
                    + HostProviderMetaData.HostTableMetaData.HOSTNAME + " TEXT,"
                    + HostProviderMetaData.HostTableMetaData.PORT + " INTEGER,"
                    + HostProviderMetaData.HostTableMetaData.USERNAME + " TEXT"
                    + ")";

            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            log.warn("Dropping database due to upgrade");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HostProviderMetaData.HostTableMetaData.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }


        /*
        @Deprecated
        public List<TermHost> getHosts() {
            List<TermHost> hosts = new ArrayList<TermHost>();
            // Select All Query
            Cursor cursor = getCursor();

            if (cursor.moveToFirst()) {
                final int typeIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.TYPE);
                final int idIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData._ID);
                final int nickNameIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.NICKNAME);
                final int hostNameIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.HOSTNAME);
                final int portIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.PORT);
                final int userNameIdx = cursor.getColumnIndex(HostProviderMetaData.HostTableMetaData.USERNAME);
                do {
                    String hosttype = cursor.getString(typeIdx);
                    TermHost host = TermHostFactory.create(hosttype);
                    host.setId(cursor.getInt(idIdx));
                    host.setNickName(cursor.getString(nickNameIdx));
                    host.setHostName(cursor.getString(hostNameIdx));
                    host.setPort(cursor.getInt(portIdx));
                    host.setUserName(cursor.getString(userNameIdx));
                    hosts.add(host);
                } while (cursor.moveToNext());
            }

            return hosts;
        }

        public void addHost(TermHost termHost) {
            if (termHost != null) {
                ContentValues values = new ContentValues();
                values.put(HostProviderMetaData.HostTableMetaData.HOSTNAME, termHost.getHostName());
                values.put(HostProviderMetaData.HostTableMetaData.TYPE, termHost.getHostType().name());
                values.put(HostProviderMetaData.HostTableMetaData.NICKNAME, termHost.getNickName());
                values.put(HostProviderMetaData.HostTableMetaData.PORT, termHost.getPort());
                values.put(HostProviderMetaData.HostTableMetaData.USERNAME, termHost.getUserName());

                SQLiteDatabase writableDatabase = this.getWritableDatabase();
                writableDatabase.insert(HostProviderMetaData.HostTableMetaData.TABLE_NAME, null, values);
                writableDatabase.close();
            }
        }

        public void removeHost(TermHost termHost) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public Cursor getCursor() {
            String selectQuery = "SELECT  * FROM " + TABLE_HOSTS;

            // TODO: leaked reference?...
            SQLiteDatabase db = this.getWritableDatabase();
            return db.query(TABLE_HOSTS, null, null, null, null, null, HOSTS_COLUMNS._ID_);
        }
        */
    }

}
