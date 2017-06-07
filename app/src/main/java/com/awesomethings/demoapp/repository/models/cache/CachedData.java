package com.awesomethings.demoapp.repository.models.cache;

import android.support.annotation.Nullable;

import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;

/**
 * Created by jemo on 6/7/17.
 */

public class CachedData {
    private static MarkersDataResponseModel cachedResponseModel = null;

    private CachedData(){}

    public static void setCachedResponseModel(final MarkersDataResponseModel responseModel){
        cachedResponseModel = responseModel;
    }

    @Nullable
    public static MarkersDataResponseModel getCachedResponseModel() {
        return cachedResponseModel;
    }
}
