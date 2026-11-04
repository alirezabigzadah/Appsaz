package af.alireza.appsaz;

import af.alireza.appsaz.AnimationPickerDialog.OnAnimSelectedListener;
import af.alireza.appsaz.AudioPickerDialog.OnAudioSelectedListener;
import af.alireza.appsaz.ColorPickerDialog.OnColorSelectedListener;
import af.alireza.appsaz.FontPickerDialog.OnFontSelectedListener;
import af.alireza.appsaz.IconPickerDialog.OnIconSelectedListener;
import af.alireza.appsaz.ImagePickerDialog.OnImageSelectedListener;
import af.alireza.appsaz.ListPickerDialog.OnListSelectedListener;
import af.alireza.appsaz.MarginPickerDialog.OnDipSelectedListener;
import af.alireza.appsaz.PagePickerDialog.OnPageSelectedListener;
import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.appsaz.VideoPickerDialog.OnVideoSelectedListener;
import af.alireza.views.BiggroupButton2;
import af.alireza.views.BiggroupSeekbar;
import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialog;
import com.rarepebble.colorpicker.ColorPickerView;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Picker {

	public static String rootAssetsPath = Applications.getInstance().getExternalFilesDir(null).getPath() + "/Project/MyApp/assets/";
	//public static String rootAssetsPath = "/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/";
	public static String imagesPath="files/images/";
	public static String iconsPath="files/icons/";
	public static String audiosPath="files/audios/";
	public static String videosPath="files/videos/";
	public static String fontsPath="files/fonts/";
    final Utilities utils = new Utilities();

    public String getAdmobAppID () {
        String id = "ca-app-pub-3940256099942544~3347511713";
        String Info = utils.readFile(rootAssetsPath + "ads");
        String items = new String(Info);
        String[] ads = items.split("\\!\\#");
		for (String p1 : ads) {
			String p=p1;
			if (p.contains("{Admob<=>")) {
				String[] code = p.replace("}", "").split("<=>");
				String mode = code[1].replaceFirst("Mode", "");
				String appId = code[2].replaceFirst("AbmobApplicationId", "");
				if (mode.equals("True")) {
					id = appId;
				}
             }
            }
        return id;
        }
	public String getImageNameById(String Id){
		String Name = "none";
		String str = Id;
		try{
			if (new File(rootAssetsPath + imagesPath + str).exists())
			{
				String data = utils.readFile(rootAssetsPath+imagesPath+"images");
				String[] datas = data.split("\\!\\#");
				for (String p1 : datas) {
					String p=p1;
					if (p.startsWith("{Image<=>"+str.replace("image",""))) {
						String[] code = p.replace("}", "").split("<=>");
						String id = code[1];
						String name = code[2].replace("Title","");
						String file = code[3].replace("Img","");
						Name = name;
					}}
			}else{
				Name = "none";
			}}
		catch (Exception e)
		{}
		return Name;
	}
	public ArrayList<String> getAllImages(){
		ArrayList<String> list = new ArrayList();
		try
		{
			String str = utils.readFile(rootAssetsPath+imagesPath+"images"); 
		//	str = str.trim();
			final String[] durarray = str.split("\\!\\#");
			for (String part : durarray) {
				String part2=part;
				if (part2.contains("{Image<=>")) {
					final String[] code = part2.replace("}", "").split("<=>");
					final String text = code[2].replace("Title","");
					final String fileName = code[3].replace("Img","");
					list.add(fileName);
				}else{

				}
			}}
		catch (Exception e)
		{}
		return list;
	}
	public String getIconNameById(String Id){
		String Name = "none";
		String str = Id;
		try{
			if (new File(rootAssetsPath + iconsPath + str).exists())
			{
				String data = utils.readFile(rootAssetsPath+iconsPath+"icons");
				String[] datas = data.split("\\!\\#");
				for (String p1 : datas) {
					String p=p1;
					if (p.startsWith("{Icon<=>"+str.replace("icon",""))) {
						String[] code = p.replace("}", "").split("<=>");
						String id = code[1];
						String name = code[2].replace("Title","");
						String file = code[3].replace("Img","");
						Name = name;
					}}
			}else{
				Name = "none";
			}}
		catch (Exception e)
		{}
		return Name;
	}
	public String getAudioNameById(String Id){
		String Name = "none";
		String str = Id;
		try{
			if (new File(rootAssetsPath + audiosPath + str).exists())
			{
				String data = utils.readFile(rootAssetsPath+audiosPath+"audios");
				String[] datas = data.split("\\!\\#");
				for (String p1 : datas) {
					String p=p1;
					if (p.startsWith("{Audio<=>"+str.replace("audio",""))) {
						String[] code = p.replace("}", "").split("<=>");
						String id = code[1];
						String name = code[2].replace("Title","");
						String file = code[3].replace("Audio","");
						Name = name;
					}}
			}else{
				Name = "none";
			}}
		catch (Exception e)
		{}
		return Name;
	}
	public ArrayList<String> getAllAudios(){
		ArrayList<String> list = new ArrayList();
		try
		{
			String str = utils.readFile(rootAssetsPath+audiosPath+"audios");
		//	str = str.trim();
			final String[] durarray = str.split("\\!\\#");
			for (String part : durarray) {
				String part2=part;
				if (part2.contains("{Audio<=>")) {
					final String[] code = part2.replace("}", "").split("<=>");
					final String text = code[2].replace("Title","");
					final String fileName = code[3].replace("Audio","");
					list.add(fileName);
				}else{

				}
			}}
		catch (Exception e)
		{}
		return list;
	}
	public String getVideoNameById(String Id){
		String Name = "none";
		String str = Id;
		try{
			if (new File(rootAssetsPath + videosPath + str).exists())
			{
				String data = utils.readFile(rootAssetsPath+videosPath+"videos");
				String[] datas = data.split("\\!\\#");
				for (String p1 : datas) {
					String p=p1;
					if (p.startsWith("{Video<=>"+str.replace("video",""))) {
						String[] code = p.replace("}", "").split("<=>");
						String id = code[1];
						String name = code[2].replace("Title","");
						String file = code[3].replace("Video","");
						Name = name;
					}}
			}else{
				Name = "none";
			}}
		catch (Exception e)
		{}
		return Name;
	}
	public ArrayList<String> getAllVideos(){
		ArrayList<String> list = new ArrayList();
		try
		{
			String str = utils.readFile(rootAssetsPath+videosPath+"videos"); 
		//	str = str.trim();
			final String[] durarray = str.split("\\!\\#");
			for (String part : durarray) {
				String part2=part;
				if (part2.contains("{Video<=>")) {
					final String[] code = part2.replace("}", "").split("<=>");
					final String text = code[2].replace("Title","");
					final String fileName = code[3].replace("Video","");
					list.add(fileName);
				}else{

				}
			}}
		catch (Exception e)
		{}
		return list;
	}
	public String getFontNameById(String Id){
		String Name = "none";
		String str = Id;
		try{
			if (new File(rootAssetsPath + fontsPath + str).exists())
			{
				String data = utils.readFile(rootAssetsPath+fontsPath+"fonts");
				String[] datas = data.split("\\!\\#");
				for (String p1 : datas) {
					String p=p1;
					if (p.startsWith("{Font<=>"+str.replace("font",""))) {
						String[] code = p.replace("}", "").split("<=>");
						String id = code[1];
						String name = code[2].replace("Title","");
						String file = code[3].replace("Font","");
						Name = name;
					}}
			}else{
				Name = "none";
			}}
		catch (Exception e)
		{}
		return Name;
	}
	public String getPageNameById(String Id){
		String Name = "none";
		String str = Id;
		try{
			if (new File(rootAssetsPath + str).exists())
			{
				String data = utils.readFile(rootAssetsPath+"pages");
				String[] datas = data.split("\\!\\#");
				for (String p1 : datas) {
					String p=p1;
					if (p.startsWith("{Page<=>"+str.replace("page",""))) {
						String[] code = p.replace("}", "").split("<=>");
						String id = code[1];
						String name = code[2].replace("Title","");
						String file = code[3].replace("Page","");
						Name = name;
					}}
			}else{
				Name = "none";
			}}
		catch (Exception e)
		{}
		return Name;
	}
	public ArrayList<String> getAllPages(){
		ArrayList<String> list = new ArrayList();
		try
		{
			String str = utils.readFile(rootAssetsPath+"pages"); 
		//	str = str.trim();
			final String[] durarray = str.split("\\!\\#");
			for (String part : durarray) {
				String part2=part;
				if (part2.contains("{Page<=>")) {
					final String[] code = part2.replace("}", "").split("<=>");
					final String text = code[2].replace("Title","");
					final String fileName = code[3].replace("Page","");
					list.add(fileName);
				}else{

				}
			}}
		catch (Exception e)
		{}
		return list;
	}
	public String getListNameById(String Id){
		String Name = "none";
		String str = Id;
		try{
			if (new File(rootAssetsPath + str).exists())
			{
				String data = utils.readFile(rootAssetsPath+"lists");
				String[] datas = data.split("\\!\\#");
				for (String p1 : datas) {
					String p=p1;
					if (p.startsWith("{List<=>"+str.replace("list",""))) {
						String[] code = p.replace("}", "").split("<=>");
						String id = code[1];
						String name = code[2].replace("Title","");
						String file = code[3].replace("List","");
						Name = name;
					}}
			}else{
				Name = "none";
			}}
		catch (Exception e)
		{}
		return Name;
	}
	public ArrayList<String> getAllLists(){
		ArrayList<String> list = new ArrayList();
		try
		{
			String str = utils.readFile(rootAssetsPath+"lists"); 
		//	str = str.trim();
			final String[] durarray = str.split("\\!\\#");
			for (String part : durarray) {
				String part2=part;
				if (part2.contains("{List<=>")) {
					final String[] code = part2.replace("}", "").split("<=>");
					final String text = code[2].replace("Title","");
					final String fileName = code[3].replace("List","");
					list.add(fileName);
				}else{

				}
			}}
		catch (Exception e)
		{}
		return list;
	}
	public LinearLayout dialog(Context context){
		final AppCompatDialog dialog = new AppCompatDialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) dialog.findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		LinearLayout dialog_Content = (LinearLayout) dialog.findViewById(R.id.dialogContent);
		dialog.show();
		return dialog_Content;
	}

}
class ColorPickerDialog extends AppCompatDialog
{
    private final OnColorSelectedListener OnColorSelectedListener;
    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }

    public ColorPickerDialog(Context context, int oldColor,String requests, OnColorSelectedListener OnColorSelectedListener) {
        super(context);
        this.OnColorSelectedListener = OnColorSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.color_picker_dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setText(context.getString(R.string.a_color_picker));

        final ColorPickerView picker = (ColorPickerView)findViewById(R.id.colorPickerView);
		Button cancelBtn = (Button)findViewById(R.id.colorPickerCancelBtn);
		Button pickBtn = (Button)findViewById(R.id.colorPickerPickBtn);
		picker.setColor(oldColor);
		if(requests.equals("HideAlpha")){
			picker.showAlpha(false);
		}
		cancelBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		pickBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ColorPickerDialog.this.OnColorSelectedListener.onColorSelected(picker.getColor());
					dismiss();
				}
			});
	}
}
class EditorDialog extends AppCompatDialog
{
    LinearLayout dialog_Content;
	TextView dialog_Title;
	public EditorDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);

	}
	public TextView getTitle (){
		return dialog_Title;
	}
	public LinearLayout getContent (){
		return dialog_Content;
	}
}
class ImagePickerDialog extends AppCompatDialog
{
    private final OnImageSelectedListener onImageSelectedListener;
    public interface OnImageSelectedListener {
        void onImageSelected(String name, String id);
    }

