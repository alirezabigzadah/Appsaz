package af.appsaz.myapplication;

import af.alireza.appsaz.R;
import af.alireza.appsaz.Utilities.Utilities;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VideoActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback
{
	private static final String TAG = "VideoPlayer";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;
    View mVideoLayout;
	String videoName;
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
	private String saveFile;
	private String shareFile;
	private String videoTitle;
    Utilities utils = new Utilities();
    String rootPath;
	String imagesPath="files/images/";
	String videosPath="files/videos/";
    private RelativeLayout   toolBar;
	private ImageView        back;
    private TextView         title;
    private ImageView        share;
	private ImageView        save;
    private String           actionBarColor;

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
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		rootPath = getExternalFilesDir(null).getPath()+"/Project/MyApp/assets/";
        toolBar = (RelativeLayout) findViewById(R.id.toolBar);
		back = (ImageView) findViewById(R.id.back);
		share = (ImageView) findViewById(R.id.share);
		save = (ImageView) findViewById(R.id.save);
		title = (TextView) findViewById(R.id.title);
        mVideoLayout = findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
		videoName = getIntent().getStringExtra("videoName");
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

		String str = videoName;
		String Info = utils.readFile(rootPath+videosPath+"videos");
		String items = new String(Info);
		String[] lists = items.split("\\!\\#");
		for (String p1 : lists) {
			String p=p1;

			if (p.startsWith("{Video<=>"+str.replace("video",""))) {
				String[] code = p.replace("}", "").split("<=>");
				String ItemeId = code[1];
				String titleFile = code[2].replaceFirst("Title","");
				String file = code[3].replace("Video","");
				saveFile = code[4].replaceFirst("SaveAble","");
				shareFile = code[5].replaceFirst("ShareAble","");
				videoTitle = titleFile;
				title.setText(videoTitle);

                if(shareFile.equals("True")){
					share.setVisibility(View.VISIBLE);
					share.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 14);
  
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
                                            ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 14);
  
                                            }else{
                                                save();
                                            }
                                        }else{
                                            save();
                                        }
							}});
				}
			}
		}
		back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		if (new File(rootPath + videosPath + videoName.toString()).exists()) {
			mVideoView.setMediaController(mMediaController);
			setVideoAreaSize();
			mVideoView.setVideoViewCallback(this);
			mVideoView.start();
			return;
		}
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					Log.d(TAG, "onCompletion ");
				}
			});

    }
	@Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }
	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		if (mSeekPosition > 0) {
			mVideoView.seekTo(mSeekPosition);
		}
		mVideoView.start();
	}

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
				@Override
				public void run() {
					int width = mVideoLayout.getWidth();
					cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
					ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
					videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
					videoLayoutParams.height = cachedHeight;
					mVideoLayout.setLayoutParams(videoLayoutParams);
					mVideoView.setVideoURI(Uri.fromFile(new File(rootPath + videosPath + videoName)));
					mVideoView.requestFocus();
					mMediaController.setTitle(videoTitle);
				}
			});
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }
    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        switchTitleBar(isFullscreen);
    }
    private void switchTitleBar(boolean isFullscreen) {
        if (toolBar != null) {
            if (isFullscreen) {
            toolBar.setVisibility(View.GONE);
            getWindow().getDecorView().setSystemUiVisibility(flags1);
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
        } else {
           toolBar.setVisibility(View.VISIBLE);
            getWindow().getDecorView().setSystemUiVisibility(flags2);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);

        }
        }
    }
    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
    }
    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onStart UniversalVideoView callback");
    }
    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }
    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }
@Override
	public void onBackPressed() {
		if(isFullscreen){
            mVideoView.setFullscreen(false);
        }else{
            finish();
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
        File file = new File(utils.saveMediaFile(VideoActivity.this, rootPath+videosPath + videoName, videoTitle, "video"));
        utils.shareFile(VideoActivity.this, file);
    }
    private void save(){
        utils.saveMediaFile(VideoActivity.this, rootPath+videosPath + videoName, videoTitle, "video");
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