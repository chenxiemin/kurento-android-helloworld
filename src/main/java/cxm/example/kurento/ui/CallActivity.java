package cxm.example.kurento.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import cxm.example.kurento.R;

/**
 * Created by cxm on 2/28/17.
 */
public class CallActivity extends BaseActivity {
    public static final String WS_ADDR = "wss://10.140.203.103:8553/";
    public static final String TURN_ADDRESS = "turn:107.182.182.152:3478?transport=udp";
    public static final String TURN_USERNAME = "name";
    public static final String TURN_PASSWORD = "pass";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_call2);

        SelectFragment selectFragment = new SelectFragment();
        getSupportFragmentManager().beginTransaction().replace(
                R.id.call_frame, selectFragment).commit();
    }
}
