package af.appsaz.myapplication;

import af.alireza.appsaz.R;
import af.alireza.appsaz.Utilities.Prefs;
import af.alireza.appsaz.Utilities.Utilities;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import jp.wasabeef.glide.transformations.BlurTransformation;
import org.romancha.playpause.PlayPauseView;

public class AudioActivity extends AppCompatActivity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {

	private ImageView        imgAudio;
	private ImageView        imgBgAudio;
	private ImageView        btnForward;
	private ImageView        btnBackward;
    private ImageView        btnRepeat;
	private AppCompatSeekBar songProgressBar;
    private String           actionBarColor;
	private TextView         songCurrentDurationLabel;
	private TextView         songTotalDurationLabel;
	private MediaPlayer      mp;
	private int              seekForwardTime = 5000;
	private int              seekBackwardTime = 5000;
	private String           audioTitle;
	private String           audioName;
	private String           audioImageName;
    private Utilities        utils = new Utilities();
    private Handler          mHandler = new Handler();
    private String           rootPath;
	private String           imagesPath="files/images/";
	private String           audiosPath="files/audios/";
	private PlayPauseView    playIconDrawable;
	private String           saveFile;
	private String           shareFile;
    private RelativeLayout   toolBar;
	private ImageView        back;
    private TextView         title;
    private ImageView        share;
	private ImageView        save;

	int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
        View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        rootPath = getExternalFilesDir(null).getPath()+"/Project/MyApp/assets/";
        toolBar = (RelativeLayout) findViewById(R.id.toolBar);
		back = (ImageView) findViewById(R.id.back);
		share = (ImageView) findViewById(R.id.share);
		save = (ImageView) findViewById(R.id.save);
		title = (TextView) findViewById(R.id.title);

