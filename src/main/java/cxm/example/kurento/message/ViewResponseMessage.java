package cxm.example.kurento.message;

/**
 * Created by xiemchen on 5/12/17.
 */

public class ViewResponseMessage extends Message {
    public static final String ACCEPT = "accepted";

    private String sdpAnswer;
    private String response;

    public ViewResponseMessage() {
        super("viewerResponse");
    }

    public String getSdpAnswer() {
        return sdpAnswer;
    }

    public void setSdpAnswer(String sdpAnswer) {
        this.sdpAnswer = sdpAnswer;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
