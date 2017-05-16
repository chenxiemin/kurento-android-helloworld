# kurento-android-helloworld
kurento android helloworld app

How to Start

Step 1:

Make sure you run the kurento helloworld web app before, refer to:
  http://doc-kurento.readthedocs.io/en/stable/tutorials/java/tutorial-helloworld.html

Step 2:

Change the LoopbackActivity.WS_URL to your web app server address:
  private static final String WS_URL = "wss://10.140.203.30:8443/helloworld";
  
Change the LoopbackActivity.TURN_ADDRESS, LoopbackActivity.TURN_USERNAME LoopbackActivity.TURN_PASSWORD to your turn server configuration.

Step 3:

Run to see the loopback video.
