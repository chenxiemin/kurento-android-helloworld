package cxm.example.kurento.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cxm.example.kurento.R;


/**
 * Created by cxm on 8/3/16.
 */
public class SelectFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.loopbackButton)
    public void onLoopback() {
        CallFragment callFragment = new CallFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(
                R.id.call_frame, callFragment).commit();
    }

    @OnClick(R.id.viewBroadcastButton)
    public void onViewBroadcast() {
        CallFragment2 callFragment = new CallFragment2();
        getActivity().getSupportFragmentManager().beginTransaction().replace(
                R.id.call_frame, callFragment).commit();
    }
}
