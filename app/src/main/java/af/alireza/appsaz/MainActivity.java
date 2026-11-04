package af.alireza.appsaz;

import af.alireza.appsaz.Adapters.FAQAdapter;
import af.alireza.appsaz.Adapters.FAQItem;
import af.alireza.appsaz.Utilities.Prefs;
import af.alireza.appsaz.Utilities.SignUtil;
import af.alireza.appsaz.Utilities.Utilities;
import af.appsaz.myapplication.SplashActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.apk.axml.aXMLDecoder;
import com.apk.axml.aXMLEncoder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends BaseActivity {

  private static String PACKAGE_NAME_PATTERN = "^[a-zA-Z]+(\\.[a-zA-Z][a-zA-Z0-9]*)+$";
  private static final int FILE_PICK_REQUEST_CODE = 101;
  private String version = "350";
  private String progressValue = "";
  private int projectVersion;
  private boolean projectIsOpen;
  // char []ch={'m','m'};
  private String imagesPath = "files/images/";
  private String audiosPath = "files/audios/";
  private String videosPath = "files/videos/";
  private TextView appName;
  private TextView packageName;
  private ImageView icLauncher;
  private ViewSwitcher view_switcher;
  private MaterialToolbar toolbar;
  private String app_name;
  private String package_name;
  private int version_code = 1;
  private String version_name = "1.0";
  private int progressBarStatus = 0;
  private int fileSize = 0;
  private Prefs prefs = new Prefs();
  private Utilities utils = new Utilities();
  private Picker picker = new Picker();

  private void pickProject() {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    intent.setType("*/*"); // تمام انواع فایل‌ها
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    // فیلتر کردن فقط فایل‌های .as
    String[] mimeTypes = {"application/octet-stream", "application/x-as"};
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
    startActivityForResult(intent, FILE_PICK_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == FILE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
      Uri uri = data.getData();
      if (uri != null) {
        // کپی فایل به پوشه خصوصی
        File destinationFile = utils.copyFileToCustomDirectory(this, uri, buildDirectory, "project.as");
        OpenProject(destinationFile);
      }
    }
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    navMenu();

    boolean showDialog = prefs.getBoolean(this, "showFirstText", true);
    if (showDialog) showFreeTrialText(MainActivity.this);
    WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    view_switcher = (ViewSwitcher) findViewById(R.id.vsw_switcher);
    //	File root1 = android.os.Environment.getExternalStorageDirectory();
    // File root = new File(getApplication().getExternalCacheDir().toString());
    appName = (TextView) findViewById(R.id.appAppName);
    packageName = (TextView) findViewById(R.id.appPackageName);
    icLauncher = (ImageView) findViewById(R.id.appIcLauncher);

    Refresh();
    FrameLayout createProject = (FrameLayout) findViewById(R.id.createProject);
    createProject.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            if (utils.checkPer(MainActivity.this)) {
              File createDire = new File(rootdirectory);
              if (createDire.isDirectory() || createDire.mkdirs()) {
                createNewApp(MainActivity.this);
              }
            }
          }
        });
    FrameLayout openProject = (FrameLayout) findViewById(R.id.openProject);
    openProject.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
                  //  throw new RuntimeException("Test Crash");
            if (utils.checkPer(MainActivity.this)) {
            pickProject();
            }
          }
        });
    FrameLayout editApp = (FrameLayout) findViewById(R.id.editApp);
    editApp.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, AppEditor.class));
          }
        });
    FrameLayout runApp = (FrameLayout) findViewById(R.id.runApp);
    runApp.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
          }
        });
    FrameLayout build = (FrameLayout) findViewById(R.id.buildApp);
    build.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            File createDire = new File(buildDirectory);
            if (createDire.isDirectory() || createDire.mkdirs()) {
              int pageCount = utils.getFileCount("page", rootAssetsPath);
              int listCount = utils.getListCount("list", rootAssetsPath);
              int fontCount = utils.getFileCount("font", rootAssetsPath + fontsPath);
              if (pageCount > 20
                  || listCount > 10
                  || fontCount > 10) {
                showEndTrialDialog(MainActivity.this);
              } else {
                String customKeyPath = prefs.getString(MainActivity.this, "key_path", "");
                if (customKeyPath.equals("")) {
                  Compile();
                } else {
                  File ks = new File(customKeyPath);
                  if (ks.exists()) {
                    Compile();
                  } else {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle(getString(R.string.a_error))
                        .setMessage(getString(R.string.key_not_found))
                        .setPositiveButton(getString(R.string.a_ok), null)
                        .setCancelable(false)
                        .show();
                  }
                }
              }
            }
          }
        });
    FrameLayout closeProject = (FrameLayout) findViewById(R.id.closeProject);
    closeProject.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle(getString(R.string.main_close_project))
                .setMessage(getString(R.string.main_close_project_alert))
                .setPositiveButton(
                    R.string.a_yes,
                    new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        utils.deleteDir(new File(rootdirectory + "MyApp"));
                        new File(rootdirectory + "Project.pro").delete();
                        Refresh();
                      }
                    })
                .setNeutralButton(
                    getString(R.string.main_save_project),
                    new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                        File createDire = new File(appsazDownloadPath);
                        if (createDire.isDirectory() || createDire.mkdirs()) {
                          String str =
                              "{Project<=>Name"
                                  + app_name
                                  + "<=>PackageName"
                                  + package_name
                                  + "<=>VersionCode"
                                  + version_code
                                  + "<=>VersionName"
                                  + version_name
                                  + "<=>ProjectVersion"
                                  + 3;
                          utils.writeFile(rootdirectory + "Project.pro", str);
                           if (utils.checkPer(MainActivity.this)) {
                          SaveAppProject();
                         }
                        }
                      }
                    })
                .setNegativeButton(
                    R.string.a_no,
                    new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                      }
                    })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
          }
        });
    FrameLayout saveProject = (FrameLayout) findViewById(R.id.saveProject);
    saveProject.setOnClickListener(
        new OnClickListener() {
          public void onClick(View v) {
            new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle(getString(R.string.main_save_project))
                .setMessage(getString(R.string.main_save_project_alert))
                .setPositiveButton(
                    R.string.a_yes,
                    new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                        File createDire = new File(appsazDownloadPath);
                        if (createDire.isDirectory() || createDire.mkdirs()) {
                          String str =
                              "{Project<=>Name"
                                  + app_name
                                  + "<=>PackageName"
                                  + package_name
                                  + "<=>VersionCode"
                                  + version_code
                                  + "<=>VersionName"
                                  + version_name
                                  + "<=>ProjectVersion"
                                  + 3;
                          utils.writeFile(rootdirectory + "Project.pro", str);
                           if (utils.checkPer(MainActivity.this)) {
                          SaveAppProject();
                         }
                        }
                      }
                    })
                .setNegativeButton(
                    R.string.a_cancel,
                    new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                      }
                    })
                .show();
          }
        });
    File rootDire = new File(rootdirectory);
    if (rootDire.isDirectory() || rootDire.mkdirs()) {
      try {
        utils.copyFromAssets(this, "pro", rootdirectory + "Pro.pro");
        // utils.copyFromAssets(this, "ma.ma", rootdirectory + "MyApp.apk");
      } catch (IOException e) {
      }
    }
    File localDire = new File(buildDirectory);
    if (localDire.isDirectory() || localDire.mkdirs()) {}
  }

  public void Compile() {
    Refresh();
    final ProgressDialog progressBar = new ProgressDialog(this);
    progressBar.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
    progressBar.setCancelable(false);
    progressBar.setTitle(getString(R.string.main_building_app) + app_name);
    progressBar.setMessage(
        getString(R.string.main_building_app) + app_name + "\n" + getString(R.string.a_wait));
    progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressBar.setProgress(1);
    progressBar.setMax(100);
    progressBar.show();
    progressBarStatus = 0;
    progressValue = "";
    fileSize = 0;
    final Handler handler =
        new Handler() {
          public void handleMessage(Message msg) {
            String signedApk = buildDirectory + "MyApplication-signed.apk";
            moveFileToDownloads(
                MainActivity.this, new File(signedApk), "Appsaz", app_name + ".apk", "apk");
          }
        };
    new Thread(
            new Runnable() {
              public void run() {
                while (progressBarStatus < 100) {
                  progressBarStatus = doSomeTasks();
                  try {
                    Thread.sleep(500);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  handler.post(
                      new Runnable() {
                        public void run() {
                          progressBar.setProgress(progressBarStatus);
                          progressBar.setMessage(progressValue);
                        }
                      });
                }
                if (progressBarStatus >= 100) {
                  try {
                    Thread.sleep(500);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  handler.sendEmptyMessage(0);
                  progressBar.dismiss();
                }
              }
            })
        .start();
  }

  public int doSomeTasks() {
    String unsignedApk = buildDirectory + "MyApplication-unsigned.apk";
    String signedApk = buildDirectory + "MyApplication-signed.apk";
    while (fileSize <= 100) {
      fileSize++;
      if (fileSize == 10) {
        progressValue =
            getString(R.string.main_collecting_data) + "\n" + getString(R.string.a_wait);
        return 10;
      } else if (fileSize == 20) {
        progressValue =
            getString(R.string.main_collecting_data) + "\n" + getString(R.string.a_wait);
        /*	try {
        utils.copyFile(
        (rootdirectory + "MyApp/res/drawable/ic_launcher.png"),
        (rootdirectory + "ic_launcher.png"));
        //	utils.unzip(new File(rootdirectory + "MyApp.apk"),new File(rootdirectory+"MyApp/"));
        utils.copyFile(rootdirectory + "ic_launcher.png",rootdirectory + "MyApp/res/drawable/ic_launcher.png");
        } catch (IOException e) {
        }*/
        return 20;
      } else if (fileSize == 50) {
        progressValue = getString(R.string.main_encoding) + "\n" + getString(R.string.a_wait);

        try {
          utils.copyFromAssets(this, "man", rootdirectory + "AndroidManifest.xml");
          String manifestData = decodeXML(rootdirectory + "AndroidManifest.xml");
          manifestData = manifestData.replace("APPLICATION_NAME", app_name);
          manifestData = manifestData.replace("PACKAGE_NAME", package_name);
          // String st = String.valueOf(version_code);
          manifestData = manifestData.replace("786786786", "" + version_code);
          manifestData = manifestData.replace("VERSION_NAME", version_name);
          manifestData = manifestData.replace("ADMOB_APPLICATION_ID", picker.getAdmobAppID());

          encodeXML(manifestData, rootdirectory + "AndroidManifest.xml");
        } catch (Exception e) {

        }
        /*	byte[] data = (byte[]) null;
        try {
        InputStream open2 = getAssets().open("Manifest.xml");
        int available = open2.available();
        Log.i("ZZ", String.valueOf(available));
        byte[] mByte = new byte[available];
        open2.read(mByte);
        open2.close();
        data = mByte;
        modifyAndroidManifest(data, app_name, package_name, version_code, version_name);
        } catch (IOException e) {
        }*/
        return 50;
      } else if (fileSize == 70) {
        try {
          progressValue =
              getString(R.string.main_compressing_data) + "\n" + getString(R.string.a_wait);
          utils.copyFromAssets(this, "ma", unsignedApk);

          String manifest = rootdirectory + "AndroidManifest.xml";
          String assets = rootdirectory + "MyApp/assets";
          try {
            long modifiedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1981-01-01 01:01:02").getTime();
           
            ZipFile zipFile = new ZipFile(unsignedApk);
                        
        Map<String, String> fileMappings = new HashMap<>();
        String resPath = rootdirectory + "MyApp/res/";
                        
        fileMappings.put(resPath+"mipmap-mdpi/ic_launcher.png", "res/u3.png");
        fileMappings.put(resPath+"mipmap-hdpi/ic_launcher.png", "res/SD.png");
        fileMappings.put(resPath+"mipmap-xhdpi/ic_launcher.png", "res/jy.png");
        fileMappings.put(resPath+"mipmap-xxhdpi/ic_launcher.png", "res/D2.png");
        fileMappings.put(resPath+"mipmap-xxxhdpi/ic_launcher.png", "res/CG.png");

        fileMappings.put(resPath+"mipmap-mdpi/ic_launcher_background.png", "res/cZ.png");
        fileMappings.put(resPath+"mipmap-hdpi/ic_launcher_background.png", "res/ik.png");
        fileMappings.put(resPath+"mipmap-xhdpi/ic_launcher_background.png", "res/63.png");
        fileMappings.put(resPath+"mipmap-xxhdpi/ic_launcher_background.png", "res/B1.png");
        fileMappings.put(resPath+"mipmap-xxxhdpi/ic_launcher_background.png", "res/DF.png");
                        
        fileMappings.put(resPath+"mipmap-mdpi/ic_launcher_foreground.png", "res/0y.png");
        fileMappings.put(resPath+"mipmap-hdpi/ic_launcher_foreground.png", "res/Mb.png");
        fileMappings.put(resPath+"mipmap-xhdpi/ic_launcher_foreground.png", "res/kb.png");
        fileMappings.put(resPath+"mipmap-xxhdpi/ic_launcher_foreground.png", "res/_e.png");
        fileMappings.put(resPath+"mipmap-xxxhdpi/ic_launcher_foreground.png", "res/Et.png");
                                      
        // اضافه کردن فایل‌ها به ZIP
        for (Map.Entry<String, String> entry : fileMappings.entrySet()) {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setFileNameInZip(entry.getValue());
            zipParameters.setLastModifiedFileTime(modifiedTime);
            zipFile.addFile(new File(entry.getKey()), zipParameters);
                        
            }
            ZipParameters assetsParams = new ZipParameters();
            assetsParams.setLastModifiedFileTime(modifiedTime); // همان تاریخ
            zipFile.addFolder(new File(assets), assetsParams);
            ZipParameters manifestParams = new ZipParameters();
            manifestParams.setLastModifiedFileTime(modifiedTime); // همان تاریخ
            zipFile.addFile(new File(manifest), manifestParams);

            System.out.println("files added successfully");
          } catch (ZipException e) {
            System.err.println("error while adding files: " + e.getMessage());
          } catch (ParseException e) {
            System.err.println("date parsing error: " + e.getMessage());
          }
          // utils.compressSingleFile(rootdirectory + "MyApp/", localDirectory + app_name +
          // "1.apk");
          // compressDirectoryToApk(rootdirectory + "MyApp/", localDirectory + app_name + "1.apk");
        } catch (IOException e) {
        }
        return 70;
      } else if (fileSize == 80) {
        progressValue = getString(R.string.main_signing) + "\n" + getString(R.string.a_wait);

        String customKeyPath = prefs.getString(MainActivity.this, "key_path", "");
        if (customKeyPath.equals("")) {
          try {
            SignUtil.loadKey(
                getApplicationContext().getAssets(),
                this,
                new SignUtil.LoadKeyCallback() {
                  @Override
                  public void call(SignUtil signTool) {
                    signTool.sign(new File(unsignedApk), new File(signedApk), 21);
                    new File(unsignedApk).delete();
                  }
                });
          } catch (Exception e) {
            System.err.println("apk signing error: " + e.getMessage());
          }
        } else {
          String keyAlias = prefs.getString(MainActivity.this, "key_alias", "");
          String keyAliasPass = prefs.getString(MainActivity.this, "key_alias_pass", "");
          String keyPass = prefs.getString(MainActivity.this, "key_pass", "");
          boolean isSigned =
              SignUtil.signWithJKS(
                  getApplicationContext(),
                  new File(unsignedApk),
                  new File(signedApk),
                  21,
                  new File(customKeyPath),
                  keyAlias,
                  keyAliasPass,
                  keyPass);
          if (isSigned) {
            System.out.println("signing task successed.");
            new File(unsignedApk).delete();
          } else {
            System.out.println("apk signing error");
          }
        }

        return 80;
      } else if (fileSize == 90) {
        progressValue = getString(R.string.main_finishing);
        new File(rootdirectory + "AndroidManifest.xml").delete();
        /*	utils.deleteDir(new File(rootdirectory + "MyApp/res/"));
        new File(rootdirectory + "MyApp/AndroidManifest.xml").delete();
        new File(rootdirectory + "MyApp/resources.arsc").delete();
        new File(rootdirectory + "MyApp/classes.dex").delete();
        // new File(rootdirectory+"key.key").delete();
        utils.move(
        new File(rootdirectory + "ic_launcher.png"),
        new File(rootdirectory + "MyApp/res/drawable/ic_launcher.png"));
        */
        return 90;
      }
    }
    return 100;
  }

  public void SaveAppProject() {
    final ProgressDialog progDailog =
        ProgressDialog.show(
            this, "", getString(R.string.main_saving_project) + " " + app_name, true);
    final Handler handler =
        new Handler() {
          public void handleMessage(Message msg) {
            moveFileToDownloads(
                MainActivity.this,
                new File(buildDirectory + "/project.as"),
                "Appsaz",
                app_name + ".as",
                "as");
          }
        };
    new Thread() {
      public void run() {
        try {
          utils.compressFolderAndFile(
              rootdirectory + "MyApp",
              rootdirectory + "Project.pro",
              buildDirectory + "/project.as");
          System.out.println("Compression completed successfully!");
        } catch (Exception e) {
          e.printStackTrace();
          System.err.println("Error during compression: " + e.getMessage());
        }
        //	String files[] = {rootdirectory + "MyApp", rootdirectory + "Project.pro"};
        //	utils.compressFiles(files, appsazDownloadPath + "/" + app_name + ".as");
        handler.sendEmptyMessage(0);
        progDailog.dismiss();
      }
    }.start();
  }

  /*
  private File signApk (File unsignedApk , String signedName )
  throws IllegalAccessException, InstantiationException,
  ClassNotFoundException, IOException,
  GeneralSecurityException {
  File signedApkFile = new File(signedName);
  ZipSigner zipSigner = new ZipSigner();
  zipSigner . setKeymode ( "auto-testkey" );
  zipSigner . signZip ( unsignedApk . getPath (),signedApkFile . getPath());
  return signedApkFile ;
  }*/
  public void onResume() {
    super.onResume();
    Refresh();
  }

  private void Refresh() {
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
        if (fileText.contains("ProjectVersion")) {
          String ProjectVersion = code[5].replace("ProjectVersion", "");
          projectVersion = Integer.parseInt(ProjectVersion);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    int id = view_switcher.getCurrentView().getId();
    File projectFile = new File(rootdirectory + "Project.pro");
    if (projectFile.exists()) {
      File In = new File(rootdirectory + "MyApp/res/mipmap-xxxhdpi/ic_launcher.png");
      Glide.with(MainActivity.this)
          .load(In)
          .diskCacheStrategy(DiskCacheStrategy.NONE)
          .skipMemoryCache(true)
          .placeholder(R.drawable.none)
          .into(icLauncher);
      projectIsOpen = true;
      if (id == R.id.mainNotOpenedProject) {
        view_switcher.showPrevious();
      }
    } else {
      projectIsOpen = false;
      if (id == R.id.mainOpenedProject) {
        view_switcher.showNext();
      }
    }
  }

  public void createNewApp(Context context) {
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
    LayoutInflater inflater = LayoutInflater.from(context);
    View dialogView = inflater.inflate(R.layout.dialog_inter_new_app, null);
    builder.setView(dialogView);
    TextInputLayout appNameLayout = dialogView.findViewById(R.id.text_input_layout_app_name);
    TextInputEditText appNameInput = dialogView.findViewById(R.id.text_input_app_name);
    appNameInput.setText(R.string.main_new_app_name);
    TextInputLayout packageNameLayout = dialogView.findViewById(R.id.text_input_layout_package_name);
    TextInputEditText packageNameInput = dialogView.findViewById(R.id.text_input_package_name);
    packageNameInput.setText(R.string.main_new_package);
    builder.setTitle(R.string.main_create_app);
    builder.setPositiveButton(R.string.a_create, null);
    builder.setNegativeButton(R.string.a_cancel, (dialog, which) -> dialog.dismiss());
    AlertDialog dialog = builder.create();
    dialog.setOnShowListener(
        dialogInterface -> {
          dialog
              .getButton(AlertDialog.BUTTON_POSITIVE)
              .setOnClickListener(
                  v -> {
                    String appName = appNameInput.getText().toString().trim();
                    String packageName = packageNameInput.getText().toString().trim();
                    boolean hasError = false;
                    if (TextUtils.isEmpty(appName)) {
                      appNameLayout.setError(getString(R.string.main_error_app_name));
                      hasError = true;
                    } else {
                      appNameLayout.setError(null); // پاک کردن خطا
                    }
                    if (TextUtils.isEmpty(packageName)) {
                      packageNameLayout.setError(getString(R.string.main_error_package_name));
                      hasError = true;
                    } else if (!isValidPackageName(packageName)) {
                      packageNameLayout.setError(
                          getString(R.string.main_error_incorrect_package_name));
                      hasError = true;
                    } else if (utils.isPackageNameReserved(packageName)) {
                      packageNameLayout.setError(
                          getString(R.string.main_error_reserved_package_name));
                      hasError = true;
                    } else {
                      packageNameLayout.setError(null); // پاک کردن خطا
                    }
                    if (!hasError) {
                      dialog.dismiss();
                      onValidationSuccess(appName, packageName);
                    }
                  });
        });

    dialog.show();
  }

  private boolean isValidPackageName(String packageName) {
    return Pattern.matches(PACKAGE_NAME_PATTERN, packageName);
  }

  private void onValidationSuccess(String appName, String packageName) {
    // اینجا عملیات مورد نظر خود را انجام دهید
    System.out.println("App Name: " + appName);
    System.out.println("Package Name: " + packageName);

    File createDire = new File(rootdirectory);
    if (createDire.isDirectory() || createDire.mkdirs()) {
      try {
        utils.extractZipFile(rootdirectory + "Pro.pro", rootdirectory);
      } catch (ZipException e) {
      }
      String app_name = appName;
      String package_name = packageName;
      int version_code = 1;
      String version_name = "1.0";
      String str =
          "{Project<=>Name"
              + app_name
              + "<=>PackageName"
              + package_name
              + "<=>VersionCode"
              + version_code
              + "<=>VersionName"
              + version_name
              + "<=>ProjectVersion"
              + 3;
      utils.writeFile(rootdirectory + "Project.pro", str);
      Refresh();
    }
  }

  public void moveFileToDownloads(
      Context context, File sourceFile, String folderName, String newFileName, String type) {
    if (!sourceFile.exists()) {
      System.out.println("Source file does not exist.");
      return;
    }
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // برای اندروید 10 و بالاتر
        ContentResolver resolver = context.getContentResolver();
        Uri downloadsUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, newFileName); // نام فایل جدید
        contentValues.put(
            MediaStore.MediaColumns.MIME_TYPE, getMimeType(sourceFile.getName())); // نوع فایل
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_DOWNLOADS + "/" + folderName);
        Uri newFileUri = resolver.insert(downloadsUri, contentValues);
        if (newFileUri != null) {
          try (InputStream inputStream = new FileInputStream(sourceFile);
              OutputStream outputStream = resolver.openOutputStream(newFileUri)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
              outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            System.out.println("File successfully moved to Downloads/" + folderName);
          }
          if (sourceFile.delete()) {
            System.out.println("Source file deleted successfully.");
          } else {
            System.err.println("Failed to delete source file.");
          }
        } else {
          System.err.println("Failed to create file in Downloads/" + folderName);
        }
        String realPath = FilesPaths.getPath(this, newFileUri);
        if (type.equals("apk")) {
          new MaterialAlertDialogBuilder(MainActivity.this)
              .setTitle(getString(R.string.main_app_built))
              .setMessage(
                  getString(R.string.a_app)
                      + ": "
                      + app_name
                      + "\n"
                      + getString(R.string.a_path)
                      + ": "
                      + realPath)
              .setNegativeButton(
                  getString(R.string.a_share),
                  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                      utils.shareFile(MainActivity.this, new File(realPath));
                    }
                  })
              .setPositiveButton(
                  getString(R.string.a_install),
                  new DialogInterface.OnClickListener() {
                    // @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                      utils.installAPK(MainActivity.this, new File(realPath));
                    }
                  })
              .show();
        } else if (type.equals("as")) {
          new MaterialAlertDialogBuilder(MainActivity.this)
              .setTitle(getString(R.string.main_save_successful))
              .setMessage(
                  getString(R.string.main_app_project)
                      + ": "
                      + app_name
                      + "\n"
                      + getString(R.string.a_path)
                      + ": "
                      + realPath)
              .setNegativeButton(
                  getString(R.string.a_ok),
                  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  })
              .show();
        }
      } else {
        // برای اندروید 9 و پایین‌تر
        File downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File targetDir = new File(downloadsDir, folderName);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
          System.err.println("Failed to create target directory.");
          return;
        }
        File targetFile = new File(targetDir, newFileName);
        try (InputStream inputStream = new FileInputStream(sourceFile);
            OutputStream outputStream = new FileOutputStream(targetFile)) {
          byte[] buffer = new byte[1024];
          int length;
          while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
          }
          outputStream.flush();
          System.out.println("File successfully moved to Downloads/" + folderName);
        }
        if (sourceFile.delete()) {
          System.out.println("Source file deleted successfully.");
        } else {
          System.err.println("Failed to delete source file.");
        }
        String realPath = targetFile.getAbsolutePath();
        if (type.equals("apk")) {
          new MaterialAlertDialogBuilder(MainActivity.this)
              .setTitle(getString(R.string.main_app_built))
              .setMessage(
                  getString(R.string.a_app)
                      + ": "
                      + app_name
                      + "\n"
                      + getString(R.string.a_path)
                      + ": "
                      + realPath)
              .setNegativeButton(
                  getString(R.string.a_share),
                  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                      utils.shareFile(MainActivity.this, new File(realPath));
                    }
                  })
              .setPositiveButton(
                  getString(R.string.a_install),
                  new DialogInterface.OnClickListener() {
                    // @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                      utils.installAPK(MainActivity.this, new File(realPath));
                    }
                  })
              .show();
        } else if (type.equals("as")) {
          new MaterialAlertDialogBuilder(MainActivity.this)
              .setTitle(getString(R.string.main_save_successful))
              .setMessage(
                  getString(R.string.main_app_project)
                      + ": "
                      + app_name
                      + "\n"
                      + getString(R.string.a_path)
                      + ": "
                      + realPath)
              .setNegativeButton(
                  getString(R.string.a_ok),
                  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  })
              .show();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error while moving file: " + e.getMessage());
    }
  }

  // متد برای استخراج نوع فایل
  private String getMimeType(String fileName) {
    String mimeType = "application/octet-stream"; // مقدار پیش‌فرض برای فایل‌های ناشناخته
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex > 0) {
      String extension = fileName.substring(dotIndex + 1).toLowerCase();
      mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    return mimeType;
  }

  private void OpenProject(final File file) {
    try {
      InputStream zipInputStream = new FileInputStream(file);
      String str = utils.readTxtFileFromZip(zipInputStream, "Project.pro");
      if (str.contains("{Project<=>")) {
        String[] code = str.replace("}", "").split("<=>");
        String AppName = code[1].replaceFirst("Name", "");
        String PackageName = code[2].replaceFirst("PackageName", "");
        String VersionCode = code[3].replace("VersionCode", "");
        String VersionName = code[4].replace("VersionName", "");
        if (str.contains("ProjectVersion")) {
          String ProjectVersion = code[5].replace("ProjectVersion", "");
        }
        new MaterialAlertDialogBuilder(MainActivity.this)
            .setTitle(getString(R.string.main_project_info))
            .setMessage(
                getString(R.string.main_app_name)
                    + ": "
                    + AppName
                    + "\n"
                    + getString(R.string.main_package_name)
                    + ": "
                    + PackageName
                    + "\n"
                    + getString(R.string.app_editor_version_code)
                    + ": "
                    + VersionCode
                    + "\n"
                    + getString(R.string.app_editor_version_name)
                    + ": "
                    + VersionName)
            .setNegativeButton(
                getString(R.string.a_cancel),
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                    if (file.exists()) {
                      file.delete();
                    }
                  }
                })
            .setNeutralButton(
                getString(R.string.main_open_another_project),
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                    if (file.exists()) {
                      file.delete();
                    }
                    pickProject();
                  }
                })
            .setPositiveButton(
                getString(R.string.a_open_project),
                new DialogInterface.OnClickListener() {
                  // @RequiresApi(api = Build.VERSION_CODES.M)
                  public void onClick(DialogInterface dialog, int which) {
                    final File assets = new File(rootdirectory + "MyApp/assets");
                    final File pro = new File(rootdirectory + "Project.pro");
                    final ProgressDialog progDailog =
                        ProgressDialog.show(
                            MainActivity.this, "", getString(R.string.a_loading), true);
                    final Handler handler =
                        new Handler() {
                          public void handleMessage(Message msg) {
                            if (pro.exists()) {
                              File menuDire = new File(rootdirectory + "MyApp/assets/menu/");
                              if (menuDire.isDirectory() || menuDire.mkdirs()) {}
                              Refresh();
                              /*if(projectVersion>3){
                              addUpdatesPages(picker.getAllPages());
                              addUpdatesLists(picker.getAllLists());
                              addUpdatesImages(picker.getAllImages());
                              addUpdatesAudios(picker.getAllAudios());
                              addUpdatesVideos(picker.getAllVideos());
                              new AlertDialog.Builder(MainActivity.this)
                              .setTitle("ناسازگاری پروژه")
                              .setMessage("این پروژه برنامه اپساز توسط نسخه بروزتری اپساز طراحی شده است، ممکن است درین نسخه با خطاهای موجه شود.")
                              .setPositiveButton("بستن پروژه", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                              utils.deleteDir(new File(rootdirectory+"MyApp"));
                              new File(rootdirectory+"Project.pro").delete();
                              Refresh();
                              dialog.dismiss();
                              }
                              })
                              .setNegativeButton("خیرست", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                              // do nothing
                              dialog.dismiss();
                              }
                              })
                              .setIcon(android.R.drawable.ic_dialog_alert)
                              .show();
                              }*/
                              return;
                            }
                            showErrorDialog(MainActivity.this);
                          }
                        };
                    new Thread() {
                      public void run() {
                        try {
                          if (assets.exists()) {
                            utils.deleteDir(new File(rootdirectory + "MyApp/assets"));
                          }
                          if (pro.exists()) {
                            pro.delete();
                          }
                          utils.extractZipFile(file.getAbsoluteFile().toString(), rootdirectory);
                          if (file.exists()) {
                            file.delete();
                          }
                        } catch (ZipException e) {
                        }
                        handler.sendEmptyMessage(0);
                        progDailog.dismiss();
                      }
                    }.start();
                  }
                })
            .show();
      } else if (str.equals("")) {
        new MaterialAlertDialogBuilder(MainActivity.this)
            .setTitle(getString(R.string.a_error))
            .setMessage(getString(R.string.main_open_project_error))
            .setPositiveButton(getString(R.string.a_ok), null)
            .setCancelable(false)
            .show();
      } else {
        new MaterialAlertDialogBuilder(MainActivity.this)
            .setTitle(getString(R.string.a_error))
            .setMessage(getString(R.string.main_open_project_error))
            .setPositiveButton(getString(R.string.a_ok), null)
            .setCancelable(false)
            .show();
      }
    } catch (Exception e) {
      e.printStackTrace();
      new MaterialAlertDialogBuilder(MainActivity.this)
          .setTitle(getString(R.string.a_error))
          .setMessage(getString(R.string.a_an_error) + ": " + e.getMessage().toString())
          .setPositiveButton(getString(R.string.a_ok), null)
          .setCancelable(false)
          .show();
    }
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    //noinspection SimplifiableIfStatement
    if (id == R.id.action_exit) {
      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void navMenu() {
    // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    // View header = navigationView.getHeaderView(0);
    final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    LinearLayout content = findViewById(R.id.content);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    // drawer.setScrimColor(0x00000000);
    drawer.setDrawerElevation(20);
    toggle.syncState();
    //	getWindow().setStatusBarColor(android.R.color.transparent);
    drawer.addDrawerListener(
        new DrawerLayout.DrawerListener() {
          @Override
          public void onDrawerSlide(View drawerView, float slideOffset) {
            float pos = drawerView.getWidth() * slideOffset;
            content.setTranslationX(pos);
          }

          @Override
          public void onDrawerOpened(View drawerView) {}

          @Override
          public void onDrawerClosed(View drawerView) {}

          @Override
          public void onDrawerStateChanged(int newState) {
            // Toast.makeText(MainActivity.this, "state: " + newState, Toast.LENGTH_SHORT).show();
          }
        });
    findViewById(R.id.nav_settings)
        .setOnClickListener(
            new OnClickListener() {
              public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
              }
            });
    findViewById(R.id.nav_about)
        .setOnClickListener(
            new OnClickListener() {
              public void onClick(View v) {
                showAboutDialog(MainActivity.this);
              }
            });
    findViewById(R.id.nav_faq)
        .setOnClickListener(
            new OnClickListener() {
              public void onClick(View v) {
                showFAQDialog(MainActivity.this);
              }
            });
  }

  public String decodeXML(String inPutPath)
      throws FileNotFoundException, XmlPullParserException, IOException {
    FileInputStream inputStream = new FileInputStream(inPutPath);
    String outPutData = new aXMLDecoder().decode(inputStream).trim();
    return outPutData;
  }

  public void encodeXML(String inputData, String outPutPath)
      throws FileNotFoundException, XmlPullParserException, IOException {
    FileOutputStream fos = new FileOutputStream(outPutPath);
    aXMLEncoder aXMLEncoder = new aXMLEncoder();
    byte[] bs = aXMLEncoder.encodeString(MainActivity.this, inputData);
    fos.write(bs);
  }
}

	/*	public void modifyAndroidManifest(
	 byte[] orgAndroidManifestData,
	 final String app_name,
	 final String package_name,
	 final int version_code,
	 final String version_name)
	 throws IOException {
	 AxmlReader ar = new AxmlReader(orgAndroidManifestData);
	 AxmlWriter aw = new AxmlWriter();
	 ar.accept(
	 new AxmlVisitor(aw) {
	 public NodeVisitor child(String ns, String name) {
	 return new NodeVisitor(super.child(ns, name)) {
	 public void attr(String ns, String name, int resourceId, int type, Object obj) {
	 if (name.equals("package")
	 || name.equals("versionCode")
	 || name.equals("versionName")) {
	 if (name.equals("package")) {
	 super.attr(ns, name, resourceId, type, package_name);
	 }
	 if (name.equals("versionCode")) {
	 super.attr(ns, name, resourceId, type, Integer.valueOf(version_code));
	 }
	 if (name.equals("versionName")) {
	 super.attr(ns, name, resourceId, type, version_name);
	 return;
	 }
	 return;
	 }
	 super.attr(ns, name, resourceId, type, obj);
	 }

	 public NodeVisitor child(String ns, String name) {
	 NodeVisitor nv = super.child(ns, name);
	 return name.equals("application")
	 ? new NodeVisitor(nv) {
	 public void attr(
	 String ns, String name, int resourceId, int type, Object obj) {
	 if (name.equals("label")) {
	 super.attr(
	 ns,
	 name,
	 resourceId,
	 type,
	 ((String) obj).replace("My Application", app_name));
	 } else {
	 super.attr(ns, name, resourceId, type, obj);
	 }
	 }
	 }
	 : new NodeVisitor(nv) {};
	 }
	 };
	 }
	 });
	 byte[] data = aw.toByteArray();
	 // String str=new String(data);
	 File file = new File(rootdirectory + "MyApp/", "AndroidManifest.xml");
	 try {
	 FileOutputStream fileOut = new FileOutputStream(file);
	 fileOut.write(data);
	 fileOut.close();
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 }
	 */

