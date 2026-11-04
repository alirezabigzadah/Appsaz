package af.alireza.appsaz;

import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.BiggroupButton2;
import af.alireza.views.MagicalTextView_Master;
import af.appsaz.myapplication.AudioActivity;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesAudios extends BaseActivity {

  int PICK_AUDIO_REQUEST = 1;
  LinearLayout content;
  String Image = "Default";
  String ID;
  Picker picker;
  Utilities utils = new Utilities();

  private void openAudioPicker() {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    intent.setType("audio/*"); // فقط فایل‌های صوتی
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    startActivityForResult(intent, PICK_AUDIO_REQUEST);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
      Uri uri = data.getData();
      if (uri != null) {
        final AppCompatDialog dialog = new AppCompatDialog(FilesAudios.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.files_enter_info);
        TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
        title.setText(getString(R.string.files_add_audio));
        final LinearLayout moreOptions = (LinearLayout) dialog.findViewById(R.id.moreOptions);
        moreOptions.setVisibility(moreOptions.VISIBLE);
        final EditText Title = (EditText) dialog.findViewById(R.id.fileName);
        final MaterialSwitch saveAble = (MaterialSwitch) dialog.findViewById(R.id.fileSaveable);
        final MaterialSwitch shareAble = (MaterialSwitch) dialog.findViewById(R.id.fileShareable);
        LinearLayout dialog_LayoutImg = (LinearLayout) dialog.findViewById(R.id.layoutAudioImg);
        final TextView dialog_ImgText = (TextView) dialog.findViewById(R.id.audioImg);
        final ImageView dialog_Img = (ImageView) dialog.findViewById(R.id.audioImage);
        Button dialog_Cancel = dialog.findViewById(R.id.cancel);
        Button dialog_Delete = dialog.findViewById(R.id.delete);
        Button dialog_Save = dialog.findViewById(R.id.save);
        dialog_LayoutImg.setVisibility(View.VISIBLE);
        dialog_Delete.setVisibility(View.GONE);

        dialog_ImgText.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                final AppCompatDialog dialog = new AppCompatDialog(FilesAudios.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog);
                TextView dtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
                dtitle.setText(
                    getString(R.string.files_audio_pic)
                        + getString(R.string.a_split)
                        + dialog_ImgText.getText().toString());
                LinearLayout dialog_Content =
                    (LinearLayout) dialog.findViewById(R.id.dialogContent);
                BiggroupButton2 noneBtn = new BiggroupButton2(FilesAudios.this);
                dialog_Content.addView(noneBtn);
                noneBtn.setText(getString(R.string.a_default));
                noneBtn.setLeftIcon(R.drawable.default_audio_img);
                noneBtn.setOnClickListener(
                    new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        Image = "Default";
                        dialog_ImgText.setText(getString(R.string.a_default));
                        Glide.with(FilesAudios.this)
                            .load(R.drawable.default_audio_img)
                            .placeholder(R.drawable.none)
                            .into(dialog_Img);
                        dialog.dismiss();
                      }
                    });
                try {
                  String fileText = utils.readFile(rootAssetsPath + "files/images/images");
                  String[] texts = fileText.split("\\!\\#");
                  BiggroupButton2 btn;
                  for (String p1 : texts) {
                    String p = p1;
                    if (p.contains("{Image<=>")) {
                      btn = new BiggroupButton2(FilesAudios.this);
                      String[] code = p.replace("}", "").split("<=>");
                      String ItemeId = code[1];
                      final String title = code[2].replace("Title", "");
                      final String file = code[3].replace("Img", "");
                      File In = new File(rootAssetsPath + imagesPath + file);
                      btn.setLeftIcon(In);
                      btn.setText(title);
                      btn.setOnClickListener(
                          new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              Image = file;
                              dialog_ImgText.setText(title);
                              File In = new File(rootAssetsPath + imagesPath + file);
                              Glide.with(FilesAudios.this)
                                  .load(In)
                                  .placeholder(R.drawable.none)
                                  .into(dialog_Img);
                              dialog.dismiss();
                            }
                          });
                      dialog_Content.addView(btn);
                    }
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
                dialog.show();
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
                String realPath = utils.getPathFromUri(getApplicationContext(), uri);
                if (realPath != null) {
                  ID = getSomeInt() + utils.getFileExtensionWithApache(realPath);
                } else {
                  ID = getSomeInt() + ".mp3";
                }

                if (Title.length() == 0) {
                  Title.setError(getString(R.string.a_error_name));
                } else {

                  final ProgressDialog progDailog =
                      ProgressDialog.show(
                          FilesAudios.this, "", getString(R.string.a_loading), true);
                  final Handler handler =
                      new Handler() {
                        public void handleMessage(Message msg) {
                          if (new File(rootAssetsPath + "files/audios/audio" + ID).exists()) {

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
                            String str = utils.readFile(rootAssetsPath + "files/audios/audios");
                            str =
                                str
                                    + "{Audio<=>"
                                    + ID
                                    + "<=>Title"
                                    + Title.getText().toString()
                                    + "<=>Audioaudio"
                                    + ID
                                    + "<=>AudioImg"
                                    + Image
                                    + "<=>SaveAble"
                                    + saveable
                                    + "<=>ShareAble"
                                    + shareable
                                    + "}!#";
                            if (str.startsWith("null")) {
                              str = str.replace("null", "");
                            }
                            utils.writeFile(rootAssetsPath + "files/audios/audios", str);
                            messageOk(FilesAudios.this, getString(R.string.a_added));
                            Refresh();
                            return;
                          }
                          showErrorDialog(FilesAudios.this);
                        }
                      };
                  new Thread() {
                    public void run() {
                      File destinationFile =
                          copyFileToCustomDirectory(
                              FilesAudios.this,
                              uri,
                              rootAssetsPath + "files/audios/",
                              "audio" + ID);

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.files);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    final ActionBar actionBar;
    actionBar = getSupportActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(R.string.app_editor_audios);

    picker = new Picker();
    content = (LinearLayout) findViewById(R.id.filesContent);

    Refresh();
    MagicalTextView_Master ADD = (MagicalTextView_Master) findViewById(R.id.add);
    ADD.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (utils.checkPer(FilesAudios.this)) {
              openAudioPicker();
            }
            
          }
        });
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
    int filesCount = utils.getFileCount("audio", rootAssetsPath + audiosPath);
    viewCount.setText(String.valueOf(filesCount));

    LinearLayout empty = (LinearLayout) findViewById(R.id.emptyLayout);
    ImageView emptyImg = (ImageView) findViewById(R.id.emptyImg);
    TextView emptyText = (TextView) findViewById(R.id.emptyText);
    content.removeAllViews();
    final String data = utils.readFile(rootAssetsPath + "files/audios/audios");

    String str = data;
    final String Info = str;
    //	str = str.trim();
    final String[] durarray = str.split("\\!\\#");
    for (String part : durarray) {
      BiggroupButton2 button;
      String part2 = part;

      if (part2.contains("{Audio<=>")) {
        empty.setVisibility(empty.GONE);
        final String[] code = part2.replace("}", "").split("<=>");
        button = new BiggroupButton2(this);
        final String text = code[2].replace("Title", "");
        final String audio = code[3].replace("Audio", "");
        final String audioImg = code[4].replace("AudioImg", "");
        final String Saveable = code[5].replace("SaveAble", "");
        final String Shareable = code[6].replace("ShareAble", "");

        button.setText(text);
        if (new File(rootAssetsPath + imagesPath + audioImg).exists()) {
          File In = new File(rootAssetsPath + imagesPath + audioImg);
          button.setLeftIcon(In);
        } else {
          button.setLeftIcon(R.drawable.default_audio_img);
        }
        this.content.addView(button);
        button.setOnClickListener(
            new OnClickListener() {
              public void onClick(View v) {
                Intent intent = new Intent(FilesAudios.this, AudioActivity.class);
                intent.putExtra("actionBarColor", "#008577");
                intent.putExtra("audioName", audio);
                startActivity(intent);
              }
            });
        button.getRightIcon().setImageResource(R.drawable.ic_edit_file);
        button
            .getRightIcon()
            .setOnClickListener(
                new OnClickListener() {
                  @Override
                  public void onClick(View v1) {
                    final AppCompatDialog dialog = new AppCompatDialog(FilesAudios.this);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.files_enter_info);
                    TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
                    title.setText(text);
                    final LinearLayout moreOptions =
                        (LinearLayout) dialog.findViewById(R.id.moreOptions);
                    moreOptions.setVisibility(moreOptions.VISIBLE);
                    final EditText Title = (EditText) dialog.findViewById(R.id.fileName);
                    Title.setText(text);
                    LinearLayout dialog_LayoutImg =
                        (LinearLayout) dialog.findViewById(R.id.layoutAudioImg);
                    final TextView dialog_ImgText = (TextView) dialog.findViewById(R.id.audioImg);
                    final ImageView dialog_Img = (ImageView) dialog.findViewById(R.id.audioImage);
                    final MaterialSwitch saveAble = dialog.findViewById(R.id.fileSaveable);
                    final MaterialSwitch shareAble = dialog.findViewById(R.id.fileShareable);
                    Button dialog_Cancel = dialog.findViewById(R.id.cancel);
                    Button dialog_Delete = dialog.findViewById(R.id.delete);
                    Button dialog_Save = dialog.findViewById(R.id.save);
                    dialog_LayoutImg.setVisibility(View.VISIBLE);
                    dialog_Delete.setVisibility(View.VISIBLE);
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
                    String str = audioImg;
                    Image = audioImg;
                    //  Toast.makeText(getApplication(), Image, Toast.LENGTH_SHORT).show();
                    if (picker.getImageNameById(str).startsWith("none")) {
                      Image = "Default";
                      dialog_ImgText.setText(getString(R.string.a_default));
                      Glide.with(FilesAudios.this)
                          .load(R.drawable.default_audio_img)
                          .placeholder(R.drawable.none)
                          .into(dialog_Img);

                    } else {
                      dialog_ImgText.setText(picker.getImageNameById(str));
                      File In = new File(rootAssetsPath + imagesPath + str);
                      Glide.with(FilesAudios.this)
                          .load(In)
                          .placeholder(R.drawable.none)
                          .into(dialog_Img);
                    }

                    dialog_LayoutImg.setOnClickListener(
                        new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                            final AppCompatDialog dialog = new AppCompatDialog(FilesAudios.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().getAttributes().windowAnimations =
                                R.style.dialog_anim;
                            dialog
                                .getWindow()
                                .setBackgroundDrawableResource(android.R.color.transparent);
                            dialog.setContentView(R.layout.dialog);
                            TextView dtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
                            dtitle.setText(
                                getString(R.string.files_audio_pic)
                                    + getString(R.string.a_split)
                                    + dialog_ImgText.getText().toString());
                            LinearLayout dialog_Content =
                                (LinearLayout) dialog.findViewById(R.id.dialogContent);
                            BiggroupButton2 noneBtn = new BiggroupButton2(FilesAudios.this);
                            dialog_Content.addView(noneBtn);
                            noneBtn.setText(getString(R.string.a_default));
                            noneBtn.setLeftIcon(R.drawable.default_audio_img);
                            noneBtn.setOnClickListener(
                                new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                    Image = "Default";
                                    dialog_ImgText.setText(getString(R.string.a_default));
                                    Glide.with(FilesAudios.this)
                                        .load(R.drawable.default_audio_img)
                                        .placeholder(R.drawable.none)
                                        .into(dialog_Img);
                                    dialog.dismiss();
                                  }
                                });
                            try {
                              String fileText =
                                  utils.readFile(rootAssetsPath + "files/images/images");
                              String[] texts = fileText.split("\\!\\#");
                              BiggroupButton2 btn;
                              for (String p1 : texts) {
                                String p = p1;
                                if (p.contains("{Image<=>")) {
                                  btn = new BiggroupButton2(FilesAudios.this);
                                  String[] code = p.replace("}", "").split("<=>");
                                  String ItemeId = code[1];
                                  final String title = code[2].replace("Title", "");
                                  final String file = code[3].replace("Img", "");
                                  File In = new File(rootAssetsPath + imagesPath + file);
                                  btn.setLeftIcon(In);
                                  btn.setText(title);
                                  btn.setOnClickListener(
                                      new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                          Image = file;
                                          dialog_ImgText.setText(title);
                                          File In = new File(rootAssetsPath + imagesPath + file);
                                          Glide.with(FilesAudios.this)
                                              .load(In)
                                              .placeholder(R.drawable.none)
                                              .into(dialog_Img);
                                          dialog.dismiss();
                                        }
                                      });
                                  dialog_Content.addView(btn);
                                }
                              }
                            } catch (Exception e) {
                              e.printStackTrace();
                            }
                            dialog.show();
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
                                      "{Audio<=>"
                                          + code[1]
                                          + "<=>Title"
                                          + text
                                          + "<=>Audio"
                                          + audio
                                          + "<=>AudioImg"
                                          + audioImg
                                          + "<=>SaveAble"
                                          + Saveable
                                          + "<=>ShareAble"
                                          + Shareable
                                          + "}!#",
                                      "{Audio<=>"
                                          + code[1]
                                          + "<=>Title"
                                          + Title.getText().toString()
                                          + "<=>Audio"
                                          + audio
                                          + "<=>AudioImg"
                                          + Image
                                          + "<=>SaveAble"
                                          + saveable
                                          + "<=>ShareAble"
                                          + shareable
                                          + "}!#");
                              utils.writeFile(rootAssetsPath + "files/audios/audios", str);
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
                                    "{Audio<=>"
                                        + code[1]
                                        + "<=>Title"
                                        + text
                                        + "<=>Audio"
                                        + audio
                                        + "<=>AudioImg"
                                        + audioImg
                                        + "<=>SaveAble"
                                        + Saveable
                                        + "<=>ShareAble"
                                        + Shareable
                                        + "}!#",
                                    "");
                            File file = new File(rootAssetsPath + "files/audios/" + audio);
                            file.delete();
                            utils.writeFile(rootAssetsPath + "files/audios/audios", str);
                            Refresh();
                            dialog.dismiss();
                          }
                        });
                    dialog.show();
                  }
                });
        File In = new File(rootAssetsPath + "files/audios/" + audio);
        Utilities utils = new Utilities();
        button.setTextDesc(utils.getSizeLabel(In));
      } else {
        empty.setVisibility(empty.VISIBLE);
        emptyImg.setImageResource(R.drawable.ic_audios);
        emptyText.setText(getString(R.string.files_empty_audios));
      }
    }
  }
}
