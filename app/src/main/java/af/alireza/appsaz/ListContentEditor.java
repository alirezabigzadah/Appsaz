package af.alireza.appsaz;

import af.alireza.appsaz.ActionPickerDialog.OnActionSelectedListener;
import af.alireza.appsaz.AnimationPickerDialog.OnAnimSelectedListener;
import af.alireza.appsaz.ColorPickerDialog.OnColorSelectedListener;
import af.alireza.appsaz.FontPickerDialog.OnFontSelectedListener;
import af.alireza.appsaz.IconPickerDialog.OnIconSelectedListener;
import af.alireza.appsaz.ImagePickerDialog.OnImageSelectedListener;
import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.BiggroupButton2;
import af.alireza.views.BiggroupSeekbar;
import af.alireza.views.MagicalTextView_Master;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.w3c.dom.Text;

public class ListContentEditor extends BaseActivity implements SearchView.OnQueryTextListener
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
	DatabaseHelper myDb; 
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
	MagicalTextView_Master ADD;
	ListView list;
	String listName;
	Picker picker;
	RelativeLayout BgLayout;

	String imagesPath="files/images/";
	String iconsPath="files/icons/";
	String fontsPath="files/fonts/";
	String Menumood;
	String Menuname;
	ActionBar actionBar;
	Toolbar toolbar;
	DrawerLayout drawer;
    Utilities utils = new Utilities();
	ContentsEditor editor = new ContentsEditor(this);
	ActionBarDrawerToggle toggle;
    Boolean isMenuActive;

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

		picker = new Picker();
		listName = getIntent().getExtras().getString("listName");
		myDb = new DatabaseHelper(this,listName); 
		myDb.open();
		Menumood = myDb.getMenuMood();
		Menuname = myDb.getMenuName();

		if(Menumood.startsWith("On")){
            isMenuActive = true;
			setContentView(R.layout.list_content_editor1);
			drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			MagicalTextView_Master MENUADD = (MagicalTextView_Master) findViewById(R.id.menuAdd);
			toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.app_name, R.string.app_name);
			drawer.addDrawerListener(toggle);
			toggle.syncState();
			RelativeLayout menuBg = (RelativeLayout) findViewById(R.id.drawer_bg);
			LinearLayout drawerContent = (LinearLayout) findViewById(R.id.drawer_content);
			editor.setContentsEditor(rootAssetsPath +"menu/"+ Menuname,drawerContent,MENUADD);
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

            String menuData = utils.readFile(rootAssetsPath +"menu/"+ Menuname);
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
						if(new File(rootAssetsPath + imagesPath + bg).exists()){
							try {
								FileInputStream In = new FileInputStream(rootAssetsPath + imagesPath + bg);
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
			setContentView(R.layout.list_content_editor);
			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			actionBar = getSupportActionBar();
		}
		BgLayout = (RelativeLayout) findViewById(R.id.listeditorBg);
		ADD = (MagicalTextView_Master) findViewById(R.id.add); 
		list = (ListView) findViewById(R.id.listView);		
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

					final AppCompatDialog dialog = new AppCompatDialog(ListContentEditor.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView dialog_Title = (TextView) dialog.findViewById(R.id.dialogTitle);

					dialog_Title.setText(getString(R.string.a_item)+getString(R.string.a_split)+TEXT);
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);

					final TextView textTitle = new TextView(ListContentEditor.this);
					textTitle.setText(getString(R.string.editor_text));
					final BiggroupButton2 text = new BiggroupButton2(ListContentEditor.this);
					final TextView descTitle = new TextView(ListContentEditor.this);
					descTitle.setText(getString(R.string.editor_desc));
					final BiggroupButton2 desc = new BiggroupButton2(ListContentEditor.this);
					final TextView fontTitle = new TextView(ListContentEditor.this);
					fontTitle.setText(getString(R.string.editor_font));
					final BiggroupButton2 font = new BiggroupButton2(ListContentEditor.this);
					final TextView rIconTitle = new TextView(ListContentEditor.this);
					rIconTitle.setText(getString(R.string.editor_right_icon));
					final BiggroupButton2 rIcon = new BiggroupButton2(ListContentEditor.this);
					final TextView lIconTitle = new TextView(ListContentEditor.this);
					lIconTitle.setText(getString(R.string.editor_left_icon));
					final BiggroupButton2 lIcon = new BiggroupButton2(ListContentEditor.this);
					final TextView backgroundTitle = new TextView(ListContentEditor.this);
					backgroundTitle.setText(getString(R.string.editor_background));
					final BiggroupButton2 background = new BiggroupButton2(ListContentEditor.this);
					final TextView radiusTitle = new TextView(ListContentEditor.this);
					radiusTitle.setText(getString(R.string.editor_radius));
					final BiggroupButton2 radius = new BiggroupButton2(ListContentEditor.this);
					final TextView textColorTitle = new TextView(ListContentEditor.this);
					textColorTitle.setText(getString(R.string.editor_text_color));
					final BiggroupButton2 textColor = new BiggroupButton2(ListContentEditor.this);
					final TextView textSizeTitle = new TextView(ListContentEditor.this);
					textSizeTitle.setText(getString(R.string.editor_text_size));
					final BiggroupButton2 textSize = new BiggroupButton2(ListContentEditor.this);
					final TextView animTitle = new TextView(ListContentEditor.this);
					animTitle.setText(getString(R.string.editor_animation));
					final BiggroupButton2 animation = new BiggroupButton2(ListContentEditor.this);
					final TextView onClickTitle = new TextView(ListContentEditor.this);
					onClickTitle.setText(getString(R.string.editor_action));
					final BiggroupButton2 onclick = new BiggroupButton2(ListContentEditor.this);
					final TextView otherTitle = new TextView(ListContentEditor.this);
					otherTitle.setText(getString(R.string.editor_other));
					final BiggroupButton2 delete = new BiggroupButton2(ListContentEditor.this);
					final BiggroupButton2 add = new BiggroupButton2(ListContentEditor.this);

					dialog_Content.addView(textTitle);
					dialog_Content.addView(text);
					dialog_Content.addView(descTitle);
					dialog_Content.addView(desc);
					dialog_Content.addView(fontTitle);
					dialog_Content.addView(font);
					dialog_Content.addView(rIconTitle);
					dialog_Content.addView(rIcon);
					dialog_Content.addView(lIconTitle);
					dialog_Content.addView(lIcon);
					dialog_Content.addView(backgroundTitle);
					dialog_Content.addView(background);
					dialog_Content.addView(radiusTitle);
					dialog_Content.addView(radius);
					dialog_Content.addView(textColorTitle);
					dialog_Content.addView(textColor);
					dialog_Content.addView(textSizeTitle);
					dialog_Content.addView(textSize);
					dialog_Content.addView(animTitle);
					dialog_Content.addView(animation);
					dialog_Content.addView(onClickTitle);
					dialog_Content.addView(onclick);
					dialog_Content.addView(otherTitle);
					dialog_Content.addView(delete);
					dialog_Content.addView(add);

					text.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								final AppCompatDialog dialog = new AppCompatDialog(ListContentEditor.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
								dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
								dialog.setContentView(R.layout.dialog);
								TextView dialog_Title = (TextView) dialog.findViewById(R.id.dialogTitle);
								dialog_Title.setText(getString(R.string.editor_text)+getString(R.string.a_split)
													 +text.getText().toString());
								LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
								LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
								TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
								TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
								dialog_Bottom.setVisibility(View.VISIBLE);
								final EditText textText = new EditText(ListContentEditor.this);
								textText.setHint(getString(R.string.a_title));
								textText.setText(text.getText().toString());
								dialog_Content.addView(textText);
								dialog_Cancel.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();

										}
									});
								dialog_Save.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											TEXT = textText.getText().toString();
											myDb.open();
											boolean isUpdate = myDb.updateData(
												ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
												,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
											myDb.close();
											Refresh("");
											dialog.dismiss();


										}
									});

								dialog.show();
							}
						});
					desc.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								final AppCompatDialog dialog = new AppCompatDialog(ListContentEditor.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
								dialog.setContentView(R.layout.dialog);
								TextView dialog_Title = (TextView) dialog.findViewById(R.id.dialogTitle);
								dialog_Title.setText(getString(R.string.editor_desc)+getString(R.string.a_split)
													 +desc.getText().toString());
								LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
								LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
								TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
								TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
								dialog_Bottom.setVisibility(View.VISIBLE);
								final EditText descText = new EditText(ListContentEditor.this);
								descText.setHint(getString(R.string.editor_desc));
								descText.setText(desc.getText().toString());
								dialog_Content.addView(descText);
								dialog_Cancel.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();

										}
									});
								dialog_Save.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											DESC = descText.getText().toString();
											myDb.open();
											boolean isUpdate = myDb.updateData(
												ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
												,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
											myDb.close();
											Refresh("");
											dialog.dismiss();


										}
									});

								dialog.show();
							}
						});
					font.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								String title = 
									getString(R.string.editor_font)+getString(R.string.a_split)
									+font.getText().toString();
								new FontPickerDialog(ListContentEditor.this,title, new OnFontSelectedListener() {
										public void onFontSelected(String fileName,String fileId) {
											FONT = fileId;
											if(fileId.equals("none")){
												FONT = "none";
												font.setText((getString(R.string.a_default)));
												font.setTypeface(null);
											}
											myDb.open();
											boolean isUpdate = myDb.updateData(
												ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
												,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
											myDb.close();
											Refresh("");
											dialog.dismiss();
										}
									}).show();

							}
						});

					rIcon.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								String title = 
									getString(R.string.editor_right_icon)+getString(R.string.a_split)
									+rIcon.getText().toString();
								new IconPickerDialog(ListContentEditor.this,title, new OnIconSelectedListener() {
										public void onIconSelected(String fileName,String fileId) {
											if(fileId.equals("none")){
												RICON = "none";
											}else{
												RICON = fileId;
											}
											myDb.open();
											boolean isUpdate = myDb.updateData(
												ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
												,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
											myDb.close();
											Refresh("");
											dialog.dismiss();
										}
									}).show();
							}
						});

					lIcon.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								String title = 
									getString(R.string.editor_left_icon)+getString(R.string.a_split)
									+lIcon.getText().toString();
								new IconPickerDialog(ListContentEditor.this,title, new OnIconSelectedListener() {
										public void onIconSelected(String fileName,String fileId) {
											if(fileId.equals("none")){
												LICON = "none";
											}else{
												LICON = fileId;
											}
											myDb.open();
											boolean isUpdate = myDb.updateData(
												ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
												,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
											myDb.close();
											Refresh("");
											dialog.dismiss();
										}
									}).show();
							}
						});
					background.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								final AppCompatDialog dialog = new AppCompatDialog(ListContentEditor.this);
                            	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            	dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
								dialog.setContentView(R.layout.dialog);
								TextView dialog_Title = (TextView) dialog.findViewById(R.id.dialogTitle);
								dialog_Title.setText(getString(R.string.editor_background)+getString(R.string.a_split)+background.getText().toString());
								LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
								try
								{
									final BiggroupButton2 noneBtn = new BiggroupButton2(ListContentEditor.this);
									dialog_Content.addView(noneBtn);
									noneBtn.setText(getString(R.string.a_default));
									final BiggroupButton2 colorBtn = new BiggroupButton2(ListContentEditor.this);
									dialog_Content.addView(colorBtn);
									colorBtn.setText(getString(R.string.a_color));
									final BiggroupButton2 ImgBtn = new BiggroupButton2(ListContentEditor.this);
									dialog_Content.addView(ImgBtn);
									ImgBtn.setText(getString(R.string.a_picture));
									final int oldColor;
									if(BACKGROUND.startsWith("Color")){
										oldColor = Color.parseColor(BACKGROUND.replace("Color",""));
									}else{
										oldColor = Color.WHITE;
									}
									noneBtn.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
												BACKGROUND = "none";
												myDb.open();
												boolean isUpdate = myDb.updateData(
													ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
													,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
												myDb.close();
												Refresh("");
												dialog.dismiss();

											}
										});
									colorBtn.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
												new ColorPickerDialog(ListContentEditor.this, oldColor, "Nothing", new OnColorSelectedListener() {
														public void onColorSelected(int color) {
															String c = String.format("#%08X", color);
															BACKGROUND = "Color"+c;
															background.setBackgroundColor(color);

															myDb.open();
															boolean isUpdate = myDb.updateData(
																ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
																,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
															myDb.close();
															Refresh("");
															dialog.dismiss();
														}
													}).show();

											}
										});
									ImgBtn.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
												String title = 
													getString(R.string.editor_background)+getString(R.string.a_split)+background.getText().toString();
												new ImagePickerDialog(ListContentEditor.this,title, new OnImageSelectedListener() {
														public void onImageSelected(String fileName,String fileId) {
															BACKGROUND = "Image"+fileId;
															myDb.open();
															boolean isUpdate = myDb.updateData(
																ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
																,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
															myDb.close();
															Refresh("");
															dialog.dismiss();
														}
													}).show();
											}
										});
								}
								catch (Exception e) { 
									e.printStackTrace(); 

								}
								dialog.show();
							}
						});
					radius.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								final AppCompatDialog dialog = new AppCompatDialog(ListContentEditor.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
								dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
								dialog.setContentView(R.layout.dialog);
								TextView dialog_Title = (TextView) dialog.findViewById(R.id.dialogTitle);
								dialog_Title.setText(getString(R.string.editor_radius)+getString(R.string.a_split)+radius.getText().toString());
								LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
								LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
								TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
								TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
								dialog_Bottom.setVisibility(View.VISIBLE);
								try
								{
									final BiggroupSeekbar radiusSeekbar = new BiggroupSeekbar(ListContentEditor.this);
									dialog_Content.addView(radiusSeekbar);
									radiusSeekbar.setMax(100);
									radiusSeekbar.setProgress(Integer.parseInt(radius.getText().toString().replace("%","")));
									dialog_Cancel.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();

											}
										});
									dialog_Save.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												RADIOS = String.valueOf(radiusSeekbar.getProgress());
												dialog.dismiss();
												myDb.open();
												boolean isUpdate = myDb.updateData(
													ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
													,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
												myDb.close();
												Refresh("");
												dialog.dismiss();
											}
										});

								}
								catch (Exception e) { 
									e.printStackTrace(); 

								}

								dialog.show();
							}
						});
					textColor.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								new ColorPickerDialog(ListContentEditor.this, Color.parseColor(TEXTCOLOR), "Nothing", new OnColorSelectedListener() {
										public void onColorSelected(int color) {
											String c = String.format("#%08X", color);
											TEXTCOLOR = c;
											myDb.open();
											boolean isUpdate = myDb.updateData(
												ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
												,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
											myDb.close();
											Refresh("");
											dialog.dismiss();
										}
									}).show();

							}
						});
					textSize.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								final AppCompatDialog dialog = new AppCompatDialog(ListContentEditor.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            	dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
								dialog.setContentView(R.layout.dialog);
								TextView dialog_Title = (TextView) dialog.findViewById(R.id.dialogTitle);
								dialog_Title.setText(getString(R.string.editor_text_size)+getString(R.string.a_split)+textSize.getText().toString());
								LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
								LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
								TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
								TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
								dialog_Bottom.setVisibility(View.VISIBLE);
								try
								{
									final BiggroupSeekbar sizeSeekbar = new BiggroupSeekbar(ListContentEditor.this);
									dialog_Content.addView(sizeSeekbar);
									sizeSeekbar.setMax(100);
									sizeSeekbar.setProgress(Integer.parseInt(textSize.getText().toString().replace("%","")));
									dialog_Cancel.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();

											}
										});
									dialog_Save.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												TEXTSIZE = String.valueOf(sizeSeekbar.getProgress());
												dialog.dismiss();
												myDb.open();
												boolean isUpdate = myDb.updateData(
													ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
													,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
												myDb.close();
												Refresh("");
												dialog.dismiss();
											}
										});

								}
								catch (Exception e) { 
									e.printStackTrace(); 

								}

								dialog.show();
							}
						});
					animation.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								String title = 
									"isList"
									+
									getString(R.string.editor_animation)+getString(R.string.a_split)+animation.getText().toString();
								new AnimationPickerDialog(ListContentEditor.this,title, new OnAnimSelectedListener() {
										public void onAnimSelected(String name,String anim) {
											ANIMATION = anim;
											myDb.open();
											boolean isUpdate = myDb.updateData(
												ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
												,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
											myDb.close();
											Refresh("");
											dialog.dismiss();
										}
									}).show();
							}
						});
					onclick.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								String title = getString(R.string.editor_action)+getString(R.string.a_split) + onclick.getText().toString();
								new ActionPickerDialog(ListContentEditor.this, title, ONCLICK, new OnActionSelectedListener() {
										public void onActionSelected(String action) {
											myDb.open();
											boolean isUpdate = myDb.updateData(
												ID,TEXT,DESC,FONT,RICON,LICON,BACKGROUND
												,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,action);
											myDb.close();
											Refresh("");
										}
									}).show();

							}
						});
					add.setOnClickListener(new View.OnClickListener() {
							@Override 
							public void onClick(View v) { 
								myDb.open();
								boolean isInserted=myDb.insertData(
									TEXT,DESC,FONT,RICON,LICON,BACKGROUND
									,RADIOS,TEXTCOLOR,TEXTSIZE,ANIMATION,ONCLICK);
								myDb.close();
								Refresh("");
								dialog.dismiss();
							} 

						}); 

					delete.setOnClickListener( 
						new View.OnClickListener() { 
							@Override 
							public void onClick(View v) { 
								myDb.open();
								Integer deletedRows = myDb.deleteData(ID); 
								myDb.close();
								Refresh("");
								dialog.dismiss();
							} 
						} 
					); 

					try
					{
						//فونت
						if(FONT.startsWith("font")){
							String str = picker.getFontNameById(FONT);
							if(str.startsWith("none")){
								FONT = "none";
								font.setText(getString(R.string.a_default));
								font.setTypeface(null);
							}else{
								Typeface tf = Typeface.createFromFile(rootAssetsPath+fontsPath+FONT);
								font.setTypeface(tf);
								font.setText(str);
							}
						}else{
							FONT = "none";
							font.setText(getString(R.string.a_default));
							font.setTypeface(null);
						}

						//آیکون راست
						if(RICON.startsWith("icon")){
							String str = picker.getIconNameById(RICON);
							if(str.startsWith("none")){
								RICON = "none";
								rIcon.setText((getString(R.string.editor_no_icon)));
							}else{
								RICON = RICON;
								rIcon.setText(str);
							}
						}else{
							RICON = "none";
							rIcon.setText((getString(R.string.editor_no_icon)));
						}
						//آیکون چپ
						if(LICON.startsWith("icon")){
							String str = picker.getIconNameById(LICON);
							if(str.startsWith("none")){
								LICON = "none";
								lIcon.setText(getString(R.string.editor_no_icon));
							}else{
								LICON = LICON;
								lIcon.setText(str);
							}
						}else{
							LICON = "none";
							lIcon.setText(getString(R.string.editor_no_icon));
						}
						//پس زمینه
						if(BACKGROUND.startsWith("Color")){
							String str = BACKGROUND.replace("Color","");
							background.setText(getString(R.string.a_color));
							background.getLeftIcon().setBackgroundColor(Color.parseColor(str));
						}else if(BACKGROUND.startsWith("Image")){
							//تصویر پس زمینه آیتم
							String bgImg = BACKGROUND.replaceFirst("Image","");
							if(bgImg.startsWith("image")){
								String str = picker.getImageNameById(bgImg);
								if(str.startsWith("none")){
									BACKGROUND = "none";
									background.setText(getString(R.string.a_default));
								}else{
									BACKGROUND = BACKGROUND;
									background.setText(getString(R.string.a_picture)+getString(R.string.a_split)+str);
									File In = new File(rootAssetsPath+"files/images/"+bgImg);
									background.setRightIcon(In);
								}}
						}else{
							BACKGROUND = "none";
							background.setText(getString(R.string.a_default));
						}
						radius.setText(RADIOS+"%");
						textColor.setText(getString(R.string.a_color));
						if(TEXTCOLOR.startsWith("#")){
							textColor.getLeftIcon().setBackgroundColor(Color.parseColor(TEXTCOLOR));
						}else{
							textColor.getLeftIcon().setBackgroundColor(Color.WHITE);
						}
						textSize.setText(TEXTSIZE+"%");
						if(ANIMATION.equals("right_in")){
							animation.setText(
								getString(R.string.editor_anim_right_in)
							);
						}else if(ANIMATION.equals("left_in")){
							animation.setText(
								getString(R.string.editor_anim_left_in)
							);
						}else if(ANIMATION.equals("fade_in")){
							animation.setText(
								getString(R.string.editor_anim_fade_in)
							);
						}else if(ANIMATION.equals("slide_down")){
							animation.setText(
								getString(R.string.editor_anim_slide_down)
							);
						}else if(ANIMATION.equals("bounce")){
							animation.setText(
								getString(R.string.editor_anim_bounce)
							);
						}else{
							animation.setText(
								getString(R.string.editor_no_animation)
							);
						}
						if(ONCLICK.startsWith("GotoPage")){
							String str = ONCLICK.replaceFirst("GotoPage","");
							if(new File(rootAssetsPath+""+str).exists()){
								ONCLICK = ONCLICK;
								onclick.setText(
									getString(R.string.editor_action_open_page)+getString(R.string.a_split)
									+picker.getPageNameById(str));
							}else{
								ONCLICK = "none";
								onclick.setText(
									getString(R.string.a_none)
								);
							}
						}else if(ONCLICK.startsWith("GotoList")){
							String str = ONCLICK.replaceFirst("GotoList","");
							if(new File(rootAssetsPath+""+str).exists()){
								ONCLICK = ONCLICK;
								onclick.setText(
									getString(R.string.editor_action_open_list)+getString(R.string.a_split)
									+picker.getListNameById(str));
							}else{
								ONCLICK = "none";
								onclick.setText(
									getString(R.string.a_none)
								);
							}
						}else if(ONCLICK.startsWith("PlayAudio")){
							String str = ONCLICK.replaceFirst("PlayAudio","");
							if(new File(rootAssetsPath+"files/audios/"+str).exists()){
								ONCLICK = ONCLICK;
								onclick.setText(
									getString(R.string.editor_action_play_audio)+getString(R.string.a_split)
									+picker.getAudioNameById(str));
							}else{
								ONCLICK = "none";
								onclick.setText(
									getString(R.string.a_none)
								);
							}
						}else if(ONCLICK.startsWith("PlayVideo")){
							String str = ONCLICK.replaceFirst("PlayVideo","");
							if(new File(rootAssetsPath+"files/videos/"+str).exists()){
								ONCLICK = ONCLICK;
								onclick.setText(
									getString(R.string.editor_action_play_video)+getString(R.string.a_split)
									+picker.getVideoNameById(str));
							}else{
								ONCLICK = "none";
								onclick.setText(
									getString(R.string.a_none)
								);
							}
						}else if(ONCLICK.startsWith("ShowImage")){
							String str = ONCLICK.replaceFirst("ShowImage","");
							if(new File(rootAssetsPath+"files/images/"+str).exists()){
								ONCLICK = ONCLICK;
								onclick.setText(
									getString(R.string.editor_action_show_pic)+getString(R.string.a_split)
									+picker.getImageNameById(str));
							}else{
								ONCLICK = "none";
								onclick.setText(
									getString(R.string.a_none)
								);
							}
						}else if(ONCLICK.startsWith("ShowToast")){
							String str = ONCLICK.replaceFirst("ShowToast","");
							ONCLICK = "ShowToast"+str;
							onclick.setText(
								getString(R.string.editor_action_show_toast)+getString(R.string.a_split)
								+str);
						}else if(ONCLICK.startsWith("ShowAlert")){
							String str = ONCLICK.replaceFirst("ShowAlert","");
							ONCLICK = "ShowAlert"+str;
							String[] message = str.split("<!>");
							onclick.setText(
								getString(R.string.editor_action_show_alert)+getString(R.string.a_split)+"\n" + 
                                getString(R.string.editor_action_alert_title)+":"+message[0].replaceFirst("Title","")+"\n"+ 
                                getString(R.string.editor_action_alert_text)+":"+"\n" +message[1].replaceFirst("Text",""));
						}else if(ONCLICK.startsWith("SendEmail")){
							String str = ONCLICK.replaceFirst("SendEmail","");
							onclick.setText(
								getString(R.string.editor_action_send_mail)+getString(R.string.a_split)+str);
						}else if(ONCLICK.startsWith("SendSms")){
							String str = ONCLICK.replaceFirst("SendSms","");
							onclick.setText(
								getString(R.string.editor_action_send_sms)+getString(R.string.a_split)+str);
						}else if(ONCLICK.startsWith("OpenLink")){
							String str = ONCLICK.replaceFirst("OpenLink","");
							onclick.setText(
								getString(R.string.editor_action_open_link)+getString(R.string.a_split)+str);
						}else if(ONCLICK.startsWith("Exit")){
							onclick.setText(
								getString(R.string.editor_action_exit)+getString(R.string.a_split));
						}else{
							ONCLICK = "none";
							onclick.setText(getString(R.string.editor_no_action));
						}
						text.setText(TEXT);
						desc.setText(DESC);
						delete.setText(getString(R.string.editor_delete));
						add.setText(getString(R.string.editor_copy));
					}
					catch (Exception e)
					{}

					dialog.show();

				}
			});

		ADD.setOnClickListener(new View.OnClickListener() { 
				@Override 
				public void onClick(View v) { 
					final AppCompatDialog dialog = new AppCompatDialog(ListContentEditor.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                	dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView dialog_Title = (TextView) dialog.findViewById(R.id.dialogTitle);
					dialog_Title.setText(getString(R.string.a_add_new_item));
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
					LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
					TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
					TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
					dialog_Bottom.setVisibility(View.VISIBLE);
					final EditText textText = new EditText(ListContentEditor.this);
					textText.setHint(getString(R.string.editor_text));
					textText.setSingleLine(true);
					final EditText textDesc = new EditText(ListContentEditor.this);
					textDesc.setHint(getString(R.string.editor_desc));
					dialog_Content.addView(textText);
					dialog_Content.addView(textDesc);
					dialog_Cancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();

							}
						});
					dialog_Save.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								myDb.open();
								boolean isInserted=myDb.insertData(
									textText.getText().toString(),
									textDesc.getText().toString(),
									"none",
									"none",
									"none",
									"Default",
									"0",
									"#000000",
									"10",
									"none",
									"none");

								myDb.close();
								Refresh("");
								dialog.dismiss();
							}
						});

					dialog.show();
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
		try
		{
			myDb.open();
			String bg =  myDb.getBG();
			if (bg.startsWith("Color")) {
				bg=bg.replace("Color","");
				BgLayout.setBackgroundColor(Color.parseColor(bg));
			} else if(bg.startsWith("Image")){
				bg=bg.replace("Image","");
				FileInputStream In = new FileInputStream(rootAssetsPath+"" + imagesPath + bg);
				InputStream inputStream = In;
				BgLayout.setBackgroundDrawable(Drawable.createFromStream(inputStream,""));
			}else{
				BgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
			}
			String actBar =  myDb.getActionBar();
			if (actBar.equals("Show")) {
				String acbTitle = myDb.getActionBarTitle();
				String acbBg = myDb.getActionBarColor();
				actionBar.setTitle(acbTitle);
				if(acbBg.startsWith("#")){
					actionBar.setBackgroundDrawable(new ColorDrawable((Color.parseColor(acbBg))));
					int color = darkenColor(Color.parseColor(acbBg));
					getWindow().setStatusBarColor(color);

					if(isColorDark(Color.parseColor(acbBg))){
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
			myDb.close();
		}
		catch (FileNotFoundException e)
		{}
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
		list.setAdapter(new myAdapter(ListContentEditor.this, R.layout.list_row, R.id.row_title, Text));

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
				if(new File(rootAssetsPath+""+fontsPath+font).exists()){
					Typeface tf = Typeface.createFromFile(rootAssetsPath+""+fontsPath+font);
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
				if(new File (rootAssetsPath+""+imagesPath + bg).exists()){
					try
					{
						FileInputStream fileIn = new FileInputStream(rootAssetsPath+""+imagesPath + bg);
						InputStream BgImg = fileIn;
						background.setBackgroundDrawable(Drawable.createFromStream(BgImg, ""));
					}
					catch (IOException e)
					{}
				}
			}
			if(RIcon.startsWith("icon")){
				if(new File (rootAssetsPath+""+iconsPath + RIcon).exists()){
					File In = new File(rootAssetsPath+""+iconsPath + RIcon);
					Glide.with(ListContentEditor.this)
						.load(In)
						.placeholder(R.drawable.none)
						.into(Ricon);
				}
			}else{
				Ricon.setVisibility(View.GONE);
			}
			if(LIcon.startsWith("icon")){
				if(new File (rootAssetsPath+""+iconsPath + LIcon).exists()){
					File In = new File(rootAssetsPath+""+iconsPath + LIcon);
					Glide.with(ListContentEditor.this)
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
        Refresh("");
    }

}
