# Default ProGuard rules for Android
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Preserve the special static methods that are required in all enumeration classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# Jetpack Compose rules
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

# Keep custom Compose components
-keep class com.example.typecraft.ui.components.** { *; }
-keep class com.example.typecraft.model.** { *; }

# Keep R8 rules
-keepattributes InnerClasses

# Preserve the special static methods that are required in all enumeration classes
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}