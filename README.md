# kurento-android-helloworld
kurento android helloworld / live broadcasting app

How to Start:

Setting the VM args to: -Dkms.url=ws://107.182.182.152:8888/kurento


For kurento-helloworld:

1. Make sure to run the kurento helloworld web app, config the refer to:
  http://doc-kurento.readthedocs.io/en/stable/tutorials/java/tutorial-helloworld.html


2. Change the CallActivity.WS_ADDR to your web app server address:
  
Change the CallActivity.TURN_ADDRESS, CallActivity.TURN_USERNAME CallActivity.TURN_PASSWORD to your turn server configuration if needs.

3. Run to see the loopback video.


For kurento-one2many-call:

1. run the one2many kurento java tutorial, refer to:
  http://doc-kurento.readthedocs.io/en/stable/tutorials/java/tutorial-one2many.html

2. Change WS_ADDR, TURN configuration accroding

3. Start presenter in browser, then run android app to see the browser's video

