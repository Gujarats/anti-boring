# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/gujarat/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-dontwarn com.squareup.okhttp.**

#
# Twitter library - start
#

-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

#
# Twitter library - end
#

# adcolony start
-dontwarn com.immersion.**
-dontnote com.immersion.**
-dontwarn android.webkit.**
#adcolony end
