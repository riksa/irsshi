package org.riksa.irsshi.fragment;

import org.riksa.irsshi.domain.TermHost;

/**
 * User: riksa
 * Date: 2/8/13
 * Time: 1:03 PM
 */
public interface TermHostEditListener {
    void onCancel();

    void onOk(TermHost termHost);
}
