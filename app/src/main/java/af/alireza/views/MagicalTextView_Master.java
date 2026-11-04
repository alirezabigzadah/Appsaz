package af.alireza.views;

import android.annotation.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Bitmap.*;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter.*;
import android.graphics.Paint.*;
import android.graphics.PorterDuff.*;
import android.graphics.drawable.*;
import android.text.*;
import android.util.*;
import android.widget.*;
import af.alireza.appsaz.R;
import java.util.*;

@SuppressLint({"DefaultLocale", "DrawAllocation", "Recycle"})
public class MagicalTextView_Master extends TextView {
  private WeakHashMap<String, Pair<Canvas, Bitmap>> canvasStore;
  private Drawable foregroundDrawable;
  private boolean frozen = false;
  private ArrayList<Shadow> innerShadows;
  private int[] lockedCompoundPadding;
  private ArrayList<Shadow> outerShadows;
  private Integer strokeColor;
  private Join strokeJoin;
  private float strokeMiter;
  private float strokeWidth;
  private Bitmap tempBitmap;
  private Canvas tempCanvas;

  private TextPaint paint;

  private Object typefaceName;

  public static class Shadow {
    int color;
    float dx;
    float dy;
    float r;

    public Shadow(float r, float dx, float dy, int color) {
      this.r = r;
      this.dx = dx;
      this.dy = dy;
      this.color = color;
    }
  }

  public MagicalTextView_Master(Context context) {
    super(context);
    init(null);
  }

  public MagicalTextView_Master(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public MagicalTextView_Master(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs);
  }

  public void init(AttributeSet attrs) {
    this.outerShadows = new ArrayList();
    this.innerShadows = new ArrayList();
    if (this.canvasStore == null) {
      this.canvasStore = new WeakHashMap();
    }
    int[] MagicalTextView_Master =
        new int[] {
          R.attr.innerShadowColor,
          R.attr.innerShadowRadius,
          R.attr.innerShadowDx,
          R.attr.innerShadowDy,
          R.attr.outerShadowColor,
          R.attr.outerShadowRadius,
          R.attr.outerShadowDx,
          R.attr.outerShadowDy,
          R.attr.typeface,
          R.attr.foreground,
          R.attr.background_1,
          R.attr.stroke_Width,
          R.attr.strokeMiter,
          R.attr.strokeColor,
          R.attr.strokeJoinStyle
        };

    if (attrs != null) {
      TypedArray a = getContext().obtainStyledAttributes(attrs, MagicalTextView_Master);
      if (a.getString(8) != null) {
        setTypeface(
            Typeface.createFromAsset(
                getContext().getAssets(),
                String.format("fonts/%s.ttf", new Object[] {typefaceName})));
      }
      if (a.hasValue(9)) {
        Drawable foreground = a.getDrawable(9);
        if (foreground != null) {
          setForegroundDrawable(foreground);
        } else {
          setTextColor(a.getColor(9, -16777216));
        }
      }
      if (a.hasValue(10)) {
        Drawable background = a.getDrawable(10);
        if (background != null) {
          setBackgroundDrawable(background);
        } else {
          setBackgroundColor(a.getColor(10, -16777216));
        }
      }
      if (a.hasValue(0)) {
        addInnerShadow(
            a.getFloat(1, 0.0f),
            a.getFloat(2, 0.0f),
            a.getFloat(3, 0.0f),
            a.getColor(0, -16777216));
      }
      if (a.hasValue(4)) {
        addOuterShadow(
            a.getFloat(5, 0.0f),
            a.getFloat(6, 0.0f),
            a.getFloat(7, 0.0f),
            a.getColor(4, -16777216));
      }
      if (a.hasValue(13)) {
        float strokeWidth = a.getFloat(11, 1.0f);
        int strokeColor = a.getColor(13, -16777216);
        float strokeMiter = a.getFloat(12, 10.0f);
        Join strokeJoin = null;
        switch (a.getInt(14, 0)) {
          case 0:
            strokeJoin = Join.MITER;
            break;
          case 1:
            strokeJoin = Join.BEVEL;
            break;
          case 2:
            strokeJoin = Join.ROUND;
            break;
        }
        setStroke(strokeWidth, strokeColor, strokeJoin, strokeMiter);
      }
    }
  }

  public void setStroke(float width, int color, Join join, float miter) {
    this.strokeWidth = width;
    this.strokeColor = Integer.valueOf(color);
    this.strokeJoin = join;
    this.strokeMiter = miter;
  }

  public void setStroke(float width, int color) {
    setStroke(width, color, Join.MITER, 10.0f);
  }

  public void addOuterShadow(float r, float dx, float dy, int color) {
    if (r == 0.0f) {
      r = 1.0E-4f;
    }
    this.outerShadows.add(new Shadow(r, dx, dy, color));
  }

  public void addInnerShadow(float r, float dx, float dy, int color) {
    if (r == 0.0f) {
      r = 1.0E-4f;
    }
    this.innerShadows.add(new Shadow(r, dx, dy, color));
  }

  public void clearInnerShadows() {
    this.innerShadows.clear();
  }

  public void clearOuterShadows() {
    this.outerShadows.clear();
  }

  public void setForegroundDrawable(Drawable d) {
    this.foregroundDrawable = d;
  }

