package af.alireza.appsaz;

import af.alireza.appsaz.ColorPickerDialog.OnColorSelectedListener;
import af.alireza.appsaz.ImagePickerDialog.OnImageSelectedListener;
import af.alireza.appsaz.ListPickerDialog.OnListSelectedListener;
import af.alireza.appsaz.PagePickerDialog.OnPageSelectedListener;
import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.BiggroupButton2;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import java.io.File;

public class SplashEditor extends BaseActivity {

	Picker picker = new Picker();
	Utilities utils = new Utilities();
	private TextView splasheDuration;
	private TextView splashNext;
	private int splashDur ;
	private String splashGoto;
	private String splashData;
	private String splashBackground;
	private String Info;
	private RelativeLayout splashBg;
	private TextView splashPage;
	private TextView splashBgText;
	private ImageView splashBgImage;
	private String splash;

	String data = "splash_content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_editor);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		final ActionBar actionBar;
		actionBar = getSupportActionBar(); 
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.splash_title);
		picker = new Picker();

		splasheDuration = (TextView) findViewById(R.id.splasheditorTimeText);
		splashNext = (TextView) findViewById(R.id.splasheditorNextText);
		splashBg = (RelativeLayout) findViewById(R.id.splasheditorPageBg);
		splashBgText = (TextView) findViewById(R.id.splasheditorPageBgText);
		splashBgImage = (ImageView) findViewById(R.id.splasheditorPageBgImage);
		refresh();
		splashPage = (TextView) findViewById(R.id.splasheditorSplashPage);
		splashPage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
					startActivity(new Intent(SplashEditor.this , SplashContentEditor.class));
				}
            });
		splasheDuration.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					final AppCompatDialog dialog = new AppCompatDialog(SplashEditor.this);

					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView dtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
					dtitle.setText(getString(R.string.splash_time) + getString(R.string.a_split) + splasheDuration.getText().toString());
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
					LinearLayout dialog_Botton = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
					dialog_Botton.setVisibility(View.VISIBLE);			
					TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
					TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);

					ContextThemeWrapper wrappedContext = new ContextThemeWrapper(
							SplashEditor.this,
							androidx.appcompat.R.style.Widget_AppCompat_SeekBar_Discrete
					);					final AppCompatSeekBar dialog_Seekbar = new AppCompatSeekBar(wrappedContext);
					final TextView dialog_Dur = new TextView(SplashEditor.this);
					dialog_Content.addView(dialog_Dur);
					dialog_Content.addView(dialog_Seekbar);
					dialog_Dur.setText(splashDur + getString(R.string.a_sec));
					dialog_Dur.setGravity(Gravity.CENTER);
					dialog_Dur.setTextSize(20);
					dialog_Seekbar.setProgress(splashDur);
					dialog_Seekbar.setMax(10);
					dialog_Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
								dialog_Dur.setText(progress + getString(R.string.a_sec));
							}

							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {

							}

							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
							}

						});

					dialog_Cancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								dialog.dismiss();
							}
						});
					dialog_Save.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								String str = "{SplashStetting<=>SplashDuration" + dialog_Seekbar.getProgress() + "<=>GoToWhere" + splashGoto + "<=>SendData" + splashData + "}!#";
								utils.writeFile(rootAssetsPath + "splash_settings", str);
								refresh();
								dialog.dismiss();
							}

						});

					dialog.show();

				}});
		splashNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					final AppCompatDialog dialog = new AppCompatDialog(SplashEditor.this);

					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView dtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
					dtitle.setText(getString(R.string.splash_after_splash) + getString(R.string.a_split) + splashNext.getText().toString());
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
					final BiggroupButton2 dialog_page = new BiggroupButton2(SplashEditor.this);
        			final BiggroupButton2 dialog_list = new BiggroupButton2(SplashEditor.this);
					dialog_Content.addView(dialog_page);
					dialog_Content.addView(dialog_list);
					dialog_page.setText(getString(R.string.a_open_page));
					dialog_list.setText(getString(R.string.a_open_list));

					dialog_page.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								String title = 
									getString(R.string.splash_after_splash) + getString(R.string.a_split) + dialog_page.getText().toString();
								new PagePickerDialog(SplashEditor.this, title, new OnPageSelectedListener() {
										public void onPageSelected(String fileName, String fileId) {
											String splashGoto = "Page";
											String splashData = fileId;
											String str = "{SplashStetting<=>SplashDuration" + splashDur + "<=>GoToWhere" + splashGoto + "<=>SendData" + splashData + "}!#";
											utils.writeFile(rootAssetsPath + "splash_settings", str);
											refresh();
										}
									}).show();
							}
						});
					dialog_list.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								String title = 
									getString(R.string.splash_after_splash) + getString(R.string.a_split) + dialog_list.getText().toString();
								new ListPickerDialog(SplashEditor.this, title, new OnListSelectedListener() {
										public void onListSelected(String fileName, String fileId) {
											splashGoto = "List";
											splashData = fileId;
											String str = "{SplashStetting<=>SplashDuration" + splashDur + "<=>GoToWhere" + splashGoto + "<=>SendData" + splashData + "}!#";
											utils.writeFile(rootAssetsPath + "splash_settings", str);
											refresh();
										}
									}).show();
							}
						});
					dialog.show();

				}});
		splashBg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final AppCompatDialog dialog = new AppCompatDialog(SplashEditor.this);

					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView dtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
					dtitle.setText(getString(R.string.splash_background) + getString(R.string.a_split) + splashBgText.getText().toString());
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);

					try {
						final BiggroupButton2 noneBtn = new BiggroupButton2(SplashEditor.this);
						dialog_Content.addView(noneBtn);
						noneBtn.setText(getString(R.string.a_default));
						final BiggroupButton2 colorBtn = new BiggroupButton2(SplashEditor.this);
						dialog_Content.addView(colorBtn);
						colorBtn.setText(getString(R.string.a_color));
						final BiggroupButton2 ImgBtn = new BiggroupButton2(SplashEditor.this);
						dialog_Content.addView(ImgBtn);
						ImgBtn.setText(getString(R.string.a_picture));
						final int oldColor;
						if (splashBackground.startsWith("Color")) {
							oldColor = Color.parseColor(splashBackground.replace("Color", ""));
						} else if (splashBackground.startsWith("Default")) {
							oldColor = Color.parseColor("#ffffff");
						} else {
							oldColor = Color.WHITE;
						}
						noneBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String Str = "Default";
									String end = "}";
									if (splash.endsWith("}}")) {
										end = "}}";
									} else {
										end = "}";
									}
									String str = Info.replace("{Splash<=>Background" + splashBackground + end + "!#", "{Splash<=>Background" + Str + "}!#");
									if (Info.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(rootAssetsPath + "splash_content", str); 
									splashBgImage.setImageResource(R.drawable.none);
									splashBgImage.setBackgroundColor(Color.parseColor("#ffffff"));
									splashBgText.setText(getString(R.string.a_default));
									refresh();
									dialog.dismiss();

								}
							});
						colorBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									new ColorPickerDialog(SplashEditor.this, oldColor, "Nothing", new OnColorSelectedListener() {
											public void onColorSelected(int color) {
												String c = String.format("#%08X", color);
												c = "Color" + c;
												String end = "}";
												if (splash.endsWith("}}")) {
													end = "}}";
												} else {
													end = "}";
												}
												String str = Info.replace("{Splash<=>Background" + splashBackground + end + "!#", "{Splash<=>Background" + c + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(rootAssetsPath + "splash_content", str); 
												splashBgImage.setImageResource(R.drawable.none);
												refresh();

											}
										}).show();

								}
							});
						ImgBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = getString(R.string.splash_background) + getString(R.string.a_split) + ImgBtn.getText().toString();
									new ImagePickerDialog(SplashEditor.this, title, new OnImageSelectedListener() {
											public void onImageSelected(String fileName, String fileId) {
												String Str = "Image" + fileId;
												splashBgText.setText(
													getString(R.string.a_picture) + getString(R.string.a_split) + fileName);
												String end = "}";
												if (splash.endsWith("}}")) {
													end = "}}";
												} else {
													end = "}";
												}
												String str = Info.replace("{Splash<=>Background" + splashBackground + end + "!#", "{Splash<=>Background" + Str + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(rootAssetsPath + "splash_content", str); 
												splashBgImage.setBackgroundColor(Color.TRANSPARENT);
												refresh();
											}
										}).show();
								}
							});
					} catch (Exception e) { 
						e.printStackTrace(); 

					}

					dialog.show();
				}
			});
	}

	private void refresh() {
		try {
			String bgsplash = utils.readFile(rootAssetsPath + "splash_content");
			Info = bgsplash;
		//	bgsplash = bgsplash.trim();
			String[] array = bgsplash.split("\\!\\#");
			for (String part : array) {
				String part2=part;
				if (part2.contains("{Splash<=>")) {
					splash = part2;
					String[] code = part2.replace("}", "").split("<=>");
					String bg = code[1].replace("Background", "");
					splashBackground = bg;
					if (bg.startsWith("Color")) {
						bg = bg.replace("Color", "");
						splashBgImage.setBackgroundColor(Color.parseColor(bg));
						splashBgText.setText(getString(R.string.a_color));
					} else if (bg.startsWith("Image")) {
						bg = bg.replace("Image", "");
						if (picker.getImageNameById(bg).startsWith("none")) {
							splashBgImage.setBackgroundColor(Color.parseColor("#ffffff"));
							splashBgText.setText(getString(R.string.a_none));
						} else {
							File In = new File(rootAssetsPath + "files/images/" + bg);
							Glide.with(this)
								.load(In)
								.into(splashBgImage);
							splashBgText.setText(getString(R.string.a_picture) + getString(R.string.a_split) + picker.getImageNameById(bg));
						}
					} else {
						splashBgImage.setBackgroundColor(Color.parseColor("#ffffff"));
						splashBgText.setText(getString(R.string.a_default));
					}}}
			final String data = utils.readFile(rootAssetsPath + "splash_settings");
			String splash = data;
		//	splash = splash.trim();
			String[] durarray = splash.split("\\!\\#");
			for (String part : durarray) {
				String part2=part;
				if (part2.contains("{SplashStetting<=>")) {
					String dur = part2.replace("}", "").split("<=>")[1].replace("SplashDuration", "");
					splashGoto = part2.replace("}", "").split("<=>")[2].replace("GoToWhere", "");
					splashData = part2.replace("}", "").split("<=>")[3].replace("SendData", "");
					if (splashData.replace("SendData", "").startsWith("list")) {
						if (new File(rootAssetsPath + "" + splashData).exists()) {
							splashData = splashData;
						} else {
							splashGoto = "none";
							splashData = "none";
						}
					}
					if (splashData.replace("SendData", "").startsWith("page")) {
						if (new File(rootAssetsPath + "" + splashData).exists()) {
							splashData = splashData;
						} else {
							splashGoto = "none";
							splashData = "none";
						}
					}
					splashDur = Integer . parseInt(dur);
					splasheDuration.setText(splashDur + getString(R.string.a_sec));
					if (splashGoto.equals("none")) {
						splashNext.setText(getString(R.string.a_none));
					} else {
						if (splashGoto.equals("Page")) {
							splashNext.setText(getString(R.string.a_open_page) + getString(R.string.a_split) + picker.getPageNameById(splashData));
						} else if (splashGoto.equals("List")) {
							splashNext.setText(getString(R.string.a_open_list) + getString(R.string.a_split) + picker.getListNameById(splashData));
						} else {
							splashNext.setText(getString(R.string.a_none));

						}

					}
				}}} catch (Exception e) {}
	}

	@Override
	protected void onResume() {
		// TODO: Implement this method
		super.onResume();
		refresh();
	}

}
