# Preserve the line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable

-keep class kotlinx.** { *; }
-keep class kotlin.reflect.** { *; }
-keep class kotlin.jvm.internal.** { *; }

-keep class androidx.arch.core.** { *; }

# Room
-keep class androidx.room.**{
    public protected private *;
}