  public Drawable getForeground() {
    return this.foregroundDrawable == null
        ? this.foregroundDrawable
        : new ColorDrawable(getCurrentTextColor());
  }

  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    freeze();
    Drawable restoreBackground = getBackground();
    Drawable[] restoreDrawables = getCompoundDrawables();
    int restoreColor = getCurrentTextColor();
    setCompoundDrawables(null, null, null, null);
    Iterator it = this.outerShadows.iterator();
    while (it.hasNext()) {
      Shadow shadow = (Shadow) it.next();
      setShadowLayer(shadow.r, shadow.dx, shadow.dy, shadow.color);
      super.onDraw(canvas);
    }
    setShadowLayer(0.0f, 0.0f, 0.0f, 0);
    setTextColor(restoreColor);
    if (this.foregroundDrawable != null && (this.foregroundDrawable instanceof BitmapDrawable)) {
      generateTempCanvas();
      super.onDraw(this.tempCanvas);
      ((BitmapDrawable) this.foregroundDrawable)
          .getPaint()
          .setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
      this.foregroundDrawable.setBounds(canvas.getClipBounds());
      this.foregroundDrawable.draw(this.tempCanvas);
      canvas.drawBitmap(this.tempBitmap, 0.0f, 0.0f, null);
      this.tempCanvas.drawColor(0, Mode.CLEAR);
    }
    if (this.strokeColor != null) {
      TextPaint paint = getPaint();
      paint.setStyle(Style.STROKE);
      paint.setStrokeJoin(this.strokeJoin);
      paint.setStrokeMiter(this.strokeMiter);
      setTextColor(this.strokeColor.intValue());
      paint.setStrokeWidth(this.strokeWidth);
      super.onDraw(canvas);
      paint.setStyle(Style.FILL);
      setTextColor(restoreColor);
    }
    if (this.innerShadows.size() > 0) {
      generateTempCanvas();
      paint = getPaint();
      it = this.innerShadows.iterator();
      while (it.hasNext()) {
        Shadow shadow = (Shadow) it.next();
        setTextColor(shadow.color);
        super.onDraw(this.tempCanvas);
        setTextColor(-16777216);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        paint.setMaskFilter(new BlurMaskFilter(shadow.r, Blur.NORMAL));
        this.tempCanvas.save();
        this.tempCanvas.translate(shadow.dx, shadow.dy);
        super.onDraw(this.tempCanvas);
        this.tempCanvas.restore();
        canvas.drawBitmap(this.tempBitmap, 0.0f, 0.0f, null);
        this.tempCanvas.drawColor(0, Mode.CLEAR);
        paint.setXfermode(null);
        paint.setMaskFilter(null);
        setTextColor(restoreColor);
        setShadowLayer(0.0f, 0.0f, 0.0f, 0);
      }
    }
    if (restoreDrawables != null) {
      setCompoundDrawablesWithIntrinsicBounds(
          restoreDrawables[0], restoreDrawables[1], restoreDrawables[2], restoreDrawables[3]);
    }
    setBackgroundDrawable(restoreBackground);
    setTextColor(restoreColor);
    unfreeze();
  }

  @SuppressLint({"DefaultLocale"})
  private void generateTempCanvas() {
    String key =
        String.format(
            "%dx%d", new Object[] {Integer.valueOf(getWidth()), Integer.valueOf(getHeight())});
    Pair<Canvas, Bitmap> stored = (Pair) this.canvasStore.get(key);
    if (stored != null) {
      this.tempCanvas = stored.first;
      this.tempBitmap = stored.second;
      return;
    }
    this.tempCanvas = new Canvas();
    this.tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
    this.tempCanvas.setBitmap(this.tempBitmap);
    this.canvasStore.put(key, new Pair(this.tempCanvas, this.tempBitmap));
  }

  public void freeze() {
    this.lockedCompoundPadding =
        new int[] {
          getCompoundPaddingLeft(),
          getCompoundPaddingRight(),
          getCompoundPaddingTop(),
          getCompoundPaddingBottom()
        };
    this.frozen = true;
  }

  public void unfreeze() {
    this.frozen = false;
  }

  public void requestLayout() {
    if (!this.frozen) {
      super.requestLayout();
    }
  }

  public void postInvalidate() {
    if (!this.frozen) {
      super.postInvalidate();
    }
  }

  public void postInvalidate(int left, int top, int right, int bottom) {
    if (!this.frozen) {
      super.postInvalidate(left, top, right, bottom);
    }
  }

  public void invalidate() {
    if (!this.frozen) {
      super.invalidate();
    }
  }

  public void invalidate(Rect rect) {
    if (!this.frozen) {
      super.invalidate(rect);
    }
  }

  public void invalidate(int l, int t, int r, int b) {
    if (!this.frozen) {
      super.invalidate(l, t, r, b);
    }
  }

  public int getCompoundPaddingLeft() {
    return !this.frozen ? super.getCompoundPaddingLeft() : this.lockedCompoundPadding[0];
  }

  public int getCompoundPaddingRight() {
    return !this.frozen ? super.getCompoundPaddingRight() : this.lockedCompoundPadding[1];
  }

  public int getCompoundPaddingTop() {
    return !this.frozen ? super.getCompoundPaddingTop() : this.lockedCompoundPadding[2];
  }

  public int getCompoundPaddingBottom() {
    return !this.frozen ? super.getCompoundPaddingBottom() : this.lockedCompoundPadding[3];
  }
}
