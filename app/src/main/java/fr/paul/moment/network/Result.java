package fr.paul.moment.network;

import android.graphics.Bitmap;

/**
 * Created by paul on 07/12/17.
 */

public class Result {
    /**
     * Wrapper class that serves as a union of a result value and an exception. When the
     * download task has completed, either the result value or exception can be a non-null
     * value. This allows you to pass exceptions to the UI thread that were thrown during
     * doInBackground().
     */
    public Bitmap mResultImage;
    public String mResultString;
    public Exception mException;
    public Result(Bitmap resultValue) {
        mResultImage = resultValue;
    }
    public Result(String resultValue) {
        mResultString = resultValue;
    }

    public Result(Exception exception) {
        mException = exception;
    }
}
