package af.appsaz.myapplication.utilities;

import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.appsaz.Applications;
import af.alireza.views.BiggroupButton1;
import af.alireza.views.BiggroupTextView1;
import af.appsaz.myapplication.AudioActivity;
import af.appsaz.myapplication.ListActivity;
import af.appsaz.myapplication.PageActivity;
import af.appsaz.myapplication.PictureActivity;
import af.appsaz.myapplication.VideoActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import af.alireza.appsaz.R;

public class ContentsReader {
	Activity context;
	String rootPath = Applications.getInstance().getExternalFilesDir(null).getPath() + "/Project/MyApp/assets/";
	String audiosPath = "files/audios/";
	String videosPath = "files/videos/";
	String imagesPath = "files/images/";
	String iconsPath = "files/icons/";
	String fontsPath = "files/fonts/";

	Utilities utils = new Utilities();

	LayoutParams lParams;

	public ContentsReader(Activity context) {
		this.context = context;
	}

	public void setContentsReader(final String data, final LinearLayout contentlinear, String actionBarColor) {
		Refresh(data, contentlinear, actionBarColor);
	}

	private void Refresh(final String data, final LinearLayout contentlinear, final String actionBarColor) {
		contentlinear.removeAllViews();
		final String Info = utils.readFile(data);
		try {
			final String Content = Info;
			final String[] contentarray = Content.split("\\!\\#");
			for (String part : contentarray) {
				String part2 = part;
				ImageView imageView;
				final BiggroupTextView1 Atextview;
				final BiggroupButton1 button;
				final TextView Stextview;

				if (part2.contains("{Image<=>")) {
					final String[] code = part2.replace("}", "").split("<=>");
					final String itemId = code[1];
					final String image = code[2].replaceFirst("Image", "");
					final String animation = code[3].replace("Animation", "");
					final String height = code[4].replace("Height", "");
					final String onclick = code[9].replace("OnClick", "");
					imageView = new ImageView(context);
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
					if(animation.equals("left_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_in);
						imageView.startAnimation(anim);
					}else if(animation.equals("right_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
						imageView.startAnimation(anim);
					}else if(animation.equals("bounce")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.bouncing);
						imageView.startAnimation(anim);
					}else if(animation.equals("slide_down")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
						imageView.startAnimation(anim);
					}else if(animation.equals("fade_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
						imageView.startAnimation(anim);
					}else if(animation.equals("blink")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.blinking);
						imageView.startAnimation(anim);
					}else if(animation.equals("rotation")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotation);
						imageView.startAnimation(anim);
					}else if(animation.equals("shake")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.shake);
						imageView.startAnimation(anim);
					}
					final String imageInfo = "{Image<=>" + itemId + "<=>Image" + image + "<=>Animation" + animation
						+ "<=>Height" + height + "<=>TopMargin" + topMargin + "<=>LeftMargin" + leftMargin
						+ "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin + "<=>OnClick" + onclick
						+ "}!#";
					imageView.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if(onclick.startsWith("GotoPage")){
									final String str = onclick.replace("GotoPage","");
									if(new File(rootPath+str).exists()){
										Intent intent = new Intent(context, PageActivity.class);
										intent.putExtra("isFirstPage","No");
										intent.putExtra("pageName",str);
										context.startActivity(intent);
									}

								}else if(onclick.startsWith("GotoList")){
									final String str = onclick.replace("GotoList","");
									if(new File(rootPath+str).exists()){
										Intent intent = new Intent(context, ListActivity.class);
										intent.putExtra("isFirstPage","No");
										intent.putExtra("listName",str);
										context.startActivity(intent);
									}

								}else if(onclick.startsWith("ShowImage")){
									final String str = onclick.replace("ShowImage","");
									if(new File(rootPath+imagesPath+str).exists()){
										Intent intent = new Intent(context, PictureActivity.class);
										intent.putExtra("actionBarColor",actionBarColor);
										intent.putExtra("picName",str);
										context.startActivity(intent);
									}
								}else if(onclick.startsWith("PlayAudio")){
									final String str = onclick.replace("PlayAudio","");
									if(new File(rootPath+audiosPath+str).exists()){
										Intent intent = new Intent(context, AudioActivity.class);
										intent.putExtra("actionBarColor",actionBarColor);
										intent.putExtra("audioName",str);
										context.startActivity(intent);
									}
								}else if(onclick.startsWith("PlayVideo")){
									final String str = onclick.replace("PlayVideo","");
									if(new File(rootPath+videosPath+str).exists()){
										Intent intent = new Intent(context, VideoActivity.class);
										intent.putExtra("actionBarColor",actionBarColor);
										intent.putExtra("videoName",str);
										context.startActivity(intent);
									}
								}else if(onclick.startsWith("ShowAlert")){
									final String str = onclick.replaceFirst("ShowAlert","");
									final String[] array = str.split("<!>");
									final String dtitle = array[0].replaceFirst("Title","").replace("$¿?¿?&¥","<!>").replace("¥&?¿?¿$","!#");
									final String dtext = array[1].replaceFirst("Text","").replace("$¿?¿?&¥","<!>").replace("¥&?¿?¿$","!#");
									alertDialog(dtitle,dtext);
								}else if(onclick.startsWith("ShowToast")){
									final String str = onclick.replaceFirst("ShowToast","");
									Toast.makeText(context,str,Toast.LENGTH_LONG).show();
								}else if(onclick.startsWith("SendEmail")){
									final String str = onclick.replaceFirst("SendEmail","");
									Intent i = new Intent(Intent.ACTION_SEND);
									i.setType("message/rfc822");
									String[] emails = str.trim().split(",");
									i.putExtra(Intent.EXTRA_EMAIL, emails);
									i.putExtra(Intent.EXTRA_SUBJECT, "");
									i.putExtra(Intent.EXTRA_TEXT, "");
									try {
										context.startActivity(Intent.createChooser(i, "ارسال ایمیل..."));
									} catch (android.content.ActivityNotFoundException ex) {
										Toast.makeText(context, "هیچ برنامه ایمیلی پیدا نشد.", Toast.LENGTH_SHORT).show();
									}
								}else if(onclick.startsWith("SendSms")){
									final String str = onclick.replaceFirst("SendSms","");
									String phoneNumber = str;
									Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
									intent.putExtra("sms_body", "");
									context.startActivity(intent);
								}else if(onclick.startsWith("OpenLink")){
									final String str = onclick.replaceFirst("OpenLink","");
                                    String url = str;
									if (!(url.startsWith("http://") || url.startsWith("https://"))) {
										url = "http://" + url;
									}
									Intent i = new Intent("android.intent.action.VIEW");
									i.setData(Uri.parse(url));
									context.startActivity(i);
								}else if(onclick.startsWith("Exit")){
									final String str = onclick.replace("Exit","");
									context.finish();
								}
							}
						});
				} else if (part2.contains("{Button<=>")) {
					String[] code = part2.replace("}", "").split("<=>");
					button = new BiggroupButton1(context);
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
				    if(animation.equals("left_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_in);
						button.startAnimation(anim);
					}else if(animation.equals("right_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
						button.startAnimation(anim);
					}else if(animation.equals("bounce")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.bouncing);
						button.startAnimation(anim);
					}else if(animation.equals("slide_down")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
						button.startAnimation(anim);
					}else if(animation.equals("fade_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
						button.startAnimation(anim);
					}else if(animation.equals("blink")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.blinking);
						button.startAnimation(anim);
					}else if(animation.equals("rotation")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotation);
						button.startAnimation(anim);
					}else if(animation.equals("shake")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.shake);
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
								if(onclick.startsWith("GotoPage")){
									final String str = onclick.replace("GotoPage","");
									if(new File(rootPath+str).exists()){
										Intent intent = new Intent(context, PageActivity.class);
										intent.putExtra("isFirstPage","No");
										intent.putExtra("pageName",str);
										context.startActivity(intent);
									}

								}else if(onclick.startsWith("GotoList")){
									final String str = onclick.replace("GotoList","");
									if(new File(rootPath+str).exists()){
										Intent intent = new Intent(context, ListActivity.class);
										intent.putExtra("isFirstPage","No");
										intent.putExtra("listName",str);
										context.startActivity(intent);
									}

								}else if(onclick.startsWith("ShowImage")){
									final String str = onclick.replace("ShowImage","");
									if(new File(rootPath+imagesPath+str).exists()){
										Intent intent = new Intent(context, PictureActivity.class);
										intent.putExtra("actionBarColor",actionBarColor);
										intent.putExtra("picName",str);
										context.startActivity(intent);
									}
								}else if(onclick.startsWith("PlayAudio")){
									final String str = onclick.replace("PlayAudio","");
									if(new File(rootPath+audiosPath+str).exists()){
										Intent intent = new Intent(context, AudioActivity.class);
										intent.putExtra("actionBarColor",actionBarColor);
										intent.putExtra("audioName",str);
										context.startActivity(intent);
									}
								}else if(onclick.startsWith("PlayVideo")){
									final String str = onclick.replace("PlayVideo","");
									if(new File(rootPath+videosPath+str).exists()){
										Intent intent = new Intent(context, VideoActivity.class);
										intent.putExtra("actionBarColor",actionBarColor);
										intent.putExtra("videoName",str);
										context.startActivity(intent);
									}
								}else if(onclick.startsWith("ShowAlert")){
									final String str = onclick.replaceFirst("ShowAlert","");
									final String[] array = str.split("<!>");
									final String dtitle = array[0].replaceFirst("Title","").replace("$¿?¿?&¥","<!>").replace("¥&?¿?¿$","!#");
									final String dtext = array[1].replaceFirst("Text","").replace("$¿?¿?&¥","<!>").replace("¥&?¿?¿$","!#");
									alertDialog(dtitle,dtext);
								}else if(onclick.startsWith("ShowToast")){
									final String str = onclick.replaceFirst("ShowToast","");
									Toast.makeText(context,str,Toast.LENGTH_LONG).show();
								}else if(onclick.startsWith("SendEmail")){
									final String str = onclick.replaceFirst("SendEmail","");
									Intent i = new Intent(Intent.ACTION_SEND);
									i.setType("message/rfc822");
									String[] emails = str.trim().split(",");
									i.putExtra(Intent.EXTRA_EMAIL, emails);
									i.putExtra(Intent.EXTRA_SUBJECT, "");
									i.putExtra(Intent.EXTRA_TEXT, "");
									try {
										context.startActivity(Intent.createChooser(i, "ارسال ایمیل..."));
									} catch (android.content.ActivityNotFoundException ex) {
										Toast.makeText(context, "هیچ برنامه ایمیلی پیدا نشد.", Toast.LENGTH_SHORT).show();
									}
								}else if(onclick.startsWith("SendSms")){
									final String str = onclick.replaceFirst("SendSms","");
									String phoneNumber = str;
									Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
									intent.putExtra("sms_body", "");
									context.startActivity(intent);
								}else if(onclick.startsWith("OpenLink")){
									final String str = onclick.replaceFirst("OpenLink","");
                                    String url = str;
									if (!(url.startsWith("http://") || url.startsWith("https://"))) {
										url = "http://" + url;
									}
									Intent i = new Intent("android.intent.action.VIEW");
									i.setData(Uri.parse(url));
									context.startActivity(i);
								}else if(onclick.startsWith("Exit")){
									final String str = onclick.replace("Exit","");
									context.finish();
								}
							}
						});

				} else if (part2.contains("{SText<=>")) {
					String[] code = part2.replace("}", "").split("<=>");
					Stextview = new TextView(context);
					Stextview.setAutoLinkMask(1);
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
					if(animation.equals("left_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_in);
						Stextview.startAnimation(anim);
					}else if(animation.equals("right_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
						Stextview.startAnimation(anim);
					}else if(animation.equals("bounce")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.bouncing);
						Stextview.startAnimation(anim);
					}else if(animation.equals("slide_down")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
						Stextview.startAnimation(anim);
					}else if(animation.equals("fade_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
						Stextview.startAnimation(anim);
					}else if(animation.equals("blink")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.blinking);
						Stextview.startAnimation(anim);
					}else if(animation.equals("rotation")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotation);
						Stextview.startAnimation(anim);
					}else if(animation.equals("shake")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.shake);
						Stextview.startAnimation(anim);
					}
					final String StextInfo = "{SText<=>" + itemId + "<=>Text" + text + "<=>TextColor" + textColor
						+ "<=>TextSize" + textSize + "<=>Gravity" + gravity + "<=>Font" + font + "<=>Animation"
						+ animation + "<=>Height" + height + "<=>TopMargin" + topMargin + "<=>LeftMargin"
						+ leftMargin + "<=>RightMargin" + rightMargin + "<=>BottomMargin" + bottomMargin
						+ "<=>Selectable" + selectAble + "}!#";
				} else if (part2.contains("{AText<=>")) {
					String[] code = part2.replace("}", "").split("<=>");
					Atextview = new BiggroupTextView1(context);
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
                        Atextview.setAutoLink();
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
					//	Atextview.setText(text + "\n\n");
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
					if(animation.equals("left_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_in);
						Atextview.startAnimation(anim);
					}else if(animation.equals("right_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
						Atextview.startAnimation(anim);
					}else if(animation.equals("bounce")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.bouncing);
						Atextview.startAnimation(anim);
					}else if(animation.equals("slide_down")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
						Atextview.startAnimation(anim);
					}else if(animation.equals("fade_in")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
						Atextview.startAnimation(anim);
					}else if(animation.equals("blink")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.blinking);
						Atextview.startAnimation(anim);
					}else if(animation.equals("rotation")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotation);
						Atextview.startAnimation(anim);
					}else if(animation.equals("shake")){
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.shake);
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

								if(onclick.startsWith("GotoPage")){
									final String str = onclick.replace("GotoPage","");
									if(new File(rootPath+str).exists()){
										Intent intent = new Intent(context, PageActivity.class);
										intent.putExtra("isFirstPage","No");
										intent.putExtra("pageName",str);
										context.startActivity(intent);
									}

								}else if(onclick.startsWith("GotoList")){
									final String str = onclick.replace("GotoList","");
									if(new File(rootPath+str).exists()){
										Intent intent = new Intent(context, ListActivity.class);
										intent.putExtra("isFirstPage","No");
										intent.putExtra("listName",str);
										context.startActivity(intent);
									}

								}else if(onclick.startsWith("ShowImage")){
									final String str = onclick.replace("ShowImage","");
									if(new File(rootPath+imagesPath+str).exists()){
										Intent intent = new Intent(context, PictureActivity.class);
										intent.putExtra("actionBarColor",actionBarColor);
										intent.putExtra("picName",str);
										context.startActivity(intent);
									}
								}else if(onclick.startsWith("PlayAudio")){
									final String str = onclick.replace("PlayAudio","");
									if(new File(rootPath+audiosPath+str).exists()){
										Intent intent = new Intent(context, AudioActivity.class);
										intent.putExtra("actionBarColor",actionBarColor);
										intent.putExtra("audioName",str);
										context.startActivity(intent);
									}
								}else if(onclick.startsWith("PlayVideo")){
									final String str = onclick.replace("PlayVideo","");
									if(new File(rootPath+videosPath+str).exists()){
										Intent intent = new Intent(context, VideoActivity.class);
										intent.putExtra("actionBarColor",actionBarColor);
										intent.putExtra("videoName",str);
										context.startActivity(intent);
									}
								}else if(onclick.startsWith("ShowAlert")){
									final String str = onclick.replaceFirst("ShowAlert","");
									final String[] array = str.split("<!>");
									final String dtitle = array[0].replaceFirst("Title","").replace("$¿?¿?&¥","<!>").replace("¥&?¿?¿$","!#");
									final String dtext = array[1].replaceFirst("Text","").replace("$¿?¿?&¥","<!>").replace("¥&?¿?¿$","!#");
									alertDialog(dtitle,dtext);
								}else if(onclick.startsWith("ShowToast")){
									final String str = onclick.replaceFirst("ShowToast","");
									Toast.makeText(context,str,Toast.LENGTH_LONG).show();
								}else if(onclick.startsWith("SendEmail")){
									final String str = onclick.replaceFirst("SendEmail","");
									Intent i = new Intent(Intent.ACTION_SEND);
									i.setType("message/rfc822");
									String[] emails = str.trim().split(",");
									i.putExtra(Intent.EXTRA_EMAIL, emails);
									i.putExtra(Intent.EXTRA_SUBJECT, "");
									i.putExtra(Intent.EXTRA_TEXT, "");
									try {
										context.startActivity(Intent.createChooser(i, "ارسال ایمیل..."));
									} catch (android.content.ActivityNotFoundException ex) {
										Toast.makeText(context, "هیچ برنامه ایمیلی پیدا نشد.", Toast.LENGTH_SHORT).show();
									}
								}else if(onclick.startsWith("SendSms")){
									final String str = onclick.replaceFirst("SendSms","");
									String phoneNumber = str;
									Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
									intent.putExtra("sms_body", "");
									context.startActivity(intent);
								}else if(onclick.startsWith("OpenLink")){
									final String str = onclick.replaceFirst("OpenLink","");
                                    String url = str;
									if (!(url.startsWith("http://") || url.startsWith("https://"))) {
										url = "http://" + url;
									}
									Intent i = new Intent("android.intent.action.VIEW");
									i.setData(Uri.parse(url));
									context.startActivity(i);
								}else if(onclick.startsWith("Exit")){
									final String str = onclick.replace("Exit","");
									context.finish();
								}
							}
						});
				}
			}
		} catch (IOException e) {
		}
	}
    private void alertDialog(String title, String body){
        new MaterialAlertDialogBuilder(context)
			.setTitle(title)
			.setMessage(body)
			.setPositiveButton(context.getString(android.R.string.ok), null)
			.setCancelable(false)
			.show();
	}
}
