package af.alireza.views;

import android.annotation.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import af.alireza.appsaz.*;

public class BiggroupSeekbar extends RelativeLayout {
  private LinearLayout bg;
  private Context context;
  private TextView text;
  private SeekBar Seekbar;
  private Button p;
  private Button n;
  private OnSeekBarChangeListener mPublicChangeListener;

  public BiggroupSeekbar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }

  public BiggroupSeekbar(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    init();
  }

  public BiggroupSeekbar(Context context) {
    super(context);
    this.context = context;
    init();
  }

  public void init() {
    inflate(context, R.layout.biggroup_seekbar, this);
    bg = (LinearLayout) findViewById(R.id.bigSbBg);
    text = (TextView) findViewById(R.id.bigSbText);
    Seekbar = (SeekBar) findViewById(R.id.bigSeekbar);
    n = (Button) findViewById(R.id.bigSbN);
    p = (Button) findViewById(R.id.bigSbP);
    text.setText(String.valueOf(Seekbar.getProgress()+"%"));
    p.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View v) {
            Seekbar.setProgress(Seekbar.getProgress() + 1);
            text.setText(String.valueOf(Seekbar.getProgress()+"%"));
          }
        });
    n.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View v) {
            Seekbar.setProgress(Seekbar.getProgress() - 1);
            text.setText(String.valueOf(Seekbar.getProgress()+"%"));
          }
        });
    Seekbar.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            notifyProgress(progress, fromUser);
            text.setText(String.valueOf(progress)+"%");
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {
            startDragging();
          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
            stopDragging();
          }
        });
  }

  public int getProgress() {
    int progress = Seekbar.getProgress();
    return progress;
  }

  public void setProgress(int progress) {
    Seekbar.setProgress(progress);
    text.setText(String.valueOf(Seekbar.getProgress())+"%");
  }

  public void setMax(int max) {
    Seekbar.setMax(max);
  }

  public void setEnabled(boolean enabled) {
    if (enabled) {
      bg.setAlpha(1);
      text.setAlpha(1);
      n.setAlpha(1);
      p.setAlpha(1);
      Seekbar.setEnabled(true);
    } else {
      bg.setAlpha(0.5f);
      text.setAlpha(0.5f);
      n.setAlpha(0.5f);
      p.setAlpha(0.5f);
      Seekbar.setEnabled(false);
    }
  }

  public interface OnSeekBarChangeListener {
    void OnProgressChanged(BiggroupSeekbar biggroupSeekBar, int i, boolean z);

    void onStartTrackingTouch(BiggroupSeekbar biggroupSeekBar);

    void onStopTrackingTouch(BiggroupSeekbar biggroupSeekBar);
  }

  public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
    this.mPublicChangeListener = listener;
  }

  private void notifyProgress(int value, boolean fromUser) {
    if (this.mPublicChangeListener != null) {
      this.mPublicChangeListener.OnProgressChanged(this, value, fromUser);
    }
  }

  private void startDragging() {
    if (this.mPublicChangeListener != null) {
      this.mPublicChangeListener.onStartTrackingTouch(this);
    }
  }

  private void stopDragging() {
    if (this.mPublicChangeListener != null) {
      this.mPublicChangeListener.onStopTrackingTouch(this);
    }
  }
}
