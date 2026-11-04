package af.alireza.appsaz.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IconGenerator {

    public static void generateAdaptiveIcons(
        Context context, Bitmap inputBitmap, int backgroundColor, float paddingPercent) {
    // تنظیم ابعاد برای پوشه‌های مختلف
    int[] sizes = {48, 162, 216, 324, 432};
    String[] folders = {
        "mipmap-mdpi", "mipmap-hdpi", "mipmap-xhdpi", "mipmap-xxhdpi", "mipmap-xxxhdpi"
    };

    // ایجاد و ذخیره آیکون پیش‌زمینه (foreground) با فاصله تنظیم‌شده
    for (int i = 0; i < sizes.length; i++) {
        int size = sizes[i];
        Bitmap scaledForeground = createCenterCropBitmap(inputBitmap, size, size, paddingPercent);
        saveIconToProjectFolders(
                context, scaledForeground, folders[i], "ic_launcher_foreground.png");
    }

    // ایجاد و ذخیره آیکون پس‌زمینه (background) از رنگ ورودی
    for (int i = 0; i < sizes.length; i++) {
        int size = sizes[i];
        Bitmap backgroundBitmap = createSolidColorBitmap(size, size, backgroundColor);
        saveIconToProjectFolders(
                context, backgroundBitmap, folders[i], "ic_launcher_background.png");
    }

    // ذخیره فایل XML برای آیکون تطبیقی
    String xmlContent =
            "<adaptive-icon xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
                    + "    <background android:drawable=\"@mipmap/ic_launcher_background\"/>\n"
                    + "    <foreground android:drawable=\"@mipmap/ic_launcher_foreground\"/>\n"
                    + "</adaptive-icon>";

    // ذخیره XML در پوشه mipmap-anydpi-v26
    File appDirectory = context.getExternalFilesDir("Project/MyApp/res/");
    File resDirectory = new File(appDirectory, "mipmap-anydpi-v26");
    if (!resDirectory.exists() && !resDirectory.mkdirs()) {
        throw new RuntimeException(
                "Failed to create directory: " + resDirectory.getAbsolutePath());
    }
    File xmlFile = new File(resDirectory, "ic_launcher.xml");
    try (FileOutputStream outputStream = new FileOutputStream(xmlFile)) {
        outputStream.write(xmlContent.getBytes());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    /**
     * ایجاد Bitmap با برش مرکزی و حفظ نسبت ابعاد.
     *
     * @param inputBitmap تصویر ورودی.
     * @param outputWidth عرض تصویر خروجی.
     * @param outputHeight ارتفاع تصویر خروجی.
     * @return Bitmap تغییر اندازه داده شده.
     */
    private static Bitmap createCenterCropBitmap(
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
    /**
     * ایجاد Bitmap با رنگ ثابت.
     *
     * @param width عرض Bitmap.
     * @param height ارتفاع Bitmap.
     * @param color رنگ ورودی.
     * @return Bitmap ایجاد شده.
     */
    private static Bitmap createSolidColorBitmap(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawRect(0, 0, width, height, paint);
        return bitmap;
    }
    
    public static Bitmap generateRegularIcon(
        Bitmap inputBitmap, int backgroundColor, float cornerRadiusPercent,
        float paddingPercent, int outputSize) {
    // ایجاد Bitmap خروجی
    Bitmap outputBitmap = Bitmap.createBitmap(outputSize, outputSize, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(outputBitmap);

    // رنگ پس‌زمینه
    Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    bgPaint.setColor(backgroundColor);
    bgPaint.setStyle(Paint.Style.FILL);

    // رسم پس‌زمینه با گوشه‌های گرد
    float cornerRadius = outputSize * (cornerRadiusPercent / 100);
    RectF bgRect = new RectF(0, 0, outputSize, outputSize);
    canvas.drawRoundRect(bgRect, cornerRadius, cornerRadius, bgPaint);

    // تنظیم مقیاس پیش‌زمینه بر اساس paddingPercent
    float scaleFactor;
    if (paddingPercent > 50) {
        // Zoom In
        scaleFactor = 1f + ((paddingPercent - 50) / 50f);
    } else {
        // Zoom Out
        scaleFactor = paddingPercent / 50f;
    }

    // محاسبه اندازه جدید پیش‌زمینه
    int scaledWidth = (int) (outputSize * scaleFactor);
    int scaledHeight = (int) (outputSize * scaleFactor);

    // تغییر اندازه آیکون پیش‌زمینه
    Bitmap scaledBitmap = Bitmap.createScaledBitmap(inputBitmap, scaledWidth, scaledHeight, true);

    // ایجاد Bitmap برای پیش‌زمینه با گوشه‌های گرد
    Bitmap foregroundBitmap = Bitmap.createBitmap(outputSize, outputSize, Bitmap.Config.ARGB_8888);
    Canvas foregroundCanvas = new Canvas(foregroundBitmap);

    // رسم پس‌زمینه گوشه‌دار
    Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    maskPaint.setColor(backgroundColor); // اینجا رنگ پس‌زمینه باید اعمال شود
    RectF fgRect = new RectF(0, 0, outputSize, outputSize);
    foregroundCanvas.drawRoundRect(fgRect, cornerRadius, cornerRadius, maskPaint);

    // تنظیم حالت ترکیب برای ماسک پیش‌زمینه
    maskPaint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));

    // محاسبه مختصات برای مرکز کردن پیش‌زمینه
    int left = (outputSize - scaledWidth) / 2;
    int top = (outputSize - scaledHeight) / 2;

    // رسم پیش‌زمینه با گوشه‌های گرد روی پس‌زمینه
    Rect srcRect = new Rect(0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());
    Rect destRect = new Rect(left, top, left + scaledWidth, top + scaledHeight);
    foregroundCanvas.drawBitmap(scaledBitmap, srcRect, destRect, maskPaint);

    // رسم پیش‌زمینه با گوشه‌های گرد روی پس‌زمینه
    canvas.drawBitmap(foregroundBitmap, 0, 0, null);

    return outputBitmap;
}

    public static void saveRegularIcons(
            Context context, Bitmap inputBitmap, int backgroundColor, float cornerRadiusPercent, float paddingPrecent) {
        // ابعاد آیکون‌ها برای هر پوشه
        int[] sizes = {48, 72, 96, 144, 192}; // mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi
        String[] folders = {
            "mipmap-mdpi", "mipmap-hdpi", "mipmap-xhdpi", "mipmap-xxhdpi", "mipmap-xxxhdpi"
        };

        for (int i = 0; i < sizes.length; i++) {
            Bitmap highQualityIcon = generateRegularIcon(
                            inputBitmap, backgroundColor, cornerRadiusPercent, paddingPrecent, sizes[i]);
            saveIconToProjectFolders(context, highQualityIcon, folders[i], "ic_launcher.png");
        }
    }
    public static void saveIconToProjectFolders(Context context, Bitmap bitmap, String folderName, String fileName) {
        // مسیر پوشه پروژه
        File appDirectory = context.getExternalFilesDir("Project/MyApp/res/");
        File iconsDirectory = new File(appDirectory, folderName);
        // ایجاد پوشه در صورت نیاز
        if (!iconsDirectory.exists() && !iconsDirectory.mkdirs()) {
            throw new RuntimeException(
                    "Failed to create directory: " + iconsDirectory.getAbsolutePath());
        }
        File outputFile = new File(iconsDirectory, fileName);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
