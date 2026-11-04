package af.alireza.appsaz;

import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.BiggroupButton2;
import af.alireza.views.MagicalTextView_Master;
import af.appsaz.myapplication.PictureActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesImages extends BaseActivity {
  int PICK_IMAGE_REQUEST = 1;
  LinearLayout content;
  String imgId;
  Picker picker = new Picker();
  Utilities utils = new Utilities();

  public static final int REQUEST_CODE_PICK_IMAGE = 101;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.files);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    final ActionBar actionBar;
    actionBar = getSupportActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(R.string.app_editor_pictures);
    content = (LinearLayout) findViewById(R.id.filesContent);

    Refresh();
    MagicalTextView_Master ADD = (MagicalTextView_Master) findViewById(R.id.add);
    ADD.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
           if (utils.checkPer(FilesImages.this)) {
            openImagePicker();
            }
          }
        });
  }
  public void openImagePicker() {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    intent.setType("image/*"); // انتخاب انواع فایل‌های عکس
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
      Uri uri = data.getData();
      if (uri != null) {
        final AppCompatDialog dialog = new AppCompatDialog(FilesImages.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.files_enter_info);
        TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
        title.setText(getString(R.string.files_add_pic));
        final LinearLayout moreOptions = (LinearLayout) dialog.findViewById(R.id.moreOptions);
        moreOptions.setVisibility(View.VISIBLE);
        final EditText Title = (EditText) dialog.findViewById(R.id.fileName);
        final MaterialSwitch saveAble = dialog.findViewById(R.id.fileSaveable);
        final MaterialSwitch shareAble = dialog.findViewById(R.id.fileShareable);
        Button dialog_Cancel = dialog.findViewById(R.id.cancel);
        Button dialog_Save = dialog.findViewById(R.id.save);
        // if thebutton is clicked, close the custom dialog
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
                String realPath = utils.getPathFromUri(getApplicationContext(), uri);
                if (realPath != null) {
                  imgId = getSomeInt() + utils.getFileExtensionWithApache(realPath);
                } else {
                  imgId = getSomeInt() + ".jpg";
                }
                if (Title.length() == 0) {
                  Title.setError(getString(R.string.a_error_name));
                } else {

                  final ProgressDialog progDailog =
                      ProgressDialog.show(
                          FilesImages.this, "", getString(R.string.a_loading), true);
                  final Handler handler =
                      new Handler() {
                        public void handleMessage(Message msg) {
                          if (new File(rootAssetsPath + "files/images/image" + imgId).exists()) {

                            String saveable = "False";
                            String shareable = "False";
                            if (saveAble.isChecked()) {
                              saveable = "True";
                            } else {
                              saveable = "False";
                            }
                            if (shareAble.isChecked()) {
                              shareable = "True";
                            } else {
                              shareable = "False";
                            }
                            String str = utils.readFile(rootAssetsPath + "files/images/images");
                            str =
                                str
                                    + "{Image<=>"
                                    + imgId
                                    + "<=>Title"
                                    + Title.getText().toString()
                                    + "<=>Imgimage"
                                    + imgId
                                    + "<=>SaveAble"
                                    + saveable
                                    + "<=>ShareAble"
                                    + shareable
                                    + "}!#";
                            if (str.startsWith("null")) {
                              str = str.replace("null", "");
                            }
                            utils.writeFile(rootAssetsPath + "files/images/images", str);
                            messageOk(FilesImages.this, getString(R.string.a_added));
                            Refresh();
                            return;
                          }
                          showErrorDialog(FilesImages.this);
                        }
                      };
                  new Thread() {
                    public void run() {
                      File destinationFile =
                          copyFileToCustomDirectory(
                              FilesImages.this,
                              uri,
                              rootAssetsPath + "files/images/",
                              "image" + imgId);
                      handler.sendEmptyMessage(0);
                      progDailog.dismiss();
                    }
                  }.start();

                  dialog.dismiss();
                }
              }
            });

        dialog.show();
      }
    }
  }

  public static File copyFileToCustomDirectory(
      Context context, Uri sourceUri, String targetPath, String fileName) {
    ContentResolver resolver = context.getContentResolver();
    // مسیر پوشه اختصاصی
    File customDir = new File(targetPath);
    if (!customDir.exists()) {
      if (!customDir.mkdirs()) {
        return null; // اگر ایجاد پوشه شکست خورد
      }
    }
    File destinationFile = new File(customDir, fileName);
    try (InputStream inputStream = resolver.openInputStream(sourceUri);
        OutputStream outputStream = new FileOutputStream(destinationFile)) {
      if (inputStream != null) {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
          outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
      }
      return destinationFile;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void Refresh() {
    TextView viewCount = findViewById(R.id.files_count);
    int filesCount = utils.getFileCount("image", rootAssetsPath + imagesPath);
    viewCount.setText(String.valueOf(filesCount));
    LinearLayout empty = (LinearLayout) findViewById(R.id.emptyLayout);
    ImageView emptyImg = (ImageView) findViewById(R.id.emptyImg);
    TextView emptyText = (TextView) findViewById(R.id.emptyText);
    content.removeAllViews();
    String data = utils.readFile(rootAssetsPath + "files/images/images");
    String str = data;
    final String Info = str;
    //	str = str.trim();
    final String[] durarray = str.split("\\!\\#");
    for (String part : durarray) {
      BiggroupButton2 button;
      String part2 = part;

      if (part2.contains("{Image<=>")) {
        empty.setVisibility(View.GONE);
        final String[] code = part2.replace("}", "").split("<=>");
        button = new BiggroupButton2(this);

        final String text = code[2].replace("Title", "");
        final String Img = code[3].replace("Img", "");
        final String Saveable = code[4].replace("SaveAble", "");
        final String Shareable = code[5].replace("ShareAble", "");
        button.setText(text);
        this.content.addView(button);
        button.setOnClickListener(
            new OnClickListener() {
              public void onClick(View v) {
                /*Intent intent = new Intent(FilesImages.this, ImageViewer.class);
                intent.putExtra("actionBarColor","#008577");
                intent.putExtra("imgName",Img);
                startActivity(intent);*/
                Intent intent = new Intent(FilesImages.this, PictureActivity.class);
                intent.putExtra("actionBarColor", "#008577");
                intent.putExtra("picName", Img);
                startActivity(intent);
              }
            });
        button.getRightIcon().setImageResource(R.drawable.ic_edit_file);
        button
            .getRightIcon()
            .setOnClickListener(
                new OnClickListener() {
                  @Override
                  public void onClick(View p1) {
                    final AppCompatDialog dialog = new AppCompatDialog(FilesImages.this);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.files_enter_info);
                    TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
                    title.setText(text);
                    final LinearLayout moreOptions =
                        (LinearLayout) dialog.findViewById(R.id.moreOptions);
                    moreOptions.setVisibility(View.VISIBLE);
                    final EditText Title = (EditText) dialog.findViewById(R.id.fileName);
                    Title.setText(text);
                    final MaterialSwitch saveAble = dialog.findViewById(R.id.fileSaveable);
                    final MaterialSwitch shareAble = dialog.findViewById(R.id.fileShareable);
                    if (Saveable.startsWith("True")) {
                      saveAble.setChecked(true);
                    } else {
                      saveAble.setChecked(false);
                    }
                    if (Shareable.startsWith("True")) {
                      shareAble.setChecked(true);
                    } else {
                      shareAble.setChecked(false);
                    }
                    Button dialog_Cancel = dialog.findViewById(R.id.cancel);
                    Button dialog_Delete = dialog.findViewById(R.id.delete);
                    Button dialog_Save = dialog.findViewById(R.id.save);
                    dialog_Delete.setVisibility(View.VISIBLE);
                    // if thebutton is clicked, close the custom dialog
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
                            if (Title.length() == 0) {
                              Title.setError(getString(R.string.a_error_name));
                            } else {

                              String saveable = Saveable;
                              String shareable = Shareable;
                              if (saveAble.isChecked()) {
                                saveable = "True";
                              } else {
                                saveable = "False";
                              }
                              if (shareAble.isChecked()) {
                                shareable = "True";
                              } else {
                                shareable = "False";
                              }
                              String str =
                                  Info.replace(
                                      "{Image<=>"
                                          + code[1]
                                          + "<=>Title"
                                          + text
                                          + "<=>Img"
                                          + Img
                                          + "<=>SaveAble"
                                          + Saveable
                                          + "<=>ShareAble"
                                          + Shareable
                                          + "}!#",
                                      "{Image<=>"
                                          + code[1]
                                          + "<=>Title"
                                          + Title.getText().toString()
                                          + "<=>Img"
                                          + Img
                                          + "<=>SaveAble"
                                          + saveable
                                          + "<=>ShareAble"
                                          + shareable
                                          + "}!#");
                              utils.writeFile(rootAssetsPath + "files/images/images", str);
                              Refresh();
                              dialog.dismiss();
                            }
                          }
                        });
                    dialog_Delete.setOnClickListener(
                        new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                            String str =
                                Info.replace(
                                    "{Image<=>"
                                        + code[1]
                                        + "<=>Title"
                                        + text
                                        + "<=>Img"
                                        + Img
                                        + "<=>SaveAble"
                                        + Saveable
                                        + "<=>ShareAble"
                                        + Shareable
                                        + "}!#",
                                    "");
                            File file = new File(rootAssetsPath + "files/images/" + Img);
                            file.delete();
                            utils.writeFile(rootAssetsPath + "files/images/images", str);

                            Refresh();
                            dialog.dismiss();
                          }
                        });

                    dialog.show();
                  }
                });
        File In = new File(rootAssetsPath + "files/images/" + Img);
        button.setLeftIcon(In);
        Utilities utils = new Utilities();
        button.setTextDesc(utils.getSizeLabel(In));
      } else {
        empty.setVisibility(View.VISIBLE);
        emptyImg.setImageResource(R.drawable.ic_images);
        emptyText.setText(getString(R.string.files_empty_pics));
      }
    }
  }
}
