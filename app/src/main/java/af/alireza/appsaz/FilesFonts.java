package af.alireza.appsaz;

import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.BiggroupButton2;
import af.alireza.views.MagicalTextView_Master;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesFonts extends BaseActivity {
  //	int PICK_FONT_REQUEST = 16;

  LinearLayout content;
  String ID;
  Utilities utils = new Utilities();
  int filesCount;

  private static final int FONT_PICK_REQUEST_CODE = 100;

  private void pickFont() {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    intent.setType("*/*");
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    String[] mimeTypes = {"font/ttf", "font/otf", "application/x-font-ttf"};
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
    startActivityForResult(intent, FONT_PICK_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == FONT_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
      Uri uri = data.getData();
      if (uri != null) {
        final AppCompatDialog dialog = new AppCompatDialog(FilesFonts.this);
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
                  ID = getSomeInt() + ".ttf";
                }
                if (Title.length() == 0) {
                  Title.setError(getString(R.string.a_error_name));
                } else {
                  final ProgressDialog progDailog =
                      ProgressDialog.show(FilesFonts.this, "", getString(R.string.a_loading), true);
                  final Handler handler =
                      new Handler() {
                        public void handleMessage(Message msg) {
                          if (new File(rootAssetsPath + "files/fonts/font" + ID).exists()) {
                            String str = utils.readFile(rootAssetsPath + "files/fonts/fonts");
                            str =
                                str
                                    + "{Font<=>"
                                    + ID
                                    + "<=>Title"
                                    + Title.getText().toString()
                                    + "<=>Fontfont"
                                    + ID
                                    + "}!#";
                            if (str.startsWith("null")) {
                              str = str.replace("null", "");
                            }
                            utils.writeFile(rootAssetsPath + "files/fonts/fonts", str);
                            messageOk(FilesFonts.this, getString(R.string.a_added));
                            Refresh();
                            return;
                          }
                          showErrorDialog(FilesFonts.this);
                        }
                      };
                  new Thread() {
                    public void run() {
                      File destinationFile =
                          copyFileToCustomDirectory(
                              FilesFonts.this, uri, rootAssetsPath + "files/fonts/", "font" + ID);

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.files);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    final ActionBar actionBar;
    actionBar = getSupportActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(R.string.app_editor_fonts);
    content = (LinearLayout) findViewById(R.id.filesContent);

    Refresh();
    MagicalTextView_Master ADD = (MagicalTextView_Master) findViewById(R.id.add);
    ADD.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (utils.checkPer(FilesFonts.this)) {
              if (filesCount > utils.fontCount) {
                showEndTrialDialog(FilesFonts.this);
              } else {
                pickFont();
              }
            }
          }
        });
  }

  public void Refresh() {
    TextView viewCount = findViewById(R.id.files_count);
    filesCount = utils.getFileCount("font", rootAssetsPath + fontsPath);
    viewCount.setText(String.valueOf(filesCount));
    LinearLayout empty = (LinearLayout) findViewById(R.id.emptyLayout);
    ImageView emptyImg = (ImageView) findViewById(R.id.emptyImg);
    TextView emptyText = (TextView) findViewById(R.id.emptyText);
    content.removeAllViews();
    String data = utils.readFile(rootAssetsPath + "files/fonts/fonts");
    String str = data;
    final String Info = str;
    //	str = str.trim();
    final String[] durarray = str.split("\\!\\#");
    for (String part : durarray) {
      BiggroupButton2 button;
      String part2 = part;
      if (part2.contains("{Font<=>")) {
        empty.setVisibility(View.GONE);
        final String[] code = part2.replace("}", "").split("<=>");
        button = new BiggroupButton2(this);
        final String text = code[2].replace("Title", "");
        final String Font = code[3].replace("Font", "");
        button.setText(text);
        button.setTextSize(22);
        final String font = rootAssetsPath + "files/fonts/" + Font;
        try {
          button.setTypeface(Typeface.createFromFile(font));
        } catch (Exception e) {
          button.setTextColor(Color.RED);
          Toast.makeText(
                  this,
                  "Font error: (" + text + ") " + e.getMessage().toString(),
                  Toast.LENGTH_SHORT)
              .show();
        }
        button.setLeftIcon(R.drawable.ic_fonts);
        button.getLeftIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorTint));
        this.content.addView(button);
        button.setOnClickListener(
            new OnClickListener() {
              public void onClick(View v) {
                File tf = new File(font);
                fontViewer(tf, text, "From hear");
              }
            });
        button.getRightIcon().setImageResource(R.drawable.ic_edit_file);
        button
            .getRightIcon()
            .setOnClickListener(
                new OnClickListener() {
                  @Override
                  public void onClick(View p1) {
                    final AppCompatDialog dialog = new AppCompatDialog(FilesFonts.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.files_enter_info);
                    TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
                    title.setText(text);
                    final EditText Title = (EditText) dialog.findViewById(R.id.fileName);
                    Title.setText(text);
                    TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
                    TextView dialog_Delete = (TextView) dialog.findViewById(R.id.delete);
                    TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
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
                                      "{Font<=>"
                                          + code[1]
                                          + "<=>Title"
                                          + text
                                          + "<=>Font"
                                          + Font
                                          + "}!#",
                                      "{Font<=>"
                                          + code[1]
                                          + "<=>Title"
                                          + Title.getText().toString()
                                          + "<=>Font"
                                          + Font
                                          + "}!#");
                              utils.writeFile(rootAssetsPath + "files/fonts/fonts", str);
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
                                    "{Font<=>"
                                        + code[1]
                                        + "<=>Title"
                                        + text
                                        + "<=>Font"
                                        + Font
                                        + "}!#",
                                    "");
                            File file = new File(rootAssetsPath + "files/fonts/" + Font);
                            file.delete();
                            utils.writeFile(rootAssetsPath + "files/fonts/fonts", str);
                            Refresh();
                            dialog.dismiss();
                          }
                        });
                    dialog.show();
                  }
                });
        File In = new File(rootAssetsPath + "files/fonts/" + Font);
        Utilities utils = new Utilities();
        button.setTextDesc(utils.getSizeLabel(In));
      } else {
        empty.setVisibility(empty.VISIBLE);
        emptyImg.setImageResource(R.drawable.ic_fonts);
        emptyText.setText(getString(R.string.files_empty_fonts));
      }
    }
  }

  public void fontViewer(final File font, String text, String type) {
    final AppCompatDialog dialog = new AppCompatDialog(FilesFonts.this);
    dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    dialog.setContentView(R.layout.files_font_viewer);
    TextView dtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
    dtitle.setText(text);
    final TextView Title = (TextView) dialog.findViewById(R.id.fontSimpleText);
    try {
      Title.setTypeface(Typeface.createFromFile(font));
    } catch (Exception e) {
      Toast.makeText(
              this, "Font error: (" + text + ") " + e.getMessage().toString(), Toast.LENGTH_SHORT)
          .show();
    }
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
    dialog_Save.setVisibility(View.GONE);
    dialog_Cancel.setText(getString(R.string.a_ok));
    dialog.show();
  }
}

