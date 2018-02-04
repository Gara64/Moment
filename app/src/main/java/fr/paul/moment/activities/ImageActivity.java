package fr.paul.moment.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import fr.paul.moment.R;
import fr.paul.moment.gallery.ImageItem;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        String title = getIntent().getStringExtra("title");
        int position = getIntent().getIntExtra("position", 0);

        ViewPager viewPager = findViewById(R.id.pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        TextView titleTextView = findViewById(R.id.title);
        titleTextView.setText(title);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return MomentActivity.imageItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((PhotoView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = ImageActivity.this;

            ImageItem imgItem = MomentActivity.imageItems.get(position);
            PhotoView photoView = new PhotoView(context);
            photoView.setImageBitmap(imgItem.getImage());

            ((ViewPager) container).addView(photoView, 0);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((PhotoView) object);
        }
    }
}

