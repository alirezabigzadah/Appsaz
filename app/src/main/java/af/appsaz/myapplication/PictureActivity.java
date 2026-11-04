package af.appsaz.myapplication;

import af.alireza.appsaz.R;
import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.PinchImageView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PictureActivity extends AppCompatActivity
{
    private PinchImageView ImgView;
	private InputStream inputStream;
	private RelativeLayout toolBar;
	private ImageView back;
	private TextView title;
	private ProgressBar loadProgress;
	private String rootPath;
	private String iconsPath="files/icons/";
	private String imagesPath="files/images/";
	private ImageView share;
	private ImageView save;
    private boolean isFullscreen;
	private String imgTitle;
	private String imgName;
    Utilities utils = new Utilities();

    int flags1 = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	| View.SYSTEM_UI_FLAG_FULLSCREEN
	| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
	int flags2 = View.SYSTEM_UI_FLAG_VISIBLE;

	int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		rootPath = getExternalFilesDir(null).getPath()+"/Project/MyApp/assets/";
		toolBar = (RelativeLayout) findViewById(R.id.toolBar);
		back = (ImageView) findViewById(R.id.back);
		share = (ImageView) findViewById(R.id.share);
		save = (ImageView) findViewById(R.id.save);
		title = (TextView) findViewById(R.id.title);
		loadProgress = (ProgressBar) findViewById(R.id.loadProgress);
		ImgView = (PinchImageView) findViewById(R.id.touchimgview);
		final String actionBarColor = getIntent().getExtras().getString("actionBarColor");
		imgName = getIntent().getExtras().getString("picName");
		if(actionBarColor.startsWith("#")){
            if(isColorDark(Color.parseColor(actionBarColor))){
				title.setTextColor(Color.WHITE);
				back.setColorFilter(Color.WHITE);
				share.setColorFilter(Color.WHITE);
				save.setColorFilter(Color.WHITE);
			} else {
				back.setColorFilter(Color.BLACK);
				title.setTextColor(Color.BLACK);
				share.setColorFilter(Color.BLACK);
				save.setColorFilter(Color.BLACK);
			}
			toolBar.setBackgroundColor(Color.parseColor(actionBarColor));
			int color = darkenColor(Color.parseColor(actionBarColor));
			getWindow().setStatusBarColor(color);
		}
		if(imgName.startsWith("isIcon")){
			imgName=imgName.replaceFirst("isIcon","");
			String str = imgName;
			String Info = utils.readFile(rootPath+iconsPath+"icons");
			String items = new String(Info);
			String[] lists = items.split("\\!\\#");
			for (String p1 : lists) {
				String p=p1;
				if (p.startsWith("{Icon<=>"+str.replace("icon",""))) {
					String[] code = p.replace("}", "").split("<=>");
					String ItemeId = code[1];
					String titleFile = code[2].replaceFirst("Title","");
					String file = code[3].replace("Img","");
					title.setText(titleFile);
				}}
			File In = new File(rootPath+iconsPath + imgName);
			Glide.with(PictureActivity.this)
                .load(In)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.none)
                .into(ImgView);
		}else{
			String str = imgName;
			String Info = utils.readFile(rootPath+imagesPath+"images");
			String items = new String(Info);
			String[] lists = items.split("\\!\\#");
			for (String p1 : lists) {
				String p=p1;
				if (p.startsWith("{Image<=>"+str.replace("image",""))) {
					String[] code = p.replace("}", "").split("<=>");
					String ItemeId = code[1];
					String titleFile = code[2].replaceFirst("Title","");
					String file = code[3].replace("Img","");
					title.setText(titleFile);
					String saveFile = code[4].replaceFirst("SaveAble","");
					String shareFile = code[5].replaceFirst("ShareAble","");
					imgTitle = titleFile;
					title.setText(imgTitle);
					if(shareFile.equals("True")){
						share.setVisibility(View.VISIBLE);
						share.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(PictureActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 14);
  
                                            }else{
                                                share();
                                            }
                                        }else{
                                            share();
                                        }
								}
							});
					}
					if(saveFile.equals("True")){
						save.setVisibility(View.VISIBLE);
						save.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
                                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(PictureActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 14);
  
                                            }else{
                                                save();
                                            }
                                        }else{
                                            save();
                                        }
								}});
					}}}
			File In = new File(rootPath+imagesPath + imgName);
			Glide.with(PictureActivity.this)
                .load(In)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.none)
                .into(ImgView);
		}
		back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		ImgView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
                    toggleFullscreen();
				}
			});

		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					loadProgress.setVisibility(View.GONE);
				}
			},1000);
	}
    public boolean isColorDark(int color){
		double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
		if(darkness<0.5){
			return false; // It's a light color
		}else{
			return true; // It's a dark color
		}
	}
	private void copy(String in ,String out) throws IOException {
        OutputStream myOutput = new FileOutputStream(out);
        byte[] buffer = new byte[1024];
        FileInputStream InputFile = new FileInputStream(rootPath+in);
		InputStream myInput = InputFile;
        while (true) {
            int lenght = myInput.read(buffer);
            if (lenght <= 0) {
                myInput.close();
                myOutput.flush();
                myOutput.close();
                return;
            }
            myOutput.write(buffer, 0, lenght);
        }
    }
    private void toggleFullscreen() {
        if (isFullscreen) {
            // Exit fullscreen
			toolBar.setVisibility(View.VISIBLE);
			getWindow().getDecorView().setSystemUiVisibility(flags2);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			//   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			//  fullscreenButton.setText("Toggle Fullscreen");
            isFullscreen = false;
        } else {
            // Enter fullscreen
			toolBar.setVisibility(View.GONE);
            getWindow().getDecorView().setSystemUiVisibility(flags1);
			//  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			//  fullscreenButton.setText("Exit Fullscreen");
            isFullscreen = true;
        }
    }
private void share(){
        File file = new File(utils.saveMediaFile(PictureActivity.this, rootPath+imagesPath + imgName, imgTitle, "image"));
        utils.shareFile(PictureActivity.this, file);
    }
    private void save(){
        utils.saveMediaFile(PictureActivity.this, rootPath+imagesPath + imgName, imgTitle, "image");
    }
    @Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == 14) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // مجوز داده شده است
            save();
        } else {
            // مجوز رد شده است
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
 }
}