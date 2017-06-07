package com.awesomethings.demoapp.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import com.awesomethings.demoapp.R;
import com.awesomethings.demoapp.ui.activities.base.BaseActivity;
import com.awesomethings.demoapp.ui.fragments.AboutPageFragment;
import com.awesomethings.demoapp.ui.fragments.MapFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private Unbinder unbinder;
    private static int selectedMenuItemId = 0;

    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.main_toolbar_id) Toolbar toolbar;
    @BindView(R.id.drawer_layout_id) DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initDrawer();
        loadFragment(new MapFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (selectedMenuItemId != item.getItemId()) {
            switch (item.getItemId()) {
                case R.id.nav_menu_home:
                    loadFragment(new MapFragment());
                    break;
                case R.id.nav_menu_about:
                    loadFragment(new AboutPageFragment());
                    break;
            }
            selectedMenuItemId = item.getItemId();
        }
        drawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
            navigationView.setNavigationItemSelectedListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
