package af.appsaz.myapplication;

import af.alireza.appsaz.R;
import af.alireza.appsaz.Utilities.Utilities;
import af.appsaz.myapplication.utilities.ContentsReader;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity 
{
	String Info;
	LinearLayout contentlinear;
	RelativeLayout BgLayout;
	String imagesPath="files/images/";
	String iconsPath="files/icons/";
	String fontsPath="files/fonts/";
	Utilities utils = new Utilities();
	ContentsReader reader = new ContentsReader(this);
	String data = "splash_content";
    private int splashDur;
	private String splashGoto;
	private String splashData;
	private String rootAssetsPath;
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
         rootAssetsPath = getExternalFilesDir(null).getPath()+"/Project/MyApp/assets/";
        
		  final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		getWindow().getDecorView().setSystemUiVisibility(flags);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		contentlinear = (LinearLayout) findViewById(R.id.splashContent);
		BgLayout = (RelativeLayout) findViewById(R.id.bgLayout);
		String Info = utils.readFile(rootAssetsPath+data);
		final String Content = new String(Info);
		Info = Content;	
		final String[] contentarray = Content.split("\\!\\#");
		for (String part : contentarray) {
			String part2=part;
			if (part2.contains("{Splash<=>")) {
				String[] code = part2.replace("}", "").split("<=>");
				String bg = code[1].replace("Background","");
				if (bg.startsWith("Color")) {
					bg=bg.replace("Color","");
					BgLayout.setBackgroundColor(Color.parseColor(bg));
				} else if(bg.startsWith("Image")) {
					bg=bg.replace("Image","");
					if(new File(rootAssetsPath + imagesPath + bg).exists()){
						try {
							FileInputStream In = new FileInputStream(rootAssetsPath + imagesPath + bg);
							InputStream inputStream = In;
							BgLayout.setBackgroundDrawable(Drawable.createFromStream(inputStream,""));
						} catch (FileNotFoundException e) {}
					}else{
						BgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
					}
				}else{
					BgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
				}

			}

		}
		reader.setContentsReader(rootAssetsPath+data,contentlinear, "#008577");
    final String data = utils.readFile(rootAssetsPath+"splash_settings");
			String splash = data;
	//		splash = splash.trim();
			String[] durarray = splash.split("\\!\\#");
			for (String part : durarray) {
				String part2=part;
				if (part2.contains("{SplashStetting<=>")) {
				    String dur = part2.replace("}", "").split("<=>")[1].replace("SplashDuration","");
					splashGoto = part2.replace("}", "").split("<=>")[2].replace("GoToWhere","");
					splashData = part2.replace("}", "").split("<=>")[3].replace("SendData","");
                    splashDur = Integer.parseInt(dur);
					if(splashData.replace("SendData","").startsWith("list")){
					if(new File(rootAssetsPath+""+splashData).exists()){
						splashData = splashData;
					}else{
						splashGoto = "none";
						splashData = "none";
						}
						}
					if(splashData.replace("SendData","").startsWith("page")){
						if(new File(rootAssetsPath+""+splashData).exists()){
							splashData = splashData;
						}else{
							splashGoto = "none";
							splashData = "none";
						}
						}
				}
			}
            	if(splashDur==0){
					Start(0,splashGoto,splashData);
				}else if(splashDur==1){
					Start(1000,splashGoto,splashData);
				}else if(splashDur==2){
					Start(2000,splashGoto,splashData);
				}else if(splashDur==3){
					Start(3000,splashGoto,splashData);
				}else if(splashDur==4){
					Start(4000,splashGoto,splashData);
				}else if(splashDur==5){
					Start(5000,splashGoto,splashData);
				}else if(splashDur==6){
					Start(6000,splashGoto,splashData);
				}else if(splashDur==7){
					Start(7000,splashGoto,splashData);
				}else if(splashDur==8){
					Start(8000,splashGoto,splashData);
				}else if(splashDur==9){
					Start(9000,splashGoto,splashData);
				}else if(splashDur==10){
					Start(10000,splashGoto,splashData);
				}
				
            }
        private void Start(int SplashDuration,final String gotoWher,final String sendData) {
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if(gotoWher.equals("Page")){
						if(new File(rootAssetsPath+sendData).exists()){
						Intent intent = new Intent(SplashActivity.this, PageActivity.class);
						intent.putExtra("isFirstPage","Yes");
						intent.putExtra("pageName",sendData);
						startActivity(intent);
						finish();
						}
					}else if(gotoWher.equals("List")){
						if(new File(rootAssetsPath+sendData).exists()){
						Intent intent = new Intent(SplashActivity.this, ListActivity.class);
						intent.putExtra("isFirstPage","Yes");
						intent.putExtra("listName",sendData);
						startActivity(intent);
						finish();
						}
					}
					
				}
			},SplashDuration);
		}
}