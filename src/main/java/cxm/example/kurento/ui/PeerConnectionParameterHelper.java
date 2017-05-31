package cxm.example.kurento.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cxm.example.kurento.R;
import cxm.example.kurento.util.XLog;

import org.appspot.apprtc.PeerConnectionClient;

/**
 * Created by xiemchen on 4/19/17.
 */

public class PeerConnectionParameterHelper {
    public static PeerConnectionClient.PeerConnectionParameters initParams(
            Context context, boolean videoEnable, boolean audioEnable) {
        SharedPreferences sharedPref;
        String keyprefVideoCallEnabled;
        String keyprefCamera2;
        String keyprefResolution;
        String keyprefFps;
        String keyprefCaptureQualitySlider;
        String keyprefVideoBitrateType;
        String keyprefVideoBitrateValue;
        String keyprefVideoCodec;
        String keyprefAudioBitrateType;
        String keyprefAudioBitrateValue;
        String keyprefAudioCodec;
        String keyprefHwCodecAcceleration;
        String keyprefCaptureToTexture;
        String keyprefNoAudioProcessingPipeline;
        String keyprefAecDump;
        String keyprefOpenSLES;
        String keyprefDisableBuiltInAec;
        String keyprefDisplayHud;
        String keyprefTracing;
        String keyprefRoomServerUrl;
        String keyprefRoom;
        String keyprefRoomList;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        keyprefVideoCallEnabled = context.getString(R.string.pref_videocall_key);
        keyprefCamera2 = context.getString(R.string.pref_camera2_key);
        keyprefResolution = context.getString(R.string.pref_resolution_key);
        keyprefFps = context.getString(R.string.pref_fps_key);
        keyprefCaptureQualitySlider = context.getString(R.string.pref_capturequalityslider_key);
        keyprefVideoBitrateType = context.getString(R.string.pref_startvideobitrate_key);
        keyprefVideoBitrateValue = context.getString(R.string.pref_startvideobitratevalue_key);
        keyprefVideoCodec = context.getString(R.string.pref_videocodec_key);
        keyprefHwCodecAcceleration = context.getString(R.string.pref_hwcodec_key);
        keyprefCaptureToTexture = context.getString(R.string.pref_capturetotexture_key);
        keyprefAudioBitrateType = context.getString(R.string.pref_startaudiobitrate_key);
        keyprefAudioBitrateValue = context.getString(R.string.pref_startaudiobitratevalue_key);
        keyprefAudioCodec = context.getString(R.string.pref_audiocodec_key);
        keyprefNoAudioProcessingPipeline = context.getString(R.string.pref_noaudioprocessing_key);
        keyprefAecDump = context.getString(R.string.pref_aecdump_key);
        keyprefOpenSLES = context.getString(R.string.pref_opensles_key);
        keyprefDisableBuiltInAec = context.getString(R.string.pref_disable_built_in_aec_key);
        keyprefDisplayHud = context.getString(R.string.pref_displayhud_key);
        keyprefTracing = context.getString(R.string.pref_tracing_key);
        keyprefRoomServerUrl = context.getString(R.string.pref_room_server_url_key);
        keyprefRoom = context.getString(R.string.pref_room_key);
        keyprefRoomList = context.getString(R.string.pref_room_list_key);

        // Video call enabled flag.
        boolean videoCallEnabled = sharedPref.getBoolean(keyprefVideoCallEnabled,
                Boolean.valueOf(context.getString(R.string.pref_videocall_default)));

        // Use Camera2 option.
        boolean useCamera2 = sharedPref.getBoolean(keyprefCamera2,
                Boolean.valueOf(context.getString(R.string.pref_camera2_default)));

        // Get default codecs.
        String videoCodec = sharedPref.getString(keyprefVideoCodec,
                context.getString(R.string.pref_videocodec_default));
        String audioCodec = sharedPref.getString(keyprefAudioCodec,
                context.getString(R.string.pref_audiocodec_default));

        // Check HW codec flag.
        boolean hwCodec = sharedPref.getBoolean(keyprefHwCodecAcceleration,
                Boolean.valueOf(context.getString(R.string.pref_hwcodec_default)));

        // Check Capture to texture.
        boolean captureToTexture = sharedPref.getBoolean(keyprefCaptureToTexture,
                Boolean.valueOf(context.getString(R.string.pref_capturetotexture_default)));

        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = sharedPref.getBoolean(
                keyprefNoAudioProcessingPipeline,
                Boolean.valueOf(context.getString(R.string.pref_noaudioprocessing_default)));

        // Check Disable Audio Processing flag.
        boolean aecDump = sharedPref.getBoolean(
                keyprefAecDump,
                Boolean.valueOf(context.getString(R.string.pref_aecdump_default)));

        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = sharedPref.getBoolean(
                keyprefOpenSLES,
                Boolean.valueOf(context.getString(R.string.pref_opensles_default)));

        // Check Disable built-in AEC flag.
        boolean disableBuiltInAEC = sharedPref.getBoolean(
                keyprefDisableBuiltInAec,
                Boolean.valueOf(context.getString(R.string.pref_disable_built_in_aec_default)));

        // Get video resolution from settings.
        int videoWidth = 0;
        int videoHeight = 0;
        String resolution = sharedPref.getString(keyprefResolution,
                context.getString(R.string.pref_resolution_default));
        String[] dimensions = resolution.split("[ x]+");
        if (dimensions.length == 2) {
            try {
                videoWidth = Integer.parseInt(dimensions[0]);
                videoHeight = Integer.parseInt(dimensions[1]);
            } catch (NumberFormatException e) {
                videoWidth = 0;
                videoHeight = 0;
                XLog.e("Wrong video resolution setting: " + resolution);
            }
        }

        // Get camera fps from settings.
        int cameraFps = 0;
        String fps = sharedPref.getString(keyprefFps,
                context.getString(R.string.pref_fps_default));
        String[] fpsValues = fps.split("[ x]+");
        if (fpsValues.length == 2) {
            try {
                cameraFps = Integer.parseInt(fpsValues[0]);
            } catch (NumberFormatException e) {
                XLog.e("Wrong camera fps setting: " + fps);
            }
        }

        // Check capture quality slider flag.
        boolean captureQualitySlider = sharedPref.getBoolean(keyprefCaptureQualitySlider,
                Boolean.valueOf(context.getString(R.string.pref_capturequalityslider_default)));

        // Get video and audio start bitrate.
        int videoStartBitrate = 0;
        String bitrateTypeDefault = context.getString(
                R.string.pref_startvideobitrate_default);
        String bitrateType = sharedPref.getString(
                keyprefVideoBitrateType, "");
        if (!bitrateType.equals(bitrateTypeDefault)) {
            String bitrateValue = sharedPref.getString(keyprefVideoBitrateValue,
                    context.getString(R.string.pref_startvideobitratevalue_default));
            videoStartBitrate = Integer.parseInt(bitrateValue);
        }
        int audioStartBitrate = 0;
        bitrateTypeDefault = context.getString(R.string.pref_startaudiobitrate_default);
        bitrateType = sharedPref.getString(
                keyprefAudioBitrateType, bitrateTypeDefault);
        if (!bitrateType.equals(bitrateTypeDefault)) {
            String bitrateValue = sharedPref.getString(keyprefAudioBitrateValue,
                    context.getString(R.string.pref_startaudiobitratevalue_default));
            audioStartBitrate = Integer.parseInt(bitrateValue);
        }

        // Check statistics display option.
        boolean displayHud = sharedPref.getBoolean(keyprefDisplayHud,
                Boolean.valueOf(context.getString(R.string.pref_displayhud_default)));

        boolean tracing = sharedPref.getBoolean(
                keyprefTracing, Boolean.valueOf(context.getString(R.string.pref_tracing_default)));

        return new PeerConnectionClient.PeerConnectionParameters(
                videoCallEnabled,
                audioEnable,
                videoEnable,
                false,
                tracing,
                useCamera2,
                videoWidth,
                videoHeight,
                cameraFps,
                videoStartBitrate,
                videoCodec,
                hwCodec,
                captureToTexture,
                audioStartBitrate,
                audioCodec,
                noAudioProcessing,
                aecDump,
                useOpenSLES,
                disableBuiltInAEC);
    }
}
