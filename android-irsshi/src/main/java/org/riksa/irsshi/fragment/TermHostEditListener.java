/*
 * This file is part of irSSHi - Android SSH client
 * Copyright (c) 2013. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

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
