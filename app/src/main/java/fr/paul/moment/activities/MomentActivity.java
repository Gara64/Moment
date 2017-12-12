package fr.paul.moment.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.paul.moment.R;
import fr.paul.moment.gallery.GridViewAdapter;
import fr.paul.moment.gallery.ImageItem;
import fr.paul.moment.json.MomentJSON;
import fr.paul.moment.network.DownloadCallback;
import fr.paul.moment.network.NetworkFragment;
import fr.paul.moment.network.Result;
import fr.paul.moment.utils.Consts;

public class MomentActivity extends FragmentActivity implements DownloadCallback {

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    GridView gv;
    ArrayList<ImageItem> imageItems;
    ArrayAdapter<ImageItem> gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);

        imageItems = new ArrayList<>();

        gv = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, imageItems);
        gv.setAdapter(gridAdapter);

        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager());
        startDownload(mNetworkFragment, Consts.BASE_URL + Consts.SERVER_SCRIPT, Consts.CONTENT_TYPE_JSON);
    }


    private void startDownload(NetworkFragment nFragment, String url, String contentType) {
        Log.d("network", "start dl : " + mNetworkFragment);
        Log.d("network", "dl status: " + mDownloading);
        //if (!mDownloading && nFragment != null) {
          if (nFragment != null) {
            // Execute the async download.
            Log.d("network", "go dl");
            nFragment.startDownload(url, contentType);
            //mDownloading = true;
        }
    }

    @Override
    public void updateFromDownload(Object res) {
        Result result = (Result)res;
        if (result != null) {
            if (result.mResultString != null) {
                readJSON(result.mResultString);
            } else if (result.mResultImage != null) {
                ImageView img = new ImageView(this);
                img.setImageBitmap(result.mResultImage);

                imageItems.add(new ImageItem(result.mResultImage, "Image#"));

                //imgList.add(imgList.size(),img);

                // Update the GridView
                gridAdapter.notifyDataSetChanged();

                //ll.addView(img);
            }
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case DownloadCallback.Progress.ERROR:
                break;
            case DownloadCallback.Progress.CONNECT_SUCCESS:
                break;
            case DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                //mDataText.setText("" + percentComplete + "%");
                break;
            case DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }

    @Override
    public void finishDownloading() {
        Log.d("network", "finish it");
        //mDownloading = false;
        // don't null it to download again
        /*if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }*/
    }

    private void readJSON(String json) {
        Log.d("JSON", json);
        MomentJSON moment = new Gson().fromJson(json, MomentJSON.class);
        TextView title = findViewById(R.id.titleView);
        title.setText(moment.title);

        TextView date = findViewById(R.id.dateView);
        date.setText(moment.date);

        TextView desc = findViewById(R.id.descView);
        desc.setText(moment.description);


        if(moment.images != null) {
            Log.d("Network", "n images " + moment.images.length);
            // TODO: we need to create one imageview per image in the list
            // TODO: wht happen with parallel dl ? probably wont work, might need 1 networkfragment per dl
            for(int i=0; i<moment.images.length; i++) {
                Log.d("Network", "path " + moment.images[i]);
                NetworkFragment nFragment = NetworkFragment.getInstance(getSupportFragmentManager());
                startDownload(nFragment, Consts.BASE_URL + moment.images[i], Consts.CONTENT_TYPE_IMAGE);
            }
        }
    }
}
