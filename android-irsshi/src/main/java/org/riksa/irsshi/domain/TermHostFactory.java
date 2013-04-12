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

package org.riksa.irsshi.domain;

/**
 * User: riksa
 * Date: 2/7/13
 * Time: 11:43 AM
 */
public class TermHostFactory {
    public static TermHost create(String hostType, Integer id) {
        assert hostType != null;
        TermHost termHost = create(TermHost.HostType.valueOf(hostType), id);
        assert hostType.equals(termHost.getHostType().toString());
        return termHost;
    }

    public static TermHost create(TermHost.HostType hostType, Integer id) {
        assert hostType != null;
        TermHost termHost = null;

        switch (hostType) {
            case MOSH:
                termHost = new MoshTermHost(id);
                break;
            case SSH:
                termHost = new SshTermHost(id);
                break;
            case LOCAL:
                termHost = new LocalTermHost(id);
                break;
            default:
                assert false;
        }

        assert termHost.getHostType() == hostType;

        return termHost;
    }
}
