package cxm.example.kurento.util;

import android.os.Environment;

/**
 * Created by cxm on 3/4/17.
 */
public class Directories {
    public static String getMediaPath() {
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + "/ppface";
    }
}
