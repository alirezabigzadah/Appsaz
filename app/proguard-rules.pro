# ProGuard / R8 Rules for Appsaz

# Preserve reflection and attributes
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,Exceptions,SourceFile,LineNumberTable

# -----------------------------------------------------------------------------
# Keep all classes, methods, and fields from local JAR libraries in 'libs/'
# -----------------------------------------------------------------------------

# Commons IO
-keep class org.apache.commons.io.** { *; }
-keep interface org.apache.commons.io.** { *; }

# APK Signer / APK Sig
-keep class com.android.apksigner.** { *; }
-keep interface com.android.apksigner.** { *; }
-keep class com.android.apksig.** { *; }
-keep interface com.android.apksig.** { *; }

# AXML & Binary Editor
-keep class pxb.android.** { *; }
-keep interface pxb.android.** { *; }
-keep class brut.** { *; }
-keep interface brut.** { *; }

# Suppress warnings from third-party libraries
-dontwarn org.apache.commons.io.**
-dontwarn com.android.apksigner.**
-dontwarn com.android.apksig.**
-dontwarn pxb.android.**
-dontwarn brut.**
