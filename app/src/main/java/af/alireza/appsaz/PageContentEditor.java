package af.alireza.appsaz;

import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.MagicalTextView_Master;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PageContentEditor extends AppCompatActivity {
	String data;

	String ID;
	String HeightForChange;
	String FontForChange;
	String OnClickForChange;
	LinearLayout contentlinear;
	RelativeLayout BgLayout;
	ActionBar actionBar;
	Toolbar toolbar;
	DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
	String Menumood;
	String Menuname;
    Boolean isMenuActive;
	//String rootPath = "/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/";
	String audiosPath="files/audios/";
	String videosPath="files/videos/";
	String imagesPath="files/images/";
	String iconsPath="files/icons/";
	String fontsPath="files/fonts/";
	Utilities utils = new Utilities();
	ContentsEditor editor = new ContentsEditor(this);
	MagicalTextView_Master ADD;

    @Override
    protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(utils.applySelectedLanguage(newBase)); // تغییر زبان به فارسی
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		if (Applications.getInstance().isNightModeEnabled(getApplicationContext())) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

		data = getIntent().getStringExtra("pageName");
		String rootPath = getExternalFilesDir(null).getPath() + "/Project/MyApp/assets/";
		try {
			final String Content = utils.readFile(rootPath + data); 
			final String[] contentarray = Content.split("\\!\\#");
			for (String part : contentarray) {
				String part2=part;
				if (part2.contains("{Menu<=>")) {
					String[] code = part2.replace("}", "").split("<=>");
					Menumood = code[1].replaceFirst("Mood", "");
					Menuname = code[2].replace("Name", "");
					if (Menumood.replace("Mood", "").startsWith("On")) {
                        isMenuActive = true;
						setContentView(R.layout.page_content_editor1);
						toolbar = (Toolbar) findViewById(R.id.toolbar);
						setSupportActionBar(toolbar);
						drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

						MagicalTextView_Master MENUADD = (MagicalTextView_Master) findViewById(R.id.menuAdd);
                        toggle = new ActionBarDrawerToggle(
							this, drawer, toolbar, R.string.app_name, R.string.app_name);
						drawer.addDrawerListener(toggle);
						toggle.syncState();
						RelativeLayout menuBg = (RelativeLayout) findViewById(R.id.drawer_bg);
						LinearLayout menuContent = (LinearLayout) findViewById(R.id.drawer_content);
						editor.setContentsEditor(rootPath + "menu/" + Menuname, menuContent, MENUADD);
						actionBar = getSupportActionBar();
						actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
                        LinearLayout mainPage = findViewById(R.id.main_page);
                        drawer.addDrawerListener(
							new DrawerLayout.DrawerListener() {
								@Override
								public void onDrawerSlide(View drawerView, float slideOffset) {
									float pos = drawerView.getWidth() * slideOffset;
									mainPage.setTranslationX(pos);
								}

								@Override
								public void onDrawerOpened(View drawerView) {}

								@Override
								public void onDrawerClosed(View drawerView) {}

								@Override
								public void onDrawerStateChanged(int newState) {
									// Toast.makeText(MainActivity.this, "state: " + newState,
									// Toast.LENGTH_SHORT).show();
								}
							});
						String menuData = utils.readFile(rootPath + "menu/" + Menuname);
						final String mContent = new String(menuData);
						menuData = mContent;	
						final String[] mContentarray = mContent.split("\\!\\#");
						for (String mPart : mContentarray) {
							String mPart2=mPart;
							if (mPart2.contains("{ThisMenu<=>")) {
								String[] mCode = mPart2.replace("}", "").split("<=>");
								String bg = mCode[1].replaceFirst("Background", "");

								if (bg.startsWith("Color")) {
									bg = bg.replace("Color", "");
									menuBg.setBackgroundColor(Color.parseColor(bg));
								} else if (bg.startsWith("Image")) {
									bg = bg.replace("Image", "");
									if (new File(rootPath + imagesPath + bg).exists()) {
										try {
											FileInputStream In = new FileInputStream(rootPath + imagesPath + bg);
											InputStream inputStream = In;
											menuBg.setBackgroundDrawable(Drawable.createFromStream(inputStream, ""));
										} catch (FileNotFoundException e) {}
									} else {
										menuBg.setBackgroundColor(Color.parseColor("#ffffff"));
									}
								} else {
									menuBg.setBackgroundColor(Color.parseColor("#ffffff"));
								}}

						}

					} else {
                        isMenuActive = false;
						setContentView(R.layout.page_content_editor);
						toolbar = (Toolbar) findViewById(R.id.toolbar);
						setSupportActionBar(toolbar);
						actionBar = getSupportActionBar();
					}
				}}
		} catch (Exception e) {}
		contentlinear = (LinearLayout) findViewById(R.id.pageContent);
		BgLayout = (RelativeLayout) findViewById(R.id.bgLayout);
		ADD = (MagicalTextView_Master) findViewById(R.id.add);
		String Info = utils.readFile(rootPath + data);
		final String Content = new String(Info);
		Info = Content;	
		final String[] contentarray = Content.split("\\!\\#");
		for (String part : contentarray) {
			String part2=part;
			if (part2.contains("{ThisPage<=>")) {
				String[] code = part2.replace("}", "").split("<=>");
				String bg = code[1].replaceFirst("Background", "");
				String actionbar = code[2].replace("ActionBar", "");
				if (actionbar.replace("ActionBar", "").startsWith("Show")) {
					String acbTitle = code[3].replaceFirst("ActionBarTitle", "");
					String acbBg = code[4].replace("ActionBarColor", "");
					actionBar.setTitle(acbTitle);
					if (acbBg.startsWith("#")) {
						actionBar.setBackgroundDrawable(new ColorDrawable((Color.parseColor(acbBg))));
						int color = utils.darkenColor(Color.parseColor(acbBg));
						getWindow().setStatusBarColor(color);

						if (isColorDark(Color.parseColor(acbBg))) {
							toolbar.setTitleTextColor(Color.WHITE);
							if (isMenuActive) toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
						} else {
							toolbar.setTitleTextColor(Color.BLACK);
							if (isMenuActive) toggle.getDrawerArrowDrawable().setColor(Color.BLACK);
						}
					}
				} else {
                    getWindow().setStatusBarColor(Color.BLACK);
					actionBar.hide();
				}
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
				}}

		}
		editor.setContentsEditor(rootPath + data, contentlinear, ADD);
	}
	public boolean isColorDark(int color) {
		double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
		if (darkness < 0.5) {
			return false; // It's a light color
		} else {
			return true; // It's a dark color
		}
	}
}