    public ImagePickerDialog(Context context, String title, OnImageSelectedListener onImageSelectedListener) {
        super(context);
        this.onImageSelectedListener = onImageSelectedListener;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		dialog_Title.setText(title);

        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);
		try
		{
			FileInputStream inputFile = new FileInputStream(Picker.rootAssetsPath+Picker.imagesPath+"images");
			InputStream input = inputFile;
			int s = input.available();
			byte[] b = new byte[s];
			input.read(b);
			input.close();
			String data = new String(b);
			String[] datas = data.split("\\!\\#");
			BiggroupButton2 btn;
			for (String p1 : datas) {
				String p=p1;
				if (p.contains("{Image<=>")) {
					btn = new BiggroupButton2(context);
					String[] code = p.replace("}", "").split("<=>");
					String id = code[1];
					final String name = code[2].replace("Title","");
					final String file = code[3].replace("Img","");

					btn.setText(name);
					File In = new File(Picker.rootAssetsPath+Picker.imagesPath+file);
					btn.setLeftIcon(In);
					btn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ImagePickerDialog.this.onImageSelectedListener.onImageSelected(name,file);
								dismiss();
							}
						});
					dialog_Content.addView(btn);

				}else{
					TextView emptyText = new TextView(context);
					emptyText.setText(context.getString(R.string.files_empty_pics1));
					emptyText.setGravity(17);
					emptyText.setHeight(100);
					dialog_Content.addView(emptyText);
				}
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 

		}

	}
}
class IconPickerDialog extends AppCompatDialog
{
    private final OnIconSelectedListener onIconSelectedListener;
    public interface OnIconSelectedListener {
        void onIconSelected(String name, String id);
    }

