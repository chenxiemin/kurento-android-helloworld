package cxm.example.kurento.ui;

import android.support.v4.app.Fragment;

import cxm.example.kurento.util.XLog;

import de.greenrobot.event.EventBus;

/**
 * Created by cxm on 7/31/16.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        XLog.d("onStart: " + this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        XLog.d("onStop: " + this);
        super.onStop();
    }

    @SuppressWarnings("unused")
    public void onEvent(BaseActivity.EventDummy eventDummy) {

    }
}