/*	@Override
    public void onActivityResult1(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_FONT_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
			final String realPath = FilesPaths.getPath(this, data.getData());
			if (realPath.equals("NoPath")) {
			} else {
				final AppCompatDialog dialog = new AppCompatDialog(FilesFonts.this);

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
				dialog_Cancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							dialog.dismiss();
						}
					});
				dialog_Save.setOnClickListener(new View.OnClickListener() {	
						@Override
						public void onClick(View v) {
                           ID = getSomeInt() + ".ttf";
                            
							if (Title.length() == 0) {
								Title.setError(getString(R.string.a_error_name));
							} else {
								final ProgressDialog progDailog = ProgressDialog.show(FilesFonts.this, "", getString(R.string.a_loading), true);
								final Handler handler = new Handler() {
									public void handleMessage(Message msg) {
										if (new File(rootAssetsPath + "files/fonts/font" + ID).exists()) {
											String str = utils.readFile(rootAssetsPath + "files/fonts/fonts");
											str = str + "{Font<=>" + ID + "<=>Title" + Title.getText().toString() + "<=>Fontfont" + ID + "}!#";
											if (str.startsWith("null")) {
												str = str.replace("null", "");
											}
											utils.writeFile(rootAssetsPath + "files/fonts/fonts", str);
											messageOk(FilesFonts.this, getString(R.string.a_added));
											Refresh();
											return;
										}
										showErrorDialog(FilesFonts.this);
									}
								};
								new Thread() {
									public void run() {
										try {

											getFile(realPath, rootAssetsPath + "files/fonts/font" + ID);

										} catch (IOException e) {

										}
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
		}}

	public void getFile(String in, String out) throws IOException {
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
	}
*/