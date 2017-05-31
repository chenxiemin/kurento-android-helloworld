package cxm.example.kurento.message;

import org.webrtc.IceCandidate;

/**
 * Created by xiemchen on 5/12/17.
 */

public class ReceiveIceCandidateMessage extends Message {
    private IceCandidateSend candidate;

    public IceCandidateSend getCandidate() {
        return candidate;
    }

    public void setCandidate(IceCandidateSend candidate) {
        this.candidate = candidate;
    }

    public ReceiveIceCandidateMessage() {
        super("iceCandidate");
    }

    public ReceiveIceCandidateMessage(IceCandidate iceCandidate) {
        super("iceCandidate");
        this.candidate = new IceCandidateSend(iceCandidate);
    }

}
