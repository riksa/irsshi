package org.riksa.irsshi.fragment;

import android.app.ListFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.riksa.irsshi.IrsshiApplication;
import org.riksa.irsshi.IrsshiService;
import org.riksa.irsshi.R;
import org.riksa.irsshi.domain.LocalTermHost;
import org.riksa.irsshi.domain.MoshTermHost;
import org.riksa.irsshi.domain.SshTermHost;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: riksa
 * Date: 3.2.2013
 * Time: 12:33
 */
public class HostListFragment extends ListFragment {
    private static final Logger log = LoggerFactory.getLogger(HostListFragment.class);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_list, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        List<TermHost> hosts = IrsshiApplication.getInstance().getTermHostDao().getHosts();
        setListAdapter(HostListSimpleAdapter.create(getActivity(), hosts));

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
                Toast.makeText(getActivity(), "TODO: Delete host " + termHost, Toast.LENGTH_SHORT).show();
                log.debug("Delete host {}", termHost);
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
}
