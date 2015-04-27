package us.shiroyama.android.pausehandler.handler;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * borrowed from the link below
 * http://stackoverflow.com/questions/8040280/how-to-handle-handler-messages-when-activity-fragment-is-paused/25322330#25322330
 */
public abstract class PauseHandler<T> extends Handler {

    private final List<Message> messageQueueBuffer = Collections.synchronizedList(new ArrayList<Message>());

    private T lifeCycle;

    /**
     * call this on lifeCycle's onResume()
     *
     * @param lifeCycle
     */
    public final synchronized void resume(T lifeCycle) {
        this.lifeCycle = lifeCycle;

        while (messageQueueBuffer.size() > 0) {
            final Message msg = messageQueueBuffer.get(0);
            messageQueueBuffer.remove(0);
            sendMessage(msg);
        }
    }

    /**
     * call this on lifeCycle's onPause()
     */
    public final synchronized void pause() {
        lifeCycle = null;
    }

    /**
     * handleMessage is assured to be called within resume - pause
     *
     * @param msg
     */
    @Override
    public final synchronized void handleMessage(Message msg) {
        if (lifeCycle == null) {
            final Message msgCopy = new Message();
            msgCopy.copyFrom(msg);
            messageQueueBuffer.add(msgCopy);
        } else {
            processMessage(lifeCycle, msg);
        }
    }

    /**
     * write code to be done when message is arrived
     *
     * @param lifeCycle
     * @param message
     */
    protected abstract void processMessage(T lifeCycle, Message message);
}
