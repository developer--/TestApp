package com.awesomethings.demoapp.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.awesomethings.demoapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by dev-00 on 6/7/17.
 */

public class SliderAdapter extends PagerAdapter {

    private final Context context;
    private final List<String> images;

    public SliderAdapter(final Context context, final List<String> images){
        this.context = context;
        this.images = images;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = LayoutInflater.from(context).inflate(R.layout.slider_item_layout,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.slider_image_view_id);
        if (! images.isEmpty()) {
            Glide.with(context).load(images.get(position)).into(imageView);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
