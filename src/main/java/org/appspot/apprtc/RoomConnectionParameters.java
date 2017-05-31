package org.appspot.apprtc;

/**
 * Struct holding the connection parameters of an AppRTC room.
 */
public class RoomConnectionParameters {
  public final String roomUrl;
  public final String roomId;
  public final boolean loopback;
  public final boolean initiator;

  public String sessionId;
  public String turnServerAddress;
  public String turnUserName;
  public String turnPassword;

//    public String getSessionId() { return sessionId; }
//    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

  public RoomConnectionParameters(
      String roomUrl, String roomId, boolean loopback) {
    this.roomUrl = roomUrl;
    this.roomId = roomId;
    this.loopback = loopback;
    initiator = false;
  }

  public RoomConnectionParameters(
          String roomUrl, String roomId, boolean loopback, boolean initiator) {
    this.roomUrl = roomUrl;
    this.roomId = roomId;
    this.loopback = loopback;
    this.initiator = initiator;
  }
}
