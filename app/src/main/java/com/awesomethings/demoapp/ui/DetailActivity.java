package com.awesomethings.demoapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesomethings.demoapp.R;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.map_overlay_layout_id) ViewGroup mapOverlayLayout;
    @BindView(R.id.marker_image_view_id) ImageView image1;
    @BindView(R.id.target_name_txt_id) TextView nameTextView;
    @BindView(R.id.price_txt_id) TextView priceTextView;
    @BindView(R.id.location_name_txt_id) TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_acitivity);
        unbinder = ButterKnife.bind(this);
        final MarkersDataResponseModel.Properties propertyModel = (MarkersDataResponseModel.Properties) getIntent().getSerializableExtra(MainActivity.VIEW_MODEL);
        if (propertyModel != null){
            Glide.with(this).load(propertyModel.getImage1()).into(image1);
            nameTextView.setText(propertyModel.getName());
            priceTextView.setText(propertyModel.getPrice());
            locationTextView.setText(propertyModel.getLat() + " : " + propertyModel.getLng());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
