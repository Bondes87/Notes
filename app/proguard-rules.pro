# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
############################################# Android ##############################################
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-dontwarn android.support.**

####################################### Application config #########################################
-dontobfuscate
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable

-keep class com.dbondarenko.shpp.notes.** { *; }
-keep interface com.dbondarenko.shpp.notes.** { *; }
-keep enum com.dbondarenko.shpp.notes.** { *; }
-keepclassmembers class com.dbondarenko.shpp.notes.** { *; }
-keepclassmembers interface com.dbondarenko.shpp.notes.** { *; }
-keepclassmembers enum com.dbondarenko.shpp.notes.** { *; }
-keepnames class com.dbondarenko.shpp.notes.** { *; }
-keepnames interface com.dbondarenko.shpp.notes.** { *; }
-keepnames enum com.dbondarenko.shpp.notes.** { *; }
-keepclassmembernames class com.dbondarenko.shpp.notes.** { *; }
-keepclassmembernames interface com.dbondarenko.shpp.notes.** { *; }
-keepclassmembernames enum com.dbondarenko.shpp.notes.** { *; }
-dontwarn com.dbondarenko.shpp.notes.**
# Remove Logging statements
-assumenosideeffects class android.util.Log { public static *** d(...); }

########################################## GSON ####################################################
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

########################################## JAVA ####################################################
-keep class com.sun.** { *; }
-keep class javax.** { *; }
-keep interface com.sun.** { *; }
-keep interface javax.** { *; }
-dontwarn com.sun.**
-dontwarn javax.**

######################################### GOOGLE ###################################################
-keep class com.google.** { *; }
-keep interface com.google.** { *; }
-dontwarn com.google.**
-keepattributes Signature
-keepattributes Annotation

######################################### SQUARE ###################################################
-keep class com.squareup.** { *; }
-keep class retrofit2.** { *; }
-keep class okio.** { *; }
-keep class rx.** { *; }
-keep class okhttp3.** { *; }

-keep interface com.squareup.** { *; }
-keep interface retrofit2.** { *; }
-keep interface okio.** { *; }
-keep interface rx.** { *; }
-keep interface okhttp3.** { *; }

-dontwarn com.squareup.**
-dontwarn okio.**
-dontwarn rx.**
-dontwarn okhttp3.**

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions