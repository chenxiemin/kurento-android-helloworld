package cxm.example.kurento.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.appspot.apprtc.AppRTCAudioManager;
import org.appspot.apprtc.AppRTCClient;
import org.appspot.apprtc.PeerConnectionClient;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;
import org.webrtc.SurfaceViewRenderer;

import java.util.ArrayList;
import java.util.List;


import butterknife.ButterKnife;
import butterknife.InjectView;

import cxm.example.kurento.R;
import cxm.example.kurento.message.SignalChannel;
import cxm.example.kurento.message.ReceiveIceCandidateMessage;
import cxm.example.kurento.message.SendIceCandidateMessage;
import cxm.example.kurento.message.StartMessage;
import cxm.example.kurento.message.StartResponseMessage;
import cxm.example.kurento.message.StopMessage;
import cxm.example.kurento.util.XLog;

/**
 * Created by cxm on 8/3/16.
 */
public class CallFragment extends BaseFragment implements PeerConnectionClient.PeerConnectionEvents {

    private static final String WS_URL = CallActivity.WS_ADDR + "helloworld";

    @InjectView(R.id.local_video_view)
    SurfaceViewRenderer localRender;

    @InjectView(R.id.remote_video_view)
    SurfaceViewRenderer remoteRender;

    private PeerConnectionClient.PeerConnectionParameters peerConnectionParameters;
    private PeerConnectionClient peerConnectionClient = null;

    private boolean isFinish = false;
    private long startTime = 0;

    private AppRTCAudioManager audioManager = null;

    // video render
    private EglBase rootEglBase;


    private SignalChannel signalChannel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_buddy, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Create video renderers.
        rootEglBase = EglBase.create();
        localRender.init(rootEglBase.getEglBaseContext(), null);
        remoteRender.init(rootEglBase.getEglBaseContext(), null);
        localRender.setZOrderMediaOverlay(true);

        peerConnectionParameters = PeerConnectionParameterHelper.initParams(
                getActivity(), true, true);

        peerConnectionClient = PeerConnectionClient.getInstance();
        peerConnectionClient.createPeerConnectionFactory(
                this.getActivity(), peerConnectionParameters, this);

        signalChannel = new SignalChannel(WS_URL);
        signalChannel.init();
    }

    @Override
    public void onStop() {
        if (null != signalChannel) {
            signalChannel.sendMessage(new StopMessage());
            signalChannel.deinit();
        }
        signalChannel = null;

        if (peerConnectionClient != null) {
            peerConnectionClient.close();
            peerConnectionClient = null;
        }
        if (localRender != null) {
            localRender.release();
            // localRender = null;
        }
        if (remoteRender != null) {
            remoteRender.release();
            // remoteRender = null;
        }
        if (null != rootEglBase) {
            rootEglBase.release();
            rootEglBase = null;
        }

        if (null != audioManager) {
            audioManager.close();
            audioManager = null;
        }

        super.onStop();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(SignalChannel.EventOnConnect args) {
        connectTo();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(SignalChannel.EventOnDisconnect args) {
        Toast.makeText(this.getActivity(),
                "Cannot connect to: " + WS_URL, Toast.LENGTH_LONG).show();
    }

    private void connectTo() {
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(new PeerConnection.IceServer(
                CallActivity.TURN_ADDRESS, CallActivity.TURN_USERNAME, CallActivity.TURN_PASSWORD));

        AppRTCClient.SignalingParameters signalingParameters = new AppRTCClient.SignalingParameters(
                iceServers, false,
                null, null, null,
                null, null);

        peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(),
                localRender, remoteRender, signalingParameters);
        // Create offer. Offer SDP will be sent to answering client in
        // PeerConnectionEvents.onLocalDescription event.
        peerConnectionClient.createOffer();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(StartResponseMessage args) {
        SessionDescription description = new SessionDescription(
                SessionDescription.Type.ANSWER, args.getSdpAnswer());
        peerConnectionClient.setRemoteDescription(description);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final ReceiveIceCandidateMessage args) {
        XLog.d("ReceiveIceCandidateMessage: " + args.getCandidate().to().sdp);

        peerConnectionClient.addRemoteIceCandidate(args.getCandidate().to());
    }

    @Override
    public void onLocalDescription(SessionDescription sdp) {
        XLog.d("onLocalDescription: " + sdp.description);
        StartMessage startMessage = new StartMessage(sdp.description);
        signalChannel.sendMessage(startMessage);
    }

    @Override
    public void onIceCandidate(IceCandidate candidate) {
        XLog.d("onIceCandidate: " + candidate);
        signalChannel.sendMessage(new SendIceCandidateMessage(candidate));
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] candidates) {
        XLog.d("onIceCandidateRemoved: " + candidates);
    }

    @Override
    public void onIceConnected() {
        XLog.d("onIceConnected");
    }

    @Override
    public void onIceDisconnected() {
        XLog.d("onIceDisconnected");
    }

    @Override
    public void onPeerConnectionClosed() {
        XLog.d("onPeerConnectionClosed");
    }

    @Override
    public void onPeerConnectionStatsReady(StatsReport[] reports) {
        XLog.d("onPeerConnectionStatsReady: " + reports);
    }

    @Override
    public void onPeerConnectionError(String description) {
        XLog.d("onPeerConnectionError: " + description);
    }
}
