package com.awesomethings.demoapp.events;

import android.support.annotation.Nullable;

/**
 * Created by Master on 6/6/17.
 */

public interface IOnRequestFinishedListener<T> {
    void onResponse(@Nullable final T t);
    void onFailed(final String errorMsg);
}
