package af.alireza.appsaz;

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
import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.BiggroupButton2;
import af.alireza.views.MagicalTextView_Master;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesIcons extends BaseActivity {
  int PICK_IMAGE_REQUEST = 1;
  LinearLayout content;
  String ID;
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
    getSupportActionBar().setTitle(R.string.app_editor_icons);
    content = (LinearLayout) findViewById(R.id.filesContent);

    Refresh();
    MagicalTextView_Master ADD = (MagicalTextView_Master) findViewById(R.id.add);
    ADD.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (utils.checkPer(FilesIcons.this)) {
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
        final AppCompatDialog dialog = new AppCompatDialog(FilesIcons.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.files_enter_info);
        TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
        title.setText(getString(R.string.files_add_icon));
        final EditText Title = (EditText) dialog.findViewById(R.id.fileName);
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
                  ID = getSomeInt() + utils.getFileExtensionWithApache(realPath);
                } else {
                  ID = getSomeInt() + ".jpg";
                }
                if (Title.length() == 0) {
                  Title.setError(getString(R.string.a_error_name));
                } else {
                  final ProgressDialog progDailog =
                      ProgressDialog.show(FilesIcons.this, "", getString(R.string.a_loading), true);
                  final Handler handler =
                      new Handler() {
                        public void handleMessage(Message msg) {
                          if (new File(rootAssetsPath + "files/icons/icon" + ID).exists()) {
                            String str = utils.readFile(rootAssetsPath + "files/icons/icons");
                            str =
                                str
                                    + "{Icon<=>"
                                    + ID
                                    + "<=>Title"
                                    + Title.getText().toString()
                                    + "<=>Imgicon"
                                    + ID
                                    + "}!#";
                            if (str.startsWith("null")) {
                              str = str.replace("null", "");
                            }
                            utils.writeFile(rootAssetsPath + "files/icons/icons", str);
                            messageOk(FilesIcons.this, getString(R.string.a_added));
                            Refresh();
                            return;
                          }
                          showErrorDialog(FilesIcons.this);
                        }
                      };
                  new Thread() {
                    public void run() {
                      File destinationFile =
                          copyFileToCustomDirectory(
                              FilesIcons.this, uri, rootAssetsPath + "files/icons/", "icon" + ID);
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
    // مسیر فایل مقصد
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
    int filesCount = utils.getFileCount("icon", rootAssetsPath + iconsPath);
    viewCount.setText(String.valueOf(filesCount));

    LinearLayout empty = (LinearLayout) findViewById(R.id.emptyLayout);
    ImageView emptyImg = (ImageView) findViewById(R.id.emptyImg);
    TextView emptyText = (TextView) findViewById(R.id.emptyText);
    content.removeAllViews();
    final String data = utils.readFile(rootAssetsPath + "files/icons/icons");
    String str = data;
    final String Info = str;
    //	str = str.trim();
    final String[] durarray = str.split("\\!\\#");
    for (String part : durarray) {
      BiggroupButton2 button;
      String part2 = part;

      if (part2.contains("{Icon<=>")) {
        empty.setVisibility(empty.GONE);
        final String[] code = part2.replace("}", "").split("<=>");
        button = new BiggroupButton2(this);
        final String text = code[2].replace("Title", "");
        final String Img = code[3].replace("Img", "");
        button.setText(text);
        this.content.addView(button);
        button.setOnClickListener(
            new OnClickListener() {
              public void onClick(View v) {
                /*Intent intent = new Intent(FilesIcons.this, ImageViewer.class);
                intent.putExtra("actionBarColor","#008577");
                intent.putExtra("imgName","isIcon"+Img);
                startActivity(intent);*/
                Intent intent = new Intent(FilesIcons.this, PictureActivity.class);
                intent.putExtra("actionBarColor", "#008577");
                intent.putExtra("picName", "isIcon" + Img);
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
                    final AppCompatDialog dialog = new AppCompatDialog(FilesIcons.this);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.files_enter_info);
                    TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
                    title.setText(text);

                    final EditText Title = (EditText) dialog.findViewById(R.id.fileName);
                    Title.setText(text);
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
                              String str =
                                  Info.replace(
                                      "{Icon<=>"
                                          + code[1]
                                          + "<=>Title"
                                          + text
                                          + "<=>Img"
                                          + Img
                                          + "}!#",
                                      "{Icon<=>"
                                          + code[1]
                                          + "<=>Title"
                                          + Title.getText().toString()
                                          + "<=>Img"
                                          + Img
                                          + "}!#");
                              utils.writeFile(rootAssetsPath + "files/icons/icons", str);
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
                                    "{Icon<=>"
                                        + code[1]
                                        + "<=>Title"
                                        + text
                                        + "<=>Img"
                                        + Img
                                        + "}!#",
                                    "");
                            File file = new File(rootAssetsPath + "files/icons/" + Img);
                            file.delete();
                            utils.writeFile(rootAssetsPath + "files/icons/icons", str);
                            Refresh();
                            dialog.dismiss();
                          }
                        });

                    dialog.show();
                  }
                });
        File In = new File(rootAssetsPath + "files/icons/" + Img);
        button.setLeftIcon(In);
        button.setTextDesc(utils.getSizeLabel(In));
      } else {
        empty.setVisibility(empty.VISIBLE);
        emptyImg.setImageResource(R.drawable.ic_icons);
        emptyText.setText(getString(R.string.files_empty_icons));
      }
    }
  }
}