    public IconPickerDialog(Context context, String title, OnIconSelectedListener onIconSelectedListener) {
        super(context);
        this.onIconSelectedListener = onIconSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		dialog_Title.setText(title.replaceFirst("GoneNone",""));
        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);

		try
		{
			if(title.startsWith("GoneNone")){

			}else{
				final BiggroupButton2 noneBtn = new BiggroupButton2(context);
				noneBtn.setText(context.getString(R.string.editor_no_icon));
				noneBtn.setLeftIcon(R.drawable.none);
				dialog_Content.addView(noneBtn);
				noneBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String name = noneBtn.getText().toString();
							String file = "none";
							IconPickerDialog.this.onIconSelectedListener.onIconSelected(name,file);
							dismiss();

						}
					});
			}		
			FileInputStream inputFile = new FileInputStream(Picker.rootAssetsPath+Picker.iconsPath+"icons");
			InputStream input = inputFile;
			int s = input.available();
			byte[] b = new byte[s];
			input.read(b);
			input.close();
			String data = new String(b);
			String[] datas = data.split("\\!\\#");
			BiggroupButton2 btn;
			for (String p1 : datas) {
				String p=p1;
				if (p.contains("{Icon<=>")) {
					btn = new BiggroupButton2(context);
					String[] code = p.replace("}", "").split("<=>");
					String id = code[1];
					final String name = code[2].replace("Title","");
					final String file = code[3].replace("Img","");

					btn.setText(name);
					File In = new File(Picker.rootAssetsPath+Picker.iconsPath+file);
					btn.setLeftIcon(In);
					btn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								IconPickerDialog.this.onIconSelectedListener.onIconSelected(name,file);
								dismiss();
							}
						});
					dialog_Content.addView(btn);

				}else{
					TextView emptyText = new TextView(context);
					emptyText.setText(context.getString(R.string.files_empty_icons1));
					emptyText.setGravity(17);
					emptyText.setHeight(100);
					dialog_Content.addView(emptyText);
				}
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 

		}

	}
}
class AudioPickerDialog extends AppCompatDialog
{
	private final OnAudioSelectedListener onAudioSelectedListener;
    public interface OnAudioSelectedListener {
        void onAudioSelected(String name, String id);
    }

