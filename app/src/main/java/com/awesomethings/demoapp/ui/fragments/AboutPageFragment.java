package com.awesomethings.demoapp.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awesomethings.demoapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jemo on 6/7/17.
 */

public class AboutPageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about_page_fragment_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.first_number_container_id)
    public void onFirstNumberCall(){
        makeACall("(302) 525 8222");
    }

    @OnClick(R.id.second_number_container_id)
    public void onSecondNumberCall(){
        makeACall("(650) 336 0000");
    }

    private void makeACall(final String phoneNumber){
        Uri number = Uri.parse("tel:"+phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }
}
