package com.awesomethings.demoapp.repository.models.response_models;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jemo on 6/6/17.
 */

public class MarkersDataResponseModel {

    private List<Properties> properties = new ArrayList<>();
    private List<Fields> fields = new ArrayList<>();

    public List<Properties> getProperties() {
        return properties;
    }

    public List<Fields> getFields() {
        return fields;
    }

    public class Properties implements Serializable{

        @SerializedName("name")
        private String name;

        @SerializedName("lat")
        private String lat;

        @SerializedName("lng")
        private String lng;

        @SerializedName("price")
        private String price;

        @SerializedName("image1")
        private String image1;

        @SerializedName("image2")
        private String image2;

        public String getName() {
            return name;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }

        public String getPrice() {
            return price;
        }

        public String getImage1() {
            return image1;
        }

        public String getImage2() {
            return image2;
        }

        @Nullable
        public LatLng getLatLng(){
            try {
                return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public class Fields {
        @SerializedName("name")
        private String name;
        @SerializedName("type")
        private String type;
        @SerializedName("value")
        private String value;
        @SerializedName("mandatory")
        private String mandatory;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public String getMandatory() {
            return mandatory;
        }
    }
}
