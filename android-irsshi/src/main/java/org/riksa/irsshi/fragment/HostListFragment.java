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

package org.riksa.irsshi.fragment;

import android.app.*;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.riksa.irsshi.IrSSHiActivity;
import org.riksa.irsshi.IrsshiApplication;
import org.riksa.irsshi.R;
import org.riksa.irsshi.TermHostDao;
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
    private CursorAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerForContextMenu(getListView());
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterForContextMenu(getListView());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
//        HostListSimpleAdapter adapter = (HostListSimpleAdapter) getListAdapter();
//        TermHost termHost = adapter.getTermHostAtPosition(menuInfo.position);

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long hostId = mAdapter.getItemId(menuInfo.position);
        switch (item.getItemId()) {
            case R.id.menu_delete:
                TermHostDao termHostDao = IrsshiApplication.getIrsshiService().getTermHostDao();
                termHostDao.deleteHost(hostId);
                break;
            case R.id.menu_edit:
                showEditDialog(hostId);
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
                showEditDialog(-1);
                break;
            case R.id.menu_generate_key:
                showGenerateKeyDialog();
                break;
            case R.id.menu_import_key:
                showImportKeyDialog();
                break;
            default:
                log.warn("Unhandled menu item clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGenerateKeyDialog() {
        Toast.makeText(getActivity(), "TODO", Toast.LENGTH_SHORT).show();
//        KeyGenerationDialogFragment dialogFragment = new KeyGenerationDialogFragment();
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        dialogFragment.show(fm, KeyGenerationDialogFragment.TAG);
    }

    private void showImportKeyDialog() {
        Toast.makeText(getActivity(), "TODO", Toast.LENGTH_SHORT).show();
//        KeyGenerationDialogFragment dialogFragment = new KeyGenerationDialogFragment();
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        dialogFragment.show(fm, KeyGenerationDialogFragment.TAG);
    }

    /**
     * Show a dialog for creating/editing a TermHost
     *
     * @param termId id of a TermHost to edit, or -1 to create a new one
     */
    private void showEditDialog(long termId) {
        TermHost termHost = null;
        if (termId != -1) {
            TermHostDao termHostDao = IrsshiApplication.getIrsshiService().getTermHostDao();
            termHost = termHostDao.findHostById(termId);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(TermHostEditDialog.TAG);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);

        DialogFragment dialogFragment = TermHostEditDialog.newInstance(termHost, new TermHostEditListener() {
            @Override
            public void onCancel() {
                log.debug("onCancel");
            }

            @Override
            public void onOk(TermHost termHost) {
                log.debug("onOk {}", termHost);
                TermHostDao termHostDao = IrsshiApplication.getIrsshiService().getTermHostDao();
                if (termHost.getId() == null) {
                    termHostDao.insertHost(termHost);
                } else {
                    termHostDao.updateHost(termHost);
                }
            }
        });
        dialogFragment.show(fragmentTransaction, TermHostEditDialog.TAG);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);    //To change body of overridden methods use File | Settings | File Templates.
//        AdapterView.AdapterContextMenuInfo meuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        long hostId = mAdapter.getItemId(position);

        IrSSHiActivity activity = (IrSSHiActivity) getActivity();
        activity.connectToHost(hostId);
//        Intent intent = new Intent(getActivity(), TerminalsActivity.class);
//        intent.putExtra(TerminalsActivity.EXTRA_HOST_ID, hostId);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
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
