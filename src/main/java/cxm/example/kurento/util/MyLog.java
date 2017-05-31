package cxm.example.kurento.util;

public class MyLog {
	public enum Level {
		Verbose,
		Debug,
		Info,
		Warning,
		Error
	}
	
	public static void Log(Level level, String message) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String calleeClass = stackTraceElements[3].getClassName();
		calleeClass += " :";
		if (Level.Verbose == level)
			android.util.Log.v(calleeClass, calleeClass + message);
		else if(Level.Debug == level)
			android.util.Log.d(calleeClass, calleeClass + message);
		else if(Level.Info == level)
			android.util.Log.i(calleeClass, calleeClass + message);
		else if(Level.Warning == level)
			android.util.Log.w(calleeClass, calleeClass + message);
		else
			android.util.Log.e(calleeClass, calleeClass + message);
	}
}
