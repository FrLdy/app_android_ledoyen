package fac.app.activities;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fac.app.R;

/**
 * Created by francoisledoyen on 03/02/2018.
 * Gestion d'une galerie d'images sous forme de slider
 */

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> urlImages;
    private ArrayList<Integer> defaultImages;

    public ImageAdapter(Context context, ArrayList<String> urlImages) {
        this.context = context;
        this.urlImages = urlImages;
        if (this.urlImages.isEmpty()){
            this.defaultImages = new ArrayList<>();
            this.defaultImages.add(R.drawable.indispo);
        }
    }

    @Override
    public int getCount() {
        if (this.urlImages.isEmpty()){
            return this.defaultImages.size();
        } else {
            return this.urlImages.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        ImageView mImageView = new ImageView(this.context);

        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        setImageView(mImageView, i);

        container.addView(mImageView, 0);

        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((ImageView) obj);
    }

    public void setImageView(ImageView imageView, int i){
        if (this.urlImages.isEmpty()){
            Picasso.with(this.context).load(this.defaultImages.get(i)).into(imageView);
        } else{
            Picasso.with(this.context).load(this.urlImages.get(i)).into(imageView);
        }
    }
}
