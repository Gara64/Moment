package fr.paul.moment.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import fr.paul.moment.R;
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

    LinearLayout ll;
    LinearLayout lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);

        lm = (LinearLayout) findViewById(R.id.linearMain);

        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        lm.addView(ll);


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
                ll.addView(img);
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
        MomentJSON moment = new Gson().fromJson(json, MomentJSON.class);
        TextView txt = findViewById(R.id.textView);
        txt.setText(moment.title);

        if(moment.images != null) {
            // TODO: we need to create one imageview per image in the list
            // TODO: wht happen with parallel dl ? probably wont work, might need 1 networkfragment per dl
            for(int i=0; i<moment.images.size(); i++) {
                Log.d("Network", moment.images.get(i).path);
                NetworkFragment nFragment = NetworkFragment.getInstance(getSupportFragmentManager());
                startDownload(nFragment, Consts.BASE_URL + moment.images.get(i).path, Consts.CONTENT_TYPE_IMAGE);
            }
        }
    }
}