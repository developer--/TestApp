package com.awesomethings.demoapp.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.awesomethings.demoapp.ui.fragments.MapFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailActivity extends AppCompatActivity {

    private List<CustomFieldItemView> fieldItemViewList = new ArrayList<>();
    private Unbinder unbinder;

    @BindView(R.id.target_name_txt_id) TextView nameTextView;
    @BindView(R.id.price_txt_id) TextView priceTextView;
    @BindView(R.id.fields_container_layout) LinearLayout fieldsContainerLayout;
    @BindView(R.id.location_name_txt_id) TextView locationTextView;
    @BindView(R.id.image_slider_pager_id) ViewPager sliderPager;
    @BindView(R.id.detail_page_toolbar_id) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_acitivity);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final MarkersDataResponseModel.Properties propertyModel = (MarkersDataResponseModel.Properties) getIntent().getSerializableExtra(MapFragment.VIEW_MODEL);
        initUI(propertyModel);
    }

    @Subscribe
    public void onSubmitButtonClick(MyEventBus.FieldsValidCheckerEventModel eventModel){
        if (fieldItemViewList != null){
            boolean isMandatoryFieldsFilled = false;
            try {
                for (CustomFieldItemView it : fieldItemViewList){
                    if (! it.isMandatoryFieldsFilled()){
                        isMandatoryFieldsFilled = false;
                        break;
                    }else {
                        isMandatoryFieldsFilled = true;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this, isMandatoryFieldsFilled ? "Success" : "Some mandatory field is empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
