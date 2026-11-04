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
import java.io.FileInputStream;
import java.io.InputStream;

public class ListEditor extends BaseActivity { 
	DatabaseHelper myDb; 
	String listName;
	private String BACKGROUND;
	private String ACTIONBAR_TITLE;
	private String ACTIONBAR_MODE;
	private String ACTIONBAR_COLOR;
	private String MENUBACKGROUND;
	Picker picker;
	private TextView tollbarTitle;
	private ImageView tollbarColorImage;
	private MaterialSwitch tollbarSwitch;
	private RelativeLayout tollbarColor;
	private MaterialSwitch menuSwitch;
	private RelativeLayout menuColor;
	private TextView bgText;
	private ImageView bgImage;
	private RelativeLayout bg; 
	String audiosPath="files/audios/";
	String videosPath="files/videos/";
	String imagesPath="files/images/";
	String iconsPath="files/icons/";
	String fontsPath="files/fonts/";
	private ImageView menuBgImage;
	private TextView menuBgText;
	private Utilities utils = new Utilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.list_editor);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		final ActionBar actionBar;
		actionBar = getSupportActionBar(); 
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.list_edit);

		picker = new Picker();
		listName = getIntent().getExtras().getString("listName");
		myDb = new DatabaseHelper(this, listName); 

		tollbarTitle = (TextView) findViewById(R.id.pageeditorTollbarTitle);
		tollbarColorImage = (ImageView) findViewById(R.id.pageeditorTollbarColorImage);
		tollbarSwitch = (MaterialSwitch) findViewById(R.id.pageeditorTollbarSwitch);
		tollbarColor = (RelativeLayout) findViewById(R.id.pageeditorTollbarColor);
		menuSwitch = (MaterialSwitch) findViewById(R.id.pageeditorMenuSwitch);
		menuColor = (RelativeLayout) findViewById(R.id.pageeditorMenuBg);
		menuBgText = (TextView) findViewById(R.id.pageeditorMenuBgText);
		menuBgImage = (ImageView) findViewById(R.id.pageeditorMenuBgImage);
		bgText = (TextView) findViewById(R.id.pageeditorPageBgText);
		bgImage = (ImageView) findViewById(R.id.pageeditorPageBgImage);
		bg = (RelativeLayout) findViewById(R.id.pageeditorPageBg);
		Refresh();
		TextView edit = (TextView) findViewById(R.id.pageeditorContentEditorBtn);
		edit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ListEditor.this, ListContentEditor.class);
					intent.putExtra("listName", listName);
					startActivity(intent);
				}
			});
		bg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                    
					final AppCompatDialog dialog = new AppCompatDialog(ListEditor.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
					title.setText(getString(R.string.list_background) + getString(R.string.a_split)
								  + bgText.getText().toString());
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);

					try {
						final BiggroupButton2 noneBtn = new BiggroupButton2(ListEditor.this);
						dialog_Content.addView(noneBtn);
						noneBtn.setText(getString(R.string.a_default));
						final BiggroupButton2 colorBtn = new BiggroupButton2(ListEditor.this);
						dialog_Content.addView(colorBtn);
						colorBtn.setText(getString(R.string.a_color));
						final BiggroupButton2 ImgBtn = new BiggroupButton2(ListEditor.this);
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
									myDb.open();
									myDb.updateThisList(Str, ACTIONBAR_MODE, ACTIONBAR_TITLE, ACTIONBAR_COLOR);
									myDb.close();
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
									new ColorPickerDialog(ListEditor.this, oldColor, "Nothing", new OnColorSelectedListener() {
											public void onColorSelected(int color) {
												String Str = String.format("#%08X", color);
												Str = "Color" + Str;
												myDb.open();
												myDb.updateThisList(Str, ACTIONBAR_MODE, ACTIONBAR_TITLE, ACTIONBAR_COLOR);
												myDb.close();
												Refresh();

											}
										}).show();

								}
							});
						ImgBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title =
										getString(R.string.list_background) + getString(R.string.a_split)
										+ ImgBtn.getText().toString();
									new ImagePickerDialog(ListEditor.this, title, new OnImageSelectedListener() {
											public void onImageSelected(String fileName, String fileId) {
												String Str = "Image" + fileId;
												myDb.open();
												myDb.updateThisList(Str, ACTIONBAR_MODE, ACTIONBAR_TITLE, ACTIONBAR_COLOR);
												myDb.close();
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
		tollbarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						// در حالت روشن است
						String Str = "Show";
						myDb.open();
						myDb.updateThisList(BACKGROUND, Str, ACTIONBAR_TITLE, ACTIONBAR_COLOR);
						myDb.close();
					} else {
						// در حالت خاموش است
						String Str = "Hide";
						myDb.open();
						myDb.updateThisList(BACKGROUND, Str, ACTIONBAR_TITLE, ACTIONBAR_COLOR);
						myDb.close();
					}
					Refresh();
				}
			});

		tollbarTitle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final AppCompatDialog dialog = new AppCompatDialog(ListEditor.this);

					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
					title.setText(getString(R.string.list_title_toolbar) + getString(R.string.a_split) + tollbarTitle.getText().toString());
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
					LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
					TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
					TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
					dialog_Bottom.setVisibility(View.VISIBLE);
					final EditText textText = new EditText(ListEditor.this);
					textText.setHint(getString(R.string.a_title));
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
								myDb.open();
								myDb.updateThisList(BACKGROUND, ACTIONBAR_MODE, Str, ACTIONBAR_COLOR);
								myDb.close();
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
					new ColorPickerDialog(ListEditor.this, oldColor, "HideAlpha", new OnColorSelectedListener() {
							public void onColorSelected(int color) {
								String Str = String.format("#%06X", 0xFFFFFF & color);
								bgText.setText(getString(R.string.a_color));
								myDb.open();
								myDb.updateThisList(BACKGROUND, ACTIONBAR_MODE, ACTIONBAR_TITLE, Str);
								myDb.close();
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
						myDb.open();
						myDb.updateMenu(Str, myDb.getMenuName());
						myDb.close();
					} else {
						// در حالت خاموش است
						String Str = "Off";
						myDb.open();
						myDb.updateMenu(Str, myDb.getMenuName());
						myDb.close();
					}
					Refresh();
				}
			});
		menuColor.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					myDb.open();
					final AppCompatDialog dialog = new AppCompatDialog(ListEditor.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.dialog);
					TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
					title.setText(getString(R.string.list_background) + getString(R.string.a_split)
								  + bgText.getText().toString());
					LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);

					try {
						final BiggroupButton2 noneBtn = new BiggroupButton2(ListEditor.this);
						dialog_Content.addView(noneBtn);
						noneBtn.setText(getString(R.string.a_default));
						final BiggroupButton2 colorBtn = new BiggroupButton2(ListEditor.this);
						dialog_Content.addView(colorBtn);
						colorBtn.setText(getString(R.string.a_color));
						final BiggroupButton2 ImgBtn = new BiggroupButton2(ListEditor.this);
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
									String data = utils.readFile(rootAssetsPath + "menu/" + myDb.getMenuName());
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
											utils.writeFile(rootAssetsPath + "menu/" + myDb.getMenuName(), str);
										}}
									myDb.close();
									Refresh();
									dialog.dismiss();

								}
							});
						colorBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									new ColorPickerDialog(ListEditor.this, oldColor, "Nothing", new OnColorSelectedListener() {
											private String Info;
											public void onColorSelected(int color) {
												String c = String.format("#%08X", color);
												c = "Color" + c;
												String data = utils.readFile(rootAssetsPath + "menu/" + myDb.getMenuName());
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
														utils.writeFile(rootAssetsPath + "menu/" + myDb.getMenuName(), str); 
													}}
												myDb.close();
												Refresh();

											}
										}).show();

								}
							});
						ImgBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title =
										getString(R.string.list_background) + getString(R.string.a_split)
										+ ImgBtn.getText().toString();
									new ImagePickerDialog(ListEditor.this, title, new OnImageSelectedListener() {
											private String Info;
											public void onImageSelected(String fileName, String fileId) {
												String Str = "Image" + fileId;
												String data = utils.readFile(rootAssetsPath + "menu/" + myDb.getMenuName());	
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
														utils.writeFile(rootAssetsPath + "menu/" + myDb.getMenuName(), str); 
													}
												}
												myDb.close();
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
		myDb.open();
		String BG =  myDb.getBG();
		BACKGROUND = BG;
		if (BG.startsWith("Color")) {
			BG = BG.replace("Color", "");
			bgImage.setImageResource(R.drawable.none);
			bgImage.setBackgroundColor(Color.parseColor(BG));

		} else if (BG.startsWith("Image")) {
			BG = BG.replace("Image", "");
			String bg=BG.replace("Image", "");
			if (picker.getImageNameById(bg).startsWith("none")) {
				bgImage.setImageResource(R.drawable.none);
				bgImage.setBackgroundColor(Color.parseColor("#ffffff"));
				bgText.setText(getString(R.string.a_default));
			} else {
				File In = new File(rootAssetsPath + "" + imagesPath + BG);
				Glide.with(this)
					.load(In)
					.placeholder(R.drawable.none)
					.into(bgImage);
				bgText.setText(
					"تصویر ←   "
					+ picker.getImageNameById(bg));
				bgImage.setBackgroundColor(Color.TRANSPARENT);
			}
		} else {
			bgImage.setImageResource(R.drawable.none);
			bgImage.setBackgroundColor(Color.parseColor("#ffffff"));
			bgText.setText(getString(R.string.a_default));
		}
		String actBar =  myDb.getActionBar();
		ACTIONBAR_MODE = actBar;
		String acbTitle = myDb.getActionBarTitle();
		String acbBg = myDb.getActionBarColor();
		ACTIONBAR_TITLE = acbTitle;
		ACTIONBAR_COLOR = acbBg;
		tollbarTitle.setText(acbTitle);
		if (acbBg.startsWith("#")) {
			tollbarColorImage.setBackgroundColor(Color.parseColor(acbBg));						
		} else {
			tollbarColorImage.setBackgroundColor(Color.DKGRAY);
		}
		if (actBar.equals("Show")) {
			tollbarTitle.setEnabled(true);
			tollbarTitle.setEnabled(true);
			tollbarColor.setEnabled(true);
			tollbarColor.setAlpha(1);
			tollbarTitle.setAlpha(1);
			tollbarSwitch.setChecked(true);


		} else {
			//actionBar.hide();
			tollbarTitle.setEnabled(false);
			tollbarTitle.setEnabled(false);
			tollbarColor.setEnabled(false);
			tollbarColor.setAlpha(0.5f);
			tollbarTitle.setAlpha(0.5f);
			tollbarSwitch.setChecked(false);

		}
		String menu = myDb.getMenuMood();
		if (menu.equals("On")) {
			menuColor.setEnabled(true);
			menuColor.setAlpha(1);
			menuSwitch.setChecked(true);


		} else {
			//actionBar.hide();
			menuColor.setEnabled(false);
			menuColor.setAlpha(0.5f);
			menuSwitch.setChecked(false);

		}

		try {
			FileInputStream fileIn = new FileInputStream(rootAssetsPath + "menu/" + myDb.getMenuName());
			InputStream myInput = fileIn;
			int size = myInput.available();
			byte[] buffer = new byte[size];
			myInput.read(buffer);
			myInput.close();
			final String Content = new String(buffer);
			final String[] contentarray = Content.split("\\!\\#");
			for (String part : contentarray) {
				String part2=part;
				if (part2.contains("{ThisMenu<=>")) {
					String[] code = part2.replace("}", "").split("<=>");
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
							File In = new File(rootAssetsPath + "" + imagesPath + Bg);
							Glide.with(this)
								.load(In)
								.placeholder(R.drawable.none)
								.into(menuBgImage);
							menuBgText.setText(
								"تصویر ←   "
								+ picker.getImageNameById(bg));
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
		myDb.close();
	}
	public void onResume() {
        super.onResume();
        Refresh();
    }
}
