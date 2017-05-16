package com.example.max.websockettest.model;

import com.example.max.websockettest.model.message.ReceiveIceCandidateMessage;
import com.example.max.websockettest.model.message.Message;
import com.example.max.websockettest.model.message.StartResponseMessage;
import com.example.max.websockettest.model.message.StopMessage;
import com.google.gson.Gson;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.greenrobot.event.EventBus;

/**
 * Created by xiemchen on 5/12/17.
 */

public class SignalChannel implements AsyncHttpClient.WebSocketConnectCallback, WebSocket.StringCallback {
    private String wsUrl;

    private WebSocket webSocket;

    public SignalChannel(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public int init() {
        SSLContext sslContext = null;
        TrustManager[] trustManagers = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            trustManagers = new TrustManager[] {
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                    }
            };
            try {
                sslContext.init(null, trustManagers, null);
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        AsyncHttpClient asyncHttpClient = AsyncHttpClient.getDefaultInstance();
        asyncHttpClient.getSSLSocketMiddleware().setSSLContext(sslContext);
        asyncHttpClient.getSSLSocketMiddleware().setTrustManagers(trustManagers);
        asyncHttpClient.getSSLSocketMiddleware().setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        asyncHttpClient.websocket(wsUrl, null, this);

        return 0;
    }

    public void deinit() {
        if (null != webSocket) {
            this.sendMessage(new StopMessage());
            webSocket.close();
        }
        webSocket = null;
    }

    public void sendMessage(Message message) {
        if (null == webSocket || null == message)
            throw new IllegalStateException("webSocket");

        Gson gson = new Gson();
        String messageString = gson.toJson(message);
        webSocket.send(messageString);

        XLog.i("sendMessage: " + messageString);
    }

    @Override
    public void onCompleted(Exception ex, WebSocket webSocket) {
        XLog.d("onComplete: " + webSocket);
        if (null != webSocket) {
            webSocket.setStringCallback(this);
            this.webSocket = webSocket;
            EventBus.getDefault().post(new EventOnConnect());
        } else {
            EventBus.getDefault().post(new EventOnDisconnect());
        }
    }

    @Override
    public void onStringAvailable(String s) {
        XLog.d("onStringAvailable: " + s);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            String id = jsonObject.getString("id");

            if ("startResponse".equals(id)) {
                Gson gson = new Gson();
                StartResponseMessage message = gson.fromJson(s, StartResponseMessage.class);
                EventBus.getDefault().post(message);
            } else if ("iceCandidate".equals(id)) {
                Gson gson = new Gson();
                ReceiveIceCandidateMessage message = gson.fromJson(s, ReceiveIceCandidateMessage.class);
                EventBus.getDefault().post(message);
            }
        } catch (JSONException e) {
            XLog.e("Cannot parse json: " + e);
        }
    }

    public class EventOnConnect {

    }

    public class EventOnDisconnect {

    }
}
