package com.example.max.websockettest.model;

import org.webrtc.MediaStream;

/**
 * Created by Max on 13-4-2015.
 * Implement this interface to be notified of events.
 */

public interface RtcListener{
    void onCallReceived(String from);

    void onCallReady(String callId);

    void onStatusChanged(String newStatus);

    void onLocalStream(MediaStream localStream);

    void onAddRemoteStream(MediaStream remoteStream);

    void onRemoveRemoteStream(int endPoint);
}