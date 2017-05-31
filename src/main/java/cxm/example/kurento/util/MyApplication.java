package cxm.example.kurento.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	public static Context context;
	private List<Activity> activities = null;

    @Override
    public void onCreate() {
        super.onCreate();

        MyLog.Log(MyLog.Level.Info, "MyApplication created");
        // save context
        context = getApplicationContext();
        // create activity list
        this.activities = new ArrayList<Activity>();
    }
    
    public void onActivityCreate(Activity activity) {
    	if (this.activities.contains(activity))
    		throw new IllegalStateException("Activity contain");
    	this.activities.add(activity);
    }
    
    public void onActivityDestroy(Activity activity) {
    	if (!this.activities.contains(activity))
    		throw new IllegalStateException("Activity not contain");
    	this.activities.remove(activity);
    }
    
    @Override
    public void onTerminate() {
    	super.onTerminate();
    	
    	MyLog.Log(MyLog.Level.Info, "MyApplication exit");
    }
}
