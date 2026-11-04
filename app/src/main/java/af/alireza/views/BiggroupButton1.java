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

public class BiggroupButton1 extends RelativeLayout {

	private ImageView rightIcon;
	private TextView text;
	private ImageView leftIcon;
	private CardView bg;
	private Context context;

	public BiggroupButton1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public BiggroupButton1(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public BiggroupButton1(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public void init() {
		inflate(getContext(), R.layout.biggroup_button1, this);
		bg = (CardView) findViewById(R.id.bigBtnBg);
		rightIcon = (ImageView) findViewById(R.id.bigBtnRightIcon);
		leftIcon = (ImageView) findViewById(R.id.bigBtnLeftIcon);
		text = (TextView) findViewById(R.id.bigBtnText);

	}

	public void setRightIcon(File icon) {
		rightIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none).into(rightIcon);
	}

	public void setRightIcon(String icon) {
		rightIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none).into(rightIcon);
	}

	public void setRightIcon(int icon) {
		rightIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none).into(rightIcon);
	}

	public void setLeftIcon(File icon) {
		leftIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none).into(leftIcon);
	}

	public void setLeftIcon(String icon) {
		leftIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none).into(leftIcon);
	}

	public void setLeftIcon(int icon) {
		leftIcon.setVisibility(View.VISIBLE);
		Glide.with(context).load(icon).placeholder(R.drawable.none).into(leftIcon);
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
		bg.setCardBackgroundColor(color);
	}

	public void setBackgroundImage(Drawable drawable) {
		bg.setBackground(drawable);
	}

	public void setCRadios(int radios) {
		bg.setRadius(radios);
	}
}