    public AudioPickerDialog(Context context, String title, OnAudioSelectedListener onAudioSelectedListener) {
        super(context);
        this.onAudioSelectedListener = onAudioSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		dialog_Title.setText(title);

        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);
		try
		{
			FileInputStream inputFile = new FileInputStream(Picker.rootAssetsPath+Picker.audiosPath+"audios");
			InputStream input = inputFile;
			int s = input.available();
			byte[] b = new byte[s];
			input.read(b);
			input.close();
			String data = new String(b);
			String[] datas = data.split("\\!\\#");
			BiggroupButton2 btn;
			for (String p1 : datas) {
				String p=p1;
				if (p.contains("{Audio<=>")) {
					btn = new BiggroupButton2(context);
					String[] code = p.replace("}", "").split("<=>");
					String id = code[1];
					final String name = code[2].replace("Title","");
					final String file = code[3].replace("Audio","");
					final String audioImg = code[4].replace("AudioImg","");
					if(new File(Picker.rootAssetsPath+Picker.imagesPath+audioImg).exists()){
						File In = new File(Picker.rootAssetsPath+Picker.imagesPath+audioImg);
						btn.setLeftIcon(In);
					}else{
						btn.setLeftIcon(R.drawable.default_audio_img);
					}

					btn.setText(name);
					btn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								AudioPickerDialog.this.onAudioSelectedListener.onAudioSelected(name,file);
								dismiss();
							}
						});
					dialog_Content.addView(btn);

				}else{
					TextView emptyText = new TextView(context);
					emptyText.setText(context.getString(R.string.files_empty_audios1));
					emptyText.setGravity(17);
					emptyText.setHeight(100);
					dialog_Content.addView(emptyText);
				}
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 

		}

	}
}
class VideoPickerDialog extends AppCompatDialog
{
 	private final OnVideoSelectedListener onVideoSelectedListener;
    public interface OnVideoSelectedListener {
        void onVideoSelected(String name, String id);
    }

    public VideoPickerDialog(Context context, String title, OnVideoSelectedListener onVideoSelectedListener) {
        super(context);
        this.onVideoSelectedListener = onVideoSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		dialog_Title.setText(title);
        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);
		try
		{
			FileInputStream inputFile = new FileInputStream(Picker.rootAssetsPath+Picker.videosPath+"videos");
			InputStream input = inputFile;
			int s = input.available();
			byte[] b = new byte[s];
			input.read(b);
			input.close();
			String data = new String(b);
			String[] datas = data.split("\\!\\#");
			BiggroupButton2 btn;
			for (String p1 : datas) {
				String p=p1;
				if (p.contains("{Video<=>")) {
					btn = new BiggroupButton2(context);
					String[] code = p.replace("}", "").split("<=>");
					String id = code[1];
					final String name = code[2].replace("Title","");
					final String file = code[3].replace("Video","");

					btn.setText(name);
					btn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								VideoPickerDialog.this.onVideoSelectedListener.onVideoSelected(name,file);
								dismiss();
							}
						});
					dialog_Content.addView(btn);

				}else{
					TextView emptyText = new TextView(context);
					emptyText.setText(context.getString(R.string.files_empty_videos1));
					emptyText.setGravity(17);
					emptyText.setHeight(100);
					dialog_Content.addView(emptyText);
				}
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 

		}

	}
}
class FontPickerDialog extends AppCompatDialog
{
	private final OnFontSelectedListener onFontSelectedListener;
    public interface OnFontSelectedListener {
        void onFontSelected(String name, String id);
    }

    public FontPickerDialog(Context context, String title, OnFontSelectedListener onFontSelectedListener) {
        super(context);
        this.onFontSelectedListener = onFontSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		dialog_Title.setText(title);
        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);
		try
		{
			final BiggroupButton2 noneBtn = new BiggroupButton2(context);
			noneBtn.setText(context.getString(R.string.a_default));
			dialog_Content.addView(noneBtn);
			noneBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String name = noneBtn.getText().toString();
						String file = "none";
						FontPickerDialog.this.onFontSelectedListener.onFontSelected(name,file);
						dismiss();

					}
				});
			FileInputStream inputFile = new FileInputStream(Picker.rootAssetsPath+Picker.fontsPath+"fonts");
			InputStream input = inputFile;
			int s = input.available();
			byte[] b = new byte[s];
			input.read(b);
			input.close();
			String data = new String(b);
			String[] datas = data.split("\\!\\#");
			BiggroupButton2 btn;
			for (String p1 : datas) {
				String p=p1;
				if (p.contains("{Font<=>")) {
					btn = new BiggroupButton2(context);
					String[] code = p.replace("}", "").split("<=>");
					String id = code[1];
					final String name = code[2].replace("Title","");
					final String file = code[3].replace("Font","");

					btn.setText(name);
					Typeface tf = Typeface.createFromFile(Picker.rootAssetsPath+Picker.fontsPath+file);
					btn.setTypeface(tf);
					btn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								FontPickerDialog.this.onFontSelectedListener.onFontSelected(name,file);
								dismiss();
							}
						});
					dialog_Content.addView(btn);

				}else{
					TextView emptyText = new TextView(context);
					emptyText.setText(context.getString(R.string.files_empty_fonts1));
					emptyText.setGravity(17);
					emptyText.setHeight(100);
					dialog_Content.addView(emptyText);
				}
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 

		}

	}
}
class PagePickerDialog extends AppCompatDialog
{
    private final OnPageSelectedListener onPageSelectedListener;
    public interface OnPageSelectedListener {
        void onPageSelected(String name, String id);
    }

