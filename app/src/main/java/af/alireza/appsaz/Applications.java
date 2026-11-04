package af.alireza.appsaz;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.security.Security;
import sun1.security.provider.JavaProvider;

public class Applications extends Application {

	private static Applications singleton;

	public static Applications getInstance() {

		if (singleton == null) {
			singleton = new Applications();
		}
		return singleton;
	}

	@Override
	public void onCreate() {
		CrashHandler.init(this);
		super.onCreate();
        Security.addProvider(new JavaProvider());
		singleton = this;
	}

	public boolean isNightModeEnabled(Context c) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(c);
		return mPrefs.getBoolean("prefs_night_mode", false);
	}
}
