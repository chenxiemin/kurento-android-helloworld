package cxm.example.kurento.ui;

import android.app.DialogFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by cxm on 7/31/16.
 */
public class BaseDialogFragment extends DialogFragment {
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @SuppressWarnings("unused")
    public void onEvent(BaseActivity.EventDummy eventDummy) {

    }
}
