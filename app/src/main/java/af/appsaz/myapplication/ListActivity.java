package af.appsaz.myapplication;

import af.alireza.appsaz.DatabaseHelper;
import af.alireza.appsaz.R;
import af.alireza.appsaz.Utilities.Utilities;
import af.appsaz.myapplication.utilities.ContentsReader;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.w3c.dom.Text;

public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{ 
	private SearchView searchView;
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService("search");
        searchView = findItem != null ? (SearchView) findItem.getActionView() : null;
		searchView.setOnQueryTextListener(this);
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onQueryTextSubmit(String p1)
	{
		// TODO: Implement this method
		return false;
	}

	@Override
	public boolean onQueryTextChange(String p1)
	{
		// TODO: Implement this method
		Refresh(p1);
		return false;
	}
	DatabaseHelper    myDb; 
	ArrayList<String> Id;
	ArrayList<String> Text;
	ArrayList<String> Description;
	ArrayList<String> Font;
	ArrayList<String> RightIcon;
	ArrayList<String> LeftIcon;
	ArrayList<String> Background;
	ArrayList<String> Radius;
	ArrayList<String> TextColor;
	ArrayList<String> TextSize;
	ArrayList<String> Animation;
	ArrayList<String> Onclick;
	ListView          list;
	String            listName;
	LinearLayout      contentlinear;
	RelativeLayout    BgLayout;
    String            isFirstPage;
    boolean           doubleBackToExitPressedOnce = false;
	String            audiosPath="files/audios/";
	String            videosPath="files/videos/";
	String            imagesPath="files/images/";
	String            iconsPath="files/icons/";
	String            fontsPath="files/fonts/";
	String            Menumood;
	String            Menuname;
    Boolean           isToolbarActive;
    String            rootPath;
	String            backGround;
	String            actionBarTitle;
    String            actionBarColor = "#008577";
	ActionBar         actionBar;
	Toolbar           toolbar;
	DrawerLayout      drawer;
    Utilities         utils = new Utilities();
	ContentsReader    reader = new ContentsReader(this);
	ActionBarDrawerToggle toggle;
    Boolean           isMenuActive;

	@Override 
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        rootPath = getExternalFilesDir(null).getPath()+"/Project/MyApp/assets/";
        isFirstPage = getIntent().getStringExtra("isFirstPage");
		listName = getIntent().getExtras().getString("listName");
		myDb = new DatabaseHelper(this,listName); 
		myDb.open();
		Menumood = myDb.getMenuMood();
		Menuname = myDb.getMenuName();
        actionBarColor = myDb.getActionBarColor();
        myDb.close();
		if(Menumood.startsWith("On")){
            isMenuActive = true;
			setContentView(R.layout.activity_list1);
			drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
			drawer.addDrawerListener(toggle);
			toggle.syncState();
			RelativeLayout menuBg = (RelativeLayout) findViewById(R.id.drawer_bg);
			LinearLayout drawerContent = (LinearLayout) findViewById(R.id.drawer_content);
			reader.setContentsReader(rootPath +"menu/"+ Menuname,drawerContent, actionBarColor);
			actionBar = getSupportActionBar();
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
            LinearLayout mainPage = findViewById(R.id.main_list);
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
			setContentView(R.layout.activity_list);
			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			actionBar = getSupportActionBar();
		}
		BgLayout = (RelativeLayout) findViewById(R.id.listeditorBg);
		list = (ListView) findViewById(R.id.listView);	
        try
		{
			myDb.open();
			String bg =  myDb.getBG();
			if (bg.startsWith("Color")) {
				bg=bg.replace("Color","");
				BgLayout.setBackgroundColor(Color.parseColor(bg));
			} else if(bg.startsWith("Image")){
				bg=bg.replace("Image","");
				FileInputStream In = new FileInputStream(rootPath+"" + imagesPath + bg);
				InputStream inputStream = In;
				BgLayout.setBackgroundDrawable(Drawable.createFromStream(inputStream,""));
			}else{
				BgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
			}
			String actBar =  myDb.getActionBar();
			if (actBar.equals("Show")) {
				actionBarTitle = myDb.getActionBarTitle();
                isToolbarActive = true;
			}else{
                isToolbarActive = false;
			}
			myDb.close();
		}
		catch (FileNotFoundException e)
		{}
        if(isToolbarActive){
			actionBar.setTitle(actionBarTitle);
			if(actionBarColor.startsWith("#")){
				actionBar.setBackgroundDrawable(new ColorDrawable((Color.parseColor(actionBarColor))));
				int color = darkenColor(Color.parseColor(actionBarColor));
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
		Refresh("");
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				private String ID;
				private String TEXT;
				private String DESC;
				private String FONT;
				private String RICON;
				private String LICON;
				private String BACKGROUND;
				private String RADIOS;
				private String TEXTCOLOR;
				private String TEXTSIZE;
				private String ANIMATION;
				private String ONCLICK;

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ID = Id.get(position); 
					TEXT = Text.get(position); 
					DESC = Description.get(position); 
					FONT = Font.get(position); 
					RICON = RightIcon.get(position); 
					LICON = LeftIcon.get(position); 
					BACKGROUND = Background.get(position); 
					RADIOS = Radius.get(position);
					TEXTCOLOR = TextColor.get(position); 
					TEXTSIZE = TextSize.get(position); 
					ANIMATION = Animation.get(position); 
					ONCLICK = Onclick.get(position);

					if(ONCLICK.startsWith("GotoPage")){
						final String str = ONCLICK.replace("GotoPage","");
						if(new File(rootPath+str).exists()){
							Intent intent = new Intent(ListActivity.this, PageActivity.class);
							intent.putExtra("isFirstPage","No");
							intent.putExtra("pageName",str);
							startActivity(intent);
						}

					}else if(ONCLICK.startsWith("GotoList")){
						final String str = ONCLICK.replace("GotoList","");
						if(new File(rootPath+str).exists()){
							Intent intent = new Intent(ListActivity.this, ListActivity.class);
							intent.putExtra("isFirstPage","No");
							intent.putExtra("listName",str);
							startActivity(intent);
						}

					}else if(ONCLICK.startsWith("ShowImage")){
						final String str = ONCLICK.replace("ShowImage","");
						if(new File(rootPath+imagesPath+str).exists()){
							Intent intent = new Intent(ListActivity.this, PictureActivity.class);
							intent.putExtra("actionBarColor",actionBarColor);
							intent.putExtra("picName",str);
							startActivity(intent);
						}
					}else if(ONCLICK.startsWith("PlayAudio")){
						final String str = ONCLICK.replace("PlayAudio","");
						if(new File(rootPath+audiosPath+str).exists()){
							Intent intent = new Intent(ListActivity.this, AudioActivity.class);
							intent.putExtra("actionBarColor",actionBarColor);
							intent.putExtra("audioName",str);
							startActivity(intent);
						}
					}else if(ONCLICK.startsWith("PlayVideo")){
						final String str = ONCLICK.replace("PlayVideo","");
						if(new File(rootPath+videosPath+str).exists()){
							Intent intent = new Intent(ListActivity.this, VideoActivity.class);
							intent.putExtra("actionBarColor",actionBarColor);
							intent.putExtra("videoName",str);
							startActivity(intent);
						}
					}else if(ONCLICK.startsWith("ShowAlert")){
						final String str = ONCLICK.replaceFirst("ShowAlert","");
						final String[] array = str.split("<!>");
						final String dtitle = array[0].replaceFirst("Title","").replace("$¿?¿?&¥","<!>").replace("¥&?¿?¿$","!#");
						final String dtext = array[1].replaceFirst("Text","").replace("$¿?¿?&¥","<!>").replace("¥&?¿?¿$","!#");
						alertDialog(dtitle,dtext);
					}else if(ONCLICK.startsWith("ShowToast")){
						final String str = ONCLICK.replaceFirst("ShowToast","");
						Toast.makeText(ListActivity.this,str,Toast.LENGTH_LONG).show();
					}else if(ONCLICK.startsWith("SendEmail")){
						final String str = ONCLICK.replaceFirst("SendEmail","");
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("message/rfc822");
						String[] emails = str.trim().split(",");
						i.putExtra(Intent.EXTRA_EMAIL, emails);
						i.putExtra(Intent.EXTRA_SUBJECT, "");
						i.putExtra(Intent.EXTRA_TEXT, "");
						try {
							startActivity(Intent.createChooser(i, "ارسال ایمیل..."));
						} catch (android.content.ActivityNotFoundException ex) {
							Toast.makeText(ListActivity.this, "هیچ برنامه ایمیلی پیدا نشد.", Toast.LENGTH_SHORT).show();
						}
					}else if(ONCLICK.startsWith("SendSms")){
						final String str = ONCLICK.replaceFirst("SendSms","");
						String phoneNumber = str;
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
						intent.putExtra("sms_body", "");
						startActivity(intent);
					}else if(ONCLICK.startsWith("OpenLink")){
						final String str = ONCLICK.replaceFirst("OpenLink","");
						String url = str;
						if (!(url.startsWith("http://") || url.startsWith("https://"))) {
							url = "http://" + url;
						}
						Intent i = new Intent("android.intent.action.VIEW");
						i.setData(Uri.parse(url));
						startActivity(i);

					}else if(ONCLICK.startsWith("ShareApp")){
						final String str = ONCLICK.replace("ShareApp","");
						//	shareApplication();
					}else if(ONCLICK.startsWith("Exit")){
						final String str = ONCLICK.replace("Exit","");
						finish();
					}
				} 

			}); 
	}
	int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }
    public boolean isColorDark(int color){
		double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
		if(darkness<0.5){
			return false; // It's a light color
		}else{
			return true; // It's a dark color
		}
    }
	private void Refresh(String data){

		myDb.open();
		Id = myDb.search(0, data);
		Text = myDb.search(1, data);
		Description = myDb.search(2, data);
		Font = myDb.search(3, data);
		RightIcon = myDb.search(4, data);
		LeftIcon = myDb.search(5, data);
		Background = myDb.search(6, data);
		Radius = myDb.search(7, data);
		TextColor = myDb.search(8, data);
		TextSize = myDb.search(9, data);
		Animation = myDb.search(10, data);
		Onclick = myDb.search(11, data);
		myDb.close();
		list.setAdapter(new myAdapter(ListActivity.this, R.layout.list_row, R.id.row_title, Text));
	}
    private class myAdapter extends ArrayAdapter<String>
	{

        public myAdapter(Context context, int resource, int textViewResourceId,
                         ArrayList strings) {
            super(context, resource, textViewResourceId, strings);

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.list_row, parent, false);
			CardView background = (CardView) row.findViewById(R.id.row_bg);
            TextView title = (TextView) row.findViewById(R.id.row_title);
            TextView description = (TextView) row.findViewById(R.id.row_desc);
            ImageView Ricon = (ImageView) row.findViewById(R.id.row_Ricon);
			ImageView Licon = (ImageView) row.findViewById(R.id.row_Licon);
			if(Text.get(position).length()==0){
				title.setVisibility(View.GONE);
			}else{
				title.setText(Text.get(position));
			}
			if(Description.get(position).length()==0){
				description.setVisibility(View.GONE);
			}else{
				description.setText(Description.get(position));
			}           
			String RIcon = RightIcon.get(position);
			String LIcon = LeftIcon.get(position);
			String animation = Animation.get(position);
			String onclick = Onclick.get(position);
			String font = Font.get(position);
			String textColor = TextColor.get(position);
			String textSize = TextSize.get(position);
			String bg = Background.get(position);
			String radios = Radius.get(position);
			if(font.startsWith("font")){
				if(new File(rootPath+""+fontsPath+font).exists()){
					Typeface tf = Typeface.createFromFile(rootPath+""+fontsPath+font);
                    try {
						title.setTypeface(tf);
						description.setTypeface(tf);
					}
					catch (Exception e)
					{

					}
				}

			}
			title.setTextColor(Color.parseColor(textColor));
			description.setTextColor(Color.parseColor(textColor));
			int TextSize = Integer.parseInt (textSize)*2;
			title.setTextSize(TextSize);
			description.setTextSize(TextSize-4);
			background.setRadius(Integer.parseInt(radios)*5);
			if (bg.startsWith("Color")) {
				bg = bg.replace("Color","");
				background.setCardBackgroundColor(Color.parseColor(bg));
			} else if(bg.startsWith("Image")){
				bg=bg.replace("Image","");
				if(new File (rootPath+""+imagesPath + bg).exists()){
					try
					{
						FileInputStream fileIn = new FileInputStream(rootPath+""+imagesPath + bg);
						InputStream BgImg = fileIn;
						background.setBackgroundDrawable(Drawable.createFromStream(BgImg, ""));
					}
					catch (IOException e)
					{}
				}
			}
			if(RIcon.startsWith("icon")){
				if(new File (rootPath+""+iconsPath + RIcon).exists()){
					File In = new File(rootPath+""+iconsPath + RIcon);
					Glide.with(ListActivity.this)
						.load(In)
						.placeholder(R.drawable.none)
						.into(Ricon);
				}
			}else{
				Ricon.setVisibility(View.GONE);
			}
			if(LIcon.startsWith("icon")){
				if(new File (rootPath+""+iconsPath + LIcon).exists()){
					File In = new File(rootPath+""+iconsPath + LIcon);
					Glide.with(ListActivity.this)
						.load(In)
						.placeholder(R.drawable.none)
						.into(Licon);
				}
			}else{
				Licon.setVisibility(View.GONE);
			}
			if(animation.equals("left_in")){
				Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in);
				row.startAnimation(anim);
			}else if(animation.equals("right_in")){
				Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in);
				row.startAnimation(anim);
			}else if(animation.equals("bounce")){
				Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bouncing);
				row.startAnimation(anim);
			}else if(animation.equals("slide_down")){
				Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
				row.startAnimation(anim);
			}else if(animation.equals("fade_in")){
				Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
				row.startAnimation(anim);
			}
			return row;
		}}
	public void onResume() {
        super.onResume();
       // Refresh("");
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
    private void alertDialog(String title, String body){
        new MaterialAlertDialogBuilder(ListActivity.this)
			.setTitle(title)
			.setMessage(body)
			.setPositiveButton(getString(android.R.string.ok), null)
			.setCancelable(false)
			.show();
	}
}
