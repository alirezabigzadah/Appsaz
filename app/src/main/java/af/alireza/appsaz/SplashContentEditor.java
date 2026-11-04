package af.alireza.appsaz;

import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.MagicalTextView_Master;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class SplashContentEditor extends AppCompatActivity {
	String Info;
	String ID;
	String HeightForChange;
	String FontForChange;
	LinearLayout contentlinear;
	RelativeLayout BgLayout;

	String imagesPath="files/images/";
	String iconsPath="files/icons/";
	String fontsPath="files/fonts/";
	Utilities utils = new Utilities();
	ContentsEditor editor = new ContentsEditor(this);
	String data = "splash_content";

	public String getSomeInt() {
		Random rand = new Random();
		int max = 1000000;
		int min = 0;
		int randomNum1 = rand.nextInt((max - min) + 1) + min;
		int randomNum2 = rand.nextInt((max - min) + 1) + min + randomNum1;
		String rands = String.valueOf(randomNum1) + String.valueOf(randomNum2);
		return String.valueOf(System.currentTimeMillis());
	}
  
    @Override
    protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(utils.applySelectedLanguage(newBase)); // تغییر زبان به فارسی
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		if (Applications.getInstance().isNightModeEnabled(getApplicationContext())) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
		final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_FULLSCREEN
			| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		getWindow().getDecorView().setSystemUiVisibility(flags);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash_content_editor);
		contentlinear = (LinearLayout) findViewById(R.id.splashContent);
		BgLayout = (RelativeLayout) findViewById(R.id.bgLayout);
		MagicalTextView_Master ADD = (MagicalTextView_Master) findViewById(R.id.add);
		String rootPath = getExternalFilesDir(null).getPath() + "/Project/MyApp/assets/";
		String Info = utils.readFile(rootPath + data);
		final String Content = new String(Info);
		Info = Content;	
		final String[] contentarray = Content.split("\\!\\#");
		for (String part : contentarray) {
			String part2=part;
			if (part2.contains("{Splash<=>")) {
				String[] code = part2.replace("}", "").split("<=>");
				String bg = code[1].replace("Background", "");
				if (bg.startsWith("Color")) {
					bg = bg.replace("Color", "");
					BgLayout.setBackgroundColor(Color.parseColor(bg));
				} else if (bg.startsWith("Image")) {
					bg = bg.replace("Image", "");
					if (new File(rootPath + imagesPath + bg).exists()) {
						try {
							FileInputStream In = new FileInputStream(rootPath + imagesPath + bg);
							InputStream inputStream = In;
							BgLayout.setBackgroundDrawable(Drawable.createFromStream(inputStream, ""));
						} catch (FileNotFoundException e) {}
					} else {
						BgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
					}
				} else {
					BgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
				}

			}

		}
		editor.setContentsEditor(rootPath + data, contentlinear, ADD);
	}
}
