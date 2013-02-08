package org.riksa.irsshi.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import org.riksa.irsshi.R;
import org.riksa.irsshi.domain.TermHost;
import org.riksa.irsshi.logger.LoggerFactory;
import org.riksa.irsshi.util.IrsshiUtils;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 2/8/13
 * Time: 12:56 PM
 */
public class TermHostEditDialog extends DialogFragment {
    private static final Logger log = LoggerFactory.getLogger(TermHostEditDialog.class);
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

    private void setTextViewText(View view, int resId, String text) throws IrsshiUtils.ViewNotFoundException {
        IrsshiUtils.findView(view, TextView.class, resId).setText(text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_host_dialog, container, false);
        getDialog().setTitle(termHost == null ? R.string.add_host : R.string.edit_host);
        if (termHost != null) {
            try {
                setTextViewText(v, R.id.nickname, termHost.getNickName());
                setTextViewText(v, R.id.username, termHost.getUserName());
                setTextViewText(v, R.id.hostname, termHost.getHostName());
                setTextViewText(v, R.id.port, Integer.toString(termHost.getPort()));

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
            } catch (IrsshiUtils.ViewNotFoundException e) {
                throw new RuntimeException(e);
            }


        }
        return v;

    }

}
