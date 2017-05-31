package cxm.example.kurento.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cxm.example.kurento.util.XLog;

import de.greenrobot.event.EventBus;

/**
 * Created by cxm on 7/31/16.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        XLog.i("onCreate: " + this);
    }

    @Override
    public void setContentView(int id) {
        super.setContentView(id);
        ButterKnife.inject(this);
    }

    @Override
    public void onStart() {
        XLog.i("onStart: " + this);
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        XLog.i("onStop: " + this);
    }

    @Override
    public void onResume() {
        XLog.i("onResume: " + this);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        XLog.i("onPause: " + this);
    }

    public static class EventDummy {

    }

    @SuppressWarnings("unused")
    public void onEvent(EventDummy eventDummy) {

    }
}
