package com.awesomethings.demoapp.presenter;

import android.content.Context;

import com.awesomethings.demoapp.events.IMapPageView;
import com.awesomethings.demoapp.events.IOnRequestFinishedListener;
import com.awesomethings.demoapp.helpers.HelpersFuncs;
import com.awesomethings.demoapp.interactors.MarkersRetrievingServiceInteractor;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;
import com.awesomethings.demoapp.repository.models.cache.CachedData;

/**
 * Created by Master on 6/6/17.
 */

public class MainPagePresenter implements IOnRequestFinishedListener<MarkersDataResponseModel> {

    private final MarkersRetrievingServiceInteractor interactor = new MarkersRetrievingServiceInteractor();
    private final IMapPageView<MarkersDataResponseModel> view;
    private final Context context;

    public MainPagePresenter(final Context context,final IMapPageView<MarkersDataResponseModel> view){
        this.view = view;
        this.context = context;
    }

    /**
     * start properties and fields network request
     */
    public void startMarkerDataRetrievingRequest(){
        if (HelpersFuncs.isNetworkAvailable(context)) {
            view.displayLoader();
            interactor.startMarkerRetrievingRequest(this);
        }else if (CachedData.getCachedResponseModel() != null){
            view.onResponse(CachedData.getCachedResponseModel());
        }
    }

    @Override
    public void onResponse(final MarkersDataResponseModel markersDataResponseModel) {
        if (view != null) {
            view.hideLoader();
            CachedData.setCachedResponseModel(markersDataResponseModel);
            view.onResponse(markersDataResponseModel);
        }
    }

    @Override
    public void onFailed(String errorMsg) {
        if (view != null) {
            view.hideLoader();
            view.onFailed(errorMsg);
        }
    }

}
