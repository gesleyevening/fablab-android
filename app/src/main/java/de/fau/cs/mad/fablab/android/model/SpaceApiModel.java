package de.fau.cs.mad.fablab.android.model;

import android.os.Handler;

import net.spaceapi.HackerSpace;
import net.spaceapi.State;

import de.fau.cs.mad.fablab.android.model.events.SpaceApiStateChangedEvent;
import de.fau.cs.mad.fablab.android.model.events.SpaceApiStatePushedEvent;
import de.fau.cs.mad.fablab.rest.core.DoorState;
import de.fau.cs.mad.fablab.rest.myapi.SpaceApi;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpaceApiModel {
    private SpaceApi mSpaceApi;
    private String mSpaceName;
    private long mPollingFrequency;
    private EventBus mEventBus = EventBus.getDefault();
    private Handler mSpaceApiHandler = new Handler();
    private Runnable mSpaceApiRunner = new SpaceApiRunner();
    private boolean mRefreshRequested;

    private boolean mOpen;
    private long mTime;

    private Callback<HackerSpace> mSpaceApiCallback = new Callback<HackerSpace>() {
        @Override
        public void success(HackerSpace hackerSpace, Response response) {
            State state = hackerSpace.getState();
            updateState(state.getOpen(), state.getLastchange());

            mSpaceApiHandler.postDelayed(mSpaceApiRunner, mPollingFrequency);
            mRefreshRequested = false;
        }

        @Override
        public void failure(RetrofitError error) {
            mSpaceApiHandler.postDelayed(mSpaceApiRunner, mPollingFrequency);
            mRefreshRequested = false;
        }
    };

    public SpaceApiModel(SpaceApi spaceApi, String spaceName, long pollingFrequency) {
        mSpaceApi = spaceApi;
        mSpaceName = spaceName;
        mPollingFrequency = pollingFrequency;

        mEventBus.register(this);
        mRefreshRequested = false;
        refreshState();
    }

    private void updateState(boolean open, double lastChange) {
        mOpen = open;

        long currentTimeSeconds = System.currentTimeMillis() / 1000L;
        double minutesSinceLastChange = (currentTimeSeconds - lastChange) / 60;
        mTime = Double.valueOf(minutesSinceLastChange).longValue();

        mEventBus.post(new SpaceApiStateChangedEvent(mOpen, mTime));
    }

    private void refreshState() {
        if (!mRefreshRequested) {
            mRefreshRequested = true;
            mSpaceApiHandler.removeCallbacks(mSpaceApiRunner);
            mSpaceApi.getSpace(mSpaceName, mSpaceApiCallback);
        }
    }

    public boolean getOpen() {
        return mOpen;
    }

    public long getTime() {
        return mTime;
    }

    public void setPollingFrequency(long pollingFrequency) {
        mPollingFrequency = pollingFrequency;
        refreshState();
    }

    private class SpaceApiRunner implements Runnable {

        @Override
        public void run() {
            mSpaceApi.getSpace(mSpaceName, mSpaceApiCallback);
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(SpaceApiStatePushedEvent event) {
        DoorState doorState = event.getDoorState();
        boolean open = doorState.state == DoorState.State.open;
        updateState(open, doorState.time);
    }
}
