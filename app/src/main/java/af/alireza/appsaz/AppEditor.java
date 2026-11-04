package af.alireza.appsaz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;

import af.alireza.appsaz.ColorPickerDialog.OnColorSelectedListener;
import af.alireza.appsaz.IconPickerDialog.OnIconSelectedListener;
import af.alireza.appsaz.Utilities.IconGenerator;
import af.alireza.appsaz.Utilities.Utilities;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

public class AppEditor extends BaseActivity {

  String app_name;
  String package_name;
  String version_name;
  int version_code;
  String iconNameForChange = "";
  LinearLayout editAppInfo;
  TextView verCode;
  TextView verName;
  TextView appName;
  TextView packageName;
  ImageView icLauncher;
  private Utilities utils = new Utilities();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.app_editor);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    final ActionBar actionBar;
    actionBar = getSupportActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(R.string.app_editor_title);

    appName = (TextView) findViewById(R.id.editAppName);
    packageName = (TextView) findViewById(R.id.editPackageName);
    verCode = (TextView) findViewById(R.id.editVersion);
    verName = (TextView) findViewById(R.id.editVersionName);
    icLauncher = (ImageView) findViewById(R.id.editchangeIcon);

    Refresh();
    editAppInfo = (LinearLayout) findViewById(R.id.editappInfo);
    editAppInfo.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {

            final AppCompatDialog dialog = new AppCompatDialog(AppEditor.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.edit_app_info);
            TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
            title.setText(getString(R.string.app_editor_app_manifest));
            final ImageView iCon = (ImageView) dialog.findViewById(R.id.editappinfoChangeIcon);
            File In = new File(rootdirectory + "MyApp/res/mipmap-xxxhdpi/ic_launcher.png");
            Glide.with(AppEditor.this)
                .load(In)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.none)
                .into(iCon);

            final EditText editAppName = (EditText) dialog.findViewById(R.id.editappinfoAppName);
            final EditText editPackageName =
                (EditText) dialog.findViewById(R.id.editappinfoPackageName);
            final EditText editVersionName =
                (EditText) dialog.findViewById(R.id.editappinfoVersionName);
            final EditText editVersionCode =
                (EditText) dialog.findViewById(R.id.editappinfoVersion);
            editAppName.setText(app_name);
            editPackageName.setText(package_name);
            editVersionName.setText(version_name);
            editVersionCode.setText(String.valueOf(version_code));
            Button dialog_Cancel = dialog.findViewById(R.id.editappinfoCancelBtn);
            Button dialog_Save = dialog.findViewById(R.id.editappinfoSaveBtn);
            // if thebutton is clicked, close the custom dialog
            iCon.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    dialog.dismiss();
                    String title = "GoneNone" + getString(R.string.app_editor_app_icon);
                    new IconPickerDialog(AppEditor.this, title,
                            new OnIconSelectedListener() {
                              public void onIconSelected(String fileName, String fileId) {
                                iconNameForChange = fileId;
                                File In = new File(rootAssetsPath + "files/icons/" + fileId);
                             /*   Glide.with(AppEditor.this)
                                    .load(In)
                                    .placeholder(R.drawable.none)
                                    .into(iCon);
                                            */
                                    showIconEditorDialog(In.getAbsolutePath());
                              }
                            })
                        .show();
                  }
                });
            dialog_Cancel.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    dialog.dismiss();
                  }
                });
            dialog_Save.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    String appName = editAppName.getText().toString();
                    String packageName = editPackageName.getText().toString();
                    String versionCode = editVersionCode.getText().toString();
                    String versionName = editVersionName.getText().toString();

                    boolean hasError = false;

                    // بررسی خالی بودن نام برنامه
                    if (TextUtils.isEmpty(appName)) {
                      editAppName.setError(getString(R.string.app_editor_error_app_name));
                      hasError = true;
                    } else {
                      editAppName.setError(null); // پاک کردن خطا
                    }
                    // بررسی خالی بودن و معتبر بودن نام بسته
                    if (TextUtils.isEmpty(packageName)) {
                      editPackageName.setError(getString(R.string.app_editor_error_package_name));
                      hasError = true;
                    } else if (!isValidPackageName(packageName)) {
                      editPackageName.setError(
                          getString(R.string.app_editor_error_incorrect_package_name));
                      hasError = true;
                    } else if (utils.isPackageNameReserved(packageName)) {
                      editPackageName.setError(getString(R.string.main_error_reserved_package_name));
                      hasError = true;
                    } else {
                      editPackageName.setError(null); // پاک کردن خطا
                    }
                    if (TextUtils.isEmpty(versionCode)) {
                      editVersionCode.setError(getString(R.string.app_editor_error_version_code));
                      hasError = true;
                    } else {
                      editVersionCode.setError(null); // پاک کردن خطا
                    }
                    if (TextUtils.isEmpty(versionName)) {
                      editVersionName.setError(getString(R.string.app_editor_error_version_name));
                      hasError = true;
                    } else {
                      editVersionName.setError(null); // پاک کردن خطا
                    }
                    // اگر هیچ خطایی نبود
                    if (!hasError) {
                      app_name = editAppName.getText().toString();
                      package_name = editPackageName.getText().toString();
                      version_name = editVersionName.getText().toString();
                      version_code = Integer.parseInt(editVersionCode.getText().toString());
                      String str =
                          "{Project<=>Name"
                              + app_name
                              + "<=>PackageName"
                              + package_name
                              + "<=>VersionCode"
                              + version_code
                              + "<=>VersionName"
                              + version_name;
                      utils.writeFile(rootdirectory + "Project.pro", str);
                      /*if (iconNameForChange.startsWith("icon")) {
                        getIcLauncher(
                            rootAssetsPath + "files/icons/" + iconNameForChange,
                            rootdirectory + "MyApp/res/mipmap/ic_launcher.png");
                      }*/
                      Refresh();
                      dialog.dismiss();
                    }
                  }
                });
                    dialog.show();
          }
        });
    RelativeLayout appSplashEdit = (RelativeLayout) findViewById(R.id.appSplash);
    appSplashEdit.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            Intent intent = new Intent(AppEditor.this, SplashEditor.class);
            startActivity(intent);
          }
        });
    RelativeLayout appPageEdit = (RelativeLayout) findViewById(R.id.appPages);
    appPageEdit.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            Intent intent = new Intent(AppEditor.this, PageCreator.class);
            startActivity(intent);
          }
        });
    RelativeLayout appListEdit = (RelativeLayout) findViewById(R.id.appLists);
    appListEdit.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            Intent intent = new Intent(AppEditor.this, ListCreator.class);
            startActivity(intent);
          }
        });
    RelativeLayout appFilesPics = (RelativeLayout) findViewById(R.id.appFilesPics);
    appFilesPics.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            Intent intent = new Intent(AppEditor.this, FilesImages.class);
            startActivity(intent);
          }
        });
    RelativeLayout appFilesIcs = (RelativeLayout) findViewById(R.id.appFilesIcs);
    appFilesIcs.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            Intent intent = new Intent(AppEditor.this, FilesIcons.class);
            startActivity(intent);
          }
        });
    RelativeLayout appFilesVids = (RelativeLayout) findViewById(R.id.appFilesVids);
    appFilesVids.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            Intent intent = new Intent(AppEditor.this, FilesVideos.class);
            startActivity(intent);
          }
        });
    RelativeLayout appFilesAuds = (RelativeLayout) findViewById(R.id.appFilesAuds);
    appFilesAuds.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            Intent intent = new Intent(AppEditor.this, FilesAudios.class);
            startActivity(intent);
          }
        });
    RelativeLayout appFilesFonts = (RelativeLayout) findViewById(R.id.appFilesFonts);
    appFilesFonts.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            Intent intent = new Intent(AppEditor.this, FilesFonts.class);
            startActivity(intent);
          }
        });
    RelativeLayout appAdvancedAds = (RelativeLayout) findViewById(R.id.appAds);
    appAdvancedAds.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            Intent intent = new Intent(AppEditor.this, AdsEditor.class);
            startActivity(intent);
          }
        });
  }

  public void getIcLauncher(String in, String out) {
    try {
      OutputStream myOutput = new FileOutputStream(out);
      byte[] buffer = new byte[1024];
      FileInputStream fileIn = new FileInputStream(in);
      InputStream myInput = fileIn;
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
    } catch (IOException e) {
    }
  }

  private void Refresh() {

    File In = new File(rootdirectory + "MyApp/res/mipmap-xxxhdpi/ic_launcher.png");
    Glide.with(AppEditor.this)
        .load(In)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .placeholder(R.drawable.none)
        .into(icLauncher);
    try {
      String fileText = utils.readFile(rootdirectory + "Project.pro");
      if (fileText.contains("{Project<=>")) {
        String[] code = fileText.replace("}", "").split("<=>");
        String AppName = code[1].replaceFirst("Name", "");
        String PackageName = code[2].replaceFirst("PackageName", "");
        String VersionCode = code[3].replace("VersionCode", "");
        String VersionName = code[4].replace("VersionName", "");

        app_name = AppName;
        package_name = PackageName;
        version_code = Integer.parseInt(VersionCode);
        version_name = VersionName;

        appName.setText(app_name);
        packageName.setText(PackageName);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    appName.setText(app_name);
    packageName.setText(package_name);
    verCode.setText(String.valueOf(version_code));
    verName.setText(version_name);
  }

  private static String PACKAGE_NAME_PATTERN = "^[a-zA-Z]+(\\.[a-zA-Z][a-zA-Z0-9]*)+$";

  private boolean isValidPackageName(String packageName) {
    return Pattern.matches(PACKAGE_NAME_PATTERN, packageName);
  }
    
    int selectedColor = Color.WHITE;
    float cornerRadiusPercent = 0;
    float regZoomPercent = 0;
    float adaptZoomPercent = 0;
    
    private void showIconEditorDialog(String fileUri) {
        // ایجاد inflater برای View سفارشی
        String selectedFileUri = fileUri;
        selectedColor = Color.WHITE;
        cornerRadiusPercent = 0;
        regZoomPercent = 50f;
        adaptZoomPercent = 50f;
        
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog_icon_editor, null);

        Button btnPickColor = customView.findViewById(R.id.btn_pick_color);
        SeekBar seekBarCornerRadius = customView.findViewById(R.id.seekbar_corner_radius);
        TextView tvCornerRadiusLabel = customView.findViewById(R.id.tv_corner_radius_label);
        ImageView iconRegularPreview = customView.findViewById(R.id.iconNormalPreview);
        ImageView iconAdaptivePreview = customView.findViewById(R.id.iconAdaptivePreview);
        SeekBar seekBarRegZoom = customView.findViewById(R.id.seekbar_regular_zoom);
        TextView tvRegZoom = customView.findViewById(R.id.tv_regular_zoom);
        SeekBar seekBarAdaptZoom = customView.findViewById(R.id.seekbar_adapt_zoom);
        TextView tvAdaptZoom = customView.findViewById(R.id.tv_adapt_zoom);
        
        ImageView iconsInfo = customView.findViewById(R.id.icon_editor_info);
        
        preview(selectedFileUri, selectedColor, cornerRadiusPercent, regZoomPercent, adaptZoomPercent, iconRegularPreview, iconAdaptivePreview);

        String str = getString(R.string.icon_editor_corner_radius);
        String str1 = getString(R.string.icon_editor_regular_padding);
        String str2 = getString(R.string.icon_editor_adaptive_padding);
        tvCornerRadiusLabel.setText(str + "0%");
        tvRegZoom.setText(str1 + "50%");
        tvAdaptZoom.setText(str2 + "50%");
        seekBarCornerRadius.setProgress(0);
        seekBarRegZoom.setProgress(50);
        seekBarAdaptZoom.setProgress(50);
        
        btnPickColor.setOnClickListener( v -> {
                new ColorPickerDialog(AppEditor.this, selectedColor, "HideAlpha", new OnColorSelectedListener() {
                		public void onColorSelected(int color) {
                			selectedColor = color;
                            preview(selectedFileUri, selectedColor, cornerRadiusPercent, regZoomPercent, adaptZoomPercent, iconRegularPreview, iconAdaptivePreview);
											}
										}).show();
                });

        seekBarCornerRadius.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        cornerRadiusPercent = (progress / 2);
                        tvCornerRadiusLabel.setText(str + progress + "%");
                        preview(selectedFileUri, selectedColor, cornerRadiusPercent, regZoomPercent, adaptZoomPercent, iconRegularPreview, iconAdaptivePreview);
                    }
                
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });

        seekBarRegZoom.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(progress<5){
                        seekBarRegZoom.setProgress(5);
                    }else{
                        regZoomPercent = (progress);
                        tvRegZoom.setText(str1 + progress + "%");
                        preview(selectedFileUri, selectedColor, cornerRadiusPercent, regZoomPercent, adaptZoomPercent, iconRegularPreview, iconAdaptivePreview);
                    }
                    }
                
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });
        
        seekBarAdaptZoom.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(progress<5){
                        seekBarAdaptZoom.setProgress(5);
                    }else{
                        adaptZoomPercent = (progress);
                        tvAdaptZoom.setText(str2 + progress + "%");
                        preview(selectedFileUri, selectedColor, cornerRadiusPercent, regZoomPercent, adaptZoomPercent, iconRegularPreview, iconAdaptivePreview);
                    }
                    }
                
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });
        
        iconsInfo.setOnClickListener( v -> {
                new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.icon_editor_info_title)
                .setMessage(R.string.icon_editor_info)
                .setNegativeButton(R.string.a_ok, (dialog, which) -> {
              }).show();
                });
        
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.icon_editor)
                .setView(customView)
                .setPositiveButton(R.string.a_save, (dialog, which) -> {
                                Bitmap inputBitmap = BitmapFactory.decodeFile(selectedFileUri);
                                IconGenerator.generateAdaptiveIcons(this, inputBitmap, selectedColor, adaptZoomPercent);
                                IconGenerator.saveRegularIcons(this, inputBitmap, selectedColor, cornerRadiusPercent, regZoomPercent);
                Refresh();
                        })
                .setNegativeButton(R.string.a_cancel, (dialog, which) -> {
              }).setCancelable(false)
                .show();
    }

    private void preview(
            String selectedFileUri,
            int backgroundColor,
            float cornerRadiusPercent, float regZoom, float adaptZoom,
            ImageView iconRegularPreview,
            ImageView iconAdaptivePreview) {
            Bitmap inputBitmap = BitmapFactory.decodeFile(selectedFileUri);
            updateRegularPreview(inputBitmap, selectedColor, cornerRadiusPercent, regZoom, iconRegularPreview);
            updateAdaptiveIconPreview(inputBitmap, selectedColor, adaptZoom, iconAdaptivePreview);
        
    }

    private void updateRegularPreview(
            Bitmap inputBitmap,
            int backgroundColor,
            float cornerRadiusPercent, float regZoom,
            ImageView iconRegularPreview) {
        int previewSize = 400; // اندازه پیش‌نمایش
        Bitmap previewBitmap =
                IconGenerator.generateRegularIcon(
                        createCenterCropBitmap(inputBitmap, previewSize, previewSize),
                        backgroundColor,
                        cornerRadiusPercent,
                        regZoom, previewSize);
        iconRegularPreview.setImageBitmap(previewBitmap);
    }

    private void updateAdaptiveIconPreview(
            Bitmap foregroundBitmap, int backgroundColor, float adaptReg, ImageView iconAdaptivePreview) {
        int size = Math.max(foregroundBitmap.getWidth(), foregroundBitmap.getHeight());
        Bitmap backgroundBitmap = createSolidColorBitmap(size, size, backgroundColor);
        Bitmap adaptiveIconBitmap =
                combineForegroundAndBackground(
                        createCenterCropBitmap1(foregroundBitmap, size, size, adaptReg), backgroundBitmap);
        iconAdaptivePreview.setImageBitmap(adaptiveIconBitmap);
    }

    private Bitmap createSolidColorBitmap(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawRect(0, 0, width, height, paint);
        return bitmap;
    }

    private Bitmap combineForegroundAndBackground(Bitmap foreground, Bitmap background) {
        int size = Math.max(foreground.getWidth(), background.getHeight());
        Bitmap outputBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, 0, 0, null);
        return outputBitmap;
    }

    /**
     * تغییر اندازه و برش مرکز تصویر برای center crop.
     *
     * @param inputBitmap تصویر ورودی.
     * @param outputWidth عرض خروجی.
     * @param outputHeight ارتفاع خروجی.
     * @return تصویر با اندازه و برش مناسب.
     */
    private Bitmap createCenterCropBitmap(Bitmap inputBitmap, int outputWidth, int outputHeight) {
        int inputWidth = inputBitmap.getWidth();
        int inputHeight = inputBitmap.getHeight();
        // محاسبه نسبت ابعاد
        float inputAspectRatio = (float) inputWidth / inputHeight;
        float outputAspectRatio = (float) outputWidth / outputHeight;
        int scaledWidth, scaledHeight;
        // مقیاس‌بندی برای پر کردن فضای خروجی
        if (inputAspectRatio > outputAspectRatio) {
            scaledHeight = outputHeight;
            scaledWidth = (int) (scaledHeight * inputAspectRatio);
        } else {
            scaledWidth = outputWidth;
            scaledHeight = (int) (scaledWidth / inputAspectRatio);
        }
        // تغییر اندازه تصویر ورودی
        Bitmap scaledBitmap =
                Bitmap.createScaledBitmap(inputBitmap, scaledWidth, scaledHeight, true);
        // برش از مرکز
        int xOffset = (scaledWidth - outputWidth) / 2;
        int yOffset = (scaledHeight - outputHeight) / 2;
        return Bitmap.createBitmap(scaledBitmap, xOffset, yOffset, outputWidth, outputHeight);
    }
   private static Bitmap createCenterCropBitmap1(
        Bitmap inputBitmap, int outputWidth, int outputHeight, float paddingPercent) {
    int inputWidth = inputBitmap.getWidth();
    int inputHeight = inputBitmap.getHeight();

    // محاسبه نسبت مقیاس با توجه به درصد پدینگ
    float scaleFactor = paddingPercent / 50f;

    // نسبت ابعاد تصویر ورودی و خروجی
    float inputAspectRatio = (float) inputWidth / inputHeight;
    float outputAspectRatio = (float) outputWidth / outputHeight;

    // تنظیم ابعاد تصویر مقیاس‌شده
    int scaledWidth, scaledHeight;
    if (inputAspectRatio > outputAspectRatio) {
        scaledHeight = (int) (outputHeight * scaleFactor);
        scaledWidth = (int) (scaledHeight * inputAspectRatio);
    } else {
        scaledWidth = (int) (outputWidth * scaleFactor);
        scaledHeight = (int) (scaledWidth / inputAspectRatio);
    }

    // ایجاد یک Bitmap جدید با اندازه خروجی
    Bitmap outputBitmap = Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(outputBitmap);

    // محاسبه مختصات برای رسم تصویر مقیاس‌شده در مرکز
    int left = (outputWidth - scaledWidth) / 2;
    int top = (outputHeight - scaledHeight) / 2;

    // رسم تصویر مقیاس‌شده در بوم
    Bitmap scaledBitmap = Bitmap.createScaledBitmap(inputBitmap, scaledWidth, scaledHeight, true);
    canvas.drawBitmap(scaledBitmap, left, top, null);

    return outputBitmap;
    }
}
