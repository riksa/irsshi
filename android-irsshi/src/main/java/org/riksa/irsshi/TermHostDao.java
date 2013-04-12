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

package org.riksa.irsshi;

import org.riksa.irsshi.domain.TermHost;

import java.util.List;

/**
 * User: riksa
 * Date: 5.2.2013
 * Time: 23:18
 */
public interface TermHostDao {
    List<TermHost> getHosts();

    void insertHost(TermHost termHost);

    int deleteHost(long hostId);

    int updateHost(TermHost termHost);

    TermHost findHostById(long hostId);

//    Cursor getCursor();
}
