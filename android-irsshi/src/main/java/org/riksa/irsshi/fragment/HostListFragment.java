package org.riksa.irsshi.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.riksa.irsshi.R;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.provider.HostProviderMetaData;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 3.2.2013
 * Time: 12:33
 */
public class HostListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final Logger log = LoggerFactory.getLogger(HostListFragment.class);
    //    private TermHostDao termHostDao;
    private CursorAdapter mAdapter;
    /*
    private IrsshiService mBoundService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBoundService = ((IrsshiService.LocalBinder) iBinder).getService();
            List<TermHost> hosts = mBoundService.getHosts();
            setListAdapter(HostListSimpleAdapter.create(getActivity(), hosts));
            registerForContextMenu(getListView());
            log.debug("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBoundService = null;
            unregisterForContextMenu(getListView());
            log.debug("onServiceDisconnected");
        }
    };           */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_host_list, container, false);
//        return view;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
//        termHostDao = IrsshiApplication.getInstance().getTermHostDao();
    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
//        List<TermHost> hosts = termHostDao.getHosts();
//        setListAdapter(HostListSimpleAdapter.create(getActivity(), hosts));

        registerForContextMenu(getListView());
//        getActivity().bindService(new Intent(getActivity(), IrsshiService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        unregisterForContextMenu(getListView());

//        if (mBoundService != null) {
//            getActivity().unbindService(mConnection);
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                new String[]{HostProviderMetaData.HostTableMetaData.HOSTNAME, HostProviderMetaData.HostTableMetaData.PORT},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        setListAdapter(mAdapter);

        setListShown(false);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_host_list_options, menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_host_list_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        HostListSimpleAdapter adapter = (HostListSimpleAdapter) getListAdapter();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TermHost termHost = adapter.getTermHostAtPosition(menuInfo.position);

        switch (item.getItemId()) {
            case R.id.menu_delete:
                ContentResolver contentResolver = getActivity().getContentResolver();
                Uri uri = ContentUris.withAppendedId(HostProviderMetaData.HostTableMetaData.CONTENT_URI, termHost.getId());
                int deleted = contentResolver.delete(uri, null, null);
                log.debug("deleted {} rows", deleted);
                break;
            default:
                log.warn("Unhandled menu item clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create:
                log.debug("Add new host");
                Toast.makeText(getActivity(), "TODO: Add new host", Toast.LENGTH_SHORT).show();
                break;
            default:
                log.warn("Unhandled menu item clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);    //To change body of overridden methods use File | Settings | File Templates.
        HostListSimpleAdapter adapter = (HostListSimpleAdapter) getListAdapter();
        TermHost termHost = adapter.getTermHostAtPosition(position);
        log.debug("Connect to {}", termHost);
        Toast.makeText(getActivity(), "TODO: Connect to " + termHost, Toast.LENGTH_SHORT).show();
    }

    // LoaderManager.LoaderCallbacks<Cursor>
// http://developer.android.com/reference/android/app/LoaderManager.html
    static final String[] HOSTS_SUMMARY_PROJECTION = new String[]{
            HostProviderMetaData.HostTableMetaData._ID,
            HostProviderMetaData.HostTableMetaData.NICKNAME,
            HostProviderMetaData.HostTableMetaData.HOSTNAME,
            HostProviderMetaData.HostTableMetaData.PORT,
    };

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri;
        baseUri = HostProviderMetaData.HostTableMetaData.CONTENT_URI;

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = "";
//        ((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
//                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
//                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
        return new CursorLoader(getActivity(), baseUri, HOSTS_SUMMARY_PROJECTION, select, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(cursor);

        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}
