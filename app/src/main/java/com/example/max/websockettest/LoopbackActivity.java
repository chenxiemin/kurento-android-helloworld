package com.example.max.websockettest;

import android.app.Activity;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.max.websockettest.model.SignalChannel;
import com.example.max.websockettest.model.XLog;
import com.example.max.websockettest.model.message.ReceiveIceCandidateMessage;
import com.example.max.websockettest.model.message.SendIceCandidateMessage;
import com.example.max.websockettest.model.message.StartMessage;
import com.example.max.websockettest.model.message.StartResponseMessage;
import com.example.max.websockettest.model.PeerConnectionParameters;
import com.example.max.websockettest.model.RtcListener;

import org.webrtc.AudioSource;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoSource;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class LoopbackActivity extends Activity implements RtcListener, PeerConnection.Observer {
    private static final String TAG = LoopbackActivity.class.getSimpleName();

    private static final String WS_URL = "wss://10.140.203.30:8443/helloworld";

    private final static int VIDEO_CALL_SENT = 666;
    private static final String VIDEO_CODEC_VP9 = "VP8";
    private static final String AUDIO_CODEC_OPUS = "opus";
    // Local preview screen position before call is connected.
    private static final int LOCAL_X_CONNECTING = 0;
    private static final int LOCAL_Y_CONNECTING = 0;
    private static final int LOCAL_WIDTH_CONNECTING = 100;
    private static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    private static final int LOCAL_X_CONNECTED = 72;
    private static final int LOCAL_Y_CONNECTED = 72;
    private static final int LOCAL_WIDTH_CONNECTED = 25;
    private static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    private static final int REMOTE_X = 0;
    private static final int REMOTE_Y = 0;
    private static final int REMOTE_WIDTH = 100;
    private static final int REMOTE_HEIGHT = 100;

    private VideoRendererGui.ScalingType scalingType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;
    private GLSurfaceView vsv;
    private VideoRenderer.Callbacks localRender;
    private VideoRenderer.Callbacks remoteRender;

    private SignalChannel signalChannel;

    public PeerConnection peerConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loopback);

        vsv = (GLSurfaceView) findViewById(R.id.glview_call);
        vsv.setPreserveEGLContextOnPause(true);
        vsv.setKeepScreenOn(true);
        VideoRendererGui.setView(vsv, new Runnable() {
            @Override
            public void run() {
            }
        });

        // local and remote render
        remoteRender = VideoRendererGui.create(
                REMOTE_X, REMOTE_Y,
                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType, false);
        localRender = VideoRendererGui.create(
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING, scalingType, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        signalChannel = new SignalChannel(WS_URL);
        signalChannel.init();
    }

    public void onStop() {
        super.onStop();

        signalChannel.deinit();
        signalChannel = null;

        EventBus.getDefault().unregister(this);
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(SignalChannel.EventOnConnect args) {
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        PeerConnectionParameters params = new PeerConnectionParameters(
                true, true,
                displaySize.x, displaySize.y, 30, 1024 * 512, VIDEO_CODEC_VP9, true,
                1024, AUDIO_CODEC_OPUS, true);

        PeerConnectionFactory.initializeAndroidGlobals(this, true, true,
                params.videoCodecHwAcceleration, VideoRendererGui.getEGLContext());
        PeerConnectionFactory factory = new PeerConnectionFactory();

        // This is my private turn server
        // Please change it to your turn server for your own usage
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(new PeerConnection.IceServer("turn:23.83.240.109:3478", "name", "pass"));

        MediaConstraints pcConstraints = new MediaConstraints();
        pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "OfferToReceiveAudio", "true"));
        pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "OfferToReceiveVideo", "true"));
        pcConstraints.optional.add(new MediaConstraints.KeyValuePair(
                "DtlsSrtpKeyAgreement", "true"));

        peerConnection = factory.createPeerConnection(iceServers, pcConstraints, this);

        MediaStream localMS = factory.createLocalMediaStream("ARDAMS");
        if (params.videoCallEnabled) {
            MediaConstraints videoConstraints = new MediaConstraints();
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                    "maxHeight", Integer.toString(params.videoHeight)));
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                    "maxWidth", Integer.toString(params.videoWidth)));
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                    "maxFrameRate", Integer.toString(params.videoFps)));
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                    "minFrameRate", Integer.toString(params.videoFps)));

            String frontCameraDeviceName = VideoCapturerAndroid.getNameOfFrontFacingDevice();
            VideoSource videoSource = factory.createVideoSource(
                    VideoCapturerAndroid.create(frontCameraDeviceName), videoConstraints);
            localMS.addTrack(factory.createVideoTrack("ARDAMSv0", videoSource));
        }

        AudioSource audioSource = factory.createAudioSource(new MediaConstraints());
        localMS.addTrack(factory.createAudioTrack("ARDAMSa0", audioSource));

        localMS.videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType);

        peerConnection.addStream(localMS);
        peerConnection.createOffer(new SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                peerConnection.setLocalDescription(new SdpObserver() {
                    @Override
                    public void onCreateSuccess(SessionDescription sessionDescription) {
                        XLog.i("onCreateSuccess");
                    }

                    @Override
                    public void onSetSuccess() {
                        XLog.i("onSetSuccess");
                    }

                    @Override
                    public void onCreateFailure(String s) {
                        XLog.i("onCreateFailure: " + s);
                    }

                    @Override
                    public void onSetFailure(String s) {
                        XLog.i("onSetFailure: " + s);
                    }
                }, sessionDescription);

                XLog.i("Create sdp success: " + sessionDescription);
                StartMessage startMessage = new StartMessage(sessionDescription.description);
                signalChannel.sendMessage(startMessage);
            }

            @Override
            public void onSetSuccess() {

            }

            @Override
            public void onCreateFailure(String s) {

            }

            @Override
            public void onSetFailure(String s) {

            }
        }, pcConstraints);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(SignalChannel.EventOnDisconnect args) {
        Toast.makeText(this, "Cannot connect to: " + WS_URL, Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(StartResponseMessage args) {
        SessionDescription description = new SessionDescription(
                SessionDescription.Type.ANSWER, args.getSdpAnswer());
        peerConnection.setRemoteDescription(new SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                XLog.i("onCreateSuccess");
            }

            @Override
            public void onSetSuccess() {
                XLog.i("onSetSuccess");
            }

            @Override
            public void onCreateFailure(String s) {
                XLog.i("onCreateFailure: " + s);
            }

            @Override
            public void onSetFailure(String s) {
                XLog.i("onSetFailure: " + s);
            }
        }, description);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(ReceiveIceCandidateMessage args) {
        peerConnection.addIceCandidate(args.getCandidate().to());
    }

    @Override
    public void onCallReceived(String from) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button answerButton = (Button) findViewById(R.id.call_answer_button);
                answerButton.setVisibility(View.VISIBLE);
                Button rejectButton = (Button) findViewById(R.id.call_reject_button);
                rejectButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onCallReady(String callId) {

    }

    @Override
    public void onStatusChanged(String newStatus) {

    }

    @Override
    public void onLocalStream(MediaStream localStream) {
        Log.d(TAG, "onLocalStream");
        localStream.videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType);
    }

    @Override
    public void onAddRemoteStream(MediaStream remoteStream) {
        Log.d(TAG, "onAddRemoteStream");
        remoteStream.videoTracks.get(0).addRenderer(new VideoRenderer(remoteRender));
        VideoRendererGui.update(remoteRender,
                REMOTE_X, REMOTE_Y,
                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType);
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
                LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED,
                scalingType);
    }

    @Override
    public void onRemoveRemoteStream(int endPoint) {
        Log.d(TAG, "onRemoveRemoteStream");
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType);
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        XLog.i("onSignalingChange");
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        XLog.i("onIceConnectionChange");
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        XLog.i("onIceGatheringChange");
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        XLog.i("onIceCandidate: " + iceCandidate);

        signalChannel.sendMessage(new SendIceCandidateMessage(iceCandidate));
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        mediaStream.videoTracks.get(0).addRenderer(new VideoRenderer(remoteRender));
        VideoRendererGui.update(remoteRender,
                REMOTE_X, REMOTE_Y,
                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType);
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
                LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED,
                scalingType);
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {

    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {

    }

    @Override
    public void onRenegotiationNeeded() {

    }
}
