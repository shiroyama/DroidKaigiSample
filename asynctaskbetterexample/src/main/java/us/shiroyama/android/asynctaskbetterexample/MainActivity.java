package us.shiroyama.android.asynctaskbetterexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask(new MyTask.Callback() {
                    @Override
                    public void onFinished() {
                        textView.setText("task finished");
                    }
                }).execute();
            }
        });
    }

    private static class MyTask extends AsyncTask<Void, Void, Void> {
        private Callback callback;

        private MyTask(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "InterruptedException", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            callback.onFinished();
        }

        private interface Callback {
            void onFinished();
        }
    }
}
