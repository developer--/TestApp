package com.awesomethings.demoapp.presenter;

import com.awesomethings.demoapp.events.IMapPageView;
import com.awesomethings.demoapp.events.IOnRequestFinishedListener;
import com.awesomethings.demoapp.interactors.MarkersRetrievingServiceInteractor;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;

/**
 * Created by Master on 6/6/17.
 */

public class MainPagePresenter implements IOnRequestFinishedListener<MarkersDataResponseModel> {

    private final MarkersRetrievingServiceInteractor interactor = new MarkersRetrievingServiceInteractor();
    private final IMapPageView<MarkersDataResponseModel> view;

    public MainPagePresenter(final IMapPageView<MarkersDataResponseModel> view){
        this.view = view;
    }

    /**
     * start properties and fields network request
     */
    public void startMarkerDataRetreivingRequest(){
        view.dispplayLoader();
        interactor.startMarkerRetrievingRequest(this);
    }

    @Override
    public void onResponse(MarkersDataResponseModel markersDataResponseModel) {
        if (view != null) {
            view.hideLoader();
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
