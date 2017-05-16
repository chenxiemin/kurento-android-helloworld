package com.example.max.websockettest.model.message;

/**
 * Created by xiemchen on 5/12/17.
 */

public class StartMessage extends Message {
    private String sdpOffer;

    public StartMessage(String sdpOffer) {
        super("start");
        this.sdpOffer = sdpOffer;
    }

    public String getSdpOffer() {
        return sdpOffer;
    }

    public void setSdpOffer(String sdpOffer) {
        this.sdpOffer = sdpOffer;
    }
}
