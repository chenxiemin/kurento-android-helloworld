package com.example.max.websockettest.model.message;

import org.webrtc.IceCandidate;

/**
 * Created by xiemchen on 5/12/17.
 */

public class SendIceCandidateMessage extends Message {
    private IceCandidateSend candidate;

    public IceCandidateSend getCandidate() {
        return candidate;
    }

    public void setCandidate(IceCandidateSend candidate) {
        this.candidate = candidate;
    }

    public SendIceCandidateMessage() {
        super("onIceCandidate");
    }

    public SendIceCandidateMessage(IceCandidate iceCandidate) {
        super("onIceCandidate");
        this.candidate = new IceCandidateSend(iceCandidate);
    }

}
