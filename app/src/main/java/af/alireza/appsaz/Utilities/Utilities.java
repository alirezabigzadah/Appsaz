package af.alireza.appsaz.Utilities;

import af.alireza.appsaz.FilesPaths;
import af.alireza.appsaz.R;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FilenameUtils;

public class Utilities {

    public int pageCount = 19;
    public int listCount = 9;
    public int fontCount = 9;
    private static final String SELECTED_LANGUAGE_ID = "SelectedLanguageID";
    private static int PERMISSION_REQUEST_CODE = 1402;

    private static final String[] RESERVED_PREFIXES = {
        "com.google",
        "com.android",
        "com.amazon",
        "com.samsung",
        "com.microsoft",
        "com.facebook",
        "com.apple",
        "com.netflix",
        "com.spotify",
        "com.whatsapp",
        "com.instagram",
        "com.twitter",
        "com.snapchat",
    };

    // بررسی اینکه آیا نام بسته رزرو شده است یا نه
    public static boolean isPackageNameReserved(String packageName) {
        for (String prefix : RESERVED_PREFIXES) {
            if (packageName.startsWith(prefix)) {
                return true; // رزرو شده است
            }
        }
        return false; // مشکلی ندارد
    }

    public boolean checkPer(Activity c) {
        if (checkPermission(c) == false) {
            new MaterialAlertDialogBuilder(c)
                    .setTitle(c.getString(R.string.a_permission_title))
                    .setMessage(c.getString(R.string.a_permission_text))
                    .setPositiveButton(
                            c.getString(R.string.a_permission_grant),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(
                                            c,
                                            new String[] {
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                            },
                                            14);
                                }
                            })
                    .setNegativeButton(
                            c.getString(R.string.a_cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPermission(Activity c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public Context applySelectedLanguage(Context context) {
        String languageId = Prefs.getString(context, SELECTED_LANGUAGE_ID, "default");
        Locale locale;

        if (languageId != null && !languageId.isEmpty()) {
            switch (languageId) {
                case "ru":
                    locale = new Locale("ru"); // روسی
                    break;
                case "zh":
                    locale = new Locale("zh"); // چینی
                    break;
                case "hi":
                    locale = new Locale("hi"); // هندی
                    break;
                case "ar":
                    locale = new Locale("ar"); // عربی
                    break;
                case "fa":
                    locale = new Locale("fa"); // فارسی
                    break;
                case "dv":
                    locale = new Locale("dv"); // دری
                    break;
                case "ps":
                    locale = new Locale("ps"); // پشتو
                    break;
                case "en":
                    locale = Locale.ENGLISH; // انگلیسی
                    break;
                default:
                    locale = Locale.getDefault(); // شناسایی خودکار زبان گوشی
                    break;
            }
        } else {
            locale = Locale.getDefault(); // شناسایی خودکار زبان گوشی
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        return context.createConfigurationContext(config);
    }

    public int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

   public static String getCurrentDateTime() {
        // دریافت تاریخ و زمان فعلی
        LocalDateTime now = LocalDateTime.now();
        // فرمت موردنظر برای نمایش
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        // بازگشت تاریخ و زمان فرمت شده
        return now.format(formatter);
    }
    
    public boolean isConnected(Context a) {
        ConnectivityManager connectivityManager =
                (android.net.ConnectivityManager) a.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void hideKeyboard(Context c) {
        InputMethodManager imm =
                (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showKeyboard(Context c) {
        InputMethodManager imm =
                (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void showMessage(Context _context, String _s) {
        Toast.makeText(_context, _s, Toast.LENGTH_SHORT).show();
    }

    public static String getSizeLabel(File file) {
        String fileSizeStr = "Unknown Size";
        try {
            if (file.exists()) {
                long fileSize = file.length(); // اندازه فایل به بایت

                // تبدیل اندازه فایل به واحدهای مختلف
                double fileSizeInKB = fileSize / 1024.0; // به کیلوبایت
                double fileSizeInMB = fileSizeInKB / 1024.0; // به مگابایت
                double fileSizeInGB = fileSizeInMB / 1024.0; // به گیگابایت

                // فرمت کردن و نمایش نتیجه
                if (fileSizeInGB >= 1) {
                    fileSizeStr = String.format("%.2f GB", fileSizeInGB); // نمایش به گیگابایت
                } else if (fileSizeInMB >= 1) {
                    fileSizeStr = String.format("%.2f MB", fileSizeInMB); // نمایش به مگابایت
                } else if (fileSizeInKB >= 1) {
                    fileSizeStr = String.format("%.2f KB", fileSizeInKB); // نمایش به کیلوبایت
                } else {
                    fileSizeStr = String.format("%d Bytes", fileSize); // نمایش به بایت
                }
            } else {
                fileSizeStr = "File does not exist.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileSizeStr = "Error calculating file size.";
        }
        return fileSizeStr;
    }

    public int getFileCount(String fileType, String folderPath) {
        File directory = new File(folderPath); // مسیر پوشه مورد نظر خود را تعیین کنید
        File[] files = directory.listFiles();
        int count = 0;

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(fileType)) {
                    count++;
                }
            }
        }

        // System.out.println("تعداد فایل‌ها با نام 'page': " + count);
        return count-1;
    }

    public int getListCount(String fileType, String folderPath) {
        File directory = new File(folderPath); // مسیر پوشه مورد نظر خود را تعیین کنید
        File[] files = directory.listFiles();
        int count = 0;

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(fileType)) {
                    count++;
                }
                if (file.isFile() && file.getName().endsWith("-journal")) {
                    count--;
                }
            }
        }
        // System.out.println("تعداد فایل‌ها با نام 'page': " + count);
        return count-1;
    }

    public boolean isExistFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    public boolean makeDir(String path) {
        if (!isExistFile(path)) {
            File file = new File(path);
            file.mkdirs();
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static void extractZipFile(String zipFilePath, String outputDirPath)
            throws ZipException {
        // Create a ZipFile object
        ZipFile zipFile = new ZipFile(zipFilePath);
        // Extract files to the output directory
        zipFile.extractAll(outputDirPath);
    }

    public static void compressFolderAndFile(
            String folderPath, String filePath, String outputZipPath) throws ZipException {
        // Check if the output ZIP file exists and delete it
        File outputZipFile = new File(outputZipPath);
        if (outputZipFile.exists()) {
            boolean deleted = outputZipFile.delete();
            if (!deleted) {
                System.err.println("Failed to delete existing ZIP file: " + outputZipPath);
                return;
            }
        }
        // Create a ZipFile object
        ZipFile zipFile = new ZipFile(outputZipPath);
        // Add the folder to the ZIP
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            zipFile.addFolder(folder);
        } else {
            System.err.println("Invalid folder path: " + folderPath);
        }
        // Add the file to the ZIP
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            zipFile.addFile(file);
        } else {
            System.err.println("Invalid file path: " + filePath);
        }
    }

    public String readFile(String data) {
        String str = null;
        try {
            FileInputStream fileIn = new FileInputStream(data);
            InputStream myInput = fileIn;
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myInput.close();
            final String Content = new String(buffer);
            str = Content;
        } catch (IOException e) {
        }

        return str;
    }

    public void writeFile(String data, String Info) {
        try {
            FileOutputStream fileOut = new FileOutputStream(data);
            fileOut.write(Info.getBytes());
            fileOut.close();
        } catch (IOException e) {
        }
    }

    public static File copyFileToCustomDirectory(
    Context context, Uri sourceUri, String targetPath, String fileName) {
    ContentResolver resolver = context.getContentResolver();
    File customDir = new File(targetPath);
    if (!customDir.exists()) {
      if (!customDir.mkdirs()) {
        return null;
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

    public String copyFromInputStream(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int i;
        try {
            while ((i = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, i);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }

    public void move(File sourceFile, File destFile) {
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            assert files != null;
            for (File file : files) move(file, new File(destFile, file.getName()));
            if (!sourceFile.delete()) throw new RuntimeException();
        } else {
            if (!destFile.getParentFile().exists())
                if (!destFile.getParentFile().mkdirs()) throw new RuntimeException();
            if (!sourceFile.renameTo(destFile)) throw new RuntimeException();
        }
    }

    public void copyFromAssets(Context c, String inName, String outName) throws IOException {
        OutputStream myOutput = new FileOutputStream(outName);
        byte[] buffer = new byte[1024];
        InputStream myInput = c.getAssets().open(inName);
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

    public void copyFile(String inName, String outName) throws IOException {
        OutputStream myOutput = new FileOutputStream(outName);
        byte[] buffer = new byte[1024];
        InputStream myInput = new FileInputStream(inName);
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

    public String readTxtFileFromZip(InputStream zipInputStream, String fileNameToRead)
            throws IOException {
        String str = "";
        ZipInputStream zis = new ZipInputStream(zipInputStream);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            // Check if the current entry is the file we want to read
            if (entry.getName().equals(fileNameToRead)) {
                // Read the content of the file
                BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                // Close the reader
                reader.close();
                // Get the content as a String
                str = stringBuilder.toString();
                System.out.println(str); // or handle it as needed
                break; // Exit the loop once we have read the desired file
            }
        }
        return str;
    }

    public void installAPK(Context c, File apkFile) {
        if (Build.VERSION.SDK_INT >= 24) {
            Uri uri = FileProvider.getUriForFile(c, c.getPackageName() + ".provider", apkFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            c.startActivity(intent);
        } else {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            c.startActivity(intent);
        }
    }

    public void shareFile(Context context, File file) {
        if (file == null || !file.exists()) {
            Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Uri fileUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // اندروید 7 به بالا: استفاده از FileProvider
                fileUri =
                        FileProvider.getUriForFile(
                                context, context.getPackageName() + ".provider", file);
            } else {
                // اندروید 6 و پایین‌تر: استفاده از Uri مستقیم
                fileUri = Uri.fromFile(file);
            }

            String mimeType = context.getContentResolver().getType(fileUri);
            if (mimeType == null) {
                mimeType = "*/*"; // پیش‌فرض اگر MIME تشخیص داده نشود
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType(mimeType);
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(Intent.createChooser(shareIntent, "Share File"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error sharing file: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        finalTimerString = finalTimerString + minutes + ":" + secondsString;
        return finalTimerString;
    }

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;
        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
        percentage = (((double) currentSeconds) / totalSeconds) * 100;
        return percentage.intValue();
    }

    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);
        return currentDuration * 1000;
    }

    public static String getFileExtensionWithApache(String filePath) {
        return "." + FilenameUtils.getExtension(filePath).toLowerCase();
    }

    public String saveMediaFile(
            Context context, String assetFileName, String outFileName, String mediaType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // اندروید 10 به بالا: استفاده از MediaStore API
            return saveFileUsingMediaStore(context, assetFileName, mediaType, outFileName);
        } else {
            // اندروید 9 و پایین‌تر: ذخیره مستقیم در مسیرهای عمومی
            return saveFileLegacy(context, assetFileName, mediaType, outFileName);
        }
    }

    // **ذخیره‌سازی فایل در اندروید 10 به بالا با MediaStore API**
    public String saveFileUsingMediaStore(
            Context context, String filePath, String mediaType, String customFileName) {
        Uri mediaUri = null;

        // انتخاب نوع رسانه
        if ("image".equalsIgnoreCase(mediaType)) {
            mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equalsIgnoreCase(mediaType)) {
            mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equalsIgnoreCase(mediaType)) {
            mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        if (mediaUri == null) {
            Toast.makeText(context, "Unsupported media type", Toast.LENGTH_SHORT).show();
            return null;
        }

        // بررسی و تنظیم نوع MIME
        String mimeType = getMimeTypeByExtension(filePath);
        if (mimeType == null) {
            Toast.makeText(context, "Unsupported file type", Toast.LENGTH_SHORT).show();
            return null;
        }

        // مقداردهی ContentValues
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, customFileName); // نام فایل دلخواه
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType); // نوع MIME
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, getMediaFolder(mediaType)); // پوشه مقصد

        Uri fileUri = context.getContentResolver().insert(mediaUri, values);
        if (fileUri != null) {
            try (InputStream inputStream = new FileInputStream(filePath);
                    OutputStream outputStream =
                            context.getContentResolver().openOutputStream(fileUri)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                Toast.makeText(
                                context,
                                "File saved to: " + FilesPaths.getPath(context, fileUri),
                                Toast.LENGTH_SHORT)
                        .show();
                return FilesPaths.getPath(context, fileUri); // بازگرداندن URI فایل
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(
                                context,
                                "Failed to save file: " + e.getMessage(),
                                Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(context, "Failed to create file", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    // **ذخیره‌سازی مستقیم برای اندروید 9 و پایین‌تر**
    private String saveFileLegacy(
            Context context, String filePath, String mediaType, String customFileName) {
        File destinationDir;

        // انتخاب پوشه مقصد بر اساس نوع رسانه
        switch (mediaType.toLowerCase()) {
            case "image":
                destinationDir =
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES);
                break;
            case "video":
                destinationDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                break;
            case "audio":
                destinationDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                break;
            default:
                destinationDir =
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS);
        }

        File outputFile =
                new File(
                        destinationDir,
                        customFileName
                                + "."
                                + getFileExtension(filePath)); // استفاده از نام فایل دلخواه

        try (InputStream inputStream = new FileInputStream(filePath);
                OutputStream outputStream = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            Toast.makeText(
                            context,
                            "File saved to: " + outputFile.getAbsolutePath(),
                            Toast.LENGTH_SHORT)
                    .show();
            return outputFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save file: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
        return null;
    }

    public static String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            int lastDotIndex = fileName.lastIndexOf('.');
            if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
                return fileName.substring(lastDotIndex + 1)
                        .toLowerCase(); // پسوند را به حروف کوچک تبدیل می‌کند
            }
        }
        return ""; // در صورتی که پسوند موجود نباشد
    }

    // **برگرداندن نوع MIME فایل**
    private String getMimeTypeByExtension(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".gif")) {
            return "image/gif";
        } else if (filePath.endsWith(".bmp")) {
            return "image/bmp";
        } else if (filePath.endsWith(".webp")) {
            return "image/webp";
        } else if (filePath.endsWith(".mp3")) {
            return "audio/mpeg";
        } else if (filePath.endsWith(".wav")) {
            return "audio/wav";
        } else if (filePath.endsWith(".aac")) {
            return "audio/aac";
        } else if (filePath.endsWith(".flac")) {
            return "audio/flac";
        } else if (filePath.endsWith(".mp4")) {
            return "video/mp4";
        } else if (filePath.endsWith(".mkv")) {
            return "video/x-matroska";
        } else if (filePath.endsWith(".avi")) {
            return "video/x-msvideo";
        } else if (filePath.endsWith(".mov")) {
            return "video/quicktime";
        }
        return null; // Unknown type
    }

    // **برگرداندن پوشه مقصد برای انواع رسانه**
    private String getMediaFolder(String mediaType) {
        switch (mediaType.toLowerCase()) {
            case "image":
                return Environment.DIRECTORY_PICTURES;
            case "video":
                return Environment.DIRECTORY_MOVIES;
            case "audio":
                return Environment.DIRECTORY_MUSIC;
            default:
                return Environment.DIRECTORY_DOWNLOADS;
        }
    }

    public static String getPathFromUri(Context context, Uri uri) {
        String filePath = null;

        // بررسی نوع URI
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            // برای محتوای MediaStore یا فایل‌های موجود در دستگاه
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    filePath = context.getExternalFilesDir(null).getAbsolutePath() + "/" + split[1];
                } else {
                    // پشتیبانی از کارت SD
                    File externalRoot = new File("/storage/" + type);
                    filePath = externalRoot.getAbsolutePath() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    filePath = id.replaceFirst("raw:", "");
                } else {
                    Uri contentUri =
                            ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())
                                    ? uri
                                    : ContentUris.withAppendedId(
                                            Uri.parse("content://downloads/public_downloads"),
                                            Long.parseLong(id));
                    filePath = getDataColumn(context, contentUri, null, null);
                }
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};

                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // برای URI‌های فایل مستقیم
            filePath = uri.getPath();
        }

        return filePath;
    }

    // بررسی فایل‌های ذخیره‌سازی خارجی
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    // بررسی اسناد دانلود شده
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    // بررسی اسناد رسانه
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    // دریافت ستون داده از پایگاه‌داده
    private static String getDataColumn(
            Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor =
                    context.getContentResolver()
                            .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
