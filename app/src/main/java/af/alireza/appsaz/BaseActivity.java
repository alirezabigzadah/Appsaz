package af.alireza.appsaz;

import af.alireza.appsaz.Adapters.FAQAdapter;
import af.alireza.appsaz.Adapters.FAQItem;
import af.alireza.appsaz.Utilities.Prefs;
import af.alireza.appsaz.Utilities.Utilities;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import java.util.List;
import java.util.Random;

public class BaseActivity extends AppCompatActivity {

//  public String rootdirectory1 = "/data/data/af.alireza.appsaz/APPSAZ/";

	public String rootdirectory;
	public static String buildDirectory;
	public String rootAssetsPath;
	public String audiosPath = "files/audios/";
	public String videosPath = "files/videos/";
	public String imagesPath = "files/images/";
	public String iconsPath = "files/icons/";
	public String fontsPath = "files/fonts/";
	public String appsazDownloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Appsaz";
	private Utilities utils = new Utilities();
	private Prefs prefs = new Prefs();
	private static final String SELECTED_LANGUAGE_ID = "SelectedLanguageID";
	private static int PERMISSION_REQUEST_CODE = 1402;
	String defaultLang;
    
   @Override
protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(utils.applySelectedLanguage(newBase)); // تغییر زبان به فارسی
}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* rootdirectory =
		 "/storage/emulated/0/Android/data/af.alireza.appsaz/files/Project/";
		 localDirectory =
		 "/storage/emulated/0/Android/data/af.alireza.appsaz/files/Build/";
		 */
		defaultLang = prefs.getString(getApplicationContext(), SELECTED_LANGUAGE_ID, "default");

		String filesdir = getExternalFilesDir(null).getPath();
		rootdirectory = filesdir + "/Project/";
		buildDirectory = filesdir + "/Build/";
		rootAssetsPath = rootdirectory + "MyApp/assets/";
		if (Applications.getInstance().isNightModeEnabled(getApplicationContext())) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		} else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}
		//        overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

    public boolean checkPer() {
    	if (checkPermission() == true) {
			new MaterialAlertDialogBuilder(this)
				.setTitle(getString(R.string.a_permission_title))
				.setMessage(getString(R.string.a_permission_text))
				.setPositiveButton(getString(R.string.a_permission_grant),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						requestPermission();
					}
				})
				.setNegativeButton(getString(R.string.a_cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();
            return false;
		} else {
			return true;
		}
    }
	private boolean checkPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			return Environment.isExternalStorageManager();
		} else {
			int result =
				ContextCompat.checkSelfPermission(
				getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
			int result1 =
				ContextCompat.checkSelfPermission(
				getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
			return result == PackageManager.PERMISSION_GRANTED
				&& result1 == PackageManager.PERMISSION_GRANTED;
		}
	}

	private void requestPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			try {
				Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(
					Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
				startActivityForResult(intent, PERMISSION_REQUEST_CODE);
			} catch (Exception e) {
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
				startActivityForResult(intent, PERMISSION_REQUEST_CODE);
			}
		} else {
			// below android 11
			ActivityCompat.requestPermissions(BaseActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
		}
	}

	public String getSomeInt() {
		Random rand = new Random();
		int max = 1000000;
		int min = 0;
		int randomNum1 = rand.nextInt((max - min) + 1) + min;
		int randomNum2 = rand.nextInt((max - min) + 1) + min + randomNum1;
		String rands = String.valueOf(randomNum1) + String.valueOf(randomNum2);
		return String.valueOf(System.currentTimeMillis());
	}

	public void messageOk(Context context, String message) {
		/*utils.CustomToastWithIcon(
		 context,
		 message,
		 ContextCompat.getColor(context, R.color.colorToast),
		 16,
		 ContextCompat.getColor(context, R.color.colorToastText),
		 12,
		 Gravity.CENTER,
		 (R.drawable.ic_marked));*/
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onResume() {
		super.onResume();
		//SharedPreferences preferences = getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
		//String languageId = prefs.getString("SelectedLanguageID", "default");
        String languageId = prefs.getString(getApplicationContext(), SELECTED_LANGUAGE_ID, "default");
        if (!defaultLang.equals(languageId)) {
            Intent intent = getIntent();
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			finish();
			startActivity(intent);
        }
	}
    public void showFreeTrialText(Activity context) {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(150, 50, 150, 0);

		final CheckBox checkBox = new CheckBox(context);
		checkBox.setText(getString(R.string.a_dont_show_again));

		layout.addView(checkBox);

		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
		builder.setTitle(getString(R.string.a_first_title))
			.setMessage(getString(R.string.a_first_text))
			.setView(layout)
			.setNegativeButton(getString(R.string.nave_about_us), (dialog, which) -> showAboutDialog(context))
		.setPositiveButton(getString(R.string.a_ok), (dialog, which) -> {
			if (checkBox.isChecked()) {
				prefs.putBoolean(getApplicationContext(), "showFirstText", false);
			}
		})
		.setCancelable(true)
			.show();
	}
	public static void showFAQDialog(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_faq, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<FAQItem> faqItems = FAQAdapter.getFAQItems(context);
        FAQAdapter adapter = new FAQAdapter(faqItems);
        recyclerView.setAdapter(adapter);
        new MaterialAlertDialogBuilder(context)
    	.setTitle(R.string.a_faq)
    	.setView(view)
    	.setPositiveButton(R.string.a_close, (dialog, which) -> dialog.dismiss())
		.show();
    }
    public static void showAboutDialog(Activity context) {
		LayoutInflater inflater = context.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_about, null);
		MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
		dialogBuilder.setTitle(R.string.nave_about_us)
        .setView(dialogView)
        .setPositiveButton(R.string.a_ok, (dialog, which) -> dialog.dismiss())
		.show();

		// حالا می‌توانید از findViewById برای دریافت ویوهای داخل dialogView استفاده کنید
		// TextView text = dialogView.findViewById(R.id.about_text);
		// String txt = getString(R.string.app_saz);
		//  text.setText(Html.fromHtml(txt, Html.FROM_HTML_MODE_LEGACY));
		//  text.setMovementMethod(LinkMovementMethod.getInstance());
		// حالا می‌توانید به yourTextView دسترسی پیدا کنید و تغییراتی روی آن اعمال کنید
	}
    public void showEndTrialDialog(Activity context) {
		new MaterialAlertDialogBuilder(context)
        .setTitle(getString(R.string.a_end_free_trial_title))
        .setMessage(getString(R.string.a_end_free_trial))
    	.setNegativeButton(getString(R.string.nave_about_us), (dialog, which) -> showAboutDialog(context))
        .setPositiveButton(getString(R.string.a_ok), (dialog, which) -> dialog.dismiss())
    	.setCancelable(false)
		.show();
    }
    public void showErrorDialog(Activity context) {
		new MaterialAlertDialogBuilder(context)
    	.setTitle(getString(R.string.a_error))
    	.setMessage(getString(R.string.a_an_error))
    	.setPositiveButton(getString(R.string.a_ok), null)
    	.setCancelable(false)
    	.show();
    }
}
  /*  private void applySelectedLanguage(String languageId) {
    Locale locale;

    if (languageId != null && !languageId.isEmpty()) {
        switch (languageId) {
            case "ru":
                locale = new Locale("ru"); // روسی
                break;
            case "zh":
                locale = new Locale("zh"); // چینی
                break;
            case "hi":
                locale = new Locale("hi"); // هندی
                break;
            case "ar":
                locale = new Locale("ar"); // عربی
                break;
            case "fa":
                locale = new Locale("fa"); // فارسی
                break;
            case "dv":
                locale = new Locale("dv"); // دری
                break;
            case "ps":
                locale = new Locale("ps"); // پشتو
                break;
            case "en":
                locale = Locale.ENGLISH; // انگلیسی
                break;
            default:
                locale = Locale.getDefault(); // شناسایی خودکار زبان گوشی
                break;
        }
    } else {
        locale = Locale.getDefault(); // شناسایی خودکار زبان گوشی
    }

    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.setLocale(locale);

    // تنظیم جهت راست‌چین یا چپ‌چین
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
        if (isRTL(languageId)) {
            config.setLayoutDirection(locale); // راست‌چین
        } else {
            config.setLayoutDirection(Locale.ENGLISH); // چپ‌چین
        }
    }

    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
}

// تابع بررسی زبان راست‌چین
private boolean isRTL(String languageId) {
    return languageId.equals("ar") || languageId.equals("fa") || languageId.equals("dv") || languageId.equals("ps") || languageId.equals("he");
}
}
*/