package cxm.example.kurento.message;

/**
 * Created by xiemchen on 5/12/17.
 */

public class Message {
    private String id;

    public Message(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
