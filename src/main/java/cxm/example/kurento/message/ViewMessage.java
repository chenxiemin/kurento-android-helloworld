package cxm.example.kurento.message;

/**
 * Created by xiemchen on 5/12/17.
 */

public class ViewMessage extends Message {
    private String sdpOffer;

    public ViewMessage(String sdpOffer) {
        super("viewer");
        this.sdpOffer = sdpOffer;
    }

    public String getSdpOffer() {
        return sdpOffer;
    }

    public void setSdpOffer(String sdpOffer) {
        this.sdpOffer = sdpOffer;
    }
}