    public PagePickerDialog(Context context, String title, OnPageSelectedListener onPageSelectedListener) {
        super(context);
        this.onPageSelectedListener = onPageSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		dialog_Title.setText(title);
        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);
		try
		{
			FileInputStream inputFile = new FileInputStream(Picker.rootAssetsPath+"pages");
			InputStream input = inputFile;
			int s = input.available();
			byte[] b = new byte[s];
			input.read(b);
			input.close();
			String data = new String(b);
			String[] datas = data.split("\\!\\#");
			BiggroupButton2 btn;
			for (String p1 : datas) {
				String p=p1;
				if (p.contains("{Page<=>")) {
					btn = new BiggroupButton2(context);
					String[] code = p.replace("}", "").split("<=>");
					String id = code[1];
					final String name = code[2].replaceFirst("Title","");
					final String file = code[3].replace("Page","");

					btn.setText(name);
					btn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								PagePickerDialog.this.onPageSelectedListener.onPageSelected(name,file);
								dismiss();
							}
						});
					dialog_Content.addView(btn);

				}else{
					TextView emptyText = new TextView(context);
					emptyText.setText(context.getString(R.string.files_empty_pages1));
					emptyText.setGravity(17);
					emptyText.setHeight(100);
					dialog_Content.addView(emptyText);
				}
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 

		}

	}
}
class ListPickerDialog extends AppCompatDialog
{
    private final OnListSelectedListener onListSelectedListener;
    public interface OnListSelectedListener {
        void onListSelected(String name, String id);
    }

    public ListPickerDialog(Context context, String title, OnListSelectedListener onListSelectedListener) {
        super(context);
        this.onListSelectedListener = onListSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		dialog_Title.setText(title);
        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);
		try
		{
			FileInputStream inputFile = new FileInputStream(Picker.rootAssetsPath+"lists");
			InputStream input = inputFile;
			int s = input.available();
			byte[] b = new byte[s];
			input.read(b);
			input.close();
			String data = new String(b);
			String[] datas = data.split("\\!\\#");
			BiggroupButton2 btn;
			for (String p1 : datas) {
				String p=p1;
				if (p.contains("{List<=>")) {
					btn = new BiggroupButton2(context);
					String[] code = p.replace("}", "").split("<=>");
					String id = code[1];
					final String name = code[2].replaceFirst("Title","");
					final String file = code[3].replace("List","");

					btn.setText(name);
					btn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ListPickerDialog.this.onListSelectedListener.onListSelected(name,file);
								dismiss();
							}
						});
					dialog_Content.addView(btn);

				}else{
					TextView emptyText = new TextView(context);
					emptyText.setText(context.getString(R.string.files_empty_lists1));
					emptyText.setGravity(17);
					emptyText.setHeight(100);
					dialog_Content.addView(emptyText);
				}
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 

		}

	}
}
class AnimationPickerDialog extends AppCompatDialog
{
    private final OnAnimSelectedListener onAnimSelectedListener;
    public interface OnAnimSelectedListener {
        void onAnimSelected(String name, String id);
    }

    public AnimationPickerDialog(Context context, String title, OnAnimSelectedListener onAnimSelectedListener) {
        super(context);
        this.onAnimSelectedListener = onAnimSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		String decodedTitle = title.replaceFirst("isList","");
		dialog_Title.setText(decodedTitle);
        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);

		final TextView once = new TextView(context);
		final BiggroupButton2 none = new BiggroupButton2(context);
		final BiggroupButton2 leftIn = new BiggroupButton2(context);
		final BiggroupButton2 rightIn = new BiggroupButton2(context);
		final BiggroupButton2 bounce = new BiggroupButton2(context);
		final BiggroupButton2 slideDown = new BiggroupButton2(context);
		final BiggroupButton2 fadeIn = new BiggroupButton2(context);
		final TextView onGoing = new TextView(context);
		final BiggroupButton2 blink = new BiggroupButton2(context);
		final BiggroupButton2 rotate = new BiggroupButton2(context);
		final BiggroupButton2 shake = new BiggroupButton2(context);
		once.setText(context.getString(R.string.editor_unstable_animations));
		onGoing.setText(context.getString(R.string.editor_stable_animations));
		none.setText(context.getString(R.string.editor_no_animation));
		rightIn.setText(context.getString(R.string.editor_anim_right_in));
		leftIn.setText(context.getString(R.string.editor_anim_left_in));
		fadeIn.setText(context.getString(R.string.editor_anim_fade_in));
		slideDown.setText(context.getString(R.string.editor_anim_slide_down));
		bounce.setText(context.getString(R.string.editor_anim_bounce));
		blink.setText(context.getString(R.string.editor_anim_blinking));
		rotate.setText(context.getString(R.string.editor_anim_rotation));
		shake.setText(context.getString(R.string.editor_anim_shake));
		dialog_Content.addView(none);
		dialog_Content.addView(once);
		dialog_Content.addView(rightIn);
		dialog_Content.addView(leftIn);
		dialog_Content.addView(fadeIn);
		dialog_Content.addView(slideDown);
		dialog_Content.addView(bounce);
		if(title.startsWith("isList")){

		}else{
			dialog_Content.addView(onGoing);
			dialog_Content.addView(blink);
			dialog_Content.addView(rotate);
			dialog_Content.addView(shake);
		}
		none.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = none.getText().toString();
					String anim = "none";
					AnimationPickerDialog.this.onAnimSelectedListener.onAnimSelected(name,anim);
					dismiss();					
				}
			});
		rightIn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = rightIn.getText().toString();
					String anim = "right_in";
					AnimationPickerDialog.this.onAnimSelectedListener.onAnimSelected(name,anim);
					dismiss();
				}
			});
		leftIn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = leftIn.getText().toString();
					String anim = "left_in";
					AnimationPickerDialog.this.onAnimSelectedListener.onAnimSelected(name,anim);
					dismiss();
				}
			});
		fadeIn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = fadeIn.getText().toString();
					String anim = "fade_in";
					AnimationPickerDialog.this.onAnimSelectedListener.onAnimSelected(name,anim);
					dismiss();
				}
			});
		slideDown.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = slideDown.getText().toString();
					String anim = "slide_down";
					AnimationPickerDialog.this.onAnimSelectedListener.onAnimSelected(name,anim);
					dismiss();
				}
			});
		bounce.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = bounce.getText().toString();
					String anim = "bounce";
					AnimationPickerDialog.this.onAnimSelectedListener.onAnimSelected(name,anim);
					dismiss();
				}
			});
		blink.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = blink.getText().toString();
					String anim = "blink";
					AnimationPickerDialog.this.onAnimSelectedListener.onAnimSelected(name,anim);
					dismiss();
				}
			});
		rotate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = rotate.getText().toString();
					String anim = "rotation";
					AnimationPickerDialog.this.onAnimSelectedListener.onAnimSelected(name,anim);
					dismiss();
				}
			});
		shake.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = shake.getText().toString();
					String anim = "shake";
					AnimationPickerDialog.this.onAnimSelectedListener.onAnimSelected(name,anim);
					dismiss();
				}
			});
	}

}
class MarginPickerDialog extends AppCompatDialog
{
    private final OnDipSelectedListener onDipSelectedListener;
    public interface OnDipSelectedListener {
        void onDipSelected(int topMargin, int bottomMargin, int rightMargin, int leftMargin);
    }

