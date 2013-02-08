package org.riksa.irsshi.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.riksa.irsshi.R;
import org.riksa.irsshi.domain.TermHost;

/**
 * User: riksa
 * Date: 2/8/13
 * Time: 12:56 PM
 */
public class TermHostEditDialog extends DialogFragment {
    public static final String TAG = "dialog";
    private TermHostEditListener listener;
    private TermHost termHost;

    public static DialogFragment newInstance(TermHost termHost, TermHostEditListener listener) {
        TermHostEditDialog termHostEditDialog = new TermHostEditDialog();
        termHostEditDialog.setListener(listener);
        termHostEditDialog.setTermHost(termHost);
        return termHostEditDialog;
    }

    private void setListener(TermHostEditListener listener) {
        this.listener = listener;
    }

    private void setTermHost(TermHost termHost) {
        this.termHost = termHost;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_host_dialog, container, false);
        getDialog().setTitle(termHost == null ? R.string.add_host : R.string.edit_host);
        if (termHost != null) {
            TextView textView = (TextView) v.findViewById(R.id.username);
            textView.setText(termHost.getUserName());

            textView = (TextView) v.findViewById(R.id.hostname);
            textView.setText(termHost.getHostName());

            textView = (TextView) v.findViewById(R.id.username);
            textView.setText(termHost.getUserName());

            textView = (TextView) v.findViewById(R.id.port);
            textView.setText(Integer.toString(termHost.getPort()));

            textView = (TextView) v.findViewById(R.id.nickname);
            textView.setText(termHost.getNickName());

            Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
            String[] types = getResources().getStringArray(R.array.host_types);
            int c = 0;
            for (String type : types) {
                if (TextUtils.equals(type, termHost.getHostType().name())) {
                    spinner.setSelection(c);
                    break;
                }
                c++;
            }


        }
        return v;

    }
}
