package af.appsaz.myapplication;

import af.alireza.appsaz.R;
import af.alireza.appsaz.Utilities.Utilities;
import af.appsaz.myapplication.utilities.ContentsReader;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PageActivity extends AppCompatActivity
{
	String data;
    String isFirstPage;
    boolean doubleBackToExitPressedOnce = false;
	LinearLayout contentlinear;
	RelativeLayout BgLayout;
    RelativeLayout menuBg;
	LinearLayout menuContent;
	ActionBar actionBar;
	Toolbar toolbar;
	DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
	String Menumood;
	String Menuname;
    Boolean isMenuActive;
    Boolean isToolbarActive;
    String rootPath;
	String backGround;
	String actionBarTitle;
    String actionBarColor = "#008577";
	//String rootPath = "/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/";
	String audiosPath="files/audios/";
	String videosPath="files/videos/";
	String imagesPath="files/images/";
	String iconsPath="files/icons/";
	String fontsPath="files/fonts/";
	Utilities utils = new Utilities();
	ContentsReader reader = new ContentsReader(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        rootPath = getExternalFilesDir(null).getPath()+"/Project/MyApp/assets/";
        isFirstPage = getIntent().getStringExtra("isFirstPage");
		data = getIntent().getStringExtra("pageName");

        body();
        menu();
		if(Menumood.replace("Mood","").startsWith("On")){
			isMenuActive = true;
			setContentView(R.layout.activity_page1);
			toolbar = findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			drawer = findViewById(R.id.drawer_layout);
			toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.app_name, R.string.app_name);
			drawer.addDrawerListener(toggle);
			toggle.syncState();
			RelativeLayout menuBg = findViewById(R.id.drawer_bg);
			LinearLayout menuContent = findViewById(R.id.drawer_content);
			reader.setContentsReader(rootPath +"menu/"+ Menuname,menuContent, actionBarColor);
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
			String menuData = utils.readFile(rootPath +"menu/"+ Menuname);
			final String mContent = new String(menuData);
			menuData = mContent;	
			final String[] mContentarray = mContent.split("\\!\\#");
			for (String mPart : mContentarray) {
				String mPart2=mPart;
				if (mPart2.contains("{ThisMenu<=>")) {
					String[] mCode = mPart2.replace("}", "").split("<=>");
					String bg = mCode[1].replaceFirst("Background","");
					if (bg.startsWith("Color")) {
						bg=bg.replace("Color","");
						menuBg.setBackgroundColor(Color.parseColor(bg));
					} else if(bg.startsWith("Image")){
						bg=bg.replace("Image","");
						if(new File(rootPath + imagesPath + bg).exists()){
							try {
								FileInputStream In = new FileInputStream(rootPath + imagesPath + bg);
								InputStream inputStream = In;
								menuBg.setBackgroundDrawable(Drawable.createFromStream(inputStream,""));
							} catch (FileNotFoundException e) {}
						}else{
							menuBg.setBackgroundColor(Color.parseColor("#ffffff"));
						}
					}else{
						menuBg.setBackgroundColor(Color.parseColor("#ffffff"));
					}}

			}

		}else{
			isMenuActive = false;
			setContentView(R.layout.activity_page);
			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			actionBar = getSupportActionBar();
		}
        if(isToolbarActive){
			actionBar.setTitle(actionBarTitle);
			if(actionBarColor.startsWith("#")){
				actionBar.setBackgroundDrawable(new ColorDrawable((Color.parseColor(actionBarColor))));
				int color = utils.darkenColor(Color.parseColor(actionBarColor));
				getWindow().setStatusBarColor(color);
				if(isColorDark(Color.parseColor(actionBarColor))){
					toolbar.setTitleTextColor(Color.WHITE);
					if(isMenuActive) toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
				} else {
					toolbar.setTitleTextColor(Color.BLACK);
					if(isMenuActive) toggle.getDrawerArrowDrawable().setColor(Color.BLACK);
				}
			}
		}else{
           getWindow().setStatusBarColor(Color.BLACK);
			actionBar.hide();
		}
		BgLayout = findViewById(R.id.bgLayout);
		if (backGround.startsWith("Color")) {
			backGround=backGround.replace("Color","");
			BgLayout.setBackgroundColor(Color.parseColor(backGround));
		} else if(backGround.startsWith("Image")){
			backGround=backGround.replace("Image","");
			if(new File(rootPath + imagesPath + backGround).exists()){
				try {
					FileInputStream In = new FileInputStream(rootPath + imagesPath + backGround);
					InputStream inputStream = In;
					BgLayout.setBackgroundDrawable(Drawable.createFromStream(inputStream,""));
				} catch (FileNotFoundException e) {}
			}else{
				BgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
			}
		}else{
			BgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
		}
        contentlinear = findViewById(R.id.pageContent);
        reader.setContentsReader(rootPath+data,contentlinear, actionBarColor);
        
	}
	public boolean isColorDark(int color){
		double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
		if(darkness<0.5){
			return false; // It's a light color
		}else{
			return true; // It's a dark color
		}
	}
    @Override
	public void onBackPressed() {
        if(isMenuActive){
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			if (drawer.isDrawerOpen(GravityCompat.START)) {
				drawer.closeDrawer(GravityCompat.START);
			} else {
				checkIsFirst();
			}
		}else{
			checkIsFirst();
		}
	}
    private void checkIsFirst(){
       	if(isFirstPage.equals("Yes")){
			if (doubleBackToExitPressedOnce) {
				super.onBackPressed();
				return;
			}
			doubleBackToExitPressedOnce = true;
			Toast.makeText(this, getString(R.string.a_back_notice), Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						doubleBackToExitPressedOnce=false;
					}
				}, 2000);
		}else{
			finish();
		}
    }
    private void menu(){
		String Info = utils.readFile(rootPath+data);
		final String Content = new String(Info);
		final String[] contentarray = Content.split("\\!\\#");
		for (String part : contentarray) {
			String part2=part;
			if (part2.contains("{Menu<=>")) {
				String[] code = part2.replace("}", "").split("<=>");
				Menumood = code[1].replaceFirst("Mood","");
				Menuname = code[2].replace("Name","");
			}}

    }
    private void body(){
       	String Info = utils.readFile(rootPath+data);
		final String Content = new String(Info);
		Info = Content;	
		final String[] contentarray = Content.split("\\!\\#");
		for (String part : contentarray) {
			String part2=part;
			if (part2.contains("{ThisPage<=>")) {
				String[] code = part2.replace("}", "").split("<=>");
				String bg = code[1].replaceFirst("Background","");
                backGround = bg;
				String actionbar = code[2].replace("ActionBar","");
				if(actionbar.replace("ActionBar","").startsWith("Show")){
					String acbTitle = code[3].replaceFirst("ActionBarTitle","");
					String acbBg = code[4].replace("ActionBarColor","");
                    actionBarTitle = acbTitle;
					actionBarColor = acbBg;
                    isToolbarActive = true;
				} else{
                    isToolbarActive = false;
				}
			}
		}
    }
}
