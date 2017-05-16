package com.cxm.kurento.model.message;

/**
 * Created by xiemchen on 5/12/17.
 */

public class StartResponseMessage extends Message {
    private String sdpAnswer;

    public StartResponseMessage() {
        super("startResponse");
    }

    public String getSdpAnswer() {
        return sdpAnswer;
    }

    public void setSdpAnswer(String sdpAnswer) {
        this.sdpAnswer = sdpAnswer;
    }
}
