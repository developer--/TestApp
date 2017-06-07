package com.awesomethings.demoapp.ui.activities.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.awesomethings.demoapp.R;

/**
 * Created by dev-00 on 6/7/17.
 */

public class BaseActivity extends AppCompatActivity {
    protected void loadFragment(Fragment fragment){
        try {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.replace(R.id.fragment_container_layout_id, fragment, fragment.getTag());
            transaction.addToBackStack(null);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
