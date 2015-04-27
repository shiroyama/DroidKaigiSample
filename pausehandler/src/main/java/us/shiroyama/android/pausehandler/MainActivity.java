package us.shiroyama.android.pausehandler;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;


public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int WHAT_SHOW_DIALOG = 0;
    private static final String KEY_MESSAGE = "key_message";

    private TextView textView;
    private Button button;
    private MyTask task;
    private AlertDialog dialog;

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

        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setCancelable(true)
                .create();
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
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
        super.onDestroy();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        Log.d(TAG, "update: " + e.count);

        Bundle data = new Bundle(1);
        data.putString(KEY_MESSAGE, "update count: " + e.count);
        sendMessage(WHAT_SHOW_DIALOG, data);
    }

    @Subscribe
    public void onFinish(FinishEvent e) {
        Log.d(TAG, "finish: " + e.count);

        Bundle data = new Bundle(1);
        data.putString(KEY_MESSAGE, "finish count: " + e.count);
        sendMessage(WHAT_SHOW_DIALOG, data);
    }

    @Override
    public void processMessage(Message message) {
        if (message.what == WHAT_SHOW_DIALOG) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Bundle data = message.getData();
            dialog.setMessage(data.getString(KEY_MESSAGE));
            dialog.show();
        }
    }

    private static class MyTask extends AsyncTask<Void, Void, Integer> {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        protected Integer doInBackground(Void... params) {
            int counter = 0;
            for (; counter < 10; counter++) {
                final int copy = counter;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BusProvider.getInstance().post(new UpdateEvent(copy));
                    }
                });
                try {
                    Thread.sleep(1000);
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

