package com.awesomethings.demoapp.events;

/**
 * Created by Master on 6/6/17.
 */

public interface IMapPageView<T> extends IExtraFuncs.Loader {
    void onResponse(final T t);
    void onFailed(final String errorMsg);
}
