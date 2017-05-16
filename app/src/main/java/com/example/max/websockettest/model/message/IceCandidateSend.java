package com.example.max.websockettest.model.message;

import org.webrtc.IceCandidate;

/**
 * Created by xiemchen on 5/12/17.
 */
public class IceCandidateSend {
    public String candidate;
    public String sdpMid;
    public int sdpMLineIndex;

    public IceCandidateSend(IceCandidate iceCandidate) {
        this.candidate = iceCandidate.sdp;
        this.sdpMid = iceCandidate.sdpMid;
        this.sdpMLineIndex = iceCandidate.sdpMLineIndex;
    }

    public IceCandidate to() {
        IceCandidate iceCandidate = new IceCandidate(sdpMid, sdpMLineIndex, candidate);
        return iceCandidate;
    }
}
