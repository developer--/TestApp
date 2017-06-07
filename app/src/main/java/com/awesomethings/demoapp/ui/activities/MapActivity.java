package com.awesomethings.demoapp.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomethings.demoapp.R;
import com.awesomethings.demoapp.events.IMapPageView;
import com.awesomethings.demoapp.presenter.MainPagePresenter;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, BottomNavigationView.OnNavigationItemSelectedListener, IMapPageView<MarkersDataResponseModel> {

    public static final String VIEW_MODEL = "VIEW_MODEL";
    private GoogleMap mMap;
    private Unbinder unbinder;
    private MainPagePresenter presenter;
    private SparseArray<MarkersDataResponseModel.Properties> propertiesSparseArray = new SparseArray<>();
    private MarkersDataResponseModel.Properties selectedMarkerModel = null;

    @BindView(R.id.loader_wrapper_layout_id) ViewGroup loaderWrapper;
    @BindView(R.id.progress_bar_id) ContentLoadingProgressBar progressBar;
    @BindView(R.id.map_overlay_layout_id) ViewGroup mapOverlayLayout;
    @BindView(R.id.marker_image_view_id) ImageView image1;
    @BindView(R.id.target_name_txt_id) TextView nameTextView;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.main_toolbar_id) Toolbar toolbar;
    @BindView(R.id.drawer_layout_id) DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initDrawer();

        presenter = new MainPagePresenter(this,this);
        SupportMapFragment mapFragment = getMapFragment();
        mapFragment.getMapAsync(this);

    }

    private void initDrawer(){
        try {
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.map_overlay_layout_id)
    protected void onMapOverlayClick(final View view){
        final Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(VIEW_MODEL, selectedMarkerModel);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "map_overlay_transition");
        startActivity(intent, options.toBundle());
    }

    private SupportMapFragment getMapFragment() {
        final FragmentManager manager = getSupportFragmentManager();
        return (SupportMapFragment) manager.findFragmentById(R.id.map_fragment_id);
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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onFailed(final String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
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
                    TextView text = new TextView(this);
                    text.setText(prop.getPrice());
                    IconGenerator generator = new IconGenerator(this);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }
}
