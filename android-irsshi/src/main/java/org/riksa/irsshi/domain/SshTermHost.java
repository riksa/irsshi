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
 * Date: 4.2.2013
 * Time: 18:31
 */
public class SshTermHost extends AbstractHost implements TermHost {
    private static final int DEFAULT_SSH_PORT = 22;

    public SshTermHost(Integer id) {
        super(id);
        setPort(getDefaultPort());
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_SSH_PORT;
    }

    @Override
    public HostType getHostType() {
        return HostType.SSH;
    }

}