/*private void addUpdatesPages (ArrayList<String> files){
for(int i=0; i < files.size(); i++) {
String str = null;
File file = new File(rootdirectory+">MyApp/assets/"+files.get(i));
try{
FileInputStream fileIn = new FileInputStream("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/"+files.get(i));
InputStream myInput = fileIn;
int size = myInput.available();
byte[] buffer = new byte[size];
myInput.read(buffer);
myInput.close();
str = new String(buffer);
}catch (IOException e)
{}
if(str.contains("{Menu<=>Mood")){
}else{
try {
FileOutputStream fileOut = new FileOutputStream(file);
String Info = "{Menu<=>MoodOff<=>Namemenu"+files.get(i)+"}!#"+str;
fileOut.write(Info.getBytes());
fileOut.close();
if(new File("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/menu/menu"+files.get(i)).exists()){
	}else{
	try{
		String menu = "{ThisMenu<=>BackgroundDefault}!#";
		FileOutputStream fileOutMenu = new FileOutputStream("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/menu/menu"+files.get(i));
		fileOutMenu.write(menu.getBytes());
		fileOutMenu.close();
		//Toast.makeText(this,file.getPath(),Toast.LENGTH_SHORT).show();
	}
	catch (IOException e)
	{}

}
} catch (IOException e8) {
e8.printStackTrace();
}
}

}
}
private void addUpdatesLists (ArrayList<String> files){
for(int i=0; i < files.size(); i++) {
File file = new File(rootdirectory+"MyApp/assets/"+files.get(i));
DatabaseHelper myDb = new DatabaseHelper(this, files.get(i));
myDb.open();
if(myDb.addUpdates()){
if(new File("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/menu/menu"+files.get(i)).exists()){
}else{
try{
	String menu = "{ThisMenu<=>BackgroundDefault}!#";
	FileOutputStream fileOutMenu = new FileOutputStream("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/menu/menu"+files.get(i));
	fileOutMenu.write(menu.getBytes());
	fileOutMenu.close();
}
catch (IOException e)
{}

}
myDb.addUpdates();
//Toast.makeText(this, file.getPath(), Toast.LENGTH_SHORT).show();
}
myDb.close();
}
}
private void addUpdatesImages (ArrayList<String> files){
for(int i=0; i < files.size(); i++) {
String str = files.get(i);
try{
FileInputStream inputFile = new FileInputStream("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/"+imagesPath+"images");
InputStream input = inputFile;
int s = input.available();
byte[] b = new byte[s];
input.read(b);
input.close();
String data = new String(b);
String[] datas = data.split("\\!\\#");
for (String p1 : datas) {
String p=p1;
if (p.startsWith("{Image<=>"+str.replace("image",""))) {
	String[] code = p.replace("}", "").split("<=>");
	String id = code[1];
	String name = code[2].replace("Title","");
	String file = code[3].replace("Img","");
	if(p.contains("<=>SaveAble")){

		}else{
		String Info = data.replace("{Image<=>"+id+"<=>Title"+name+"<=>Img"+file+"}!#","{Image<=>"+id+"<=>Title"+name+"<=>Img"+file+"<=>SaveAble"+"False"+"<=>ShareAble"+"False"+"}!#");
		try
		{
			FileOutputStream fileOut = new FileOutputStream("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/"+imagesPath+"images");
			fileOut.write(Info.getBytes());
			fileOut.close();
			//Toast.makeText(MainActivity.this,name,Toast.LENGTH_LONG).show();
			} catch (Exception e) {
			e.printStackTrace();

		}
	}

}}
}
catch (IOException e)
{}}
}
private void addUpdatesAudios (ArrayList<String> files){
for(int i=0; i < files.size(); i++) {
String str = files.get(i);
try{
FileInputStream inputFile = new FileInputStream("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/"+audiosPath+"audios");
InputStream input = inputFile;
int s = input.available();
byte[] b = new byte[s];
input.read(b);
input.close();
String data = new String(b);
String[] datas = data.split("\\!\\#");
for (String p1 : datas) {
String p=p1;
if (p.startsWith("{Audio<=>"+str.replace("audio",""))) {
	String[] code = p.replace("}", "").split("<=>");
	String id = code[1];
	String name = code[2].replace("Title","");
	String file = code[3].replace("Audio","");
	if(p.contains("<=>SaveAble")){

		}else{
		String Info = data.replace("{Audio<=>"+id+"<=>Title"+name+"<=>Audio"+file+"}!#","{Audio<=>"+id+"<=>Title"+name+"<=>Audio"+file+"<=>SaveAble"+"False"+"<=>ShareAble"+"False"+"}!#");
		try
		{
			FileOutputStream fileOut = new FileOutputStream("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/"+audiosPath+"audios");
			fileOut.write(Info.getBytes());
			fileOut.close();
			//Toast.makeText(MainActivity.this,name,Toast.LENGTH_LONG).show();
			} catch (Exception e) {
			e.printStackTrace();

		}
	}

}}
}
catch (IOException e)
{}}
}
private void addUpdatesVideos (ArrayList<String> files){
for(int i=0; i < files.size(); i++) {
String str = files.get(i);
try{
FileInputStream inputFile = new FileInputStream("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/"+videosPath+"videos");
InputStream input = inputFile;
int s = input.available();
byte[] b = new byte[s];
input.read(b);
input.close();
String data = new String(b);
String[] datas = data.split("\\!\\#");
for (String p1 : datas) {
String p=p1;
if (p.startsWith("{Video<=>"+str.replace("video",""))) {
	String[] code = p.replace("}", "").split("<=>");
	String id = code[1];
	String name = code[2].replace("Title","");
	String file = code[3].replace("Video","");
	if(p.contains("<=>SaveAble")){

		}else{
		String Info = data.replace("{Video<=>"+id+"<=>Title"+name+"<=>Video"+file+"}!#","{Video<=>"+id+"<=>Title"+name+"<=>Video"+file+"<=>SaveAble"+"False"+"<=>ShareAble"+"False"+"}!#");
		try
		{
			FileOutputStream fileOut = new FileOutputStream("/data/data/af.alireza.appsaz/APPSAZ/MyApp/assets/"+videosPath+"videos");
			fileOut.write(Info.getBytes());
			fileOut.close();
			//Toast.makeText(MainActivity.this,name,Toast.LENGTH_LONG).show();
			} catch (Exception e) {
			e.printStackTrace();

		}
	}

}}
}
catch (IOException e)
{}}
}*/
