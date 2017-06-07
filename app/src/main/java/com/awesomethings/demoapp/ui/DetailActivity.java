package com.awesomethings.demoapp.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomethings.demoapp.R;
import com.awesomethings.demoapp.events.bus.MyEventBus;
import com.awesomethings.demoapp.repository.models.cache.CachedData;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;
import com.awesomethings.demoapp.transorm_anim.DepthPageTransformer;
import com.awesomethings.demoapp.ui.adapter.SliderAdapter;
import com.awesomethings.demoapp.ui.custom.CustomFieldItemView;
import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailActivity extends AppCompatActivity {

    private List<CustomFieldItemView> fieldItemViewList = new ArrayList<>();
    private Unbinder unbinder;

    @BindView(R.id.map_overlay_layout_id) ViewGroup mapOverlayLayout;
    @BindView(R.id.target_name_txt_id) TextView nameTextView;
    @BindView(R.id.price_txt_id) TextView priceTextView;
    @BindView(R.id.fields_container_layout) LinearLayout fieldsContainerLayout;
    @BindView(R.id.location_name_txt_id) TextView locationTextView;
    @BindView(R.id.image_slider_pager_id) ViewPager sliderPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_acitivity);
        unbinder = ButterKnife.bind(this);
        final MarkersDataResponseModel.Properties propertyModel = (MarkersDataResponseModel.Properties) getIntent().getSerializableExtra(MainActivity.VIEW_MODEL);
        initUI(propertyModel);
    }

    @Subscribe
    public void onSubmitButtonClick(MyEventBus.FieldsValidCheckerEventModel eventModel){
        if (fieldItemViewList != null){
            boolean isMandatoryFieldsFilled = false;
            for (CustomFieldItemView it : fieldItemViewList){
                try {
                    if (! it.isMandatoryFieldsFilled()){
                        Toast.makeText(this, "Some mandatory field is empty", Toast.LENGTH_SHORT).show();
                        break;
                    }else {
                        isMandatoryFieldsFilled = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isMandatoryFieldsFilled){
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyEventBus.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyEventBus.getBus().unregister(this);
    }

    private void initUI(final MarkersDataResponseModel.Properties propertyModel) {
        if (propertyModel != null){
            nameTextView.setText(propertyModel.getName());
            priceTextView.setText(propertyModel.getPrice());
            locationTextView.setText(propertyModel.getLat() + " : " + propertyModel.getLng());
            final List<String> images = new ArrayList<>();
            images.add(propertyModel.getImage1());
            images.add(propertyModel.getImage2());
            final SliderAdapter adapter = new SliderAdapter(this, images);
            sliderPager.setPageTransformer(true,new DepthPageTransformer());
            sliderPager.setAdapter(adapter);
            if (CachedData.getCachedResponseModel() != null)
                drawCustomFields(CachedData.getCachedResponseModel().getFields());
        }
    }

    private void drawCustomFields(final List<MarkersDataResponseModel.Fields> fields){
        for (MarkersDataResponseModel.Fields it : fields){
            final CustomFieldItemView itemView = new CustomFieldItemView(this);
            itemView.drawField(it);
            fieldItemViewList.add(itemView);
            fieldsContainerLayout.addView(itemView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
