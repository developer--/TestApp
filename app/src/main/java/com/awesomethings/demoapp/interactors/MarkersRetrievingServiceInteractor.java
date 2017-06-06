package com.awesomethings.demoapp.interactors;

import com.awesomethings.demoapp.events.IOnRequestFinishedListener;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;
import com.awesomethings.demoapp.repository.network.NetworkApi;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Iterator;

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
        final Gson gson = new Gson();
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    final ResponseBody body = response.body();
                    if (body != null) {
                        final JSONObject jsonObject = new JSONObject(body.string());
                        final JSONObject propertiesJsonObject = jsonObject.getJSONObject("properties");
                        final Iterator propertiesKey = propertiesJsonObject.keys();
                        final MarkersDataResponseModel responseModel = new MarkersDataResponseModel();

                        while (propertiesKey.hasNext()) {
                            String currentDynamicKey = (String) propertiesKey.next();
                            JSONObject currentDynamicValue = propertiesJsonObject.getJSONObject(currentDynamicKey);
                            MarkersDataResponseModel.Properties properties = gson.fromJson(currentDynamicValue.toString(), MarkersDataResponseModel.Properties.class);
                            responseModel.getProperties().add(properties);
                        }

                        final JSONObject fieldsObject = jsonObject.getJSONObject("fields");
                        final Iterator fieldsKeys = propertiesJsonObject.keys();

                        while (fieldsKeys.hasNext()){
                            String fieldsKey = (String) fieldsKeys.next();
                            JSONObject fieldsValue = fieldsObject.getJSONObject(fieldsKey);
                            MarkersDataResponseModel.Fields fields = gson.fromJson(fieldsValue.toString(), MarkersDataResponseModel.Fields.class);
                            responseModel.getFields().add(fields);
                        }

                        listener.onResponse(responseModel);
                    }else {
                        listener.onResponse(new MarkersDataResponseModel());
                    }
                }catch (Exception e){
                    listener.onFailed(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailed(t.getMessage());
            }
        });

    }
}
