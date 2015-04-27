package us.shiroyama.android.pausehandler;

import com.squareup.otto.Bus;

/**
 * EventBus Provider
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
    }
}
