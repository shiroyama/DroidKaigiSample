package us.shiroyama.android.pausehandler;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import us.shiroyama.android.pausehandler.handler.PauseHandler;


public class BaseActivity extends AppCompatActivity {
    private final ActivityPauseHandler pauseHandler = new ActivityPauseHandler();

    @Override
    protected void onResume() {
        super.onResume();
        pauseHandler.resume(this);
    }

    @Override
    protected void onPause() {
        pauseHandler.pause();
        super.onPause();
    }

    public void sendMessage(int what) {
        sendMessage(what, null);
    }

    public void sendMessage(int what, Bundle bundle) {
        Message message = pauseHandler.obtainMessage(what);
        if (bundle != null) {
            message.setData(bundle);
        }
        pauseHandler.sendMessage(message);
    }

    public void processMessage(Message message) {
        // blank implementation
    }

    private static class ActivityPauseHandler extends PauseHandler<BaseActivity> {
        @Override
        protected void processMessage(BaseActivity lifeCycle, Message message) {
            lifeCycle.processMessage(message);
        }
    }
}

