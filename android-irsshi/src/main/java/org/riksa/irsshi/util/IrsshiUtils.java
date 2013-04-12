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

package org.riksa.irsshi.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.riksa.irsshi.logger.LoggerFactory;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 19.10.2012
 * Time: 20:48
 */
public class IrsshiUtils {

    public static <T extends View> T findView(Activity activity, Class<T> clazz, int id) throws ViewNotFoundException {
        View view = activity.findViewById(id);
        if (view != null && clazz.isAssignableFrom(view.getClass())) {
            return (T) view;
        }
        throw new ViewNotFoundException("Cannot find view of type " + clazz.toString() + " with resId " + id);
    }

    public static <T extends View> T findView(View v, Class<T> clazz, int id) throws ViewNotFoundException {
        View view = v.findViewById(id);
        if (view != null && clazz.isAssignableFrom(view.getClass())) {
            return (T) view;
        }
        throw new ViewNotFoundException("Cannot find view of type " + clazz.toString() + " with resId " + id);
    }

//    public static <T extends View> T findView(ViewGroup view, Class<? extends View> clazz, int id) throws ViewNotFoundException {
//        //To change body of created methods use File | Settings | File Templates.
//    }

    public static class ViewNotFoundException extends RuntimeException {
        public ViewNotFoundException() {
            super();
        }

        public ViewNotFoundException(String man) {
            super(man);
        }
    }
}