    public MarginPickerDialog(Context context, String title, int topMargin, int bottomMargin, int rightMargin, int leftMargin, OnDipSelectedListener onDipSelectedListener) {
        super(context);
        this.onDipSelectedListener = onDipSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
		dialog_Title.setText(title);
        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);
		LinearLayout dialog_Bottom = (LinearLayout) findViewById(R.id.dialogBottom);
		Button dialog_Save = findViewById(R.id.save);
		Button dialog_Cancel = findViewById(R.id.cancel);
		dialog_Bottom.setVisibility(View.VISIBLE);

		final TextView text1Text = new TextView(context);
		text1Text.setText(context.getString(R.string.editor_top_margin));
		final BiggroupSeekbar BiggroupSeekbar1 = new BiggroupSeekbar(context);
		final TextView text2Text = new TextView(context);
		text2Text.setText(context.getString(R.string.editor_bottom_margin));
		final BiggroupSeekbar BiggroupSeekbar2 = new BiggroupSeekbar(context);
		final TextView text3Text = new TextView(context);
		text3Text.setText(context.getString(R.string.editor_right_margin));
		final BiggroupSeekbar BiggroupSeekbar3 = new BiggroupSeekbar(context);
		final TextView text4Text = new TextView(context);
		text4Text.setText(context.getString(R.string.editor_left_margin));
		final BiggroupSeekbar BiggroupSeekbar4 = new BiggroupSeekbar(context);

		dialog_Content.addView(text1Text);
		dialog_Content.addView(BiggroupSeekbar1);
		dialog_Content.addView(text2Text);
		dialog_Content.addView(BiggroupSeekbar2);
		dialog_Content.addView(text3Text);
		dialog_Content.addView(BiggroupSeekbar3);
		dialog_Content.addView(text4Text);
		dialog_Content.addView(BiggroupSeekbar4);

		BiggroupSeekbar1.setMax(100);
		BiggroupSeekbar1.setProgress(topMargin);

		BiggroupSeekbar2.setMax(100);
		BiggroupSeekbar2.setProgress(bottomMargin);

		BiggroupSeekbar3.setMax(100);
		BiggroupSeekbar3.setProgress(rightMargin);

		BiggroupSeekbar4.setMax(100);
		BiggroupSeekbar4.setProgress(leftMargin);
		dialog_Cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();

				}
			});
		dialog_Save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {												
					int topMargin1 = BiggroupSeekbar1.getProgress();
					int bottomMargin1 = BiggroupSeekbar2.getProgress();
					int rightMargin1 = BiggroupSeekbar3.getProgress();
					int leftMargin1 = BiggroupSeekbar4.getProgress();
					MarginPickerDialog.this.onDipSelectedListener.onDipSelected(topMargin1,bottomMargin1,rightMargin1,leftMargin1);
					dismiss();

				}
			});

	}
}
class ActionPickerDialog extends AppCompatDialog
{
    String OnClickForChange;
    private final OnActionSelectedListener onActionSelectedListener;
    public interface OnActionSelectedListener {
        void onActionSelected(String action);
    }

    public ActionPickerDialog(Context context, String title, String OnClickForChange1, OnActionSelectedListener onActionSelectedListener)
	{
        super(context);
        this.onActionSelectedListener = onActionSelectedListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog);
		TextView dialog_Title = (TextView) findViewById(R.id.dialogTitle);
		dialog_Title.setVisibility(View.VISIBLE);
        LinearLayout dialog_Content = (LinearLayout) findViewById(R.id.dialogContent);
        OnClickForChange = OnClickForChange1;
		dialog_Title.setText(title);

