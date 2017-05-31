package cxm.example.kurento.util;

import java.io.File;

import android.os.Environment;

public class StorageHelper {
	public enum StorageType {
		StorageInternal,
		StorageExtrenal
	}
	
	public static File getFile(StorageType storageType, String name) {
		File file = null;
		if (storageType == StorageType.StorageExtrenal &&
				StorageHelper.isExternalStorageWritable())
			file = new File(MyApplication.context.getExternalFilesDir(null), name);
		else
			file = new File(MyApplication.context.getFilesDir(), name);
		return file;
		
		/*
		// create file to save
		File file = StorageHelper.getFile(StorageHelper.StorageType.StorageExtrenal,
				MyApplication.context.getPackageName());
		try {
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write("cxm".getBytes());
			outputStream.close();
			
			String readResults = "";
			int readData;
			InputStream inputStream = new FileInputStream(file);
			
			while (-1 != (readData = inputStream.read()))
				readResults += (char)readData;
			inputStream.close();
			
			TextView text = (TextView)findViewById(R.id.fullscreen_content);
			text.setText(readResults);
		}
		catch (IOException e) {
			Log.d(MyApplication.context.getPackageName(), "Catch Exception " + e);
		}
		*/
	}
	
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
}
