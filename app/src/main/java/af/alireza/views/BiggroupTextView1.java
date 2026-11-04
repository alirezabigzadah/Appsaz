package af.alireza.views;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.text.Spanned;
import android.text.util.Linkify;
import android.util.*;
import android.view.*;
import android.widget.*;
import androidx.cardview.widget.*;
import af.alireza.appsaz.*;

public class BiggroupTextView1 extends RelativeLayout
{

	private TextView text;
	private CardView bg;
	private LinearLayout shareAbleLayout;
	private ImageView shareBtn;
	private Context context;

    public BiggroupTextView1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
		this.context = context;
        init();
    }

    public BiggroupTextView1(Context context, AttributeSet attrs) {
        super(context, attrs);
		this.context = context;
		init();
	}

    public BiggroupTextView1(Context context) {
        super(context);
		this.context = context;
        init();
    }

    public void init() {
		inflate(getContext(),R.layout.biggroup_textview1,this);
		bg = (CardView) findViewById(R.id.bigTxtBg);
		text = (TextView) findViewById(R.id.bigTxtText);
		shareAbleLayout = (LinearLayout) findViewById(R.id.bigTxtShareAble);
		shareBtn = (ImageView) findViewById(R.id.bigTxtShareBtn);
	}
	public void setText(String txt) {
		text.setText(txt);
	}
    public void setText(Spanned txt) {
		text.setText(txt);
	}
	public void setTextSize(int size) {
		text.setTextSize(size);
	}
	public void setTextColor(int color) {
		text.setTextColor(color);
	}
	public void setTextGravity(int gravity) {
		text.setGravity(gravity);
	}
	public void setTextIsSelectable(boolean selectAble) {
		text.setTextIsSelectable(selectAble);
	}
	public void setTextIsShareable(boolean shareAble) {
		if(shareAble){
			shareAbleLayout.setVisibility(View.VISIBLE);
			shareBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
						sharingIntent.setType("text/plain");
						String shareBody = text.getText().toString();
						sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
						context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.a_share)));
					}
				});
		}else{
			shareAbleLayout.setVisibility(View.GONE);
		}
	}
    public void setAutoLink() {
		text.setAutoLinkMask(Linkify.ALL);
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
		bg.setBackgroundDrawable(drawable);
	}
	public void setHeight(int height) {
		text.setHeight(height);
	}
	public void setCRadios(int radios) {
		bg.setRadius(radios);
	}
    public ImageView getShareAbleButton() {
		shareAbleLayout.setVisibility(View.VISIBLE);
		return shareBtn;
	}
	public String getText() {
		return text.getText().toString();
	}
	public TextView getATextView() {
		return text;
	}}