		final BiggroupButton2 none = new BiggroupButton2(context);
		final BiggroupButton2 gotoPage = new BiggroupButton2(context);
		final BiggroupButton2 gotoList = new BiggroupButton2(context);
		final BiggroupButton2 showImage = new BiggroupButton2(context);
		final BiggroupButton2 playAudio = new BiggroupButton2(context);
		final BiggroupButton2 playVideo = new BiggroupButton2(context);
		final BiggroupButton2 showAlert = new BiggroupButton2(context);
		final BiggroupButton2 showToast = new BiggroupButton2(context);
		final BiggroupButton2 sendEmail = new BiggroupButton2(context);
		final BiggroupButton2 sendSms = new BiggroupButton2(context);
		final BiggroupButton2 openLink = new BiggroupButton2(context);
		//	final BiggroupButton2 shareApp = new BiggroupButton2(context);
		final BiggroupButton2 exit = new BiggroupButton2(context);
		none.setText(context.getString(R.string.editor_no_action));
		gotoList.setText(context.getString(R.string.editor_action_open_list));
		gotoPage.setText(context.getString(R.string.editor_action_open_page));
		playVideo.setText(context.getString(R.string.editor_action_play_video));
		playAudio.setText(context.getString(R.string.editor_action_play_audio));
		showImage.setText(context.getString(R.string.editor_action_show_pic));
		showAlert.setText(context.getString(R.string.editor_action_show_alert));
		showToast.setText(context.getString(R.string.editor_action_show_toast));
		sendEmail.setText(context.getString(R.string.editor_action_send_mail));
		sendSms.setText(context.getString(R.string.editor_action_send_sms));
		openLink.setText(context.getString(R.string.editor_action_open_link));
		//	shareApp.setText(context.getString(R.string.editor_action_send_app));
		exit.setText(context.getString(R.string.editor_action_exit));
		dialog_Content.addView(none);
		dialog_Content.addView(gotoList);
		dialog_Content.addView(gotoPage);
		dialog_Content.addView(playVideo);
		dialog_Content.addView(playAudio);
		dialog_Content.addView(showImage);
		dialog_Content.addView(showAlert);
		dialog_Content.addView(showToast);
		dialog_Content.addView(sendEmail);
		dialog_Content.addView(sendSms);
		dialog_Content.addView(openLink);
		//	dialog_Content.addView(shareApp);
		dialog_Content.addView(exit);
		none.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					String action = "none";
					ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
				}
			});
		gotoPage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					String title = context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ gotoPage.getText().toString();
					new PagePickerDialog(context, title, new OnPageSelectedListener() {
							public void onPageSelected(String fileName, String fileId) {
								String action = "GotoPage" + fileId;
								ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
							}
						}).show();
				}
			});
		gotoList.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					String title = context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ gotoList.getText().toString();
					new ListPickerDialog(context, title, new OnListSelectedListener() {
							public void onListSelected(String fileName, String fileId) {
								String action = "GotoList" + fileId;
								ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
							}
						}).show();
				}
			});
		playAudio.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					String title = context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ playAudio.getText().toString();
					new AudioPickerDialog(context, title, new OnAudioSelectedListener() {
							public void onAudioSelected(String fileName, String fileId) {
								String action = "PlayAudio" + fileId;
								ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
							}
						}).show();
				}
			});
		playVideo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					String title = context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ playVideo.getText().toString();
					new VideoPickerDialog(context, title, new OnVideoSelectedListener() {
							public void onVideoSelected(String fileName, String fileId) {
								String action = "PlayVideo" + fileId;
								ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
							}
						}).show();
				}
			});
		showImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					String title = context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ showImage.getText().toString();
					new ImagePickerDialog(context, title, new OnImageSelectedListener() {
							public void onImageSelected(String fileName, String fileId) {
								String action = "ShowImage" + fileId;
								ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
							}
						}).show();
				}
			});
		showToast.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					final EditorDialog dialog = new EditorDialog(context);
					TextView dialog_Title = dialog.getTitle();
					LinearLayout dialog_Content = dialog.getContent();
					dialog_Title.setText(context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ showToast.getText().toString());

					LinearLayout dialog_Bottom = (LinearLayout) dialog
						.findViewById(R.id.dialogBottom);
					TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
					TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
					dialog_Bottom.setVisibility(View.VISIBLE);
					final EditText toastText = new EditText(context);
					toastText.setHint(context.getString(R.string.editor_action_toast_text));
					if (OnClickForChange.startsWith("ShowToast")) {
						toastText.setText(OnClickForChange.replaceFirst("ShowToast", ""));
					}
					dialog_Content.addView(toastText);
					dialog_Cancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();

							}
						});
					dialog_Save.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (toastText.getText().toString().length() == 0) {
									toastText.setError(context.getString(R.string.editor_action_error_toast));
								} else {
									String action = "ShowToast" + toastText.getText().toString();
									dialog.dismiss();
									ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
								}
							}
						});
					dialog.show();
				}
			});
		showAlert.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					final EditorDialog dialog = new EditorDialog(context);
					TextView dialog_Title = dialog.getTitle();
					LinearLayout dialog_Content = dialog.getContent();
					dialog_Title.setText(context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ showAlert.getText().toString());

					LinearLayout dialog_Bottom = (LinearLayout) dialog
						.findViewById(R.id.dialogBottom);
					TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
					TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
					dialog_Bottom.setVisibility(View.VISIBLE);
					final EditText titleText = new EditText(context);
					final EditText textText = new EditText(context);
					titleText.setHint(context.getString(R.string.editor_action_alert_title));
					textText.setHint(context.getString(R.string.editor_action_alert_text));
					if (OnClickForChange.startsWith("ShowAlert")) {
						String str = OnClickForChange.replaceFirst("ShowAlert", "");
						String[] message = str.split("<!>");
						titleText.setText(message[0].replaceFirst("Title", ""));
						textText.setText(message[1].replaceFirst("Text", ""));
					}
					dialog_Content.addView(titleText);
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
								if (titleText.getText().toString().length() == 0
									| textText.getText().toString().length() == 0) {
									titleText.setError(context.getString(R.string.editor_action_error_alert_title));
									textText.setError(context.getString(R.string.editor_action_error_alert_text));
								} else {
									String action = "ShowAlertTitle"
										+ titleText.getText().toString() + "<!>Text"
										+ textText.getText().toString();
									ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
									dialog.dismiss();
								}
							}
						});
					dialog.show();
				}
			});
		sendEmail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					final EditorDialog dialog = new EditorDialog(context);
					TextView dialog_Title = dialog.getTitle();
					LinearLayout dialog_Content = dialog.getContent();
					dialog_Title.setText(context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ sendEmail.getText().toString());

					LinearLayout dialog_Bottom = (LinearLayout) dialog
						.findViewById(R.id.dialogBottom);
					TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
					TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
					dialog_Bottom.setVisibility(View.VISIBLE);
					final EditText editText = new EditText(context);
					editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
					editText.setHint(context.getString(R.string.editor_action_email_address));
					if (OnClickForChange.startsWith("SendEmail")) {
						editText.setText(OnClickForChange.replaceFirst("SendEmail", ""));
					}
					dialog_Content.addView(editText);
					dialog_Cancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
					dialog_Save.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (editText.getText().toString().length() == 0) {
									editText.setError(context.getString(R.string.editor_action_error_email));
								} else {
									if (checkEmailCorrect(editText.getText().toString())) {
										String action = "SendEmail"
											+ editText.getText().toString();
										ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
										dialog.dismiss();
									} else {
										editText.setError(context.getString(R.string.editor_action_error_incorrect_email));
									}
								}
							}
						});
					dialog.show();
				}
			});
		sendSms.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					final EditorDialog dialog = new EditorDialog(context);
					TextView dialog_Title = dialog.getTitle();
					LinearLayout dialog_Content = dialog.getContent();
					dialog_Title.setText(context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ sendSms.getText().toString());
					LinearLayout dialog_Bottom = (LinearLayout) dialog
						.findViewById(R.id.dialogBottom);
					TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
					TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
					dialog_Bottom.setVisibility(View.VISIBLE);
					final EditText editText = new EditText(context);
					editText.setInputType(InputType.TYPE_CLASS_PHONE);
					editText.setHint(context.getString(R.string.editor_action_phone_number));
					if (OnClickForChange.startsWith("SendSms")) {
						editText.setText(OnClickForChange.replaceFirst("SendSms", ""));
					}
					dialog_Content.addView(editText);
					dialog_Cancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
					dialog_Save.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (editText.getText().toString().length() == 0) {
									editText.setError(context.getString(R.string.editor_action_error_phone_number));
								} else {
									String action = "SendSms" + editText.getText().toString();
									ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
									dialog.dismiss();
								}
							}
						});

					dialog.show();
				}
			});
		openLink.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					final EditorDialog dialog = new EditorDialog(context);
					TextView dialog_Title = dialog.getTitle();
					LinearLayout dialog_Content = dialog.getContent();
					dialog_Title.setText(context.getString(R.string.editor_action)+context.getString(R.string.a_split)+ openLink.getText().toString());

					LinearLayout dialog_Bottom = (LinearLayout) dialog
						.findViewById(R.id.dialogBottom);
					TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
					TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
					dialog_Bottom.setVisibility(View.VISIBLE);
					final EditText editText = new EditText(context);
					editText.setHint(context.getString(R.string.editor_action_link));
					if (OnClickForChange.startsWith("OpenLink")) {
						editText.setText(OnClickForChange.replaceFirst("OpenLink", ""));
					}
					dialog_Content.addView(editText);
					dialog_Cancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();

							}
						});
					dialog_Save.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (editText.getText().toString().length() == 0) {
									editText.setError(context.getString(R.string.editor_action_error_link));
								} else {
									String action = "OpenLink" + editText.getText().toString();
									ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
									dialog.dismiss();
								}
							}
						});

					dialog.show();
				}
			});
		exit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String action = "Exit";
					ActionPickerDialog.this.onActionSelectedListener.onActionSelected(action);
					dismiss();
				}
			});
	}
	private boolean checkEmailCorrect(String email) {
		boolean correct = true;
		String wrongEmailNumbers = "";
		if (!isEmailValid(email)) {
			wrongEmailNumbers = wrongEmailNumbers + 1;
		}
		if (!wrongEmailNumbers.equals("")) {
			char[] wrongNumbers = wrongEmailNumbers.toCharArray();
			wrongEmailNumbers = "";
			if (wrongNumbers.length == 1) {
				wrongEmailNumbers = wrongEmailNumbers + wrongNumbers[0];
			} else {
				for (int i = 0; i < wrongNumbers.length - 2; i++) {
					wrongEmailNumbers = wrongEmailNumbers + wrongNumbers[i] + " ← ";
				}
				wrongEmailNumbers = wrongEmailNumbers + wrongNumbers[wrongNumbers.length - 2] + " و "
					+ wrongNumbers[wrongNumbers.length - 1];
			}
			correct = false;
		}
		return correct;
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
}