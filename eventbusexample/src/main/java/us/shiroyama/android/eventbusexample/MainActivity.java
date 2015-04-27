package us.shiroyama.android.eventbusexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;


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
                task = new MyTask();
                task.execute();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        Log.d(TAG, "update: " + e.count);
        textView.setText("count: " + e.count);
    }

    @Subscribe
    public void onFinish(FinishEvent e) {
        Log.d(TAG, "finish: " + e.count);
        textView.setText("count: " + e.count);
    }

    private static class MyTask extends AsyncTask<Void, Void, Integer> {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        protected Integer doInBackground(Void... params) {
            int counter = 0;
            for (; counter < 1000; counter++) {
                final int copy = counter;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BusProvider.getInstance().post(new UpdateEvent(copy));
                    }
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException", e);
                }
            }
            return counter;
        }

        @Override
        protected void onPostExecute(Integer count) {
            BusProvider.getInstance().post(new FinishEvent(count));
        }
    }

    private static class UpdateEvent {
        private int count;

        private UpdateEvent(int count) {
            this.count = count;
        }
    }

    private static class FinishEvent {
        private int count;

        private FinishEvent(int count) {
            this.count = count;
        }
    }
}

