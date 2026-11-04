package af.alireza.appsaz;

import af.alireza.appsaz.Utilities.Utilities;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.materialswitch.MaterialSwitch;

public class AdsEditor extends BaseActivity {

    TextView viewAdAppId;
    TextView viewAdBannerId;
    Utilities utils = new Utilities();
    MaterialSwitch viewAdBannerIdSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ads_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar;
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.app_editor_ads);

        ids();
        Refresh();

    }

    private void Refresh() {
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
					viewAdAppId.setText(appId);
					viewAdAppId.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								final AppCompatDialog dialog = new AppCompatDialog(AdsEditor.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
								dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
								dialog.setContentView(R.layout.files_enter_info);
								TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
								title.setText(R.string.ads_admob_application_id);
								final EditText ID = (EditText) dialog.findViewById(R.id.fileName);
								ID.setText(appId);
								TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
								//  TextView dialog_Delete = (TextView) dialog.findViewById(R.id.delete);
								TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
								//dialog_Delete.setVisibility(View.VISIBLE);
								// if thebutton is clicked, close the custom dialog
								dialog_Cancel.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									});
								dialog_Save.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											if (ID.length() == 0) {
												ID.setError(getString(R.string.a_error_name));
											} else {
												String str = Info.replace(
                                                	"{Admob" + "<=>Mode" + mode + "<=>AbmobApplicationId" + appId + "}!#",
                                                    "{Admob" + "<=>Mode" + mode + "<=>AbmobApplicationId" + ID.getText().toString() + "}!#");
												utils.writeFile(rootAssetsPath + "ads", str);
												Refresh();
												dialog.dismiss();
											}
										}
									});
								dialog.show();
							}
						});
				}
			}
			if (p.contains("{AdmobBannerAd<=>")) {
				String[] code = p.replace("}", "").split("<=>");
				String mode = code[1].replaceFirst("Mode", "");
				String bannerId = code[2].replaceFirst("AdmobBannerAdId", "");
                viewAdBannerId.setText(bannerId);
				if (mode.equals("True")) {
                    viewAdBannerIdSwitch.setChecked(true);
                    viewAdBannerId.setEnabled(true);
                    viewAdBannerId.setAlpha(1);
                    viewAdBannerId.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								final AppCompatDialog dialog = new AppCompatDialog(AdsEditor.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
								dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
								dialog.setContentView(R.layout.files_enter_info);
								TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
								title.setText(R.string.ads_banner_ad_id);
								final EditText ID = (EditText) dialog.findViewById(R.id.fileName);
								ID.setText(bannerId);
								TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
								//  TextView dialog_Delete = (TextView) dialog.findViewById(R.id.delete);
								TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
								//dialog_Delete.setVisibility(View.VISIBLE);
								// if thebutton is clicked, close the custom dialog
								dialog_Cancel.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									});
								dialog_Save.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											if (ID.length() == 0) {
												ID.setError(getString(R.string.a_error_name));
											} else {
												String str = Info.replace(
                                                	"{AdmobBannerAd" + "<=>Mode" + mode + "<=>AdmobBannerAdId" + bannerId + "}!#",
                                                    "{AdmobBannerAd" + "<=>Mode" + mode + "<=>AdmobBannerAdId" + ID.getText().toString() + "}!#");
												utils.writeFile(rootAssetsPath + "ads", str);
												Refresh();
												dialog.dismiss();
											}
										}
									});
								dialog.show();
							}
						});
				} else {
                    viewAdBannerIdSwitch.setChecked(false);
                    viewAdBannerId.setEnabled(false);
                    viewAdBannerId.setAlpha(0.5f);
				}
                viewAdBannerIdSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if (isChecked) {
								// در حالت روشن است
								String Str = "True";
								String str = Info.replace(
									"{AdmobBannerAd" + "<=>Mode" + mode + "<=>AdmobBannerAdId" + bannerId + "}!#",
									"{AdmobBannerAd" + "<=>Mode" + Str + "<=>AdmobBannerAdId" + bannerId + "}!#");
								utils.writeFile(rootAssetsPath + "ads", str);

							} else {
								// در حالت خاموش است
								String Str = "False";
								String str = Info.replace(
									"{AdmobBannerAd" + "<=>Mode" + mode + "<=>AdmobBannerAdId" + bannerId + "}!#",
									"{AdmobBannerAd" + "<=>Mode" + Str + "<=>AdmobBannerAdId" + bannerId + "}!#");
								utils.writeFile(rootAssetsPath + "ads", str);
							}
							Refresh();
						}
					});
			}
		}
		//String bannerId = code[2].replaceFirst("AdmobBannerAdId", "");
        //String interstitialId = code[3].replace("AdmobInterstitialAdId", "");
		//String videoId = code[4].replace("AdmobVideoAdId", "");

	}
	private void ids() {
        viewAdAppId = findViewById(R.id.ads_admob_application_id);
        viewAdBannerId = findViewById(R.id.ads_banner_ad_id);
        viewAdBannerIdSwitch = findViewById(R.id.ads_banner_ad_switch);
	}
}