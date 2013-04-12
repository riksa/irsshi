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

package org.riksa.irsshi.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * User: riksa
 * Date: 2/8/13
 * Time: 9:08 AM
 */
public class HostProviderMetaData implements BaseColumns {
    public static final String AUTHORITY = "org.riksa.irsshi.provider.HostProvider";

    // Database
    public static final String DB_NAME = "host.db";
    public static final int DB_VERSION = 1;

    // Tables
//    public static final String TABLE_HOSTS = "hosts";

    private HostProviderMetaData() {
    }

    public static final class HostTableMetaData implements BaseColumns {
        private HostTableMetaData() {
        }

        public static final String TABLE_NAME = "hosts";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/hosts");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.irsshi.host";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.irsshi.host";
        public static final String DEFAULT_SORT_ORDER = _ID;

        // Columns
        public static final String TYPE = "hosttype";
        public static final String NICKNAME = "nickname";
        public static final String HOSTNAME = "hostname";
        public static final String PORT = "port";
        public static final String USERNAME = "username";
    }

}
