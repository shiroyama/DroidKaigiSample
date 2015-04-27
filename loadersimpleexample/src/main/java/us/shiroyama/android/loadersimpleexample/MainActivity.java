package us.shiroyama.android.loadersimpleexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private static final String KEY_STRING = "bundle_key_string";

    private TextView textView;
    private Button button;

    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        // should be called before onStart()
        loaderManager = getSupportLoaderManager();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle(1);
                args.putString(KEY_STRING, textView.getText().toString());

                // call restartLoader if you need to force fetch
                loaderManager.initLoader(LOADER_ID, args, MainActivity.this);
            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new MyLoader(getApplicationContext(), args.getString(KEY_STRING));
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.i(TAG, "fetched value: " + data);

        // assured to be called on UI thread.
        textView.setText(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // NOP
    }

    private static class MyLoader extends AsyncTaskLoader<String> {
        private String result;
        private String firstValue;

        private MyLoader(Context context, String firstValue) {
            super(context);
            this.firstValue = firstValue;
        }

        @Override
        public String loadInBackground() {
            // this is useless code, just an example, you know ;)
            Log.i(TAG, "firstValue: " + firstValue);

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Log.e(TAG, "InterruptedException", e);
            }
            return "Data fetched";
        }

        @Override
        public void deliverResult(String data) {
            if (isReset()) {
                if (result != null) {
                    result = null;
                }
                return;
            }

            result = data;

            if (isStarted()) {
                super.deliverResult(data);
            }
        }

        @Override
        protected void onStartLoading() {
            if (result != null) {
                deliverResult(result);
                return;
            }

            if (takeContentChanged() || result == null) {
                forceLoad();
            }
        }

        @Override
        protected void onStopLoading() {
            cancelLoad();
            super.onStopLoading();
        }

        @Override
        protected void onReset() {
            onStopLoading();
            super.onReset();
        }
    }
}

