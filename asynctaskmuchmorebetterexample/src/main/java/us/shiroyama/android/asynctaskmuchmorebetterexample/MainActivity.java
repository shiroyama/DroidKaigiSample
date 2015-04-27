package us.shiroyama.android.asynctaskmuchmorebetterexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView textView;
    private Button button;
    private MyTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new MyTask(new MyTask.Callback() {
                    @Override
                    public void onUpdate(int count) {
                        textView.setText("count: " + count);
                    }

                    @Override
                    public void onFinished(int lastCount) {
                        textView.setText("lastCount: " + lastCount);
                    }
                });
                task.execute();
            }
        });
    }

    @Override
    protected void onDestroy() {
        task.cancel(true);
        super.onDestroy();
    }

    private static class MyTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Callback> callbackRef;

        private static final int LIMIT = 1000;
        private int counter = 0;

        private final Handler handler = new Handler(Looper.getMainLooper());

        private MyTask(Callback callback) {
            callbackRef = new WeakReference<>(callback);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (; counter < LIMIT; counter++) {
                if (isCancelled()) {
                    return null;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final Callback callback = callbackRef.get();
                        if (callback != null) {
                            callback.onUpdate(counter);
                        }
                    }
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException", e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Callback callback = callbackRef.get();
            if (callback != null) {
                callback.onFinished(counter);
            }
        }

        private interface Callback {
            void onUpdate(int count);

            void onFinished(int lastCount);
        }
    }
}

