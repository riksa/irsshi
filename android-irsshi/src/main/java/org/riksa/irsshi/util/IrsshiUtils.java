/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
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

    public static class ViewNotFoundException extends Exception {
        public ViewNotFoundException() {
            super();
        }

        public ViewNotFoundException(String man) {
            super(man);
        }
    }
}
