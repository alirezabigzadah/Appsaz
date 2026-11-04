package af.alireza.views;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import androidx.cardview.widget.*;
import af.alireza.appsaz.*;
import com.bumptech.glide.Glide;
import java.io.*;

public class BiggroupButton2 extends RelativeLayout {

	private ImageView rightIcon;
	private TextView text;
	private TextView desc;
	private ImageView leftIcon;
	private FrameLayout bg;
	private Context context;

	public BiggroupButton2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public BiggroupButton2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public BiggroupButton2(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public void init() {
		inflate(getContext(), R.layout.biggroup_button2, this);
		bg = (FrameLayout) findViewById(R.id.bigBtnBg);
		rightIcon = (ImageView) findViewById(R.id.bigBtnRightIcon);
		leftIcon = (ImageView) findViewById(R.id.bigBtnLeftIcon);
		text = (TextView) findViewById(R.id.bigBtnText);
		desc = (TextView) findViewById(R.id.bigBtnDesc);

	}

	public void setRightIcon(File icon) {
		rightIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none)
				.into(rightIcon);
	}

	public void setRightIcon(String icon) {
		rightIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none)
				.into(rightIcon);
	}

	public void setRightIcon(int icon) {
		rightIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none)
				.into(rightIcon);
	}

	public void setLeftIcon(File icon) {
		leftIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none)
				.into(leftIcon);
	}

	public void setLeftIcon(String icon) {
		leftIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none)
				.into(leftIcon);
	}

	public void setLeftIcon(int icon) {
		leftIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none)
				.into(leftIcon);
	}

	public void setText(String txt) {
		text.setText(txt);
	}

	public void setTextSize(int size) {
		text.setTextSize(size);
	}

	public void setTextColor(int color) {
		text.setTextColor(color);
	}

	public void setTextDesc(String txt) {
		desc.setVisibility(View.VISIBLE);
		desc.setText(txt);
	}

	public String getText() {
		return text.getText().toString();
	}

	public ImageView getRightIcon() {
		rightIcon.setVisibility(View.VISIBLE);
		return rightIcon;
	}

	public ImageView getLeftIcon() {
		leftIcon.setVisibility(View.VISIBLE);
		return leftIcon;
	}

	public void setTypeface(Typeface typeface) {
        try {
        	text.setTypeface(typeface);;
            }
    	catch (Exception e)
				{
              }
	}

	public void setBackgroundColor(int color) {
		bg.setBackgroundColor(color);
	}

	public void setBackgroundImage(Drawable drawable) {
		bg.setBackgroundDrawable(drawable);
	}
	/*public void setCRadios(int radios) {
		bg.setRadius(radios);
	}*/
}
