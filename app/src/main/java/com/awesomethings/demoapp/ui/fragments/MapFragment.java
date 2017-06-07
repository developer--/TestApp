package com.awesomethings.demoapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomethings.demoapp.R;
import com.awesomethings.demoapp.events.IMapPageView;
import com.awesomethings.demoapp.presenter.MainPagePresenter;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;
import com.awesomethings.demoapp.ui.activities.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MapFragment extends Fragment  implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, IMapPageView<MarkersDataResponseModel> {

    public static final String VIEW_MODEL = "VIEW_MODEL";
    private GoogleMap mMap;
    private Unbinder unbinder;
    private MainPagePresenter presenter;
    private SparseArray<MarkersDataResponseModel.Properties> propertiesSparseArray = new SparseArray<>();
    private MarkersDataResponseModel.Properties selectedMarkerModel = null;

    @BindView(R.id.loader_wrapper_layout_id) ViewGroup loaderWrapper;
    @BindView(R.id.progress_bar_id) ProgressBar progressBar;
    @BindView(R.id.map_overlay_layout_id) ViewGroup mapOverlayLayout;
    @BindView(R.id.marker_image_view_id) ImageView image1;
    @BindView(R.id.target_name_txt_id) TextView nameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this,view);
        presenter = new MainPagePresenter(getContext(),this);
        SupportMapFragment mapFragment = getMapFragment();
        mapFragment.getMapAsync(this);
        return view;
    }

    @OnClick(R.id.map_overlay_layout_id)
    protected void onMapOverlayClick(final View view){
        final Intent intent = new Intent(getContext(),DetailActivity.class);
        intent.putExtra(VIEW_MODEL, selectedMarkerModel);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), view, "map_overlay_transition");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setOnMarkerClickListener(this);
        this.mMap.setOnMapClickListener(this);
        presenter.startMarkerDataRetrievingRequest();
    }

    @Override
    public void displayLoader() {
        loaderWrapper.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
        loaderWrapper.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(final MarkersDataResponseModel markersDataResponseModel) {
        addMarkers(markersDataResponseModel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onFailed(final String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final int markerHash = marker.hashCode();
        if (propertiesSparseArray.get(markerHash) != null) {
            selectedMarkerModel = propertiesSparseArray.get(markerHash);
            showMapOverlay(selectedMarkerModel);
        }
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        hideMapOverlay();
    }

    private SupportMapFragment getMapFragment() {
        final FragmentManager manager = getChildFragmentManager();
        return (SupportMapFragment) manager.findFragmentById(R.id.map_fragment_id);
    }

    private void showMapOverlay(final MarkersDataResponseModel.Properties propertyModel){
        mapOverlayLayout.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(propertyModel.getImage1())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        nameTextView.setText(propertyModel.getName());
                        return false;
                    }
                })
                .into(image1);
    }

    private void hideMapOverlay(){
        mapOverlayLayout.setVisibility(View.GONE);
    }

    private void addMarkers(final MarkersDataResponseModel markersDataResponseModel){
        try {
            if (! markersDataResponseModel.getProperties().isEmpty()) {
                for (MarkersDataResponseModel.Properties prop : markersDataResponseModel.getProperties()) {
                    final LatLng latLng = prop.getLatLng();
                    assert latLng != null;
                    TextView text = new TextView(getContext());
                    text.setText(prop.getPrice());
                    IconGenerator generator = new IconGenerator(getContext());
                    generator.setContentView(text);
                    Bitmap icon = generator.makeIcon();

                    MarkerOptions tp = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(icon));
                    Marker marker = mMap.addMarker(tp);
                    propertiesSparseArray.put(marker.hashCode(), prop);
//                    final MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(prop.getPrice());
//                    mMap.addMarker(markerOptions);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersDataResponseModel.getProperties().get(0).getLatLng(), 10f));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
