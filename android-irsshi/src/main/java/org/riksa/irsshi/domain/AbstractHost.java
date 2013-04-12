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

import android.text.TextUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * User: riksa
 * Date: 2/7/13
 * Time: 11:56 AM
 */
public abstract class AbstractHost implements TermHost {
    private final Integer id;
    private String nickName;
    private String hostName;
    private int port;
    private String userName;
    private final Collection<TermHostValidationError> errors = new HashSet<TermHostValidationError>();

    public AbstractHost(Integer id) {
        this.id = id;
    }

    @Override
    public abstract HostType getHostType();

    public Integer getId() {
        return id;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
        if (getNickName() == null || getNickName().isEmpty()) {
            setNickName(hostName);
        }
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getHostName() {
        return hostName;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getUserName() {
        return userName;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getPort() {
        return port;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return String.format("%s://%s@%s:%d", getHostType(), getUserName(), getHostName(), getPort());
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public boolean validate() {
        errors.clear();
        if (!validateNickName()) {
            errors.add(TermHostValidationError.NICKNAME);
        }
        if (!validateHostName()) {
            errors.add(TermHostValidationError.HOSTNAME);
        }
        if (!validatePort()) {
            errors.add(TermHostValidationError.PORT);
        }
        if (!validateUserName()) {
            errors.add(TermHostValidationError.USERNAME);
        }
        return errors.size() == 0;
    }

    @Override
    public Collection<TermHostValidationError> getErrors() {
        return errors;
    }

    protected boolean validateNickName() {
        return !TextUtils.isEmpty(nickName);
    }

    protected boolean validateHostName() {
        return !TextUtils.isEmpty(hostName);
    }

    protected boolean validateUserName() {
        return !TextUtils.isEmpty(userName);
    }

    protected boolean validatePort() {
        return port > 0 && port < 65536;
    }

}
