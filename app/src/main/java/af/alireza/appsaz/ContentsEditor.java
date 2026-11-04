package af.alireza.appsaz;

import af.alireza.appsaz.ActionPickerDialog.OnActionSelectedListener;
import af.alireza.appsaz.AnimationPickerDialog.OnAnimSelectedListener;
import af.alireza.appsaz.ColorPickerDialog.OnColorSelectedListener;
import af.alireza.appsaz.FontPickerDialog.OnFontSelectedListener;
import af.alireza.appsaz.IconPickerDialog.OnIconSelectedListener;
import af.alireza.appsaz.ImagePickerDialog.OnImageSelectedListener;
import af.alireza.appsaz.MarginPickerDialog.OnDipSelectedListener;
import com.google.android.material.materialswitch.MaterialSwitch;
import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.BiggroupButton3;
import af.alireza.views.BiggroupButton2;
import af.alireza.views.BiggroupSeekbar;
import af.alireza.views.BiggroupTextView2;
import af.alireza.views.MagicalTextView_Master;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ContentsEditor {
	Activity context;

	private String getSomeInt() {
		Random rand = new Random();
		int max = 1000000;
		int min = 0;
		int randomNum1 = rand.nextInt((max - min) + 1) + min;
		int randomNum2 = rand.nextInt((max - min) + 1) + min + randomNum1;
		String rands = String.valueOf(randomNum1) + String.valueOf(randomNum2);
		return String.valueOf(System.currentTimeMillis());
	}

	String ID;
	String HeightForChange;
	String FontForChange;
	String OnClickForChange;
	String rootPath = Applications.getInstance().getExternalFilesDir(null).getPath() + "/Project/MyApp/assets/";
	String audiosPath = "files/audios/";
	String videosPath = "files/videos/";
	String imagesPath = "files/images/";
	String iconsPath = "files/icons/";
	String fontsPath = "files/fonts/";
    String str1 = Applications.getInstance().getString(R.string.editor_simple_text);
    String str2 = Applications.getInstance().getString(R.string.editor_advanced_text);
    String str3 = Applications.getInstance().getString(R.string.editor_pic);
    String str4 = Applications.getInstance().getString(R.string.editor_button);
    
	String newSText = "<=>Text"+str1+"<=>TextColor#000000<=>TextSize20<=>GravityAuto<=>Fontnone<=>Animationnone<=>HeightWRAP_CONTENT<=>TopMargin0<=>LeftMargin0<=>RightMargin0<=>BottomMargin0<=>SelectableFalse}!#";
	String newAText = "<=>BackgroundColor#ffffff<=>Text"+str2+"<=>TextColor#000000<=>TextSize20<=>GravityAuto<=>Fontnone<=>Radius12<=>Animationnone<=>HeightWRAP_CONTENT<=>TopMargin0<=>LeftMargin0<=>RightMargin0<=>BottomMargin0<=>SelectableFalse<=>ShareableFalse<=>OnClicknone}!#";
	String newImage = "<=>Imagenone<=>Animationnone<=>HeightWRAP_CONTENT<=>TopMargin0<=>LeftMargin0<=>RightMargin0<=>BottomMargin0<=>OnClicknone}!#";
	String newButton = "<=>BackgroundColor#ffffff<=>Text"+str4+"<=>TextColor#000000<=>TextSize20<=>Fontnone<=>RightIconnone<=>LeftIconnone<=>Radius12<=>Animationnone<=>HeightWRAP_CONTENT<=>TopMargin0<=>LeftMargin0<=>RightMargin0<=>BottomMargin0<=>OnClicknone}!#";

	Picker picker = new Picker();
	Utilities utils = new Utilities();

	LayoutParams lParams;

	public ContentsEditor(Activity context) {
		this.context = context;
	}

	public void setContentsEditor(final String data, final LinearLayout contentlinear,
			MagicalTextView_Master ADD) {
		Refresh(data, contentlinear);
		ADD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ID = getSomeInt();
				final EditorDialog dialog = new EditorDialog(context);
				TextView dialog_Title = dialog.getTitle();
				LinearLayout dialog_Content = dialog.getContent();
				dialog_Title.setText(context.getString(R.string.a_add_new_item));
				BiggroupButton2 STextView = new BiggroupButton2(context);
				STextView.setText(context.getString(R.string.editor_simple_text));
				BiggroupButton2 ATextView = new BiggroupButton2(context);
				ATextView.setText(context.getString(R.string.editor_advanced_text));
				BiggroupButton2 Button = new BiggroupButton2(context);
				Button.setText(context.getString(R.string.editor_button));
				BiggroupButton2 ImageView = new BiggroupButton2(context);
				ImageView.setText(context.getString(R.string.editor_pic));
				dialog_Content.addView(STextView);
				dialog_Content.addView(ATextView);
				dialog_Content.addView(Button);
				dialog_Content.addView(ImageView);
				STextView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String str = utils.readFile(data);
						str = str + "{SText<=>" + ID + newSText;
						if (str.startsWith("null")) {
							str = str.replace("null", "");
						}
						utils.writeFile(data, str);
						Refresh(data, contentlinear);
						dialog.dismiss();
					}
				});
				ATextView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String str = utils.readFile(data);
						str = str + "{AText<=>" + ID + newAText;
						if (str.startsWith("null")) {
							str = str.replace("null", "");
						}
						utils.writeFile(data, str);
						Refresh(data, contentlinear);
						dialog.dismiss();
					}
				});
				Button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String str = utils.readFile(data);
						str = str + "{Button<=>" + ID + newButton;
						if (str.startsWith("null")) {
							str = str.replace("null", "");
						}
						utils.writeFile(data, str);
						Refresh(data, contentlinear);
						dialog.dismiss();
					}
				});
				ImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String str = utils.readFile(data);
						str = str + "{Image<=>" + ID + newImage;
						if (str.startsWith("null")) {
							str = str.replace("null", "");
						}
						utils.writeFile(data, str);
						Refresh(data, contentlinear);
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

	private void Refresh(final String data, final LinearLayout contentlinear) {
		contentlinear.removeAllViews();
		final String Info = utils.readFile(data);
		try {
			final String Content = Info;
			final String[] contentarray = Content.split("\\!\\#");
			for (String part : contentarray) {
				String part2 = part;
				ImageView imageView;
				final BiggroupTextView2 Atextview;
				final BiggroupButton3 button;
				final TextView Stextview;

				if (part2.contains("{Image<=>")) {
					final String[] code = part2.replace("}", "").split("<=>");
					final String itemId = code[1];
					final String image = code[2].replaceFirst("Image", "");
					final String animation = code[3].replace("Animation", "");
					final String height = code[4].replace("Height", "");
					final String onclick = code[9].replace("OnClick", "");
					imageView = new ImageView(context);
                    imageView.setBackgroundResource(R.drawable.bg_stroke);
					if (image.startsWith("image")) {
						File In = new File(rootPath + imagesPath + image);
						if (In.exists()) {
							Glide.with(context).load(In).placeholder(R.drawable.none).into(imageView);
						} else {
							imageView.setImageResource(R.drawable.ic_image);
						}
					} else {
						imageView.setImageResource(R.drawable.ic_image);
					}
					contentlinear.addView(imageView);
					lParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
					if (height.startsWith("WRAP_CONTENT")) {
						lParams.height = lParams.WRAP_CONTENT;
					} else if (height.startsWith("Size")) {
						String str = height.replace("Size", "");
						int percent = Integer.parseInt(str)*20;
						int Height = percent;
						lParams.height = Height;
					}
					lParams.width = lParams.MATCH_PARENT;
					final int topMargin = Integer.parseInt(code[5].replace("TopMargin", ""));
					final int leftMargin = Integer.parseInt(code[6].replace("LeftMargin", ""));
					final int rightMargin = Integer.parseInt(code[7].replace("RightMargin", ""));
					final int bottomMargin = Integer.parseInt(code[8].replace("BottomMargin", ""));
					lParams.topMargin = topMargin*12;
					lParams.leftMargin = leftMargin*6;
					lParams.rightMargin = rightMargin*6;
					lParams.bottomMargin = bottomMargin*12;
					imageView.setLayoutParams(lParams);
					if (animation.equals("left_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_in);
						imageView.startAnimation(anim);
					} else if (animation.equals("right_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
						imageView.startAnimation(anim);
					} else if (animation.equals("bounce")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.bouncing);
						imageView.startAnimation(anim);
					} else if (animation.equals("slide_down")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
						imageView.startAnimation(anim);
					} else if (animation.equals("fade_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
						imageView.startAnimation(anim);
					}
					final String imageInfo = "{Image<=>" + itemId + "<=>Image" + image + "<=>Animation" + animation
							+ "<=>Height" + height + "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
							+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin + "<=>OnClick" + onclick
							+ "}!#";
					imageView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							final EditorDialog dialog = new EditorDialog(context);
							TextView dialog_Title = dialog.getTitle();
							LinearLayout dialog_Content = dialog.getContent();
							final TextView srcTitle = new TextView(context);
							srcTitle.setText(context.getString(R.string.editor_pic));
							final BiggroupButton2 Src = new BiggroupButton2(context);
							final TextView animTitle = new TextView(context);
							animTitle.setText(context.getString(R.string.editor_animation));
							final BiggroupButton2 Anim = new BiggroupButton2(context);
							final TextView heightTitle = new TextView(context);
							heightTitle.setText(context.getString(R.string.editor_height));
							final BiggroupButton2 Height = new BiggroupButton2(context);
							final TextView marginsTitle = new TextView(context);
							marginsTitle.setText(context.getString(R.string.editor_margins));
							final BiggroupButton2 Margins = new BiggroupButton2(context);
							final TextView onClickTitle = new TextView(context);
							onClickTitle.setText(context.getString(R.string.editor_action));
							final BiggroupButton2 OnClick = new BiggroupButton2(context);
							TextView otherTitle = new TextView(context);
							otherTitle.setText(context.getString(R.string.editor_other));
							final BiggroupButton2 Above = new BiggroupButton2(context);
							final BiggroupButton2 Below = new BiggroupButton2(context);
							final BiggroupButton2 Copy = new BiggroupButton2(context);
							Copy.setText(context.getString(R.string.editor_copy));
							final BiggroupButton2 Delete = new BiggroupButton2(context);
							Margins.setText(context.getString(R.string.editor_margins));
							Above.setText(context.getString(R.string.editor_add_above));
							Below.setText(context.getString(R.string.editor_add_below));
							Delete.setText(context.getString(R.string.editor_delete));
							dialog_Content.addView(srcTitle);
							dialog_Content.addView(Src);
							dialog_Content.addView(animTitle);
							dialog_Content.addView(Anim);
							dialog_Content.addView(heightTitle);
							dialog_Content.addView(Height);
							dialog_Content.addView(marginsTitle);
							dialog_Content.addView(Margins);
							dialog_Content.addView(onClickTitle);
							dialog_Content.addView(OnClick);
							dialog_Content.addView(otherTitle);
							dialog_Content.addView(Above);
							dialog_Content.addView(Below);
							dialog_Content.addView(Copy);
							dialog_Content.addView(Delete);
							Src.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.a_picture)+context.getString(R.string.a_split) + Src.getText().toString();
									new ImagePickerDialog(context, title, new OnImageSelectedListener() {
										public void onImageSelected(String fileName, String fileId) {
											String str = Info.replace(imageInfo,
													"{Image<=>" + itemId + "<=>Image" + fileId + "<=>Animation"
															+ animation + "<=>Height" + height + "<=>TopMargin"
															+ topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Src.setText(fileName);
											Refresh(data, contentlinear);
										}
									}).show();
								}
							});
							Anim.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_animation)+context.getString(R.string.a_split) + Anim.getText().toString();
									new AnimationPickerDialog(context, title, new OnAnimSelectedListener() {
										public void onAnimSelected(String name, String anim) {
											String str = Info.replace(imageInfo,
													"{Image<=>" + itemId + "<=>Image" + image + "<=>Animation" + anim
															+ "<=>Height" + height + "<=>TopMargin" + topMargin
															+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
															+ rightMargin + "<=>BottomMargin" + bottomMargin
															+ "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
										}
									}).show();
								}
							});
							Height.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									HeightForChange = height;
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_height)+context.getString(R.string.a_split) + Height.getText().toString());

									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									final TextView aotuText = new TextView(context);
									aotuText.setText(context.getString(R.string.a_auto));
									final TextView sizeText = new TextView(context);
									sizeText.setText(context.getString(R.string.a_size));
									final BiggroupSeekbar Seekbar = new BiggroupSeekbar(context);
									MaterialSwitch toggle = new MaterialSwitch(context);
                                    toggle.setBackgroundResource(R.drawable.row_bg);
									dialog_Content.addView(aotuText);
									dialog_Content.addView(toggle);
									dialog_Content.addView(sizeText);
									dialog_Content.addView(Seekbar);
									Seekbar.setMax(100);
									toggle.setText(context.getString(R.string.editor_auto_height));
									toggle.setGravity(17);
									toggle.setTextOff(context.getString(R.string.a_off));
									toggle.setTextOn(context.getString(R.string.a_on));
									if (height.startsWith("WRAP")) {
										toggle.setChecked(true);
										sizeText.setEnabled(false);
										Seekbar.setEnabled(false);
										Seekbar.setProgress(50);
									} else {
										toggle.setChecked(false);
										sizeText.setEnabled(true);
										Seekbar.setEnabled(true);
										Seekbar.setProgress(Integer.parseInt(height.replace("Size", "")));
									}
									toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
										public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
											if (isChecked) {
												// در حالت روشن است
												sizeText.setEnabled(false);
												Seekbar.setEnabled(false);
												HeightForChange = "WRAP_CONTENT";
											} else {
												// در حالت خاموش است
												sizeText.setEnabled(true);
												Seekbar.setEnabled(true);
												HeightForChange = "Size" + String.valueOf(Seekbar.getProgress());
											}
										}
									});
									Seekbar.setOnSeekBarChangeListener(new BiggroupSeekbar.OnSeekBarChangeListener() {
										@Override
										public void OnProgressChanged(BiggroupSeekbar biggroupSeekBar, int i,
												boolean z) {
											// TODO: Implement this method
										}

										@Override
										public void onStartTrackingTouch(BiggroupSeekbar biggroupSeekBar) {
											// TODO: Implement this method
										}

										@Override
										public void onStopTrackingTouch(BiggroupSeekbar seekBar) {
											// TODO: Implement this method
											if (seekBar.getProgress() < 2) {
												seekBar.setProgress(2);
											}
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
											if (HeightForChange.startsWith("WRAP")) {
												String str = Info.replace(imageInfo,
														"{Image<=>" + itemId + "<=>Image" + image + "<=>Animation"
																+ animation + "<=>Height" + HeightForChange
																+ "<=>TopMargin" + topMargin + "<=>LeftMargin"
																+ leftMargin + "<=>RightMargin" + rightMargin
																+ "<=>BottomMargin" + bottomMargin + "<=>OnClick"
																+ onclick + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											} else {
												HeightForChange = "Size" + String.valueOf(Seekbar.getProgress());
												String str = Info.replace(imageInfo,
														"{Image<=>" + itemId + "<=>Image" + image + "<=>Animation"
																+ animation + "<=>Height" + HeightForChange
																+ "<=>TopMargin" + topMargin + "<=>LeftMargin"
																+ leftMargin + "<=>RightMargin" + rightMargin
																+ "<=>BottomMargin" + bottomMargin + "<=>OnClick"
																+ onclick + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											}
											HeightForChange = "";

										}
									});
									dialog.show();
								}
							});
							Margins.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_margins);
									new MarginPickerDialog(context, title, topMargin, bottomMargin, rightMargin,
											leftMargin, new OnDipSelectedListener() {
												@Override
												public void onDipSelected(int topMargin, int bottomMargin,
														int rightMargin, int leftMargin) {
													// TODO: Implement this method
													String str = Info.replace(imageInfo,
															"{Image<=>" + itemId + "<=>Image" + image + "<=>Animation"
																	+ animation + "<=>Height" + height + "<=>TopMargin"
																	+ topMargin + "<=>LeftMargin" + leftMargin
																	+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																	+ bottomMargin + "<=>OnClick" + onclick + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
												}
											}).show();
								}
							});
							OnClick.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_action)+context.getString(R.string.a_split) + OnClick.getText().toString();
                                    new ActionPickerDialog(context, title, OnClickForChange, new OnActionSelectedListener() {
										public void onActionSelected(String action) {
											String str = Info.replace(imageInfo,
													"{Image<=>" + itemId + "<=>Image" + image + "<=>Animation"
															+ animation + "<=>Height" + height + "<=>TopMargin"
															+ topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>OnClick" + action + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
										}
									}).show();
                                      
										}
									});
									
							Above.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									ID = getSomeInt();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(Above.getText().toString());

									BiggroupButton2 STextView = new BiggroupButton2(context);
									STextView.setText(context.getString(R.string.editor_simple_text));
									BiggroupButton2 ATextView = new BiggroupButton2(context);
									ATextView.setText(context.getString(R.string.editor_advanced_text));
									BiggroupButton2 Button = new BiggroupButton2(context);
									Button.setText(context.getString(R.string.editor_button));
									BiggroupButton2 ImageView = new BiggroupButton2(context);
									ImageView.setText(context.getString(R.string.editor_pic));
									dialog_Content.addView(STextView);
									dialog_Content.addView(ATextView);
									dialog_Content.addView(Button);
									dialog_Content.addView(ImageView);
									STextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(imageInfo,
													"{SText<=>" + ID + newSText + imageInfo);
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});
									ATextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(imageInfo,
													"{AText<=>" + ID + newAText + imageInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});
									Button.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(imageInfo,
													"{Button<=>" + ID + newButton + imageInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ImageView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(imageInfo,
													"{Image<=>" + ID + newImage + imageInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});
									dialog.show();
								}
							});
							Below.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									ID = getSomeInt();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(Below.getText().toString());

									BiggroupButton2 STextView = new BiggroupButton2(context);
									STextView.setText(context.getString(R.string.editor_simple_text));
									BiggroupButton2 ATextView = new BiggroupButton2(context);
									ATextView.setText(context.getString(R.string.editor_advanced_text));
									BiggroupButton2 Button = new BiggroupButton2(context);
									Button.setText(context.getString(R.string.editor_button));
									BiggroupButton2 ImageView = new BiggroupButton2(context);
									ImageView.setText(context.getString(R.string.editor_pic));
									dialog_Content.addView(STextView);
									dialog_Content.addView(ATextView);
									dialog_Content.addView(Button);
									dialog_Content.addView(ImageView);
									STextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(imageInfo,
													imageInfo + "{SText<=>" + ID + newSText);
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ATextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(imageInfo,
													imageInfo + "{AText<=>" + ID + newAText);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});
									Button.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(imageInfo,
													imageInfo + "{Button<=>" + ID + newButton);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ImageView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(imageInfo,
													imageInfo + "{Image<=>" + ID + newImage);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Copy.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String ID = getSomeInt();
									String imageInfo = "{Image<=>" + ID + "<=>Image" + image + "<=>Animation"
											+ animation + "<=>Height" + height + "<=>TopMargin" + topMargin
											+ "<=>LeftMargin" + leftMargin + "<=>RightMargin" + rightMargin
											+ "<=>BottomMargin" + bottomMargin + "<=>OnClick" + onclick + "}!#";
									String str = Info + imageInfo;
									if (Info.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(data, str);
									Refresh(data, contentlinear);
									dialog.dismiss();
								}
							});
							Delete.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String str = Info.replace(imageInfo, "");
									if (Info.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(data, str);
									Refresh(data, contentlinear);
									dialog.dismiss();
								}
							});

							if (height.startsWith("WRAP")) {
								Height.setText(context.getString(R.string.a_auto));
							} else {
								Height.setText(height.replace("Size", "")+"%");
							}
							if (picker.getImageNameById(image).startsWith("none")) {
								Src.setText(context.getString(R.string.editor_empty_pic));
								dialog_Title.setText(context.getString(R.string.editor_empty_pic));
							} else {
								File In = new File(rootPath + imagesPath + image);
								Src.setLeftIcon(In);
								Src.setText(picker.getImageNameById(image));
								dialog_Title.setText(context.getString(R.string.a_picture)+context.getString(R.string.a_split)+picker.getImageNameById(image));
							}
							if (animation.equals("left_in")) {
								Anim.setText(context.getString(R.string.editor_anim_left_in));
							} else if (animation.equals("right_in")) {
								Anim.setText(context.getString(R.string.editor_anim_right_in));
							} else if (animation.equals("fade_in")) {
								Anim.setText(context.getString(R.string.editor_anim_fade_in));
							} else if (animation.equals("slide_down")) {
								Anim.setText(context.getString(R.string.editor_anim_slide_down));
							} else if (animation.equals("bounce")) {
								Anim.setText(context.getString(R.string.editor_anim_bounce));
							} else if (animation.equals("blink")) {
								Anim.setText(context.getString(R.string.editor_anim_blinking));
							} else if (animation.equals("rotation")) {
								Anim.setText(context.getString(R.string.editor_anim_rotation));
							} else if (animation.equals("shake")) {
								Anim.setText(context.getString(R.string.editor_anim_shake));
							} else {
								Anim.setText(context.getString(R.string.editor_no_animation));
							}
							Margins.setText(context.getString(R.string.editor_top_margin)+context.getString(R.string.a_split) + topMargin +"%" + "\n" + context.getString(R.string.editor_bottom_margin)+context.getString(R.string.a_split) + bottomMargin +"%" + "\n"
									+ context.getString(R.string.editor_right_margin)+context.getString(R.string.a_split) + rightMargin +"%" + "\n" + context.getString(R.string.editor_left_margin)+context.getString(R.string.a_split) + leftMargin +"%");
							if (onclick.startsWith("GotoPage")) {
								String str = onclick.replaceFirst("GotoPage", "");
								if (new File(rootPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_open_page)+context.getString(R.string.a_split) + picker.getPageNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("GotoList")) {
								String str = onclick.replaceFirst("GotoList", "");
								if (new File(rootPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_open_list)+context.getString(R.string.a_split) + picker.getListNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("PlayAudio")) {
								String str = onclick.replaceFirst("PlayAudio", "");
								if (new File(rootPath + audiosPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_play_audio)+context.getString(R.string.a_split) + picker.getAudioNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("PlayVideo")) {
								String str = onclick.replaceFirst("PlayVideo", "");
								if (new File(rootPath + videosPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_play_video)+context.getString(R.string.a_split) + picker.getVideoNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("ShowImage")) {
								String str = onclick.replaceFirst("ShowImage", "");
								if (new File(rootPath + imagesPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_show_pic)+context.getString(R.string.a_split) + picker.getImageNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("ShowToast")) {
								String str = onclick.replaceFirst("ShowToast", "");
								OnClickForChange = "ShowToast" + str;
								OnClick.setText(context.getString(R.string.editor_action_show_toast)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("ShowAlert")) {
								String str = onclick.replaceFirst("ShowAlert", "");
								OnClickForChange = "ShowAlert" + str;
								String[] message = str.split("<!>");
								OnClick.setText(
										context.getString(R.string.editor_action_show_alert)+context.getString(R.string.a_split)+"\n" + context.getString(R.string.editor_action_alert_title)+":"+"\n" + message[0].replaceFirst("Title", "")
												+ "\n"+context.getString(R.string.editor_action_alert_text)+":"+"\n" + message[1].replaceFirst("Text", ""));
							} else if (onclick.startsWith("SendEmail")) {
								String str = onclick.replaceFirst("SendEmail", "");
                               OnClickForChange = "SendEmail" + str;
								OnClick.setText(context.getString(R.string.editor_action_send_mail)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("SendSms")) {
								String str = onclick.replaceFirst("SendSms", "");
                               OnClickForChange = "SendSms" + str;
								OnClick.setText(context.getString(R.string.editor_action_send_sms)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("OpenLink")) {
								String str = onclick.replaceFirst("OpenLink", "");
                                OnClickForChange = "OpenLink" + str;
								OnClick.setText(context.getString(R.string.editor_action_open_link)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("Exit")) {
                                OnClickForChange = "Exit";
								OnClick.setText(context.getString(R.string.editor_action_exit)+context.getString(R.string.a_split));
							} else {
								OnClickForChange = "none";
								OnClick.setText(context.getString(R.string.editor_no_action));
							}
							dialog.show();
						}
					});
				} else if (part2.contains("{Button<=>")) {
					String[] code = part2.replace("}", "").split("<=>");
					button = new BiggroupButton3(context);
					final String itemId = code[1];
					final String bg = code[2].replace("Background", "");
					final String text = code[3].replaceFirst("Text", "").replace("@*&$split=<=>!?$?", "<=>")
							.replace("{split}={*?}%@$*?", "!#");
					final String textColor = code[4].replace("TextColor", "");
					final String textSize = code[5].replace("TextSize", "");
					final String font = code[6].replace("Font", "");
					final String rightIcon = code[7].replace("RightIcon", "");
					final String leftIcon = code[8].replace("LeftIcon", "");
					final String Radios = code[9].replace("Radius", "");
					final String animation = code[10].replace("Animation", "");
					final String height = code[11].replace("Height", "");
					final String onclick = code[16].replace("OnClick", "");
					if (bg.startsWith("Color")) {
						String str = bg.replace("Color", "");
						button.setBackgroundColor(Color.parseColor(str));
						//txt.setText(bgColor+"<"+cRadios);
					} else if (bg.startsWith("Image")) {
						String str = bg.replace("Image", "");
						if (new File(rootPath + imagesPath + str).exists()) {
							FileInputStream In = new FileInputStream(rootPath + imagesPath + str);
							InputStream inputStream = In;
							button.setBackgroundImage(Drawable.createFromStream(inputStream, ""));
						}
					}
					if (font.startsWith("font")) {
						if (new File(rootPath + fontsPath + font).exists()) {
							Typeface tf = Typeface.createFromFile(rootPath + fontsPath + font);
							button.setTypeface(tf);
						}
					}
					button.setTextColor(Color.parseColor(textColor));
					int Tsize = Integer.parseInt(textSize);
					button.setTextSize(Tsize);
					button.setText(text);

					if (leftIcon.startsWith("icon")) {
						if (new File(rootPath + iconsPath + leftIcon).exists()) {
							File In = new File(rootPath + iconsPath + leftIcon);
							button.setLeftIcon(In);
						}
					}
					if (rightIcon.startsWith("icon")) {
						if (new File(rootPath + iconsPath + rightIcon).exists()) {
							File In = new File(rootPath + iconsPath + rightIcon);
							button.setRightIcon(In);
						}
					}
					contentlinear.addView(button);
					lParams = (LinearLayout.LayoutParams) button.getLayoutParams();
					if (height.startsWith("WRAP_CONTENT")) {
						lParams.height = lParams.WRAP_CONTENT;
					} else if (height.startsWith("Size")) {
						String str = height.replace("Size", "");
						int Height = Integer.parseInt(str)*20;
						lParams.height = Height;
					}
					lParams.width = lParams.MATCH_PARENT;
					final int topMargin = Integer.parseInt(code[12].replace("TopMargin", ""));
					final int leftMargin = Integer.parseInt(code[13].replace("LeftMargin", ""));
					final int rightMargin = Integer.parseInt(code[14].replace("RightMargin", ""));
					final int bottomMargin = Integer.parseInt(code[15].replace("BottomMargin", ""));
					lParams.topMargin = topMargin*12;
					lParams.leftMargin = leftMargin*6;
					lParams.rightMargin = rightMargin*6;
					lParams.bottomMargin = bottomMargin*12;
					button.setLayoutParams(lParams);
					button.setCRadios(Integer.parseInt(Radios)*5);
					if (animation.equals("left_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_in);
						button.startAnimation(anim);
					} else if (animation.equals("right_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
						button.startAnimation(anim);
					} else if (animation.equals("bounce")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.bouncing);
						button.startAnimation(anim);
					} else if (animation.equals("slide_down")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
						button.startAnimation(anim);
					} else if (animation.equals("fade_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
						button.startAnimation(anim);
					}
					final String buttonInfo = "{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
							+ "<=>TextColor" + textColor + "<=>TextSize" + textSize + "<=>Font" + font + "<=>RightIcon"
							+ rightIcon + "<=>LeftIcon" + leftIcon + "<=>Radius" + Radios + "<=>Animation" + animation
							+ "<=>Height" + height + "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
							+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin + "<=>OnClick" + onclick
							+ "}!#";
					button.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							final EditorDialog dialog = new EditorDialog(context);
							TextView dialog_Title = dialog.getTitle();
							LinearLayout dialog_Content = dialog.getContent();
							dialog_Title.setText(context.getString(R.string.editor_button)+context.getString(R.string.a_split)+text);

							TextView textTitle = new TextView(context);
							textTitle.setText(context.getString(R.string.editor_text));
							final BiggroupButton2 Text = new BiggroupButton2(context);
							TextView backgroundTitle = new TextView(context);
							backgroundTitle.setText(context.getString(R.string.editor_background));
							final BiggroupButton2 Background = new BiggroupButton2(context);
							TextView textSizeTitle = new TextView(context);
							textSizeTitle.setText(context.getString(R.string.editor_text_size));
							final BiggroupButton2 TextSize = new BiggroupButton2(context);
							TextView textColorTitle = new TextView(context);
							textColorTitle.setText(context.getString(R.string.editor_text_color));
							final BiggroupButton2 TextColor = new BiggroupButton2(context);
							TextView fontTitle = new TextView(context);
							fontTitle.setText(context.getString(R.string.editor_font));
							final BiggroupButton2 Font = new BiggroupButton2(context);
							TextView radiusTitle = new TextView(context);
							radiusTitle.setText(context.getString(R.string.editor_radius));
							final BiggroupButton2 Radius = new BiggroupButton2(context);
							TextView rIconTitle = new TextView(context);
							rIconTitle.setText(context.getString(R.string.editor_right_icon));
							final BiggroupButton2 RIcon = new BiggroupButton2(context);
							TextView lIconTitle = new TextView(context);
							lIconTitle.setText(context.getString(R.string.editor_left_icon));
							final BiggroupButton2 LIcon = new BiggroupButton2(context);
							TextView animTitle = new TextView(context);
							animTitle.setText(context.getString(R.string.editor_animation));
							final BiggroupButton2 Anim = new BiggroupButton2(context);
							TextView heightTitle = new TextView(context);
							heightTitle.setText(context.getString(R.string.editor_height));
							final BiggroupButton2 Height = new BiggroupButton2(context);
							TextView marginsTitle = new TextView(context);
							marginsTitle.setText(context.getString(R.string.editor_margins));
							final BiggroupButton2 Margins = new BiggroupButton2(context);
							final TextView onClickTitle = new TextView(context);
							onClickTitle.setText(context.getString(R.string.editor_action));
							final BiggroupButton2 OnClick = new BiggroupButton2(context);
							TextView otherTitle = new TextView(context);
							otherTitle.setText(context.getString(R.string.editor_other));
							final BiggroupButton2 Above = new BiggroupButton2(context);
							final BiggroupButton2 Below = new BiggroupButton2(context);
							final BiggroupButton2 Copy = new BiggroupButton2(context);
							Copy.setText(context.getString(R.string.editor_copy));
							final BiggroupButton2 Delete = new BiggroupButton2(context);
							Margins.setText(context.getString(R.string.editor_margins));
							Above.setText(context.getString(R.string.editor_add_above));
							Below.setText(context.getString(R.string.editor_add_below));
							Delete.setText(context.getString(R.string.editor_delete));
							dialog_Content.addView(textTitle);
							dialog_Content.addView(Text);
							dialog_Content.addView(backgroundTitle);
							dialog_Content.addView(Background);
							dialog_Content.addView(textSizeTitle);
							dialog_Content.addView(TextSize);
							dialog_Content.addView(textColorTitle);
							dialog_Content.addView(TextColor);
							dialog_Content.addView(fontTitle);
							dialog_Content.addView(Font);
							dialog_Content.addView(rIconTitle);
							dialog_Content.addView(RIcon);
							dialog_Content.addView(lIconTitle);
							dialog_Content.addView(LIcon);
							dialog_Content.addView(radiusTitle);
							dialog_Content.addView(Radius);
							dialog_Content.addView(animTitle);
							dialog_Content.addView(Anim);
							dialog_Content.addView(heightTitle);
							dialog_Content.addView(Height);
							dialog_Content.addView(marginsTitle);
							dialog_Content.addView(Margins);
							dialog_Content.addView(onClickTitle);
							dialog_Content.addView(OnClick);
							dialog_Content.addView(otherTitle);
							dialog_Content.addView(Above);
							dialog_Content.addView(Below);
							dialog_Content.addView(Copy);
							dialog_Content.addView(Delete);
							Text.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text)+context.getString(R.string.a_split) + Text.getText().toString());

									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									final EditText textText = new EditText(context);
									textText.setHint(context.getString(R.string.editor_text));
									textText.setText(Text.getText().toString());
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
											String str = Info.replace(buttonInfo, "{Button<=>" + itemId
													+ "<=>Background" + bg + "<=>Text" + textText.getText().toString()
													+ "<=>TextColor" + textColor + "<=>TextSize" + textSize + "<=>Font"
													+ font + "<=>RightIcon" + rightIcon + "<=>LeftIcon" + leftIcon
													+ "<=>Radius" + Radios + "<=>Animation" + animation + "<=>Height"
													+ height + "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
													+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin
													+ "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							TextColor.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									new ColorPickerDialog(context, Color.parseColor(textColor), "Nothing",
											new OnColorSelectedListener() {
												public void onColorSelected(int color) {
													String Color = String.format("#%08X", color);
													String str = Info.replace(buttonInfo,
															"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text"
																	+ text + "<=>TextColor" + Color + "<=>TextSize"
																	+ textSize + "<=>Font" + font + "<=>RightIcon"
																	+ rightIcon + "<=>LeftIcon" + leftIcon + "<=>Radius"
																	+ Radios + "<=>Animation" + animation + "<=>Height"
																	+ height + "<=>TopMargin" + topMargin
																	+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																	+ rightMargin + "<=>BottomMargin" + bottomMargin
																	+ "<=>OnClick" + onclick + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
												}
											}).show();

								}
							});
							TextSize.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text_size)+context.getString(R.string.a_split) + TextSize.getText().toString());

									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									final BiggroupSeekbar sizeSeekbar = new BiggroupSeekbar(context);
									dialog_Content.addView(sizeSeekbar);
									sizeSeekbar.setMax(100);
									sizeSeekbar.setProgress(Integer.parseInt(textSize));

									dialog_Cancel.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									});
									dialog_Save.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String ts = String.valueOf(sizeSeekbar.getProgress());
											String str = Info.replace(buttonInfo,
													"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
															+ "<=>TextColor" + textColor + "<=>TextSize" + ts
															+ "<=>Font" + font + "<=>RightIcon" + rightIcon
															+ "<=>LeftIcon" + leftIcon + "<=>Radius" + Radios
															+ "<=>Animation" + animation + "<=>Height" + height
															+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Font.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_font)+context.getString(R.string.a_split) + Font.getText().toString();
									new FontPickerDialog(context, title, new OnFontSelectedListener() {
										public void onFontSelected(String fileName, String fileId) {
											if (fileId.equals("none")) {
												FontForChange = "none";
												String str = Info.replace(buttonInfo,
														"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
																+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
																+ "<=>Font" + FontForChange + "<=>RightIcon" + rightIcon
																+ "<=>LeftIcon" + leftIcon + "<=>Radius" + Radios
																+ "<=>Animation" + animation + "<=>Height" + height
																+ "<=>TopMargin" + topMargin + "<=>LeftMargin"
																+ leftMargin + "<=>RightMargin" + rightMargin
																+ "<=>BottomMargin" + bottomMargin + "<=>OnClick"
																+ onclick + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												FontForChange = "";
											} else {
												FontForChange = fileId;
												String str = Info.replace(buttonInfo,
														"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
																+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
																+ "<=>Font" + FontForChange + "<=>RightIcon" + rightIcon
																+ "<=>LeftIcon" + leftIcon + "<=>Radius" + Radios
																+ "<=>Animation" + animation + "<=>Height" + height
																+ "<=>TopMargin" + topMargin + "<=>LeftMargin"
																+ leftMargin + "<=>RightMargin" + rightMargin
																+ "<=>BottomMargin" + bottomMargin + "<=>OnClick"
																+ onclick + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												FontForChange = "";
											}
										}
									}).show();
								}
							});
							RIcon.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_right_icon)+context.getString(R.string.a_split) + RIcon.getText().toString();
									new IconPickerDialog(context, title, new OnIconSelectedListener() {
										public void onIconSelected(String fileName, String fileId) {
											String icon;
											if (fileId.equals("none")) {
												icon = "none";
											} else {
												icon = fileId;
											}
											String str = Info.replace(buttonInfo,
													"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
															+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
															+ "<=>Font" + font + "<=>RightIcon" + icon + "<=>LeftIcon"
															+ leftIcon + "<=>Radius" + Radios + "<=>Animation"
															+ animation + "<=>Height" + height + "<=>TopMargin"
															+ topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
										}
									}).show();
								}
							});
							LIcon.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_left_icon)+context.getString(R.string.a_split) + LIcon.getText().toString();
									new IconPickerDialog(context, title, new OnIconSelectedListener() {
										public void onIconSelected(String fileName, String fileId) {
											String icon;
											if (fileId.equals("none")) {
												icon = "none";
											} else {
												icon = fileId;
											}
											String str = Info.replace(buttonInfo,
													"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
															+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
															+ "<=>Font" + font + "<=>RightIcon" + rightIcon
															+ "<=>LeftIcon" + icon + "<=>Radius" + Radios
															+ "<=>Animation" + animation + "<=>Height" + height
															+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
										}
									}).show();
								}
							});
							Background.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_background)+context.getString(R.string.a_split) + Background.getText().toString());

									final BiggroupButton2 noneBtn = new BiggroupButton2(context);
									dialog_Content.addView(noneBtn);
									noneBtn.setText(context.getString(R.string.a_default));
									final BiggroupButton2 colorBtn = new BiggroupButton2(context);
									dialog_Content.addView(colorBtn);
									colorBtn.setText(context.getString(R.string.a_color));
									final BiggroupButton2 ImgBtn = new BiggroupButton2(context);
									dialog_Content.addView(ImgBtn);
									ImgBtn.setText(context.getString(R.string.editor_pic));
									final int oldColor;
									if (bg.startsWith("Color")) {
										oldColor = Color.parseColor(bg.replace("Color", ""));
									} else {
										oldColor = Color.WHITE;
									}
									noneBtn.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Str = "none";
											String str = Info.replace(buttonInfo,
													"{Button<=>" + itemId + "<=>Background" + Str + "<=>Text" + text
															+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
															+ "<=>Font" + font + "<=>RightIcon" + rightIcon
															+ "<=>LeftIcon" + leftIcon + "<=>Radius" + Radios
															+ "<=>Animation" + animation + "<=>Height" + height
															+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									colorBtn.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
											new ColorPickerDialog(context, oldColor, "Nothing",
													new OnColorSelectedListener() {
														public void onColorSelected(int color) {
															String Color = String.format("#%08X", color);
															Color = "Color" + Color;
															String str = Info.replace(buttonInfo, "{Button<=>" + itemId
																	+ "<=>Background" + Color + "<=>Text" + text
																	+ "<=>TextColor" + textColor + "<=>TextSize"
																	+ textSize + "<=>Font" + font + "<=>RightIcon"
																	+ rightIcon + "<=>LeftIcon" + leftIcon + "<=>Radius"
																	+ Radios + "<=>Animation" + animation + "<=>Height"
																	+ height + "<=>TopMargin" + topMargin
																	+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																	+ rightMargin + "<=>BottomMargin" + bottomMargin
																	+ "<=>OnClick" + onclick + "}!#");
															if (Info.startsWith("null")) {
																str = str.replace("null", "");
															}
															utils.writeFile(data, str);
															Refresh(data, contentlinear);
														}
													}).show();
										}
									});
									ImgBtn.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
											String title = context.getString(R.string.editor_background)+context.getString(R.string.a_split) + Background.getText().toString();
											new ImagePickerDialog(context, title, new OnImageSelectedListener() {
												public void onImageSelected(String fileName, String fileId) {
													String Str = "Image" + fileId;
													String str = Info.replace(buttonInfo,
															"{Button<=>" + itemId + "<=>Background" + Str + "<=>Text"
																	+ text + "<=>TextColor" + textColor + "<=>TextSize"
																	+ textSize + "<=>Font" + font + "<=>RightIcon"
																	+ rightIcon + "<=>LeftIcon" + leftIcon + "<=>Radius"
																	+ Radios + "<=>Animation" + animation + "<=>Height"
																	+ height + "<=>TopMargin" + topMargin
																	+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																	+ rightMargin + "<=>BottomMargin" + bottomMargin
																	+ "<=>OnClick" + onclick + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
												}
											}).show();
										}
									});
									dialog.show();
								}
							});
							Radius.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_radius)+context.getString(R.string.a_split) + Radius.getText().toString());

									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									final BiggroupSeekbar sizeSeekbar = new BiggroupSeekbar(context);
									dialog_Content.addView(sizeSeekbar);
									sizeSeekbar.setMax(100);
									sizeSeekbar.setProgress(Integer.parseInt(Radios));
									dialog_Cancel.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									});
									dialog_Save.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String rad = String.valueOf(sizeSeekbar.getProgress());
											String str = Info.replace(buttonInfo,
													"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
															+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
															+ "<=>Font" + font + "<=>RightIcon" + rightIcon
															+ "<=>LeftIcon" + leftIcon + "<=>Radius" + rad
															+ "<=>Animation" + animation + "<=>Height" + height
															+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();

										}
									});
									dialog.show();
								}
							});
							Anim.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_animation)+context.getString(R.string.a_split) + Anim.getText().toString();
									new AnimationPickerDialog(context, title, new OnAnimSelectedListener() {
										public void onAnimSelected(String name, String anim) {
											String str = Info.replace(buttonInfo,
													"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
															+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
															+ "<=>Font" + font + "<=>RightIcon" + rightIcon
															+ "<=>LeftIcon" + leftIcon + "<=>Radius" + Radios
															+ "<=>Animation" + anim + "<=>Height" + height
															+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
										}
									}).show();
								}
							});
							Height.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									HeightForChange = height;
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_height)+context.getString(R.string.a_split) + Height.getText().toString());
									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									final TextView aotuText = new TextView(context);
									aotuText.setText(context.getString(R.string.a_auto));
									final TextView sizeText = new TextView(context);
									sizeText.setText(context.getString(R.string.a_size));
									final BiggroupSeekbar Seekbar = new BiggroupSeekbar(context);
									MaterialSwitch toggle = new MaterialSwitch(context);
                                    toggle.setBackgroundResource(R.drawable.row_bg);
									dialog_Content.addView(aotuText);
									dialog_Content.addView(toggle);
									dialog_Content.addView(sizeText);
									dialog_Content.addView(Seekbar);
									Seekbar.setMax(100);
									toggle.setText(context.getString(R.string.editor_auto_height));
									toggle.setGravity(17);
									toggle.setTextOff(context.getString(R.string.a_off));
									toggle.setTextOn(context.getString(R.string.a_on));
									if (height.startsWith("WRAP")) {
										toggle.setChecked(true);
										sizeText.setEnabled(false);
										Seekbar.setEnabled(false);
										Seekbar.setProgress(50);
									} else {
										toggle.setChecked(false);
										sizeText.setEnabled(true);
										Seekbar.setEnabled(true);
										Seekbar.setProgress(Integer.parseInt(height.replace("Size", "")));
									}
									toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
										public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
											if (isChecked) {
												// در حالت روشن است
												sizeText.setEnabled(false);
												Text.setEnabled(false);
												Seekbar.setEnabled(false);
												HeightForChange = "WRAP_CONTENT";
											} else {
												// در حالت خاموش است
												sizeText.setEnabled(true);
												Text.setEnabled(true);
												Seekbar.setEnabled(true);
												HeightForChange = "Size" + String.valueOf(Seekbar.getProgress());
											}
										}
									});
									Seekbar.setOnSeekBarChangeListener(new BiggroupSeekbar.OnSeekBarChangeListener() {
										@Override
										public void OnProgressChanged(BiggroupSeekbar discreteSeekBar, int i,
												boolean z) {
											// TODO: Implement this method
										}

										@Override
										public void onStartTrackingTouch(BiggroupSeekbar discreteSeekBar) {
											// TODO: Implement this method
										}

										@Override
										public void onStopTrackingTouch(BiggroupSeekbar seekBar) {
											// TODO: Implement this method
											if (seekBar.getProgress() < 2) {
												seekBar.setProgress(2);
											}
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
											if (HeightForChange.startsWith("WRAP")) {
												String str = Info.replace(buttonInfo,
														"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
																+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
																+ "<=>Font" + font + "<=>RightIcon" + rightIcon
																+ "<=>LeftIcon" + leftIcon + "<=>Radius" + Radios
																+ "<=>Animation" + animation + "<=>Height"
																+ HeightForChange + "<=>TopMargin" + topMargin
																+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																+ rightMargin + "<=>BottomMargin" + bottomMargin
																+ "<=>OnClick" + onclick + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											} else {
												HeightForChange = "Size" + String.valueOf(Seekbar.getProgress());
												String str = Info.replace(buttonInfo,
														"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text" + text
																+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
																+ "<=>Font" + font + "<=>RightIcon" + rightIcon
																+ "<=>LeftIcon" + leftIcon + "<=>Radius" + Radios
																+ "<=>Animation" + animation + "<=>Height"
																+ HeightForChange + "<=>TopMargin" + topMargin
																+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																+ rightMargin + "<=>BottomMargin" + bottomMargin
																+ "<=>OnClick" + onclick + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											}
											HeightForChange = "";
										}
									});
									dialog.show();
								}
							});
							Margins.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_margins);
									new MarginPickerDialog(context, title, topMargin, bottomMargin, rightMargin,
											leftMargin, new OnDipSelectedListener() {
												@Override
												public void onDipSelected(int topMargin, int bottomMargin,
														int rightMargin, int leftMargin) {
													// TODO: Implement this method
													String str = Info.replace(buttonInfo,
															"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text"
																	+ text + "<=>TextColor" + textColor + "<=>TextSize"
																	+ textSize + "<=>Font" + font + "<=>RightIcon"
																	+ rightIcon + "<=>LeftIcon" + leftIcon + "<=>Radius"
																	+ Radios + "<=>Animation" + animation + "<=>Height"
																	+ height + "<=>TopMargin" + topMargin
																	+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																	+ rightMargin + "<=>BottomMargin" + bottomMargin
																	+ "<=>OnClick" + onclick + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
												}
											}).show();
								}
							});
							OnClick.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_action)+context.getString(R.string.a_split) + OnClick.getText().toString();
                                    new ActionPickerDialog(context, title, OnClickForChange, new OnActionSelectedListener() {
										public void onActionSelected(String action) {
											String str = Info.replace(buttonInfo,
															"{Button<=>" + itemId + "<=>Background" + bg + "<=>Text"
																	+ text + "<=>TextColor" + textColor + "<=>TextSize"
																	+ textSize + "<=>Font" + font + "<=>RightIcon"
																	+ rightIcon + "<=>LeftIcon" + leftIcon + "<=>Radius"
																	+ Radios + "<=>Animation" + animation + "<=>Height"
																	+ height + "<=>TopMargin" + topMargin
																	+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																	+ rightMargin + "<=>BottomMargin" + bottomMargin
																	+ "<=>OnClick" + action + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
										}
									}).show();
                                      
										}
									});
									
							Above.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									ID = getSomeInt();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(Above.getText().toString());

									BiggroupButton2 STextView = new BiggroupButton2(context);
									STextView.setText(context.getString(R.string.editor_simple_text));
									BiggroupButton2 ATextView = new BiggroupButton2(context);
									ATextView.setText(context.getString(R.string.editor_advanced_text));
									BiggroupButton2 Button = new BiggroupButton2(context);
									Button.setText(context.getString(R.string.editor_button));
									BiggroupButton2 ImageView = new BiggroupButton2(context);
									ImageView.setText(context.getString(R.string.editor_pic));
									dialog_Content.addView(STextView);
									dialog_Content.addView(ATextView);
									dialog_Content.addView(Button);
									dialog_Content.addView(ImageView);
									STextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(buttonInfo,
													"{SText<=>" + ID + newSText + buttonInfo);
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});
									ATextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(buttonInfo,
													"{AText<=>" + ID + newAText + buttonInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									Button.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(buttonInfo,
													"{Button<=>" + ID + newButton + buttonInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});
									ImageView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(buttonInfo,
													"{Image<=>" + ID + newImage + buttonInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Below.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									ID = getSomeInt();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(Below.getText().toString());

									BiggroupButton2 STextView = new BiggroupButton2(context);
									STextView.setText(context.getString(R.string.editor_simple_text));
									BiggroupButton2 ATextView = new BiggroupButton2(context);
									ATextView.setText(context.getString(R.string.editor_advanced_text));
									BiggroupButton2 Button = new BiggroupButton2(context);
									Button.setText(context.getString(R.string.editor_button));
									BiggroupButton2 ImageView = new BiggroupButton2(context);
									ImageView.setText(context.getString(R.string.editor_pic));
									dialog_Content.addView(STextView);
									dialog_Content.addView(ATextView);
									dialog_Content.addView(Button);
									dialog_Content.addView(ImageView);
									STextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(buttonInfo,
													buttonInfo + "{SText<=>" + ID + newSText);
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});
									ATextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(buttonInfo,
													buttonInfo + "{AText<=>" + ID + newAText);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									Button.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(buttonInfo,
													buttonInfo + "{Button<=>" + ID + newButton);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ImageView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(buttonInfo,
													buttonInfo + "{Image<=>" + ID + newImage);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Copy.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String ID = getSomeInt();
									String buttonInfo = "{Button<=>" + ID + "<=>Background" + bg + "<=>Text" + text
											+ "<=>TextColor" + textColor + "<=>TextSize" + textSize + "<=>Font" + font
											+ "<=>RightIcon" + rightIcon + "<=>LeftIcon" + leftIcon + "<=>Radius"
											+ Radios + "<=>Animation" + animation + "<=>Height" + height
											+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
											+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin
											+ "<=>OnClick" + onclick + "}!#";
									String str = Info + buttonInfo;
									if (Info.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(data, str);
									Refresh(data, contentlinear);
									dialog.dismiss();
								}
							});
							Delete.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String str = Info.replace(buttonInfo, "");
									if (Info.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(data, str);
									Refresh(data, contentlinear);
									dialog.dismiss();
								}
							});
							Text.setText(text);
							TextColor.setText(context.getString(R.string.a_color));
							TextColor.getLeftIcon().setBackgroundColor(Color.parseColor(textColor));
							TextSize.setText(textSize + "%");
							if (bg.startsWith("Color")) {
								String str = bg.replace("Color", "");
								Background.setText(context.getString(R.string.a_color));
								Background.getLeftIcon().setBackgroundColor(Color.parseColor(str));
							} else if (bg.startsWith("Image")) {
								String str = bg.replaceFirst("Image", "");
								if (picker.getImageNameById(str).startsWith("none")) {
									Background.setText(context.getString(R.string.a_default));
								} else {
									File In = new File(rootPath + imagesPath + str);
									Background.setLeftIcon(In);
									Background.setText(context.getString(R.string.a_picture)+context.getString(R.string.a_split) + picker.getImageNameById(str));
								}
							} else {
								Background.setText(context.getString(R.string.a_default));
							}
							if (picker.getFontNameById(font).equals("none")) {
								Font.setText(context.getString(R.string.a_default));
							} else {
								Typeface tf = Typeface.createFromFile(rootPath + fontsPath + font);
								Font.setTypeface(tf);
								Font.setText(picker.getFontNameById(font));
							}
							if (picker.getIconNameById(rightIcon).startsWith("none")) {
								RIcon.setText(context.getString(R.string.editor_no_icon));
							} else {
								File In = new File(rootPath + iconsPath + rightIcon);
								RIcon.setRightIcon(In);
								RIcon.setText(picker.getIconNameById(rightIcon));
							}
							if (picker.getIconNameById(leftIcon).startsWith("none")) {
								LIcon.setText(context.getString(R.string.editor_no_icon));
							} else {
								File In = new File(rootPath + iconsPath + leftIcon);
								LIcon.setLeftIcon(In);
								LIcon.setText(picker.getIconNameById(leftIcon));
							}
							Radius.setText(Radios + "%");
							if (height.startsWith("WRAP")) {
								Height.setText(context.getString(R.string.a_auto));
							} else {
								Height.setText(height.replace("Size", "") + "%");
							}
							if (animation.equals("left_in")) {
								Anim.setText(context.getString(R.string.editor_anim_left_in));
							} else if (animation.equals("right_in")) {
								Anim.setText(context.getString(R.string.editor_anim_right_in));
							} else if (animation.equals("fade_in")) {
								Anim.setText(context.getString(R.string.editor_anim_fade_in));
							} else if (animation.equals("slide_down")) {
								Anim.setText(context.getString(R.string.editor_anim_slide_down));
							} else if (animation.equals("bounce")) {
								Anim.setText(context.getString(R.string.editor_anim_bounce));
							} else if (animation.equals("blink")) {
								Anim.setText(context.getString(R.string.editor_anim_blinking));
							} else if (animation.equals("rotation")) {
								Anim.setText(context.getString(R.string.editor_anim_rotation));
							} else if (animation.equals("shake")) {
								Anim.setText(context.getString(R.string.editor_anim_shake));
							} else {
								Anim.setText(context.getString(R.string.editor_no_animation));
							}
							Margins.setText(context.getString(R.string.editor_top_margin)+context.getString(R.string.a_split) + topMargin + "%" + "\n" + context.getString(R.string.editor_bottom_margin)+context.getString(R.string.a_split) + bottomMargin + "%" + "\n"
									+ context.getString(R.string.editor_right_margin)+context.getString(R.string.a_split) + rightMargin + "%" + "\n" + context.getString(R.string.editor_left_margin)+context.getString(R.string.a_split) + leftMargin + "%");
							if (onclick.startsWith("GotoPage")) {
								String str = onclick.replaceFirst("GotoPage", "");
								if (new File(rootPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_open_page)+context.getString(R.string.a_split) + picker.getPageNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("GotoList")) {
								String str = onclick.replaceFirst("GotoList", "");
								if (new File(rootPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_open_list)+context.getString(R.string.a_split) + picker.getListNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("PlayAudio")) {
								String str = onclick.replaceFirst("PlayAudio", "");
								if (new File(rootPath + audiosPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_play_audio)+context.getString(R.string.a_split) + picker.getAudioNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("PlayVideo")) {
								String str = onclick.replaceFirst("PlayVideo", "");
								if (new File(rootPath + videosPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_play_video)+context.getString(R.string.a_split) + picker.getVideoNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("ShowImage")) {
								String str = onclick.replaceFirst("ShowImage", "");
								if (new File(rootPath + imagesPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_show_pic)+context.getString(R.string.a_split) + picker.getImageNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("ShowToast")) {
								String str = onclick.replaceFirst("ShowToast", "");
								OnClickForChange = "ShowToast" + str;
								OnClick.setText(context.getString(R.string.editor_action_show_toast)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("ShowAlert")) {
								String str = onclick.replaceFirst("ShowAlert", "");
								OnClickForChange = "ShowAlert" + str;
								String[] message = str.split("<!>");
								OnClick.setText(
										context.getString(R.string.editor_action_show_alert)+context.getString(R.string.a_split)+"\n" + context.getString(R.string.editor_action_alert_title)+":"+"\n" + message[0].replaceFirst("Title", "")
												+ "\n"+context.getString(R.string.editor_action_alert_text)+":"+"\n" + message[1].replaceFirst("Text", ""));
							} else if (onclick.startsWith("SendEmail")) {
								String str = onclick.replaceFirst("SendEmail", "");
                               OnClickForChange = "SendEmail" + str;
								OnClick.setText(context.getString(R.string.editor_action_send_mail)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("SendSms")) {
								String str = onclick.replaceFirst("SendSms", "");
                               OnClickForChange = "SendSms" + str;
								OnClick.setText(context.getString(R.string.editor_action_send_sms)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("OpenLink")) {
								String str = onclick.replaceFirst("OpenLink", "");
                                OnClickForChange = "OpenLink" + str;
								OnClick.setText(context.getString(R.string.editor_action_open_link)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("Exit")) {
                                OnClickForChange = "Exit";
								OnClick.setText(context.getString(R.string.editor_action_exit)+context.getString(R.string.a_split));
							} else {
								OnClickForChange = "none";
								OnClick.setText(context.getString(R.string.editor_no_action));
							}
							dialog.show();
						}
					});

				} else if (part2.contains("{SText<=>")) {
					String[] code = part2.replace("}", "").split("<=>");
					Stextview = new TextView(context);
                    Stextview.setBackgroundResource(R.drawable.bg_stroke);
				//	Stextview.setAutoLinkMask(1);
					final String itemId = code[1];
					final String text = code[2].replaceFirst("Text", "").replace("@*&$split=<=>!?$?", "<=>")
							.replace("{split}={*?}%@$*?", "!#");
					final String textColor = code[3].replace("TextColor", "");
					final String textSize = code[4].replace("TextSize", "");
					final String gravity = code[5].replace("Gravity", "");
					final String font = code[6].replace("Font", "");
					final String animation = code[7].replace("Animation", "");
					final String height = code[8].replace("Height", "");
					final String selectAble = code[13].replace("Selectable", "");
					if (selectAble.equals("True")) {
						Stextview.setTextIsSelectable(true);
					} else {
						Stextview.setTextIsSelectable(false);
					}
					if (gravity.startsWith("Right")) {
						Stextview.setGravity(5);
					} else if (gravity.startsWith("Center")) {
						Stextview.setGravity(17);
					} else if (gravity.startsWith("Left")) {
						Stextview.setGravity(3);
					}
					if (font.startsWith("font")) {
						if (new File(rootPath + fontsPath + font).exists()) {
							Typeface tf = Typeface.createFromFile(rootPath + fontsPath + font);
                           try {
                                Stextview.setTypeface(tf);
                                }
                            catch (Exception e)
                        	{
                                }
						}
					}
					Stextview.setTextColor(Color.parseColor(textColor));
					int TextSize = Integer.parseInt(textSize);
					Stextview.setTextSize(TextSize);
					Stextview.setText(text);
					contentlinear.addView(Stextview);
					lParams = (LinearLayout.LayoutParams) Stextview.getLayoutParams();
					if (height.startsWith("WRAP_CONTENT")) {
						lParams.height = lParams.WRAP_CONTENT;
					} else if (height.startsWith("Size")) {
						String str = height.replace("Size", "");
						int Height = Integer.parseInt(str)*20;
						lParams.height = Height;
					}
					lParams.width = lParams.MATCH_PARENT;
					final int topMargin = Integer.parseInt(code[9].replace("TopMargin", ""));
					final int leftMargin = Integer.parseInt(code[10].replace("LeftMargin", ""));
					final int rightMargin = Integer.parseInt(code[11].replace("RightMargin", ""));
					final int bottomMargin = Integer.parseInt(code[12].replace("BottomMargin", ""));
					lParams.topMargin = topMargin*12;
					lParams.leftMargin = leftMargin*6;
					lParams.rightMargin = rightMargin*6;
					lParams.bottomMargin = bottomMargin*12;
					Stextview.setLayoutParams(lParams);
					if (animation.equals("left_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_in);
						Stextview.startAnimation(anim);
					} else if (animation.equals("right_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
						Stextview.startAnimation(anim);
					} else if (animation.equals("bounce")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.bouncing);
						Stextview.startAnimation(anim);
					} else if (animation.equals("slide_down")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
						Stextview.startAnimation(anim);
					} else if (animation.equals("fade_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
						Stextview.startAnimation(anim);
					}
					final String StextInfo = "{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor" + textColor
							+ "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font" + font + "<=>Animation"
							+ animation + "<=>Height" + height + "<=>TopMargin" + topMargin + "<=>LeftMargin"
							+ leftMargin + "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin
							+ "<=>Selectable" + selectAble + "}!#";
					Stextview.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							final EditorDialog dialog = new EditorDialog(context);
							TextView dialog_Title = dialog.getTitle();
							LinearLayout dialog_Content = dialog.getContent();
							dialog_Title.setText(context.getString(R.string.editor_simple_text)+context.getString(R.string.a_split)+text);

							TextView textTitle = new TextView(context);
							textTitle.setText(context.getString(R.string.editor_text));
							final BiggroupButton2 Text = new BiggroupButton2(context);
							TextView textSizeTitle = new TextView(context);
							textSizeTitle.setText(context.getString(R.string.editor_text_size));
							final BiggroupButton2 TextSize = new BiggroupButton2(context);
							TextView textColorTitle = new TextView(context);
							textColorTitle.setText(context.getString(R.string.editor_text_color));
							final BiggroupButton2 TextColor = new BiggroupButton2(context);
							TextView selectAbleTitle = new TextView(context);
							selectAbleTitle.setText(context.getString(R.string.editor_text_is_selectable));
							final BiggroupButton2 Selectable = new BiggroupButton2(context);
							TextView gravityTitle = new TextView(context);
							gravityTitle.setText(context.getString(R.string.editor_text_gravity));
							final BiggroupButton2 Gravity = new BiggroupButton2(context);
							TextView fontTitle = new TextView(context);
							fontTitle.setText(context.getString(R.string.editor_font));
							final BiggroupButton2 Font = new BiggroupButton2(context);
							TextView animTitle = new TextView(context);
							animTitle.setText(context.getString(R.string.editor_animation));
							final BiggroupButton2 Animation = new BiggroupButton2(context);
							TextView heightTitle = new TextView(context);
							heightTitle.setText(context.getString(R.string.editor_height));
							final BiggroupButton2 Height = new BiggroupButton2(context);
							TextView marginsTitle = new TextView(context);
							marginsTitle.setText(context.getString(R.string.editor_margins));
							final BiggroupButton2 Margins = new BiggroupButton2(context);
							TextView otherTitle = new TextView(context);
							otherTitle.setText(context.getString(R.string.editor_other));
							final BiggroupButton2 Above = new BiggroupButton2(context);
							final BiggroupButton2 Below = new BiggroupButton2(context);
							final BiggroupButton2 Copy = new BiggroupButton2(context);
							Copy.setText(context.getString(R.string.editor_copy));
							final BiggroupButton2 Delete = new BiggroupButton2(context);
							Delete.setText(context.getString(R.string.editor_delete));
							Above.setText(context.getString(R.string.editor_add_above));
							Below.setText(context.getString(R.string.editor_add_below));
							dialog_Content.addView(textTitle);
							dialog_Content.addView(Text);
							dialog_Content.addView(textSizeTitle);
							dialog_Content.addView(TextSize);
							dialog_Content.addView(textColorTitle);
							dialog_Content.addView(TextColor);
							dialog_Content.addView(selectAbleTitle);
							dialog_Content.addView(Selectable);
							dialog_Content.addView(gravityTitle);
							dialog_Content.addView(Gravity);
							dialog_Content.addView(fontTitle);
							dialog_Content.addView(Font);
							dialog_Content.addView(animTitle);
							dialog_Content.addView(Animation);
							dialog_Content.addView(heightTitle);
							dialog_Content.addView(Height);
							dialog_Content.addView(marginsTitle);
							dialog_Content.addView(Margins);
							dialog_Content.addView(otherTitle);
							dialog_Content.addView(Above);
							dialog_Content.addView(Below);
							dialog_Content.addView(Copy);
							dialog_Content.addView(Delete);
							TextColor.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									new ColorPickerDialog(context, Color.parseColor(textColor), "Nothing",
											new OnColorSelectedListener() {
												public void onColorSelected(int color) {
													String c = String.format("#%08X", color);
													String str = Info.replace(StextInfo,
															"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor" + c
																	+ "<=>TextSize" + textSize + "<=>Gravity" + gravity
																	+ "<=>Font" + font + "<=>Animation" + animation
																	+ "<=>Height" + height + "<=>TopMargin" + topMargin
																	+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																	+ rightMargin + "<=>BottomMargin" + bottomMargin
																	+ "<=>Selectable" + selectAble + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
													dialog.dismiss();
												}
											}).show();
								}
							});
							TextSize.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text_size)+context.getString(R.string.a_split) + TextSize.getText().toString());

									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									try {
										final BiggroupSeekbar sizeSeekbar = new BiggroupSeekbar(context);
										dialog_Content.addView(sizeSeekbar);
										sizeSeekbar.setMax(100);
										sizeSeekbar.setProgress(Integer.parseInt(textSize));
										dialog_Cancel.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
											}
										});
										dialog_Save.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												String ts = String.valueOf(sizeSeekbar.getProgress());
												String str = Info.replace(StextInfo,
														"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor"
																+ textColor + "<=>TextSize" + ts + "<=>Gravity"
																+ gravity + "<=>Font" + font + "<=>Animation"
																+ animation + "<=>Height" + height + "<=>TopMargin"
																+ topMargin + "<=>LeftMargin" + leftMargin
																+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																+ bottomMargin + "<=>Selectable" + selectAble + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											}
										});

									} catch (Exception e) {
										e.printStackTrace();
									}
									dialog.show();
								}
							});
							Selectable.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text_is_selectable)+context.getString(R.string.a_split) + Selectable.getText().toString());

									final BiggroupButton2 True = new BiggroupButton2(context);
									final BiggroupButton2 False = new BiggroupButton2(context);
									True.setText(context.getString(R.string.a_on));
									False.setText(context.getString(R.string.a_off));
									dialog_Content.addView(True);
									dialog_Content.addView(False);
									True.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Select = "True";
											String str = Info.replace(StextInfo,
													"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor" + textColor
															+ "<=>TextSize" + textSize + "<=>Gravity" + gravity
															+ "<=>Font" + font + "<=>Animation" + animation
															+ "<=>Height" + height + "<=>TopMargin" + topMargin
															+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
															+ rightMargin + "<=>BottomMargin" + bottomMargin
															+ "<=>Selectable" + Select + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									False.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Select = "False";
											String str = Info.replace(StextInfo,
													"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor" + textColor
															+ "<=>TextSize" + textSize + "<=>Gravity" + gravity
															+ "<=>Font" + font + "<=>Animation" + animation
															+ "<=>Height" + height + "<=>TopMargin" + topMargin
															+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
															+ rightMargin + "<=>BottomMargin" + bottomMargin
															+ "<=>Selectable" + Select + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Gravity.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text_gravity)+context.getString(R.string.a_split) + Gravity.getText().toString());

									final BiggroupButton2 auto = new BiggroupButton2(context);
									final BiggroupButton2 left = new BiggroupButton2(context);
									final BiggroupButton2 center = new BiggroupButton2(context);
									final BiggroupButton2 right = new BiggroupButton2(context);
									auto.setText(context.getString(R.string.a_auto));
									right.setText(context.getString(R.string.editor_right));
									center.setText(context.getString(R.string.editor_center));
									left.setText(context.getString(R.string.editor_left));
									dialog_Content.addView(auto);
									dialog_Content.addView(right);
									dialog_Content.addView(center);
									dialog_Content.addView(left);
									auto.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Grav = "Auto";
											String str = Info.replace(StextInfo,
													"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor" + textColor
															+ "<=>TextSize" + textSize + "<=>Gravity" + Grav + "<=>Font"
															+ font + "<=>Animation" + animation + "<=>Height" + height
															+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>Selectable" + selectAble + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									center.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Grav = "Center";
											String str = Info.replace(StextInfo,
													"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor" + textColor
															+ "<=>TextSize" + textSize + "<=>Gravity" + Grav + "<=>Font"
															+ font + "<=>Animation" + animation + "<=>Height" + height
															+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>Selectable" + selectAble + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									left.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Gravity = "Left";
											String str = Info.replace(StextInfo, StextInfo);
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									right.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Grav = "Right";
											String str = Info.replace(StextInfo,
													"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor" + textColor
															+ "<=>TextSize" + textSize + "<=>Gravity" + Grav + "<=>Font"
															+ font + "<=>Animation" + animation + "<=>Height" + height
															+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>Selectable" + selectAble + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Font.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_font)+context.getString(R.string.a_split) + Font.getText().toString();
									new FontPickerDialog(context, title, new OnFontSelectedListener() {
										public void onFontSelected(String fileName, String fileId) {
											if (fileId.equals("none")) {
												FontForChange = "none";
												String str = Info.replace(StextInfo,
														"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor"
																+ textColor + "<=>TextSize" + textSize + "<=>Gravity"
																+ gravity + "<=>Font" + FontForChange + "<=>Animation"
																+ animation + "<=>Height" + height + "<=>TopMargin"
																+ topMargin + "<=>LeftMargin" + leftMargin
																+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																+ bottomMargin + "<=>Selectable" + selectAble + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												FontForChange = "";
												Refresh(data, contentlinear);
											} else {
												FontForChange = fileId;
												String str = Info.replace(StextInfo,
														"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor"
																+ textColor + "<=>TextSize" + textSize + "<=>Gravity"
																+ gravity + "<=>Font" + FontForChange + "<=>Animation"
																+ animation + "<=>Height" + height + "<=>TopMargin"
																+ topMargin + "<=>LeftMargin" + leftMargin
																+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																+ bottomMargin + "<=>Selectable" + selectAble + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												FontForChange = "";
												Refresh(data, contentlinear);
											}
										}
									}).show();
								}
							});
							Text.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text)+context.getString(R.string.a_split) + Stextview.getText().toString());

									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									final EditText textText = new EditText(context);
									textText.setHint(context.getString(R.string.editor_text));
									textText.setText(text);
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

											String str = Info.replace(StextInfo,
													"{SText<=>" + itemId + "<=>Text" + textText.getText().toString()
															+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
															+ "<=>Gravity" + gravity + "<=>Font" + font + "<=>Animation"
															+ animation + "<=>Height" + height + "<=>TopMargin"
															+ topMargin + "<=>LeftMargin" + leftMargin
															+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
															+ bottomMargin + "<=>Selectable" + selectAble + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});

							Animation.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_animation)+context.getString(R.string.a_split) + Animation.getText().toString();
									new AnimationPickerDialog(context, title, new OnAnimSelectedListener() {
										public void onAnimSelected(String name, String anim) {
											String str = Info.replace(StextInfo,
													"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor" + textColor
															+ "<=>TextSize" + textSize + "<=>Gravity" + gravity
															+ "<=>Font" + font + "<=>Animation" + anim + "<=>Height"
															+ height + "<=>TopMargin" + topMargin + "<=>LeftMargin"
															+ leftMargin + "<=>RightMargin" + rightMargin
															+ "<=>BottomMargin" + bottomMargin + "<=>Selectable"
															+ selectAble + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
										}
									}).show();
								}
							});
							Height.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									HeightForChange = height;
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_height)+context.getString(R.string.a_split) + Height.getText().toString());

									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									final TextView aotuText = new TextView(context);
									aotuText.setText(context.getString(R.string.a_auto));
									final TextView sizeText = new TextView(context);
									sizeText.setText(context.getString(R.string.a_size));
									final BiggroupSeekbar Seekbar = new BiggroupSeekbar(context);
									MaterialSwitch toggle = new MaterialSwitch(context);
                                    toggle.setBackgroundResource(R.drawable.row_bg);
									dialog_Content.addView(aotuText);
									dialog_Content.addView(toggle);
									dialog_Content.addView(sizeText);
									dialog_Content.addView(Seekbar);
									Seekbar.setMax(100);
									toggle.setText(context.getString(R.string.editor_auto_height));
									toggle.setGravity(17);
									toggle.setTextOff(context.getString(R.string.a_off));
									toggle.setTextOn(context.getString(R.string.a_on));
									if (height.startsWith("WRAP")) {
										toggle.setChecked(true);
										sizeText.setEnabled(false);
										Seekbar.setEnabled(false);
										Text.setText("10");
										Seekbar.setProgress(50);
									} else {
										toggle.setChecked(false);
										sizeText.setEnabled(true);
										Seekbar.setEnabled(true);
										Text.setText(height.replace("Size", ""));
										Seekbar.setProgress(Integer.parseInt(height.replace("Size", "")));
									}
									toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
										public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
											if (isChecked) {
												// در حالت روشن است
												sizeText.setEnabled(false);
												Text.setEnabled(false);
												Seekbar.setEnabled(false);
												HeightForChange = "WRAP_CONTENT";
											} else {
												// در حالت خاموش است
												sizeText.setEnabled(true);
												Text.setEnabled(true);
												Seekbar.setEnabled(true);
												HeightForChange = "Size" + String.valueOf(Seekbar.getProgress());
											}
										}
									});
									Seekbar.setOnSeekBarChangeListener(new BiggroupSeekbar.OnSeekBarChangeListener() {
										@Override
										public void OnProgressChanged(BiggroupSeekbar biggroupSeekBar, int i,
												boolean z) {
										}

										@Override
										public void onStartTrackingTouch(BiggroupSeekbar biggroupSeekBar) {
										}

										@Override
										public void onStopTrackingTouch(BiggroupSeekbar seekBar) {
											if (seekBar.getProgress() < 2) {
												seekBar.setProgress(2);
											}
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
											if (HeightForChange.startsWith("WRAP")) {
												HeightForChange = "WRAP_CONTENT";
												String str = Info.replace(StextInfo, "{SText<=>" + itemId + "<=>Text"
														+ text + "<=>TextColor" + textColor + "<=>TextSize" + textSize
														+ "<=>Gravity" + gravity + "<=>Font" + font + "<=>Animation"
														+ animation + "<=>Height" + HeightForChange + "<=>TopMargin"
														+ topMargin + "<=>LeftMargin" + leftMargin + "<=>RightMargin"
														+ rightMargin + "<=>BottomMargin" + bottomMargin
														+ "<=>Selectable" + selectAble + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											} else {
												HeightForChange = "Size" + String.valueOf(Seekbar.getProgress());
												String str = Info.replace(StextInfo, "{SText<=>" + itemId + "<=>Text"
														+ text + "<=>TextColor" + textColor + "<=>TextSize" + textSize
														+ "<=>Gravity" + gravity + "<=>Font" + font + "<=>Animation"
														+ animation + "<=>Height" + HeightForChange + "<=>TopMargin"
														+ topMargin + "<=>LeftMargin" + leftMargin + "<=>RightMargin"
														+ rightMargin + "<=>BottomMargin" + bottomMargin
														+ "<=>Selectable" + selectAble + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											}
											HeightForChange = "";

										}
									});
									dialog.show();
								}
							});
							Margins.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_margins);
									new MarginPickerDialog(context, title, topMargin, bottomMargin, rightMargin,
											leftMargin, new OnDipSelectedListener() {
												@Override
												public void onDipSelected(int topMargin, int bottomMargin,
														int rightMargin, int leftMargin) {
													// TODO: Implement this method
													String str = Info.replace(StextInfo,
															"{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor"
																	+ textColor + "<=>TextSize" + textSize
																	+ "<=>Gravity" + gravity + "<=>Font" + font
																	+ "<=>Animation" + animation + "<=>Height" + height
																	+ "<=>TopMargin" + topMargin + "<=>LeftMargin"
																	+ leftMargin + "<=>RightMargin" + rightMargin
																	+ "<=>BottomMargin" + bottomMargin + "<=>Selectable"
																	+ selectAble + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
												}
											}).show();
								}
							});
							Above.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									ID = getSomeInt();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(Above.getText().toString());
									BiggroupButton2 STextView = new BiggroupButton2(context);
									STextView.setText(context.getString(R.string.editor_simple_text));
									BiggroupButton2 ATextView = new BiggroupButton2(context);
									ATextView.setText(context.getString(R.string.editor_advanced_text));
									BiggroupButton2 Button = new BiggroupButton2(context);
									Button.setText(context.getString(R.string.editor_button));
									BiggroupButton2 ImageView = new BiggroupButton2(context);
									ImageView.setText(context.getString(R.string.editor_pic));
									dialog_Content.addView(STextView);
									dialog_Content.addView(ATextView);
									dialog_Content.addView(Button);
									dialog_Content.addView(ImageView);
									STextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(StextInfo,
													"{SText<=>" + ID + newSText + StextInfo);
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});
									ATextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(StextInfo,
													"{AText<=>" + ID + newAText + StextInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									Button.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(StextInfo,
													"{Button<=>" + ID + newButton + StextInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ImageView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(StextInfo,
													"{Image<=>" + ID + newImage + StextInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});

									dialog.show();
								}
							});
							Below.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									ID = getSomeInt();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(Below.getText().toString());

									BiggroupButton2 STextView = new BiggroupButton2(context);
									STextView.setText(context.getString(R.string.editor_simple_text));
									BiggroupButton2 ATextView = new BiggroupButton2(context);
									ATextView.setText(context.getString(R.string.editor_advanced_text));
									BiggroupButton2 Button = new BiggroupButton2(context);
									Button.setText(context.getString(R.string.editor_button));
									BiggroupButton2 ImageView = new BiggroupButton2(context);
									ImageView.setText(context.getString(R.string.editor_pic));
									dialog_Content.addView(STextView);
									dialog_Content.addView(ATextView);
									dialog_Content.addView(Button);
									dialog_Content.addView(ImageView);
									STextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(StextInfo,
													StextInfo + "{SText<=>" + ID + newSText);
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ATextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(StextInfo,
													StextInfo + "{AText<=>" + ID + newAText);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									Button.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(StextInfo,
													StextInfo + "{Button<=>" + ID + newButton);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ImageView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(StextInfo,
													StextInfo + "{Image<=>" + ID + newImage);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}

									});

									dialog.show();
								}
							});
							Copy.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String ID = getSomeInt();
									String StextInfo = "{SText<=>" + ID + "<=>Text" + text + "<=>TextColor" + textColor
											+ "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font" + font
											+ "<=>Animation" + animation + "<=>Height" + height + "<=>TopMargin"
											+ topMargin + "<=>LeftMargin" + leftMargin + "<=>RightMargin" + rightMargin
											+ "<=>BottomMargin" + bottomMargin + "<=>Selectable" + selectAble + "}!#";
									String str = Info + StextInfo;
									if (Info.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(data, str);
									Refresh(data, contentlinear);
									dialog.dismiss();
								}
							});
							Delete.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String str = Info.replace(StextInfo, "");
									if (Info.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(data, str);
									Refresh(data, contentlinear);
									dialog.dismiss();
								}
							});
							Text.setText(text);
							TextSize.setText(textSize + "%");
							if (gravity.startsWith("Right")) {
								Gravity.setText(context.getString(R.string.editor_right));
							} else if (gravity.startsWith("Center")) {
								Gravity.setText(context.getString(R.string.editor_center));
							} else if (gravity.startsWith("Left")) {
								Gravity.setText(context.getString(R.string.editor_left));
							} else {
								Gravity.setText(context.getString(R.string.a_auto));
							}
							if (picker.getFontNameById(font).equals("none")) {
								Font.setText(context.getString(R.string.a_default));
							} else {
								Typeface tf = Typeface.createFromFile(rootPath + fontsPath + font);
								Font.setTypeface(tf);
								Font.setText(picker.getFontNameById(font));
							}
							TextColor.getLeftIcon().setBackgroundColor(Color.parseColor(textColor));
							TextColor.setText(context.getString(R.string.editor_text_color));
							if (selectAble.equals("True")) {
								Selectable.setText(context.getString(R.string.a_on));
							} else {
								Selectable.setText(context.getString(R.string.a_off));
							}
							if (animation.equals("left_in")) {
								Animation.setText(context.getString(R.string.editor_anim_left_in));
							} else if (animation.equals("right_in")) {
								Animation.setText(context.getString(R.string.editor_anim_right_in));
							} else if (animation.equals("fade_in")) {
								Animation.setText(context.getString(R.string.editor_anim_fade_in));
							} else if (animation.equals("slide_down")) {
								Animation.setText(context.getString(R.string.editor_anim_slide_down));
							} else if (animation.equals("bounce")) {
								Animation.setText(context.getString(R.string.editor_anim_bounce));
							} else if (animation.equals("blink")) {
								Animation.setText(context.getString(R.string.editor_anim_blinking));
							} else if (animation.equals("rotation")) {
								Animation.setText(context.getString(R.string.editor_anim_rotation));
							} else if (animation.equals("shake")) {
								Animation.setText(context.getString(R.string.editor_anim_shake));
							} else {
								Animation.setText(context.getString(R.string.editor_no_animation));
							}
							if (height.startsWith("WRAP")) {
								Height.setText(context.getString(R.string.a_auto));
							} else {
								Height.setText(height.replace("Size", "") + "%");
							}
							Margins.setText(context.getString(R.string.editor_top_margin)+context.getString(R.string.a_split) + topMargin + "%" + "\n" + context.getString(R.string.editor_bottom_margin)+context.getString(R.string.a_split) + bottomMargin + "%" + "\n"
									+ context.getString(R.string.editor_right_margin)+context.getString(R.string.a_split) + rightMargin + "%" + "\n" + context.getString(R.string.editor_left_margin)+context.getString(R.string.a_split) + leftMargin + "%");
							dialog.show();
						}
					});
				} else if (part2.contains("{AText<=>")) {
					String[] code = part2.replace("}", "").split("<=>");
					Atextview = new BiggroupTextView2(context);
					final String itemId = code[1];
					final String background = code[2].replace("Background", "");
					final String text = code[3].replaceFirst("Text", "").replace("@*&$split=<=>!?$?", "<=>")
							.replace("{split}={*?}%@$*?", "!#");
					final String textColor = code[4].replace("TextColor", "");
					final String textSize = code[5].replace("TextSize", "");
					final String gravity = code[6].replace("Gravity", "");
					final String font = code[7].replace("Font", "");
					final String Radios = code[8].replace("Radius", "");
					final String animation = code[9].replace("Animation", "");
					final String height = code[10].replace("Height", "");
					final String selectAble = code[15].replace("Selectable", "");
					final String shareAble = code[16].replace("Shareable", "");
					final String onclick = code[17].replace("OnClick", "");
					if (background.startsWith("Color")) {
						String str = background.replace("Color", "");
						Atextview.setBackgroundColor(Color.parseColor(str));
					} else if (background.startsWith("Image")) {
						String str = background.replace("Image", "");
						if (new File(rootPath + imagesPath + str).exists()) {
							FileInputStream In = new FileInputStream(rootPath + imagesPath + str);
							InputStream inputStream = In;
							Atextview.setBackgroundImage(Drawable.createFromStream(inputStream, ""));
						}
					}
                    if (selectAble.equals("True")) {
						Atextview.setTextIsSelectable(true);
                       // Atextview.setAutoLink();
					} else {
						Atextview.setTextIsSelectable(false);
					}
					if (gravity.startsWith("Right")) {
						Atextview.setTextGravity(5);
					} else if (gravity.startsWith("Center")) {
						Atextview.setTextGravity(17);
					} else if (gravity.startsWith("Left")) {
						Atextview.setTextGravity(3);
					}
					if (font.startsWith("font")) {
						if (new File(rootPath + fontsPath + font).exists()) {
							Typeface tf = Typeface.createFromFile(rootPath + fontsPath + font);
							Atextview.setTypeface(tf);
						}
					}
					Atextview.setTextColor(Color.parseColor(textColor));
					int TextSize = Integer.parseInt(textSize);
					Atextview.setTextSize(TextSize);
					Atextview.setText(Html.fromHtml(text));
					contentlinear.addView(Atextview);
					if (shareAble.equals("True")) {
						Atextview.setTextIsShareable(true);
					//	Atextview.setText(Html.fromHtml(text));
					}
					lParams = (LinearLayout.LayoutParams) Atextview.getLayoutParams();
					if (height.startsWith("WRAP_CONTENT")) {
						lParams.height = lParams.WRAP_CONTENT;
					} else if (height.startsWith("Size")) {
						String str = height.replace("Size", "");
						int Height = Integer.parseInt(str)*20;
						lParams.height = Height;
					}
					lParams.width = lParams.MATCH_PARENT;
					final int topMargin = Integer.parseInt(code[11].replace("TopMargin", ""));
					final int leftMargin = Integer.parseInt(code[12].replace("LeftMargin", ""));
					final int rightMargin = Integer.parseInt(code[13].replace("RightMargin", ""));
					final int bottomMargin = Integer.parseInt(code[14].replace("BottomMargin", ""));
					lParams.topMargin = topMargin*12;
					lParams.leftMargin = leftMargin*6;
					lParams.rightMargin = rightMargin*6;
					lParams.bottomMargin = bottomMargin*12;
					Atextview.setLayoutParams(lParams);
					Atextview.setCRadios(Integer.parseInt(Radios)*5);
					if (animation.equals("left_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_in);
						Atextview.startAnimation(anim);
					} else if (animation.equals("right_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
						Atextview.startAnimation(anim);
					} else if (animation.equals("bounce")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.bouncing);
						Atextview.startAnimation(anim);
					} else if (animation.equals("slide_down")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
						Atextview.startAnimation(anim);
					} else if (animation.equals("fade_in")) {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
						Atextview.startAnimation(anim);
					}
					final String AtextInfo = "{AText<=>" + itemId + "<=>Background" + background + "<=>Text" + text
							+ "<=>TextColor" + textColor + "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font"
							+ font + "<=>Radius" + Radios + "<=>Animation" + animation + "<=>Height" + height
							+ "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin + "<=>RightMargin" + rightMargin
							+ "<=>BottomMargin" + bottomMargin + "<=>Selectable" + selectAble + "<=>Shareable"
							+ shareAble + "<=>OnClick" + onclick + "}!#";
					Atextview.getATextView().setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							final EditorDialog dialog = new EditorDialog(context);
							TextView dialog_Title = dialog.getTitle();
							LinearLayout dialog_Content = dialog.getContent();
							dialog_Title.setText(context.getString(R.string.editor_advanced_text)+context.getString(R.string.a_split)+text);

							TextView textTitle = new TextView(context);
							textTitle.setText(context.getString(R.string.editor_text));
							final BiggroupButton2 Text = new BiggroupButton2(context);
							TextView backgroundTitle = new TextView(context);
							backgroundTitle.setText(context.getString(R.string.editor_background));
							final BiggroupButton2 Background = new BiggroupButton2(context);
							TextView textSizeTitle = new TextView(context);
							textSizeTitle.setText(context.getString(R.string.editor_text_size));
							final BiggroupButton2 TextSize = new BiggroupButton2(context);
							TextView textColorTitle = new TextView(context);
							textColorTitle.setText(context.getString(R.string.editor_text_color));
							final BiggroupButton2 TextColor = new BiggroupButton2(context);
							TextView gravityTitle = new TextView(context);
							gravityTitle.setText(context.getString(R.string.editor_text_gravity));
							final BiggroupButton2 Gravity = new BiggroupButton2(context);
							TextView fontTitle = new TextView(context);
							fontTitle.setText(context.getString(R.string.editor_font));
							final BiggroupButton2 Font = new BiggroupButton2(context);
							TextView radiusTitle = new TextView(context);
							radiusTitle.setText(context.getString(R.string.editor_radius));
							final BiggroupButton2 Radius = new BiggroupButton2(context);
							TextView animTitle = new TextView(context);
							animTitle.setText(context.getString(R.string.editor_animation));
							final BiggroupButton2 Anim = new BiggroupButton2(context);
							TextView heightTitle = new TextView(context);
							heightTitle.setText(context.getString(R.string.editor_height));
							final BiggroupButton2 Height = new BiggroupButton2(context);
							TextView marginsTitle = new TextView(context);
							marginsTitle.setText(context.getString(R.string.editor_margins));
							final BiggroupButton2 Margins = new BiggroupButton2(context);
							Margins.setText(context.getString(R.string.editor_margins));
							TextView selectAbleTitle = new TextView(context);
							selectAbleTitle.setText(context.getString(R.string.editor_text_is_selectable));
							final BiggroupButton2 Selectable = new BiggroupButton2(context);
							TextView shareAbleTitle = new TextView(context);
							shareAbleTitle.setText(context.getString(R.string.editor_text_is_shareable));
							final BiggroupButton2 Shareable = new BiggroupButton2(context);
							final TextView onClickTitle = new TextView(context);
							onClickTitle.setText(context.getString(R.string.editor_action));
							final BiggroupButton2 OnClick = new BiggroupButton2(context);
							TextView otherTitle = new TextView(context);
							otherTitle.setText(context.getString(R.string.editor_other));
							final BiggroupButton2 Above = new BiggroupButton2(context);
							final BiggroupButton2 Below = new BiggroupButton2(context);
							Above.setText(context.getString(R.string.editor_add_above));
							Below.setText(context.getString(R.string.editor_add_below));
							final BiggroupButton2 Copy = new BiggroupButton2(context);
							Copy.setText(context.getString(R.string.editor_copy));
							final BiggroupButton2 Delete = new BiggroupButton2(context);
							Delete.setText(context.getString(R.string.editor_delete));
							dialog_Content.addView(textTitle);
							dialog_Content.addView(Text);
							dialog_Content.addView(backgroundTitle);
							dialog_Content.addView(Background);
							dialog_Content.addView(textSizeTitle);
							dialog_Content.addView(TextSize);
							dialog_Content.addView(textColorTitle);
							dialog_Content.addView(TextColor);
							dialog_Content.addView(gravityTitle);
							dialog_Content.addView(Gravity);
							dialog_Content.addView(fontTitle);
							dialog_Content.addView(Font);
							dialog_Content.addView(selectAbleTitle);
							dialog_Content.addView(Selectable);
							dialog_Content.addView(shareAbleTitle);
							dialog_Content.addView(Shareable);
							dialog_Content.addView(radiusTitle);
							dialog_Content.addView(Radius);
							dialog_Content.addView(animTitle);
							dialog_Content.addView(Anim);
							dialog_Content.addView(heightTitle);
							dialog_Content.addView(Height);
							dialog_Content.addView(marginsTitle);
							dialog_Content.addView(Margins);
							dialog_Content.addView(onClickTitle);
							dialog_Content.addView(OnClick);
							dialog_Content.addView(otherTitle);
							dialog_Content.addView(Above);
							dialog_Content.addView(Below);
							dialog_Content.addView(Copy);
							dialog_Content.addView(Delete);
							Text.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text)+context.getString(R.string.a_split) + text);
									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									final EditText textText = new EditText(context);
									textText.setHint(context.getString(R.string.editor_text));
									textText.setText(text);
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
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + textText.getText().toString()
													+ "<=>TextColor" + textColor + "<=>TextSize" + textSize
													+ "<=>Gravity" + gravity + "<=>Font" + font + "<=>Radius" + Radios
													+ "<=>Animation" + animation + "<=>Height" + height + "<=>TopMargin"
													+ topMargin + "<=>LeftMargin" + leftMargin + "<=>RightMargin"
													+ rightMargin + "<=>BottomMargin" + bottomMargin + "<=>Selectable"
													+ selectAble + "<=>Shareable" + shareAble + "<=>OnClick" + onclick
													+ "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							TextColor.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									new ColorPickerDialog(context, Color.parseColor(textColor), "Nothing",
											new OnColorSelectedListener() {
												public void onColorSelected(int color) {
													String Color = String.format("#%08X", color);
													String str = Info.replace(AtextInfo,
															"{AText<=>" + itemId + "<=>Background" + background
																	+ "<=>Text" + text + "<=>TextColor" + Color
																	+ "<=>TextSize" + textSize + "<=>Gravity" + gravity
																	+ "<=>Font" + font + "<=>Radius" + Radios
																	+ "<=>Animation" + animation + "<=>Height" + height
																	+ "<=>TopMargin" + topMargin + "<=>LeftMargin"
																	+ leftMargin + "<=>RightMargin" + rightMargin
																	+ "<=>BottomMargin" + bottomMargin + "<=>Selectable"
																	+ selectAble + "<=>Shareable" + shareAble
																	+ "<=>OnClick" + onclick + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
												}
											}).show();
								}
							});
							TextSize.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text_size)+context.getString(R.string.a_split) + TextSize.getText().toString());

									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									try {
										final BiggroupSeekbar sizeSeekbar = new BiggroupSeekbar(context);
										dialog_Content.addView(sizeSeekbar);
										sizeSeekbar.setMax(100);
										sizeSeekbar.setProgress(Integer.parseInt(textSize));
										dialog_Cancel.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
											}
										});
										dialog_Save.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												String ts = String.valueOf(sizeSeekbar.getProgress());
												String str = Info.replace(AtextInfo,
														"{AText<=>" + itemId + "<=>Background" + background + "<=>Text"
																+ text + "<=>TextColor" + textColor + "<=>TextSize" + ts
																+ "<=>Gravity" + gravity + "<=>Font" + font
																+ "<=>Radius" + Radios + "<=>Animation" + animation
																+ "<=>Height" + height + "<=>TopMargin" + topMargin
																+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																+ rightMargin + "<=>BottomMargin" + bottomMargin
																+ "<=>Selectable" + selectAble + "<=>Shareable"
																+ shareAble + "<=>OnClick" + onclick + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											}
										});

									} catch (Exception e) {
										e.printStackTrace();
									}
									dialog.show();
								}
							});
							Gravity.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text_gravity)+context.getString(R.string.a_split) + Gravity.getText().toString());

									final BiggroupButton2 auto = new BiggroupButton2(context);
									final BiggroupButton2 left = new BiggroupButton2(context);
									final BiggroupButton2 center = new BiggroupButton2(context);
									final BiggroupButton2 right = new BiggroupButton2(context);
									auto.setText(context.getString(R.string.a_auto));
									right.setText(context.getString(R.string.editor_right));
									center.setText(context.getString(R.string.editor_center));
									left.setText(context.getString(R.string.editor_left));
									dialog_Content.addView(auto);
									dialog_Content.addView(right);
									dialog_Content.addView(center);
									dialog_Content.addView(left);
									auto.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Grav = "Auto";
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + Grav + "<=>Font" + font
													+ "<=>Radius" + Radios + "<=>Animation" + animation + "<=>Height"
													+ height + "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
													+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin
													+ "<=>Selectable" + selectAble + "<=>Shareable" + shareAble
													+ "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									center.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Grav = "Center";
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + Grav + "<=>Font" + font
													+ "<=>Radius" + Radios + "<=>Animation" + animation + "<=>Height"
													+ height + "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
													+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin
													+ "<=>Selectable" + selectAble + "<=>Shareable" + shareAble
													+ "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									left.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Grav = "Left";
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + Grav + "<=>Font" + font
													+ "<=>Radius" + Radios + "<=>Animation" + animation + "<=>Height"
													+ height + "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
													+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin
													+ "<=>Selectable" + selectAble + "<=>Shareable" + shareAble
													+ "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									right.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Grav = "Right";
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + Grav + "<=>Font" + font
													+ "<=>Radius" + Radios + "<=>Animation" + animation + "<=>Height"
													+ height + "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
													+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin
													+ "<=>Selectable" + selectAble + "<=>Shareable" + shareAble
													+ "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Font.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_font)+context.getString(R.string.a_split) + Font.getText().toString();
									new FontPickerDialog(context, title, new OnFontSelectedListener() {
										public void onFontSelected(String fileName, String fileId) {
											if (fileId.equals("none")) {
												FontForChange = "none";
												String str = Info.replace(AtextInfo,
														"{AText<=>" + itemId + "<=>Background" + background + "<=>Text"
																+ text + "<=>TextColor" + textColor + "<=>TextSize"
																+ textSize + "<=>Gravity" + gravity + "<=>Font"
																+ FontForChange + "<=>Radius" + Radios + "<=>Animation"
																+ animation + "<=>Height" + height + "<=>TopMargin"
																+ topMargin + "<=>LeftMargin" + leftMargin
																+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																+ bottomMargin + "<=>Selectable" + selectAble
																+ "<=>Shareable" + shareAble + "<=>OnClick" + onclick
																+ "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												FontForChange = "";
											} else {
												FontForChange = fileId;
												String str = Info.replace(AtextInfo,
														"{AText<=>" + itemId + "<=>Background" + background + "<=>Text"
																+ text + "<=>TextColor" + textColor + "<=>TextSize"
																+ textSize + "<=>Gravity" + gravity + "<=>Font"
																+ FontForChange + "<=>Radius" + Radios + "<=>Animation"
																+ animation + "<=>Height" + height + "<=>TopMargin"
																+ topMargin + "<=>LeftMargin" + leftMargin
																+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																+ bottomMargin + "<=>Selectable" + selectAble
																+ "<=>Shareable" + shareAble + "<=>OnClick" + onclick
																+ "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												FontForChange = "";
											}
										}
									}).show();
								}
							});
							Selectable.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text_is_selectable)+context.getString(R.string.a_split) + Selectable.getText().toString());

									final BiggroupButton2 True = new BiggroupButton2(context);
									final BiggroupButton2 False = new BiggroupButton2(context);
									True.setText(context.getString(R.string.a_on));
									False.setText(context.getString(R.string.a_off));
									dialog_Content.addView(True);
									dialog_Content.addView(False);
									True.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Select = "True";
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font"
													+ font + "<=>Radius" + Radios + "<=>Animation" + animation
													+ "<=>Height" + height + "<=>TopMargin" + topMargin
													+ "<=>LeftMargin" + leftMargin + "<=>RightMargin" + rightMargin
													+ "<=>BottomMargin" + bottomMargin + "<=>Selectable" + Select
													+ "<=>Shareable" + shareAble + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									False.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Select = "False";
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font"
													+ font + "<=>Radius" + Radios + "<=>Animation" + animation
													+ "<=>Height" + height + "<=>TopMargin" + topMargin
													+ "<=>LeftMargin" + leftMargin + "<=>RightMargin" + rightMargin
													+ "<=>BottomMargin" + bottomMargin + "<=>Selectable" + Select
													+ "<=>Shareable" + shareAble + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Shareable.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_text_is_shareable)+context.getString(R.string.a_split) + Shareable.getText().toString());
									final BiggroupButton2 True = new BiggroupButton2(context);
									final BiggroupButton2 False = new BiggroupButton2(context);
									True.setText(context.getString(R.string.a_on));
									False.setText(context.getString(R.string.a_off));
									dialog_Content.addView(True);
									dialog_Content.addView(False);
									True.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Share = "True";
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font"
													+ font + "<=>Radius" + Radios + "<=>Animation" + animation
													+ "<=>Height" + height + "<=>TopMargin" + topMargin
													+ "<=>LeftMargin" + leftMargin + "<=>RightMargin" + rightMargin
													+ "<=>BottomMargin" + bottomMargin + "<=>Selectable" + selectAble
													+ "<=>Shareable" + Share + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									False.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Share = "False";
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font"
													+ font + "<=>Radius" + Radios + "<=>Animation" + animation
													+ "<=>Height" + height + "<=>TopMargin" + topMargin
													+ "<=>LeftMargin" + leftMargin + "<=>RightMargin" + rightMargin
													+ "<=>BottomMargin" + bottomMargin + "<=>Selectable" + selectAble
													+ "<=>Shareable" + Share + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Radius.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_radius)+context.getString(R.string.a_split) + Radius.getText().toString());
									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);
									try {
										final BiggroupSeekbar sizeSeekbar = new BiggroupSeekbar(context);
										dialog_Content.addView(sizeSeekbar);
										sizeSeekbar.setMax(100);
										sizeSeekbar.setProgress(Integer.parseInt(Radios));
										dialog_Cancel.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
											}
										});
										dialog_Save.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												String rad = String.valueOf(sizeSeekbar.getProgress());
												String str = Info.replace(AtextInfo,
														"{AText<=>" + itemId + "<=>Background" + background + "<=>Text"
																+ text + "<=>TextColor" + textColor + "<=>TextSize"
																+ textSize + "<=>Gravity" + gravity + "<=>Font" + font
																+ "<=>Radius" + rad + "<=>Animation" + animation
																+ "<=>Height" + height + "<=>TopMargin" + topMargin
																+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
																+ rightMargin + "<=>BottomMargin" + bottomMargin
																+ "<=>Selectable" + selectAble + "<=>Shareable"
																+ shareAble + "<=>OnClick" + onclick + "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											}
										});
									} catch (Exception e) {
										e.printStackTrace();
									}
									dialog.show();
								}
							});
							Background.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_background)+context.getString(R.string.a_split) + Background.getText().toString());
									final BiggroupButton2 noneBtn = new BiggroupButton2(context);
									dialog_Content.addView(noneBtn);
									noneBtn.setText(context.getString(R.string.a_default));
									final BiggroupButton2 colorBtn = new BiggroupButton2(context);
									dialog_Content.addView(colorBtn);
									colorBtn.setText(context.getString(R.string.a_color));
									final BiggroupButton2 ImgBtn = new BiggroupButton2(context);
									dialog_Content.addView(ImgBtn);
									ImgBtn.setText(context.getString(R.string.editor_pic));
									final int oldColor;
									if (background.startsWith("Color")) {
										oldColor = Color.parseColor(background.replace("Color", ""));
									} else {
										oldColor = Color.WHITE;
									}
									noneBtn.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String Str = "none";
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ Str + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font"
													+ font + "<=>Radius" + Radios + "<=>Animation" + animation
													+ "<=>Height" + height + "<=>TopMargin" + topMargin
													+ "<=>LeftMargin" + leftMargin + "<=>RightMargin" + rightMargin
													+ "<=>BottomMargin" + bottomMargin + "<=>Selectable" + selectAble
													+ "<=>Shareable" + shareAble + "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									colorBtn.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
											new ColorPickerDialog(context, oldColor, "Nothing",
													new OnColorSelectedListener() {
														public void onColorSelected(int color) {
															String Color = String.format("#%08X", color);
															Color = "Color" + Color;
															String str = Info.replace(AtextInfo, "{AText<=>" + itemId
																	+ "<=>Background" + Color + "<=>Text" + text
																	+ "<=>TextColor" + textColor + "<=>TextSize"
																	+ textSize + "<=>Gravity" + gravity + "<=>Font"
																	+ font + "<=>Radius" + Radios + "<=>Animation"
																	+ animation + "<=>Height" + height + "<=>TopMargin"
																	+ topMargin + "<=>LeftMargin" + leftMargin
																	+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																	+ bottomMargin + "<=>Selectable" + selectAble
																	+ "<=>Shareable" + shareAble + "<=>OnClick"
																	+ onclick + "}!#");
															if (Info.startsWith("null")) {
																str = str.replace("null", "");
															}
															utils.writeFile(data, str);
															Refresh(data, contentlinear);
														}
													}).show();
										}
									});
									ImgBtn.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
											String title = context.getString(R.string.editor_background)+context.getString(R.string.a_split) + ImgBtn.getText().toString();
											new ImagePickerDialog(context, title, new OnImageSelectedListener() {
												public void onImageSelected(String fileName, String fileId) {
													String Str = "Image" + fileId;
													String str = Info.replace(AtextInfo,
															"{AText<=>" + itemId + "<=>Background" + Str + "<=>Text"
																	+ text + "<=>TextColor" + textColor + "<=>TextSize"
																	+ textSize + "<=>Gravity" + gravity + "<=>Font"
																	+ font + "<=>Radius" + Radios + "<=>Animation"
																	+ animation + "<=>Height" + height + "<=>TopMargin"
																	+ topMargin + "<=>LeftMargin" + leftMargin
																	+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																	+ bottomMargin + "<=>Selectable" + selectAble
																	+ "<=>Shareable" + shareAble + "<=>OnClick"
																	+ onclick + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
												}
											}).show();
										}
									});
									dialog.show();
								}
							});

							Anim.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_animation)+context.getString(R.string.a_split) + Anim.getText().toString();
									new AnimationPickerDialog(context, title, new OnAnimSelectedListener() {
										public void onAnimSelected(String name, String anim) {
											String str = Info.replace(AtextInfo, "{AText<=>" + itemId + "<=>Background"
													+ background + "<=>Text" + text + "<=>TextColor" + textColor
													+ "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font"
													+ font + "<=>Radius" + Radios + "<=>Animation" + anim + "<=>Height"
													+ height + "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
													+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin
													+ "<=>Selectable" + selectAble + "<=>Shareable" + shareAble
													+ "<=>OnClick" + onclick + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
										}
									}).show();
								}
							});
							Height.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									HeightForChange = height;
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(context.getString(R.string.editor_height)+context.getString(R.string.a_split) + Height.getText().toString());

									LinearLayout dialog_Bottom = (LinearLayout) dialog.findViewById(R.id.dialogBottom);
									TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
									TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
									dialog_Bottom.setVisibility(View.VISIBLE);

									final TextView aotuText = new TextView(context);
									aotuText.setText(context.getString(R.string.a_auto));
									final TextView sizeText = new TextView(context);
									sizeText.setText(context.getString(R.string.a_size));
									final BiggroupSeekbar Seekbar = new BiggroupSeekbar(context);
									MaterialSwitch toggle = new MaterialSwitch(context);
                                    toggle.setBackgroundResource(R.drawable.row_bg);
									dialog_Content.addView(aotuText);
									dialog_Content.addView(toggle);
									dialog_Content.addView(sizeText);
									dialog_Content.addView(Seekbar);
									Seekbar.setMax(100);
									toggle.setText(context.getString(R.string.editor_auto_height));
									toggle.setGravity(17);
									toggle.setTextOff(context.getString(R.string.a_off));
									toggle.setTextOn(context.getString(R.string.a_on));
									if (height.startsWith("WRAP")) {
										toggle.setChecked(true);
										sizeText.setEnabled(false);
										Seekbar.setEnabled(false);
										Seekbar.setProgress(50);
									} else {
										toggle.setChecked(false);
										sizeText.setEnabled(true);
										Seekbar.setEnabled(true);
										Seekbar.setProgress(Integer.parseInt(height.replace("Size", "")));
									}
									toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
										public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
											if (isChecked) {
												// در حالت روشن است
												sizeText.setEnabled(false);
												Text.setEnabled(false);
												Seekbar.setEnabled(false);
												HeightForChange = "WRAP_CONTENT";
											} else {
												// در حالت خاموش است
												sizeText.setEnabled(true);
												Text.setEnabled(true);
												Seekbar.setEnabled(true);
												HeightForChange = "Size" + String.valueOf(Seekbar.getProgress());
											}
										}
									});
									Seekbar.setOnSeekBarChangeListener(new BiggroupSeekbar.OnSeekBarChangeListener() {

										@Override
										public void OnProgressChanged(BiggroupSeekbar biggroupSeekBar, int i,
												boolean z) {
											// TODO: Implement this method
										}

										@Override
										public void onStartTrackingTouch(BiggroupSeekbar biggroupSeekBar) {
											// TODO: Implement this method
										}

										@Override
										public void onStopTrackingTouch(BiggroupSeekbar seekBar) {
											// TODO: Implement this method
											if (seekBar.getProgress() < 2) {
												seekBar.setProgress(2);
											}
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
											if (HeightForChange.startsWith("WRAP")) {
												String str = Info.replace(AtextInfo,
														"{AText<=>" + itemId + "<=>Background" + background + "<=>Text"
																+ text + "<=>TextColor" + textColor + "<=>TextSize"
																+ textSize + "<=>Gravity" + gravity + "<=>Font" + font
																+ "<=>Radius" + Radios + "<=>Animation" + animation
																+ "<=>Height" + HeightForChange + "<=>TopMargin"
																+ topMargin + "<=>LeftMargin" + leftMargin
																+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																+ bottomMargin + "<=>Selectable" + selectAble
																+ "<=>Shareable" + shareAble + "<=>OnClick" + onclick
																+ "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											} else {
												HeightForChange = "Size" + String.valueOf(Seekbar.getProgress());
												String str = Info.replace(AtextInfo,
														"{AText<=>" + itemId + "<=>Background" + background + "<=>Text"
																+ text + "<=>TextColor" + textColor + "<=>TextSize"
																+ textSize + "<=>Gravity" + gravity + "<=>Font" + font
																+ "<=>Radius" + Radios + "<=>Animation" + animation
																+ "<=>Height" + HeightForChange + "<=>TopMargin"
																+ topMargin + "<=>LeftMargin" + leftMargin
																+ "<=>RightMargin" + rightMargin + "<=>BottomMargin"
																+ bottomMargin + "<=>Selectable" + selectAble
																+ "<=>Shareable" + shareAble + "<=>OnClick" + onclick
																+ "}!#");
												if (Info.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(data, str);
												Refresh(data, contentlinear);
												dialog.dismiss();
											}
											HeightForChange = "";
										}
									});
									dialog.show();
								}
							});
							Margins.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_margins);
									new MarginPickerDialog(context, title, topMargin, bottomMargin, rightMargin,
											leftMargin, new OnDipSelectedListener() {
												@Override
												public void onDipSelected(int topMargin, int bottomMargin,
														int rightMargin, int leftMargin) {
													// TODO: Implement this method
													String str = Info.replace(AtextInfo,
															"{AText<=>" + itemId + "<=>Background" + background
																	+ "<=>Text" + text + "<=>TextColor" + textColor
																	+ "<=>TextSize" + textSize + "<=>Gravity" + gravity
																	+ "<=>Font" + font + "<=>Radius" + Radios
																	+ "<=>Animation" + animation + "<=>Height" + height
																	+ "<=>TopMargin" + topMargin + "<=>LeftMargin"
																	+ leftMargin + "<=>RightMargin" + rightMargin
																	+ "<=>BottomMargin" + bottomMargin + "<=>Selectable"
																	+ selectAble + "<=>Shareable" + shareAble
																	+ "<=>OnClick" + onclick + "}!#");
													if (Info.startsWith("null")) {
														str = str.replace("null", "");
													}
													utils.writeFile(data, str);
													Refresh(data, contentlinear);
												}
											}).show();
								}
							});
							OnClick.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									String title = context.getString(R.string.editor_action)+context.getString(R.string.a_split) + OnClick.getText().toString();
                                    new ActionPickerDialog(context, title, OnClickForChange, new OnActionSelectedListener() {
										public void onActionSelected(String action) {
											String str = Info.replace(AtextInfo,
													"{AText<=>" + itemId + "<=>Background" + background + "<=>Text"
															+ text + "<=>TextColor" + textColor + "<=>TextSize"
															+ textSize + "<=>Gravity" + gravity + "<=>Font" + font
															+ "<=>Radius" + Radios + "<=>Animation" + animation
															+ "<=>Height" + height + "<=>TopMargin" + topMargin
															+ "<=>LeftMargin" + leftMargin + "<=>RightMargin"
															+ rightMargin + "<=>BottomMargin" + bottomMargin
															+ "<=>Selectable" + selectAble + "<=>Shareable" + shareAble
															+ "<=>OnClick" + action + "}!#");
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
										}
									}).show();
                                      
										}
									});
							Above.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									ID = getSomeInt();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(Above.getText().toString());
									BiggroupButton2 STextView = new BiggroupButton2(context);
									STextView.setText(context.getString(R.string.editor_simple_text));
									BiggroupButton2 ATextView = new BiggroupButton2(context);
									ATextView.setText(context.getString(R.string.editor_advanced_text));
									BiggroupButton2 Button = new BiggroupButton2(context);
									Button.setText(context.getString(R.string.editor_button));
									BiggroupButton2 ImageView = new BiggroupButton2(context);
									ImageView.setText(context.getString(R.string.editor_pic));
									dialog_Content.addView(STextView);
									dialog_Content.addView(ATextView);
									dialog_Content.addView(Button);
									dialog_Content.addView(ImageView);
									STextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(AtextInfo,
													"{SText<=>" + ID + newSText + AtextInfo);
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ATextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(AtextInfo,
													"{AText<=>" + ID + newAText + AtextInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									Button.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(AtextInfo,
													"{Button<=>" + ID + newButton + AtextInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ImageView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(AtextInfo,
													"{Image<=>" + ID + newImage + AtextInfo);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Below.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									ID = getSomeInt();
									final EditorDialog dialog = new EditorDialog(context);
									TextView dialog_Title = dialog.getTitle();
									LinearLayout dialog_Content = dialog.getContent();
									dialog_Title.setText(Below.getText().toString());
									BiggroupButton2 STextView = new BiggroupButton2(context);
									STextView.setText(context.getString(R.string.editor_simple_text));
									BiggroupButton2 ATextView = new BiggroupButton2(context);
									ATextView.setText(context.getString(R.string.editor_advanced_text));
									BiggroupButton2 Button = new BiggroupButton2(context);
									Button.setText(context.getString(R.string.editor_button));
									BiggroupButton2 ImageView = new BiggroupButton2(context);
									ImageView.setText(context.getString(R.string.editor_pic));
									dialog_Content.addView(STextView);
									dialog_Content.addView(ATextView);
									dialog_Content.addView(Button);
									dialog_Content.addView(ImageView);
									STextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(AtextInfo,
													AtextInfo + "{SText<=>" + ID + newSText);
											if (Info.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ATextView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(AtextInfo,
													AtextInfo + "{AText<=>" + ID + newAText);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									Button.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(AtextInfo,
													AtextInfo + "{Button<=>" + ID + newButton);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									ImageView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String str = Info.replace(AtextInfo,
													AtextInfo + "{Image<=>" + ID + newImage);
											if (Info.startsWith("null")) {
												str = Info.replace("null", "");
											}
											utils.writeFile(data, str);
											Refresh(data, contentlinear);
											dialog.dismiss();
										}
									});
									dialog.show();
								}
							});
							Copy.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String ID = getSomeInt();
									String AtextInfo = "{AText<=>" + ID + "<=>Background" + background + "<=>Text"
											+ text + "<=>TextColor" + textColor + "<=>TextSize" + textSize
											+ "<=>Gravity" + gravity + "<=>Font" + font + "<=>Radius" + Radios
											+ "<=>Animation" + animation + "<=>Height" + height + "<=>TopMargin"
											+ topMargin + "<=>LeftMargin" + leftMargin + "<=>RightMargin" + rightMargin
											+ "<=>BottomMargin" + bottomMargin + "<=>Selectable" + selectAble
											+ "<=>Shareable" + shareAble + "<=>OnClick" + onclick + "}!#";
									String str = Info + AtextInfo;
									if (Info.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(data, str);
									Refresh(data, contentlinear);
									dialog.dismiss();
								}
							});
							Delete.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String str = Info.replace(AtextInfo, "");
									if (Info.startsWith("null")) {
										str = str.replace("null", "");
									}
									utils.writeFile(data, str);
									Refresh(data, contentlinear);
									dialog.dismiss();
								}
							});
							Text.setText(text);
							TextColor.setText(context.getString(R.string.a_color));
							TextColor.getLeftIcon().setBackgroundColor(Color.parseColor(textColor));
							TextSize.setText(textSize + "%");
							if (background.startsWith("Color")) {
								String str = background.replace("Color", "");
								Background.setText(context.getString(R.string.a_color));
								Background.getLeftIcon().setBackgroundColor(Color.parseColor(str));
							} else if (background.startsWith("Image")) {
								String str = background.replace("Image", "");
								if (picker.getImageNameById(str).startsWith("none")) {
									Background.setText(context.getString(R.string.a_default));
								} else {
									File In = new File(rootPath + imagesPath + str);
									Background.setLeftIcon(In);
									Background.setText(context.getString(R.string.a_picture)+context.getString(R.string.a_split) + picker.getImageNameById(str));
								}
							} else {
								Background.setText(context.getString(R.string.a_default));
							}
							if (picker.getFontNameById(font).equals("none")) {
								Font.setText(context.getString(R.string.a_default));
							} else {
								Typeface tf = Typeface.createFromFile(rootPath + fontsPath + font);
								Font.setTypeface(tf);
								Font.setText(picker.getFontNameById(font));
							}
							if (selectAble.equals("True")) {
								Selectable.setText(context.getString(R.string.a_on));
							} else {
								Selectable.setText(context.getString(R.string.a_off));
							}
							if (shareAble.equals("True")) {
								Shareable.setText(context.getString(R.string.a_on));
							} else {
								Shareable.setText(context.getString(R.string.a_off));
							}
							if (height.startsWith("WRAP")) {
								Height.setText(context.getString(R.string.a_auto));
							} else {
								Height.setText(height.replace("Size", ""));
							}
							if (gravity.startsWith("Right")) {
								Gravity.setText(context.getString(R.string.editor_right));
							} else if (gravity.startsWith("Center")) {
								Gravity.setText(context.getString(R.string.editor_center));
							} else if (gravity.startsWith("Left")) {
								Gravity.setText(context.getString(R.string.editor_left));
							} else {
								Gravity.setText(context.getString(R.string.a_auto));
							}
							Radius.setText(Radios + "%");
							if (animation.equals("left_in")) {
								Anim.setText(context.getString(R.string.editor_anim_left_in));
							} else if (animation.equals("right_in")) {
								Anim.setText(context.getString(R.string.editor_anim_right_in));
							} else if (animation.equals("fade_in")) {
								Anim.setText(context.getString(R.string.editor_anim_fade_in));
							} else if (animation.equals("slide_down")) {
								Anim.setText(context.getString(R.string.editor_anim_slide_down));
							} else if (animation.equals("bounce")) {
								Anim.setText(context.getString(R.string.editor_anim_bounce));
							} else if (animation.equals("blink")) {
								Anim.setText(context.getString(R.string.editor_anim_blinking));
							} else if (animation.equals("rotation")) {
								Anim.setText(context.getString(R.string.editor_anim_rotation));
							} else if (animation.equals("shake")) {
								Anim.setText(context.getString(R.string.editor_anim_shake));
							} else {
								Anim.setText(context.getString(R.string.editor_no_animation));
							}
							Margins.setText(context.getString(R.string.editor_top_margin)+context.getString(R.string.a_split) + topMargin + "%" + "\n" + context.getString(R.string.editor_bottom_margin)+context.getString(R.string.a_split) + bottomMargin + "%" + "\n"
									+ context.getString(R.string.editor_right_margin)+context.getString(R.string.a_split) + rightMargin + "%" + "\n" + context.getString(R.string.editor_left_margin)+context.getString(R.string.a_split) + leftMargin + "%");
							if (onclick.startsWith("GotoPage")) {
								String str = onclick.replaceFirst("GotoPage", "");
								if (new File(rootPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_open_page)+context.getString(R.string.a_split) + picker.getPageNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("GotoList")) {
								String str = onclick.replaceFirst("GotoList", "");
								if (new File(rootPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_open_list)+context.getString(R.string.a_split) + picker.getListNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("PlayAudio")) {
								String str = onclick.replaceFirst("PlayAudio", "");
								if (new File(rootPath + audiosPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_play_audio)+context.getString(R.string.a_split) + picker.getAudioNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("PlayVideo")) {
								String str = onclick.replaceFirst("PlayVideo", "");
								if (new File(rootPath + videosPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_play_video)+context.getString(R.string.a_split) + picker.getVideoNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("ShowImage")) {
								String str = onclick.replaceFirst("ShowImage", "");
								if (new File(rootPath + imagesPath + str).exists()) {
									OnClickForChange = onclick;
									OnClick.setText(context.getString(R.string.editor_action_show_pic)+context.getString(R.string.a_split) + picker.getImageNameById(str));
								} else {
									OnClickForChange = "none";
									OnClick.setText(context.getString(R.string.editor_no_action));
								}
							} else if (onclick.startsWith("ShowToast")) {
								String str = onclick.replaceFirst("ShowToast", "");
								OnClickForChange = "ShowToast" + str;
								OnClick.setText(context.getString(R.string.editor_action_show_toast)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("ShowAlert")) {
								String str = onclick.replaceFirst("ShowAlert", "");
								OnClickForChange = "ShowAlert" + str;
								String[] message = str.split("<!>");
								OnClick.setText(
										context.getString(R.string.editor_action_show_alert)+context.getString(R.string.a_split)+"\n" 
                                        + context.getString(R.string.editor_action_alert_title)+":"+"\n" + message[0].replaceFirst("Title", "")+"\n"+
                                        context.getString(R.string.editor_action_alert_text)+":"+"\n" + message[1].replaceFirst("Text", ""));
							} else if (onclick.startsWith("SendEmail")) {
								String str = onclick.replaceFirst("SendEmail", "");
                               OnClickForChange = "SendEmail" + str;
								OnClick.setText(context.getString(R.string.editor_action_send_mail)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("SendSms")) {
								String str = onclick.replaceFirst("SendSms", "");
                               OnClickForChange = "SendSms" + str;
								OnClick.setText(context.getString(R.string.editor_action_send_sms)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("OpenLink")) {
								String str = onclick.replaceFirst("OpenLink", "");
                                OnClickForChange = "OpenLink" + str;
								OnClick.setText(context.getString(R.string.editor_action_open_link)+context.getString(R.string.a_split) + str);
							} else if (onclick.startsWith("Exit")) {
                                OnClickForChange = "Exit";
								OnClick.setText(context.getString(R.string.editor_action_exit)+context.getString(R.string.a_split));
							} else {
								OnClickForChange = "none";
								OnClick.setText(context.getString(R.string.editor_no_action));
							}

							dialog.show();
						}
					});
				}
			}
		} catch (IOException e) {
		}
	}
}