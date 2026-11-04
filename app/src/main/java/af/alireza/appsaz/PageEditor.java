package af.alireza.appsaz;

import af.alireza.appsaz.ColorPickerDialog.OnColorSelectedListener;
import af.alireza.appsaz.ImagePickerDialog.OnImageSelectedListener;
import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.BiggroupButton2;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.android.material.materialswitch.MaterialSwitch;
import java.io.File;

public class PageEditor extends BaseActivity {
	Picker picker = new Picker();
	Utilities utils = new Utilities();

	private RelativeLayout bg;
	private TextView bgText;
	private ImageView bgImage;
	private String BACKGROUND;
	private String ACTIONBAR_TITLE;
	private String ACTIONBAR_MODE;
	private String ACTIONBAR_COLOR;
	private TextView tollbarTitle;
	private ImageView tollbarColorImage;
	private String pageName;

	private RelativeLayout tollbarColor;
	private MaterialSwitch tollbarSwitch;
	private MaterialSwitch menuSwitch;
	private RelativeLayout menuColor;
	private ImageView menuBgImage;
	private TextView menuBgText;

	private String menuName;
	private String menuMood;
	private String MENUBACKGROUND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.page_editor);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		final ActionBar actionBar;
		actionBar = getSupportActionBar(); 
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.page_edit);

		picker = new Picker();
		pageName = getIntent().getExtras().getString("pageName");
		tollbarTitle = (TextView) findViewById(R.id.pageeditorTollbarTitle);
		tollbarColorImage = (ImageView) findViewById(R.id.pageeditorTollbarColorImage);
		tollbarSwitch = (MaterialSwitch) findViewById(R.id.pageeditorTollbarSwitch);
		tollbarColor = (RelativeLayout) findViewById(R.id.pageeditorTollbarColor);
		bgText = (TextView) findViewById(R.id.pageeditorPageBgText);
		bgImage = (ImageView) findViewById(R.id.pageeditorPageBgImage);
		bg = (RelativeLayout) findViewById(R.id.pageeditorPageBg);

		menuSwitch = (MaterialSwitch) findViewById(R.id.pageeditorMenuSwitch);
		menuColor = (RelativeLayout) findViewById(R.id.pageeditorMenuBg);
		menuBgText = (TextView) findViewById(R.id.pageeditorMenuBgText);
		menuBgImage = (ImageView) findViewById(R.id.pageeditorMenuBgImage);

		Refresh();
		TextView edit = (TextView) findViewById(R.id.pageeditorContentEditorBtn);
		edit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(PageEditor.this, PageContentEditor.class);
					intent.putExtra("pageName", pageName);
					startActivity(intent);
				}
			});
		bg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final AppCompatDialog dialog = new AppCompatDialog(PageEditor.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView dtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
					dtitle.setText(getString(R.string.page_background) + getString(R.string.a_split) + bgText.getText().toString());
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
					try {
						final BiggroupButton2 noneBtn = new BiggroupButton2(PageEditor.this);
						dialog_Content.addView(noneBtn);
						noneBtn.setText(getString(R.string.a_default));
						final BiggroupButton2 colorBtn = new BiggroupButton2(PageEditor.this);
						dialog_Content.addView(colorBtn);
						colorBtn.setText(getString(R.string.a_color));
						final BiggroupButton2 ImgBtn = new BiggroupButton2(PageEditor.this);
						dialog_Content.addView(ImgBtn);
						ImgBtn.setText(getString(R.string.a_picture));
						final int oldColor;
						if (BACKGROUND.startsWith("Color")) {
							oldColor = Color.parseColor(BACKGROUND.replace("Color", ""));
						} else if (BACKGROUND.startsWith("Default")) {
							oldColor = Color.parseColor("#ffffff");
						} else {
							oldColor = Color.WHITE;
						}
						noneBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String Str = "Default";
									String str = utils.readFile(rootAssetsPath + pageName);
									str = str.replace("{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#", "{ThisPage<=>Background" + Str + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#");
									if (str.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(rootAssetsPath + pageName, str); 
									bgImage.setImageResource(R.drawable.none);
									bgImage.setBackgroundColor(Color.parseColor("#ffffff"));
									bgText.setText(getString(R.string.a_default));
									Refresh();
									dialog.dismiss();

								}
							});
						colorBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									new ColorPickerDialog(PageEditor.this, oldColor, "Nothing", new OnColorSelectedListener() {
											public void onColorSelected(int color) {
												String c = String.format("#%08X", color);
												c = "Color" + c;
												bgText.setText(getString(R.string.a_color));
												String str = utils.readFile(rootAssetsPath + pageName);
												str = str.replace("{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#", "{ThisPage<=>Background" + c + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#");
												if (str.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(rootAssetsPath + pageName, str); 
												bgImage.setImageResource(R.drawable.none);
												Refresh();

											}
										}).show();

								}
							});
						ImgBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title =getString(R.string.page_background) + getString(R.string.a_split) + ImgBtn.getText().toString();
									new ImagePickerDialog(PageEditor.this, title, new OnImageSelectedListener() {
											public void onImageSelected(String fileName, String fileId) {
												String Str = "Image" + fileId;
												bgText.setText(getString(R.string.a_picture) + getString(R.string.a_split) + fileName);
												String str = utils.readFile(rootAssetsPath + pageName);
												str = str.replace("{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#", "{ThisPage<=>Background" + Str + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#");
												if (str.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(rootAssetsPath + pageName, str); 
												Refresh();
												bgImage.setBackgroundColor(Color.TRANSPARENT);

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
		tollbarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						// در حالت روشن است
						String Str = "Show";
						String str = utils.readFile(rootAssetsPath + pageName);
						str = str.replace("{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#", "{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + Str + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#");
						if (str.startsWith("null")) {
							str = str.replace("null", "");
						}
						utils.writeFile(rootAssetsPath + pageName, str); 
					} else {
						// در حالت خاموش است
						String Str = "Hide";
						String str = utils.readFile(rootAssetsPath + pageName);
						str = str.replace("{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#", "{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + Str + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#");
						if (str.startsWith("null")) {
							str = str.replace("null", "");
						}
						utils.writeFile(rootAssetsPath + pageName, str); 
					}
					Refresh();
				}
			});
		tollbarTitle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final AppCompatDialog dialog = new AppCompatDialog(PageEditor.this);

					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView dtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
					dtitle.setText(getString(R.string.page_title_toolbar) + getString(R.string.a_split) + tollbarTitle.getText().toString());
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
					LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
					TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
					TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
					dialog_Bottom.setVisibility(View.VISIBLE);
					final EditText textText = new EditText(PageEditor.this);
					textText.setHint(getString(R.string.a_text));
					textText.setText(ACTIONBAR_TITLE);
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

								String Str = textText.getText().toString();
								String str = utils.readFile(rootAssetsPath + pageName);
								str = str.replace("{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#", "{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + Str + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#");
								if (str.startsWith("null")) {
									str = str.replace("null", "");
								}
								utils.writeFile(rootAssetsPath + pageName, str); 
								Refresh();
								dialog.dismiss();
							}
						});

					dialog.show();
				}
			});
		tollbarColor.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int oldColor = Color.DKGRAY;
					if (ACTIONBAR_COLOR.startsWith("#")) {
						oldColor = Color.parseColor(ACTIONBAR_COLOR);
					}
					new ColorPickerDialog(PageEditor.this, oldColor, "HideAlpha", new OnColorSelectedListener() {
							public void onColorSelected(int color) {
								String c = String.format("#%06X", 0xFFFFFF & color);
								bgText.setText(getString(R.string.a_color));
								String str = utils.readFile(rootAssetsPath + pageName);
								str = str.replace("{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + ACTIONBAR_COLOR + "}!#", "{ThisPage<=>Background" + BACKGROUND + "<=>ActionBar" + ACTIONBAR_MODE + "<=>ActionBarTitle" + ACTIONBAR_TITLE + "<=>ActionBarColor" + c + "}!#");
								if (str.startsWith("null")) {
									str = str.replace("null", "");
								}
								utils.writeFile(rootAssetsPath + pageName, str); 
								Refresh();

							}
						}).show();

				}
			});
		menuSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						// در حالت روشن است
						String Str = "On";
						String str = utils.readFile(rootAssetsPath + pageName);
						str = str.replace("{Menu<=>Mood" + menuMood + "<=>Name" + menuName + "}!#", "{Menu<=>Mood" + Str + "<=>Name" + menuName + "}!#");
						if (str.startsWith("null")) {
							str = str.replace("null", "");
						}
						utils.writeFile(rootAssetsPath + pageName, str); 
					} else {
						// در حالت خاموش است
						String Str = "Off";
						String str = utils.readFile(rootAssetsPath + pageName);
						str = str.replace("{Menu<=>Mood" + menuMood + "<=>Name" + menuName + "}!#", "{Menu<=>Mood" + Str + "<=>Name" + menuName + "}!#");
						if (str.startsWith("null")) {
							str = str.replace("null", "");
						}
						utils.writeFile(rootAssetsPath + pageName, str);
					}
					Refresh();
				}
			});
		menuColor.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					final AppCompatDialog dialog = new AppCompatDialog(PageEditor.this);

					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView dtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
					dtitle.setText(getString(R.string.page_menu_background) + getString(R.string.a_split) + menuBgText.getText().toString());
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);

					try {
						final BiggroupButton2 noneBtn = new BiggroupButton2(PageEditor.this);
						dialog_Content.addView(noneBtn);
						noneBtn.setText(getString(R.string.a_default));
						final BiggroupButton2 colorBtn = new BiggroupButton2(PageEditor.this);
						dialog_Content.addView(colorBtn);
						colorBtn.setText(getString(R.string.a_color));
						final BiggroupButton2 ImgBtn = new BiggroupButton2(PageEditor.this);
						dialog_Content.addView(ImgBtn);
						ImgBtn.setText(getString(R.string.a_picture));
						final int oldColor;
						if (MENUBACKGROUND.startsWith("Color")) {
							oldColor = Color.parseColor(MENUBACKGROUND.replace("Color", ""));
						} else if (MENUBACKGROUND.startsWith("Default")) {
							oldColor = Color.parseColor("#ffffff");
						} else {
							oldColor = Color.WHITE;
						}
						noneBtn.setOnClickListener(new View.OnClickListener() {
								private String Info;
								@Override
								public void onClick(View v) {
									String data = utils.readFile(rootAssetsPath + "menu/" + menuName);
									final String Content = data;
									Info = Content;	
									final String[] contentarray = Content.split("\\!\\#");
									for (String part : contentarray) {
										String part2=part;
										if (part2.contains("{ThisMenu<=>")) {
											String[] code = part2.replace("}", "").split("<=>");
											String bg = code[1].replaceFirst("Background", "");
											String str = Info.replace("{ThisMenu<=>Background" + bg + "}!#", "{ThisMenu<=>Background" + "Default" + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(rootAssetsPath + "menu/" + menuName, str); 
										}}
									Refresh();
									dialog.dismiss();

								}
							});
						colorBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									new ColorPickerDialog(PageEditor.this, oldColor, "Nothing", new OnColorSelectedListener() {
											private String Info;
											public void onColorSelected(int color) {
												String c = String.format("#%08X", color);
												c = "Color" + c;
												String data = utils.readFile(rootAssetsPath + "menu/" + menuName);
												final String Content = data;
												Info = Content;	
												final String[] contentarray = Content.split("\\!\\#");
												for (String part : contentarray) {
													String part2=part;
													if (part2.contains("{ThisMenu<=>")) {
														String[] code = part2.replace("}", "").split("<=>");
														String bg = code[1].replaceFirst("Background", "");
														String str = Info.replace("{ThisMenu<=>Background" + bg + "}!#", "{ThisMenu<=>Background" + c + "}!#");
														if (Info.startsWith("null")) {
															str = str.replace("null", "");
														}
														utils.writeFile(rootAssetsPath + "menu/" + menuName, str);
													}}
												Refresh();

											}
										}).show();

								}
							});
						ImgBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = getString(R.string.page_menu_background) + getString(R.string.a_split) + ImgBtn.getText().toString();
									new ImagePickerDialog(PageEditor.this, title, new OnImageSelectedListener() {

											private String Info;
											public void onImageSelected(String fileName, String fileId) {
												String Str = "Image" + fileId;
												String data = utils.readFile(rootAssetsPath + "menu/" + menuName);
												final String Content = data;
												Info = Content;	
												final String[] contentarray = Content.split("\\!\\#");
												for (String part : contentarray) {
													String part2=part;
													if (part2.contains("{ThisMenu<=>")) {
														String[] code = part2.replace("}", "").split("<=>");
														String bg = code[1].replaceFirst("Background", "");
														String str = Info.replace("{ThisMenu<=>Background" + bg + "}!#", "{ThisMenu<=>Background" + Str + "}!#");
														if (Info.startsWith("null")) {
															str = str.replace("null", "");
														}
														utils.writeFile(rootAssetsPath + "menu/" + menuName, str);
													}}
												Refresh();

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
	private void Refresh() {
		String data = utils.readFile(rootAssetsPath + pageName);
		String splash = data;
	//	splash = splash.trim();
		String[] durarray = splash.split("\\!\\#");
		for (String part : durarray) {
			String part2=part;
			if (part2.contains("{ThisPage<=>")) {
				String[] code = part2.replace("}", "").split("<=>");
				String bg = code[1].replaceFirst("Background", "");

				String actionbar = code[2].replace("ActionBar", "");
				ACTIONBAR_MODE = actionbar;
				String acbTitle = code[3].replaceFirst("ActionBarTitle", "");
				String acbBg = code[4].replace("ActionBarColor", "");
				ACTIONBAR_TITLE = acbTitle;
				ACTIONBAR_COLOR = acbBg;
				tollbarTitle.setText(acbTitle);
				if (acbBg.startsWith("#")) {
					tollbarColorImage.setBackgroundColor(Color.parseColor(acbBg));						
				} else {
					tollbarColorImage.setBackgroundColor(Color.DKGRAY);
				}
				if (actionbar.replace("ActionBar", "").startsWith("Show")) {
					tollbarTitle.setEnabled(true);
					tollbarColor.setEnabled(true);
					tollbarColor.setAlpha(1);
					tollbarTitle.setAlpha(1);
					tollbarSwitch.setChecked(true);
				} else {
					tollbarTitle.setEnabled(false);
					tollbarColor.setEnabled(false);
					tollbarColor.setAlpha(0.5f);
					tollbarTitle.setAlpha(0.5f);
					tollbarSwitch.setChecked(false);
				}
				BACKGROUND = bg;
				if (bg.startsWith("Color")) {
					bg = bg.replace("Color", "");
					bgImage.setBackgroundColor(Color.parseColor(bg));
				} else if (bg.startsWith("Image")) {
					bg = bg.replace("Image", "");
					if (picker.getImageNameById(bg).startsWith("none")) {
						bgImage.setBackgroundColor(Color.parseColor("#ffffff"));
						bgText.setText(getString(R.string.a_default));
					} else {
						File In = new File(rootAssetsPath + imagesPath + bg);
						Glide.with(this)
							.load(In)
							.into(bgImage);
						bgText.setText(getString(R.string.a_picture) + getString(R.string.a_split) + picker.getImageNameById(bg));
					}

				} else {
					bgImage.setBackgroundColor(Color.parseColor("#ffffff"));
					bgText.setText(getString(R.string.a_default));
				}

			}
			if (part2.contains("{Menu<=>")) {
				String[] code = part2.replace("}", "").split("<=>");
				menuMood = code[1].replaceFirst("Mood", "");
				menuName = code[2].replaceFirst("Name", "");
				if (menuMood.equals("On")) {
					menuColor.setEnabled(true);
					menuColor.setAlpha(1);
					menuSwitch.setChecked(true);
				} else {
					//actionBar.hide();
					menuColor.setEnabled(false);
					menuColor.setAlpha(0.5f);
					menuSwitch.setChecked(false);
				}
			}
			try {
				final String Content = utils.readFile(rootAssetsPath + "menu/" + menuName);
				final String[] contentarray = Content.split("\\!\\#");
				for (String part1 : contentarray) {
					String part21=part1;
					if (part21.contains("{ThisMenu<=>")) {
						String[] code = part21.replace("}", "").split("<=>");
						String Bg = code[1].replaceFirst("Background", "");
						MENUBACKGROUND = Bg;
						if (Bg.startsWith("Color")) {
							Bg = Bg.replace("Color", "");
							menuBgImage.setImageResource(R.drawable.none);
							menuBgImage.setBackgroundColor(Color.parseColor(Bg));
							menuBgText.setText(getString(R.string.a_color));
						} else if (Bg.startsWith("Image")) {
							Bg = Bg.replace("Image", "");
							String bg=Bg.replace("Image", "");
							if (picker.getImageNameById(bg).startsWith("none")) {
								menuBgImage.setImageResource(R.drawable.none);
								menuBgImage.setBackgroundColor(Color.parseColor("#ffffff"));
								menuBgText.setText(getString(R.string.a_default));
							} else {
								File In = new File(rootAssetsPath + imagesPath + Bg);
								Glide.with(this)
									.load(In)
									.placeholder(R.drawable.none)
									.into(menuBgImage);
								menuBgText.setText(getString(R.string.a_picture) + getString(R.string.a_split) + picker.getImageNameById(bg));
								menuBgImage.setBackgroundColor(Color.TRANSPARENT);
							}
						} else {
							menuBgImage.setImageResource(R.drawable.none);
							menuBgImage.setBackgroundColor(Color.parseColor("#ffffff"));
							menuBgText.setText(getString(R.string.a_default));
						}
					}}} catch (Exception e) { 
				e.printStackTrace(); 
			}
		}

	}
	public void onResume() {
        super.onResume();
        Refresh();
    }
}