		imgBgAudio = (ImageView) findViewById(R.id.songBgImage);
		imgAudio = (ImageView) findViewById(R.id.songImage);
		playIconDrawable = (PlayPauseView) findViewById(R.id.btnPlay);
		btnForward = (ImageView) findViewById(R.id.btnForward);
		btnBackward = (ImageView) findViewById(R.id.btnBackward);
        btnRepeat = (ImageView) findViewById(R.id.btnRepeat);
		songProgressBar = (androidx.appcompat.widget.AppCompatSeekBar) findViewById(R.id.songProgressBar);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		audioName = getIntent().getStringExtra("audioName");
	    actionBarColor = getIntent().getExtras().getString("actionBarColor");

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
		String str = audioName;
		try
		{
			FileInputStream InputFile = new FileInputStream(rootPath+audiosPath+"audios");
			InputStream input = InputFile;
			int s = input.available();
			byte[] b = new byte[s];
			input.read(b);
			input.close();
			String items = new String(b);
			String[] lists = items.split("\\!\\#");
			for (String p1 : lists) {
				String p=p1;
				if (p.startsWith("{Audio<=>"+str.replace("audio",""))) {
					String[] code = p.replace("}", "").split("<=>");
					String ItemeId = code[1];
					String titleFile = code[2].replaceFirst("Title","");
					String file = code[3].replace("Audio","");
					String fileImg = code[4].replace("AudioImg","");
					saveFile = code[5].replaceFirst("SaveAble","");
					shareFile = code[6].replaceFirst("ShareAble","");
					audioTitle = titleFile;
					audioImageName = fileImg;
					title.setText(audioTitle);
					if(audioImageName.startsWith("image")){
						File In = new File(rootPath+imagesPath + audioImageName);
                        Glide.with(AudioActivity.this)
							.load(In)
							.skipMemoryCache(true)
							.placeholder(R.drawable.none)
							.into(imgAudio);
                        Glide.with(AudioActivity.this)
							.load(In)
							.skipMemoryCache(true)
							.transform(new BlurTransformation(200))
							.placeholder(R.drawable.none)
							.into(imgBgAudio);
					}else{
                        int In = R.drawable.default_audio_img;
						Glide.with(AudioActivity.this)
							.load(In)
							.skipMemoryCache(true)
							.placeholder(R.drawable.none)
							.into(imgAudio);
                        Glide.with(AudioActivity.this)
							.load(In)
							.skipMemoryCache(true)
							.transform(new BlurTransformation(200))
							.placeholder(R.drawable.none)
							.into(imgBgAudio);
					}
                    if(shareFile.equals("True")){
						share.setVisibility(View.VISIBLE);
						share.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
                                    
                                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(AudioActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 14);
  
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
                                            ActivityCompat.requestPermissions(AudioActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 14);
  
                                            }else{
                                                save();
                                            }
                                        }else{
                                            save();
                                        }
								}});
					}
				}}
		}
		catch (IOException e)
		{}

		mp = new MediaPlayer();
		utils = new Utilities();
		playSong(audioName);
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important
        loop();
        
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               // Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_SHORT).show();
               // clearMediaPlayer();
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        
		//	BarVisualizer barVisualizer = (BarVisualizer) findViewById(R.id.visualizer);
        // set custom color to the line.
		//  barVisualizer.setColor(Color.CYAN);
        // define custom number of bars you want in the visualizer between (10 - 256).
		//   barVisualizer.setDensity(70);
        // Set your media player to the visualizer.
		//  barVisualizer.setPlayer(mp.getAudioSessionId());
		//   playIconDrawable.setPlayBackgroundColor(Color.BLACK);
        back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		playIconDrawable.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					if(mp.isPlaying()){
						if(mp!=null){
							mp.pause();
							playIconDrawable.toggle(true);
						}
					}else{
						if(mp!=null){
							mp.start();
							playIconDrawable.toggle(true);
						}
					}

				}
			});

		btnForward.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					int currentPosition = mp.getCurrentPosition();
					if(currentPosition + seekForwardTime <= mp.getDuration()){
						mp.seekTo(currentPosition + seekForwardTime);
					}else{
						mp.seekTo(mp.getDuration());
					}
				}
			});

		btnBackward.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					int currentPosition = mp.getCurrentPosition();
					if(currentPosition - seekBackwardTime >= 0){
						mp.seekTo(currentPosition - seekBackwardTime);
					}else{
						mp.seekTo(0);
					}

				}
			});

        btnRepeat.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
                   if(Prefs.getBoolean(AudioActivity.this,"is_looping_music", false)){
                        Prefs.putBoolean(AudioActivity.this,"is_looping_music", false);
                        btnRepeat.setImageResource(R.drawable.ic_repeat_off);
                        }else{
                        Prefs.putBoolean(AudioActivity.this,"is_looping_music", true);
                        btnRepeat.setImageResource(R.drawable.ic_repeat_one);
                        }
				}
			});
	}

	public void start() {
		playSong(audioName);
	}

	public void  playSong(String audioName){
        playIconDrawable.toggle(true);
		try {
			if (new File(rootPath+audiosPath + audioName.toString()).exists()) {
				mp.reset();
				mp.setDataSource(rootPath+audiosPath + audioName);
				mp.prepare();
				mp.start();
				songProgressBar.setProgress(0);
				songProgressBar.setMax(100);
				updateProgressBar();
				return;
			}else{
				start();
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);        
    }	

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mp.getDuration();
			long currentDuration = mp.getCurrentPosition();
			songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
			int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			Log.d("Progress", ""+progress);
			songProgressBar.setProgress(progress);
			mHandler.postDelayed(this, 100);
		}
	};

	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

		mHandler.removeCallbacks(mUpdateTimeTask);
    }

	@Override
    public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
		mp.seekTo(currentPosition);
		updateProgressBar();
    }

	@Override
	public void onCompletion(MediaPlayer arg0) {
		playIconDrawable.toggle(true);
        if(Prefs.getBoolean(AudioActivity.this,"is_looping_music",false)){
            mp.start();
            playIconDrawable.toggle(true);
        }
	}
	@Override
	protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
	}
    private void clearMediaPlayer() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
            mHandler.removeCallbacks(mUpdateTimeTask);
            }
    }
    private void loop(){
        if(Prefs.getBoolean(AudioActivity.this,"is_looping_music", false)){
            btnRepeat.setImageResource(R.drawable.ic_repeat_one);
            }else{
            btnRepeat.setImageResource(R.drawable.ic_repeat_off);
            }
    }
    public boolean isColorDark(int color){
		double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
		if(darkness<0.5){
			return false; // It's a light color
		}else{
			return true; // It's a dark color
		}
	}
    private void share(){
        File file = new File(utils.saveMediaFile(AudioActivity.this, rootPath+audiosPath + audioName, audioTitle, "audio"));
        utils.shareFile(AudioActivity.this, file);
    }
    private void save(){
        utils.saveMediaFile(AudioActivity.this, rootPath+audiosPath + audioName, audioTitle, "audio");
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
 