package com.awesomethings.demoapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.awesomethings.demoapp.R;
import com.awesomethings.demoapp.events.IMapPageView;
import com.awesomethings.demoapp.presenter.MainPagePresenter;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, IMapPageView<MarkersDataResponseModel> {

    private GoogleMap mMap;
    private MainPagePresenter presenter;

    @BindView(R.id.loader_wrapper_layout_id) ViewGroup loaderWrapper;
    @BindView(R.id.progress_bar_id) ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPagePresenter(this);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = getMapFragment();
        mapFragment.getMapAsync(this);
    }

    /**
     * ამოვიღოთ მეპის ფრაგმენტი
     * @return SupportMapFragment
     */
    private SupportMapFragment getMapFragment() {
        FragmentManager manager = getSupportFragmentManager();
        return (SupportMapFragment) manager.findFragmentById(R.id.map_fragment_id);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.mMap = googleMap;
        presenter.startMarkerDataRetrievingRequest();
    }

    @Override
    public void displayLoader() {
        loaderWrapper.setVisibility(View.VISIBLE);
        progressBar.show();
    }

    @Override
    public void hideLoader() {
        progressBar.hide();
        loaderWrapper.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(final MarkersDataResponseModel markersDataResponseModel) {
        addMarkers(markersDataResponseModel);
    }

    @Override
    public void onFailed(final String errorMsg) {

    }

    private void addMarkers(final MarkersDataResponseModel markersDataResponseModel){
        try {
            if (! markersDataResponseModel.getProperties().isEmpty()) {
                for (MarkersDataResponseModel.Properties prop : markersDataResponseModel.getProperties()) {
                    final LatLng latLng = prop.getLatLng();
                    assert latLng != null;
                    mMap.addMarker(new MarkerOptions().position(latLng).title(prop.getPrice()));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersDataResponseModel.getProperties().get(0).getLatLng(), 13f));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
