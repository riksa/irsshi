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

package org.riksa.a3;

/**
 * User: riksa
 * Date: 8.4.2013
 * Time: 18:55
 */
public interface KeyGeneratorCallback {
    /**
     * Called after a key generation finishes successfully
     * @param alias of the key that was generated
     */
    void succeeded(String alias);

    /**
     * Called if the key generation fails for some reason
     * @param alias alias of the key that was requested
     * @param message the error message
     */
    void failed(String alias, String message);
}
