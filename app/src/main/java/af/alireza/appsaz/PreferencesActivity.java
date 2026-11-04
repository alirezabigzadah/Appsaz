package af.alireza.appsaz;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class PreferencesActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_preferences);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		//SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		//this.isNightModeEnabled = mPrefs.getBoolean("NIGHT_MODE", false);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.settings);

		if (findViewById(R.id.idFrameLayout) != null) {
			if (savedInstanceState != null) {
				return ;
			}
			getSupportFragmentManager().beginTransaction().add(R.id.idFrameLayout,
															   new Preferences()).commit();
		}
	}

}