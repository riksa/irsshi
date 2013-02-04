/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.irsshi.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import org.riksa.irsshi.R;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: riksa
 * Date: 10/18/12
 * Time: 2:59 PM
 */
public class HostListSimpleAdapter extends SimpleAdapter {
    private static final Logger log = LoggerFactory.getLogger(HostListSimpleAdapter.class);
    private static final String KEY_TYPE = "type";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_HOSTNAME = "hostname";
    private static final String KEY_PORT = "port";
    //    private static final String[] from = new String[]{KEY_TYPE, KEY_USERNAME, KEY_HOSTNAME, KEY_PORT};
//    private static final int[] to = new int[]{R.id.col_host_list_type, R.id.col_host_list_username, R.id.col_host_list_hostname, R.id.col_host_list_port};
    private static final String[] from = new String[]{KEY_TYPE};
    private static final int[] to = new int[]{R.id.col_host_list_type};
    private final List<TermHost> hosts;

    public static ListAdapter create(Context context, List<TermHost> hosts) {
        List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
        for (TermHost host : hosts) {
            Map<String, Object> entry = new HashMap<String, Object>();
            entry.put(KEY_TYPE, host.toString());
//            entry.put(KEY_TYPE, host.getHostType().toString());
//            entry.put(KEY_USERNAME, host.getUserName());
//            entry.put(KEY_HOSTNAME, host.getHostName());
//            entry.put(KEY_PORT, Integer.toString(host.getPort()));
            data.add(entry);
        }
        return new HostListSimpleAdapter(context, data, R.layout.row_host_list, from, to, hosts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
//        Map<String, Object> entry = (Map<String, Object>) getItem(position);
        return view;
    }

//    public String getKeyIdAtPosition(int position) {
//        Map<String, Object> entry = (Map<String, Object>) getItem(position);
//        return (String) entry.get(KEYALIAS);
//    }

    private HostListSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, List<TermHost> hosts) {
        super(context, data, resource, from, to);
        this.hosts = hosts;
    }

    public TermHost getTermHostAtPosition(int position) {
        assert position < hosts.size();
        return hosts.get(position);
    }
}
