package cxm.example.kurento.util;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {
	private MyApplication myApp = (MyApplication)this.getApplication();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (null == myApp)
			throw new IllegalStateException("Set MyApplication in Manifest");
		this.myApp.onActivityCreate(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.myApp.onActivityDestroy(this);
	}
}
