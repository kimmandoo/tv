package com.kimmandoo.tv;

import android.os.AsyncTask;
/**
 * AsyncTask<Params, Progress, Result>
 *     Params:   Input parameter type
 *        - arg of {@link #doInBackground}
 *
 *     Progress: Progress parameter type
 *        - arg of {@link #onProgressUpdate}
 *
 *     Result:   Return parameter from background process
 *        - return type of {@link #doInBackground}
 *        - arg of {@link #onPostExecute}
 */
public class CustomAsyncTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = CustomAsyncTask.class.getSimpleName();
    private int mParam;
    public CustomAsyncTask(int param) {
        // Constructor can be used to set field variable.
        mParam = param;
    }
    @Override
    protected void onPreExecute() {
        // Initial UI set up here (if necessary)
    }
    @Override
    protected Void doInBackground(Void... params) {
        // Implement (maybe long) background process here!
        //publishProgress(); // If you want to update UI during this process
        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        // invoked when publishProgress() is called,
        // to update UI of progress during doInBackground is running.
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        // Finally, the result of doInBackground is handled on UI thread here.
    }
}