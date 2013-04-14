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

import android.content.Loader;

import java.util.Collection;

/**
 * User: riksa
 * Date: 4.2.2013
 * Time: 18:31
 */
public interface TermHost {

    int getDefaultPort();

    String getIdentityAlias();

    void setIdentityAlias(String identityAlias);

    public enum TermHostValidationError {
        NICKNAME, HOSTNAME, PORT, USERNAME
    }

    Integer getId();

    void setNickName(String string);

    String getNickName();

    boolean validate();

    Collection<TermHostValidationError> getErrors();

    public enum HostType {SSH, MOSH, LOCAL}

    HostType getHostType();

    String getHostName();

    void setHostName(String hostName);

    String getUserName();

    void setUserName(String userName);

    int getPort();

    void setPort(int port);
}
