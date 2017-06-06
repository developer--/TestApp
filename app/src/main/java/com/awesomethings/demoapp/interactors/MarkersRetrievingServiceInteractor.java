package com.awesomethings.demoapp.interactors;

import com.awesomethings.demoapp.events.IOnRequestFinishedListener;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;
import com.awesomethings.demoapp.repository.network.NetworkApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Master on 6/6/17.
 */

public class MarkersRetrievingServiceInteractor {
    public void startMarkerRetrievingRequest(final IOnRequestFinishedListener<MarkersDataResponseModel> listener){
        final Call<ResponseBody> request = NetworkApi.Factory.getApi().startMarkersRetrievingRequest();
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailed(t.getMessage());
            }
        });

    }
}
